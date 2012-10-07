package org.sweble.wikitext.parser.nodes;

public class WtHorizontalRule
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtHorizontalRule()
	{
	}
	
	@Override
	public int getNodeType()
	{
		return NT_HORIZONTAL_RULE;
	}
}
