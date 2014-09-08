/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3Break;
import org.sweble.wom3.Wom3Clear;
import org.sweble.wom3.Wom3Node;

public class BreakImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3Break
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public BreakImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "br";
	}
	
	// =========================================================================
	
	@Override
	public Wom3Clear getClear()
	{
		return getClearAttr("clear");
	}
	
	@Override
	public Wom3Clear setClear(Wom3Clear clear)
	{
		return setClearAttr(Attributes.CLEAR, "clear", clear);
	}
	
	// =========================================================================
	
	private static final Map<String, AttributeDescriptor> nameMap = getNameMap();
	
	private static Map<String, AttributeDescriptor> getNameMap()
	{
		Map<String, AttributeDescriptor> nameMap =
				new HashMap<String, AttributeDescriptor>();
		
		nameMap.putAll(CoreAttributes.getNameMap());
		nameMap.put("clear", Attributes.CLEAR);
		
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
		CLEAR
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.CLEAR.verifyAndConvert(parent, verified);
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
