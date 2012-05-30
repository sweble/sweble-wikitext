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

public class ConsoleWriterDummy
		extends
			ConsoleWriterBase
{
	public ConsoleWriterDummy(AbortHandler abortHandler)
	{
		super(ConsoleWriterDummy.class.getSimpleName(), abortHandler);
	}
	
	// =========================================================================
	
	@Override
	protected void work() throws Throwable
	{
	}
	
	// =========================================================================
	
	public void writeProgress(
			long parsedCount,
			long decompressedBytesRead,
			long compressedBytesRead,
			long compressedLength)
	{
	}
	
	public void updateOutTray(int size)
	{
	}
	
	public void updateInTray(int size)
	{
	}
	
	public void updateCompletedJobs(int size)
	{
	}
	
	public void finish()
	{
	}
}
