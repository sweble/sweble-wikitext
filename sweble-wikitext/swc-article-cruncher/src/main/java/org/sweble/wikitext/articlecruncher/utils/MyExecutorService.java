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

import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;

public class MyExecutorService
		implements
			ExecutorService
{
	private static final long SHUTDOWN_TIMEOUT_IN_SECONDS = 60 * 5;

	// =========================================================================

	private final Logger logger;

	private final ExecutorService executor;

	private final DaemonThreadFactory threadFactory;

	// =========================================================================

	public MyExecutorService(ExecutorType type, Logger logger)
	{
		this(type, logger, null);
	}

	public MyExecutorService(
			ExecutorType type,
			Logger logger,
			ThreadGroup threadGroup)
	{
		this.logger = logger;

		this.threadFactory = new DaemonThreadFactory(
				logger.getName(), threadGroup);

		switch (type)
		{
			case CACHED_THREAD_POOL:
				this.executor = Executors.newCachedThreadPool(threadFactory);
				break;

			default:
				throw new IllegalArgumentException("Invalid executor type");
		}
	}

	public MyExecutorService(
			Logger logger,
			int corePoolSize,
			int maximumPoolSize,
			long keepAliveTime,
			TimeUnit unit,
			BlockingQueue<Runnable> workQueue)
	{
		this(logger, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, null);
	}

	public MyExecutorService(
			Logger logger,
			int corePoolSize,
			int maximumPoolSize,
			long keepAliveTime,
			TimeUnit unit,
			BlockingQueue<Runnable> workQueue,
			ThreadGroup threadGroup)
	{
		this.logger = logger;

		this.threadFactory = new DaemonThreadFactory(
				logger.getName(), threadGroup);

		this.executor = new ThreadPoolExecutor(
				corePoolSize,
				maximumPoolSize,
				60,
				TimeUnit.SECONDS,
				workQueue,
				threadFactory);
	}

	public MyExecutorService(
			Logger logger,
			int corePoolSize,
			int maximumPoolSize,
			long keepAliveTime,
			TimeUnit unit,
			BlockingQueue<Runnable> workQueue,
			ThreadGroup threadGroup,
			RejectedExecutionHandler handler)
	{
		this.logger = logger;

		this.threadFactory = new DaemonThreadFactory(
				logger.getName(), threadGroup);

		this.executor = new ThreadPoolExecutor(
				corePoolSize,
				maximumPoolSize,
				60,
				TimeUnit.SECONDS,
				workQueue,
				threadFactory,
				handler);
	}

	// =========================================================================

	public ExecutorService getExecutor()
	{
		return executor;
	}

	public ThreadGroup getThreadGroup()
	{
		return threadFactory.getThreadGroup();
	}

	public void setThreadNameTemplate(String threadNameTemplate)
	{
		this.threadFactory.setThreadNameTemplate(threadNameTemplate);
	}

	// =========================================================================

	/*
	 * Taken from usage example from:
	 * http://docs.oracle.com/javase/6/docs/api/java/util/concurrent/ExecutorService.html
	 */
	public synchronized void shutdownAndAwaitTermination()
	{
		logger.info("Shutting down workers");

		// Disable new tasks from being submitted
		executor.shutdown();

		try
		{
			logger.info("Waiting for workers to terminate");
			if (!executor.awaitTermination(SHUTDOWN_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS))
			{
				logger.error("Workers are not responding to shutdown! Forcing shutdown.");

				// Cancel currently executing tasks
				executor.shutdownNow();

				// Wait a while for tasks to respond to being cancelled
				if (!executor.awaitTermination(SHUTDOWN_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS))
					logger.error("Workers did not terminate!");
			}
		}
		catch (InterruptedException ie)
		{
			logger.error("Worker shutdown interrupted! Forcing shutdown.");

			// (Re-)Cancel if current thread also interrupted
			executor.shutdownNow();

			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

	// =========================================================================

	@Override
	public void execute(Runnable command)
	{
		executor.execute(command);
	}

	@Override
	public void shutdown()
	{
		executor.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow()
	{
		return executor.shutdownNow();
	}

	@Override
	public boolean isShutdown()
	{
		return executor.isShutdown();
	}

	@Override
	public boolean isTerminated()
	{
		return executor.isTerminated();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
	{
		return executor.awaitTermination(timeout, unit);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task)
	{
		return executor.submit(task);
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result)
	{
		return executor.submit(task, result);
	}

	@Override
	public Future<?> submit(Runnable task)
	{
		return executor.submit(task);
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException
	{
		return executor.invokeAll(tasks);
	}

	@Override
	public <T> List<Future<T>> invokeAll(
			Collection<? extends Callable<T>> tasks,
			long timeout,
			TimeUnit unit) throws InterruptedException
	{
		return executor.invokeAll(tasks, timeout, unit);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException
	{
		return executor.invokeAny(tasks);
	}

	@Override
	public <T> T invokeAny(
			Collection<? extends Callable<T>> tasks,
			long timeout,
			TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
	{
		return executor.invokeAny(tasks, timeout, unit);
	}
}
