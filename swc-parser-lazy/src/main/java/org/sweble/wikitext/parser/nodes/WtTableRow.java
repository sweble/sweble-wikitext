package org.sweble.wikitext.parser.nodes;

public class WtTableRow
		extends
			WtInnerNode2
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	public WtTableRow()
	{
		super(null, null);
	}
	
	public WtTableRow(WtXmlAttributes xmlAttributes, WtBody body)
	{
		super(xmlAttributes, body);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_TABLE_ROW;
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
