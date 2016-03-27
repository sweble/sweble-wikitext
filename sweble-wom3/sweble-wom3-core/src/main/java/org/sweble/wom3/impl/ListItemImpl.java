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

import org.sweble.wom3.Wom3ListItem;

public class ListItemImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3ListItem
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	public ListItemImpl(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	@Override
	public String getWomName()
	{
		return "li";
	}

	// =========================================================================

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

	@Override
	public Integer getItemValue()
	{
		return (Integer) getAttributeNativeData("value");
	}

	@Override
	public Integer setItemValue(Integer value)
	{
		return setAttributeDirect(ATTR_DESC_VALUE, "value", value);
	}

	// =========================================================================

	protected static final AttributeDescriptor ATTR_DESC_TYPE = new AttrDescType();

	protected static final AttributeDescriptor ATTR_DESC_VALUE = new AttrDescValue();

	private static final Map<String, AttributeDescriptor> NAME_MAP = new HashMap<String, AttributeDescriptor>();

	static
	{
		NAME_MAP.putAll(UniversalAttributes.getNameMap());
		NAME_MAP.put("type", ATTR_DESC_TYPE);
		NAME_MAP.put("value", ATTR_DESC_VALUE);
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
			return AttributeVerifiers.ITEMTYPE.verifyAndConvert(parent, verified);
		}
	}

	public static final class AttrDescValue
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
