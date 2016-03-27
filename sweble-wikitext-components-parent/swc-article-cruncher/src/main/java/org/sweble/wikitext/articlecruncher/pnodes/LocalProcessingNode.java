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
import java.util.concurrent.CompletionService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.articlecruncher.ProcessingNode;
import org.sweble.wikitext.articlecruncher.WorkerInstantiator;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.ExecutorType;
import org.sweble.wikitext.articlecruncher.utils.MyExecutorService;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;
import org.sweble.wikitext.articlecruncher.utils.WorkerLauncher;
import org.sweble.wikitext.articlecruncher.utils.WorkerSynchronizer;

public class LocalProcessingNode
		extends
			ProcessingNode
{
	private final BlockingQueue<Job> inTray;

	private final BlockingQueue<Job> processedJobs;

	private final int numWorkers;

	private final LpnJobProcessorFactory jobProcessorFactory;

	private MyExecutorService executor;

	private Semaphore backPressure;

	private WorkerLauncher distributor;

	private WorkerLauncher gatherer;

	// =========================================================================

	public LocalProcessingNode(
			AbortHandler abortHandler,
			BlockingQueue<Job> inTray,
			BlockingQueue<Job> processedJobs,
			LpnJobProcessorFactory jobProcessorFactory,
			int numWorkers)
	{
		super(getClassName(), abortHandler);

		Thread.currentThread().setName(getClassName());

		this.inTray = inTray;
		this.processedJobs = processedJobs;
		this.jobProcessorFactory = jobProcessorFactory;
		this.numWorkers = numWorkers;
	}

	private static String getClassName()
	{
		return LocalProcessingNode.class.getSimpleName();
	}

	// =========================================================================

	@Override
	protected void work() throws Throwable
	{
		try
		{
			executor = new MyExecutorService(
					ExecutorType.CACHED_THREAD_POOL,
					getLogger());

			final AbortHandler abortHandler = new AbortHandler()
			{
				@Override
				public void notify(Throwable t)
				{
					stop();
				}
			};

			WorkerSynchronizer synchronizer = new WorkerSynchronizer();

			backPressure = new Semaphore(numWorkers);

			final BlockingQueue<CompletionService<Job>> ecsQueue =
					new LinkedBlockingQueue<CompletionService<Job>>();

			distributor = new WorkerLauncher(new WorkerInstantiator()
			{
				@Override
				public WorkerBase instantiate()
				{
					LpnDistributor d = new LpnDistributor(
							abortHandler,
							inTray,
							executor.getThreadGroup(),
							numWorkers,
							jobProcessorFactory,
							backPressure);
					ecsQueue.add(d.getEcs());
					return d;
				}
			}, abortHandler);

			distributor.start(executor, synchronizer);

			final CompletionService<Job> ecs = ecsQueue.take();

			gatherer = new WorkerLauncher(new WorkerInstantiator()
			{
				@Override
				public WorkerBase instantiate()
				{
					return new LpnGatherer(
							abortHandler,
							ecs,
							processedJobs,
							backPressure);
				}
			}, abortHandler);

			gatherer.start(executor, synchronizer);

			synchronizer.waitForAny();
		}
		finally
		{
			info("Sending kill signal to workers");

			if (distributor != null)
				distributor.stop();

			if (gatherer != null)
				gatherer.stop();

			if (executor != null)
				executor.shutdownAndAwaitTermination();
		}
	}
}
