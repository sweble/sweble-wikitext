/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3TableCaption;
import org.sweble.wom3.Wom3TableCaptionAlign;

public class TableCaptionImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3TableCaption
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public TableCaptionImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "caption";
	}
	
	// =========================================================================
	
	@Override
	public Wom3TableCaptionAlign getAlign()
	{
		return (Wom3TableCaptionAlign) getAttributeNativeData("align");
	}
	
	@Override
	public Wom3TableCaptionAlign setAlign(Wom3TableCaptionAlign align)
	{
		return setAttributeDirect(Attributes.ALIGN, "align", align);
	}
	
	// =========================================================================
	
	private static final Map<String, AttributeDescriptor> nameMap = getNameMap();
	
	private static Map<String, AttributeDescriptor> getNameMap()
	{
		Map<String, AttributeDescriptor> nameMap =
				new HashMap<String, AttributeDescriptor>();
		
		nameMap.putAll(UniversalAttributes.getNameMap());
		nameMap.put("align", Attributes.ALIGN);
		
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
				return AttributeVerifiers.TBLR_ALIGN.verifyAndConvert(parent, verified);
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
