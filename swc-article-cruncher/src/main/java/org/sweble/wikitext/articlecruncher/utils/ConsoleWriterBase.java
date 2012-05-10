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
