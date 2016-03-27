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

package org.sweble.wikitext.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.concurrent.BlockingQueue;

import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.articlecruncher.JobTrace;
import org.sweble.wikitext.articlecruncher.JobTraceSet;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;
import org.sweble.wikitext.dumpreader.DumpReader;
import org.sweble.wikitext.dumpreader.export_0_10.PageType;
import org.sweble.wikitext.dumpreader.export_0_10.RevisionType;

import de.fau.cs.osr.utils.WrappedException;

public class DumpReaderJobGenerator
		extends
			WorkerBase
{
	private final BlockingQueue<Job> inTray;

	private final JobTraceSet jobTraces;

	private final DumpCruncher dumpCruncher;

	private final DumpReader dumpReader;

	private InputStream is;

	// =========================================================================

	public DumpReaderJobGenerator(
			DumpCruncher dumpCruncher,
			File dumpFile,
			Charset charset,
			AbortHandler abortHandler,
			BlockingQueue<Job> inTray,
			JobTraceSet jobTraces)
	{
		super(DumpReaderJobGenerator.class.getSimpleName(), abortHandler);

		this.dumpCruncher = dumpCruncher;
		this.inTray = inTray;
		this.jobTraces = jobTraces;

		try
		{
			is = new FileInputStream(dumpFile);
			this.dumpReader = new DumpReader(
					is,
					charset,
					dumpFile.getPath(),
					getLogger(),
					false)
			{
				@Override
				protected void processPage(Object mediaWiki, Object page)
				{
					try
					{
						DumpReaderJobGenerator.this.processPage(mediaWiki, page);
					}
					catch (InterruptedException e)
					{
						throw new WrappedException(e);
					}
					catch (IOException e)
					{
						throw new WrappedException(e);
					}
				}
			};
		}
		catch (Exception e)
		{
			after();
			throw new WrappedException(e);
		}
	}

	@Override
	public void after()
	{
		if (is != null)
		{
			try
			{
				info("Close the input stream");
				is.close();
				is = null;
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
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
	}

	protected void processPage(Object mediaWiki, Object page_) throws InterruptedException, IOException
	{
		PageType page = (PageType) page_;

		for (Object o : page.getRevisionOrUpload())
		{
			if (o instanceof RevisionType)
			{
				RevisionJob job = new RevisionJob(page, (RevisionType) o);

				JobTrace trace = job.getTrace();
				trace.signOff(getClass(), null);

				jobTraces.add(trace);

				inTray.put(job);

				Gui gui = dumpCruncher.getGui();
				gui.setPageCount((int) getParsedCount());
				gui.setBytesRead(getCompressedBytesRead());
				gui.redrawLater();
			}
		}
	}
}
