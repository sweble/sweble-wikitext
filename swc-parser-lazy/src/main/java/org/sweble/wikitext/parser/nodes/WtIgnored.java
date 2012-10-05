package org.sweble.wikitext.parser.nodes;


/**
 * <h1>Ignored</h1>
 */
public class WtIgnored
		extends
			WtStringContentNode
		implements
			WtPreproNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtIgnored()
	{
		super();
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
