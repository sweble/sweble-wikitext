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
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.log4j.Logger;
import org.apache.xerces.util.XMLCatalogResolver;

import de.fau.cs.osr.utils.WrappedException;

public abstract class DumpReader
{
	private final File dumpFile;
	
	private final Logger logger;
	
	private final XMLStreamReader xmlStreamReader;
	
	private final Unmarshaller unmarshaller;
	
	private final long fileLength;
	
	private final ExportSchemaVersion schemaVersion;
	
	private CountingInputStream decompressedInputStream;
	
	private CountingInputStream compressedInputStream;
	
	private long parsedCount;
	
	private boolean decompress;
	
	// =========================================================================
	
	public DumpReader(File dumpFile, Logger logger) throws Exception
	{
		this.dumpFile = dumpFile;
		this.logger = logger;
		
		logger.info("Setting up parser for file " + dumpFile.getAbsolutePath());
		
		schemaVersion = determineExportVersion();
		
		unmarshaller = createUnmarshaller(schemaVersion.getContextPath());
		
		installCallbacks();
		
		setSchema(
				DumpReader.class.getResource("/catalog.xml"),
				DumpReader.class.getResource(schemaVersion.getSchema()));
		
		getDumpInputStream();
		xmlStreamReader = getXmlStreamReader();
		
		fileLength = dumpFile.length();
		
		parsedCount = 0;
	}
	
	// =========================================================================
	
	public void unmarshal() throws Throwable
	{
		unmarshaller.unmarshal(xmlStreamReader);
		xmlStreamReader.close();
	}
	
	public long getFileSize()
	{
		return fileLength;
	}
	
	public long getDecompressedBytesRead() throws IOException
	{
		return decompressedInputStream.getCount();
	}
	
	public long getCompressedBytesRead() throws IOException
	{
		if (decompress)
		{
			return compressedInputStream.getCount();
		}
		else
		{
			return getDecompressedBytesRead();
		}
	}
	
	public long getParsedCount()
	{
		return parsedCount;
	}
	
	// =========================================================================
	
	protected abstract void processPage(Object mediaWiki, Object page) throws Exception;
	
	// =========================================================================
	
	private void handlePage(Object mediaWiki, Object page) throws Exception
	{
		++parsedCount;
		
		processPage(mediaWiki, page);
	}
	
	protected boolean handleEvent(ValidationEvent ve, ValidationEventLocator vel) throws Exception
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
	
	private void getDumpInputStream() throws Exception
	{
		if (dumpFile.getName().endsWith(".bz2"))
		{
			decompress = true;
			
			compressedInputStream = new CountingInputStream(
					new FileInputStream(dumpFile));
			
			decompressedInputStream = new CountingInputStream(
					new BZip2CompressorInputStream(compressedInputStream));
		}
		else if (dumpFile.getName().endsWith(".gz"))
		{
			decompress = true;
			
			compressedInputStream = new CountingInputStream(
					new FileInputStream(dumpFile));
			
			decompressedInputStream = new CountingInputStream(
					new GzipCompressorInputStream(compressedInputStream));
		}
		else
		{
			decompress = false;
			
			decompressedInputStream = new CountingInputStream(
					new FileInputStream(dumpFile));
		}
	}
	
	private ExportSchemaVersion determineExportVersion() throws Exception
	{
		final int LOOKAHEAD = 4096;
		byte[] b = new byte[LOOKAHEAD];
		
		getDumpInputStream();
		int read = decompressedInputStream.read(b, 0, LOOKAHEAD);
		decompressedInputStream.close();
		
		String header = new String(b, 0, read);
		
		if (header.contains("xmlns=\"http://www.mediawiki.org/xml/export-0.5/\""))
		{
			return ExportSchemaVersion.V0_5;
		}
		else if (header.contains("xmlns=\"http://www.mediawiki.org/xml/export-0.6/\""))
		{
			return ExportSchemaVersion.V0_6;
		}
		else if (header.contains("xmlns=\"http://www.mediawiki.org/xml/export-0.7/\""))
		{
			return ExportSchemaVersion.V0_7;
		}
		else if (header.contains("xmlns=\"http://www.mediawiki.org/xml/export-0.8/\""))
		{
			return ExportSchemaVersion.V0_8;
		}
		else
		{
			throw new IllegalArgumentException("Unknown xmlns");
		}
	}
	
	private XMLStreamReader getXmlStreamReader() throws FactoryConfigurationError, XMLStreamException
	{
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		return xmlInputFactory.createXMLStreamReader(decompressedInputStream);
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
							return DumpReader.this.handleEvent(ve, ve.getLocator());
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
			public void handlePage(Object mediaWiki, Object page)
			{
				try
				{
					DumpReader.this.handlePage(mediaWiki, page);
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
				schemaVersion.setPageListener(target, pageListener);
			}
			
			public void afterUnmarshal(Object target, Object parent)
			{
				schemaVersion.setPageListener(target, pageListener);
			}
		});
	}
	
	private Unmarshaller createUnmarshaller(String contextPath) throws JAXBException
	{
		JAXBContext context = JAXBContext.newInstance(contextPath);
		
		return context.createUnmarshaller();
	}
}
