package org.sweble.wikitext.parser.nodes;

public class WtSemiPreLine
		extends
			WtContentNodeImpl
		implements
			WtIntermediate
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtSemiPreLine()
	{
	}
	
	public WtSemiPreLine(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_SEMI_PRE_LINE;
	}
	
	// =========================================================================
	
	@Override
	public boolean isSynthetic()
	{
		return false;
	}
}
