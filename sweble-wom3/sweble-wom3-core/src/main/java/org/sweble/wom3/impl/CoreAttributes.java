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

public abstract class CoreAttributes
{
	public static final AttrDescId ATTR_DESC_ID = new AttrDescId();

	public static final AttrDescClass ATTR_DESC_CLASS = new AttrDescClass();

	public static final AttrDescStyle ATTR_DESC_STYLE = new AttrDescStyle();

	public static final AttrDescTitle ATTR_DESC_TITLE = new AttrDescTitle();

	// =========================================================================

	public static final class AttrDescId
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
			return AttributeVerifiers.ID.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescClass
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
			return AttributeVerifiers.NMTOKENS.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescStyle
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
			return AttributeVerifiers.STYLESHEET.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescTitle
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
			return super.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	private static Map<String, AttributeDescriptor> NAME_MAP;

	public synchronized static Map<String, AttributeDescriptor> getNameMap()
	{
		Map<String, AttributeDescriptor> nameMap = NAME_MAP;
		if (nameMap == null)
		{
			nameMap = new HashMap<String, AttributeDescriptor>();
			nameMap.put("id", ATTR_DESC_ID);
			nameMap.put("class", ATTR_DESC_CLASS);
			nameMap.put("style", ATTR_DESC_STYLE);
			nameMap.put("title", ATTR_DESC_TITLE);
			NAME_MAP = nameMap;
		}
		return nameMap;
	}

	public static AttributeDescriptor getDescriptor(String name)
	{
		return getNameMap().get(name);
	}
}
