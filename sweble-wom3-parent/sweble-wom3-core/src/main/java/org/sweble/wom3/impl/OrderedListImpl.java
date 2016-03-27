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

import org.sweble.wom3.Wom3OrderedList;

public class OrderedListImpl
		extends
			ListBaseImpl
		implements
			Wom3OrderedList
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	public OrderedListImpl(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	@Override
	public String getWomName()
	{
		return "ol";
	}

	// =========================================================================

	@Override
	public Integer getStart()
	{
		return (Integer) getAttributeNativeData("start");
	}

	@Override
	public Integer setStart(Integer start)
	{
		return setAttributeDirect(ATTR_DESC_START, "start", start);
	}

	@Override
	public String getItemType()
	{
		return (String) getAttributeNativeData("type");
	}

	@Override
	public String setItemType(String type)
	{
		return setAttributeDirect(ATTR_DESC_TYPE, "type", type);
	}

	// =========================================================================

	protected static final AttributeDescriptor ATTR_DESC_TYPE = new AttrDescType();

	protected static final AttributeDescriptor ATTR_DESC_START = new AttrDescStart();

	private static final Map<String, AttributeDescriptor> NAME_MAP = new HashMap<String, AttributeDescriptor>();

	static
	{
		NAME_MAP.putAll(UniversalAttributes.getNameMap());
		NAME_MAP.put("compact", CommonAttributeDescriptors.ATTR_DESC_COMPACT);
		NAME_MAP.put("start", ATTR_DESC_START);
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
			return AttributeVerifiers.OLTYPE.verifyAndConvert(parent, verified);
		}
	}

	public static final class AttrDescStart
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
			return AttributeVerifiers.NUMBER.verifyAndConvert(parent, verified);
		}
	}
}
