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

import java.io.File;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class DumpReader
{
	private static final File DUMP_FILE = new File("/home/dohrn/home.local/downloads/enwiki-dump/20120211/enwiki-20120211-pages-meta-current1.xml-p000000010p000010000.bz2");
	
	private static final Logger logger =
			Logger.getLogger(DumpReader.class.getName());
	
	// =========================================================================
	
	public static void main(String[] args)
	{
		new DumpReader().run();
	}
	
	// =========================================================================
	
	private void run()
	{
		logger.info("Starting dump reader");
		try
		{
			setUp();
		}
		catch (Exception e)
		{
			logger.error("Dump reader hit by exception", e);
		}
		finally
		{
			logger.info("Dump reader exiting");
		}
	}
	
	public void setUp() throws Exception
	{
		Nexus nexus = Nexus.get();
		
		nexus.start(DUMP_FILE);
		
		// Add one local processing node
		logger.info("Adding local processing node");
		nexus.addProcessingNode(new ProcessingNodeFactory()
		{
			@Override
			public Runnable create(
					BlockingQueue<JobWithHistory> inTray,
					BlockingQueue<JobWithHistory> completedJobs,
					ThreadGroup parentThreadGroup)
			{
				return new LocalProcessingNode(
						inTray,
						completedJobs,
						parentThreadGroup,
						8);
			}
		});
	}
}
