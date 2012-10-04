package org.sweble.wikitext.parser.nodes;

/**
 * <h1>Newline</h1>
 */
public class WtNewline
		extends
			WtStringContentNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtNewline()
	{
	}
	
	public WtNewline(String content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_NEWLINE;
	}
}
