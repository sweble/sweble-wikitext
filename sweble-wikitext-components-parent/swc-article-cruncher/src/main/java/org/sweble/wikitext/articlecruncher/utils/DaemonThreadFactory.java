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

package org.sweble.wikitext.articlecruncher.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DaemonThreadFactory
		implements
			ThreadFactory
{
	private final AtomicInteger threadNumber = new AtomicInteger(1);

	private final ThreadGroup group;

	private String threadNameTemplate;

	// =========================================================================

	public DaemonThreadFactory(String poolName)
	{
		this(poolName, Thread.currentThread().getThreadGroup());
	}

	public DaemonThreadFactory(String poolName, ThreadGroup group)
	{
		if (group == null)
			group = new ThreadGroup(poolName);

		this.group = group;

		this.threadNameTemplate = String.format("%s-%%02d", poolName);
	}

	// =========================================================================

	public void setThreadNameTemplate(String threadNameTemplate)
	{
		this.threadNameTemplate = threadNameTemplate;
	}

	// =========================================================================

	@Override
	public Thread newThread(Runnable runnable)
	{
		String name = String.format(threadNameTemplate, threadNumber.getAndIncrement());

		Thread thread = new Thread(group, runnable, name);

		if (!thread.isDaemon())
			thread.setDaemon(true);

		if (thread.getPriority() != Thread.NORM_PRIORITY)
			thread.setPriority(Thread.NORM_PRIORITY);

		return thread;
	}

	public ThreadGroup getThreadGroup()
	{
		return group;
	}
}
