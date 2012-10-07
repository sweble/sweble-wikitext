package org.sweble.wikitext.parser.nodes;

public class WtTableHeader
		extends
			WtInnerNode2
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	public WtTableHeader()
	{
		super(null, null);
	}
	
	public WtTableHeader(WtXmlAttributes xmlAttributes, WtBody body)
	{
		super(xmlAttributes, body);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_TABLE_HEADER;
	}
	
	// =========================================================================
	// Children
	
	public final void setXmlAttributes(WtXmlAttributes xmlAttributes)
	{
		set(0, xmlAttributes);
	}
	
	public final WtXmlAttributes getXmlAttributes()
	{
		return (WtXmlAttributes) get(0);
	}
	
	public final void setBody(WtBody body)
	{
		set(1, body);
	}
	
	public final WtBody getBody()
	{
		return (WtBody) get(1);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "xmlAttributes", "body" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
