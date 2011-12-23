package org.sweble.wikitext.engine.astwom;

import org.sweble.wikitext.engine.wom.WomNode;

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
	
	@Override
	public String verify(WomNode parent, String value) throws IllegalArgumentException
	{
		return value;
	}
	
	@Override
	public boolean isRemovable()
	{
		return true;
	}
	
	@Override
	public boolean syncToAst()
	{
		return true;
	}
	
	@Override
	public Normalization getNormalizationMode()
	{
		return Normalization.CDATA;
	}
	
	@Override
	public void customAction(WomNode parent, String value)
	{
	}
}
