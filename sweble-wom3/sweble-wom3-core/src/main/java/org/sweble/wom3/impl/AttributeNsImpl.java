/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3.impl;

import org.w3c.dom.DOMException;

public class AttributeNsImpl
		extends
			AttributeImpl
{
	private static final long serialVersionUID = 1L;

	private static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";

	private static final String XML_URI = "http://www.w3.org/XML/1998/namespace";

	private String namespaceUri;

	private String localName;

	private String prefix;

	// =========================================================================

	public AttributeNsImpl(
			DocumentImpl owner,
			String namespaceUri,
			String qualifiedName)
	{
		super(owner);
		setNamespaceUri(namespaceUri);
		setPrefixAndLocalName(qualifiedName);
	}

	// =========================================================================

	@Override
	public String getNamespaceURI()
	{
		return namespaceUri;
	}

	@Override
	public String getLocalName()
	{
		return localName;
	}

	@Override
	public String getPrefix()
	{
		return prefix;
	}

	@Override
	public void setPrefix(String prefix) throws DOMException
	{
		if (prefix != null && !prefix.isEmpty())
		{
			setName(prefix + ":" + localName);
		}
		else
		{
			setName(localName);
		}
	}

	/**
	 * WOM / Non DOM
	 */
	@Override
	public String setName(String qualifiedName) throws IllegalArgumentException, NullPointerException
	{
		return setPrefixAndLocalName(qualifiedName);
	}

	// =========================================================================

	private void setNamespaceUri(String namespaceUri)
	{
		// DOM Level 3 Core
		// 1.3.3 XML Namespaces
		// In programming languages where empty strings can be differentiated 
		// from null, empty strings, when given as a namespace URI, are 
		// converted to null. This is true even though the DOM does no lexical 
		// checking of URIs.
		this.namespaceUri = namespaceUri;
		if (this.namespaceUri != null && this.namespaceUri.isEmpty())
			this.namespaceUri = null;
	}

	private String setPrefixAndLocalName(String qualifiedName)
	{
		String prefix = null;
		String localName = null;

		int colon = qualifiedName.indexOf(':');
		if (colon < 0)
		{
			// No prefix
			prefix = null;
			localName = qualifiedName;

			checkNamespaceUriAndName(namespaceUri, qualifiedName);
		}
		else
		{
			// We have a prefix
			if ((colon == 0)
					|| (colon == qualifiedName.length() - 1)
					|| (qualifiedName.indexOf(':', colon + 1) >= 0))
				throw new DOMException(
						DOMException.NAMESPACE_ERR,
						"Invalid qualified attribute name: " + qualifiedName);

			// We have a valid prefix
			prefix = qualifiedName.substring(0, colon);
			localName = qualifiedName.substring(colon + 1);

			checkNamespaceUriAndPrefix(prefix, namespaceUri);

			// FIXME: Must check according to XML version we have to support
			Toolbox.checkValidXmlName(prefix);
		}

		// FIXME: Must check according to XML version we have to support
		Toolbox.checkValidXmlName(localName);

		String old = this.getName();

		// Will do nothing if localName is unchanged.
		// This method does not mess with namespaceUri and a prefix change does not change the nature of an attribute.
		setNameIntern(namespaceUri, localName, qualifiedName);

		// Only set values after checks ran
		this.prefix = prefix;
		this.localName = localName;

		return old;
	}

	protected String setNameIntern(
			String namespaceUri,
			String localName,
			String name)
	{
		String old = getName();
		if ((this.localName != null) && this.localName.equals(localName))
			return old;

		validateNameChangeWithParent(namespaceUri, localName, name);

		return old;
	}

	private void validateNameChangeWithParent(
			String namespaceUri,
			String localName,
			String name)
	{
		BackboneElement parent = (BackboneElement) getOwnerElement();

		if (parent == null)
		{
			doSetName(name);
		}
		else
		{
			boolean exists = (localName != null) ?
					(parent.getAttributeNodeNS(namespaceUri, localName) != null) :
					(parent.getAttributeNode(name) != null);

			if (exists)
				throw new IllegalArgumentException("Attribute with this name " +
						"already exists for the corresponding element!");

			AttributeDescriptor oldDescriptor = parent.getAttributeDescriptorOrFail(
					getNamespaceURI(),
					getLocalName(),
					getName());

			AttributeDescriptor newDescriptor = parent.getAttributeDescriptorOrFail(
					namespaceUri,
					localName,
					name);

			validateNameChangeWithParent(name, parent, oldDescriptor, newDescriptor);
		}
	}

	private static void checkNamespaceUriAndName(
			String namespaceUri,
			String qualifiedName)
	{
		assert qualifiedName != null;

		if (qualifiedName.equals("xmlns") && !XMLNS_URI.equals(namespaceUri))
			throw new DOMException(
					DOMException.NAMESPACE_ERR,
					"An attribute with name 'xmlns' must have its its namespace URI set to '" + XMLNS_URI + "'");

		if (XMLNS_URI.equals(namespaceUri) && !qualifiedName.equals("xmlns"))
			throw new DOMException(
					DOMException.NAMESPACE_ERR,
					"An attribute with namespace URI '" + XMLNS_URI + "' must be called 'xmlns' or must have a prefix called 'xmlns'");
	}

	private static void checkNamespaceUriAndPrefix(
			String prefix,
			String namespaceUri)
	{
		assert prefix != null && namespaceUri != null;

		if (namespaceUri == null)
			throw new DOMException(
					DOMException.NAMESPACE_ERR,
					"A qualified (prefixed) attribute name requires a non-null namespace URI");

		if (prefix.equals("xml") && !namespaceUri.equals(XML_URI))
			throw new DOMException(
					DOMException.NAMESPACE_ERR,
					"An attribute with prefix 'xml' must have its namespace URI set to '" + XML_URI + "'");

		if (prefix.equals("xmlns") && !namespaceUri.equals(XMLNS_URI))
			throw new DOMException(
					DOMException.NAMESPACE_ERR,
					"An attribute with prefix 'xmlns' must have its its namespace URI set to '" + XMLNS_URI + "'");

		if (namespaceUri.equals(XMLNS_URI) && !prefix.equals("xmlns"))
			throw new DOMException(
					DOMException.NAMESPACE_ERR,
					"An attribute with namespace URI '" + XMLNS_URI + "' must be called 'xmlns' or must have a prefix called 'xmlns'");

		// TODO: Is it allowed to redefine the xmlns or xml prefixes?
	}
}
