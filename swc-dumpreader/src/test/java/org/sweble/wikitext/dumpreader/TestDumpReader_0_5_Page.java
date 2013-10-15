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

import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.xerces.util.XMLCatalogResolver;
import org.junit.Before;
import org.junit.Test;
import org.sweble.wikitext.dumpreader.export_0_5.PageType;

public class TestDumpReader_0_5_Page
{
	@Before
	public void setUp()
	{
	}
	
	@Test
	public void testExport() throws Throwable
	{
		JAXBContext context = JAXBContext.newInstance(
				"org.sweble.wikitext.dumpreader.export_0_5");
		
		Unmarshaller unmarshaller = context.createUnmarshaller();
		
		setSchema(
				unmarshaller,
				TestDumpReader_0_5_Page.class.getResource("/catalog.xml"),
				TestDumpReader_0_5_Page.class.getResource("/export-0.5-page.xsd"));
		
		InputStream xmlInputStream = getClass().getResourceAsStream("/input-0.5-page.xml");
		Source source = new StreamSource(xmlInputStream);
		
		@SuppressWarnings("unused")
		Object result = unmarshaller.unmarshal(source, PageType.class);
		
		xmlInputStream.close();
	}
	
	private void setSchema(Unmarshaller unmarshaller, URL catalog, URL schemaUrl) throws Exception
	{
		SchemaFactory sf = SchemaFactory
				.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
		sf.setResourceResolver(new XMLCatalogResolver(
				new String[] { catalog.getFile() }));
		
		Schema schema = sf.newSchema(schemaUrl);
		
		unmarshaller.setSchema(schema);
	}
}
