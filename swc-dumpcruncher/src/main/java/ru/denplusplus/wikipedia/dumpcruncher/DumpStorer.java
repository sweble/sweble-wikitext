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

package ru.denplusplus.wikipedia.dumpcruncher;

import java.io.OutputStreamWriter;
import java.util.regex.Pattern;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.articlecruncher.JobTrace;
import org.sweble.wikitext.articlecruncher.JobTraceSet;
import org.sweble.wikitext.articlecruncher.storers.DummyStorer;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;

public class DumpStorer extends WorkerBase
{
	private final JobTraceSet jobTraces;
	
	private final BlockingQueue<Job> outTray;
	
	private final OutputStreamWriter swCars;
	private final OutputStreamWriter swPersons;

	private static final Pattern CARS_PATTERN = Pattern.compile("\\[\\[Category:([^\\]]*) cars\\]\\]");
	
	private int count;
	private AtomicInteger matchedCount;
	
	// =========================================================================
	
	public DumpStorer(AbortHandler abortHandler, JobTraceSet jobTraces, BlockingQueue<Job> outTray, OutputStreamWriter swCars, OutputStreamWriter swPersons, AtomicInteger matchedCount)
	{
		super(DummyStorer.class.getSimpleName(), abortHandler);
		
		this.outTray = outTray;
		this.jobTraces = jobTraces;
		this.swCars = swCars;
		this.swPersons = swPersons;
		
		this.count = 0;
		this.matchedCount = matchedCount;
	}
	
	// =========================================================================
	
	@Override
	protected void work() throws Throwable
	{
		while (true)
		{
			RevisionJob job = (RevisionJob)outTray.take();
			++count;
			
			String text = job.getTextText();
			if (-1 != text.indexOf("{{Infobox person"))
			{
				swPersons.write(text);
				swPersons.write("\n=======EOF=======\n");
                swPersons.flush();
				matchedCount.incrementAndGet();
			}
			else if (CARS_PATTERN.matcher(text).find())
			{
				swCars.write(text);
				swCars.write("\n=======EOF=======\n");
				swCars.flush();
                matchedCount.incrementAndGet();
			}
			
			JobTrace trace = job.getTrace();
			trace.signOff(getClass(), null);
			
			if (!jobTraces.remove(trace))
				throw new InternalError("Missing job trace");
		}
	}
	
	@Override
	protected void after()
	{
		info(getClass().getSimpleName() + " counts " + count + " items, matched " + matchedCount.get() + " items");
	}
}
