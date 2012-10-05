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

package org.sweble.wikitext.parser.postprocessor;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.sweble.wikitext.parser.AstNodeTypes;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtXmlStartTag;
import org.sweble.wikitext.parser.parser.WtNamedXmlElement;
import org.sweble.wikitext.parser.utils.WtPrinter;

import de.fau.cs.osr.utils.StringUtils;

public final class ElementScopeStack
{
	Scope top = null;
	
	// =========================================================================
	
	public Scope top()
	{
		return top;
	}
	
	public void push(ScopeType type, WtNode n, boolean open)
	{
		final Scope s = new Scope(type, n, open);
		if (top == null)
		{
			top = s;
		}
		else
		{
			insertAfter(top, s);
		}
	}
	
	public Scope insertAfter(
			Scope i,
			ScopeType type,
			WtNode n,
			boolean open)
	{
		return insertAfter(i, new Scope(type, n, open));
	}
	
	private Scope insertAfter(Scope i, Scope insert)
	{
		insert.previous = i;
		insert.next = i.next;
		if (i.next != null)
			i.next.previous = insert;
		i.next = insert;
		
		if (i == top)
			top = insert;
		
		//checkIntegrity();
		return insert;
	}
	
	public Scope drop(Scope s)
	{
		if (s == top)
			throw new UnsupportedOperationException();
		
		s.next.previous = s.previous;
		if (s.previous != null)
			s.previous.next = s.next;
		
		//checkIntegrity();
		return s;
	}
	
	public Scope pop()
	{
		Scope old = top;
		if (top == null)
			throw new NoSuchElementException();
		top = top.previous();
		if (top != null)
			top.next = null;
		
		//checkIntegrity();
		return old;
	}
	
	/*
	private void checkIntegrity()
	{
		Scope i = top;
		if (i == null)
			return;
		
		Scope j = null;
		while (i.hasPrevious())
		{
			if (i.next() != j)
				throw new IllegalStateException();
			j = i;
			i = i.previous();
		}
		if (i.next() != j)
			throw new IllegalStateException();
	}
	*/
	
	// =========================================================================
	
	public void append(WtNode n)
	{
		top().append(n);
	}
	
	// =========================================================================
	
	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		s.append("ScopeStack:\n\n");
		int indent = 2;
		Scope i = top;
		if (i != null)
		{
			while (i.hasPrevious())
				i = i.previous();
		}
		while (i != null)
		{
			s.append(StringUtils.indent(
					i.toString().trim(),
					StringUtils.strrep(' ', indent)));
			s.append("\n\n");
			indent += 2;
			i = i.next();
		}
		return s.toString();
	}
	
	// =========================================================================
	
	public static final class Scope
	{
		private Scope previous;
		
		private Scope next;
		
		// =====================================================================
		
		private final ScopeType type;
		
		private final WtNode element;
		
		private final boolean open;
		
		private WtNodeList content = new WtNodeList();
		
		private LinkedList<Scope> closedInline;
		
		// =====================================================================
		
		public Scope(ScopeType type, WtNode n, boolean open)
		{
			this.type = type;
			this.element = n;
			this.open = open;
		}
		
		// =====================================================================
		
		public boolean hasPrevious()
		{
			return previous != null;
		}
		
		public Scope previous()
		{
			return previous;
		}
		
		public boolean hasNext()
		{
			return next != null;
		}
		
		public Scope next()
		{
			return next;
		}
		
		// =====================================================================
		
		public WtNode getElement()
		{
			return element;
		}
		
		public WtNodeList getContent()
		{
			return content;
		}
		
		public ScopeType getType()
		{
			return type;
		}
		
		public LinkedList<Scope> getClosedInline()
		{
			return closedInline;
		}
		
		public boolean isOpen()
		{
			return open;
		}
		
		// =====================================================================
		
		public void append(WtNode n)
		{
			content.add(n);
		}
		
		public void addClosedInline(Scope s)
		{
			if (closedInline == null)
				closedInline = new LinkedList<Scope>();
			closedInline.addFirst(s);
		}
		
		public LinkedList<Scope> clearClosedInline()
		{
			LinkedList<Scope> ci = closedInline;
			closedInline = null;
			return ci;
		}
		
		public boolean match(
				/*ScopeType otherType, */WtNamedXmlElement otherElement)
		{
			/*
			if (type != otherType)
				return false;
			*/
			
			if (element instanceof WtNamedXmlElement)
			{
				WtNamedXmlElement e = (WtNamedXmlElement) element;
				String en = e.getName();
				String on = otherElement.getName();
				/*
				if (en.startsWith("@"))
					en = en.substring(1);
				if (on.startsWith("@"))
					on = on.substring(1);
				*/
				if (en.equalsIgnoreCase(on))
					return true;
			}
			
			return false;
		}
		
		public WtNodeList clearContent()
		{
			WtNodeList c = content;
			content = new WtNodeList();
			return c;
		}
		
		public void setContent(WtNodeList content)
		{
			this.content = content;
		}
		
		// =====================================================================
		
		@Override
		public String toString()
		{
			StringBuilder s = new StringBuilder();
			s.append("Node: ");
			s.append(element.getClass().getSimpleName());
			switch (element.getNodeType())
			{
				case AstNodeTypes.NT_XML_TAG_OPEN:
				{
					s.append(": <");
					final WtXmlStartTag e = (WtXmlStartTag) element;
					s.append(e.getName());
					if (e.getXmlAttributes() != null && !e.getXmlAttributes().isEmpty())
					{
						s.append(' ');
						s.append(e.getXmlAttributes().toString());
					}
					s.append('>');
					break;
				}
				case AstNodeTypes.NT_XML_TAG_CLOSE:
				{
					s.append(": </");
					final WtXmlStartTag e = (WtXmlStartTag) element;
					s.append(e.getName());
					s.append('>');
					break;
				}
			}
			s.append("\n");
			s.append("Type: ");
			s.append(type);
			s.append("\n");
			if (closedInline != null)
			{
				s.append("Closed inline: ");
				int j = 0;
				for (Scope scope : closedInline)
				{
					if (j++ > 0)
						s.append(", ");
					s.append(scope.getElement().getClass().getSimpleName());
				}
				s.append("\n");
			}
			s.append("Content:\n");
			String c = WtPrinter.print(content);
			c = StringUtils.indent(c.trim(), "  | ");
			s.append(c);
			s.append("\n");
			return s.toString();
		}
	}
}
