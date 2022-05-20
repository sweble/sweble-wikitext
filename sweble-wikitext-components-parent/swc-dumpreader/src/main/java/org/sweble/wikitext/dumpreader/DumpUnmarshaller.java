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
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.sweble.wikitext.dumpreader.model.DumpConverter;
import org.sweble.wikitext.dumpreader.model.Page;
import org.sweble.wikitext.dumpreader.model.Revision;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DumpUnmarshaller
		implements
			Unmarshaller
{
	public static final String CATALOG = "/catalog.xml";

	private final Unmarshaller unmarshaller;

	private final ExportSchemaVersion version;

	private final JAXBContext context;

	private DumpConverter converter;

	// =========================================================================

	public DumpUnmarshaller(ExportSchemaVersion version, boolean enableSchema) throws JAXBException, SAXException
	{
		this.version = version;

		this.context = JAXBContext.newInstance(
				version.getContextPath());

		this.unmarshaller = context.createUnmarshaller();

		if (enableSchema)
			setSchema(getClass().getResource(version.getFragmentsSchema()));
	}

	// =========================================================================

	public ExportSchemaVersion getVersion()
	{
		return version;
	}

	public JAXBContext getContext()
	{
		return context;
	}

	public Unmarshaller getUnmarshaller()
	{
		return unmarshaller;
	}

	public DumpConverter getConverter()
	{
		if (converter == null)
			converter = new DumpConverter();
		return converter;
	}

	public void setSchema(URL schemaUrl) throws SAXException
	{
		if (schemaUrl == null)
			throw new IllegalArgumentException();

		SchemaFactory sf = SchemaFactory
				.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);

		sf.setResourceResolver(new LSResourceResolverImplementation());

		unmarshaller.setSchema(sf.newSchema(schemaUrl));
	}

	// =========================================================================

	public <T> T unmarshalToPageType(Node node, Class<T> pageType) throws JAXBException
	{
		if (pageType != version.getPageType())
			throw new IllegalArgumentException("Expected pageType == " + version.getPageType().getName());
		@SuppressWarnings("unchecked")
		T value = (T) unmarshaller.unmarshal(node, version.getPageType()).getValue();
		return value;
	}

	public <T> T unmarshalToPageType(Source source, Class<T> pageType) throws JAXBException
	{
		if (pageType != version.getPageType())
			throw new IllegalArgumentException("Expected pageType == " + version.getPageType().getName());
		@SuppressWarnings("unchecked")
		T value = (T) unmarshaller.unmarshal(source, version.getPageType()).getValue();
		return value;
	}

	public <T> T unmarshalToPageType(XMLStreamReader reader, Class<T> pageType) throws JAXBException
	{
		if (pageType != version.getPageType())
			throw new IllegalArgumentException("Expected pageType == " + version.getPageType().getName());
		@SuppressWarnings("unchecked")
		T value = (T) unmarshaller.unmarshal(reader, version.getPageType()).getValue();
		return value;
	}

	public <T> T unmarshalToPageType(XMLEventReader reader, Class<T> pageType) throws JAXBException
	{
		if (pageType != version.getPageType())
			throw new IllegalArgumentException("Expected pageType == " + version.getPageType().getName());
		@SuppressWarnings("unchecked")
		T value = (T) unmarshaller.unmarshal(reader, version.getPageType()).getValue();
		return value;
	}

	// =========================================================================

	public Page unmarshalToPage(Node node) throws JAXBException
	{
		return getConverter().convertPage(
				unmarshaller.unmarshal(node, version.getPageType()).getValue());
	}

	public Page unmarshalToPage(Source source) throws JAXBException
	{
		return getConverter().convertPage(
				unmarshaller.unmarshal(source, version.getPageType()).getValue());
	}

	public Page unmarshalToPage(XMLStreamReader reader) throws JAXBException
	{
		return getConverter().convertPage(
				unmarshaller.unmarshal(reader, version.getPageType()).getValue());
	}

	public Page unmarshalToPage(XMLEventReader reader) throws JAXBException
	{
		return getConverter().convertPage(
				unmarshaller.unmarshal(reader, version.getPageType()).getValue());
	}

	// =========================================================================

	public <T> T unmarshalToRevisionType(Node node, Class<T> revisionType) throws JAXBException
	{
		if (revisionType != version.getRevisionType())
			throw new IllegalArgumentException("Expected revisionType == " + version.getRevisionType().getName());
		@SuppressWarnings("unchecked")
		T value = (T) unmarshaller.unmarshal(node, version.getRevisionType()).getValue();
		return value;
	}

	public <T> T unmarshalToRevisionType(Source source, Class<T> revisionType) throws JAXBException
	{
		if (revisionType != version.getRevisionType())
			throw new IllegalArgumentException("Expected revisionType == " + version.getRevisionType().getName());
		@SuppressWarnings("unchecked")
		T value = (T) unmarshaller.unmarshal(source, version.getRevisionType()).getValue();
		return value;
	}

	public <T> T unmarshalToRevisionType(
			XMLStreamReader reader,
			Class<T> revisionType) throws JAXBException
	{
		if (revisionType != version.getRevisionType())
			throw new IllegalArgumentException("Expected revisionType == " + version.getRevisionType().getName());
		@SuppressWarnings("unchecked")
		T value = (T) unmarshaller.unmarshal(reader, version.getRevisionType()).getValue();
		return value;
	}

	public <T> T unmarshalToRevisionType(
			XMLEventReader reader,
			Class<T> revisionType) throws JAXBException
	{
		if (revisionType != version.getRevisionType())
			throw new IllegalArgumentException("Expected revisionType == " + version.getRevisionType().getName());
		@SuppressWarnings("unchecked")
		T value = (T) unmarshaller.unmarshal(reader, version.getRevisionType()).getValue();
		return value;
	}

	// =========================================================================

	public Revision unmarshalToRevision(Node node) throws JAXBException
	{
		return getConverter().convertRevision(
				unmarshaller.unmarshal(node, version.getRevisionType()).getValue());
	}

	public Revision unmarshalToRevision(Source source) throws JAXBException
	{
		return getConverter().convertRevision(
				unmarshaller.unmarshal(source, version.getRevisionType()).getValue());
	}

	public Revision unmarshalToRevision(XMLStreamReader reader) throws JAXBException
	{
		return getConverter().convertRevision(
				unmarshaller.unmarshal(reader, version.getRevisionType()).getValue());
	}

	public Revision unmarshalToRevision(XMLEventReader reader) throws JAXBException
	{
		return getConverter().convertRevision(
				unmarshaller.unmarshal(reader, version.getRevisionType()).getValue());
	}

	// =========================================================================

	public Object unmarshal(File f) throws JAXBException
	{
		return unmarshaller.unmarshal(f);
	}

	public Object unmarshal(InputStream is) throws JAXBException
	{
		return unmarshaller.unmarshal(is);
	}

	public Object unmarshal(Reader reader) throws JAXBException
	{
		return unmarshaller.unmarshal(reader);
	}

	public Object unmarshal(URL url) throws JAXBException
	{
		return unmarshaller.unmarshal(url);
	}

	public Object unmarshal(InputSource source) throws JAXBException
	{
		return unmarshaller.unmarshal(source);
	}

	public Object unmarshal(Node node) throws JAXBException
	{
		return unmarshaller.unmarshal(node);
	}

	public <T> JAXBElement<T> unmarshal(Node node, Class<T> declaredType) throws JAXBException
	{
		return unmarshaller.unmarshal(node, declaredType);
	}

	public Object unmarshal(Source source) throws JAXBException
	{
		return unmarshaller.unmarshal(source);
	}

	public <T> JAXBElement<T> unmarshal(Source source, Class<T> declaredType) throws JAXBException
	{
		return unmarshaller.unmarshal(source, declaredType);
	}

	public Object unmarshal(XMLStreamReader reader) throws JAXBException
	{
		return unmarshaller.unmarshal(reader);
	}

	public <T> JAXBElement<T> unmarshal(
			XMLStreamReader reader,
			Class<T> declaredType) throws JAXBException
	{
		return unmarshaller.unmarshal(reader, declaredType);
	}

	public Object unmarshal(XMLEventReader reader) throws JAXBException
	{
		return unmarshaller.unmarshal(reader);
	}

	public <T> JAXBElement<T> unmarshal(
			XMLEventReader reader,
			Class<T> declaredType) throws JAXBException
	{
		return unmarshaller.unmarshal(reader, declaredType);
	}

	public UnmarshallerHandler getUnmarshallerHandler()
	{
		return unmarshaller.getUnmarshallerHandler();
	}

	/**
	 * @deprecated since JAXB2.0, please see
	 *             {@link #setSchema(javax.xml.validation.Schema)}
	 */
	public void setValidating(boolean validating) throws JAXBException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated since JAXB2.0, please see
	 *             {@link #setSchema(javax.xml.validation.Schema)}
	 */
	public boolean isValidating() throws JAXBException
	{
		throw new UnsupportedOperationException();
	}

	public void setEventHandler(ValidationEventHandler handler) throws JAXBException
	{
		unmarshaller.setEventHandler(handler);
	}

	public ValidationEventHandler getEventHandler() throws JAXBException
	{
		return unmarshaller.getEventHandler();
	}

	public void setProperty(String name, Object value) throws PropertyException
	{
		unmarshaller.setProperty(name, value);
	}

	public Object getProperty(String name) throws PropertyException
	{
		return unmarshaller.getProperty(name);
	}

	public void setSchema(Schema schema)
	{
		unmarshaller.setSchema(schema);
	}

	public Schema getSchema()
	{
		return unmarshaller.getSchema();
	}

	public void setAdapter(@SuppressWarnings("rawtypes") XmlAdapter adapter)
	{
		unmarshaller.setAdapter(adapter);
	}

	@SuppressWarnings("rawtypes")
	public <A extends XmlAdapter> void setAdapter(Class<A> type, A adapter)
	{
		unmarshaller.setAdapter(type, adapter);
	}

	@SuppressWarnings("rawtypes")
	public <A extends XmlAdapter> A getAdapter(Class<A> type)
	{
		return unmarshaller.getAdapter(type);
	}

	public void setAttachmentUnmarshaller(AttachmentUnmarshaller au)
	{
		unmarshaller.setAttachmentUnmarshaller(au);
	}

	public AttachmentUnmarshaller getAttachmentUnmarshaller()
	{
		return unmarshaller.getAttachmentUnmarshaller();
	}

	public void setListener(Listener listener)
	{
		unmarshaller.setListener(listener);
	}

	public Listener getListener()
	{
		return unmarshaller.getListener();
	}
}
