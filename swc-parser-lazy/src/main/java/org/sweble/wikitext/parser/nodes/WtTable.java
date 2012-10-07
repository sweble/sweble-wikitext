package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.nodes.WtBody.WtNullBody;

public class WtTable
		extends
			WtInnerNode2
{
	private static final long serialVersionUID = 1L;
	
	private static final WtBody NO_BODY = WtBody.NULL_BODY;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtTable()
	{
		super(null, null);
	}
	
	public WtTable(WtXmlAttributes xmlAttributes)
	{
		super(xmlAttributes, WtBody.NULL_BODY);
	}
	
	public WtTable(WtXmlAttributes xmlAttributes, WtBody body)
	{
		super(xmlAttributes, body);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_TABLE;
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
	
	public final boolean hasBody()
	{
		return getBody() != NO_BODY;
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
