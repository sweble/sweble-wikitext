/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sweble.wikitext.articlecruncher;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.log4j.Logger;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.ConsoleWriter;
import org.sweble.wikitext.articlecruncher.utils.ConsoleWriterBase;
import org.sweble.wikitext.articlecruncher.utils.ConsoleWriterDummy;
import org.sweble.wikitext.articlecruncher.utils.ExecutorType;
import org.sweble.wikitext.articlecruncher.utils.MyExecutorService;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;
import org.sweble.wikitext.articlecruncher.utils.WorkerSynchronizer;

public class Nexus
{
	public static enum NexusState
	{
		INITIALIZED,
		RUNNING,
		SHUTDOWN
	}
	
	// =========================================================================
	
	/*
	private static Nexus nexus;
	
	public static Nexus get()
	{
		if (nexus == null)
			nexus = new Nexus();
		return nexus;
	}
	
	public static ConsoleWriterBase getConsoleWriter()
	{
		return get().consoleWriter;
	}
	*/
	
	// =========================================================================
	
	private static final Logger logger =
			Logger.getLogger(Nexus.class.getName());
	
	private static final int COMPLETION_TIMEOUT_IN_SECONDS = 60 * 5;
	
	// =========================================================================
	
	private BlockingQueue<JobWithHistory> inTray;
	
	private BlockingQueue<JobWithHistory> processedJobs;
	
	private BlockingQueue<ProcessedJob> outTray;
	
	private JobTraceSet jobTraces = new JobTraceSet();
	
	private MyExecutorService executor;
	
	private Throwable emergencyCause;
	
	//private WorkerBase parser;
	
	private WorkerBase gatherer;
	
	//private WorkerBase storer;
	
	private ConsoleWriterBase consoleWriter;
	
	private NexusState state;
	
	private AbortHandler abortHandler;
	
	private WorkerSynchronizer synchronizer = new WorkerSynchronizer();
	
	private List<WorkerBase> jobGenerators = new ArrayList<WorkerBase>();
	
	private List<WorkerBase> processingNodes = new ArrayList<WorkerBase>();
	
	private List<WorkerBase> storers = new ArrayList<WorkerBase>();
	
	// =========================================================================
	
	public Nexus()
	{
	}
	
	// =========================================================================
	
	public void setUp(
			int inTrayCapacity,
			int processedJobsCapacity,
			int outTrayCapacity) throws Throwable
	{
		setUp(inTrayCapacity, processedJobsCapacity, outTrayCapacity, true);
	}
	
	public void setUp(
			int inTrayCapacity,
			int processedJobsCapacity,
			int outTrayCapacity,
			boolean withConsoleWriter) throws Throwable
	{
		synchronized (synchronizer.getMonitor())
		{
			if (state != null)
				throw new IllegalStateException("Can only set-up Nexus once");
			
			try
			{
				logger.info("Nexus starting");
				
				inTray = new LinkedBlockingDeque<JobWithHistory>(inTrayCapacity);
				
				processedJobs = new LinkedBlockingDeque<JobWithHistory>(processedJobsCapacity);
				
				outTray = new LinkedBlockingDeque<ProcessedJob>(outTrayCapacity);
				
				executor = new MyExecutorService(ExecutorType.CACHED_THREAD_POOL, logger);
				
				abortHandler = new AbortHandler()
				{
					@Override
					public void notify(Throwable t)
					{
						emergencyShutdown(t);
					}
				};
				
				if (withConsoleWriter)
				{
					consoleWriter = new ConsoleWriter(
							abortHandler,
							inTrayCapacity,
							processedJobsCapacity,
							outTrayCapacity);
				}
				else
				{
					consoleWriter = new ConsoleWriterDummy(abortHandler);
				}
				
				gatherer = new Gatherer(abortHandler, inTray, processedJobs, outTray);
				
				state = NexusState.INITIALIZED;
			}
			catch (Throwable t)
			{
				logger.error("Nexus hit by exception", t);
				setEmergencyCause(t);
				nexusStopped();
			}
		}
	}
	
