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

package org.sweble.wikitext.dumpreader;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class Nexus
{
	private static final long SHUTDOWN_TIMEOUT_IN_SECONDS = 60 * 5;
	
	// =========================================================================
	
	private static Nexus nexus;
	
	public static Nexus get()
	{
		if (nexus == null)
			nexus = new Nexus();
		return nexus;
	}
	
	public static ConsoleWriter getConsoleWriter()
	{
		return get().consoleWriter;
	}
	
	public static boolean terminate()
	{
		return get().terminate;
	}
	
	// =========================================================================
	
	private static final Logger logger =
			Logger.getLogger(Nexus.class.getName());
	
	// =========================================================================
	
	private BlockingQueue<JobWithHistory> inTray = new LinkedBlockingDeque<JobWithHistory>();
	
	private BlockingQueue<JobWithHistory> completedJobs = new LinkedBlockingDeque<JobWithHistory>();
	
	private BlockingQueue<CompletedJob> outTray = new LinkedBlockingDeque<CompletedJob>();
	
	private ExecutorService executor;
	
	private ThreadGroup parentThreadGroup;
	
	private Future<?> parserF;
	
	private Future<?> gathererF;
	
	private Future<?> storerF;
	
	private Future<?> consoleWriterF;
	
	private ConsoleWriter consoleWriter;
	
	private Exception emergencyCause;
	
	private volatile boolean terminate = false;
	
	private CountDownLatch finishLatch;
	
	// =========================================================================
	
	private Nexus()
	{
	}
	
	// =========================================================================
	
	public synchronized void start(File dumpFile) throws Exception
	{
		try
		{
			logger.info("Nexus starting");
			
			init();
			
			setUpExecutorService();
			
			startConsoleWriter();
			
			parserF = executor.submit(new Parser(dumpFile, inTray));
			
			gathererF = executor.submit(new Gatherer(inTray, completedJobs, outTray));
			
			storerF = executor.submit(new Storer(outTray));
			
			try
			{
				logger.info("Nexus waiting for termination signal");
				
				finishLatch.await();
			}
			catch (InterruptedException e)
			{
				logger.fatal("Nexus interrupted unexpectedly", e);
				
				throw e;
			}
			finally
			{
				logger.info("Nexus shutting down");
				
				sendKill();
				shutdownAndAwaitTermination();
			}
		}
		catch (Exception e)
		{
			logger.error("Nexus hit by exception", e);
			
			sendKill();
			shutdownAndAwaitTermination();
			
			throw e;
		}
		finally
		{
			logger.info("Nexus stopped");
			
			if (emergencyCause != null)
				throw emergencyCause;
		}
	}
	
	private void init()
	{
		inTray = new LinkedBlockingDeque<JobWithHistory>();
		
		completedJobs = new LinkedBlockingDeque<JobWithHistory>();
		
		outTray = new LinkedBlockingDeque<CompletedJob>();
		
		finishLatch = new CountDownLatch(1);
	}
	
	private void setUpExecutorService()
	{
		DaemonThreadFactory threadFactory = new DaemonThreadFactory("Nexus");
		parentThreadGroup = threadFactory.getGroup();
		
		executor = Executors.newCachedThreadPool(threadFactory);
	}
	
	private void startConsoleWriter()
	{
		consoleWriter = new ConsoleWriterImpl();
		if (consoleWriter != null)
		{
			consoleWriterF = executor.submit(consoleWriter);
		}
		else
		{
			consoleWriter = new QuietConsoleWriter();
		}
	}
	
	public synchronized void addProcessingNode(ProcessingNodeFactory factory)
	{
		if (!terminate())
			executor.execute(factory.create(inTray, completedJobs, parentThreadGroup));
	}
	
	public static synchronized void shutdown()
	{
		logger.info("Nexus performing orderly shutdown");
		get().finishLatch.countDown();
	}
	
	public static synchronized void emergencyShutdown(Exception e)
	{
		get().emergencyCause = e;
		
		logger.info("Nexus performing emergency shutdown");
		get().finishLatch.countDown();
	}
	
	private void sendKill()
	{
		logger.info("Sending kill signal to workers");
		
		terminate = true;
		synchronized (this)
		{
			if (parserF != null)
				parserF.cancel(true);
			
			if (gathererF != null)
				gathererF.cancel(true);
			
			if (storerF != null)
				storerF.cancel(true);
			
			if (consoleWriterF != null)
				consoleWriterF.cancel(true);
		}
	}
	
	/*
	 * Taken from usage example from:
	 * http://docs.oracle.com/javase/6/docs/api/java/util/concurrent/ExecutorService.html
	 */
	private synchronized void shutdownAndAwaitTermination()
	{
		logger.info("Shutting down workers");
		
		if (executor == null)
			return;
		
		// Disable new tasks from being submitted
		executor.shutdown();
		
		try
		{
			if (!executor.awaitTermination(SHUTDOWN_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS))
			{
				logger.error("Workers are not responding to shutdown! Forcing shutdown.");
				
				// Cancel currently executing tasks
				executor.shutdownNow();
				
				// Wait a while for tasks to respond to being cancelled
				if (!executor.awaitTermination(SHUTDOWN_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS))
					logger.error("Workers did not terminate!");
			}
		}
		catch (InterruptedException ie)
		{
			logger.error("Worker shutdown interrupted! Forcing shutdown.");
			
			// (Re-)Cancel if current thread also interrupted
			executor.shutdownNow();
			
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}
}
