package org.sweble.wikitext.parser.nodes;

/**
 * <h1>WtOrderedList</h1>
 */
public class WtOrderedList
		extends
			WtContentNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtOrderedList()
	{
		super();
	}
	
	@Override
	public int getNodeType()
	{
		return NT_ORDERED_LIST;
	}
}
