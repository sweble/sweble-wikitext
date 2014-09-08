/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3Node;

public enum EventAttributes implements AttributeDescriptor
{
	ONCLICK
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	},
	
	ONDBLCLICK
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	},
	
	ONMOUSEDOWN
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	},
	
	ONMOUSEUP
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	},
	
	ONMOUSEOVER
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	},
	
	ONMOUSEMOVE
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	},
	
	ONMOUSEOUT
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	},
	
	ONKEYPRESS
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	},
	
	ONKEYDOWN
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	},
	
	ONKEYUP
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
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
	
	static Map<String, AttributeDescriptor> getNameMap()
	{
		if (nameMap != null)
			return nameMap;
		
		Map<String, AttributeDescriptor> nameMap =
				new HashMap<String, AttributeDescriptor>();
		
		nameMap.put("onclick", ONCLICK);
		nameMap.put("ondblclick", ONDBLCLICK);
		nameMap.put("onmousedown", ONMOUSEDOWN);
		nameMap.put("onmouseup", ONMOUSEUP);
		nameMap.put("onmouseover", ONMOUSEOVER);
		nameMap.put("onmousemove", ONMOUSEMOVE);
		nameMap.put("onmouseout", ONMOUSEOUT);
		nameMap.put("onkeypress", ONKEYPRESS);
		nameMap.put("onkeydown", ONKEYDOWN);
		nameMap.put("onkeyup", ONKEYUP);
		
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
