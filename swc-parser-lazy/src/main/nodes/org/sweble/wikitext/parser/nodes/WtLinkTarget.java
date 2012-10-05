package org.sweble.wikitext.parser.nodes;

/**
 * <h1>Link Target</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * LinkOptionTarget ::= [^\u0000-\u001F\u007F\uFFFD<>{}|[\]]+
 * </p>
 * </li>
 * </ul>
 */
public class WtLinkTarget
		extends
			WtStringContentNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtLinkTarget()
	{
	}
	
	public WtLinkTarget(String content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_LINK_TARGET;
	}
}
