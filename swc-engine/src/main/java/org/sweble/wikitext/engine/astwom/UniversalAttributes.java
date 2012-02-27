package org.sweble.wikitext.engine.astwom;

import org.sweble.wikitext.engine.astwom.adapters.NativeOrXmlAttributeAdapter;
import org.sweble.wikitext.engine.wom.WomNode;

public enum UniversalAttributes implements AttributeDescriptor
{
	// FIXME: IMPLEMENT VERIFY!
	
	// ==[ Core ]===============================================================
	
	ID, // xs:ID
	CLASS, // xs:NMTOKENS
	STYLE, // StyleSheet
	TITLE, // Text
	
	// ==[ I18N ]===============================================================
	
	DIR, // xs:token [ltr, rtl]
	LANG, // LanguageCode
	XMLLANG, // xml:lang
	
	// ==[ Event ]==============================================================
	
	ONCLICK, // Script
	ONDBLCLICK, // Script
	ONMOUSEDOWN, // Script
	ONMOUSEUP, // Script
	ONMOUSEOVER, // Script
	ONMOUSEMOVE, // Script
	ONMOUSEOUT, // Script
	ONKEYPRESS, // Script
	ONKEYDOWN, // Script
	ONKEYUP; // Script
	
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
		return Normalization.NON_CDATA;
	}
	
	@Override
	public void customAction(WomNode parent, NativeOrXmlAttributeAdapter oldAttr, NativeOrXmlAttributeAdapter newAttr)
	{
	}
}
