/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3Node;

public enum CoreAttributes implements AttributeDescriptor
{
	ID
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.ID.verifyAndConvert(parent, verified);
		}
	},
	
	CLASS
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.NMTOKENS.verifyAndConvert(parent, verified);
		}
	},
	
	STYLE
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.STYLESHEET.verifyAndConvert(parent, verified);
		}
	},
	
	TITLE
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return true;
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
		
		nameMap.put("id", ID);
		nameMap.put("class", CLASS);
		nameMap.put("style", STYLE);
		nameMap.put("title", TITLE);
		
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
