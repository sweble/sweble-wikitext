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

import org.sweble.wikitext.engine.astwom.FullElement;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.astwom.WomBackbone;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.engine.wom.WomText;
import org.sweble.wikitext.parser.AstNodeTypes;
import org.sweble.wikitext.parser.nodes.Newline;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.XmlCharRef;
import org.sweble.wikitext.parser.nodes.XmlEntityRef;
import org.sweble.wikitext.parser.utils.TextUtils;

import de.fau.cs.osr.utils.StringUtils;

/**
 * FIXME: We are not defragmenting the AST after insert/append/delete
 * operations.
 * 
 * FIXME: We don't handle IllegalCodePoint nodes yet
 */
public class TextAdapter
		extends
			WomBackbone
		implements
			WomText
{
	private static final long serialVersionUID = 1L;
	
	private static final boolean DEBUG = true;
	
	private TextPart parts = null;
	
	private String text = "";
	
	// =========================================================================
	
	public TextAdapter(String text)
	{
		super(null);
		
		if (text.isEmpty())
		{
			setAstNode(new WtText(""));
		}
		else
		{
			if (StringUtils.hasParagraphSeparators(text))
				throw new UnsupportedOperationException(
						"Insertion would split text into multiple paragraphs");
			
			TextPart first = stringToAst(text);
			
			// The operation cannot fail any more
			this.text = text;
			
			setAstNode(first.astNode);
			this.parts = first.next;
			
			if (DEBUG)
				checkIntegrity();
		}
	}
	
	public TextAdapter(WtText astNode)
	{
		super(astNode);
		text = astNode.getContent();
	}
	
	public TextAdapter(Newline newline)
	{
		super(newline);
		text = "\n";
	}
	
	public TextAdapter(XmlCharRef ref)
	{
		super(ref);
		text = Toolbox.toText(ref);
	}
	
	public TextAdapter(XmlEntityRef ref)
	{
		super(ref);
		text = Toolbox.toText(ref);
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
	
	@Override
	public void appendText(String text) throws UnsupportedOperationException, IllegalArgumentException
	{
		if (text.isEmpty())
			return;
		
		TextPart first = stringToAst(text);
		
		int length = this.text.length();
		
		String newText = this.text + text;
		
		if (StringUtils.hasParagraphSeparators(newText))
			throw new UnsupportedOperationException(
					"Insertion would split text into multiple paragraphs");
		
		// The operation cannot fail any more
		this.text = newText;
		
		TextPart last = getLastPart();
		
		WtNode appendTo =
				(last != null) ? last.astNode : getAstNode();
		
		if (appendTo.getNodeType() == WtNode.NT_TEXT
				&& first.astNode.getNodeType() == WtNode.NT_TEXT)
		{
			// collapse adjacent text nodes
			
			WtText n = (WtText) appendTo;
			if (last != null)
			{
				last.text += first.text;
				n.setContent(last.text);
			}
			else
				n.setContent(n.getContent() + first.text);
			
			first = first.next;
		}
		
		if (last == null)
			this.parts = first;
		else
			last.next = first;
		
		ListIterator<WtNode> i =
				Toolbox.advanceAfter(getTextContainer(), appendTo);
		
		TextPart p = first;
		while (p != null)
		{
			i.add(p.astNode);
			p.offset += length;
			p = p.next;
		}
		
		if (DEBUG)
			checkIntegrity();
	}
	
	// =========================================================================
	
	@Override
	public void deleteText(int from, int length)
			throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		if (from < 0 || length < 0 || length + from > this.text.length())
			throw new IndexOutOfBoundsException();
		
		if (length == 0)
			return;
		
		if (this.text.length() == length)
		{
			clear();
		}
		else
		{
			String firstText = (this.parts != null) ?
					this.text.substring(0, this.parts.offset) :
					this.text;
			
			String a = this.text.substring(0, from);
			String b = this.text.substring(from + length);
			String newText = a + b;
			if (StringUtils.hasIsolatedSurrogates(a) || StringUtils.hasIsolatedSurrogates(b))
				throw new UnsupportedOperationException(
						"Deletion splits code point into surrogates");
			
			if (StringUtils.hasParagraphSeparators(newText))
				throw new UnsupportedOperationException(
						"Deletion would split text into multiple paragraphs");
			
			this.text = newText;
			
			TextPart first = new TextPart(getAstNode(), 0, firstText);
			first.next = this.parts;
			
			deleteText(from, length, first);
		}
		
		if (DEBUG)
			checkIntegrity();
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
		
		ListIterator<WtNode> i = getTextContainer().listIterator();
		
		int correct = 0;
		while (true)
		{
			TextPart next = p.next;
			
			int partLen = p.text.length();
			int partFrom = from - p.offset;
			int delPartLen = length;
			if (partFrom + delPartLen > partLen)
				delPartLen = partLen - partFrom;
			
			if (partFrom == 0 && delPartLen == partLen)
			{
				Toolbox.removeAstNode(i, p.astNode);
				if (prev == null)
					first = next;
				else
					prev.next = next;
			}
			else
			{
				deleteFromPart(p, partFrom, delPartLen);
				prev = p;
			}
			
			length -= delPartLen;
			correct += delPartLen;
			if (length == 0)
			{
				p = next;
				break;
			}
			
			next.offset -= correct;
			from = next.offset;
			p = next;
		}
		
		while (p != null)
		{
			p.offset -= correct;
			p = p.next;
		}
		
		this.parts = first.next;
		setAstNode(first.astNode);
	}
	
	private void deleteFromPart(TextPart p, int partFrom, int delPartLen)
	{
		WtNode astNode = p.astNode;
		switch (astNode.getNodeType())
		{
			case WtNode.NT_TEXT:
			{
				WtText n = (WtText) astNode;
				String text = n.getContent();
				
				text = text.substring(0, partFrom) +
						text.substring(partFrom + delPartLen);
				
				n.setContent(text);
				if (p != null)
					p.text = text;
				
				break;
			}
			case AstNodeTypes.NT_NEWLINE:
			{
				// A newline is always only one character ... we cannot split that
				throw new AssertionError();
			}
			case AstNodeTypes.NT_XML_CHAR_REF:
			{
				// A entity reference can only represent one UTF-16 character or
				// two surrogates, never more.
				if ((partFrom < 0 || partFrom > 1) && delPartLen != 1)
					throw new AssertionError();
				
				// Deleting only one of the surrogates creates a corrupted  
				// unicode string. But that case should have been caught 
				// already!
				throw new AssertionError();
			}
			case AstNodeTypes.NT_XML_ENTITY_REF:
			{
				// HTML entities are all one code point, BMP Unicode characters.
				// So there's nothing to split here.
				throw new AssertionError();
				
				// FIXME: But users of Sweble can always define additional entities 
				// that can resolve to arbitrary strings ... and these strings might
				// need escaping again.
			}
			default:
				throw new AssertionError();
		}
	}
	
	// =========================================================================
	
	@Override
	public void insertText(int at, String text)
			throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		if (at < 0 || at > this.text.length())
			throw new IndexOutOfBoundsException();
		
		if (text.isEmpty())
			return;
		
		String a = this.text.substring(0, at);
		String b = this.text.substring(at);
		String newText = a + text + b;
		
		if (StringUtils.hasIsolatedSurrogates(a)
				|| StringUtils.hasIsolatedSurrogates(b))
			throw new UnsupportedOperationException(
					"Insertion splits code points into surrogates.");
		
		if (StringUtils.hasParagraphSeparators(newText))
			throw new UnsupportedOperationException(
					"Insertion would split text into multiple paragraphs");
		
		TextPart firstToInsert = stringToAst(text);
		
		String firstText = (this.parts != null) ?
				firstText = this.text.substring(0, this.parts.offset) :
				this.text;
		
		this.text = newText;
		
		TextPart first = new TextPart(getAstNode(), 0, firstText);
		first.next = this.parts;
		
		TextPart p = firstToInsert;
		while (true)
		{
			p.offset += at;
			if (p.next == null)
				break;
			p = p.next;
		}
		
		// Insert the new parts
		insertText(first, at, firstToInsert, p, text.length());
		
		if (DEBUG)
			checkIntegrity();
	}
	
	private void insertText(
			TextPart first,
			int at,
			TextPart firstNewPart,
			TextPart lastNewPart,
			int length)
	{
		TextPart prev = null;
		TextPart p = first;
		
		while (p.next != null && at >= p.next.offset)
		{
			prev = p;
			p = p.next;
		}
		
		if (at == p.offset)
		{
			// Insert exactly between two existing parts or before the first part.
			
			if (prev == null)
				first = firstNewPart;
			else
				prev.next = firstNewPart;
			lastNewPart.next = p;
			
			insertAstNodes(
					Toolbox.advanceBefore(getTextContainer(), p.astNode),
					firstNewPart,
					lastNewPart);
		}
		else if (at == p.offset + p.text.length())
		{
			// Insert after the last part.
			insertAstNodes(
					Toolbox.advanceAfter(getTextContainer(), p.astNode),
					firstNewPart,
					lastNewPart);
			
			p.next = firstNewPart;
			lastNewPart.next = null;
			
			p = null;
		}
		else
		{
			// Split a part and insert into the new gap.
			
			p = splitPart(p, at, firstNewPart, lastNewPart);
			if (prev == null)
				first = p;
			else
				prev.next = p;
			
			p = lastNewPart.next;
		}
		
		// Fix offsets of parts following the inserted text
		while (p != null)
		{
			p.offset += length;
			p = p.next;
		}
		
		this.parts = first.next;
		setAstNode(first.astNode);
	}
	
	private TextPart splitPart(
			TextPart p,
			int at,
			TextPart firstNewPart,
			TextPart lastNewPart)
	{
		WtNode n = p.astNode;
		switch (n.getNodeType())
		{
			case WtNode.NT_TEXT:
			{
				return splitPart(p, (WtText) n, at, firstNewPart, lastNewPart);
			}
			case AstNodeTypes.NT_NEWLINE:
			{
				// A newline is always only one character ... we cannot split that
				throw new AssertionError();
			}
			case AstNodeTypes.NT_XML_CHAR_REF:
			{
				// Splitting a code point consiting of two code units creates a 
				// corrupted unicode string. But that case should have been 
				// caught already!
				throw new AssertionError();
			}
			case AstNodeTypes.NT_XML_ENTITY_REF:
			{
				// HTML entities are all one code point, BMP Unicode characters.
				// So there's nothing to split here.
				throw new AssertionError();
				
				// FIXME: But users of Sweble can always define additional entities 
				// that can resolve to arbitrary strings ... and these strings might
				// need escaping again.
			}
			default:
				throw new AssertionError();
		}
	}
	
	private TextPart splitPart(
			TextPart p,
			WtText na,
			int at,
			TextPart firstNewPart,
			TextPart lastNewPart)
	{
		// Before: prev p........ next
		// After:  prev as     bs next
		//              ^   ^
		//              |   |
		//              ofs at
		
		int ofs = p.offset;
		int ins = at - ofs;
		String text = p.text;
		if (ins <= 0 || ins >= text.length())
			throw new AssertionError();
		
		TextPart as = null;
		TextPart bs = null;
		TextPart next = p.next;
		
		// Find new text for left and right side of split
		String aText = text.substring(0, ins);
		String bText = text.substring(ins);
		
		// Fix/Create parts and ast nodes for left and right side of split
		as = p;
		as.text = aText;
		((WtText) as.astNode).setContent(aText);
		
		WtText astNode = new WtText(bText);
		bs = new TextPart(astNode, ofs + ins, bText);
		
		// Insert and remove ast nodes
		ListIterator<WtNode> i =
				Toolbox.advanceAfter(getTextContainer(), p.astNode);
		
		insertAstNodes(i, firstNewPart, lastNewPart);
		i.add(bs.astNode);
		
		// Link parts
		as.next = firstNewPart;
		lastNewPart.next = bs;
		bs.next = next;
		
		return as;
	}
	
	private void insertAstNodes(
			ListIterator<WtNode> i,
			TextPart firstNewPart,
			TextPart lastNewPart)
	{
		TextPart q = firstNewPart;
		while (true)
		{
			i.add(q.astNode);
			if (q == lastNewPart)
				break;
			q = q.next;
		}
	}
	
	// =========================================================================
	
	@Override
	public boolean replaceText(String search, String replacement)
			throws UnsupportedOperationException
	{
		// FIXME: not exception safe!
		
		int i = text.indexOf(search, 0);
		if (i == -1)
			return false;
		
		final int sLen = search.length();
		final int rLen = replacement.length();
		
		while (true)
		{
			replaceTextIntern(i, sLen, replacement);
			
			i = text.indexOf(search, i + rLen);
			if (i == -1)
				break;
		}
		
		return true;
	}
	
	@Override
	public String replaceText(int from, int length, String text)
			throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		// FIXME: not exception safe!
		
		String replacee = this.text.substring(from, from + length);
		replaceTextIntern(from, length, text);
		return replacee;
	}
	
	public void replaceTextIntern(int from, int length, String text)
			throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		deleteText(from, length);
		insertText(from, text);
	}
	
	// =========================================================================
	
	private static final class TextPart
	{
		public WtNode astNode;
		
		public int offset;
		
		public String text;
		
		public TextPart next = null;
		
		public TextPart(WtNode astNode, int offset, String text)
		{
			this.astNode = astNode;
			this.offset = offset;
			this.text = text;
		}
	}
	
	private TextPart getLastPart()
	{
		TextPart p = this.parts;
		while (p != null)
		{
			if (p.next == null)
				return p;
			p = p.next;
		}
		return null;
	}
	
	// =========================================================================
	
	private void checkIntegrity()
	{
		StringBuilder sb = new StringBuilder();
		
		String t = Toolbox.toText(getAstNode());
		sb.append(t);
		
		int ofs = t.length();
		
		ListIterator<WtNode> i = null;
		if (getParent() != null)
		{
			i = Toolbox.advanceAfter(getTextContainer(), getAstNode());
			if (i == null)
				throw new AssertionError();
		}
		
		TextPart p = this.parts;
		while (p != null)
		{
			while (i != null)
			{
				if (!i.hasNext())
					throw new AssertionError();
				
				WtNode n = i.next();
				if (n == p.astNode)
					break;
				
				switch (n.getNodeType())
				{
					case WtNode.NT_TEXT:
					case AstNodeTypes.NT_NEWLINE:
					case AstNodeTypes.NT_XML_CHAR_REF:
					case AstNodeTypes.NT_XML_ENTITY_REF:
						throw new AssertionError();
						
					default:
						continue;
				}
			}
			
			if (ofs != p.offset)
				throw new AssertionError();
			
			t = Toolbox.toText(p.astNode);
			ofs += t.length();
			
			if (!t.equals(p.text))
				throw new AssertionError();
			
			sb.append(t);
			
			p = p.next;
		}
		
		if (!sb.toString().equals(this.text))
			throw new AssertionError();
	}
	
	// =========================================================================
	
	private WtNodeList getTextContainer()
	{
		return ((FullElement) getParent()).getAstChildContainer();
	}
	
	// =========================================================================
	
	private void clear() throws AssertionError, AssertionError
	{
		WtNodeList container = getTextContainer();
		
		WtNode n = getAstNode();
		
		WtText empty = new WtText("");
		Toolbox.insertAstNode(container, empty, n);
		
		ListIterator<WtNode> i = container.listIterator();
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
			throw new AssertionError();
		
		setAstNode(empty);
		
		this.text = "";
		this.parts = null;
	}
	
	// =========================================================================
	
	private static TextPart stringToAst(String text)
	{
		if (text == null || text.isEmpty())
			throw new AssertionError();
		
		TextPart first = null;
		TextPart cur = null;
		
		int n = text.length();
		int i = 0;
		int j = 0;
		for (; j < n; ++j)
		{
			WtNode nonTextNode = null;
			
			// The index of the first non-text character
			int k = j;
			
			char ch = text.charAt(j);
			switch (ch)
			{
				case ' ':
				case '\n':
				case '\t':
					continue;
				case '<':
					nonTextNode = TextUtils.xmlEntity("lt", "<");
					break;
				case '>':
					nonTextNode = TextUtils.xmlEntity("gt", ">");
					break;
				case '&':
					nonTextNode = TextUtils.xmlEntity("amp", "&");
					break;
				default:
					if ((ch >= 0 && ch < 0x20) || (ch == 0xFE))
					{
						nonTextNode = TextUtils.xmlCharRef(ch);
						break;
					}
					else if (Character.isHighSurrogate(ch))
					{
						++j;
						if (j < n)
						{
							char ch2 = text.charAt(j);
							if (Character.isLowSurrogate(ch2))
							{
								int codePoint = Character.toCodePoint(ch, ch2);
								switch (Character.getType(codePoint))
								{
									case Character.CONTROL:
									case Character.PRIVATE_USE:
									case Character.UNASSIGNED:
										nonTextNode = TextUtils.xmlCharRef(codePoint);
										break;
									
									default:
										continue;
								}
							}
						}
					}
					else if (!Character.isLowSurrogate(ch))
					{
						continue;
					}
					
					// No low surrogate followed or only low surrogate
					if (nonTextNode == null)
						throw new IllegalArgumentException("String contains isolated surrogates!");
					
					break;
			}
			
			// we only get here if a non-text element needs to be added
			if (k > i)
			{
				String part = text.substring(i, k);
				WtText textNode = new WtText(part);
				TextPart p = new TextPart(textNode, i, part);
				if (cur == null)
					first = p;
				else
					cur.next = p;
				cur = p;
			}
			
			i = j + 1;
			
			// add the non-text element
			{
				String part = text.substring(k, i);
				TextPart p = new TextPart(nonTextNode, k, part);
				if (cur == null)
					first = p;
				else
					cur.next = p;
				cur = p;
			}
		}
		
		if (j > i)
		{
			String part = text.substring(i, j);
			WtText textNode = new WtText(part);
			TextPart p = new TextPart(textNode, i, part);
			if (cur == null)
				first = p;
			else
				cur.next = p;
		}
		
		return first;
	}
	
	// =========================================================================
	
	public void append(WtNode n)
	{
		switch (n.getNodeType())
		{
			case AstNodeTypes.NT_NEWLINE:
				append("\n", n);
				break;
			default:
				append(Toolbox.toText(n), n);
				break;
		}
	}
	
	private void append(String text, WtNode n)
	{
		TextPart part = new TextPart(n, this.text.length(), text);
		
		this.text += text;
		
		TextPart last = getLastPart();
		if (last != null)
			last.next = part;
		else
			this.parts = part;
	}
}
