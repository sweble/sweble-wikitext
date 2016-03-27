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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.MyExecutorCompletionService;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;

import de.fau.cs.osr.utils.WrappedException;

public class LpnDistributor
		extends
			WorkerBase
{
	private final BlockingQueue<Job> inTray;

	private final MyExecutorCompletionService<Job> execComplServ;

	private final Semaphore backPressure;

	private final LpnJobProcessorFactory jobProcessorFactory;

	private int count = 0;

	// =========================================================================

	public LpnDistributor(
			AbortHandler abortHandler,
			BlockingQueue<Job> inTray,
			ThreadGroup fatherThreadGroup,
			int numWorkers,
			LpnJobProcessorFactory jobProcessorFactory,
			Semaphore backPressure)
	{
		super(getClassName(), abortHandler);

		Thread.currentThread().setName(getClassName());

		this.inTray = inTray;
		this.backPressure = backPressure;
		this.jobProcessorFactory = jobProcessorFactory;

		int corePoolSize = numWorkers;
		int maximumPoolSize = numWorkers;

		info(getClass().getSimpleName() + " starts with a pool of " + numWorkers + " workers");

		execComplServ = new MyExecutorCompletionService<Job>(
				getLogger(),
				corePoolSize,
				maximumPoolSize,
				60,
				TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>(),
				new RejectedExecutionHandlerImpl());

		execComplServ.setThreadNameTemplate(jobProcessorFactory.getProcessorNameTemplate());
	}

	protected static String getClassName()
	{
		return LpnDistributor.class.getSimpleName();
	}

	// =========================================================================

	@Override
	protected void work() throws Throwable
	{
		while (true)
		{
			// Prevent a accumulation of processed jobs in the gatherer.
			backPressure.acquire();

			final Job job = inTray.take();
			++count;

			job.signOff(getClass(), null);

			Callable<Job> worker = new LpnWorker(jobProcessorFactory, job);

			execComplServ.submit(worker);
		}
	}

	@Override
	protected void after()
	{
		if (execComplServ != null)
			execComplServ.shutdownAndAwaitTermination();

		info(getClass().getSimpleName() + " counts " + count + " items");
	}

	// =========================================================================

	public CompletionService<Job> getEcs()
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
