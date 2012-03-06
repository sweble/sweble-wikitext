package org.sweble.wikitext.engine.astwom;

import org.sweble.wikitext.engine.astwom.adapters.NativeOrXmlAttributeAdapter;
import org.sweble.wikitext.engine.wom.WomNode;

import de.fau.cs.osr.utils.Utils;

public enum I18nAttributes implements AttributeDescriptor
{
	DIR
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			if (!Utils.isOneOf(DIR_EXPECTED, value))
				throw new MustBeOneOfException(DIR_EXPECTED, value);
			
			return value;
		}
	},
	
	LANG
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			// LanguageCode
			// TODO Auto-generated method stub
			return value;
		}
	},
	
	XMLLANG
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			// xml:lang
			// TODO Auto-generated method stub
			return value;
		}
	};
	
	// =========================================================================
	
	static final String[] DIR_EXPECTED = { "ltr", "rtl" };
	
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
