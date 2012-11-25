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

public class JobWithHistory
{
	private final Job job;
	
	private final JobWithHistory history;
	
	private final ProcessedJob lastAttempt;
	
	// =========================================================================
	
	/**
	 * Wrap a new job without history.
	 */
	public JobWithHistory(Job job)
	{
		this.job = job;
		this.history = null;
		this.lastAttempt = null;
	}
	
	/**
	 * Wrap a job with history
	 * 
	 * @param history
	 *            The last history object to build the history chain.
	 * @param lastAttempt
	 *            The processed job (this can be a failed or successful
	 *            attempt).
	 */
	public JobWithHistory(JobWithHistory history, ProcessedJob lastAttempt)
	{
		this.job = lastAttempt.getJob();
		this.history = history;
		this.lastAttempt = lastAttempt;
	}
	
	// =========================================================================
	
	public Job getJob()
	{
		return job;
	}
	
	public JobWithHistory getHistory()
	{
		return history;
	}
	
	public ProcessedJob getLastAttempt()
	{
		return lastAttempt;
	}
}
