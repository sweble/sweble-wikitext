package org.sweble.wikitext.parser.nodes;

/**
 * <h1>Link Option: Garbage</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * LinkOptionGarbage ::= LinkTitleContent{ALT}* &'|'
 * </p>
 * </li>
 * </ul>
 */
public class LinkOptionGarbage
		extends
			WtStringContentNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public LinkOptionGarbage()
	{
		super();
		
	}
	
	public LinkOptionGarbage(String content)
	{
		super(content);
		
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_LINK_OPTION_GARBAGE;
	}
}
