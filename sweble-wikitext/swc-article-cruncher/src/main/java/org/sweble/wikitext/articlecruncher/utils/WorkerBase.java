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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WorkerBase
{
	private final String workerName;

	private final AbortHandler abortHandler;

	private final Logger logger;

	private WorkerLauncher launcher;

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

	public WorkerBase(String workerName, AbortHandler abortHandler)
	{
		this.workerName = workerName;
		this.abortHandler = abortHandler;
		this.logger = LoggerFactory.getLogger(workerName);
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

	protected void stop()
	{
		launcher.stop();
	}

	// =========================================================================

	protected void setLauncher(WorkerLauncher launcher)
	{
		this.launcher = launcher;
	}

	protected String getWorkerName()
	{
		return workerName;
	}

	protected Logger getLogger()
	{
		return logger;
	}

	protected void trace(String message)
	{
		logger.trace(message);
	}

	protected void trace(String message, Throwable t)
	{
		logger.trace(message, t);
	}

	protected void debug(String message)
	{
		logger.debug(message);
	}

	protected void debug(String message, Throwable t)
	{
		logger.debug(message, t);
	}

	protected void info(String message)
	{
		logger.info(message);
	}

	protected void info(String message, Throwable t)
	{
		logger.info(message, t);
	}

	protected void warn(String message)
	{
		logger.warn(message);
	}

	protected void warn(String message, Throwable t)
	{
		logger.warn(message, t);
	}

	protected void error(String message)
	{
		logger.error(message);
	}

	protected void error(String message, Throwable t)
	{
		logger.error(message, t);
	}
}
