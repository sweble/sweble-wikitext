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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.articlecruncher.Processor;

final class LpnWorker
		implements
			Callable<Job>
{
	private static final Logger logger = LoggerFactory.getLogger(LpnWorker.class.getSimpleName());

	private final LpnJobProcessorFactory jobProcessorFactory;

	private final Job job;

	// =========================================================================

	LpnWorker(LpnJobProcessorFactory jobProcessorFactory, Job jobHistory)
	{
		this.jobProcessorFactory = jobProcessorFactory;
		this.job = jobHistory;
	}

	// =========================================================================

	@Override
	public Job call()
	{
		try
		{
			job.signOff(getClass(), null);

			Processor processor = jobProcessorFactory.createProcessor();

			job.processed(processor.process(job));
		}
		catch (Exception t)
		{
			logger.warn("Processing failed with exception", t);

			job.failed(t);
		}

		return job;
	}
}
