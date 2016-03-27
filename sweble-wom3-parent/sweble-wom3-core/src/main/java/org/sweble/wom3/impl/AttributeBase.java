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
	public final short getNodeType()
	{
		return Node.ATTRIBUTE_NODE;
	}

	@Override
	public final Backbone getParentNode()
	{
		return null;
	}

	@Override
	public final Element getOwnerElement()
	{
		return (Element) getParentNodeIntern();
	}

	// =========================================================================

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

	// =========================================================================

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public final String getNodeName()
	{
		return getName();
	}

	@Override
	public String setName(String name) throws IllegalArgumentException, NullPointerException
	{
		// DOM Level 1 implementation

		if (this.name != null && this.name.equals(name))
			return this.name;

		Toolbox.checkValidXmlName(name);

		return setNameIntern(name);
	}

	// =========================================================================

	@Override
	public abstract String getValue();

	@Override
	public final String getNodeValue() throws DOMException
	{
		return getValue();
	}

	protected abstract Object getNativeValue();

	// =========================================================================

	@Override
	public void setValue(String value)
	{
		if (value == null)
			throw new NullPointerException();

		validateValueChangeWithParentAndSet(value);
	}

	@Override
	public final void setNodeValue(String nodeValue) throws DOMException
	{
		setValue(nodeValue);
	}

	@Override
	public void setTextContent(String textContent) throws DOMException
	{
		// TODO: Actually an attr can hold children and the node value is 
		// composed of those children!

		setValue(textContent);
	}

	@Override
	public String getTextContent() throws DOMException
	{
		// TODO: Actually an attr can hold children and the node value is 
		// composed of those children!

		return getValue();
	}

	// =========================================================================

	private String setNameIntern(String name)
	{
		String old = getName();
		if ((this.name != null) && this.name.equals(name))
			return old;

		validateNameChangeWithParent(name);

		return old;
	}

	// =========================================================================

	private void validateNameChangeWithParent(String name)
	{
		BackboneElement parent = (BackboneElement) getOwnerElement();

		if (parent == null)
		{
			doSetName(name);
		}
		else
		{
			if (parent.hasAttribute(name))
				throw new IllegalArgumentException("Attribute with this name " +
						"already exists for the corresponding element!");

			AttributeDescriptor oldDescriptor = parent.getAttributeDescriptorOrFail(
					null, null, getName());

			AttributeDescriptor newDescriptor = parent.getAttributeDescriptorOrFail(
					null, null, name);

			validateNameChangeWithParent(name, parent, oldDescriptor, newDescriptor);
		}
	}

	protected void validateNameChangeWithParent(
			String name,
			BackboneElement parent,
			AttributeDescriptor oldDescriptor,
			AttributeDescriptor newDescriptor)
	{
		// Assert writable before performing a custom action.
		assertWritableOnDocument();

		if (oldDescriptor != newDescriptor)
		{
			if (getOwnerDocument().getStrictErrorChecking())
			{
				if (!oldDescriptor.isRemovable())
					throw new UnsupportedOperationException(
							"Attribute `" + name + "' cannot be removed");
			}

			if (oldDescriptor.hasCustomAction())
				oldDescriptor.customAction(parent, this, null);

			doSetName(name);

			if (newDescriptor.hasCustomAction())
				newDescriptor.customAction(parent, null, this);
		}
		else
		{
			doSetName(name);

			if (newDescriptor.hasCustomAction())
				newDescriptor.customAction(parent, this, this);
		}
	}

	protected void doSetName(String name)
	{
		this.name = name;
	}

	// =========================================================================

	/**
	 * Is expected to check {@link #assertWritableOnDocument()}
	 */
	protected abstract void setValue(
			Object value,
			String strValue,
			boolean cloning);

	private void validateValueChangeWithParentAndSet(String value)
	{
		BackboneElement parent =
				(BackboneElement) getOwnerElement();

		if (parent == null)
		{
			setValue(value, value, false /* cloning */);
		}
		else
		{
			String namespaceUri = getNamespaceURI();
			String localName = getLocalName();
			AttributeDescriptor descriptor = parent.getAttributeDescriptorOrFail(
					namespaceUri, localName, name);

			NativeAndStringValuePair verified = new NativeAndStringValuePair(value);
			if ((value != null) && descriptor.verifyAndConvert(parent, verified))
			{
				assertWritable(this, descriptor);

				setValue(verified.value, verified.strValue, false /* cloning */);

				if (descriptor.hasCustomAction())
					descriptor.customAction(parent, this, this);
			}
			else
			{
				throw new UnsupportedOperationException(
						"Attribute `" + namespaceUri + "' / `" + name + "' cannot remove itself!");
			}
		}
	}
}
