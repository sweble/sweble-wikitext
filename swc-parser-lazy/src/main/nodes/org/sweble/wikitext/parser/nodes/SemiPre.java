package org.sweble.wikitext.parser.nodes;

/**
 * <h1>Semi Pre Block Level Element</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * SemiPreLine (Eol SemiPreLine)*
 * </p>
 * </li>
 * </ul>
 */
public class SemiPre
		extends
			WtContentNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public SemiPre()
	{
		super();
		
	}
	
	public SemiPre(WtNodeList content)
	{
		super(content);
		
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_SEMI_PRE;
	}
}
