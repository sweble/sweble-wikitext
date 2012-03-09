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

public class ConsoleWriterImpl
		implements
			ConsoleWriter
{
	private static Object progressLock = new Object();
	
	private boolean progressHasUpdate = false;
	
	private long progressFilePointer;
	
	private long progressFileLength;
	
	private int progressQueueFill;
	
	private long progressParsedCount;
	
	// =========================================================================
	
	private final ConsoleProgress progress = new ConsoleProgress();
	
	// =========================================================================
	
	@Override
	public void run()
	{
		while (!Nexus.terminate())
		{
			synchronized (progressLock)
			{
				if (progressHasUpdate)
				{
					progress.reportProgress(
							progressFilePointer,
							progressFileLength,
							progressQueueFill,
							progressParsedCount);
					progressHasUpdate = false;
				}
			}
			
			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException e)
			{
			}
		}
	}
	
	// =========================================================================
	
	@Override
	public void writeProgress(
			long filePointer,
			long fileLength,
			int queueFill,
			long parsedCount)
	{
		synchronized (progressLock)
		{
			this.progressFilePointer = filePointer;
			this.progressFileLength = fileLength;
			this.progressQueueFill = queueFill;
			this.progressParsedCount = parsedCount;
			this.progressHasUpdate = true;
		}
	}
}
