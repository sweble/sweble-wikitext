/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Node;

public class GenericAttributeDescriptor
		implements
			AttributeDescriptor
{
	private static final GenericAttributeDescriptor SINGLETON = new GenericAttributeDescriptor();
	
	// =========================================================================
	
	public static GenericAttributeDescriptor get()
	{
		return SINGLETON;
	}
	
	// =========================================================================
	
	public boolean verifyAndConvert(
			Backbone parent,
			NativeAndStringValuePair verified)
	{
		return true;
	}
	
	@Override
	public boolean isRemovable()
	{
		return true;
	}
	
	@Override
	public Normalization getNormalizationMode()
	{
		return Normalization.CDATA;
	}
	
	@Override
	public void customAction(
			Wom3Node parent,
			AttributeBase oldAttr,
			AttributeBase newAttr)
	{
	}
}
