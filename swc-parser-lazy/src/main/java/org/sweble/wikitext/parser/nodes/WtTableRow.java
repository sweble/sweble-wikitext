package org.sweble.wikitext.parser.nodes;

/**
 * <h1>WtTableRow</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * WS* '|-' TableAttribute* S*
 * </p>
 * </li>
 * </ul>
 */
public class WtTableRow
		extends
			WtInnerNode2
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtTableRow()
	{
		super(new WtNodeList(), new WtNodeList());
	}
	
	public WtTableRow(WtNodeList xmlAttributes, WtNodeList body)
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
	
	public final void setXmlAttributes(WtNodeList xmlAttributes)
	{
		set(0, xmlAttributes);
	}
	
	public final WtNodeList getXmlAttributes()
	{
		return (WtNodeList) get(0);
	}
	
	public final void setBody(WtNodeList body)
	{
		set(1, body);
	}
	
	public final WtNodeList getBody()
	{
		return (WtNodeList) get(1);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "xmlAttributes", "body" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
