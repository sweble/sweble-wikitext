package org.sweble.wikitext.parser.nodes;

/**
 * <h1>WtItalics</h1>
 */
public class WtItalics
		extends
			WtContentNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtItalics()
	{
		super();
	}
	
	public WtItalics(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_ITALICS;
	}
}
