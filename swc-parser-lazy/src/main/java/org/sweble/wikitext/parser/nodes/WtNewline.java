package org.sweble.wikitext.parser.nodes;

public class WtNewline
		extends
			WtStringNodeImpl
		implements
			WtIntermediate
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtNewline()
	{
		super(null);
	}
	
	public WtNewline(String content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_NEWLINE;
	}
	
	// =========================================================================
	
	@Override
	public boolean isSynthetic()
	{
		return false;
	}
}
