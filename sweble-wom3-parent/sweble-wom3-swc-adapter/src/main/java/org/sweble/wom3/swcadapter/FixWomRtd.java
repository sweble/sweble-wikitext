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

import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wom3.Wom3Article;
import org.sweble.wom3.Wom3Articles;
import org.sweble.wom3.Wom3Big;
import org.sweble.wom3.Wom3Blockquote;
import org.sweble.wom3.Wom3Body;
import org.sweble.wom3.Wom3Bold;
import org.sweble.wom3.Wom3Break;
import org.sweble.wom3.Wom3Center;
import org.sweble.wom3.Wom3Cite;
import org.sweble.wom3.Wom3Code;
import org.sweble.wom3.Wom3Comment;
import org.sweble.wom3.Wom3DefinitionList;
import org.sweble.wom3.Wom3DefinitionListDef;
import org.sweble.wom3.Wom3DefinitionListTerm;
import org.sweble.wom3.Wom3Div;
import org.sweble.wom3.Wom3Document;
import org.sweble.wom3.Wom3Element;
import org.sweble.wom3.Wom3ElementNode;
import org.sweble.wom3.Wom3Emphasize;
import org.sweble.wom3.Wom3ExtLink;
import org.sweble.wom3.Wom3Font;
import org.sweble.wom3.Wom3For;
import org.sweble.wom3.Wom3Heading;
import org.sweble.wom3.Wom3HorizontalRule;
import org.sweble.wom3.Wom3Image;
import org.sweble.wom3.Wom3ImageCaption;
import org.sweble.wom3.Wom3IntLink;
import org.sweble.wom3.Wom3Italics;
import org.sweble.wom3.Wom3List;
import org.sweble.wom3.Wom3ListItem;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3Nowiki;
import org.sweble.wom3.Wom3OrderedList;
import org.sweble.wom3.Wom3Paragraph;
import org.sweble.wom3.Wom3Pre;
import org.sweble.wom3.Wom3Redirect;
import org.sweble.wom3.Wom3Ref;
import org.sweble.wom3.Wom3Repl;
import org.sweble.wom3.Wom3Rtd;
import org.sweble.wom3.Wom3Section;
import org.sweble.wom3.Wom3Small;
import org.sweble.wom3.Wom3Span;
import org.sweble.wom3.Wom3Strike;
import org.sweble.wom3.Wom3Strong;
import org.sweble.wom3.Wom3Sub;
import org.sweble.wom3.Wom3Subst;
import org.sweble.wom3.Wom3Sup;
import org.sweble.wom3.Wom3Table;
import org.sweble.wom3.Wom3TableBody;
import org.sweble.wom3.Wom3TableCaption;
import org.sweble.wom3.Wom3TableCell;
import org.sweble.wom3.Wom3TableHeaderCell;
import org.sweble.wom3.Wom3TableRow;
import org.sweble.wom3.Wom3Teletype;
import org.sweble.wom3.Wom3Text;
import org.sweble.wom3.Wom3Title;
import org.sweble.wom3.Wom3Underline;
import org.sweble.wom3.Wom3UnorderedList;

import de.fau.cs.osr.utils.WrappedException;

