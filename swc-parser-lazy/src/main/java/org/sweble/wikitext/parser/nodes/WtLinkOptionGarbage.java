package org.sweble.wikitext.parser.nodes;

public class WtLinkOptionGarbage
		extends
			WtStringNodeImpl
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtLinkOptionGarbage()
	{
		super(null);
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
