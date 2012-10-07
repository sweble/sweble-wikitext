package org.sweble.wikitext.engine;

import org.sweble.wikitext.parser.nodes.WtContentNode.WtContentNodeImpl;
import org.sweble.wikitext.parser.nodes.WtNodeList;

public class Page
		extends
			WtContentNodeImpl
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public Page()
	{
	}
	
	public Page(WtNodeList content)
	{
		super(content);
	}
}
