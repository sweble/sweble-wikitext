package org.sweble.wikitext.parser.nodes;

public class WtIgnored
		extends
			WtStringNodeImpl
		implements
			WtPreproNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtIgnored()
	{
		super(null);
	}
	
	public WtIgnored(String content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_IGNORED;
	}
}
