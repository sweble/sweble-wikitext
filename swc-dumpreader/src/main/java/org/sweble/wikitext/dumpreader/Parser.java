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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.concurrent.BlockingQueue;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.sweble.wikitext.dumpreader.xjc.MediaWikiType;
import org.sweble.wikitext.dumpreader.xjc.PageListener;
import org.sweble.wikitext.dumpreader.xjc.PageType;

import com.sun.org.apache.xerces.internal.util.XMLCatalogResolver;

public class Parser
		implements
			Runnable
{
	private static final class TerminationException
			extends
				RuntimeException
	{
		private static final long serialVersionUID = 1L;
	}
	
	// =========================================================================
	
	private static final Logger logger =
			Logger.getLogger(Parser.class.getName());
	
	// =========================================================================
	
	private final BlockingQueue<JobWithHistory> inTray;
	
	private final File dumpFile;
	
	private final RandomAccessFile dumpRaFile;
	
	private final XMLStreamReader xmlStreamReader;
	
	private final Unmarshaller unmarshaller;
	
	private final long fileLength;
	
	private long parsedCount;
	
	// =========================================================================
	
	public Parser(File dumpFile, BlockingQueue<JobWithHistory> inTray)
	{
		this.dumpFile = dumpFile;
		this.inTray = inTray;
		
		try
		{
			dumpRaFile = new RandomAccessFile(dumpFile, "r");
			
			unmarshaller = createUnmarshaller();
			
			installCallbacks();
			
			setSchema(
					Parser.class.getResource("/catalog.xml"),
					Parser.class.getResource("/export-0.5.xsd"));
			
			xmlStreamReader = setUpParser(dumpRaFile);
			
			fileLength = dumpRaFile.length();
			
			parsedCount = 0;
		}
		catch (Exception e)
		{
			throw new WrappedException(e);
		}
	}
	
	// =========================================================================
	
	public long getFileLength()
	{
		return fileLength;
	}
	
	public long getFilePointer() throws IOException
	{
		return dumpRaFile.getFilePointer();
	}
	
	public long getParsedCount()
	{
		return parsedCount;
	}
	
	// =========================================================================
	
	@Override
	public void run()
	{
		try
		{
			logger.info("Parser starts unmarshalling");
			
			unmarshaller.unmarshal(xmlStreamReader);
			
			logger.info("Parser finished successfully");
			Nexus.shutdown();
		}
		catch (TerminationException e)
		{
			logger.warn("Parser received termination signal before finishing");
		}
		catch (Exception e)
		{
			logger.error("Parser hit by exception", e);
			Nexus.emergencyShutdown(e);
		}
		finally
		{
			logger.info("Parser stopped");
		}
	}
	
	private void handlePage(PageType page) throws IOException
	{
		++parsedCount;
		
		ConsoleWriter consoleWriter = Nexus.getConsoleWriter();
		if (consoleWriter != null)
			consoleWriter.writeProgress(
					getFilePointer(), fileLength, -1, parsedCount);
		
		Job job = new Job(page);
		
		// Post to in tray
		try
		{
			inTray.put(new JobWithHistory(job));
		}
		catch (InterruptedException e)
		{
			if (!Nexus.terminate())
			{
				logger.fatal("Gatherer interrupted unexpectedly", e);
				Nexus.emergencyShutdown(e);
			}
			throw new TerminationException();
		}
	}
	
	private boolean handleEvent(ValidationEvent ve, ValidationEventLocator vel) throws Exception
	{
		logger.warn(String.format(
				"%s:%d:%d: %s",
				dumpFile.getAbsolutePath(),
				vel.getLineNumber(),
				vel.getColumnNumber(),
				ve.getMessage()));
		
		return true;
	}
	
	// =========================================================================
	
	private static XMLStreamReader setUpParser(RandomAccessFile dumpFile) throws Exception
	{
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		
		InputStream inputStream = new FileInputStream(dumpFile.getFD());
		
		return xmlInputFactory.createXMLStreamReader(inputStream);
	}
	
	private void setSchema(URL catalog, URL schemaUrl) throws Exception
	{
		SchemaFactory sf = SchemaFactory.newInstance(
				javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
		sf.setResourceResolver(
				new XMLCatalogResolver(
						new String[] { catalog.getFile() }));
		
		Schema schema = sf.newSchema(schemaUrl);
		
		unmarshaller.setSchema(schema);
		
		unmarshaller.setEventHandler(
				new ValidationEventHandler()
				{
					public boolean handleEvent(ValidationEvent ve)
					{
						try
						{
							return Parser.this.handleEvent(ve, ve.getLocator());
						}
						catch (RuntimeException e)
						{
							throw e;
						}
						catch (Exception e)
						{
							throw new WrappedException(e);
						}
					}
				});
	}
	
	private void installCallbacks()
	{
		final PageListener pageListener = new PageListener()
		{
			@Override
			public void handlePage(MediaWikiType mediaWiki, PageType page)
			{
				try
				{
					Parser.this.handlePage(page);
				}
				catch (RuntimeException e)
				{
					throw e;
				}
				catch (Exception e)
				{
					throw new WrappedException(e);
				}
			}
		};
		
		unmarshaller.setListener(new Unmarshaller.Listener()
		{
			public void beforeUnmarshal(Object target, Object parent)
			{
				if (target instanceof MediaWikiType)
					((MediaWikiType) target).setPageListener(pageListener);
			}
			
			public void afterUnmarshal(Object target, Object parent)
			{
				if (target instanceof MediaWikiType)
					((MediaWikiType) target).setPageListener(null);
			}
		});
	}
	
	private static Unmarshaller createUnmarshaller() throws JAXBException
	{
		JAXBContext context = JAXBContext.newInstance(
				"org.sweble.wikitext.dumpreader.xjc");
		
		return context.createUnmarshaller();
	}
}
