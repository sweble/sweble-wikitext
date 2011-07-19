package org.sweble.wikitext.engine.astdom;

import org.sweble.wikitext.lazy.parser.EnumerationItem;

public class EnumerationItemAdapter
        extends
            ListItemAdapter
{
	private static final long serialVersionUID = 1L;
	
	public EnumerationItemAdapter(EnumerationItem astNode)
	{
		super(astNode);
	}
}
