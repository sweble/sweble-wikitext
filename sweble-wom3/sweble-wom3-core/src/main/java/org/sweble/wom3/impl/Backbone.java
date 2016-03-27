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

import java.util.Collection;
import java.util.Iterator;

import org.sweble.wom3.Wom3Attribute;
import org.sweble.wom3.Wom3Document;
import org.sweble.wom3.Wom3Node;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

import de.fau.cs.osr.utils.StringTools;

public abstract class Backbone
		implements
			Wom3Node
{
	private static final long serialVersionUID = 1L;

	protected transient int childrenChanges = 0;

	private DocumentImpl owner;

	private Backbone parent;

	private Backbone prevSibling;

	private Backbone nextSibling;

	private BackboneUserData userData;

	// =========================================================================

	public Backbone(DocumentImpl owner)
	{
		this.owner = owner;
	}

	// =========================================================================
	// 
	// DOM Level 3 Node Interface Attributes
	// 
	// =========================================================================

	@Override
	public abstract String getNodeName();

	@Override
	public String getNodeValue() throws DOMException
	{
		return null;
	}

	@Override
	public void setNodeValue(String nodeValue) throws DOMException
	{
		// Doing nothing is the sensible default

		// Unless we are in read-only mode...
		assertWritableOnDocument();
	}

	@Override
	public abstract short getNodeType();

	@Override
	public Wom3Document getOwnerDocument()
	{
		return owner;
	}

	/**
	 * The {@code parent} attribute of a {@link Backbone} instance is always
	 * properly set (can be {@code null}) but not all node types (especially
	 * {@link AttributeBase}) return it when getParentNode() is called and
	 * always return {@code null} instead. The {@code parent} attribute can only
	 * be altered by the {@code Backbone} class.
	 */
	@Override
	public abstract Backbone getParentNode();

	@Override
	public Backbone getFirstChild()
	{
		return null;
	}

	@Override
	public Backbone getLastChild()
	{
		return null;
	}

	@Override
	public Backbone getPreviousSibling()
	{
		return prevSibling;
	}

	@Override
	public Backbone getNextSibling()
	{
		return nextSibling;
	}

	@Override
	public NodeList getChildNodes()
	{
		return new NodeListImpl(this);
	}

	@Override
	public NamedNodeMap getAttributes()
	{
		return null;
	}

	@Override
	public String getNamespaceURI()
	{
		return null;
	}

	@Override
	public String getPrefix()
	{
		// DOM Level 1 implementation
		return null;
	}

	@Override
	public void setPrefix(String prefix) throws DOMException
	{
		// DOM Level 1 implementation
		// Do nothing since it's defined to be null for all types of nodes.

		// Unless we are in read-only mode...
		assertWritableOnDocument();
	}

	@Override
	public String getLocalName()
	{
		// DOM Level 1 implementation
		return null;
	}

	@Override
	public final String getBaseURI()
	{
		// We don't support this attribute.
		return null;
	}

	@Override
	public String getTextContent() throws DOMException
	{
		if (!hasChildNodes())
			return "";

		if (getFirstChild() == getLastChild())
			return getFirstChild().getTextContent();

		StringBuilder b = new StringBuilder();
		getTextContentRecursive(b);
		return b.toString();
	}

	// =========================================================================
	// 
	// DOM Level 3 Node Interface Operations
	// 
	// =========================================================================

	@Override
	public boolean hasAttributes()
	{
		return getFirstAttr() != null;
	}

	@Override
	public boolean hasChildNodes()
	{
		return getFirstChild() != null;
	}

	@Override
	public Wom3Node insertBefore(Node child_, Node before_) throws DOMException
	{
		return doesNotSupportChildNodes();
	}

	@Override
	public Wom3Node replaceChild(Node newChild_, Node oldChild_) throws DOMException
	{
		return doesNotSupportChildNodes();
	}

	@Override
	public Wom3Node removeChild(Node child_) throws DOMException
	{
		return doesNotSupportChildNodes();
	}

	@Override
	public Wom3Node appendChild(Node child_) throws DOMException
	{
		return doesNotSupportChildNodes();
	}

	@Override
	public void normalize()
	{
		// TODO: Implement
		throw new UnsupportedOperationException();

		//assertWritable();
	}

	@Override
	public String lookupPrefix(String namespaceURI)
	{
		// TODO: Implement
		throw new UnsupportedOperationException();
	}

	@Override
	public String lookupNamespaceURI(String prefix)
	{
		// TODO: Implement
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDefaultNamespace(String namespaceURI)
	{
		// FIXME: This does not look like the correct implementation for this method?!
		return (namespaceURI != null && namespaceURI.equals(WOM_NS_URI));
	}

	@Override
	public boolean isSameNode(Node other)
	{
		return other == this;
	}

	@Override
	public boolean isEqualNode(Node arg)
	{
		// TODO: Implement
		throw new UnsupportedOperationException();
	}

	@Override
	public short compareDocumentPosition(Node otherNode) throws DOMException
	{
		Node thisNode = this;

		if (thisNode.isSameNode(otherNode))
			return 0;

		if (!(otherNode instanceof Backbone))
			throw new DOMException(
					DOMException.NOT_SUPPORTED_ERR,
					"Cannot compare nodes from different implementations");

		Document thisOwner = getOwnerDocument();
		if (thisOwner == null)
		{
			assert thisNode.getNodeType() == Node.DOCUMENT_NODE;
			thisOwner = (Document) thisNode;
		}

		Document otherOwner = otherNode.getOwnerDocument();
		if (otherOwner == null)
		{
			assert otherNode.getNodeType() == Node.DOCUMENT_NODE;
			otherOwner = (Document) otherNode;
		}

		if (thisOwner == null || !thisOwner.isSameNode(otherOwner))
			throw new DOMException(
					DOMException.NOT_SUPPORTED_ERR,
					"Cannot compare nodes from different documents");

		int thisDepth = 1;
		Node thisAncestor = thisNode;
		{
			for (Node n = thisNode.getParentNode(); n != null; n = n.getParentNode())
			{
				if (n.isSameNode(otherNode))
					return DOCUMENT_POSITION_CONTAINS | DOCUMENT_POSITION_PRECEDING;
				thisAncestor = n;
				++thisDepth;
			}
		}

		int otherDepth = 1;
		Node otherAncestor = otherNode;
		{
			for (Node n = otherNode.getParentNode(); n != null; n = n.getParentNode())
			{
				if (n.isSameNode(thisNode))
					return DOCUMENT_POSITION_CONTAINED_BY | DOCUMENT_POSITION_FOLLOWING;
				otherAncestor = n;
				++otherDepth;
			}
		}

		if (thisAncestor.getNodeType() == Node.ATTRIBUTE_NODE)
		{
			Wom3Attribute thisAncestorAttr = (Wom3Attribute) thisAncestor;
			if (otherAncestor.getNodeType() == Node.ATTRIBUTE_NODE)
			{
				Wom3Attribute otherAncestorAttr = (Wom3Attribute) otherAncestor;
				Element thisOwnerE = thisAncestorAttr.getOwnerElement();
				Element otherOwnerE = otherAncestorAttr.getOwnerElement();
				if (thisOwnerE == null || otherOwnerE == null)
				{
					return DOCUMENT_POSITION_DISCONNECTED;
				}
				else if (thisOwnerE.isSameNode(otherOwnerE))
				{
					return (short) (((Backbone) otherAncestorAttr).precedes(thisAncestorAttr) ?
							(DOCUMENT_POSITION_PRECEDING | DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC) :
							(DOCUMENT_POSITION_FOLLOWING | DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC));
				}
				else
					; // Fall through
			}

			// otherNode is not an attribute or both are attributes but
			// of different owner nodes. In both cases we compare the owner 
			// nodes instead of the attributes

			// This time we start at 0 and not with the parent node to catch the 
			// case in which "otherNode" is the element that owns the attribute 
			// "thisNode". 
			thisDepth = 0;
			thisNode = thisAncestorAttr.getOwnerElement();
			{
				for (Node n = thisNode; n != null; n = n.getParentNode())
				{
					if (n.isSameNode(otherNode))
						return DOCUMENT_POSITION_CONTAINS | DOCUMENT_POSITION_PRECEDING;
					thisAncestor = n;
					++thisDepth;
				}
			}
		}

		if (otherAncestor.getNodeType() == Node.ATTRIBUTE_NODE)
		{
			Wom3Attribute otherAncestorAttr = (Wom3Attribute) otherAncestor;

			// If we get here either thisNode is not an attribute or both are
			// attributes but of different owner nodes. In both cases we compare 
			// the owner nodes instead of the attributes

			// This time we start at 0 and not with the parent node to catch the 
			// case in which "otherNode" is the element that owns the attribute 
			// "thisNode". 
			otherDepth = 0;
			otherNode = otherAncestorAttr.getOwnerElement();
			{
				for (Node n = otherNode; n != null; n = n.getParentNode())
				{
					if (n.isSameNode(thisNode))
						return DOCUMENT_POSITION_CONTAINED_BY | DOCUMENT_POSITION_FOLLOWING;
					otherAncestor = n;
					++otherDepth;
				}
			}
		}

		if (thisAncestor != otherAncestor || thisAncestor != thisOwner)
			throw new RuntimeException("This should not happen...");

		if (thisDepth > otherDepth)
		{
			for (int i = 0; i < thisDepth - otherDepth; i++)
			{
				thisNode = thisNode.getParentNode();
				if (thisNode.isSameNode(otherNode))
					return DOCUMENT_POSITION_PRECEDING;
			}
		}
		else
		{
			for (int i = 0; i < otherDepth - thisDepth; i++)
			{
				otherNode = otherNode.getParentNode();
				if (otherNode.isSameNode(thisNode))
					return DOCUMENT_POSITION_FOLLOWING;
			}
		}

		// Find common ancestor
		Node thisNode2 = thisNode.getParentNode();
		Node otherNode2 = otherNode.getParentNode();
		while (thisNode2 != otherNode2)
		{
			thisNode = thisNode2;
			thisNode2 = thisNode2.getParentNode();
			otherNode = otherNode2;
			otherNode2 = otherNode2.getParentNode();
		}

		// Find the leftmost.
		for (Node n = thisNode2.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if (n == otherNode)
				return DOCUMENT_POSITION_PRECEDING;
			if (n == thisNode)
				return DOCUMENT_POSITION_FOLLOWING;
		}

		throw new RuntimeException("This should not happen...");
	}

	@Override
	public boolean isSupported(String feature, String version)
	{
		// TODO: Implement
		return false;
	}

	@Override
	public Object getFeature(String feature, String version)
	{
		// TODO: Implement
		return null;
	}

	@Override
	public Object setUserData(String key, Object data, UserDataHandler handler)
	{
		assertWritableOnDocument();

		Object oldValue;
		if (data == null)
		{
			if (this.userData == null)
				return null;
			oldValue = this.userData.remove(key);
			if (this.userData.isEmpty())
				this.userData = null;
		}
		else
		{
			if (this.userData == null)
				this.userData = new BackboneUserData();
			oldValue = this.userData.set(key, data, handler);
		}
		return oldValue;
	}

	@Override
	public Object getUserData(String key)
	{
		if (this.userData == null)
			return null;
		return this.userData.get(key);
	}

	@Override
	public Wom3Node cloneNode(boolean deep)
	{
		Backbone newNode;
		try
		{
			newNode = (Backbone) this.clone();
		}
		catch (CloneNotSupportedException e)
		{
			// Wom3Nodes must always be cloneable
			throw new AssertionError("Internal error!");
		}

		// Detach from parent and siblings
		newNode.parent = null;
		newNode.prevSibling = null;
		newNode.nextSibling = null;
		newNode.userData = null;

		return newNode;
	}

	// =========================================================================
	// 
	// WOM Level 3 only Extensions
	// 
	// =========================================================================

	@Override
	public Collection<Wom3Node> getWomChildNodes()
	{
		return new ImmutableSiblingCollection<Wom3Node>(getFirstChild());
	}

	@Override
	public Iterator<Wom3Node> iterator()
	{
		return new ImmutableSiblingCollection<Wom3Node>(getFirstChild()).iterator();
	}

	@Override
	public Collection<Wom3Attribute> getWomAttributes()
	{
		return new ImmutableSiblingCollection<Wom3Attribute>(getFirstAttr());
	}

	@Override
	public Iterator<Wom3Attribute> attributeIterator()
	{
		return new ImmutableSiblingCollection<Wom3Attribute>(getFirstAttr()).iterator();
	}

	// =========================================================================

	/**
	 * Override in element nodes that can have attributes.
	 */
	protected Wom3Attribute getFirstAttr()
	{
		return null;
	}

	/**
	 * Override according to node type.
	 */
	protected void getTextContentRecursive(StringBuilder b)
	{
		// Implementation works for all nodes that either should return their 
		// nodeValue or null.
		String value = getNodeValue();
		if (value != null)
			b.append(value);
	}

	/**
	 * Override in text nodes which can potentially be content whitespace only.
	 */
	public boolean isContentWhitespace()
	{
		// TODO: replace with isElementContentWhitespace()!
		return false;
	}

	// =========================================================================

	protected void assertWritableOnDocument()
	{
		Wom3Document ownerDocument = getOwnerDocument();
		if (ownerDocument == null)
			ownerDocument = (Wom3Document) this;

		if (ownerDocument.getReadOnly())
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This document is read-only!");
	}

	protected void assertWritable(
			AttributeBase attributeBase,
			AttributeDescriptor descriptor)
	{
		// An uninitialized attribute value may be written once, even if read-only
		if ((attributeBase != null) /*&& (attributeBase.getValue() != null)*/)
		{
			if (descriptor.isReadOnly())
				throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "The attribute '" + attributeBase.getName() + "' is read-only!");
		}
	}

	protected Wom3Node doesNotSupportChildNodes() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException(
				"Node `" + getNodeName() + "' does not support child nodes!");
	}

	protected void adoptTo(DocumentImpl doc)
	{
		this.owner = doc;
	}

	protected boolean hasSameOwnerDocument(Backbone other)
	{
		Document thisOwner = getOwnerDocument();
		if (thisOwner == null)
			thisOwner = (Document) this;
		Document otherOwner = other.getOwnerDocument();
		if (otherOwner == null)
			otherOwner = (Document) other;
		return thisOwner.isSameNode(otherOwner);
	}

	protected boolean precedes(Wom3Node other)
	{
		for (Wom3Node n = this.getNextSibling(); n != null; n = n.getNextSibling())
		{
			if (n == other)
				return true;
		}
		return false;
	}

	protected boolean precedesOrIsSame(Wom3Node other)
	{
		for (Backbone n = this; n != null; n = n.getNextSibling())
		{
			if (n == other)
				return true;
		}
		return false;
	}

	// =========================================================================

	protected Backbone getParentNodeIntern()
	{
		return parent;
	}

	protected boolean isLinked()
	{
		return getParentNodeIntern() != null;
	}

	protected void unlink()
	{
		parent = null;

		if (prevSibling != null)
			prevSibling.nextSibling = nextSibling;
		if (nextSibling != null)
			nextSibling.prevSibling = prevSibling;

		prevSibling = null;
		nextSibling = null;
	}

	protected void link(
			Backbone parent,
			Backbone prevSibling,
			Backbone nextSibling)
	{
		if (!hasSameOwnerDocument(parent))
			throw new IllegalStateException(
					"Different owner documents.");

		if (isLinked())
			throw new IllegalStateException(
					"Node is still child of another WOM node.");

		this.parent = parent;

		this.prevSibling = prevSibling;
		if (prevSibling != null)
		{
			if (prevSibling.nextSibling != nextSibling)
				throw new IllegalStateException(
						"WOM sibling chain inconsistent.");

			prevSibling.nextSibling = this;
		}

		this.nextSibling = nextSibling;
		if (nextSibling != null)
		{
			if (nextSibling.prevSibling != prevSibling)
				throw new IllegalStateException(
						"WOM sibling chain inconsistent.");

			nextSibling.prevSibling = this;
		}
	}

	// =========================================================================

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}

	protected StringBuilder toString(StringBuilder b)
	{
		switch (getNodeType())
		{
			case Node.ENTITY_REFERENCE_NODE:
			case Node.ENTITY_NODE:
			case Node.PROCESSING_INSTRUCTION_NODE:
			case Node.DOCUMENT_TYPE_NODE:
			case Node.NOTATION_NODE:
				// We don't support these
				throw new InternalError();

			case Node.ATTRIBUTE_NODE:
				b.append(((Wom3Attribute) this).getName());
				b.append("=\"");
				b.append(StringTools.escHtml(getNodeValue()));
				b.append('"');
				break;

			case Node.COMMENT_NODE:
				b.append("<!--");
				b.append(getNodeValue());
				b.append("-->");
				break;

			case Node.DOCUMENT_FRAGMENT_NODE:
			case Node.DOCUMENT_NODE:
			case Node.ELEMENT_NODE:
				b.append('<');
				b.append(getNodeName());
				for (Wom3Attribute attr : getWomAttributes())
				{
					b.append(' ');
					if (attr instanceof Backbone)
						((Backbone) attr).toString(b);
					else
						b.append(attr.toString());
				}
				if (hasChildNodes())
				{
					b.append('>');
					for (Wom3Node child : getWomChildNodes())
					{
						if (child instanceof Backbone)
							((Backbone) child).toString(b);
						else
							b.append(child.toString());
					}
					b.append("</");
					b.append(getNodeName());
					b.append('>');
				}
				else
				{
					b.append(" />");
				}
				break;

			case Node.CDATA_SECTION_NODE:
				b.append("<![CDATA[");
				b.append(StringTools.escHtml(getNodeValue()));
				b.append("]]>");
				break;

			case Node.TEXT_NODE:
				b.append(StringTools.escHtml(getNodeValue()));
				break;
		}
		return b;
	}
}
