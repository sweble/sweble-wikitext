package org.sweble.wikitext.parser.nodes;

public class WtBodyImpl
		extends
			WtContentNodeImpl
		implements
			WtBody
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtBodyImpl()
	{
	}
	
	public WtBodyImpl(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_BODY;
	}
	
	@Override
	public String getNodeName()
	{
		return WtBody.class.getSimpleName();
	}
}
