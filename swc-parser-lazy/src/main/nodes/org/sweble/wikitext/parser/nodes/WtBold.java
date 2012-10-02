package org.sweble.wikitext.parser.nodes;

/**
 * <h1>WtBold</h1>
 */
public class WtBold
		extends
			WtContentNodeMarkTwo
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtBold()
	{
		super();
	}
	
	public WtBold(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_BOLD;
	}
}
