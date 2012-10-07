package org.sweble.wikitext.parser.nodes;

public class WtPageName
		extends
			WtStringNodeImpl
		implements
			WtLinkTarget
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtPageName()
	{
		super(null);
	}
	
	public WtPageName(String content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_LINK_TARGET;
	}
	
	@Override
	public LinkTargetType getTargetType()
	{
		return LinkTargetType.PAGE;
	}
}
