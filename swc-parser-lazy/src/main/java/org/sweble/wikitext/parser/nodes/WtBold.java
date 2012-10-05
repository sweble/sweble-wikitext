package org.sweble.wikitext.parser.nodes;

/**
 * <h1>WtBold</h1>
 */
public class WtBold
		extends
			WtContentNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtBold()
	{
	}
	
	public WtBold(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_BOLD;
	}
}
