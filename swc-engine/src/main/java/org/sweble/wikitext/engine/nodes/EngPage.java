package org.sweble.wikitext.engine.nodes;

import org.sweble.wikitext.parser.nodes.WtContentNode.WtContentNodeImpl;
import org.sweble.wikitext.parser.nodes.WtNodeList;

public class EngPage
		extends
			WtContentNodeImpl
		implements
			EngNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	protected EngPage()
	{
	}
	
	/**
	 * Swaps content from received WtNodeList object into this object and
	 * therefore EMPTIES the received list!
	 */
	protected EngPage(WtNodeList content)
	{
		exchange(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_PAGE;
	}
}
