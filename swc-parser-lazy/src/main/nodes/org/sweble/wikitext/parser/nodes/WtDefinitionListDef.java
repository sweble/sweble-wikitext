package org.sweble.wikitext.parser.nodes;

/**
 * <h1>WtDefinitionListDef</h1>
 */
public class WtDefinitionListDef
		extends
			WtContentNodeMarkTwo
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtDefinitionListDef()
	{
	}
	
	public WtDefinitionListDef(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_DEFINITION_DEFINITION;
	}
}
