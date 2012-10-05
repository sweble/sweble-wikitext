package org.sweble.wikitext.parser.nodes;

public class WtSectionBody
		extends
			WtContentNodeMarkTwo
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtSectionBody()
	{
		super();
	}
	
	public WtSectionBody(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_SECTION_BODY;
	}
}
