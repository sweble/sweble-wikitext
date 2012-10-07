package org.sweble.wikitext.parser.nodes;

public class WtTemplateArguments
		extends
			WtContentNodeImpl
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtTemplateArguments()
	{
	}
	
	public WtTemplateArguments(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_TEMPLATE_ARGUMENTS;
	}
}
