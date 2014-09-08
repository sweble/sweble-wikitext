/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3Blockquote;
import org.sweble.wom3.Wom3Node;

public class BlockquoteImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3Blockquote
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public BlockquoteImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "blockquote";
	}
	
	// =========================================================================
	
	@Override
	public URL getCite()
	{
		return getUrlAttr("cite");
	}
	
	@Override
	public URL setCite(URL source)
	{
		return setUrlAttr(Attributes.CITE, "cite", source);
	}
	
	// =========================================================================
	
	private static final Map<String, AttributeDescriptor> nameMap = getNameMap();
	
	private static Map<String, AttributeDescriptor> getNameMap()
	{
		Map<String, AttributeDescriptor> nameMap =
				new HashMap<String, AttributeDescriptor>();
		
		nameMap.putAll(UniversalAttributes.getNameMap());
		nameMap.put("cite", Attributes.CITE);
		
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
		CITE
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.URL.verifyAndConvert(parent, verified);
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
