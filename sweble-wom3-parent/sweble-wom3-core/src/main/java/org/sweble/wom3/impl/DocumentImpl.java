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

import org.sweble.wom3.Wom3Attribute;
import org.sweble.wom3.Wom3Document;
import org.sweble.wom3.Wom3DocumentFragment;
import org.sweble.wom3.Wom3ElementNode;
import org.sweble.wom3.Wom3Node;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;

public class DocumentImpl
		extends
			BackboneWithChildren
		implements
			Wom3Document
{
	private static final long serialVersionUID = 1L;

	private final DomImplementationImpl impl;

	private Wom3ElementNode root;

	/**
	 * As long as we don't support creating doctypes or appending them to this
	 * document, this variable will always be null.
	 */
	private final DocumentType doctype = null;

	private String documentUri;

	/**
	 * Always null for now.
	 */
	private final String xmlEncoding = null;

	/**
	 * Always null for now.
	 */
	private final String inputEncoding = null;

	private boolean strictErrorChecking = true;

	private boolean readOnly = false;

	// =========================================================================

	public DocumentImpl()
	{
		this(DomImplementationImpl.get());
	}

	public DocumentImpl(DomImplementationImpl impl)
	{
		super(null);
		this.impl = impl;
	}

	// =========================================================================

	@Override
	public String getNodeName()
	{
		return "#document";
	}

	@Override
	public short getNodeType()
	{
		return Node.DOCUMENT_NODE;
	}

	@Override
	public Backbone getParentNode()
	{
		return null;
	}

	// =========================================================================
	// org.w3c.dom.Document - Getters

	@Override
	public DomImplementationImpl getImplementation()
	{
		return impl;
	}

	@Override
	public DocumentType getDoctype()
	{
		return doctype;
	}

	@Override
	public Wom3ElementNode getDocumentElement()
	{
		return root;
	}

	@Override
	public Element getElementById(String elementId)
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getTextContent() throws DOMException
	{
		return null;
	}

	// =========================================================================
	// org.w3c.dom.Document - Document URI

	@Override
	public String getDocumentURI()
	{
		return documentUri;
	}

	@Override
	public void setDocumentURI(String documentURI)
	{
		this.documentUri = documentURI;
	}

	// =========================================================================
	// org.w3c.dom.Document - Creation

	@Override
	public AttributeImpl createAttribute(String name) throws DOMException
	{
		return new AttributeImpl(this, name);
	}

	@Override
	public AttributeImpl createAttributeNS(
			String namespaceURI,
			String qualifiedName) throws DOMException
	{
		return new AttributeNsImpl(this, namespaceURI, qualifiedName);
	}

	@Override
	public XmlCommentImpl createComment(String data)
	{
		return new XmlCommentImpl(this, data);
	}

	@Override
	public Wom3ElementNode createElement(String tagName) throws DOMException
	{
		return impl.createElement(this, tagName);
	}

	@Override
	public Wom3ElementNode createElementNS(
			String namespaceURI,
			String qualifiedName) throws DOMException
	{
		return impl.createElement(this, namespaceURI, qualifiedName);
	}

	@Override
	public XmlTextImpl createTextNode(String data)
	{
		return new XmlTextImpl(this, data);
	}

	@Override
	public CDATASection createCDATASection(String data) throws DOMException
	{
		return new CdataSection(this, data);
	}

	@Override
	public Wom3DocumentFragment createDocumentFragment()
	{
		return new DocumentFragmentImpl(this);
	}

	@Override
	public EntityReference createEntityReference(String name) throws DOMException
	{
		// TODO: Implement
		throw new UnsupportedOperationException();
	}

	@Override
	public ProcessingInstruction createProcessingInstruction(
			String target,
			String data) throws DOMException
	{
		// TODO: Implement
		throw new UnsupportedOperationException();
	}

	// =========================================================================
	// org.w3c.dom.Document - Node adoption

	@Override
	public Wom3Node adoptNode(Node source) throws DOMException
	{
		if (!isSameNode(source.getOwnerDocument()))
		{
			if (source.getNodeType() == Node.ATTRIBUTE_NODE)
			{
				Wom3Attribute attr = (Wom3Attribute) source;
				if (attr.getOwnerElement() != null)
				{
					// Only the adopted node's document has to be writable
					Toolbox.expectType(Backbone.class, source).assertWritableOnDocument();

					attr.getOwnerElement().removeAttributeNode(attr);
				}
			}
			else
			{
				if (source.getParentNode() != null)
				{
					// Only the adopted node's document has to be writable
					Toolbox.expectType(Backbone.class, source).assertWritableOnDocument();

					source.getParentNode().removeChild(source);
				}
			}

			adoptRecursively(source);
		}
		return (Wom3Node) source;
	}

	private void adoptRecursively(Node source_)
	{
		Backbone source = Toolbox.expectType(Backbone.class, source_);
		switch (source.getNodeType())
		{
			case ATTRIBUTE_NODE:
				source.adoptTo(this);
				break;
			case ELEMENT_NODE:
				source.adoptTo(this);
				for (Wom3Node child : source.getWomAttributes())
					adoptRecursively(child);
				for (Wom3Node child : source.getWomChildNodes())
					adoptRecursively(child);
				break;
			case TEXT_NODE:
				source.adoptTo(this);
				break;

			case DOCUMENT_FRAGMENT_NODE:
			case ENTITY_REFERENCE_NODE:
				// Can be adopted (specifics!), only we don't ...
				// Fall through

			case PROCESSING_INSTRUCTION_NODE:
			case CDATA_SECTION_NODE:
			case COMMENT_NODE:
				// Can all be adopted (no specifics), only we don't ...
				// Fall through

			case DOCUMENT_NODE:
			case DOCUMENT_TYPE_NODE:
			case ENTITY_NODE:
			case NOTATION_NODE:
				// Cannot be adopted
				// Fall through

			default:
				throw new UnsupportedOperationException("Cannot clone node: " + source.getNodeName());
		}
	}

	@Override
	public Node importNode(Node importedNode, boolean deep) throws DOMException
	{
		assertWritableOnDocument();
		// TODO: Implement
		throw new UnsupportedOperationException();
	}

	// =========================================================================
	// org.w3c.dom.Document - Error checking

	@Override
	public boolean getStrictErrorChecking()
	{
		return strictErrorChecking;
	}

	@Override
	public void setStrictErrorChecking(boolean strictErrorChecking)
	{
		this.strictErrorChecking = strictErrorChecking;
	}

	// =========================================================================
	// Wom3Document - Read Only

	@Override
	public boolean getReadOnly()
	{
		return readOnly;
	}

	@Override
	public void setReadOnly(boolean readOnly)
	{
		this.readOnly = readOnly;
	}

	// =========================================================================
	// org.w3c.dom.Document - XML

	@Override
	public boolean getXmlStandalone()
	{
		return false;
	}

	@Override
	public String getXmlVersion()
	{
		return "1.1";
	}

	@Override
	public void setXmlStandalone(boolean xmlStandalone) throws DOMException
	{
		assertWritableOnDocument();
		throw new UnsupportedOperationException();
	}

	@Override
	public void setXmlVersion(String xmlVersion) throws DOMException
	{
		assertWritableOnDocument();
		throw new UnsupportedOperationException();
	}

	// =========================================================================
	// org.w3c.dom.Document - Encoding

	@Override
	public String getXmlEncoding()
	{
		return xmlEncoding;
	}

	@Override
	public String getInputEncoding()
	{
		return inputEncoding;
	}

	// =========================================================================
	// org.w3c.dom.Document - Document normalization

	@Override
	public DOMConfiguration getDomConfig()
	{
		// TODO: Implement
		throw new UnsupportedOperationException();
	}

	@Override
	public void normalizeDocument()
	{
		assertWritableOnDocument();
		// TODO: Implement
		throw new UnsupportedOperationException();
	}

	// =========================================================================
	// org.w3c.dom.Document - Node renaming

	@Override
	public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException
	{
		assertWritableOnDocument();
		// TODO: Implement
		throw new UnsupportedOperationException();
	}

	// =========================================================================

	@Override
	public Wom3Node cloneNode(boolean deep)
	{
		throw new UnsupportedOperationException();
	}

	// =========================================================================

	@Override
	protected void allowsInsertion(Backbone prev, Backbone child)
	{
		if (prev == null && root == null && child instanceof Wom3ElementNode)
			return;
		doesNotAllowInsertion(prev, child);
	}

	@Override
	protected void allowsRemoval(Backbone child)
	{
	}

	@Override
	protected void allowsReplacement(Backbone oldChild, Backbone newChild)
	{
		if (oldChild == root && newChild instanceof Wom3ElementNode)
			return;
		doesNotAllowInsertion(oldChild, newChild);
	}

	@Override
	protected void childInserted(Backbone prev, Backbone added)
	{
		if (added instanceof Wom3ElementNode)
			root = (Wom3ElementNode) added;
	}

	@Override
	protected void childRemoved(Backbone prev, Backbone removed)
	{
		if (removed == root)
			root = null;
	}

	@Override
	public NodeList getElementsByTagName(String tagname)
	{
		return new ElementsByTagNameNodeList(this, tagname);
	}

	@Override
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName)
	{
		// TODO: Implement
		throw new UnsupportedOperationException();
	}
}
