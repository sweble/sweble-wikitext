package org.sweble.wikitext.parser.nodes;

/**
 * <h1>WtDefinitionListTerm</h1>
 */
public class WtDefinitionListTerm
		extends
			WtContentNode
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
