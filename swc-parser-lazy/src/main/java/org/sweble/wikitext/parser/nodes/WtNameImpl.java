package org.sweble.wikitext.parser.nodes;

public class WtNameImpl
		extends
			WtContentNodeImpl
		implements
			WtName
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtNameImpl()
	{
	}
	
	public WtNameImpl(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_TEMPLATE_NAME;
	}
	
	@Override
	public String getNodeName()
	{
		return WtName.class.getSimpleName();
	}
}