public class FixWomRtd
		extends
			FixWomRtdBase
{
	//	private static final String LIST_PREFIXES = "*#:;";

	public enum ListTypeEnum
	{
		// Render as HTML list items
		HTML_LIST,
		// Try to render as native if possible
		PRERENDER,
	}

	private final WikiConfig wikiConfig;

	private int inInlineBlock = 0;

	//	private String curListPrefix = "";

	private boolean inSemiPre;

	//	private ListTypeEnum inListType;

	// =========================================================================

	public FixWomRtd(WikiConfig wikiConfig)
	{
		this.wikiConfig = wikiConfig;
	}

	// =========================================================================

	public static Wom3Node process(WikiConfig wikiConfig, Wom3Node wom)
	{
		new FixWomRtd(wikiConfig).go(wom);
		return wom;
	}

	// =========================================================================
	// Containers and other nodes without RTD information

	public void visit(Wom3Document document)
	{
		dispatch((Wom3Node) document.getDocumentElement());
	}

	public void visit(Wom3Articles articles)
	{
		iterate(articles);
	}

	public void visit(Wom3Article article)
	{
		iterate(article);
	}

	public void visit(Wom3Body body)
	{
		iterate(body);
	}

	public void visit(Wom3Title title)
	{
		iterate(title);
	}

	// =========================================================================
	// Other nodes without RTD information

	public void visit(Wom3Redirect redirect)
	{
		// Invisible, don't descend
	}

	// =========================================================================
	// Nodes which will be restored to HTML elements

	public void visit(Wom3Div div)
	{
		restoreHtmlRtd(div);
	}

	public void visit(Wom3Blockquote bq)
	{
		restoreHtmlRtd(bq);
	}

	public void visit(Wom3Center center)
	{
		restoreHtmlRtd(center);
	}

	public void visit(Wom3Span span)
	{
		restoreHtmlRtd(span);
	}

	public void visit(Wom3Break br)
	{
		restoreHtmlRtd(br);
	}

	public void visit(Wom3Sub sub)
	{
		restoreHtmlRtd(sub);
	}

	public void visit(Wom3Sup sup)
	{
		restoreHtmlRtd(sup);
	}

	public void visit(Wom3Cite cite)
	{
		restoreHtmlRtd(cite);
	}

	public void visit(Wom3Strong strong)
	{
		restoreHtmlRtd(strong);
	}

	public void visit(Wom3Emphasize em)
	{
		restoreHtmlRtd(em);
	}

	public void visit(Wom3Small small)
	{
		restoreHtmlRtd(small);
	}

	public void visit(Wom3Big big)
	{
		restoreHtmlRtd(big);
	}

	public void visit(Wom3Font font)
	{
		restoreHtmlRtd(font);
	}

	public void visit(Wom3Code code)
	{
		restoreHtmlRtd(code);
	}

	public void visit(Wom3Underline u)
	{
		restoreHtmlRtd(u);
	}

	public void visit(Wom3Strike strike)
	{
		restoreHtmlRtd(strike);
	}

	public void visit(Wom3Teletype tt)
	{
		restoreHtmlRtd(tt);
	}

	private void restoreHtmlRtd(Wom3ElementNode e)
	{
		// TODO: Implement!
		if (!startsWithRtd(e))
		{
			// This can happen if the HTML tag was generated by the tree fixer...
			// throw new UnsupportedOperationException();
		}

		fixNewlinesBeforeElement(e, false);
		iterate(e);
	}

	// =========================================================================
	// HTML/Native nodes

	public void visit(Wom3HorizontalRule hr)
	{
		// TODO: Implement!
		if (!startsWithRtd(hr))
			throw new UnsupportedOperationException();

		fixNewlinesBeforeElement(hr, true /*TODO: Compute correctly!*/);
		iterate(hr);
	}

	public void visit(Wom3Bold b)
	{
		// TODO: Implement!
		if (!startsWithRtd(b))
			throw new UnsupportedOperationException();

		fixNewlinesBeforeElement(b, false);
		iterate(b);
	}

	public void visit(Wom3Italics i)
	{
		// TODO: Implement!
		if (!startsWithRtd(i))
			throw new UnsupportedOperationException();

		fixNewlinesBeforeElement(i, false);
		iterate(i);
	}

	// =========================================================================
	// Normalize text nodes

	public void visit(Wom3Text text)
	{
		String content = text.getTextContent();
		// stripDangerousWhitespace() also updates the wiki markup
		if (!(text.getParentNode() instanceof Wom3Nowiki)
				&& !(text.getParentNode() instanceof Wom3Pre))
		{
			String stripped = stripDangerousWhitespace(text, content);
			if (stripped != content)
			{
				if (stripped.isEmpty())
				{
					Wom3Node next = text.getNextSibling();
					text.getParentNode().removeChild(text);
					continueAfterDelete(next);
				}
				else
					text.setTextContent(stripped);
			}
		}
	}

	public void visit(Wom3Rtd rtd)
	{
		String content = rtd.getTextContent();
		/*
		String stripped = stripDangerousWhitespace(rtd, content);
		if (stripped != content)
			rtd.setTextContent(stripped);
		appendWm(stripped);
		*/
		appendWm(content);
	}

	/**
	 * Reduces more than two newlines to one newline if inside a paragraph to
	 * prevent the paragraph from being split into two paragraphs.
	 * 
	 * Makes sure that there are no spaces at the beginning of a line if not
	 * inside a "semi" pre environment.
	 */
	private String stripDangerousWhitespace(Wom3Node node, String text)
	{
		int newlines = getNewlineCount();

		boolean atStartOfP = false;
		if (inInlineBlock > 0)
		{
			atStartOfP = isAtStartOfParagraph(node);
			if (newlines > 1 && !atStartOfP)
			{
				removePrecedingNewlines(node, newlines - 1);
				newlines = 1;
			}
			if (newlines > 0 && hadSpaceAfterLastNewline())
				removePrecedingSpace(node);
		}

		Wom3Node last = node;
		String stripped = null;

		int l = text.length();
		int firstNewline = -1;
		boolean hadSpaceSinceNl = false;

		for (int i = 0; i < l; ++i)
		{
			char ch = text.charAt(i);
			switch (ch)
			{
				case '\n':
					++newlines;
					hadSpaceSinceNl = false;
					if (firstNewline == -1)
						firstNewline = i;
					break;

				case ' ':
				case '\t':
					hadSpaceSinceNl = true;
					break;

				default:
				{
					if ((inInlineBlock > 0 && newlines > 1 && !atStartOfP) ||
							(!inSemiPre && newlines > 0 && hadSpaceSinceNl))
					{
						// TODO: Why are "too many newlines" and "has space after newline and before text" 
						// treated the same? the fixes should be different, right? Remove too many newlines, but only remove the space, not the newlines as well...
						int from = firstNewline + 1;
						int count = i - from;
						text = text.substring(0, from) +
								text.substring(i);
						i -= count;
						l = text.length();

						switch (ch)
						{
							case '*':
							case '#':
							case ':':
							case ';':
							{
								// Wrap these in <nowiki> tags when encountered at line start
								if (!hadSpaceSinceNl)
								{
									// Everything in front of the dangerous character has 
									// has been processed and can be committed (to the 
									// original node or to a new node)
									String checked = text.substring(0, i);
									if (stripped == null)
										stripped = checked;
									else if (!checked.isEmpty())
										last = insertTextAfterNoMerge(last, checked);
									appendWm(checked);

									// Protected and insert the dangerous character
									last = insertNowikiAfter(last, String.valueOf(ch));

									// Process the rest
									text = text.substring(i + 1);
									l = text.length();
									i = 0;
								}
							}
						}
					}

					newlines = 0;
					firstNewline = -1;
					hadSpaceSinceNl = false;
					break;
				}
			}
		}

		// If we only removed stuff from "text" we return "text". If we had to
		// emit additional nodes, "stripped" will be != null and contain the
		// text that goes in the original node
		if (stripped == null)
		{
			appendWm(text);
			return text;
		}
		else
		{
			// wiki markup was already updated
			return stripped;
		}
	}

	// =========================================================================

	public void visit(Wom3Nowiki nowiki)
	{
		if (!startsWithRtd(nowiki))
		{
			prependRtd(nowiki, "<" + nowiki.getTagName() + ">");
			appendRtd(nowiki, "</" + nowiki.getTagName() + ">");
		}

		fixNewlinesBeforeElement(nowiki, false);

		for (Wom3Node child : nowiki)
		{
			if (child instanceof Wom3Text)
			{
				// The content of a nowiki node is not subject to further 
				// processing.
				appendWm(child.getTextContent());
			}
			else
			{
				dispatch(child);
			}
		}
	}

	public void visit(Wom3Comment comment)
	{
		// TODO: Implement!
		if (!startsWithRtd(comment))
			throw new UnsupportedOperationException();

		// Invisible to parser, don't descend!
		// The comment's prefix and suffix are also invisible to the parser!

		// TODO: If the comment is preceded and followed by a newline (which
		// are not already part of its prefix/suffix we have to add newlines
		// to the prefix and suffix! Otherwise the newlines in front and after
		// the comment will be parsed as prefix and suffix by the parser and
		// therefore disappear!
	}

	// =========================================================================
	// Table

	public void visit(Wom3Table table)
	{
		// TODO: Implement!
		/* Extremely tricky: As with lists we have to find out if this is a
		 * native or an HTML table. If we're dealing with an HTML table, 
		 * whitespace in between elements (tr, td, ...) will cause big 
		 * headaches. 
		 */
		if (!startsWithRtd(table))
		{
			// TODO: Happens ...
			//throw new UnsupportedOperationException();
		}

		fixNewlinesBeforeElement(table, true /*TODO: Compute correctly*/);
		iterate(table);
	}

	public void visit(Wom3TableCaption caption)
	{
		// TODO: Implement!
		if (!startsWithRtd(caption))
		{
			// TODO: Happens ...
			//throw new UnsupportedOperationException();
		}

		// TODO: We're not trimming any whitespace yet since we have no clue how
		// tables and whitespace behave
		iterate(caption);
	}

	public void visit(Wom3TableBody body)
	{
		// TODO: Implement!
		if (!startsWithRtd(body))
		{
			// TODO: Happens ...
			//throw new UnsupportedOperationException();
		}

		// TODO: We're not trimming any whitespace yet since we have no clue how
		// tables and whitespace behave
		iterate(body);
	}

	public void visit(Wom3TableRow row)
	{
		// TODO: Implement!
		if (!startsWithRtd(row))
		{
			// TODO: Happens ...
			//throw new UnsupportedOperationException();
		}

		// TODO: We're not trimming any whitespace yet since we have no clue how
		// tables and whitespace behave
		iterate(row);
	}

	public void visit(Wom3TableHeaderCell header)
	{
		// TODO: Implement!
		if (!startsWithRtd(header))
		{
			// TODO: Happens ...
			//throw new UnsupportedOperationException();
		}

		// TODO: We're not trimming any whitespace yet since we have no clue how
		// tables and whitespace behave
		iterate(header);
	}

	public void visit(Wom3TableCell cell)
	{
		// TODO: Implement!
		if (!startsWithRtd(cell))
		{
			// TODO: Happens ...
			//throw new UnsupportedOperationException();
		}

		// TODO: We're not trimming any whitespace yet since we have no clue how
		// tables and whitespace behave
		iterate(cell);
	}

	// =========================================================================
	// Section & Heading

	public void visit(Wom3Section section)
	{
		// Has no RTD information
		fixNewlinesBeforeElement(section, true);
		iterate(section);
	}

	public void visit(Wom3Heading heading)
	{
		// TODO: Implement!
		if (!startsWithRtd(heading))
			throw new UnsupportedOperationException();

		/* TODO: Tricky: Trimming might be necessary in case the heading is an 
		 * HTML element!
		 */
		iterate(heading);
	}

	// =========================================================================
	// Paragraph

	public void visit(Wom3Paragraph p)
	{
		++inInlineBlock;

		// Always check RTD around a paragraph. Changing other nodes can always
		// lead to slight changes in the number of surrounding newlines

		// A paragraph needs two newlines in front of it if it follows another 
		// element. It needs none and tolerates one newline in front of it at
		// the start of the page.

		// TODO: Consider paragraph with bottom gap in front!

		// If this paragraph is using HTML syntax and no top or bottom gap is 
		// set the rules for gaps between block elements in general apply.
		if (hasHtmlTagRtd(p))
		{
			if (p.getTopGap() == 0 && p.getBottomGap() == 0)
			{
				fixNewlinesBeforeElement(p, false);
				iterate(p);
				// TODO: Sure we don't have to fix the bottom gap?
				--inInlineBlock;
				return;
			}
			else
			{
				// Otherwise remove the HTML tag RTD
				p.removeChild(p.getFirstChild());
				p.removeChild(p.getLastChild());
			}
		}

		int haveNewlines = getNewlineCount();
		if (isAtPageStart())
		{
			if (haveNewlines > 1)
				removePrecedingNewlines(p, haveNewlines - 1);
		}
		else
		{
			if (haveNewlines > 2)
				removePrecedingNewlines(p, haveNewlines - 2);
			else if (haveNewlines < 2)
			{
				// Not true:
				// Well, we usually only need one newline between block elements
				// but if the preceding block element is a non-HTML paragraph we
				// need two newlines
				/*
				...
				*/

				// New truth:
				// We always need two newlines in front of a non-HTML paragraph.
				// Only if it's the first paragraph in some container (section, 
				// table cell, ...) we need only one.
				/*
				int needNewlines = 2;
				if (isFirstInContainer(p))
					needNewlines = 1;
				*/

				// Even better truth:
				int needNewlines = 2;
				Wom3Node pnws = getPrecedingNonWsNode(p);
				if (!(pnws instanceof Wom3Paragraph))
				{
					if ((pnws != null && isNonHtmlBlockElement(pnws)) || isFirstInContainer(p))
						needNewlines = 1;
				}

				if (haveNewlines < needNewlines)
					addPrecedingTextNewlines(p, needNewlines - haveNewlines);
			}
		}

		// We only fix the top gap. The bottom gap has to be fixed by elements
		// that come after the paragraph (see trimNewlinesBeforeElement())
		Wom3Node text0 = getFirstTextNode(p);
		if (text0 != null)
			assureStartWithEnoughNewlines(text0, p.getTopGap());
		else if (p.getTopGap() > 0)
			insertTextBefore(p.getFirstChild(), genNewlines(p.getTopGap()));

		iterate(p);

		// TODO: Sure we don't have to fix the bottom gap?

		--inInlineBlock;
	}

	// =========================================================================
	// Definition list

	public void visit(Wom3DefinitionList list)
	{
		// TODO: Implement
		/* Really tricky: if there's no RTD information we don't know if this is
		 * a HTML list or a native list. Maybe we should first render the list 
		 * items and find out if a) they have HTML or native RTD information or
		 * b) are not suitable for a native list. What's more we have to trim 
		 * whitespace in between list items if the list is done using HTML tags.
		 */
		//fixNewlinesBeforeElement(list, true /*TODO: Compute correctly*/);
		iterate(list);
	}

	public void visit(Wom3DefinitionListTerm term)
	{
		++inInlineBlock;
		// TODO: Implement
		// TODO: Tricky: Might require trimming when HTML element!
		iterate(term);
		--inInlineBlock;
	}

	public void visit(Wom3DefinitionListDef def)
	{
		++inInlineBlock;
		// TODO: Implement
		// TODO: Tricky: Might require trimming when HTML element!
		iterate(def);
		--inInlineBlock;
	}

	// =========================================================================
	// Ordered/Unordered list

	public void visit(Wom3OrderedList list)
	{
		processList(list, "#");
	}

	public void visit(Wom3UnorderedList list)
	{
		processList(list, "*");
	}

	private void processList(Wom3List list, String bulletType)
	{
		//fixNewlinesBeforeElement(list, true);
		iterate(list);

		//	ListTypeEnum oldInListType = inListType;
		//	String oldListLevel = curListPrefix;
		//	
		//	// If a list starts with HTML RTD we assume the RTD is properly 
		//	// formatted and we treat the list like any other block element.
		//	if (hasHtmlTagRtd(list))
		//	{
		//		inListType = ListTypeEnum.HTML_LIST;
		//		curListPrefix = "";
		//		fixNewlinesBeforeElement(list, false);
		//		iterate(list);
		//	}
		//	else
		//	{
		//		// If a list is not an HTML list it does not have RTD (the list! the 
		//		// children are a different story). We also won't added RTD unless
		//		// we are forced to render a HTML list and if we do render an HTML
		//		// list it won't have newlines. Therefore, trimNewlines... should
		//		// work here.
		//		
		//		fixNewlinesBeforeElement(list, true);
		//		
		//		// Remember markup position before the children were rendered.
		//		int wmPosBeforeChildren = getWmPos();
		//		boolean needHtmlList = false;
		//		
		//		// We fix the list items first and see if they can be formatted as 
		//		// native wiki markup list or if it has to be an HTML list.
		//		inListType = ListTypeEnum.PRERENDER;
		//		curListPrefix += bulletType;
		//		for (Wom3Node child : list)
		//		{
		//			if (child instanceof Wom3ListItem)
		//			{
		//				int wmPosBeforeListItem = getWmPos();
		//				isHtmlListItem = true;
		//				dispatch(child);
		//				if (hasHtmlTagRtd(child) || countNewlinesSince(wmPosBeforeListItem) > 1)
		//				{
		//					needHtmlList = true;
		//					break;
		//				}
		//			}
		//			else
		//			{
		//				dispatch(child);
		//			}
		//		}
		//		
		//		if (needHtmlList)
		//		{
		//			// Reformat whole list
		//			inListType = ListTypeEnum.HTML_LIST;
		//			curListPrefix = "";
		//			discardWm(wmPosBeforeChildren);
		//			
		//			prependText(list, "\n");
		//			prependRtd(list, "<" + list.getTagName() + ">");
		//			appendRtd(list, "</" + list.getTagName() + ">");
		//			
		//			iterate(list);
		//		}
		//		else
		//			// The list was completey rendered as native list in the PRERENDER
		//			// trial run. No need to do it again.
		//			;
		//	}
		//	inListType = oldInListType;
		//	curListPrefix = oldListLevel;
	}

	//	// TODO: Use me ...
	//	private boolean isHtmlListItem = false;

	public void visit(Wom3ListItem li)
	{
		++inInlineBlock;

		//fixNewlinesBeforeElement(li, true);
		iterate(li);

		//	if (hasHtmlTagRtd(li))
		//	{
		//		fixNewlinesBeforeElement(li, false);
		//		iterate(li);
		//
		//		// After iterating over our children let the parent list know that 
		//		// this is an HTML list item
		//		isHtmlListItem = true;
		//	}
		//	else if (inListType == ListTypeEnum.HTML_LIST)
		//	{
		//		// Remove any old RTD first
		//		Wom3Rtd rtd0 = getFirstRtdNode(li);
		//		if (rtd0 != null)
		//		{
		//			li.removeChild(rtd0);
		//			Wom3Rtd rtd1 = getLastRtdNode(li);
		//			if (rtd1 != null)
		//				// rtd1 may be null for the last list item in a list.
		//				li.removeChild(rtd1);
		//		}
		//
		//		// Add new HTML RTD
		//		prependText(li, "\n");
		//		prependRtd(li, "<" + li.getTagName() + ">");
		//		appendRtd(li, "</" + li.getTagName() + ">");
		//
		//		// Treat like any other element
		//		fixNewlinesBeforeElement(li, false);
		//
		//		iterate(li);
		//
		//		// After iterating over our children let the parent list know that 
		//		// this is an HTML list item (unnecessary, in HTML_LIST mode the
		//		// parent list knows anyway
		//		isHtmlListItem = true;
		//	}
		//	else
		//	{
		//		// If we're not forced to render as HTML we simply render as native
		//		// and don't check if native is an option. The parent list will do
		//		// that for us afterwards and re-render the list if HTML should 
		//		// be necessary.
		//
		//		// Each list item is only allowed to have one newline at the end.
		//		// There can only be a gap of newlines between native list items 
		//		// if would they would violate this rule. If they do violate that
		//		// rule the list will re-render as HTML anyway and HTML list items
		//		// make sure that there is no such gap.
		//
		//		// TODO: I think this is missing: fixNewlinesBeforeElement(li, true);
		//
		//		Wom3Node lastLi = null;
		//
		//		// Fix prefix
		//		Wom3Rtd rtd0 = getFirstRtdNode(li);
		//		if (rtd0 != null)
		//		{
		//			// We have a prefix (or at least RTD), update it if necessary
		//			String prefix = rtd0.getTextContent();
		//			int lastBullet = lastIndexOfOneOf(prefix, LIST_PREFIXES);
		//			if (lastBullet == -1)
		//				// that's ok
		//				;
		//			lastBullet += 1;
		//			String ws = prefix.substring(lastBullet, prefix.length());
		//			String newPrefix = curListPrefix + ws;
		//
		//			if (!newPrefix.equals(prefix))
		//			{
		//				rtd0.setTextContent(newPrefix);
		//
		//				// TODO: Why inside the (rtd0 != null) if?
		//				// TODO: Probably doesn't work for list nested more than once?
		//				// If a surrounding list was dissolved the last RTD of the
		//				// last list item might be unwanted
		//				//					lastLi = findLastChildOfType(li.getParentNode(), Wom3ListItem.class);
		//				//					if (lastLi == li)
		//				//					{
		//				//						Wom3Rtd last = getLastRtdNode(li);
		//				//						if (last != null)
		//				//							li.removeChild(last);
		//				//					}
		//			}
		//		}
		//		else
		//		{
		//			// We don't have RTD at all, generate it
		//			prependRtd(li, curListPrefix + " ");
		//		}
		//
		//		iterate(li);
		//
		//		if (getNewlineCount() < 1)
		//		{
		//			// The last list item does not need to add a newline
		//			if (lastLi == null)
		//				lastLi = findLastChildOfType(li.getParentNode(), Wom3ListItem.class);
		//			if (lastLi != li)
		//				appendRtdAfterProcessing(li, genNewlines(1));
		//		}
		//	}

		--inInlineBlock;
	}

	//	private String gatherAncestorListItemPrefix(Wom3Node li)
	//	{
	//		String prefix = "";
	//		Wom3Node list = li.getParentNode();
	//		Wom3Node listContainer = list.getParentNode();
	//		while (listContainer != null)
	//		{
	//			if (!((listContainer instanceof Wom3ListItem)
	//					|| (listContainer instanceof Wom3DefinitionListTerm)
	//					|| (listContainer instanceof Wom3DefinitionListDef)))
	//				break;
	//			// The list items list is again child of a list item (c)
	//
	//			Wom3Node prev = getPrecedingNonWsNode(list);
	//			if (prev != null)
	//				break;
	//			// Our list is the first item in the containing list item (c)
	//
	//			Wom3Rtd rtd0 = getFirstRtdNode(listContainer);
	//			String containerPrefix = rtd0.getTextContent();
	//			if (rtd0 == null || lastIndexOfOneOf(containerPrefix, LIST_PREFIXES) == -1)
	//				break;
	//			// The containing list item (c) has a native list prefix
	//
	//			prefix = containerPrefix.trim() + prefix;
	//			li = listContainer;
	//		}
	//		return prefix;
	//	}

	// =========================================================================
	// A pre element

	public void visit(Wom3Pre pre)
	{
		// TODO: Implement!
		/* Tricky: We have to check if this is a <pre> tag extension (which then 
		 * would contain a <nowiki> node as well) or a whitespace prefixed pre
		 * paragraph.
		 */
		if (!startsWithRtd(pre) && !startsWithText(pre))
			throw new UnsupportedOperationException();

		fixNewlinesBeforeElement(pre, false /*TODO: Compute correctly!*/);

		Wom3Nowiki nowiki = findFirstChildOfType(pre, Wom3Nowiki.class);
		if (nowiki == null)
		{
			iterate(pre);
		}
		else
		{
			// The <nowiki> contens are invisible. Also the <nowiki> is 
			// synthetic and should not get RTD attached
			for (Wom3Node c : pre)
			{
				if (c != nowiki)
					dispatch(c);
			}
		}
	}

	// =========================================================================
	// Internal and external links and images

	public void visit(Wom3IntLink link)
	{
		fixNewlinesBeforeElement(link, false);

		if (!startsWithRtd(link))
		{
			prependRtd(link, "[[" + link.getTarget());
			Wom3Title title = link.getLinkTitle();
			if (title != null && !startsWithRtd(title))
				prependRtd(title, "|");
			appendRtd(link, "]]");
		}

		iterate(link);
	}

	public void visit(Wom3ExtLink link)
	{
		fixNewlinesBeforeElement(link, false);

		// TODO: Implement!
		if (!startsWithRtd(link))
			throw new UnsupportedOperationException();

		iterate(link);
	}

	public void visit(Wom3Image image)
	{
		// TODO: Implement!
		if (!startsWithRtd(image))
			throw new UnsupportedOperationException();

		fixNewlinesBeforeElement(image, false);
		iterate(image);
	}

	public void visit(Wom3ImageCaption caption)
	{
		// TODO: Implement!
		if (!startsWithRtd(caption))
			throw new UnsupportedOperationException();

		iterate(caption);
	}

	// =========================================================================
	// WOM incompatible elements that have been substituted

	public void visit(Wom3Subst subst)
	{
		Wom3For for_ = subst.getFor();
		if (for_.getFirstChild() instanceof Wom3Element)
		{
			String name = ((Wom3Element) for_.getFirstChild()).getLocalName();
			if (name.equals("intlink"))
			{
				processSubstIntLink(subst);
			}
			else if (name.equals("xml-entity-ref"))
			{
				processXmlEntityRef(subst);
			}
			else if (name.equals("xml-char-ref"))
			{
				processXmlCharRef(subst);
			}
			else
			{
				throw new InternalError("NYI: " + name);
			}
		}
		else
		{
			throw new InternalError();
		}
	}

	private void processXmlEntityRef(Wom3Subst subst)
	{
		// TODO Auto-generated method stub
		iterate(subst.getFor());
	}

	private void processXmlCharRef(Wom3Subst subst)
	{
		// TODO Auto-generated method stub
		iterate(subst.getFor());
	}

	private void processSubstIntLink(Wom3Subst subst)
	{
		fixNewlinesBeforeElement(subst, false);

		Wom3Repl repl = subst.getRepl();
		if (!repl.hasChildNodes()
				|| !(findFirstNonWhitespaceNode(repl) instanceof Wom3IntLink))
		{
			unwrapIntlink(subst, repl);
		}
		else
		{
			Wom3IntLink replLink = findFirstChildOfType(repl, Wom3IntLink.class);
			Wom3Title replTitle = replLink.getLinkTitle();
			String replTarget = replLink.getTarget();

			Wom3For for_ = subst.getFor();
			Wom3Element forLink = findFirstChildOfType(for_, Wom3Element.class);
			String forTarget = forLink.getAttribute("target");
			String forPrefix = forLink.getAttribute("prefix");
			String forSuffix = forLink.getAttribute("postfix");
			Wom3Title forTitle = findFirstChildOfType(forLink, Wom3Title.class);

			String newTitle = stringifyTitle(replTitle, replTarget);

			// Build the oldTitle using the new target as replacement in case 
			// there is no title node.
			String oldTitle = stringifyTitle(forPrefix, forTitle, forSuffix, forTarget);

			if (!newTitle.equals(oldTitle))
			{
				// The title has changed => unwrap "repl/intlink", remove 
				// "subst" and fix unwrapped intlink.

				unwrapIntlink(subst, repl);
			}
			else
			{
				boolean update = false;
				if (!replTarget.equals(forTarget))
				{
					// First normalize both targets!
					PageTitle replTargetNl;
					PageTitle forTargetNl;
					try
					{
						replTargetNl = PageTitle.make(wikiConfig, replTarget);
						forTargetNl = PageTitle.make(wikiConfig, forTarget);
					}
					catch (LinkTargetException e)
					{
						throw new WrappedException(e);
					}

					if (!replTargetNl.equals(forTargetNl))
					{
						// If the target changed and there was no title, we have 
						// to create a title node with the old target name. In 
						// this case we can drop the prefix/postfix and can then
						// drop the whole <subst> thing
						if (forTitle == null)
						{
							unwrapIntlink(subst, repl);
							return;
						}

						// The target has changed, fix target in  "for/e" and fix the
						// RTD inforamtion.
						forLink.setAttribute("target", replTarget);
						update = true;
					}
				}

				if (update || getFirstRtdNode(forLink) == null)
					fixIntLink(forLink, forTarget, replTarget);

				// Iterate children to update this.wt
				iterate(forLink);
			}
		}
	}

	private void unwrapIntlink(Wom3Subst subst, Wom3Repl repl)
	{
		// The link was converted or removed.
		// unwrap the "repl" and remove the whole "subst".

		Wom3Node next = subst.getNextSibling();
		moveChildrenInFrontOfXRemoveXAndAndProcess(repl, subst);
		continueAfterDelete(next);
	}

	protected String stringifyTitle(Wom3Title replTitle, String target)
	{
		return (replTitle == null) ? target : stringifyChildren(replTitle);
	}

	protected String stringifyTitle(
			String prefix,
			Wom3Title title,
			String postfix,
			String target)
	{
		String t = (title == null) ? target : stringifyChildren(title);
		if (prefix != null && !prefix.isEmpty())
			t = prefix + t;
		if (postfix != null && !postfix.isEmpty())
			t = t + postfix;
		return t;
	}

	private void fixIntLink(Wom3Element link, String oldTarget, String newTarget)
	{
		String prefix = link.getAttribute("prefix");
		String suffix = link.getAttribute("postfix");

		String rtd0Str = "[[" + newTarget;
		if (prefix != null && !prefix.isEmpty())
			rtd0Str = prefix + rtd0Str;

		String rtd1Str = "]]";
		if (suffix != null && !suffix.isEmpty())
			rtd1Str = rtd1Str + suffix;

		Wom3Rtd rtd0 = getFirstRtdNode(link);
		if (rtd0 != null)
		{
			// There may or may not be a title in between.
			Wom3Rtd rtd1 = getLastRtdNode(link);
			if (rtd0 == rtd1)
				rtd0Str += rtd1Str;
			else
				rtd1.setTextContent(rtd1Str);
			rtd0.setTextContent(rtd0Str);
		}
		else
		{
			prependRtd(link, rtd0Str);
			appendRtd(link, rtd1Str);
		}

		Wom3Title title = findFirstChildOfType(link, Wom3Title.class);
		if (title != null && !startsWithRtd(title))
			prependRtd(title, "|");
	}

	// =========================================================================

	public void visit(Wom3Element e)
	{
		String name = e.getLocalName();
		if (name.equals("tagext"))
		{
			processTagExt(e);
		}
		else if (name.equals("transclusion"))
		{
			processTransclusion(e);
		}
		else if (name.equals("xml-entity-ref"))
		{
			processXmlEntityRef(e);
		}
		else if (name.equals("xmlelement"))
		{
			processXmlElement(e);
		}
		else
		{
			// TODO: AAAHHH, Tu mal wat ...
			//throw new InternalError("NYI: " + name);
		}
	}

	private void processXmlElement(Wom3Element e)
	{
		iterate(e);
	}

	private void processXmlEntityRef(Wom3Element e)
	{
		iterate(e);
	}

	private void processTagExt(Wom3Element e)
	{
		// The stuff inside tag extensions is invisible to the parser
		appendWm("<" + e.getAttribute("name") + "/>");
	}

	private void processTransclusion(Wom3Element e)
	{
		// The stuff inside transclusions is invisible to the parser
		appendWm("{{N|...}}");
	}

	// =========================================================================

	public void visit(Wom3Ref ref)
	{
		iterate(ref);
	}

	// =========================================================================

	/**
	 * (a) There must not be an amount of whitespace between two block elements
	 * that would cause the parser to insert an empty paragraph. (b) There must
	 * at least be one newline between two block elements. (c) If the upper
	 * block element is a paragraph, the method must make sure that its bottom
	 * gap is respected.
	 */
	private void fixNewlinesBeforeElement(Wom3ElementNode e, boolean needNewline)
	{
		int newlines = getNewlineCount();
		int allowed = 2;
		int required = 0;

		// We need a newline (for example in front of a native wm list).
		// Page start is an implicit newline
		if (needNewline && !isAtPageStart())
			required += 1;

		if (isAtStartOfParagraph(e))
		{
			// At the start of a paragraph the paragraph made sure the top gap
			// has the right size. We don't have to remove any spaces ourselves.
			return;
		}
		else if (inInlineBlock > 0 && !needNewline)
		{
			// TODO: This is bullshit ... inInlineBlock is not a direct test.
			// There could be non-inline block in between (<dl><dd><dl><dd>...)

			// TODO: I added !needNewline, don't know if that makes so much more
			// sense but should at least work for the <dl><dd>... case.

			// Inside an inline block (paragraph, table cell, ...) only one 
			// newline is allowed, more would split text into paragraphs.
			allowed -= 1;
		}
		else
		{
			// If the previous node is a paragraph we have to respect its
			// bottom gap.
			Wom3Paragraph p = getPrecedingParagraph(e);
			if (p != null && p.getBottomGap() > 0)
			{
				allowed += p.getBottomGap();
				required = allowed;
			}
		}

		if (newlines > allowed)
		{
			removePrecedingNewlines(e, newlines - allowed);
		}
		else if (newlines < required)
		{
			insertTextBeforeAfterProcessing(e, genNewlines(required - newlines));
		}
	}
}
