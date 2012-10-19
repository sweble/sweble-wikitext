package org.sweble.wikitext.engine.utils;

import java.io.StringWriter;
import java.io.Writer;

import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;

public final class NoTransparentRtDataPrettyPrinter
		extends
			EngineRtDataPrettyPrinter
{
	@Override
	public Object dispatch(WtNode node)
	{
		switch (node.getNodeType())
		{
			case WtNode.NT_IGNORED:
			case WtNode.NT_XML_COMMENT:
				return null;
				
			case WtNode.NT_ONLY_INCLUDE:
				visit((WtNodeList) node);
				return null;
				
			default:
				return super.dispatch(node);
		}
	}
	
	// =====================================================================
	
	public static String print(WtNode node)
	{
		return print(new StringWriter(), node).toString();
	}
	
	public static Writer print(Writer writer, WtNode node)
	{
		new NoTransparentRtDataPrettyPrinter(writer).go(node);
		return writer;
	}
	
	// =====================================================================
	
	public NoTransparentRtDataPrettyPrinter(Writer writer)
	{
		super(writer);
	}
}
