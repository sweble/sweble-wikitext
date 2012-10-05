package org.sweble.wikitext.parser.nodes;

/**
 * <h1>External Link</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * '[' WtUrl Space* ']'
 * </p>
 * </li>
 * <li>
 * <p>
 * '[' WtUrl Space+ Title ']'
 * </p>
 * </li>
 * </ul>
 * <h2>The title can contain</h2>
 * <ul>
 * <li>
 * <p>
 * WtInternalLink
 * </p>
 * </li>
 * <li>
 * <p>
 * WtPageSwitch
 * </p>
 * </li>
 * <li>
 * <p>
 * WtParserEntity
 * </p>
 * </li>
 * <li>
 * <p>
 * WtSignature
 * </p>
 * </li>
 * <li>
 * <p>
 * WtTicks
 * </p>
 * </li>
 * <li>
 * <p>
 * XmlReference
 * </p>
 * </li>
 * </ul>
 * <h2>The title cannot contain</h2>
 * <ul>
 * <li>
 * <p>
 * Newline
 * </p>
 * <ul>
 * <li>
 * <p>
 * Tables
 * </p>
 * </li>
 * <li>
 * <p>
 * Headings
 * </p>
 * </li>
 * <li>
 * <p>
 * Horizontal lines
 * </p>
 * </li>
 * <li>
 * <p>
 * Block level elements
 * </p>
 * </li>
 * </ul>
 * </li>
 * <li>
 * <p>
 * WtExternalLink
 * </p>
 * </li>
 * <li>
 * <p>
 * PlainExternalLink
 * </p>
 * </li>
 * <li>
 * <p>
 * WtXmlElement(*)
 * </p>
 * </li>
 * </ul>
 * <h2>The title can syntactically not contain</h2>
 * <ul>
 * <li>
 * <p>
 * Newlines
 * </p>
 * </li>
 * </ul>
 */
public class WtExternalLink
		extends
			WtInnerNode2
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtExternalLink()
	{
		super(new WtUrl(), new WtNodeList());
	}
	
	public WtExternalLink(WtUrl target)
	{
		super(target, new WtNodeList());
	}
	
	public WtExternalLink(WtUrl target, WtNodeList title)
	{
		super(target, title);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_EXTERNAL_LINK;
	}
	
	// =========================================================================
	// Children
	
	public final void setTarget(WtUrl target)
	{
		set(0, target);
	}
	
	public final WtUrl getTarget()
	{
		return (WtUrl) get(0);
	}
	
	public final void setTitle(WtNodeList title)
	{
		set(1, title);
	}
	
	public final WtNodeList getTitle()
	{
		return (WtNodeList) get(1);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "target", "title" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
