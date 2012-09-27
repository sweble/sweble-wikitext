package org.sweble.wikitext.parser.nodes;

/**
 * <h1>Horizontal Rule</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * '----' '-'*
 * </p>
 * </li>
 * </ul>
 */
public class HorizontalRule
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public HorizontalRule()
	{
		super();
		
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_HORIZONTAL_RULE;
	}
}
