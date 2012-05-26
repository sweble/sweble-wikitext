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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class MyExecutorCompletionService<V>
		implements
			CompletionService<V>
{
	private final ExecutorCompletionService<V> execComplServ;
	
	// =========================================================================
	
	public MyExecutorCompletionService(ExecutorType type, Logger logger)
	{
		this(type, logger, null);
	}
	
	public MyExecutorCompletionService(
			ExecutorType type,
			Logger logger,
			ThreadGroup threadGroup)
	{
		ExecutorService executor =
				new MyExecutorService(type, logger, threadGroup);
		
		execComplServ = new ExecutorCompletionService<V>(executor);
	}
	
	public MyExecutorCompletionService(
			Logger logger,
			int corePoolSize,
			int maximumPoolSize,
			long keepAliveTime,
			TimeUnit unit,
			BlockingQueue<Runnable> workQueue)
	{
		this(logger,
				corePoolSize,
				maximumPoolSize,
				keepAliveTime,
				unit,
				workQueue,
				(ThreadGroup) null);
	}
	
	public MyExecutorCompletionService(
			Logger logger,
			int corePoolSize,
			int maximumPoolSize,
			long keepAliveTime,
			TimeUnit unit,
			BlockingQueue<Runnable> workQueue,
			RejectedExecutionHandler handler)
	{
		this(logger,
				corePoolSize,
				maximumPoolSize,
				keepAliveTime,
				unit,
				workQueue,
				null,
				handler);
	}
	
	public MyExecutorCompletionService(
			Logger logger,
			int corePoolSize,
			int maximumPoolSize,
			long keepAliveTime,
			TimeUnit unit,
			BlockingQueue<Runnable> workQueue,
			ThreadGroup threadGroup)
	{
		ExecutorService executor =
				new MyExecutorService(
						logger,
						maximumPoolSize,
						maximumPoolSize,
						keepAliveTime,
						unit,
						workQueue,
						threadGroup);
		
		execComplServ = new ExecutorCompletionService<V>(executor);
	}
	
	public MyExecutorCompletionService(
			Logger logger,
			int corePoolSize,
			int maximumPoolSize,
			long keepAliveTime,
			TimeUnit unit,
			BlockingQueue<Runnable> workQueue,
			ThreadGroup threadGroup,
			RejectedExecutionHandler handler)
	{
		ExecutorService executor =
				new MyExecutorService(
						logger,
						maximumPoolSize,
						maximumPoolSize,
						keepAliveTime,
						unit,
						workQueue,
						threadGroup,
						handler);
		
		execComplServ = new ExecutorCompletionService<V>(executor);
	}
	
	// =========================================================================
	
	public Future<V> submit(Callable<V> task)
	{
		return execComplServ.submit(task);
	}
	
	public Future<V> submit(Runnable task, V result)
	{
		return execComplServ.submit(task, result);
	}
	
	public Future<V> take() throws InterruptedException
	{
		return execComplServ.take();
	}
	
	public Future<V> poll()
	{
		return execComplServ.poll();
	}
	
	public Future<V> poll(long timeout, TimeUnit unit) throws InterruptedException
	{
		return execComplServ.poll(timeout, unit);
	}
}
