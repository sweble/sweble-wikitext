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

public abstract class I18nAttributes
{
	public static final AttributeDescriptor ATTR_DESC_DIR = new AttrDescDir();

	public static final AttributeDescriptor ATTR_DESC_LANG = new AttrDescLang();

	public static final AttributeDescriptor ATTR_DESC_XML_LANG = new AttrDescXmlLang();

	// =========================================================================

	public static final class AttrDescDir
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
			return AttributeVerifiers.DIR.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescLang
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
			return AttributeVerifiers.LANGUAGE_CODE.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescXmlLang
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
			return AttributeVerifiers.LANGUAGE_CODE.verifyAndConvert(parent, verified);
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
			nameMap.put("dir", ATTR_DESC_DIR);
			nameMap.put("lang", ATTR_DESC_LANG);
			nameMap.put("xml:lang", ATTR_DESC_XML_LANG);
			NAME_MAP = nameMap;
		}
		return nameMap;
	}

	public static AttributeDescriptor getDescriptor(String name)
	{
		return getNameMap().get(name);
	}
}
