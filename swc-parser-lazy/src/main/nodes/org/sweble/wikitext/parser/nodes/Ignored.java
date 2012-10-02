package org.sweble.wikitext.parser.nodes;

/**
 * <h1>Ignored</h1>
 */
public class Ignored
		extends
			WtStringContentNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public Ignored()
	{
		super();
		
	}
	
	public Ignored(String content)
	{
		super(content);
		
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_IGNORED;
	}
}
