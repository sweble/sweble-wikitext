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

import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3Break;
import org.sweble.wom3.Wom3Clear;

public class BreakImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3Break
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	public BreakImpl(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	@Override
	public String getWomName()
	{
		return "br";
	}

	// =========================================================================

	@Override
	public Wom3Clear getClear()
	{
		return getClearAttr("clear");
	}

	@Override
	public Wom3Clear setClear(Wom3Clear clear)
	{
		return setClearAttr(CommonAttributeDescriptors.ATTR_DESC_CLEAR, "clear", clear);
	}

	// =========================================================================

	private static final Map<String, AttributeDescriptor> NAME_MAP = new HashMap<String, AttributeDescriptor>();

	static
	{
		NAME_MAP.putAll(CoreAttributes.getNameMap());
		NAME_MAP.put("clear", CommonAttributeDescriptors.ATTR_DESC_CLEAR);
	}

	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDesc(namespaceUri, localName, qualifiedName, NAME_MAP);
	}
}
