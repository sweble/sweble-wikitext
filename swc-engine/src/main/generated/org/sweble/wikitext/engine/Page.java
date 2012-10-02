package org.sweble.wikitext.engine;

import org.sweble.wikitext.parser.nodes.WtContentNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;

/**
 * <h1>Page Node</h1>
 */
public class Page
		extends
			WtContentNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public Page()
	{
		super();
		
	}
	
	public Page(WtNodeList content)
	{
		super(content);
		
	}
}
