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
package org.sweble.wikitext.engine.astwom.adapters;

import java.util.ListIterator;

import org.sweble.wikitext.engine.astwom.WomBackbone;
import org.sweble.wikitext.engine.astwom.FullElement;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.engine.wom.WomText;
import org.sweble.wikitext.lazy.AstNodeTypes;
import org.sweble.wikitext.lazy.parser.Whitespace;
import org.sweble.wikitext.lazy.utils.TextUtils;
import org.sweble.wikitext.lazy.utils.XmlCharRef;
import org.sweble.wikitext.lazy.utils.XmlEntityRef;
import org.sweble.wikitext.lazy.utils.XmlEntityResolver;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;

public class TextAdapter
        extends
            WomBackbone
        implements
            WomText
{
	private static final long serialVersionUID = 1L;
	
	private TextPart parts = null;
	
	private String text = "";
	
	// =========================================================================
	
	public TextAdapter(Text astNode)
	{
		super(astNode);
		text = astNode.getContent();
	}
	
	public TextAdapter(Whitespace ws)
	{
		super(ws);
		text = Toolbox.toText(null, ws);
	}
	
	public TextAdapter(XmlEntityResolver entityResolver, XmlEntityRef ref)
	{
		super(ref);
		text = Toolbox.toText(entityResolver, ref);
	}
	
	public TextAdapter(XmlCharRef ref)
	{
		super(ref);
		text = Toolbox.toText(null, ref);
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "text";
	}
	
	@Override
	public WomNodeType getNodeType()
	{
		return WomNodeType.TEXT;
	}
	
	@Override
	public String getText()
	{
		return text;
	}
	
	@Override
	public String getValue()
	{
		return getText();
	}
	
	// =========================================================================
	
	private static final class TextPart
	{
		public AstNode astNode;
		
		public int offset;
		
		public String text;
		
		public TextPart next = null;
		
		public TextPart(AstNode astNode, int offset, String text)
		{
			this.astNode = astNode;
			this.offset = offset;
			this.text = text;
		}
	}
	
	public void append(String text, AstNode n)
	{
		String curText = getText();
		
		TextPart part = new TextPart(n, curText.length(), text);
		
		this.text += text;
		
		TextPart p;
		for (p = this.parts; p != null; p = p.next)
		{
			if (p.next == null)
			{
				p.next = part;
				break;
			}
		}
		if (p == null)
			this.parts = part;
	}
	
	public void append(XmlEntityResolver entityResolver, AstNode n)
	{
		append(Toolbox.toText(entityResolver, n), n);
	}
	
	private AstNode getLastPart()
	{
		TextPart p = this.parts;
		while (true)
		{
			if (p.next == null)
				return p.astNode;
			p = p.next;
		}
	}
	
	private NodeList getTextContainer()
	{
		return ((FullElement) getParent()).getAstChildContainer();
	}
	
	// =========================================================================
	
	@Override
	public void appendText(String text) throws UnsupportedOperationException
	{
		if (this.parts == null)
		{
			boolean isWs =
			        getAstNode().getNodeType() == AstNodeTypes.NT_WHITESPACE;
			
			if (isWs || TextUtils.needsEscaping(text, false))
			{
				NodeList astParts = TextUtils.stringToAst(text, false);
				Toolbox.insertAstNodeAfter(getTextContainer(), astParts, getAstNode());
				for (AstNode n : astParts)
					append(TextUtils.getSimpleXmlEntityResolver(), n);
			}
			else
			{
				this.text += text;
				Text textNode = (Text) getAstNode();
				textNode.setContent(this.text);
			}
		}
		else
		{
			NodeList astParts = TextUtils.stringToAst(text, false);
			Toolbox.insertAstNodeAfter(getTextContainer(), astParts, getLastPart());
			for (AstNode n : astParts)
				append(TextUtils.getSimpleXmlEntityResolver(), n);
		}
	}
	
	@Override
	public void deleteText(int from, int length) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		if (from < 0 || length + from > this.text.length())
			throw new IndexOutOfBoundsException();
		
		if (length == 0)
			return;
		
		if (this.text.length() == length)
		{
			this.text = "";
			clear();
		}
		else
		{
			if (this.parts == null)
			{
				this.text = this.text.substring(0, from) +
				        this.text.substring(from + length);
				
				if (getAstNode().getNodeType() != AstNodeTypes.NT_WHITESPACE)
				{
					Text textNode = (Text) getAstNode();
					textNode.setContent(this.text);
				}
				else
				{
					deleteText(null, (Whitespace) getAstNode(), from, length);
				}
			}
			else
			{
				String firstText =
				        this.text.substring(0, this.parts.offset);
				
				this.text = this.text.substring(0, from) +
				        this.text.substring(from + length);
				
				TextPart first = new TextPart(getAstNode(), 0, firstText);
				first.next = this.parts;
				
				deleteText(from, length, first);
			}
		}
	}
	
	private void deleteText(int from, int length, TextPart first)
	{
		TextPart prev = null;
		TextPart p = first;
		
		while (p.next != null && from >= p.next.offset)
		{
			prev = p;
			p = p.next;
		}
		
		ListIterator<AstNode> i = getTextContainer().listIterator();
		
		int correct = 0;
		while (true)
		{
			TextPart next = p.next;
			
			AstNode replacement = deleteText(p, from - p.offset, length);
			if (replacement == null)
			{
				removeAstNode(i, p.astNode);
				if (prev == null)
				{
					first = next;
					prev = null;
				}
				else
				{
					prev.next = next;
				}
			}
			else
			{
				prev = p;
				
				if (replacement != p.astNode)
					replaceAstNode(i, p.astNode, replacement);
			}
			
			if (next == null)
				break;
			
			int lenRemoved = Math.min(next.offset - from, length);
			correct += lenRemoved;
			length -= lenRemoved;
			if (length <= 0)
			{
				do
				{
					next.offset -= correct;
					next = next.next;
				} while (next != null);
				break;
			}
			
			next.offset -= correct;
			from = next.offset;
			p = next;
		}
		
		this.parts = first.next;
		if (first.astNode != getAstNode())
			setAstNode(first.astNode);
	}
	
	private void clear() throws AssertionError, InternalError
	{
		NodeList container = getTextContainer();
		
		AstNode n = getAstNode();
		
		Text empty = new Text("");
		Toolbox.insertAstNode(container, empty, n);
		
		ListIterator<AstNode> i = container.listIterator();
		while (i.hasNext())
		{
			if (i.next() == n)
			{
				i.remove();
				break;
			}
		}
		
		TextPart p = this.parts;
		while (p != null && i.hasNext())
		{
			if (i.next() == p.astNode)
			{
				i.remove();
				p = p.next;
			}
		}
		
		if (p != null)
			throw new InternalError();
		
		setAstNode(empty);
		this.parts = null;
	}
	
	private static void removeAstNode(ListIterator<AstNode> i, AstNode astNode)
	{
		while (i.hasNext())
		{
			if (i.next() == astNode)
			{
				i.remove();
				return;
			}
		}
		throw new InternalError();
	}
	
	private static void replaceAstNode(ListIterator<AstNode> i, AstNode astNode, AstNode replacement)
	{
		while (i.hasNext())
		{
			if (i.next() == astNode)
			{
				i.set(replacement);
				return;
			}
		}
		throw new InternalError();
	}
	
	private static AstNode deleteText(TextPart p, int from, int length)
	{
		AstNode n = p.astNode;
		switch (n.getNodeType())
		{
			case AstNode.NT_TEXT:
				return deleteText(p, (Text) n, from, length);
				
			case AstNodeTypes.NT_WHITESPACE:
				return deleteText(p, (Whitespace) n, from, length);
				
			case AstNodeTypes.NT_XML_CHAR_REF:
				return deleteText(p, (XmlCharRef) n, from, length, p.text);
				
			case AstNodeTypes.NT_XML_ENTITY_REF:
				return deleteText(p, (XmlEntityRef) n, from, length, p.text);
				
			default:
				throw new IllegalArgumentException();
		}
	}
	
	private static Text deleteText(TextPart p, Text n, int from, int length)
	{
		String text = n.getContent();
		
		if (from + length > text.length())
			length = text.length() - from;
		
		if (from == 0 && length == text.length())
			return null;
		
		text = text.substring(0, from) +
		        text.substring(from + length);
		
		if (p != null)
			p.text = text;
		
		n.setContent(text);
		
		return n;
	}
	
	private static AstNode deleteText(TextPart p, XmlEntityRef n, int from, int length, String text)
	{
		if (from + length > text.length())
			length = text.length() - from;
		
		if (from == 0 && length == text.length())
			return null;
		
		p.text = p.text.substring(0, from) +
		        p.text.substring(from + length);
		
		return new Text(p.text);
	}
	
	private static AstNode deleteText(TextPart p, XmlCharRef n, int from, int length, String text)
	{
		if (from + length > text.length())
			length = text.length() - from;
		
		if (from == 0 && length == text.length())
			return null;
		
		p.text = p.text.substring(0, from) +
		        p.text.substring(from + length);
		
		return new Text(p.text);
	}
	
	private static Whitespace deleteText(TextPart p, Whitespace n, int from, int length)
	{
		NodeList list = n.getContent();
		if (list.size() > 1)
		{
			String text = "";
			for (AstNode m : list)
			{
				if (m.getNodeType() != AstNode.NT_TEXT)
					throw new InternalError();
				text += ((Text) m).getContent();
			}
			
			Text m = (Text) list.get(0);
			m.setContent(text);
			
			list.clear();
			list.add(m);
		}
		
		if (list.size() != 1 || list.get(0).getNodeType() != AstNode.NT_TEXT)
			throw new InternalError();
		
		if (deleteText(null, (Text) list.get(0), from, length) == null)
			return null;
		
		if (p != null)
			p.text = ((Text) list.get(0)).getContent();
		
		return n;
	}
	
	@Override
	public void insertText(int at, String text) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		// TODO: Implement
	}
	
	@Override
	public String replaceText(String text) throws UnsupportedOperationException
	{
		// TODO: Implement
		return null;
	}
	
	@Override
	public String replaceText(int from, int length, String text) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		// TODO: Implement
		return null;
	}
}
