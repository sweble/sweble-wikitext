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

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.sweble.wikitext.articlecruncher.JobWithHistory;
import org.sweble.wikitext.articlecruncher.ProcessedJob;
import org.sweble.wikitext.articlecruncher.Processor;

final class LpnWorker
		implements
			Callable<JobWithHistory>
{
	private static final Logger logger = Logger.getLogger(LpnWorker.class.getSimpleName());
	
	private final LpnJobProcessorFactory jobProcessorFactory;
	
	private final JobWithHistory jobHistory;
	
	// =========================================================================
	
	LpnWorker(LpnJobProcessorFactory jobProcessorFactory, JobWithHistory jobHistory)
	{
		this.jobProcessorFactory = jobProcessorFactory;
		this.jobHistory = jobHistory;
	}
	
	// =========================================================================
	
	@Override
	public JobWithHistory call() throws Exception
	{
		ProcessedJob lastAttempt;
		try
		{
			jobHistory.getJob().getTrace().signOff(getClass(), null);
			
			Processor processor = jobProcessorFactory.createProcessor();
			
			lastAttempt = processor.process(jobHistory);
		}
		catch (Throwable t)
		{
			logger.warn("Processing failed with exception", t);
			
			lastAttempt = new ProcessedJob(jobHistory.getJob(), t);
		}
		
		return new JobWithHistory(jobHistory, lastAttempt);
	}
}
