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

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;

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
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import de.fau.cs.osr.utils.WrappedException;

public abstract class DumpReader
		implements
			Closeable
{
	private final InputStream dumpInputStream;

	private final String dumpUri;

	private final Logger logger;

	private final XMLStreamReader xmlStreamReader;

	private final Unmarshaller unmarshaller;

	private final ExportSchemaVersion schemaVersion;

	private CountingInputStream decompressedInputStream;

	private CountingInputStream compressedInputStream;

	private long fileLength;

	private long parsedCount;

	private boolean decompress;

	private static final int LOOKAHEAD = 4096;

	// =========================================================================

	/**
	 * Use a constructor that expects an encoding to prevent bug in xerces
	 * parser code.
	 * 
	 * @deprecated
	 */
	public DumpReader(File dumpFile, Logger logger) throws JAXBException, FactoryConfigurationError, XMLStreamException, IOException, SAXException
	{
		this(new FileInputStream(dumpFile), dumpFile.getAbsolutePath(), logger);

		fileLength = dumpFile.length();
	}

	/**
	 * Use a constructor that expects an encoding to prevent bug in xerces
	 * parser code.
	 * 
	 * @deprecated
	 */
	public DumpReader(InputStream is, String url, Logger logger) throws JAXBException, FactoryConfigurationError, XMLStreamException, IOException, SAXException
	{
		this(is, null, url, logger, true);
	}

	/**
	 * Use a constructor that expects an encoding to prevent bug in xerces
	 * parser code.
	 * 
	 * @deprecated
	 */
	public DumpReader(
			InputStream is,
			String url,
			Logger logger,
			boolean useSchema) throws JAXBException, FactoryConfigurationError, XMLStreamException, IOException, SAXException
	{
		this(is, null, url, logger, useSchema);
	}

	public DumpReader(
			InputStream is,
			Charset encoding,
			String url,
			Logger logger,
			boolean useSchema) throws JAXBException, FactoryConfigurationError, XMLStreamException, IOException, SAXException
	{
		this.dumpInputStream = is;
		this.dumpUri = url;
		this.logger = logger;

		logger.info("Setting up parser for file " + dumpUri);

		getDumpInputStream();

		schemaVersion = determineExportVersion();

		unmarshaller = createUnmarshaller(schemaVersion.getContextPath());

		installCallbacks();

		if (useSchema)
			setSchema(DumpReader.class.getResource(schemaVersion.getSchema()));

		xmlStreamReader = getXmlStreamReader(encoding);

		fileLength = -1;
		parsedCount = 0;
	}

	// =========================================================================

	public void unmarshal() throws JAXBException, XMLStreamException
	{
		try
		{
			unmarshaller.unmarshal(xmlStreamReader);
		}
		finally
		{
			closeStreams();
		}
	}

	@Override
	public void close() throws IOException
	{
		closeStreams();
	}

	private void closeStreams()
	{
		IOUtils.closeQuietly(decompressedInputStream);
		IOUtils.closeQuietly(compressedInputStream);
		IOUtils.closeQuietly(dumpInputStream);
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

	protected abstract void processPage(Object mediaWiki, Object page);

	protected boolean processRevision(Object page, Object revision)
	{
		// Add by default
		return true;
	}

	protected boolean processEvent(
			ValidationEvent ve,
			ValidationEventLocator vel)
	{
		logger.warn(String.format(
				"%s:%d:%d: %s",
				dumpUri,
				vel.getLineNumber(),
				vel.getColumnNumber(),
				ve.getMessage()));

		return true;
	}

	// =========================================================================

	private void handlePage(Object mediaWiki, Object page)
	{
		++parsedCount;

		processPage(mediaWiki, page);
	}

	private boolean handleRevision(Object page, Object revision)
	{
		return processRevision(page, revision);
	}

	protected boolean handleEvent(ValidationEvent ve, ValidationEventLocator vel)
	{
		return processEvent(ve, vel);
	}

	// =========================================================================

	private void getDumpInputStream() throws IOException
	{
		InputStream decomp;
		if (dumpUri.endsWith(".bz2"))
		{
			decompress = true;

			compressedInputStream = new CountingInputStream(dumpInputStream);

			decomp = new BZip2CompressorInputStream(compressedInputStream, true);
		}
		else if (dumpUri.endsWith(".gz"))
		{
			decompress = true;

			compressedInputStream = new CountingInputStream(dumpInputStream);

			decomp = new GzipCompressorInputStream(compressedInputStream);
		}
		else
		{
			decompress = false;

			decomp = dumpInputStream;
		}

		decompressedInputStream = new CountingInputStream(
				new BufferedInputStream(decomp, LOOKAHEAD));
	}

	private ExportSchemaVersion determineExportVersion() throws IOException
	{
		byte[] b = new byte[LOOKAHEAD];

		decompressedInputStream.mark(LOOKAHEAD);
		int read = decompressedInputStream.read(b, 0, LOOKAHEAD);
		decompressedInputStream.reset();

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
		else if (header.contains("xmlns=\"http://www.mediawiki.org/xml/export-0.9/\""))
		{
			return ExportSchemaVersion.V0_9;
		}
		else if (header.contains("xmlns=\"http://www.mediawiki.org/xml/export-0.10/\""))
		{
			return ExportSchemaVersion.V0_10;
		}
		else
		{
			throw new IllegalArgumentException("Unknown xmlns");
		}
	}

	/**
	 * The xerces UTF8Reader is broken. If the xerces XML parser is given an
	 * input stream, it will instantiate a reader for the encoding found in the
	 * XML file, a UTF8Reader in case of an UTF8 encoded XML file. Sadly, this
	 * UTF8Reader crashes for certain input (not sure why exactly).
	 * 
	 * On the other hand, when given a reader, which is forced to work with a
	 * certain encoding, the xerces XML parser does not have this freedom and
	 * will apparently process Wikipedia dumps just fine.
	 * 
	 * Therefore, in case you have trouble to parse a XML file, by specifying an
	 * encoding, you force the use of a Reader and can circumvent the crash.
	 */
	private XMLStreamReader getXmlStreamReader(Charset encoding) throws FactoryConfigurationError, XMLStreamException, UnsupportedEncodingException
	{
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

		if (encoding != null)
		{
			InputStreamReader isr = new InputStreamReader(decompressedInputStream, encoding);
			return xmlInputFactory.createXMLStreamReader(isr);
		}
		else
		{
			return xmlInputFactory.createXMLStreamReader(decompressedInputStream);
		}
	}

	private void setSchema(URL schemaUrl) throws SAXException, JAXBException
	{
		SchemaFactory sf = SchemaFactory.newInstance(
				javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);

		sf.setResourceResolver(new LSResourceResolverImplementation());

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
		final DumpReaderListener pageListener = new DumpReaderListener()
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

			@Override
			public boolean handleRevisionOrUploadOrLogitem(
					Object page,
					Object revision)
			{
				try
				{
					return DumpReader.this.handleRevision(page, revision);
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
