package org.sweble.wikitext.parser.nodes;

public class WtItalics
		extends
			WtContentNodeImpl
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtItalics()
	{
	}
	
	public WtItalics(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_ITALICS;
	}
}
