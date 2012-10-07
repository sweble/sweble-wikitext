package org.sweble.wikitext.parser.nodes;

public class WtLinkTitleImpl
		extends
			WtContentNodeImpl
		implements
			WtLinkTitle
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtLinkTitleImpl()
	{
	}
	
	public WtLinkTitleImpl(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_LINK_TITLE;
	}
	
	@Override
	public String getNodeName()
	{
		return WtLinkTitle.class.getSimpleName();
	}
}
