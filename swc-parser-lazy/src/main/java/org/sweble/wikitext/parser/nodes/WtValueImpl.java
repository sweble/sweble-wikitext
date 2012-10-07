package org.sweble.wikitext.parser.nodes;

public class WtValueImpl
		extends
			WtContentNodeImpl
		implements
			WtValue
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtValueImpl()
	{
	}
	
	public WtValueImpl(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_TEMPLATE_VALUE;
	}
	
	@Override
	public String getNodeName()
	{
		return WtValue.class.getSimpleName();
	}
}
