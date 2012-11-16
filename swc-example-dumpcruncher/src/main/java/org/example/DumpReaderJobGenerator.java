package org.example;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.sweble.wikitext.articlecruncher.JobTrace;
import org.sweble.wikitext.articlecruncher.JobTraceSet;
import org.sweble.wikitext.articlecruncher.JobWithHistory;
import org.sweble.wikitext.articlecruncher.Nexus;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;
import org.sweble.wikitext.dumpreader.DumpReader;
import org.sweble.wikitext.dumpreader.export_0_6.PageType;
import org.sweble.wikitext.dumpreader.export_0_6.RevisionType;

public class DumpReaderJobGenerator
		extends
			WorkerBase
{
	private final BlockingQueue<JobWithHistory> inTray;
	
	private final JobTraceSet jobTraces;
	
	private final DumpReader dumpReader;
	
	// =========================================================================
	
	public DumpReaderJobGenerator(
			File dumpFile,
			AbortHandler abortHandler,
			BlockingQueue<JobWithHistory> inTray,
			JobTraceSet jobTraces) throws Exception
	{
		super(DumpReaderJobGenerator.class.getSimpleName(), abortHandler);
		
		this.inTray = inTray;
		this.jobTraces = jobTraces;
		
		this.dumpReader = new DumpReader(dumpFile, getLogger())
		{
			@Override
			protected void processPage(Object mediaWiki, Object page) throws Exception
			{
				DumpReaderJobGenerator.this.processPage(mediaWiki, page);
			}
		};
	}
	
	// =========================================================================
	
	public long getFileSize()
	{
		return dumpReader.getFileSize();
	}
	
	public long getDecompressedBytesRead() throws IOException
	{
		return dumpReader.getDecompressedBytesRead();
	}
	
	public long getCompressedBytesRead() throws IOException
	{
		return dumpReader.getCompressedBytesRead();
	}
	
	public long getParsedCount()
	{
		return dumpReader.getParsedCount();
	}
	
	// =========================================================================
	
	@Override
	protected void work() throws Throwable
	{
		dumpReader.unmarshal();
		
		Nexus.getConsoleWriter().writeProgress(
				getParsedCount(),
				getDecompressedBytesRead(),
				getCompressedBytesRead(),
				getFileSize());
		
		Nexus.getConsoleWriter().finish();
	}
	
	protected void processPage(Object mediaWiki, Object page_) throws Exception
	{
		Nexus.getConsoleWriter().writeProgress(
				getParsedCount(),
				getDecompressedBytesRead(),
				getCompressedBytesRead(),
				getFileSize());
		
		PageType page = (PageType) page_;
		
		for (Object o : page.getRevisionOrUploadOrLogitem())
		{
			if (o instanceof RevisionType)
			{
				RevisionJob job = new RevisionJob(page, (RevisionType) o);
				
				JobTrace trace = job.getTrace();
				trace.signOff(getClass(), null);
				
				jobTraces.add(trace);
				
				inTray.put(new JobWithHistory(job));
				Nexus.getConsoleWriter().updateInTray(inTray.size());
			}
		}
	}
}
