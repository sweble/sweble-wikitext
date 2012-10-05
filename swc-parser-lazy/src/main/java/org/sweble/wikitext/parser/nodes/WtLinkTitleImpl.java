package org.sweble.wikitext.parser.nodes;

/**
 * <h1>Link Title</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * WtLinkTitle ::= LinkTitleContent{TITLE}*
 * </p>
 * </li>
 * </ul>
 * <h2>LinkTitleContent can contain</h2>
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
 * Horizontal rules
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
 * WtSignature
 * </p>
 * </li>
 * <li>
 * <p>
 * WtInternalLink
 * </p>
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
 * WtTicks
 * </p>
 * </li>
 * <li>
 * <p>
 * XmlReference
 * </p>
 * </li>
 * <li>
 * <p>
 * WtXmlElement
 * </p>
 * </li>
 * <li>
 * <p>
 * WtParserEntity
 * </p>
 * </li>
 * </ul>
 * <h2>The LinkTitleContent cannot contain</h2>
 * <ul>
 * <li>
 * <p>
 * WtPageSwitch
 * </p>
 * </li>
 * </ul>
 */
public class WtLinkTitleImpl
		extends
			WtContentNode
		implements
			WtLinkTitle
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtLinkTitleImpl()
	{
	}
	
	public WtLinkTitleImpl(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_LINK_TITLE;
	}
}
