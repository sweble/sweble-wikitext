/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3Color;
import org.sweble.wom3.Wom3Font;
import org.sweble.wom3.Wom3Node;

public class FontImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3Font
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public FontImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "font";
	}
	
	// =========================================================================
	
	@Override
	public Wom3Color getColor()
	{
		return getColorAttr("color");
	}
	
	@Override
	public Wom3Color setColor(Wom3Color color)
	{
		return setColorAttr(Attributes.COLOR, "color", color);
	}
	
	@Override
	public String getFace()
	{
		return getStringAttr("face");
	}
	
	@Override
	public String setFace(String face)
	{
		return setStringAttr(Attributes.FACE, "face", face);
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
	
	// =========================================================================
	
	private static final Map<String, AttributeDescriptor> nameMap = getNameMap();
	
	private static Map<String, AttributeDescriptor> getNameMap()
	{
		Map<String, AttributeDescriptor> nameMap =
				new HashMap<String, AttributeDescriptor>();
		
		nameMap.putAll(CoreAttributes.getNameMap());
		nameMap.putAll(I18nAttributes.getNameMap());
		nameMap.put("color", Attributes.COLOR);
		nameMap.put("face", Attributes.FACE);
		nameMap.put("size", Attributes.SIZE);
		
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
		COLOR
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.COLOR.verifyAndConvert(parent, verified);
			}
		},
		
		FACE
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return true;
			}
		},
		
		SIZE
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.FONTSIZE.verifyAndConvert(parent, verified);
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
