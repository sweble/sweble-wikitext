package org.sweble.wikitext.parser.nodes;

public class WtLinkOptionsImpl
		extends
			WtContentNodeImpl
		implements
			WtLinkOptions
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtLinkOptionsImpl()
	{
	}
	
	public WtLinkOptionsImpl(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_LINK_OPTIONS;
	}
	
	@Override
	public String getNodeName()
	{
		return WtLinkOptions.class.getSimpleName();
	}
}
