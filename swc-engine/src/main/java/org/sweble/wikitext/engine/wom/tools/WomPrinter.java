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
import java.util.Stack;

import org.sweble.wikitext.engine.wom.WomAttribute;
import org.sweble.wikitext.engine.wom.WomBlockElement;
import org.sweble.wikitext.engine.wom.WomInlineElement;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomNodeType;

import de.fau.cs.osr.utils.StringUtils;

public class WomPrinter
		extends
			WomVisitor
{
	protected final PrintWriter out;
	
	private String indentStr = new String();
	
	private Stack<Boolean> blockContent = new Stack<Boolean>();
	
	private final boolean explicitTextNodes;
	
	private boolean firstLine = true;
	
	// =========================================================================
	
	public WomPrinter(Writer writer, boolean explicitTextNodes)
	{
		this.out = new PrintWriter(writer);
		this.explicitTextNodes = explicitTextNodes;
	}
	
	@Override
	protected boolean before(WomNode node)
	{
		this.blockContent.push(true);
		return super.before(node);
	}
	
	@Override
	protected Object after(WomNode node, Object result)
	{
		this.blockContent.pop();
		this.out.close();
		return super.after(node, result);
	}
	
	// =========================================================================
	
	public static String print(WomNode node)
	{
		return print(node, false);
	}
	
	public static String print(WomNode node, boolean explicitTextNodes)
	{
		StringWriter writer = new StringWriter();
		new WomPrinter(writer, explicitTextNodes).go(node);
		return writer.toString();
	}
	
	public static Writer print(Writer writer, WomNode node)
	{
		return print(writer, node, false);
	}
	
	public static Writer print(
			Writer writer,
			WomNode node,
			boolean explicitTextNodes)
	{
		new WomPrinter(writer, explicitTextNodes).go(node);
		return writer;
	}
	
	// =========================================================================
	
	public void visit(WomNode n)
	{
		if (n.hasChildNodes())
		{
			if (isBlock(n))
			{
				println();
				indent();
				out.print('<');
				out.print(n.getNodeName());
				iterateAttributes(n);
				out.print(">");
				
				blockContent.push(true);
				incIndent();
				iterateChildren(n);
				decIndent();
				
				if (blockContent.peek())
				{
					println();
					indent();
				}
				out.print("</");
				out.print(n.getNodeName());
				out.print(">");
				
				blockContent.pop();
			}
			else
			{
				blockContent.set(blockContent.size() - 1, false);
				
				out.print('<');
				out.print(n.getNodeName());
				iterateAttributes(n);
				out.print('>');
				
				iterateChildren(n);
				
				out.print("</");
				out.print(n.getNodeName());
				out.print('>');
			}
		}
		else if (n.getNodeType() == WomNodeType.TEXT)
		{
			blockContent.set(blockContent.size() - 1, false);
			
			if (explicitTextNodes)
			{
				out.print("<text>");
				out.print(StringUtils.escHtml(n.getText()));
				out.print("</text>");
			}
			else
			{
				out.print(StringUtils.escHtml(n.getText()));
			}
		}
		else if (n.getNodeType() == WomNodeType.COMMENT)
		{
			if (blockContent.peek())
			{
				println();
				indent();
			}
			
			out.print("<comment>");
			out.print(n.getValue());
			out.print("</comment>");
		}
		else
		{
			if (isBlock(n))
			{
				println();
				indent();
				out.print('<');
				out.print(n.getNodeName());
				iterateAttributes(n);
				out.print(" />");
			}
			else
			{
				blockContent.set(blockContent.size() - 1, false);
				
				out.print('<');
				out.print(n.getNodeName());
				iterateAttributes(n);
				out.print(" />");
			}
		}
	}
	
	private void println()
	{
		if (!firstLine)
		{
			out.println();
		}
		else
		{
			firstLine = false;
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
	
	private boolean isBlock(WomNode n)
	{
		if (n instanceof WomBlockElement)
			return true;
		if (!(n instanceof WomInlineElement))
			return true;
		return false;
	}
}
