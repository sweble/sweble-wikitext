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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.sweble.wikitext.engine.utils.SimpleWikiConfiguration;

public class LocalProcessingNode
		implements
			ProcessingNode
{
	private static final Logger logger =
			Logger.getLogger(LocalProcessingNode.class.getName());
	
	// =========================================================================
	
	private final BlockingQueue<JobWithHistory> inTray;
	
	private final BlockingQueue<JobWithHistory> completedJobs;
	
	private final ThreadGroup fatherThreadGroup;
	
	private final int numWorkers;
	
	private ExecutorService executor;
	
	// =========================================================================
	
	public LocalProcessingNode(
			BlockingQueue<JobWithHistory> inTray,
			BlockingQueue<JobWithHistory> completedJobs,
			ThreadGroup fatherThreadGroup,
			int numWorkers)
	{
		this.inTray = inTray;
		this.completedJobs = completedJobs;
		this.fatherThreadGroup = fatherThreadGroup;
		this.numWorkers = numWorkers;
	}
	
	// =========================================================================
	
	@Override
	public void run()
	{
		try
		{
			logger.fatal("LocalProcessingNode starting");
			
			SimpleWikiConfiguration config = new SimpleWikiConfiguration(
					"classpath:/org/sweble/wikitext/engine/SimpleWikiConfiguration.xml");
			
			setUpExecutorService();
			
			while (true)
			{
				JobWithHistory job = inTray.take();
				
				Future<CompletedJob> f = executor.submit(
						new LocalWorker(config, job.getJob()));
				
				try
				{
					completedJobs.put(new JobWithHistory(job, f.get()));
				}
				catch (ExecutionException e)
				{
					completedJobs.put(new JobWithHistory(
							job,
							new CompletedJob(
									CompletedJobState.FAILED_WITH_EXCEPTION,
									job.getJob(),
									new Result(e))));
				}
			}
		}
		catch (InterruptedException e)
		{
			if (!Nexus.terminate())
			{
				logger.fatal("LocalProcessingNode interrupted unexpectedly", e);
				Nexus.emergencyShutdown(e);
			}
		}
		catch (Exception e)
		{
			logger.error("LocalProcessingNode hit by exception", e);
			Nexus.emergencyShutdown(e);
		}
		finally
		{
			logger.info("LocalProcessingNode stopped");
		}
	}
	
	private void setUpExecutorService()
	{
		int corePoolSize = numWorkers;
		int maximumPoolSize = numWorkers;
		executor = new ThreadPoolExecutor(
				corePoolSize,
				maximumPoolSize,
				60,
				TimeUnit.SECONDS,
				new LinkedBlockingDeque<Runnable>(1),
				new DaemonThreadFactory(
						"LocalProcessingNode",
						fatherThreadGroup));
	}
}
