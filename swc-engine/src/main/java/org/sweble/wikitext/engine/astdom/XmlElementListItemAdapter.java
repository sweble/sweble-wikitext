package org.sweble.wikitext.engine.astdom;

import org.sweble.wikitext.engine.dom.DomListItem;
import org.sweble.wikitext.lazy.parser.XmlElement;

public class XmlElementListItemAdapter
        extends
            XmlElementAdapter
        implements
            DomListItem
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public XmlElementListItemAdapter(XmlElement astNode)
	{
		super(astNode);
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "li";
	}
}
