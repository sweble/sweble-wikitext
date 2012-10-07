package org.sweble.wikitext.parser.nodes;

public class WtTagExtensionBodyImpl
		extends
			WtStringNodeImpl
		implements
			WtTagExtensionBody
{
	private static final long serialVersionUID = -6588373105033239206L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtTagExtensionBodyImpl()
	{
		super(null);
	}
	
	public WtTagExtensionBodyImpl(String content)
	{
		super(content);
	}
}
