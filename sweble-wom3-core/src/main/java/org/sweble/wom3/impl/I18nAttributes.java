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

import org.sweble.wom3.Wom3Node;

public enum I18nAttributes implements AttributeDescriptor
{
	DIR
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.DIR.verifyAndConvert(parent, verified);
		}
	},
	
	LANG
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.LANGUAGE_CODE.verifyAndConvert(parent, verified);
		}
	},
	
	XMLLANG
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.LANGUAGE_CODE.verifyAndConvert(parent, verified);
		}
	};
	
	// =========================================================================
	
	@Override
	public boolean isRemovable()
	{
		return true;
	}
	
	@Override
	public Normalization getNormalizationMode()
	{
		return Normalization.NON_CDATA;
	}
	
	@Override
	public void customAction(
			Wom3Node parent,
			AttributeBase oldAttr,
			AttributeBase newAttr)
	{
	}
	
	// =========================================================================
	
	private static final Map<String, AttributeDescriptor> nameMap;
	
	public static Map<String, AttributeDescriptor> getNameMap()
	{
		if (nameMap != null)
			return nameMap;
		
		Map<String, AttributeDescriptor> nameMap =
				new HashMap<String, AttributeDescriptor>();
		
		nameMap.put("dir", DIR);
		nameMap.put("lang", LANG);
		nameMap.put("xml:lang", XMLLANG);
		
		return nameMap;
	}
	
	static
	{
		nameMap = getNameMap();
	}
	
	public static AttributeDescriptor getDescriptor(String name)
	{
		return nameMap.get(name);
	}
}
