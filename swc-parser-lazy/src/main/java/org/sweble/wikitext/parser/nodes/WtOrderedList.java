package org.sweble.wikitext.parser.nodes;

public class WtOrderedList
		extends
			WtContentNodeImpl
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtOrderedList()
	{
	}
	
	public WtOrderedList(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_ORDERED_LIST;
	}
}
