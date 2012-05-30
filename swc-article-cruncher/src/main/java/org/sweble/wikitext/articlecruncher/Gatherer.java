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

import java.util.concurrent.BlockingQueue;

import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;

public class Gatherer
		extends
			WorkerBase
{
	private final BlockingQueue<JobWithHistory> inTray;
	
	private final BlockingQueue<JobWithHistory> completedJobs;
	
	private final BlockingQueue<CompletedJob> outTray;
	
	// =========================================================================
	
	public Gatherer(
			AbortHandler abortHandler,
			BlockingQueue<JobWithHistory> inTray,
			BlockingQueue<JobWithHistory> completedJobs,
			BlockingQueue<CompletedJob> outTray)
	{
		super(Gatherer.class.getSimpleName(), abortHandler);
		
		this.inTray = inTray;
		this.completedJobs = completedJobs;
		this.outTray = outTray;
	}
	
	// =========================================================================
	
	int count = 0;
	
	@Override
	protected void work() throws Throwable
	{
		while (true)
		{
			JobWithHistory completed = completedJobs.take();
			Nexus.getConsoleWriter().updateCompletedJobs(completedJobs.size());
			++count;
			
			completed.getJob().getTrace().signOff(getClass(), null);
			
			// TODO: Decide what to do with it.
			boolean tryAgain = false;
			
			if (tryAgain)
			{
				inTray.put(completed);
				Nexus.getConsoleWriter().updateInTray(inTray.size());
			}
			else
			{
				outTray.put(completed.getLastAttempt());
				Nexus.getConsoleWriter().updateOutTray(outTray.size());
			}
		}
	}
	
	@Override
	protected void after()
	{
		info(getClass().getSimpleName() + " counts " + count + " items");
	}
}
