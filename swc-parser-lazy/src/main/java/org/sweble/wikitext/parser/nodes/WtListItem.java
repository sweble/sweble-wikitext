package org.sweble.wikitext.parser.nodes;

public class WtListItem
		extends
			WtContentNodeImpl
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtListItem()
	{
	}
	
	public WtListItem(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_LIST_ITEM;
	}
}
