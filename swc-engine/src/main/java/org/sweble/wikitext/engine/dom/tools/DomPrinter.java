package org.sweble.wikitext.engine.dom.tools;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.sweble.wikitext.engine.dom.DomAttribute;
import org.sweble.wikitext.engine.dom.DomNode;

import de.fau.cs.osr.utils.StringUtils;

public class DomPrinter
        extends
            DomVisitor
{
	protected PrintWriter out;
	
	private String indentStr = new String();
	
	// =========================================================================
	
	public DomPrinter(Writer writer)
	{
		this.out = new PrintWriter(writer);
	}
	
	@Override
	protected Object after(DomNode node, Object result)
	{
		this.out.close();
		return super.after(node, result);
	}
	
	// =========================================================================
	
	public static String print(DomNode node)
	{
		StringWriter writer = new StringWriter();
		new DomPrinter(writer).go(node);
		return writer.toString();
	}
	
	public static Writer print(Writer writer, DomNode node)
	{
		new DomPrinter(writer).go(node);
		return writer;
	}
	
	// =========================================================================
	
	public void visit(DomNode n)
	{
		indent();
		out.print('<');
		out.print(n.getNodeName());
		iterateAttributes(n);
		if (n.hasChildNodes())
		{
			out.print(">\n");
			
			incIndent();
			iterateChildren(n);
			decIndent();
			
			indent();
			out.print("</");
			out.print(n.getNodeName());
			out.print(">\n");
		}
		else if (n.getText() != null)
		{
			out.print(">");
			out.print(StringUtils.escHtml(n.getText()));
			out.print("</");
			out.print(n.getNodeName());
			out.print(">\n");
		}
		else
		{
			out.print(" />\n");
		}
	}
	
	public void visit(DomAttribute a)
	{
		out.print(' ');
		out.print(a.getName());
		out.print("=\"");
		out.print(StringUtils.escHtml(a.getValue()));
		out.print('"');
	}
	
	// =========================================================================
	
	protected void incIndent()
	{
		this.indentStr += "  ";
	}
	
	protected void decIndent()
	{
		this.indentStr =
		        this.indentStr.substring(0, this.indentStr.length() - 2);
	}
	
	protected void indent()
	{
		out.print(this.indentStr);
	}
}
