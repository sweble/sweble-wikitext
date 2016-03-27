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

import org.sweble.wom3.Wom3BulletStyle;
import org.sweble.wom3.Wom3UnorderedList;

public class UnorderedListImpl
		extends
			ListBaseImpl
		implements
			Wom3UnorderedList
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	public UnorderedListImpl(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	@Override
	public String getWomName()
	{
		return "ul";
	}

	// =========================================================================

	@Override
	public Wom3BulletStyle getItemType()
	{
		return (Wom3BulletStyle) getAttributeNativeData("type");
	}

	@Override
	public Wom3BulletStyle setItemType(Wom3BulletStyle type)
	{
		return setAttributeDirect(ATTR_DESC_TYPE, "type", type);
	}

	// =========================================================================

	protected static final AttributeDescriptor ATTR_DESC_TYPE = new AttrDescType();

	private static final Map<String, AttributeDescriptor> NAME_MAP = new HashMap<String, AttributeDescriptor>();

	static
	{
		NAME_MAP.putAll(UniversalAttributes.getNameMap());
		NAME_MAP.put("compact", CommonAttributeDescriptors.ATTR_DESC_COMPACT);
		NAME_MAP.put("type", ATTR_DESC_TYPE);
	}

	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDesc(namespaceUri, localName, qualifiedName, NAME_MAP);
	}

	public static final class AttrDescType
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.ULTYPE.verifyAndConvert(parent, verified);
		}
	}
}
