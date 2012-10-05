package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.nodes.WtLinkTitle.WtNullLinkTitle;

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
	
	public static final WtLinkTitle HAS_NO_TITLE = new WtNullLinkTitle();
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtExternalLink()
	{
		super(null, null);
	}
	
	public WtExternalLink(WtUrl target)
	{
		super(target, HAS_NO_TITLE);
	}
	
	public WtExternalLink(WtUrl target, WtLinkTitle title)
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
	
	public boolean hasTitle()
	{
		return getTitle() != HAS_NO_TITLE;
	}
	
	public final void setTitle(WtLinkTitle title)
	{
		set(1, title);
	}
	
	public final WtLinkTitle getTitle()
	{
		return (WtLinkTitle) get(1);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "target", "title" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
