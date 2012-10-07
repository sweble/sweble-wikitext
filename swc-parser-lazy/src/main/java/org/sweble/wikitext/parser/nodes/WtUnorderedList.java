package org.sweble.wikitext.parser.nodes;

public class WtUnorderedList
		extends
			WtContentNodeImpl
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtUnorderedList()
	{
	}
	
	public WtUnorderedList(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_UNORDERED_LIST;
	}
}
