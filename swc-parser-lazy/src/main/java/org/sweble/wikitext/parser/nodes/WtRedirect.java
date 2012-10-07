package org.sweble.wikitext.parser.nodes;

public class WtRedirect
		extends
			WtInnerNode1
		implements
			WtPreproNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtRedirect()
	{
		super((WtNode) null);
	}
	
	public WtRedirect(WtPageName target)
	{
		super(target);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_REDIRECT;
	}
	
	// =========================================================================
	// Children
	
	public final void setTarget(WtPageName target)
	{
		set(0, target);
	}
	
	public final WtPageName getTarget()
	{
		return (WtPageName) get(0);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "target" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
