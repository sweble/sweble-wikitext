package org.sweble.wikitext.parser.nodes;

/**
 * <h1>WtOrderedList</h1>
 */
public class WtOrderedList
		extends
			WtContentNodeMarkTwo
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
		return org.sweble.wikitext.parser.AstNodeTypes.NT_ORDERED_LIST;
	}
}
