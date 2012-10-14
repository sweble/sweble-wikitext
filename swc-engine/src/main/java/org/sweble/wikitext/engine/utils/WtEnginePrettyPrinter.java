package org.sweble.wikitext.engine.utils;

import java.io.StringWriter;
import java.io.Writer;

import org.sweble.wikitext.engine.nodes.EngNowiki;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.WtPrettyPrinter;

public class WtEnginePrettyPrinter
		extends
			WtPrettyPrinter
{
	public void visit(EngNowiki n)
	{
		p.print("<nowiki>");
		p.print(n.getContent());
		p.print("</nowiki>");
	}
	
	// =========================================================================
	
	public static <T extends WtNode> String print(T node)
	{
		return print(new StringWriter(), node).toString();
	}
	
	public static <T extends WtNode> Writer print(Writer writer, T node)
	{
		new WtEnginePrettyPrinter(writer).go(node);
		return writer;
	}
	
	// =========================================================================
	
	public WtEnginePrettyPrinter(Writer writer)
	{
		super(writer);
	}
}
