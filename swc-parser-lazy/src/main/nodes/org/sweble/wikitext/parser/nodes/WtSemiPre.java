package org.sweble.wikitext.parser.nodes;

/**
 * <h1>Semi Pre Block Level Element</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * WtSemiPreLine (Eol WtSemiPreLine)*
 * </p>
 * </li>
 * </ul>
 */
public class WtSemiPre
		extends
			WtContentNodeMarkTwo
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtSemiPre()
	{
	}
	
	public WtSemiPre(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_SEMI_PRE;
	}
}
