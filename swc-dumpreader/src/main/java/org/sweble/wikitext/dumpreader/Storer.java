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

package org.sweble.wikitext.dumpreader;

import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class Storer
		implements
			Runnable
{
	private static final Logger logger =
			Logger.getLogger(Storer.class.getName());
	
	// =========================================================================
	
	private BlockingQueue<CompletedJob> outTray;
	
	// =========================================================================
	
	public Storer(BlockingQueue<CompletedJob> outTray)
	{
		this.outTray = outTray;
	}
	
	// =========================================================================
	
	@Override
	public void run()
	{
		try
		{
			logger.info("Storer starting");
			
			while (true)
			{
				CompletedJob job = outTray.take();
				
				// TODO: Store job results
			}
		}
		catch (InterruptedException e)
		{
			if (!Nexus.terminate())
			{
				logger.fatal("Storer interrupted unexpectedly", e);
				Nexus.emergencyShutdown(e);
			}
		}
		catch (Exception e)
		{
			logger.error("Storer hit by exception", e);
			Nexus.emergencyShutdown(e);
		}
		finally
		{
			logger.info("Storer stopped");
		}
	}
}
