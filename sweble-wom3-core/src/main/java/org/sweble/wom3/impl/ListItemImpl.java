/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3ListItem;
import org.sweble.wom3.Wom3Node;

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
		return setAttributeDirect(Attributes.TYPE, "type", type);
	}
	
	@Override
	public Integer getItemValue()
	{
		return (Integer) getAttributeNativeData("value");
	}
	
	@Override
	public Integer setItemValue(Integer value)
	{
		return setAttributeDirect(Attributes.VALUE, "value", value);
	}
	
	// =========================================================================
	
	private static final Map<String, AttributeDescriptor> nameMap = getNameMap();
	
	private static Map<String, AttributeDescriptor> getNameMap()
	{
		Map<String, AttributeDescriptor> nameMap =
				new HashMap<String, AttributeDescriptor>();
		
		nameMap.putAll(UniversalAttributes.getNameMap());
		nameMap.put("type", Attributes.TYPE);
		nameMap.put("value", Attributes.VALUE);
		
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
		TYPE
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.ITEMTYPE.verifyAndConvert(parent, verified);
			}
		},
		VALUE
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.NUMBER.verifyAndConvert(parent, verified);
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
