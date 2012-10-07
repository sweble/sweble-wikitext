package org.sweble.wikitext.parser.nodes;

public class WtLinkOptions
		extends
			WtContentNodeImpl
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtLinkOptions()
	{
	}
	
	public WtLinkOptions(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_LINK_OPTIONS;
	}
}
