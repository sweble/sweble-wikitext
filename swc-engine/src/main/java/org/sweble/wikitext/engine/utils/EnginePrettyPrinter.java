package org.sweble.wikitext.engine.utils;

import java.io.StringWriter;
import java.io.Writer;

import org.sweble.wikitext.engine.nodes.CompleteEngineVisitorNoReturn;
import org.sweble.wikitext.engine.nodes.EngCompiledPage;
import org.sweble.wikitext.engine.nodes.EngNowiki;
import org.sweble.wikitext.engine.nodes.EngPage;
import org.sweble.wikitext.engine.nodes.EngSoftErrorNode;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtXmlElement;
import org.sweble.wikitext.parser.utils.WtPrettyPrinter;

public class EnginePrettyPrinter
		extends
			WtPrettyPrinter
		implements
			CompleteEngineVisitorNoReturn
{
	public void visit(EngNowiki n)
	{
		if (n.getContent().isEmpty())
		{
			p.print("<nowiki />");
		}
		else
		{
			p.print("<nowiki>");
			p.print(n.getContent());
			p.print("</nowiki>");
		}
	}
	
	@Override
	public void visit(EngCompiledPage n)
	{
		dispatch(n.getPage());
	}
	
	@Override
	public void visit(EngPage n)
	{
		iterate(n);
	}
	
	@Override
	public void visit(EngSoftErrorNode n)
	{
		visit((WtXmlElement) n);
	}
	
	// =========================================================================
	
	public static <T extends WtNode> String print(T node)
	{
		return print(new StringWriter(), node).toString();
	}
	
	public static <T extends WtNode> Writer print(Writer writer, T node)
	{
		new EnginePrettyPrinter(writer).go(node);
		return writer;
	}
	
	// =========================================================================
	
	public EnginePrettyPrinter(Writer writer)
	{
		super(writer);
	}
}
