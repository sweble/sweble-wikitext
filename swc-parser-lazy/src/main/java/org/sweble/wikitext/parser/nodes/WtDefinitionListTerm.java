package org.sweble.wikitext.parser.nodes;

public class WtDefinitionListTerm
		extends
			WtContentNodeImpl
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtDefinitionListTerm()
	{
	}
	
	public WtDefinitionListTerm(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_DEFINITION_LIST_TERM;
	}
}
