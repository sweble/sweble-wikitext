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

import org.sweble.wom3.Wom3Element;
import org.sweble.wom3.Wom3Node;
import org.w3c.dom.DOMException;

public class ElementImpl
		extends
			BackboneElement
		implements
			Wom3Element
{
	private static final long serialVersionUID = 1L;
	public static final String MWW_NS_URI = "http://sweble.org/schema/mww30";
	private static final String BODY_TAG = "body";
	
	private String name;
	
	// =========================================================================
	
	protected ElementImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	public ElementImpl(DocumentImpl owner, String name)
	{
		super(owner);
		setNodeName(name);
	}
	
	// =========================================================================
	
	protected final void setNodeName(String name)
	{
		assertWritableOnDocument();
		this.name = name;
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return name;
	}
	
	@Override
	protected void allowsInsertion(Backbone prev, Backbone child)
	{
	}
	
	@Override
	protected void allowsRemoval(Backbone child)
	{
	}
	
	@Override
	protected void allowsReplacement(Backbone oldChild, Backbone newChild)
	{
	}
	
	@Override
	protected boolean ignoresContentWhitespace()
	{
		return false;
	}
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return CommonAttributeDescriptors.ATTR_DESC_GENERIC;
	}

	// this fixes the problem that getTextContent returned also the tag, i.e. <tag>textContent</tag>
	@Override
	public String getTextContent() throws DOMException {
		Wom3Node child = this.getFirstChild();
		while (child != null)
		{
			if (child.getLocalName().equals(BODY_TAG) && child.getNamespaceURI().equals(MWW_NS_URI))
			{
				return child.getTextContent();
			}
			child = child.getNextSibling();
		}
		return super.getTextContent(); // fallback
	}
}
