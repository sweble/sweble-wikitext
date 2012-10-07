package org.sweble.wikitext.parser.nodes;

public class WtExternalLink
		extends
			WtInnerNode2
{
	private static final long serialVersionUID = 1L;
	
	public static final WtLinkTitle HAS_NO_TITLE = WtLinkTitle.NULL;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtExternalLink()
	{
		super(null, null);
	}
	
	public WtExternalLink(WtUrl target)
	{
		super(target, HAS_NO_TITLE);
	}
	
	public WtExternalLink(WtUrl target, WtLinkTitle title)
	{
		super(target, title);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_EXTERNAL_LINK;
	}
	
	// =========================================================================
	// Children
	
	public final void setTarget(WtUrl target)
	{
		set(0, target);
	}
	
	public final WtUrl getTarget()
	{
		return (WtUrl) get(0);
	}
	
	public boolean hasTitle()
	{
		return getTitle() != HAS_NO_TITLE;
	}
	
	public final void setTitle(WtLinkTitle title)
	{
		set(1, title);
	}
	
	public final WtLinkTitle getTitle()
	{
		return (WtLinkTitle) get(1);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "target", "title" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
