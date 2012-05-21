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

public abstract class ConsoleWriterBase
		extends
			WorkerBase
{
	public ConsoleWriterBase(String workerName, AbortHandler abortHandler)
	{
		super(workerName, abortHandler);
	}
	
	public ConsoleWriterBase(String workerName)
	{
		super(workerName);
	}
	
	public abstract void writeProgress(
			long parsedCount,
			long decompressedBytesRead,
			long compressedBytesRead,
			long compressedLength);
	
	public abstract void updateOutTray(int size);
	
	public abstract void updateInTray(int size);
	
	public abstract void updateCompletedJobs(int size);
	
	public abstract void finish();
	
}
