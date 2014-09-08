/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3BulletStyle;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3UnorderedList;

public class UnorderedListImpl
		extends
			ListBaseImpl
		implements
			Wom3UnorderedList
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public UnorderedListImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "ul";
	}
	
	// =========================================================================
	
	@Override
	public Wom3BulletStyle getItemType()
	{
		return (Wom3BulletStyle) getAttributeNativeData("type");
	}
	
	@Override
	public Wom3BulletStyle setItemType(Wom3BulletStyle type)
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
				return AttributeVerifiers.ULTYPE.verifyAndConvert(parent, verified);
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
