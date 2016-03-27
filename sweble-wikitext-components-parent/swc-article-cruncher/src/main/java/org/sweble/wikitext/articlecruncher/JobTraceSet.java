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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class JobTraceSet
{
	private Set<JobTrace> traces = new HashSet<JobTrace>();

	// =========================================================================

	public synchronized void add(JobTrace trace)
	{
		traces.add(trace);
	}

	public synchronized boolean remove(JobTrace trace)
	{
		boolean removed = traces.remove(trace);
		notifyAll();
		return removed;
	}

	public synchronized Set<JobTrace> getTraces()
	{
		return Collections.unmodifiableSet(traces);
	}

	public synchronized void waitForCompletion(int timeoutInSeconds) throws InterruptedException
	{
		while (!traces.isEmpty())
			wait(timeoutInSeconds * 1000);
	}
}
