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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;

public class LpnGatherer
		extends
			WorkerBase
{
	private final CompletionService<Job> execCompServ;

	private final BlockingQueue<Job> processedJobs;

	private final Semaphore backPressure;

	private int count = 0;

	private int failureCount = 0;

	private int successCount = 0;

	// =========================================================================

	public LpnGatherer(
			AbortHandler abortHandler,
			CompletionService<Job> execCompServ,
			BlockingQueue<Job> processedJobs,
			Semaphore backPressure)
	{
		super(getClassName(), abortHandler);

		Thread.currentThread().setName(getClassName());

		this.execCompServ = execCompServ;
		this.processedJobs = processedJobs;
		this.backPressure = backPressure;
	}

	private static String getClassName()
	{
		return LpnGatherer.class.getSimpleName();
	}

	// =========================================================================

	@Override
	protected void work() throws Throwable
	{
		while (true)
		{
			Future<Job> f = execCompServ.take();
			++count;

			try
			{
				Job processedJob = f.get();

				switch (processedJob.getState())
				{
					case FAILED:
						++failureCount;
						break;

					case HAS_RESULT:
						++successCount;
						break;

					default:
						throw new AssertionError();
				}

				processedJobs.put(processedJob);
				backPressure.release();
			}
			catch (ExecutionException e)
			{
				error(LpnWorker.class.getSimpleName() + " failed with unhandled expection", e.getCause());

				abort(e.getCause());
			}
		}
	}

	@Override
	protected void after()
	{
		info(getClass().getSimpleName() + " counts " + count + " items");
		info(getClass().getSimpleName() + " counts " + failureCount + " items for which processing failed");
		info(getClass().getSimpleName() + " counts " + successCount + " items which were successfully processed");
	}
}
