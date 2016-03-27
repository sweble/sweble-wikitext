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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;
import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.articlecruncher.JobGeneratorFactory;
import org.sweble.wikitext.articlecruncher.JobTrace;
import org.sweble.wikitext.articlecruncher.Nexus;
import org.sweble.wikitext.articlecruncher.ProcessingNodeFactory;
import org.sweble.wikitext.articlecruncher.StorerFactory;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;

public class CruncherTest
		extends
			CruncherTestBase
{
	private Nexus nexus;

	private AtomicLong processed = new AtomicLong(0);

	private long failAfter = -1;

	// =========================================================================

	@Before
	public void before() throws Throwable
	{
		nexus = new Nexus();

		nexus.setUp(
				16, /* in tray capacity */
				16, /* processed jobs capacity */
				16 /* out tray capacity */);

		JobGeneratorFactory jobFactory = createJobFactory();
		nexus.addJobGenerator(jobFactory);

		ProcessingNodeFactory pnFactory = createPnFactory();
		nexus.addProcessingNode(pnFactory);

		StorerFactory storerFactory = createStorerFactory();
		nexus.addStorer(storerFactory);
	}

	// =========================================================================

	@Test
	public void testWithoutFailing() throws Throwable
	{
		nexus.start();

		assertEquals(NUM_JOBS_TO_GENERATE, generated.get());

		assertEquals(NUM_JOBS_TO_GENERATE, processed.get());

		assertEquals(NUM_JOBS_TO_GENERATE, stored.get());

		Set<JobTrace> jobTraces = nexus.getJobTraces();
		assertTrue(jobTraces.isEmpty());
	}

	@Test
	public void testWithFailing() throws Throwable
	{
		failAfter = (NUM_JOBS_TO_GENERATE * 3) / 4;

		try
		{
			nexus.start();
		}
		catch (RuntimeException e)
		{
			assertEquals("Die!", e.getMessage());
		}

		Set<JobTrace> jobTraces = nexus.getJobTraces();

		assertEquals(generated.get(), processed.get() + jobTraces.size());
		assertEquals(generated.get(), stored.get() + jobTraces.size());

		/*
		System.out.println("numJobsToGenerate: " + NUM_JOBS_TO_GENERATE);
		System.out.println("generated: " + generated.get());
		System.out.println("processed: " + processed.get());
		System.out.println("stored: " + stored.get());
		System.out.println("job traces left: " + jobTraces.size());
		*/
	}

	// =========================================================================

	private ProcessingNodeFactory createPnFactory()
	{
		return new ProcessingNodeFactory()
		{
			@Override
			public WorkerBase create(
					final AbortHandler abortHandler,
					final BlockingQueue<Job> inTray,
					final BlockingQueue<Job> processedJobs)
			{
				return new WorkerBase("ProcessingNode", abortHandler)
				{
					@Override
					protected void work() throws InterruptedException
					{
						while (true)
						{
							Job job = inTray.take();

							job.signOff(getClass(), null);

							// do process.
							if (failAfter >= 0 && generated.get() > failAfter)
								throw new RuntimeException("Die!");

							processed.incrementAndGet();

							job.processed((Object) null);

							processedJobs.put(job);
						}
					}
				};
			}
		};
	}
}
