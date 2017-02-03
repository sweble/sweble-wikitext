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

package org.sweble.wikitext.articlecruncher.storers;

import java.util.concurrent.BlockingQueue;

import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.articlecruncher.JobTrace;
import org.sweble.wikitext.articlecruncher.JobTraceSet;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;

public class DummyStorer
		extends
			WorkerBase
{
	private final JobTraceSet jobTraces;

	private final BlockingQueue<Job> outTray;

	// =========================================================================

	public DummyStorer(
			AbortHandler abortHandler,
			JobTraceSet jobTraces,
			BlockingQueue<Job> outTray)
	{
		super(DummyStorer.class.getSimpleName(), abortHandler);

		this.outTray = outTray;
		this.jobTraces = jobTraces;
	}

	// =========================================================================

	int count = 0;

	@Override
	protected void work() throws Throwable
	{
		while (true)
		{
			Job job = outTray.take();
			++count;

			// Do nothing

			JobTrace trace = job.getTrace();
			trace.signOff(getClass(), null);

			if (!jobTraces.remove(trace))
				throw new AssertionError("Missing job trace");
		}
	}

	@Override
	protected void after()
	{
		info(getClass().getSimpleName() + " counts " + count + " items");
	}
}
