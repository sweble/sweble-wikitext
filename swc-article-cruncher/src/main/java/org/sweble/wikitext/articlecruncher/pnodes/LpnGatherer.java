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

import static org.sweble.wikitext.articlecruncher.JobCompletionState.*;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

import org.sweble.wikitext.articlecruncher.CompletedJob;
import org.sweble.wikitext.articlecruncher.JobWithHistory;
import org.sweble.wikitext.articlecruncher.Nexus;
import org.sweble.wikitext.articlecruncher.Result;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;

public class LpnGatherer
		extends
			WorkerBase
{
	private final CompletionService<CompletedJob> execCompServ;
	
	private final BlockingQueue<JobWithHistory> completedJobs;
	
	private final Map<Future<CompletedJob>, JobWithHistory> runningJobs;
	
	private final Semaphore backPressure;
	
	private int count = 0;
	
	private int failureCount = 0;
	
	private int successCount = 0;
	
	// =========================================================================
	
	public LpnGatherer(
			AbortHandler abortHandler,
			CompletionService<CompletedJob> execCompServ,
			Map<Future<CompletedJob>, JobWithHistory> runningJobs,
			BlockingQueue<JobWithHistory> completedJobs,
			Semaphore backPressure)
	{
		super(LpnGatherer.class.getSimpleName(), abortHandler);
		
		this.execCompServ = execCompServ;
		this.runningJobs = runningJobs;
		this.completedJobs = completedJobs;
		this.backPressure = backPressure;
	}
	
	// =========================================================================
	
	@Override
	protected void work() throws Throwable
	{
		while (true)
		{
			Future<CompletedJob> f = execCompServ.take();
			++count;
			
			JobWithHistory job = runningJobs.remove(f);
			if (job == null)
			{
				getLogger().fatal("There must be a job for every future ... " + f);
				//throw new InternalError("There must be a job for every future ...");
			}
			
			CompletedJob lastAttempt;
			try
			{
				lastAttempt = f.get();
				
				switch (lastAttempt.getState())
				{
					case FAILED:
						++failureCount;
						break;
					
					case HAS_RESULT:
						++successCount;
						break;
				}
			}
			catch (ExecutionException e)
			{
				warn("Processing failed with exception", e.getCause());
				
				if (job != null)
				{
					lastAttempt = new CompletedJob(
							FAILED,
							job.getJob(),
							new Result(
									job.getJob(),
									e));
				}
				else
				{
					lastAttempt = null;
				}
				
				++failureCount;
			}
			
			if (job != null)
			{
				completedJobs.put(new JobWithHistory(job, lastAttempt));
				Nexus.getConsoleWriter().updateInTray(completedJobs.size());
			}
			else
			{
				getLogger().fatal("Lost job... " + lastAttempt);
			}
			
			backPressure.release();
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
