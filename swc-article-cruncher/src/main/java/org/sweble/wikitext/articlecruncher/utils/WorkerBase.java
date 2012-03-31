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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import de.fau.cs.osr.utils.WrappedException;

public abstract class WorkerBase
{
	public enum WorkerState
	{
		INITIALIZED,
		RUNNING,
		POISONED,
		STOPPED,
	}
	
	private final Object kickOffLock = new Object();
	
	private final String workerName;
	
	private final AbortHandler abortHandler;
	
	private final Logger logger;
	
	private Future<?> future;
	
	private WorkerState state;
	
	private WorkerSynchronizer synchronizer;
	
	// =========================================================================
	
	public WorkerBase(String workerName)
	{
		this(workerName, new AbortHandler()
		{
			@Override
			public void notify(Throwable e)
			{
				// Does nothing
			}
		});
	}
	
	public WorkerBase(
			String workerName,
			AbortHandler abortHandler)
	{
		this.workerName = workerName;
		this.abortHandler = abortHandler;
		
		this.logger = Logger.getLogger(workerName);
		this.state = WorkerState.INITIALIZED;
	}
	
	// =========================================================================
	
	protected abstract void work() throws Throwable;
	
	protected void after()
	{
	}
	
	protected void abort(Throwable e)
	{
		abortHandler.notify(e);
	}
	
	// =========================================================================
	
	public final synchronized void start(ExecutorService executor)
	{
		start(executor, null);
	}
	
	public final synchronized void start(
			ExecutorService executor,
			WorkerSynchronizer synchronizer)
	{
		synchronized (state)
		{
			if (state != WorkerState.INITIALIZED)
			{
				throw new IllegalStateException("start() can be called only once");
			}
			else
			{
				// Make sure that this.future is set before the worker can kick-off
				synchronized (kickOffLock)
				{
					this.state = WorkerState.RUNNING;
					this.synchronizer = synchronizer;
					
					this.future = executor.submit(new WorkerRunnable());
				}
			}
		}
	}
	
	public final synchronized void stop()
	{
		synchronized (state)
		{
			switch (state)
			{
				case RUNNING:
					logger.info("Sending stop signal to worker " + workerName);
					state = WorkerState.POISONED;
					future.cancel(true);
					break;
			
			/*
			case POISONED:
			logger.warn("Already sent stop signal to worker " + workerName);
			break;
			
			case STOPPED:
			logger.warn("Worker " + workerName + " already terminated");
			break;
			
			case INITIALIZED:
			throw new IllegalStateException("stop() can only be called after start()");
			*/
			}
		}
	}
	
	public final synchronized void await() throws InterruptedException, ExecutionException
	{
		await(Long.MAX_VALUE, null);
	}
	
	public final synchronized boolean await(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException
	{
		Future<?> f;
		synchronized (state)
		{
			switch (state)
			{
				case INITIALIZED:
					throw new IllegalStateException("await() can only be called after start()");
			}
			
			f = future;
		}
		
		if (f != null)
		{
			try
			{
				if (timeout == Long.MAX_VALUE && unit == null)
				{
					future.get();
				}
				else
				{
					future.get(timeout, unit);
				}
			}
			catch (TimeoutException e)
			{
				// timed out
				return false;
			}
		}
		else
		{
			logger.warn("Worker " + workerName + " already terminated");
		}
		
		// joined with future
		return true;
	}
	
	// =========================================================================
	
	private final class WorkerRunnable
			implements
				Runnable
	{
		@Override
		public void run()
		{
			synchronized (kickOffLock)
			{
			}
			
			try
			{
				if (synchronizer != null)
					synchronizer.oneStarted();
				
				logger.info(workerName + " starting");
				
				try
				{
					work();
				}
				catch (WrappedException e)
				{
					throw e.getCause();
				}
			}
			catch (InterruptedException e)
			{
				boolean unexpected = false;
				synchronized (state)
				{
					// Don't call abort inside lock!
					if (state != WorkerState.POISONED)
						unexpected = true;
				}
				
				if (unexpected)
				{
					logger.error(workerName + " interrupted unexpectedly", e);
					abort(e);
				}
			}
			catch (Throwable t)
			{
				logger.error(workerName + " terminated by exception", t);
				abort(t);
			}
			finally
			{
				try
				{
					after();
				}
				catch (Throwable t)
				{
					logger.error(workerName + ".after() threw exception", t);
				}
				
				synchronized (state)
				{
					state = WorkerState.STOPPED;
				}
				
				logger.info(workerName + " stopped");
				
				if (synchronizer != null)
					synchronizer.oneStopped();
			}
		}
	}
	
	// =========================================================================
	
	protected Logger getLogger()
	{
		return logger;
	}
	
	protected void trace(Object message)
	{
		logger.trace(message);
	}
	
	protected void trace(Object message, Throwable t)
	{
		logger.trace(message, t);
	}
	
	protected void debug(Object message)
	{
		logger.debug(message);
	}
	
	protected void debug(Object message, Throwable t)
	{
		logger.debug(message, t);
	}
	
	protected void info(Object message)
	{
		logger.info(message);
	}
	
	protected void info(Object message, Throwable t)
	{
		logger.info(message, t);
	}
	
	protected void warn(Object message)
	{
		logger.warn(message);
	}
	
	protected void warn(Object message, Throwable t)
	{
		logger.warn(message, t);
	}
	
	protected void error(Object message)
	{
		logger.error(message);
	}
	
	protected void error(Object message, Throwable t)
	{
		logger.error(message, t);
	}
	
	protected void fatal(Object message)
	{
		logger.fatal(message);
	}
	
	protected void fatal(Object message, Throwable t)
	{
		logger.fatal(message, t);
	}
}
