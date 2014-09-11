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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
