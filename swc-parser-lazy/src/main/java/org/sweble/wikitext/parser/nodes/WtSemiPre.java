package org.sweble.wikitext.parser.nodes;

public class WtSemiPre
		extends
			WtContentNodeImpl
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtSemiPre()
	{
	}
	
	public WtSemiPre(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_SEMI_PRE;
	}
}
