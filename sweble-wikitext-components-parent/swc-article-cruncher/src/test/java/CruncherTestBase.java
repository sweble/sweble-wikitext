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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.articlecruncher.JobGeneratorFactory;
import org.sweble.wikitext.articlecruncher.JobTrace;
import org.sweble.wikitext.articlecruncher.JobTraceSet;
import org.sweble.wikitext.articlecruncher.StorerFactory;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;

import de.fau.cs.osr.utils.WrappedException;

public class CruncherTestBase
{
	protected static final long NUM_JOBS_TO_GENERATE = (long) Math.pow(2, 20);

	protected AtomicLong generated = new AtomicLong(0);

	protected AtomicLong stored = new AtomicLong(0);

	// =========================================================================

	protected static final class TestJob
			extends
				Job
	{
	}

	// =========================================================================

	protected JobGeneratorFactory createJobFactory()
	{
		return new JobGeneratorFactory()
		{
			@Override
			public WorkerBase create(
					final AbortHandler abortHandler,
					final BlockingQueue<Job> inTray,
					final JobTraceSet jobTraces)
			{
				try
				{
					return new WorkerBase("JobGenerator", abortHandler)
					{
						@Override
						protected void work() throws InterruptedException
						{
							for (long i = 0; i < NUM_JOBS_TO_GENERATE; ++i)
							{
								// generate jobs out of thin air.
								Job job = new TestJob();
								generated.incrementAndGet();

								JobTrace trace = job.getTrace();
								trace.signOff(getClass(), null);

								jobTraces.add(trace);

								inTray.put(job);
							}
						}
					};
				}
				catch (Exception e)
				{
					throw new WrappedException(e);
				}
			}
		};
	}

	protected StorerFactory createStorerFactory()
	{
		return new StorerFactory()
		{
			@Override
			public WorkerBase create(
					final AbortHandler abortHandler,
					final JobTraceSet jobTraces,
					final BlockingQueue<Job> outTray)
			{
				return new WorkerBase("Storer", abortHandler)
				{
					@Override
					protected void work() throws InterruptedException
					{
						while (true)
						{
							TestJob job = (TestJob) outTray.take();

							JobTrace trace = job.getTrace();
							trace.signOff(getClass(), null);

							// do store.
							stored.incrementAndGet();

							if (!jobTraces.remove(trace))
								throw new AssertionError("Missing job trace");
						}
					}
				};
			}
		};
	}
}
