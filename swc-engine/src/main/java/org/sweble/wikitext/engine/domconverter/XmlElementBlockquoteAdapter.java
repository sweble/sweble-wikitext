package org.sweble.wikitext.engine.domconverter;

import org.sweble.wikitext.lazy.parser.XmlElement;

public class XmlElementBlockquoteAdapter
        extends
            XmlElementAdapter
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public XmlElementBlockquoteAdapter(XmlElement element)
	{
		super(element);
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "blockquote";
	}
}
