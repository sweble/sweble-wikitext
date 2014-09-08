/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3Node;
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
		return setAttributeDirect(Attributes.START, "start", start);
	}
	
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
	
	// =========================================================================
	
	private static final Map<String, AttributeDescriptor> nameMap = getNameMap();
	
	private static Map<String, AttributeDescriptor> getNameMap()
	{
		Map<String, AttributeDescriptor> nameMap =
				new HashMap<String, AttributeDescriptor>();
		
		nameMap.putAll(UniversalAttributes.getNameMap());
		nameMap.put("compact", ListBaseImpl.Attributes.COMPACT);
		nameMap.put("start", Attributes.START);
		nameMap.put("type", Attributes.TYPE);
		
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
				return AttributeVerifiers.OLTYPE.verifyAndConvert(parent, verified);
			}
		},
		START
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
