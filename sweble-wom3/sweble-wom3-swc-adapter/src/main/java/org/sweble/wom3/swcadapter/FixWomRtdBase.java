/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3.swcadapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.sweble.wom3.Wom3Attribute;
import org.sweble.wom3.Wom3Comment;
import org.sweble.wom3.Wom3Document;
import org.sweble.wom3.Wom3Element;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3Nowiki;
import org.sweble.wom3.Wom3Paragraph;
import org.sweble.wom3.Wom3Repl;
import org.sweble.wom3.Wom3Rtd;
import org.sweble.wom3.Wom3Text;
import org.sweble.wom3.Wom3XmlText;
import org.sweble.wom3.swcadapter.utils.AnalyzingStringBuffer;
import org.sweble.wom3.util.Wom3Visitor;
import org.w3c.dom.Node;

import de.fau.cs.osr.utils.StringTools;

public class FixWomRtdBase
		extends
			Wom3Visitor
{
	private final AnalyzingStringBuffer wm = new AnalyzingStringBuffer();

	private static Map<String, ElementProperties> elements;

	private static Set<String> blockElementSet;

	private Wom3Document doc;

	// =========================================================================

	public FixWomRtdBase()
	{
		synchronized (FixWomRtdBase.class)
		{
			if (elements == null)
			{
				elements = new HashMap<String, ElementProperties>();
				for (ElementProperties e : ElementProperties.values())
					elements.put(e.toString(), e);
			}

			if (blockElementSet == null)
			{
				blockElementSet = new HashSet<String>();

				blockElementSet.add("address");
				blockElementSet.add("blockquote");
				blockElementSet.add("center");
				//blockElementSet.add("del");
				blockElementSet.add("div");
				blockElementSet.add("dl");
				blockElementSet.add("section");
				blockElementSet.add("hr");
				//blockElementSet.add("ins");
				blockElementSet.add("ol");
				blockElementSet.add("p");
				blockElementSet.add("pre");
				blockElementSet.add("table");
				blockElementSet.add("ul");
			}
		}
	}

	// =========================================================================

	protected static ElementProperties getInfo(String tagName)
	{
		return elements.get(tagName);
	}

	// =========================================================================

	@Override
	protected Wom3Node before(Wom3Node node)
	{
		doc = (Wom3Document) node.getOwnerDocument();
		if (doc == null)
			doc = (Wom3Document) node;
		return super.before(node);
	}

	@Override
	protected Object after(Wom3Node node, Object result)
	{
		//Assert.assertEquals(womToWmFast(node), wm.toString());
		return super.after(node, result);
	}

	public String womToWmFast(Wom3Node wom)
	{
		StringBuilder sb = new StringBuilder();
		restoreWmFromWomFast(sb, wom);
		return sb.toString();
	}

	private void restoreWmFromWomFast(StringBuilder sb, Wom3Node wom)
	{
		if (wom instanceof Wom3Rtd || wom instanceof Wom3Text)
		{
			sb.append(wom.getTextContent());
			return;
		}

		if ((wom instanceof Wom3Repl)
				|| (wom instanceof Wom3Comment))
		{
			// Ignore stuff
			return;
		}

		if (wom instanceof Wom3Element)
		{
			Wom3Element element = (Wom3Element) wom;
			String name = element.getLocalName();
			if (name.equals("tagext"))
			{
				// The stuff inside tag extensions is invisible to the parser
				sb.append("<" + element.getAttribute("name") + "/>");
				return;
			}
			else if (name.equals("transclusion"))
			{
				// The stuff inside transclusions is invisible to the parser
				sb.append("{{N|...}}");
				return;
			}

			// Fall through
		}

		for (Wom3Node c : wom)
			restoreWmFromWomFast(sb, c);
	}

	// =========================================================================

	protected void appendWm(String text)
	{
		wm.append(text);
	}

	protected void discardWm(int wmPosBeforeChildren)
	{
		wm.discard(wmPosBeforeChildren);
	}

	private void rollbackWm(int count)
	{
		wm.rollback(count);
	}

	// =========================================================================

	/**
	 * Used to turn node content into a string (basically an XML document). This
	 * can become necessary to compare subtrees in a WOM document when text
	 * around that subtree has changed. An example is the comparison of link
	 * titles (see stringifyTitle).
	 */
	protected String stringifyChildren(Wom3Node node)
	{
		if (node == null)
			throw new NullPointerException();

		StringBuilder b = new StringBuilder();
		for (Wom3Node c = node.getFirstChild(); c != null; c = c.getNextSibling())
			stringify(b, c);
		return b.toString();
	}

	private void stringify(StringBuilder b, Wom3Node node)
	{
		if (node == null)
			throw new NullPointerException();

		if (node instanceof Wom3Rtd)
			return;

		boolean isText = node instanceof Wom3Text;
		if (!isText)
		{
			b.append('<');
			b.append(node.getNodeName());
			stringifyAttrs(b, node.getWomAttributes());
			b.append('>');

			for (Wom3Node c = node.getFirstChild(); c != null; c = c.getNextSibling())
				stringify(b, c);

			b.append("</");
			b.append(node.getNodeName());
			b.append('>');
		}
		else
		{
			String text = node.getTextContent();
			if (text != null)
				b.append(text);
		}
	}

	private void stringifyAttrs(
			StringBuilder b,
			Collection<Wom3Attribute> attributes)
	{
		if (attributes.isEmpty())
			return;

		ArrayList<Wom3Attribute> attrs = new ArrayList<Wom3Attribute>(attributes);
		Collections.sort(attrs, new Comparator<Wom3Attribute>()
		{
			@Override
			public int compare(Wom3Attribute o1, Wom3Attribute o2)
			{
				return o1.getName().compareTo(o2.getName());
			}
		});

		for (Wom3Attribute attr : attrs)
		{
			b.append(' ');
			b.append(attr.getName());
			b.append("=\"");
			b.append(attr.getValue());
			b.append('"');
		}
	}

	// =========================================================================

	protected int lastIndexOfOneOf(String haystack, String needleSet)
	{
		int len = haystack.length();
		for (int i = len - 1; i >= 0; --i)
		{
			char ch = haystack.charAt(i);
			if (needleSet.indexOf(ch) != -1)
				return i;
		}
		return -1;
	}

	// =========================================================================

	protected Wom3Text text(String text)
	{
		Wom3Text node = (Wom3Text) doc.createElementNS(Wom3Node.WOM_NS_URI, "text");
		node.setTextContent(text);
		return node;
	}

	/**
	 * Inserts a &lt;text> node in front of node {@code n}. If there is already
	 * a &lt;text> node in front of {@code n}, the text is appended to that
	 * &lt;text> node.
	 */
	protected void insertTextBefore(Wom3Node n, String text)
	{
		Wom3Node prev = n.getPreviousSibling();
		if (prev instanceof Wom3Text)
		{
			prev.setTextContent(
					prev.getTextContent() + text);
		}
		else
		{
			n.getParentNode().insertBefore(text(text), n);
		}
	}

	/**
	 * Like {@code insertTextBefore()} but assumes that the RTD node was already
	 * processed and adds the newlines to the wiki markup manually.
	 */
	protected void insertTextBeforeAfterProcessing(Wom3Node n, String text)
	{
		insertTextBefore(n, text);
		appendWm(text);
	}

	/**
	 * Prepends an &lt;text> node to the children of node {@code n}. If the
	 * first child of node {@code n} is already an &lt;text> node, the text is
	 * prepended to that &lt;text> node.
	 */
	protected void prependText(Wom3Node n, String text)
	{
		Wom3Node first = n.getFirstChild();
		if (first instanceof Wom3Text)
		{
			first.setTextContent(
					text + first.getTextContent());
		}
		else
		{
			prependNode(n, text(text));
		}
	}

	/**
	 * Appends a &lt;text> node to the children of node {@code n}. If the last
	 * child of node {@code n} is already a &lt;text> node, the text is appended
	 * to that &lt;text> node.
	 */
	protected void appendText(Wom3Node n, String text)
	{
		Wom3Node last = n.getLastChild();
		if (last instanceof Wom3Text)
		{
			last.setTextContent(
					last.getTextContent() + text);
		}
		else
		{
			n.appendChild(text(text));
		}
	}

	/**
	 * Inserts a &lt;text> node after node {@code n}.
	 */
	protected Wom3Node insertTextAfterNoMerge(Wom3Node n, String text)
	{
		Wom3Node next = n.getNextSibling();
		Wom3Text newText = text(text);
		if (next != null)
			n.getParentNode().insertBefore(newText, next);
		else
			n.getParentNode().appendChild(newText);
		return newText;
	}

	protected Wom3Nowiki nowiki(String text)
	{
		Wom3Nowiki node = (Wom3Nowiki) doc.createElementNS(Wom3Node.WOM_NS_URI, "nowiki");
		node.appendChild(text(text));
		return node;
	}

	/**
	 * Inserts a &lt;nowiki> node after node {@code n}. If {@code n} is a nowiki
	 * node the given text will be appended to the xml text within {@code n}.
	 */
	protected Wom3Node insertNowikiAfter(Wom3Node n, String text)
	{
		Wom3Nowiki newNowiki;
		if (n instanceof Wom3Nowiki)
		{
			newNowiki = (Wom3Nowiki) n;
			n.setTextContent(
					n.getTextContent() + text);
		}
		else
		{
			Wom3Node next = n.getNextSibling();
			newNowiki = nowiki(text);
			if (next != null)
				n.getParentNode().insertBefore(newNowiki, next);
			else
				n.getParentNode().appendChild(newNowiki);
			return newNowiki;
		}
		return newNowiki;
	}

	protected Wom3Rtd rtd(String text)
	{
		Wom3Rtd node = (Wom3Rtd) doc.createElementNS(Wom3Node.WOM_NS_URI, "rtd");
		node.setTextContent(text);
		return node;
	}

	/**
	 * Inserts an &lt;rtd> node in front of node {@code n}. If there is already
	 * a &lt;rtd> node in front of {@code n}, the text is appended to that
	 * &lt;rtd> node.
	 */
	protected void insertRtdBefore(Wom3Node n, String rtd)
	{
		Wom3Node prev = n.getPreviousSibling();
		if (prev instanceof Wom3Rtd)
		{
			prev.setTextContent(
					prev.getTextContent() + rtd);
		}
		else
		{
			n.getParentNode().insertBefore(rtd(rtd), n);
		}
	}

	/**
	 * Prepends an &lt;rtd> node to the children of node {@code n}. If the first
	 * child of node {@code n} is already an &lt;rtd> node, the text is
	 * prepended to that &lt;rtd> node.
	 * 
	 * @return
	 */
	protected Wom3Rtd prependRtd(Wom3Node n, String rtd)
	{
		Wom3Node first = n.getFirstChild();
		if (first instanceof Wom3Rtd)
		{
			first.setTextContent(
					rtd + first.getTextContent());
			return (Wom3Rtd) first;
		}
		else
		{
			Wom3Rtd rtdNode = rtd(rtd);
			prependNode(n, rtdNode);
			return rtdNode;
		}
	}

	/**
	 * Appends an &lt;rtd> node to the children of node {@code n}. If the last
	 * child of node {@code n} is already an &lt;rtd> node, the text is appended
	 * to that &lt;rtd> node.
	 */
	protected void appendRtd(Wom3Node n, String rtd)
	{
		Wom3Node last = n.getLastChild();
		if (last instanceof Wom3Rtd)
		{
			last.setTextContent(
					last.getTextContent() + rtd);
		}
		else
		{
			n.appendChild(rtd(rtd));
		}
	}

	/**
	 * Like {@code appendRtd()} but assumes that the RTD node was already
	 * processed and adds the newlines to the wiki markup manually.
	 */
	protected void appendRtdAfterProcessing(Wom3Node node, String newlines)
	{
		appendRtd(node, newlines);
		appendWm(newlines);
	}

	/**
	 * Inserts the given {@code child} node as first child of node
	 * {@code parent}.
	 */
	protected void prependNode(Wom3Node parent, Wom3Node child)
	{
		Wom3Node first = parent.getFirstChild();
		if (first != null)
		{
			parent.insertBefore(child, first);
		}
		else
		{
			parent.appendChild(child);
		}
	}

	/**
	 * This method assures that there is a certain number of newlines in an RTD
	 * element. This method assumes that the contents of the RTD element have
	 * not yet been committed to the wiki markup buffer.
	 */
	protected void assureStartWithEnoughNewlines(Wom3Node n, int topGap)
	{
		String text = n.getTextContent();
		int len = text.length();

		int count = 0;
		outer: for (int i = 0; i < len; ++i)
		{
			switch (text.charAt(i))
			{
				case '\n':
					count += 1;
					if (count > topGap)
					{
						int to1 = i;
						inner: for (++i; i < len; ++i)
						{
							switch (text.charAt(i))
							{
								case '\n':
								case ' ':
								case '\t':
									break;
								default:
									break inner;
							}
						}
						int from2 = i;
						n.setTextContent(text.substring(0, to1) +
								text.substring(from2));
						return;
					}
					break;
				case ' ':
				case '\t':
					break;
				default:
					break outer;
			}
		}

		if (count < topGap)
			n.setTextContent(genNewlines(topGap - count) + text);
	}

	/**
	 * Adds newlines text in front of node {@code current} and, assuming that
	 * the everything in front of {@code current} has been processed already,
	 * also prints newlines to wiki markup.
	 */
	protected void addPrecedingTextNewlines(Wom3Node current, int count)
	{
		String missing = genNewlines(count);
		insertTextBefore(current, missing);
		appendWm(missing);
	}

	/**
	 * Remove newlines from RTD and text nodes preceding node {@code current} in
	 * document order (not restricted to siblings!).
	 */
	protected void removePrecedingNewlines(Wom3Node current, int count)
	{
		if (count <= 0)
			throw new IllegalArgumentException();

		int rollback = 0;

		Wom3Node n = current;
		while (true)
		{
			// Go to previous node in document order.
			n = toPreviousXmlTextInDocumentOrder(n);

			Wom3Node p = n.getParentNode();
			if ((p instanceof Wom3Text)
					|| (p instanceof Wom3Rtd))
			{
				String text = n.getTextContent();
				int l = text.length();
				for (int i = l - 1; i >= 0; --i, ++rollback)
				{
					char ch = text.charAt(i);
					switch (ch)
					{
						case '\n':
							count -= 1;
							if (count <= 0)
							{
								if (i == 0)
									p.getParentNode().removeChild(p);
								else
									((Wom3XmlText) n).deleteData(i, l - i);

								rollbackWm(rollback + 1);
								return;
							}
							break;
						case ' ':
						case '\t':
							break;
						default:
							throw new RuntimeException("Should not happen");
					}
				}

				// The whole text/RTD node has to go. Since these nodes have 
				// already been processed we can remove them without confusing
				// the visitation process
				p.getParentNode().removeChild(p);
			}
		}
	}

	/**
	 * @param container
	 *            has to be a descendant of {@code x}.
	 */
	protected void moveChildrenInFrontOfXRemoveXAndAndProcess(
			Wom3Node container,
			Wom3Node x)
	{
		Wom3Node parent = x.getParentNode();
		Wom3Node first = container.getFirstChild();
		Wom3Node last = container.getLastChild();
		for (Wom3Node c = first; c != null;)
		{
			Wom3Node n = c.getNextSibling();
			container.removeChild(c);
			parent.insertBefore(c, x);
			c = n;
		}

		parent.removeChild(x);

		if (first != null)
		{
			for (Wom3Node c = first;; c = c.getNextSibling())
			{
				dispatch(c);
				if (c == last)
					break;
			}
		}
	}

	protected void clearChildren(Wom3Element n)
	{
		for (Wom3Node c = n.getFirstChild(); c != null;)
		{
			Wom3Node remove = c;
			c = c.getNextSibling();
			n.removeChild(remove);
		}
	}

	/**
	 * Remove newlines from RTD and text nodes preceding node {@code current} in
	 * document order (not restricted to siblings!).
	 */
	protected void removePrecedingSpace(Wom3Node node)
	{
		// TODO: Implement
		throw new UnsupportedOperationException();
	}

	protected String genNewlines(int n)
	{
		return StringTools.strrep('\n', n);
	}

	// =========================================================================

	protected int getNewlineCount()
	{
		return wm.newlineCount();
	}

	protected boolean isAtPageStart()
	{
		return wm.noContentYet();
	}

	protected boolean hadSpaceAfterLastNewline()
	{
		return wm.hadSpaceAfterLastNewline();
	}

	protected int countNewlinesSince(int wmPosBeforeListItem)
	{
		return wm.countNewlinesSince(wmPosBeforeListItem);
	}

	protected int getWmPos()
	{
		return wm.length();
	}

	// =========================================================================

	private Wom3XmlText toPreviousXmlTextInDocumentOrder(Wom3Node n)
	{
		while (true)
		{
			// Go up until we find a node with a previous sibling
			Wom3Node p;
			while (true)
			{
				if (n == null)
					System.err.println();
				p = n.getPreviousSibling();
				if (p != null)
					break;
				n = n.getParentNode();
			}

			// Found the predecessor (not necessary a sibling), now go down to the 
			// last descendant of the predecessor
			while (p.hasChildNodes())
				p = p.getLastChild();

			if (p.getNodeType() == Node.TEXT_NODE)
				return (Wom3XmlText) p;

			// Not an xml text node, search continues ...
			n = p;
		}
	}

	/**
	 * Searches among the children, not the descendants!
	 */
	protected <T extends Wom3Node> T findFirstChildOfType(
			Wom3Node container,
			Class<T> type)
	{
		for (Wom3Node c = container.getFirstChild(); c != null; c = c.getNextSibling())
		{
			if (type.isInstance(c))
			{
				@SuppressWarnings("unchecked")
				T tmp = (T) c;
				return tmp;
			}
		}
		return null;
	}

	/**
	 * Searches among the children, not the descendants!
	 */
	protected <T extends Wom3Node> T findLastChildOfType(
			Wom3Node container,
			Class<T> type)
	{
		for (Wom3Node c = container.getLastChild(); c != null; c = c.getPreviousSibling())
		{
			if (type.isInstance(c))
			{
				@SuppressWarnings("unchecked")
				T tmp = (T) c;
				return tmp;
			}
		}
		return null;
	}

	protected boolean hasHtmlTagRtd(Wom3Node n)
	{
		Wom3Node rtd = getFirstRtdNode(n);
		if (rtd == null)
			return false;
		String text = rtd.getTextContent();
		if (text.length() < 3)
			return false;
		if (text.charAt(0) != '<')
			return false;
		if (text.lastIndexOf('>') == -1)
			return false;
		return true;
	}

	protected Wom3Rtd getFirstRtdNode(Wom3Node parent)
	{
		for (Wom3Node n = parent.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if (n instanceof Wom3Rtd)
				return (Wom3Rtd) n;
			else if (!((n instanceof Wom3Comment) || isElementContentWhitespace(n)))
				return null;
		}
		return null;
	}

	protected Wom3Text getFirstTextNode(Wom3Node parent)
	{
		for (Wom3Node n = parent.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if (n instanceof Wom3Text)
				return (Wom3Text) n;
			else if (!((n instanceof Wom3Comment) || isElementContentWhitespace(n)))
				return null;
		}
		return null;
	}

	protected Wom3Rtd getLastRtdNode(Wom3Node parent)
	{
		for (Wom3Node n = parent.getLastChild(); n != null; n = n.getPreviousSibling())
		{
			if (n instanceof Wom3Rtd)
				return (Wom3Rtd) n;
			else if (!((n instanceof Wom3Comment) || isElementContentWhitespace(n)))
				return null;
		}
		return null;
	}

	protected Wom3Node findFirstNonWhitespaceNode(Wom3Node parent)
	{
		for (Wom3Node n = parent.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if (!isElementContentWhitespace(n))
				return n;
		}
		return null;
	}

	protected boolean isElementContentWhitespace(Wom3Node n)
	{
		/* TODO: Revisit once Wom3XmlText.isElementContentWhitespace() is 
		 * implemented.
		 */

		return (n instanceof Wom3XmlText)
				&& StringTools.isWhitespace(((Wom3XmlText) n).getTextContent());
	}

	protected boolean isAtStartOfParagraph(Wom3Node node)
	{
		for (Wom3Node prev;; node = prev)
		{
			prev = node.getPreviousSibling();
			if (prev == null)
				break;
			else if ((prev instanceof Wom3Rtd)
					|| (prev instanceof Wom3Comment)
					|| isTextWhitespace(prev))
				continue;
			else
				return false;
		}
		Wom3Node p = node.getParentNode();
		return (p instanceof Wom3Paragraph)/*
											|| (p instanceof Wom3TableCaption)
											|| (p instanceof Wom3TableCellBase)*/;
	}

	/**
	 * Returns the paragraph node that is a preceding sibling of node {@code n}.
	 * If another non RTD/WS/comment node comes first the search is aborted.
	 */
	protected Wom3Paragraph getPrecedingParagraph(Wom3Node n)
	{
		for (Wom3Node prev = n.getPreviousSibling(); prev != null; prev = prev.getPreviousSibling())
		{
			if (prev instanceof Wom3Paragraph)
				return (Wom3Paragraph) prev;
			else if (!((prev instanceof Wom3Rtd)
					|| (prev instanceof Wom3Comment)
					|| isTextWhitespace(prev)))
				break;
		}
		return null;
	}

	/**
	 * Returns true if node is the first non RTD/WS/comment node in a body or
	 * similar container.
	 */
	protected boolean isFirstInContainer(Wom3Node n)
	{
		for (Wom3Node prev = n.getPreviousSibling(); prev != null; prev = prev.getPreviousSibling())
		{
			if (!((prev instanceof Wom3Rtd)
					|| (prev instanceof Wom3Comment)
					|| isTextWhitespace(prev)))
				return false;
		}
		/*
		Wom3Node parent = n.getParentNode();
		return ((parent instanceof Wom3Body));
		*/
		return true;
	}

	protected Wom3Node getPrecedingNonWsNode(Wom3Node n)
	{
		for (Wom3Node prev = n.getPreviousSibling(); prev != null; prev = prev.getPreviousSibling())
		{
			if (!((prev instanceof Wom3Rtd)
					|| (prev instanceof Wom3Comment)
					|| isTextWhitespace(prev)))
				return prev;
		}
		return null;
	}

	protected boolean isNonHtmlBlockElement(Wom3Node pnws)
	{
		return (blockElementSet.contains(pnws.getNodeName()) && !hasHtmlTagRtd(pnws));
	}

	protected boolean isTextWhitespace(Wom3Node n)
	{
		return (n instanceof Wom3Text)
				&& StringTools.isWhitespace(n.getTextContent());
	}

	/**
	 * Checks if a nodes first starts with a &lt;rtd> node, irgnoring &lt;text>
	 * nodes in the search.
	 */
	protected boolean startsWithRtd(Wom3Node n)
	{
		for (Wom3Node c : n)
		{
			if (c instanceof Wom3Rtd)
				return true;
			if (!((c instanceof Wom3Text)
					|| c instanceof Wom3Comment
					|| isElementContentWhitespace(c)))
				return false;
		}
		return false;
	}

	/**
	 * Checks if a nodes first starts with a &lt;text> node, irgnoring &lt;text>
	 * nodes in the search.
	 */
	protected boolean startsWithText(Wom3Node n)
	{
		for (Wom3Node c : n)
		{
			if (c instanceof Wom3Text)
				return true;
			if (!(c instanceof Wom3Comment
			|| isElementContentWhitespace(c)))
				return false;
		}
		return false;
	}
}
