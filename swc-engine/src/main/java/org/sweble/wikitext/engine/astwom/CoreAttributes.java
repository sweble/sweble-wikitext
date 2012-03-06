package org.sweble.wikitext.engine.astwom;

import org.sweble.wikitext.engine.astwom.adapters.NativeOrXmlAttributeAdapter;
import org.sweble.wikitext.engine.wom.WomNode;

public enum CoreAttributes implements AttributeDescriptor
{
	ID
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			// xs:ID
			// TODO Auto-generated method stub
			return value;
		}
	},
	
	CLASS
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			// xs:NMTOKENS
			// TODO Auto-generated method stub
			return value;
		}
	},
	
	STYLE
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			// StyleSheet
			// TODO Auto-generated method stub
			return value;
		}
	},
	
	TITLE
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			// Text
			// TODO Auto-generated method stub
			return value;
		}
	};
	
	// =========================================================================
	
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
		return Normalization.NON_CDATA;
	}
	
	@Override
	public void customAction(
			WomNode parent,
			NativeOrXmlAttributeAdapter oldAttr,
			NativeOrXmlAttributeAdapter newAttr)
	{
	}
}
