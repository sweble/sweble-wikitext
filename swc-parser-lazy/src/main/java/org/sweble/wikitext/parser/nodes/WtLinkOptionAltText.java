package org.sweble.wikitext.parser.nodes;

/**
 * <h1>Link Option: AltText</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * WtLinkOptionAltText ::= Ws* 'alt=' LinkTitleContent{ALT}*
 * </p>
 * </li>
 * </ul>
 */
public class WtLinkOptionAltText
		extends
			WtContentNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtLinkOptionAltText()
	{
	}
	
	public WtLinkOptionAltText(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_LINK_OPTION_ALT_TEXT;
	}
}
