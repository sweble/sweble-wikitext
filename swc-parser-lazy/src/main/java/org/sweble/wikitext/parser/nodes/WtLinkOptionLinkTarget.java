package org.sweble.wikitext.parser.nodes;

public class WtLinkOptionLinkTarget
		extends
			WtInnerNode1
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtLinkOptionLinkTarget()
	{
		super((WtNode) null);
	}
	
	public WtLinkOptionLinkTarget(WtLinkTarget target)
	{
		super(target);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_LINK_OPTION_LINK_TARGET;
	}
	
	// =========================================================================
	// Children
	
	public final void setTarget(WtLinkTarget target)
	{
		set(0, target);
	}
	
	public final WtLinkTarget getTarget()
	{
		return (WtLinkTarget) get(0);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "target" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
