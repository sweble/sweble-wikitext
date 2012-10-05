package org.sweble.wikitext.parser.nodes;

/**
 * <h1>Link Option: Garbage</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * WtLinkOptionGarbage ::= LinkTitleContent{ALT}* &'|'
 * </p>
 * </li>
 * </ul>
 */
public class WtLinkOptionGarbage
		extends
			WtStringContentNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtLinkOptionGarbage()
	{
	}
	
	public WtLinkOptionGarbage(String content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_LINK_OPTION_GARBAGE;
	}
}
