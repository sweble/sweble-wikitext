package org.sweble.wikitext.engine.astdom;

import org.sweble.wikitext.lazy.parser.ItemizationItem;

public class ItemizationItemAdapter
        extends
            ListItemAdapter
{
	private static final long serialVersionUID = 1L;
	
	public ItemizationItemAdapter(ItemizationItem astNode)
	{
		super(astNode);
	}
}
