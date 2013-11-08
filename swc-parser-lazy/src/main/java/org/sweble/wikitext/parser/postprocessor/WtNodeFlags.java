package org.sweble.wikitext.parser.postprocessor;

import org.sweble.wikitext.parser.nodes.WtNode;

public class WtNodeFlags
{
	public static final String PARSER_RECOGNIZED = "parser-recognized";
	
	public static void setParserRecognized(WtNode pEndTag)
	{
		pEndTag.setBooleanAttribute(PARSER_RECOGNIZED, true);
	}
	
	public static boolean isParserRecognized(WtNode n)
	{
		return n.getBooleanAttribute(PARSER_RECOGNIZED);
	}
	
	// =========================================================================
	
	public static final String REPAIR = "repair";
	
	public static WtNode setRepairNode(WtNode node)
	{
		node.setAttribute(REPAIR, true);
		return node;
	}
	
	public static boolean isRepairNode(WtNode node)
	{
		return node.getBooleanAttribute(REPAIR);
	}
	
	// =========================================================================
	
	private static final String IMPLICIT = "implicit";
	
	public static void setImplicit(WtNode node)
	{
		node.setBooleanAttribute(IMPLICIT, true);
	}
	
	public static boolean isImplicit(WtNode node)
	{
		return node.getBooleanAttribute(IMPLICIT);
	}
}
