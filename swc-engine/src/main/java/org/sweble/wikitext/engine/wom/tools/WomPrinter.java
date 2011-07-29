/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sweble.wikitext.engine.wom.tools;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.sweble.wikitext.engine.wom.WomAttribute;
import org.sweble.wikitext.engine.wom.WomNode;

import de.fau.cs.osr.utils.StringUtils;

public class WomPrinter
        extends
            WomVisitor
{
	protected PrintWriter out;
	
	private String indentStr = new String();
	
	// =========================================================================
	
	public WomPrinter(Writer writer)
	{
		this.out = new PrintWriter(writer);
	}
	
	@Override
	protected Object after(WomNode node, Object result)
	{
		this.out.close();
		return super.after(node, result);
	}
	
	// =========================================================================
	
	public static String print(WomNode node)
	{
		StringWriter writer = new StringWriter();
		new WomPrinter(writer).go(node);
		return writer.toString();
	}
	
	public static Writer print(Writer writer, WomNode node)
	{
		new WomPrinter(writer).go(node);
		return writer;
	}
	
	// =========================================================================
	
	public void visit(WomNode n)
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
	
	public void visit(WomAttribute a)
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
