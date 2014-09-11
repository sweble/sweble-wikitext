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

import org.sweble.wom3.Wom3HorizAlign;
import org.sweble.wom3.Wom3HorizontalRule;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3ValueWithUnit;

public class HorizontalRuleImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3HorizontalRule
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public HorizontalRuleImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "hr";
	}
	
	// =========================================================================
	
	@Override
	public Wom3HorizAlign getAlign()
	{
		return getAlignAttr("align");
	}
	
	@Override
	public Wom3HorizAlign setAlign(Wom3HorizAlign align)
	{
		return setAlignAttr(Attributes.ALIGN, "align", align);
	}
	
	@Override
	public boolean isNoshade()
	{
		return getBoolAttr("noshade");
	}
	
	@Override
	public boolean setNoshade(boolean noshade)
	{
		return setBoolAttr(Attributes.NOSHADE, "noshade", noshade);
	}
	
	@Override
	public Integer getSize()
	{
		return getIntAttr("size");
	}
	
	@Override
	public Integer setSize(Integer size)
	{
		return setIntAttr(Attributes.SIZE, "size", size);
	}
	
	@Override
	public Wom3ValueWithUnit getWidth()
	{
		return getValueWithUnitAttr("width");
	}
	
	@Override
	public Wom3ValueWithUnit setWidth(Wom3ValueWithUnit width)
	{
		return setValueWithUnitAttr(Attributes.WIDTH, "width", width);
	}
	
	// =========================================================================
	
	private static final Map<String, AttributeDescriptor> nameMap = getNameMap();
	
	private static Map<String, AttributeDescriptor> getNameMap()
	{
		Map<String, AttributeDescriptor> nameMap =
				new HashMap<String, AttributeDescriptor>();
		
		nameMap.putAll(UniversalAttributes.getNameMap());
		nameMap.put("align", Attributes.ALIGN);
		nameMap.put("noshade", Attributes.NOSHADE);
		nameMap.put("size", Attributes.SIZE);
		nameMap.put("width", Attributes.WIDTH);
		
		return nameMap;
	}
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDesc(namespaceUri, localName, qualifiedName, nameMap);
	}
	
	private static enum Attributes implements AttributeDescriptor
	{
		ALIGN
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.LCR_ALIGN.verifyAndConvert(parent, verified);
			}
		},
		NOSHADE
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.verifyAndConvertBool(parent, verified, "noshade");
			}
		},
		SIZE
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.NUMBER.verifyAndConvert(parent, verified);
			}
		},
		WIDTH
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.LENGTH.verifyAndConvert(parent, verified);
			}
		};
		
		// =====================================================================
		
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
	}
}
