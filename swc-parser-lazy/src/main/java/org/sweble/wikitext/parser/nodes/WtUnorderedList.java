package org.sweble.wikitext.parser.nodes;

/**
 * <h1>WtUnorderedList</h1>
 */
public class WtUnorderedList
		extends
			WtContentNodeMarkTwo
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtUnorderedList()
	{
		super();
	}
	
	@Override
	public int getNodeType()
	{
		return NT_UNORDERED_LIST;
	}
}
