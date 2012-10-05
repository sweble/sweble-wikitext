package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.postprocessor.WtIntermediate;

/**
 * <h1>Semi Pre Line</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * Eol? ' ' SemiPreContentStar
 * </p>
 * </li>
 * </ul>
 */
public class WtSemiPreLine
		extends
			WtContentNodeMarkTwo
		implements
			WtIntermediate
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtSemiPreLine()
	{
	}
	
	public WtSemiPreLine(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_SEMI_PRE_LINE;
	}
	
	// =========================================================================
	
	@Override
	public boolean isSynthetic()
	{
		return false;
	}
}
