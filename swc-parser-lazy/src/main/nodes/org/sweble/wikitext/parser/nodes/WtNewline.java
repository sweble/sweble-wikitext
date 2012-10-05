package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.postprocessor.WtIntermediate;

/**
 * <h1>Newline</h1>
 */
public class WtNewline
		extends
			WtStringContentNode
		implements
			WtIntermediate
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
		return NT_NEWLINE;
	}
	
	// =========================================================================
	
	@Override
	public boolean isSynthetic()
	{
		return false;
	}
}
