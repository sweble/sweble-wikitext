package org.sweble.wikitext.parser.nodes;

public class WtDefinitionList
		extends
			WtContentNodeImpl
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtDefinitionList()
	{
	}
	
	@Override
	public int getNodeType()
	{
		return NT_DEFINITION_LIST;
	}
}
