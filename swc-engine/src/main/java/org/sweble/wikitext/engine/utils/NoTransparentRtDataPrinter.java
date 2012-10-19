package org.sweble.wikitext.engine.utils;

import java.io.StringWriter;
import java.io.Writer;

import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.utils.RtDataPrinter;

public final class NoTransparentRtDataPrinter
		extends
			RtDataPrinter
{
	@Override
	public void dispatch(WtNode node)
	{
		switch (node.getNodeType())
		{
			case WtNode.NT_IGNORED:
			case WtNode.NT_XML_COMMENT:
				break;
			
			case WtNode.NT_ONLY_INCLUDE:
				printNodeList((WtNodeList) node);
				break;
			
			default:
				super.dispatch(node);
				break;
		}
	}
	
	// =====================================================================
	
	public static String print(WtNode node)
	{
		return print(new StringWriter(), node).toString();
	}
	
	public static Writer print(Writer writer, WtNode node)
	{
		new NoTransparentRtDataPrinter(writer).go(node);
		return writer;
	}
	
	// =====================================================================
	
	public NoTransparentRtDataPrinter(Writer writer)
	{
		super(writer);
	}
}
