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

package org.sweble.wikitext.parser.utils;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.AstNodeListImpl;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.AstStringNodeImpl;
import de.fau.cs.osr.ptk.common.ast.AstText;
import de.fau.cs.osr.utils.PrinterBase;
import de.fau.cs.osr.utils.StringUtils;

public class NewAstPrinter<T extends AstNode<T>>
		extends
			AstVisitor<T>
{
	public void visit(AstNode<T> n)
	{
		if (n.isEmpty() && !hasVisibleProperties(n))
		{
			p.indent(n.getNodeName());
			p.println("()");
		}
		else
		{
			printNode(n);
		}
	}
	
	public void visit(AstText<T> n)
	{
		if (!hasVisibleProperties(n))
		{
			p.indent("T\"");
			p.print(StringUtils.escJava(n.getContent()));
			p.println('"');
		}
		else
		{
			printNode(n);
		}
	}
	
	public void visit(AstStringNodeImpl<T> n)
	{
		if (!hasVisibleProperties(n))
		{
			p.indent(n.getNodeName());
			p.print("(\"");
			p.print(StringUtils.escJava(n.getContent()));
			p.println("\")");
		}
		else
		{
			printNode(n);
		}
	}
	
	public void visit(AstNodeListImpl<T> n)
	{
		if (hasVisibleProperties(n))
		{
			printNode(n);
		}
		else if (n.isEmpty())
		{
			p.indentln("L[]");
		}
		else
		{
			p.indentln("L[");
			
			p.incIndent();
			printListOfNodes(n);
			p.decIndent();
			
			p.indentln(']');
		}
	}
	
	/* FIXME: Remove
	public void visit(GenericContentNode<T, AstNodeListImpl<T>> n)
	{
		if (hasVisibleProperties(n))
		{
			printNode(n);
		}
		else if (n.isEmpty())
		{
			p.indent(n.getNodeName());
			p.println("[]");
		}
		else
		{
			p.indent(n.getNodeName());
			p.println('[');
			
			p.incIndent();
			printListOfNodes(n.getContent());
			p.decIndent();
			
			p.indentln(']');
		}
	}
	*/
	
	// =========================================================================
	
	protected boolean hasVisibleProperties(AstNode<T> n)
	{
		if (n.hasAttributes())
		{
			return true;
		}
		else
		{
			// Shortcuts
			int count = n.getPropertyCount();
			if (count > 2)
				return true;
			if (count == 0)
				return false;
			
			AstNodePropertyIterator i = n.propertyIterator();
			while (i.next())
			{
				if (i.getName().equals("rtd"))
				{
					if (i.getValue() != null)
						return true;
				}
				else if (!i.getName().equals("content") || !(n instanceof AstStringNodeImpl))
				{
					return true;
				}
			}
			
			return false;
		}
	}
	
	protected void printNode(AstNode<T> n)
	{
		p.indent(n.getNodeName());
		p.println('(');
		
		p.incIndent();
		printNodeContent(n);
		p.decIndent();
		
		p.indentln(')');
	}
	
	protected void printNodeContent(AstNode<T> n)
	{
		if (hasVisibleProperties(n))
		{
			printProperties(n);
			if (!n.isEmpty())
				p.println();
		}
		
		printListOfNodes(n);
	}
	
	protected void printProperties(AstNode<T> n)
	{
		Map<String, Object> props = new TreeMap<String, Object>();
		
		for (Entry<String, Object> entry : n.getAttributes().entrySet())
			props.put("{A} " + entry.getKey(), entry.getValue());
		
		AstNodePropertyIterator i = n.propertyIterator();
		while (i.next())
		{
			if (i.getValue() != null || !i.getName().equals("rtd"))
				props.put("{P} " + i.getName(), i.getValue());
		}
		
		for (Entry<String, Object> entry : props.entrySet())
		{
			p.indent(entry.getKey());
			p.print(" = ");
			printPropertyValue(entry.getValue());
		}
	}
	
	protected void printPropertyValue(Object value)
	{
		if (value == null)
		{
			p.println("null");
		}
		else if (value instanceof String)
		{
			p.print('"');
			p.print(StringUtils.escJava((String) value));
			p.println('"');
		}
		else if (value instanceof AstNode)
		{
			p.incIndent();
			@SuppressWarnings("unchecked")
			T node = (T) value;
			dispatch(node);
			p.decIndent();
		}
		else if (value instanceof Collection)
		{
			Collection<?> c = (Collection<?>) value;
			if (c.isEmpty())
			{
				p.println("Collection[]");
			}
			else
			{
				p.println("Collection[");
				
				p.incIndent();
				for (Iterator<?> k = c.iterator(); k.hasNext();)
				{
					p.indent();
					printPropertyValue(k.next());
					p.println(k.hasNext() ? "," : "");
				}
				p.decIndent();
				
				p.indentln(']');
			}
		}
		else
		{
			p.println(value);
		}
	}
	
	protected void printListOfNodes(AstNode<T> n)
	{
		for (Iterator<T> i = n.iterator(); i.hasNext();)
		{
			dispatch(i.next());
			if (i.hasNext())
			{
				p.ignoreNewlines();
				p.println(',');
			}
		}
	}
	
	// =========================================================================
	
	public static <T extends AstNode<T>> String print(T node)
	{
		return print(new StringWriter(), node).toString();
	}
	
	public static <T extends AstNode<T>> Writer print(Writer writer, T node)
	{
		new NewAstPrinter<T>(writer).go(node);
		return writer;
	}
	
	// =========================================================================
	
	protected final PrinterBase p;
	
	public NewAstPrinter(Writer writer)
	{
		this.p = new PrinterBase(writer);
	}
}
