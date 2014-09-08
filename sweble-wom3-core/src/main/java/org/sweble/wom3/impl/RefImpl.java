/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3Ref;

public class RefImpl
		extends
			BackboneContainer
		implements
			Wom3Ref
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public RefImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "ref";
	}
	
	// =========================================================================
	
	@Override
	public String getType()
	{
		return (String) getAttributeNativeData("type");
	}
	
	@Override
	public String setType(String type)
	{
		return setAttributeDirect(Attributes.TYPE, "type", type);
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceUri, localName, qualifiedName,
				"type", Attributes.TYPE);
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
				return true;
			}
		};
		
		// =====================================================================
		
		@Override
		public boolean isRemovable()
		{
			return false;
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
