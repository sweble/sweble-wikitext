package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.postprocessor.WtPreproNode;

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
		return org.sweble.wikitext.parser.AstNodeTypes.NT_IGNORED;
	}
}
