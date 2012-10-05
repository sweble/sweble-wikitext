package org.sweble.wikitext.parser.nodes;

/**
 * <h1>WtTable</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * S* '{|' TableAttribute* Eol TableContent* S* '|}'
 * </p>
 * </li>
 * </ul>
 */
public class WtTable
		extends
			WtInnerNode2
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtTable()
	{
		super(new WtNodeListImpl(), new WtNodeListImpl());
	}
	
	public WtTable(WtNodeList xmlAttributes, WtNodeList body)
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
