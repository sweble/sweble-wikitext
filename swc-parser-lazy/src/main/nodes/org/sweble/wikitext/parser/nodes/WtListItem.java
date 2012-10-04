package org.sweble.wikitext.parser.nodes;

/**
 * <h1>WtListItem</h1>
 */
public class WtListItem
		extends
			WtContentNodeMarkTwo
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtListItem()
	{
		super();
	}
	
	public WtListItem(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_ITEMIZATION_ITEM;
	}
}
