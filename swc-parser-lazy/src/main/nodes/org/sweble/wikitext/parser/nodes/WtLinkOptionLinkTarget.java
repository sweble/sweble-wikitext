package org.sweble.wikitext.parser.nodes;

/**
 * <h1>Link Option: LinkTarget</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * WtLinkOptionLinkTarget ::= Ws* 'link=' LinkTarget Ws*
 * </p>
 * </li>
 * <li>
 * <p>
 * WtLinkOptionLinkTarget ::= Ws* 'link=' WtUrl Ws*
 * </p>
 * </li>
 * </ul>
 */
public class WtLinkOptionLinkTarget
		extends
			WtInnerNode1
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtLinkOptionLinkTarget()
	{
		super((WtNode) null);
	}
	
	public WtLinkOptionLinkTarget(WtNode target)
	{
		super(target);
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_LINK_OPTION_LINK_TARGET;
	}
	
	// =========================================================================
	// Children
	
	public final void setTarget(WtNode target)
	{
		set(0, target);
	}
	
	public final WtNode getTarget()
	{
		return (WtNode) get(0);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "target" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
