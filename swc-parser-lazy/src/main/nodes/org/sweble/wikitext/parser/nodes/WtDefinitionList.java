package org.sweble.wikitext.parser.nodes;

/**
 * <h1>WtDefinitionList</h1>
 */
public class WtDefinitionList
		extends
			WtContentNodeMarkTwo
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtDefinitionList()
	{
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_DEFINITION_LIST;
	}
}
