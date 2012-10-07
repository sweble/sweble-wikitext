package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.nodes.WtContentNode.WtContentNodeImpl;

public class WtParagraph
		extends
			WtContentNodeImpl
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtParagraph()
	{
	}
	
	public WtParagraph(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_PARAGRAPH;
	}
}
