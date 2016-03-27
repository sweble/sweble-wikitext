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

public class ElementImpl
		extends
			BackboneElement
		implements
			Wom3Element
{
	private static final long serialVersionUID = 1L;

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
}
