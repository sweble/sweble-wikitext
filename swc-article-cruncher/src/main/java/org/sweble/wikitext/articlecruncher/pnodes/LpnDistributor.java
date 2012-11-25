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

package org.sweble.wikitext.articlecruncher.pnodes;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.sweble.wikitext.articlecruncher.ProcessedJob;
import org.sweble.wikitext.articlecruncher.JobWithHistory;
import org.sweble.wikitext.articlecruncher.Nexus;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.MyExecutorCompletionService;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;

import de.fau.cs.osr.utils.WrappedException;

public class LpnDistributor
		extends
			WorkerBase
{
	private final Map<Future<ProcessedJob>, JobWithHistory> runningJobs =
			new HashMap<Future<ProcessedJob>, JobWithHistory>();
	
	private final BlockingQueue<JobWithHistory> inTray;
	
	private final CompletionService<ProcessedJob> execComplServ;
	
	private final Semaphore backPressure;
	
	private final LpnJobProcessorFactory jobProcessorFactory;
	
	private int count = 0;
	
	// =========================================================================
	
	public LpnDistributor(
			AbortHandler abortHandler,
			BlockingQueue<JobWithHistory> inTray,
			ThreadGroup fatherThreadGroup,
			int numWorkers,
			LpnJobProcessorFactory jobProcessorFactory,
			Semaphore backPressure)
	{
		super(LpnDistributor.class.getSimpleName(), abortHandler);
		
		this.inTray = inTray;
		this.backPressure = backPressure;
		this.jobProcessorFactory = jobProcessorFactory;
		
		int corePoolSize = numWorkers;
		int maximumPoolSize = numWorkers;
		
		info(getClass().getSimpleName() + " starts with a pool of " + numWorkers + " workers");
		
		execComplServ = new MyExecutorCompletionService<ProcessedJob>(
				getLogger(),
				corePoolSize,
				maximumPoolSize,
				60,
				TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>(),
				new RejectedExecutionHandlerImpl());
	}
	
	// =========================================================================
	
	@Override
	protected void work() throws Throwable
	{
		while (true)
		{
			// Prevent a accumulation of processed jobs in the gatherer.
			backPressure.acquire();
			
			final JobWithHistory job = inTray.take();
			Nexus.getConsoleWriter().updateInTray(inTray.size());
			++count;
			
			job.getJob().getTrace().signOff(getClass(), null);
			
			// Make sure we retrieve the future handle before the worker kicks off
			synchronized (job)
			{
				Callable<ProcessedJob> worker =
						new LpnWorker(jobProcessorFactory, job);
				
				Future<ProcessedJob> f = execComplServ.submit(worker);
				
				runningJobs.put(f, job);
			}
		}
	}
	
	@Override
	protected void after()
	{
		info(getClass().getSimpleName() + " counts " + count + " items");
	}
	
	// =========================================================================
	
	public Map<Future<ProcessedJob>, JobWithHistory> getRunningJobs()
	{
		return runningJobs;
	}
	
	public CompletionService<ProcessedJob> getEcs()
	{
		return execComplServ;
	}
	
	// =========================================================================
	
	private final class RejectedExecutionHandlerImpl
			implements
				RejectedExecutionHandler
	{
		@Override
		public void rejectedExecution(
				Runnable r,
				ThreadPoolExecutor executor)
		{
			if (executor.isShutdown())
				return;
			
			try
			{
				executor.getQueue().put(r);
			}
			catch (InterruptedException e)
			{
				throw new WrappedException(e);
			}
		}
	}
}
