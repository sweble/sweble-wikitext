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
import org.sweble.wom3.Wom3ElementNode;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3NodeList;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;

public abstract class BackboneElement
		extends
			BackboneWithChildren
		implements
			Wom3ElementNode
{
	private static final long serialVersionUID = 1L;

	private AttributeBase firstAttr;

	// =========================================================================

	public BackboneElement(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	@Override
	public abstract String getNodeName();

	@Override
	public final String getTagName()
	{
		return getNodeName();
	}

	@Override
	public final short getNodeType()
	{
		return Node.ELEMENT_NODE;
	}

	@Override
	public Backbone getParentNode()
	{
		return getParentNodeIntern();
	}

	@Override
	public TypeInfo getSchemaTypeInfo()
	{
		// TODO: Implement
		throw new UnsupportedOperationException();
	}

	// =========================================================================

	@Override
	public Wom3NodeList getElementsByTagName(String name)
	{
		return new ElementsByTagNameNodeList(this, name);
	}

	@Override
	public Wom3NodeList getElementsByTagNameNS(
			String namespaceURI,
			String localName) throws DOMException
	{
		return new ElementsByTagNameNodeList(this, namespaceURI, localName);
	}

	// =========================================================================

	@Override
	public Wom3Node cloneNode(boolean deep)
	{
		BackboneElement newNode = (BackboneElement) super.cloneNode(deep);

		newNode.firstAttr = null;

		// Attributes are integral part of an element and will always be cloned deeply
		AttributeBase child = this.getFirstAttr();
		while (child != null)
		{
			newNode.setAttributeNode((AttributeBase) child.cloneNode(deep), true /* cloning */);
			child = (AttributeBase) child.getNextSibling();
		}

		return newNode;
	}

	// =========================================================================

	@Override
	public AttributeBase getFirstAttr()
	{
		return firstAttr;
	}

	private final void setFirstAttr(AttributeBase firstAttr)
	{
		this.firstAttr = firstAttr;
	}

	// =========================================================================

	@Override
	public final boolean hasAttribute(String name)
	{
		return (getAttributeNode(name) != null);
	}

	@Override
	public final boolean hasAttributeNS(String namespaceUri, String localName) throws DOMException
	{
		return (getAttributeNodeNS(namespaceUri, localName) != null);
	}

	// =========================================================================

	@Override
	public final NamedNodeMap getAttributes()
	{
		return new AttributeMap(this);
	}

	// =========================================================================

	@Override
	public final String getAttribute(String name)
	{
		Wom3Attribute attributeNode = getAttributeNode(name);
		if (attributeNode == null)
			return "";
		return attributeNode.getNodeValue();
	}

	@Override
	public final AttributeBase getAttributeNode(String name)
	{
		if (name == null)
			throw new IllegalArgumentException("Argument `name' is null.");

		for (Wom3Attribute i = getFirstAttr(); i != null; i = (Wom3Attribute) i.getNextSibling())
		{
			if (i.getName().equals(name))
				return (AttributeBase) i;
		}

		return null;
	}

	@Override
	public final String getAttributeNS(String namespaceUri, String localName) throws DOMException
	{
		Wom3Attribute attributeNode = getAttributeNodeNS(namespaceUri, localName);
		if (attributeNode == null)
			return "";
		return attributeNode.getNodeValue();
	}

	@Override
	public final AttributeBase getAttributeNodeNS(
			String namespaceUri,
			String localName) throws DOMException
	{
		if (localName == null)
			throw new IllegalArgumentException("Argument `localName' is null.");

		for (Wom3Attribute i = getFirstAttr(); i != null; i = (Wom3Attribute) i.getNextSibling())
		{
			String iNamespaceUri = i.getNamespaceURI();
			String iLocalName = i.getLocalName();
			if (namespaceUri == null)
			{
				if (iNamespaceUri != null)
					continue;
				if (localName.equals(iLocalName))
					return (AttributeBase) i;
				// This one holds when the attribute we search for was added as 
				// DOM Level 1 element. It would neither have a namespace URI 
				// nor a local name. But if we search with a DOM Level 2 method
				// and the namespace URI we search for is null, a  DOM Level 1
				// attribute might still match.
				if (iLocalName == null && localName.equals(i.getNodeName()))
					return (AttributeBase) i;
			}
			else
			{
				if (namespaceUri.equals(iNamespaceUri) && localName.equals(iLocalName))
					return (AttributeBase) i;
			}
		}

		return null;
	}

	// =========================================================================

	protected final Object getAttributeNativeData(String name)
	{
		AttributeBase attr = getAttributeNode(name);
		return (attr == null) ? null : attr.getNativeValue();
	}

	// =========================================================================

	/**
	 * Override in sub classes to change behavior.
	 */
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return null;
	}

	protected final AttributeDescriptor getAttributeDescriptorOrFail(String name)
	{
		return getAttributeDescriptorOrFail(null, null, name);
	}

	protected final AttributeDescriptor getAttributeDescriptorOrFail(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		AttributeDescriptor d = getAttributeDescriptor(namespaceUri, localName, qualifiedName);
		if (d == null)
		{
			if (getOwnerDocument().getStrictErrorChecking())
				throw new IllegalArgumentException(
						"An attribute named `" + namespaceUri + "' / `" + qualifiedName + "' is not supported by this element!");

			d = CommonAttributeDescriptors.ATTR_DESC_GENERIC;
		}
		return d;
	}

	// =========================================================================

	@Override
	public final void setAttribute(String name, String value) throws DOMException
	{
		Toolbox.checkValidXmlName(name);
		AttributeDescriptor descriptor = getAttributeDescriptorOrFail(name);

		NativeAndStringValuePair verified = new NativeAndStringValuePair(value);
		if (value != null && descriptor.verifyAndConvert(this, verified))
		{
			// keep attribute
			setAttribute(descriptor, name, verified);
		}
		else
		{
			// remove attribute
			AttributeBase old = getAttributeNode(name);
			removeAttribute(descriptor, name, old);
		}
	}

	private final AttributeBase setAttribute(
			AttributeDescriptor descriptor,
			String name,
			NativeAndStringValuePair verified)
	{
		assertWritableOnDocument();

		AttributeBase oldAttr = getAttributeNode(name);
		assertWritable(oldAttr, descriptor);

		AttributeBase newAttr = createAttribute(name, verified);
		replaceAttributeInternal(descriptor, oldAttr, newAttr);
		return oldAttr;
	}

	/**
	 * Test cases may want to overwrite this -> no final
	 */
	protected AttributeBase createAttribute(
			String name,
			NativeAndStringValuePair verified)
	{
		AttributeBase attr = (AttributeBase) getOwnerDocument().createAttribute(name);
		// We don't check an attribute descriptor for read-only since a new 
		// attribute may be set once anyway.
		attr.setValue(verified.value, verified.strValue, false /* cloning */);
		return attr;
	}

	@Override
	public final void setAttributeNS(
			String namespaceUri,
			String qualifiedName,
			String value) throws DOMException
	{
		Toolbox.checkValidXmlName(qualifiedName);

		int index = qualifiedName.indexOf(':');
		String localName;
		if (index < 0)
		{
			localName = qualifiedName;
		}
		else
		{
			localName = qualifiedName.substring(index + 1);
		}

		AttributeDescriptor descriptor = getAttributeDescriptorOrFail(
				namespaceUri, localName, qualifiedName);

		NativeAndStringValuePair verified = new NativeAndStringValuePair(value);
		if (value != null && descriptor.verifyAndConvert(this, verified))
		{
			// keep attribute
			setAttributeNs(descriptor, namespaceUri, localName, qualifiedName, verified);
		}
		else
		{
			// remove attribute
			AttributeBase old = getAttributeNodeNS(namespaceUri, localName);
			removeAttribute(descriptor, localName, old);
		}
	}

	private final AttributeBase setAttributeNs(
			AttributeDescriptor descriptor,
			String namespaceUri,
			String localName,
			String qualifiedName,
			NativeAndStringValuePair verified)
	{
		assertWritableOnDocument();

		AttributeBase oldAttr = getAttributeNodeNS(namespaceUri, localName);
		assertWritable(oldAttr, descriptor);

		AttributeBase newAttr = createAttributeNS(namespaceUri, qualifiedName, verified);
		replaceAttributeInternal(descriptor, oldAttr, newAttr);
		return oldAttr;
	}

	/**
	 * Test cases may want to overwrite this -> no final
	 */
	protected AttributeBase createAttributeNS(
			String namespaceUri,
			String qualifiedName,
			NativeAndStringValuePair verified)
	{
		AttributeBase attr = (AttributeBase) getOwnerDocument().createAttributeNS(namespaceUri, qualifiedName);
		// We don't check an attribute descriptor for read-only since a new 
		// attribute may be set once anyway.
		attr.setValue(verified.value, verified.strValue, false /* cloning */);
		return attr;
	}

	@Override
	public final Wom3Attribute setAttributeNode(Attr attr_) throws IllegalArgumentException, DOMException
	{
		return setAttributeNode(attr_, false /* cloning */);
	}

	public final Wom3Attribute setAttributeNode(
			Attr attr_,
			boolean cloning) throws IllegalArgumentException, DOMException
	{
		if (attr_ == null)
			throw new IllegalArgumentException("Argument `attr' is null.");

		Wom3Attribute attr = Toolbox.expectType(Wom3Attribute.class, attr_);

		if (attr.getOwnerElement() == this)
			return (AttributeBase) attr;

		// FIXME: I'm not sure if that's correct.
		// It's the only way since the XML parser doesn't distinguish between 
		// setAttributeNode and setAttributeNodeNS
		if (((attr.getNamespaceURI() != null) && (!attr.getNamespaceURI().isEmpty()))
				|| ((attr.getPrefix() != null) && (!attr.getPrefix().isEmpty())))
			return setAttributeNodeNS(attr_, cloning);

		String attrName = attr.getName();
		AttributeDescriptor descriptor = getAttributeDescriptorOrFail(attrName);

		/* An attribute cannot verify its name/value when it is not attached to 
		 * a node. Therefore we have to verify it when it gets attached to this 
		 * node.
		 */
		NativeAndStringValuePair verified = new NativeAndStringValuePair(attr.getNodeValue());
		if (attr.getNodeValue() == null || descriptor.verifyAndConvert(this, verified))
		{
			// keep attribute
			return setAttributeNode(descriptor, attr, verified, cloning);
		}
		else if (!cloning)
		{
			// remove attribute
			AttributeBase old = getAttributeNode(attrName);
			removeAttribute(descriptor, attrName, old);
			return old;
		}
		else
		{
			return null;
		}
	}

	private final AttributeBase setAttributeNode(
			AttributeDescriptor descriptor,
			Wom3Attribute attr,
			NativeAndStringValuePair verified,
			boolean cloning)
	{
		if (!cloning)
			assertWritableOnDocument();

		String attrName = attr.getName();
		AttributeBase oldAttr = getAttributeNode(attrName);
		return setAttribute(descriptor, attrName, attr, verified, oldAttr, cloning);
	}

	@Override
	public final Wom3Attribute setAttributeNodeNS(Attr attr_) throws DOMException
	{
		return setAttributeNodeNS(attr_, false /* cloning */);
	}

	public final Wom3Attribute setAttributeNodeNS(Attr attr_, boolean cloning) throws DOMException
	{
		if (attr_ == null)
			throw new IllegalArgumentException("Argument `attr' is null.");

		Wom3Attribute attr = Toolbox.expectType(Wom3Attribute.class, attr_);

		if (attr.getOwnerElement() == this)
			return (AttributeBase) attr;

		String namespaceUri = attr.getNamespaceURI();
		String localName = attr.getLocalName();
		String name = attr.getName();
		AttributeDescriptor descriptor = getAttributeDescriptorOrFail(
				namespaceUri, localName, name);

		/* An attribute cannot verify its name/value when it is not attached to 
		 * a node. Therefore we have to verify it when it gets attached to this 
		 * node.
		 */
		NativeAndStringValuePair verified = new NativeAndStringValuePair(attr.getNodeValue());
		if (attr.getNodeValue() == null || descriptor.verifyAndConvert(this, verified))
		{
			// keep attribute
			return setAttributeNodeNS(descriptor, attr, verified, cloning);
		}
		else if (!cloning)
		{
			// remove attribute
			AttributeBase old = getAttributeNodeNS(namespaceUri, localName);
			removeAttribute(descriptor, localName, old);
			return old;
		}
		else
		{
			return null;
		}
	}

	private final AttributeBase setAttributeNodeNS(
			AttributeDescriptor descriptor,
			Wom3Attribute attr,
			NativeAndStringValuePair verified,
			boolean cloning)
	{
		if (!cloning)
			assertWritableOnDocument();

		String attrLocalName = attr.getLocalName();
		AttributeBase oldAttr = getAttributeNodeNS(attr.getNamespaceURI(), attrLocalName);
		return setAttribute(descriptor, attrLocalName, attr, verified, oldAttr, cloning);
	}

	private AttributeBase setAttribute(
			AttributeDescriptor descriptor,
			String attrName,
			Wom3Attribute attr,
			NativeAndStringValuePair verified,
			AttributeBase oldAttr,
			boolean cloning)
	{
		if (!cloning)
			assertWritable(oldAttr, descriptor);

		AttributeBase newAttr =
				Toolbox.expectType(AttributeBase.class, attr, "attr");

		// Is expected to assertWritable() on doc
		newAttr.setValue(verified.value, verified.strValue, cloning);

		if (newAttr.isLinked())
			throw new IllegalStateException(
					"Given attribute `attr' is still attribute of another WOM node.");

		replaceAttributeInternal(descriptor, oldAttr, newAttr);
		return oldAttr;
	}

	// =========================================================================

	/**
	 * For use by direct setters which have access to the native value and thus
	 * require conversion to string.
	 */
	protected final <T> T setAttributeDirect(
			String name,
			T value)
	{
		AttributeDescriptor descriptor = getAttributeDescriptorOrFail(name);
		return setAttributeDirect(descriptor, name, value);
	}

	/**
	 * For use by direct setters which have access to the native value and thus
	 * require conversion to string.
	 */
	protected final <T> T setAttributeDirect(
			AttributeDescriptor descriptor,
			String name,
			T value)
	{
		AttributeBase old;
		NativeAndStringValuePair verified = new NativeAndStringValuePair((Object) value);
		if (value != null && descriptor.verifyAndConvert(this, verified))
		{
			// keep attribute
			// Is expected to assertWritable()
			old = setAttribute(descriptor, name, verified);
		}
		else
		{
			// remove attribute
			old = getAttributeNode(name);
			removeAttribute(descriptor, name, old);
		}
		if (old == null)
			return null;
		@SuppressWarnings("unchecked")
		T oldT = (T) old.getNativeValue();
		return oldT;
	}

	/**
	 * For special attributes which are only set once and cannot be changed
	 * (example: "version" on WomPage).
	 */
	protected final void setAttributeDirectNoChecks(String name, String value)
	{
		NativeAndStringValuePair verified = new NativeAndStringValuePair(value);
		// Is expected to assertWritable()
		setAttribute(null, name, verified);
	}

	// =========================================================================

	private final void replaceAttributeInternal(
			AttributeDescriptor descriptor,
			AttributeBase oldAttr,
			AttributeBase newAttr)
	{
		Backbone prev = null;
		Backbone next = null;
		if (oldAttr != null)
		{
			prev = oldAttr.getPreviousSibling();
			next = oldAttr.getNextSibling();
			oldAttr.unlink();
		}
		else if (getFirstAttr() != null)
		{
			prev = getFirstAttr();
			while (prev.getNextSibling() != null)
				prev = (Backbone) prev.getNextSibling();
		}

		newAttr.link(this, prev, next);
		if (getFirstAttr() == null || oldAttr == getFirstAttr())
			setFirstAttr(newAttr);

		if ((descriptor != null) && (descriptor.hasCustomAction()))
			descriptor.customAction(this, oldAttr, newAttr);
	}

	// =========================================================================

	@Override
	public void setIdAttribute(String name, boolean isId) throws DOMException
	{
		assertWritableOnDocument();

		// TODO: Implement
		if (isId)
			throw new UnsupportedOperationException();
	}

	@Override
	public void setIdAttributeNS(
			String namespaceURI,
			String localName,
			boolean isId) throws DOMException
	{
		assertWritableOnDocument();

		// TODO: Implement
		if (isId)
			throw new UnsupportedOperationException();
	}

	@Override
	public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException
	{
		assertWritableOnDocument();

		// TODO: Implement
		if (isId)
			throw new UnsupportedOperationException();
	}

	// =========================================================================

	@Override
	public final void removeAttribute(String name)
	{
		if (name == null)
			throw new IllegalArgumentException("Argument `name' is null.");

		AttributeDescriptor descriptor = getAttributeDescriptorOrFail(name);
		AttributeBase remove = getAttributeNode(name);
		removeAttribute(descriptor, name, remove);
	}

	@Override
	public final void removeAttributeNS(String namespaceUri, String localName) throws DOMException
	{
		if (localName == null)
			throw new IllegalArgumentException("Argument `localName' is null.");

		AttributeDescriptor descriptor = getAttributeDescriptorOrFail(namespaceUri, localName, localName);
		AttributeBase remove = getAttributeNodeNS(namespaceUri, localName);
		removeAttribute(descriptor, localName, remove);
	}

	@Override
	public final Wom3Attribute removeAttributeNode(Attr attr_) throws DOMException
	{
		Wom3Attribute attr = Toolbox.expectType(Wom3Attribute.class, attr_);

		if (attr == null)
			throw new IllegalArgumentException("Argument `attr' is null.");

		if (attr.getOwnerElement() != this)
			throw new IllegalArgumentException("Given attribute `attr' is not an attribute of this XML element.");

		String attrName = attr.getName();
		removeAttribute(getAttributeDescriptorOrFail(attrName), attrName, (AttributeBase) attr);

		return attr;
	}

	private final void removeAttribute(
			AttributeDescriptor descriptor,
			String name,
			AttributeBase attribute)
	{
		assertWritableOnDocument();

		if (attribute != null)
		{
			checkAttributeRemoval(name, descriptor);

			Backbone parent = (Backbone) attribute.getOwnerElement();

			// remove from WOM
			if (attribute == getFirstAttr())
				setFirstAttr((AttributeBase) attribute.getNextSibling());
			attribute.unlink();

			if ((descriptor != null) && (descriptor.hasCustomAction()))
				descriptor.customAction(parent, attribute, null);
		}
		else
		{
			checkAttributeRemoval(name, descriptor);
		}
	}

	protected final void checkAttributeRemoval(
			String name,
			AttributeDescriptor descriptor)
	{
		if (getOwnerDocument().getStrictErrorChecking())
		{
			if (descriptor.isReadOnly())
				throw new UnsupportedOperationException("Read-only attribute is '" + name + "' is already set!");

			if (!descriptor.isRemovable())
				throw new UnsupportedOperationException(
						"Attribute `" + name + "' cannot be removed");
		}
	}
}
