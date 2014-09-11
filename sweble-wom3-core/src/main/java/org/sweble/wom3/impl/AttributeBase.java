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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Attribute;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;

/**
 * DOM v1 Attribute Implementation (no namespace awareness)
 */
public abstract class AttributeBase
		extends
			Backbone
		implements
			Wom3Attribute
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The fully qualified name of the attribute, if this is a namespace aware
	 * DOM Level 2 attributes node.
	 */
	private String name;
	
	// =========================================================================
	
	/**
	 * For AttributeNsImpl only!
	 */
	protected AttributeBase(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public abstract String getValue();
	
	protected abstract Object getNativeValue();
	
	protected abstract void setValue(Object value, String strValue);
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return getName();
	}
	
	@Override
	public short getNodeType()
	{
		return Node.ATTRIBUTE_NODE;
	}
	
	@Override
	public Backbone getParentNode()
	{
		return null;
	}
	
	@Override
	public Element getOwnerElement()
	{
		return (Element) getParentNodeIntern();
	}
	
	@Override
	public void setTextContent(String textContent) throws DOMException
	{
		// TODO: Actually an attr can hold children and the node value is 
		// composed of those children!
	}
	
	@Override
	public boolean getSpecified()
	{
		// TODO: Implement
		return true;
	}
	
	@Override
	public TypeInfo getSchemaTypeInfo()
	{
		// TODO: Implement
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean isId()
	{
		// TODO: Implement
		return false;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String setName(String name) throws IllegalArgumentException, NullPointerException
	{
		// DOM Level 1 implementation
		
		if (this.name != null && this.name.equals(name))
			return this.name;
		
		Toolbox.checkValidXmlName(name);
		
		return setName(null, null, name);
	}
	
	@Override
	public String getNodeValue() throws DOMException
	{
		// TODO: Actually an attr can hold children and the node value is 
		// composed of those children!
		return getValue();
	}
	
	@Override
	public void setValue(String value)
	{
		setNodeValue(value);
	}
	
	@Override
	public void setNodeValue(String nodeValue) throws DOMException
	{
		// TODO: Actually an attr can hold children and the node value is 
		// composed of those children!
		
		if (nodeValue == null)
			throw new NullPointerException();
		
		validateValueChangeWithParentAndSet(nodeValue);
	}
	
	// =========================================================================
	
	protected String setName(String namespaceUri, String localName, String name) throws IllegalArgumentException, NullPointerException
	{
		if ((this.name != null && this.name.equals(name)) &&
				(getNamespaceURI() != null && getNamespaceURI().equals(namespaceUri)))
			return this.name;
		
		validateNameChangeWithParent(namespaceUri, localName, name);
		
		String old = getName();
		setNameUnchecked(name);
		return old;
	}
	
	protected void setNameUnchecked(String name)
	{
		this.name = name;
	}
	
	// =========================================================================
	
	private void validateNameChangeWithParent(
			String namespaceUri,
			String localName,
			String name)
	{
		BackboneElement parent = (BackboneElement) getOwnerElement();
		
		if (parent == null)
			return;
		
		boolean exists = (localName != null) ?
				(parent.getAttributeNodeNS(namespaceUri, localName) != null) :
				(parent.getAttributeNode(name) != null);
		
		if (exists)
			throw new IllegalArgumentException("Attribute with this name " +
					"already exists for the corresponding element!");
		
		// Check if attribute name is allowed on our parent node.
		AttributeDescriptor descriptor = parent.getAttributeDescriptorOrFail(
				namespaceUri, localName, name);
		
		descriptor.customAction(parent, this, this);
	}
	
	private void validateValueChangeWithParentAndSet(String value)
	{
		BackboneElement parent =
				(BackboneElement) getOwnerElement();
		
		if (parent == null)
		{
			setValue(value, value);
			return;
		}
		
		String namespaceUri = getNamespaceURI();
		String localName = getLocalName();
		AttributeDescriptor descriptor = parent.getAttributeDescriptorOrFail(
				namespaceUri, localName, name);
		
		NativeAndStringValuePair verified = new NativeAndStringValuePair(value);
		if (value != null && descriptor.verifyAndConvert(parent, verified))
		{
			setValue(verified.value, verified.strValue);
			descriptor.customAction(parent, this, this);
		}
		else
		{
			throw new UnsupportedOperationException(
					"Attribute `" + namespaceUri + "' / `" + name + "' cannot remove itself!");
		}
	}
}