	public void start() throws Throwable
	{
		try
		{
			synchronized (synchronizer.getMonitor())
			{
				if (state != NexusState.INITIALIZED)
					throw new IllegalStateException("Can only start Nexus after initialization");
				
				state = NexusState.RUNNING;
				
				try
				{
					logger.info("Nexus starting workers");
					
					consoleWriter.start(executor);
					gatherer.start(executor);
					
					logger.info("Nexus waiting for end of input stream");
					synchronizer.waitForAll(1);
				}
				catch (InterruptedException e)
				{
					logger.fatal("Nexus interrupted unexpectedly", e);
					setEmergencyCause(e);
				}
				catch (Throwable t)
				{
					logger.error("Nexus hit by exception", t);
					setEmergencyCause(t);
				}
			}
			
			if (emergencyCause == null)
			{
				logger.info("Nexus waiting for processing to finish");
				jobTraces.waitForCompletion(COMPLETION_TIMEOUT_IN_SECONDS);
			}
		}
		finally
		{
			MyExecutorService exec = null;
			synchronized (synchronizer.getMonitor())
			{
				logger.info("Nexus shutting down");
				exec = stopAll();
			}
			
			if (exec != null)
				exec.shutdownAndAwaitTermination();
			nexusStopped();
		}
	}
	
	public void addJobGenerator(JobGeneratorFactory factory)
	{
		synchronized (synchronizer.getMonitor())
		{
			switch (state)
			{
				case INITIALIZED:
				case RUNNING:
				{
					logger.info("Adding job generator");
					
					WorkerBase jg = factory.create(
							abortHandler,
							inTray,
							jobTraces);
					
					jobGenerators.add(jg);
					
					jg.start(executor, synchronizer);
					
					break;
				}
				default:
					throw new IllegalStateException("Can only add job generators to initialized/running Nexus");
			}
		}
	}
	
	public void addProcessingNode(ProcessingNodeFactory factory)
	{
		synchronized (synchronizer.getMonitor())
		{
			switch (state)
			{
				case INITIALIZED:
				case RUNNING:
				{
					logger.info("Adding processing node");
					
					WorkerBase pn = factory.create(
							abortHandler,
							inTray,
							processedJobs);
					
					processingNodes.add(pn);
					
					pn.start(executor);
					
					break;
				}
				default:
					throw new IllegalStateException("Can only add processing nodes to initialized/running Nexus");
			}
		}
	}
	
	public void addStorer(StorerFactory factory)
	{
		synchronized (synchronizer.getMonitor())
		{
			switch (state)
			{
				case INITIALIZED:
				case RUNNING:
				{
					logger.info("Adding storer");
					
					WorkerBase storer = factory.create(
							abortHandler,
							jobTraces,
							outTray);
					
					storers.add(storer);
					
					storer.start(executor);
					
					break;
				}
				default:
					throw new IllegalStateException("Can only add storers to initialized/running Nexus");
			}
		}
	}
	
	public Set<JobTrace> getJobTraces()
	{
		return jobTraces.getTraces();
	}
	
	public BlockingQueue<JobWithHistory> getInTray()
	{
		return inTray;
	}
	
	public BlockingQueue<ProcessedJob> getOutTray()
	{
		return outTray;
	}
	
	public/*static*/void shutdown()
	{
		internalShutdown(null);
	}
	
	public/*static*/void emergencyShutdown(Throwable t)
	{
		internalShutdown(t);
	}
	
	// =========================================================================
	
	private/*static*/void internalShutdown(Throwable t)
	{
		synchronized (/*get().*/synchronizer.getMonitor())
		{
			switch (/*get().*/state)
			{
				case RUNNING:
				{
					WorkerSynchronizer sync = /*get().*/synchronizer;
					if (!sync.isSynchronized() && !sync.isAborted())
					{
						/*get().*/setEmergencyCause(t);
						
						logger.info(t == null ?
								"Nexus performing orderly shutdown" :
								"Nexus performing emergency shutdown");
						
						sync.abort();
					}
					break;
				}
				
				default:
					throw new IllegalStateException("Can only shutdown running Nexus");
			}
		}
	}
	
	private void setEmergencyCause(Throwable t)
	{
		// Don't overwrite original cause
		if (emergencyCause == null)
			emergencyCause = t;
	}
	
	private MyExecutorService stopAll()
	{
		synchronized (synchronizer.getMonitor())
		{
			MyExecutorService exec = null;
			
			// TODO: We should wait for them to complete their work ... 
			// if it's not an emergency shutdown
			
			logger.info("Stopping workers");
			
			for (WorkerBase jg : jobGenerators)
				jg.stop();
			
			for (WorkerBase pn : processingNodes)
				pn.stop();
			
			if (gatherer != null)
				gatherer.stop();
			
			for (WorkerBase s : storers)
				s.stop();
			
			if (consoleWriter != null)
				consoleWriter.stop();
			
			if (executor != null)
			{
				exec = executor;
				executor = null;
			}
			
			return exec;
		}
	}
	
	private void nexusStopped() throws Throwable
	{
		state = NexusState.SHUTDOWN;
		
		logger.info("Nexus stopped");
		
		if (emergencyCause != null)
			throw emergencyCause;
	}
}
