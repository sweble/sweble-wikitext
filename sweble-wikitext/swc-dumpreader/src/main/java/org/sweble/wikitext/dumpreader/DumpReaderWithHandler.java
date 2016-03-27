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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.xml.sax.SAXException;

public class DumpReaderWithHandler
		extends
			DumpReader
{
	private final DumpReaderListener listener;

	// =========================================================================

	public DumpReaderWithHandler(
			InputStream is,
			Charset encoding,
			DumpReaderListener listener,
			String url,
			Logger logger,
			boolean useSchema) throws JAXBException, FactoryConfigurationError, XMLStreamException, IOException, SAXException
	{
		super(is, encoding, url, logger, useSchema);
		this.listener = listener;
	}

	// =========================================================================

	@Override
	protected void processPage(Object mediaWiki, Object page)
	{
		listener.handlePage(mediaWiki, page);
	}

	@Override
	protected boolean processRevision(Object page, Object revision)
	{
		return listener.handleRevisionOrUploadOrLogitem(page, revision);
	}
}
