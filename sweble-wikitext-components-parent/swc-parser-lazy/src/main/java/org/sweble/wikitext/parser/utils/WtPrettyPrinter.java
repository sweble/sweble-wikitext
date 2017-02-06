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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.sweble.wikitext.parser.nodes.WtBody;
import org.sweble.wikitext.parser.nodes.WtBold;
import org.sweble.wikitext.parser.nodes.WtDefinitionList;
import org.sweble.wikitext.parser.nodes.WtDefinitionListDef;
import org.sweble.wikitext.parser.nodes.WtDefinitionListTerm;
import org.sweble.wikitext.parser.nodes.WtExternalLink;
import org.sweble.wikitext.parser.nodes.WtHeading;
import org.sweble.wikitext.parser.nodes.WtHorizontalRule;
import org.sweble.wikitext.parser.nodes.WtIgnored;
import org.sweble.wikitext.parser.nodes.WtIllegalCodePoint;
import org.sweble.wikitext.parser.nodes.WtImEndTag;
import org.sweble.wikitext.parser.nodes.WtImStartTag;
import org.sweble.wikitext.parser.nodes.WtImageLink;
import org.sweble.wikitext.parser.nodes.WtInternalLink;
import org.sweble.wikitext.parser.nodes.WtItalics;
import org.sweble.wikitext.parser.nodes.WtLctFlags;
import org.sweble.wikitext.parser.nodes.WtLctRule;
import org.sweble.wikitext.parser.nodes.WtLctRuleConv;
import org.sweble.wikitext.parser.nodes.WtLctRuleGarbage;
import org.sweble.wikitext.parser.nodes.WtLctRules;
import org.sweble.wikitext.parser.nodes.WtLctVarConv;
import org.sweble.wikitext.parser.nodes.WtLinkOptionAltText;
import org.sweble.wikitext.parser.nodes.WtLinkOptionGarbage;
import org.sweble.wikitext.parser.nodes.WtLinkOptionKeyword;
import org.sweble.wikitext.parser.nodes.WtLinkOptionLinkTarget;
import org.sweble.wikitext.parser.nodes.WtLinkOptionResize;
import org.sweble.wikitext.parser.nodes.WtLinkOptions;
import org.sweble.wikitext.parser.nodes.WtLinkTarget;
import org.sweble.wikitext.parser.nodes.WtLinkTitle;
import org.sweble.wikitext.parser.nodes.WtListItem;
import org.sweble.wikitext.parser.nodes.WtName;
import org.sweble.wikitext.parser.nodes.WtNewline;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtOnlyInclude;
import org.sweble.wikitext.parser.nodes.WtOrderedList;
import org.sweble.wikitext.parser.nodes.WtPageName;
import org.sweble.wikitext.parser.nodes.WtPageSwitch;
import org.sweble.wikitext.parser.nodes.WtParagraph;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.nodes.WtPreproWikitextPage;
import org.sweble.wikitext.parser.nodes.WtRedirect;
import org.sweble.wikitext.parser.nodes.WtSection;
import org.sweble.wikitext.parser.nodes.WtSemiPre;
import org.sweble.wikitext.parser.nodes.WtSemiPreLine;
import org.sweble.wikitext.parser.nodes.WtSignature;
import org.sweble.wikitext.parser.nodes.WtTable;
import org.sweble.wikitext.parser.nodes.WtTableCaption;
import org.sweble.wikitext.parser.nodes.WtTableCell;
import org.sweble.wikitext.parser.nodes.WtTableHeader;
import org.sweble.wikitext.parser.nodes.WtTableImplicitTableBody;
import org.sweble.wikitext.parser.nodes.WtTableRow;
import org.sweble.wikitext.parser.nodes.WtTagExtension;
import org.sweble.wikitext.parser.nodes.WtTagExtensionBody;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtTemplateArgument;
import org.sweble.wikitext.parser.nodes.WtTemplateArguments;
import org.sweble.wikitext.parser.nodes.WtTemplateParameter;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtTicks;
import org.sweble.wikitext.parser.nodes.WtUnorderedList;
import org.sweble.wikitext.parser.nodes.WtUrl;
import org.sweble.wikitext.parser.nodes.WtValue;
import org.sweble.wikitext.parser.nodes.WtWhitespace;
import org.sweble.wikitext.parser.nodes.WtXmlAttribute;
import org.sweble.wikitext.parser.nodes.WtXmlAttributeGarbage;
import org.sweble.wikitext.parser.nodes.WtXmlAttributes;
import org.sweble.wikitext.parser.nodes.WtXmlCharRef;
import org.sweble.wikitext.parser.nodes.WtXmlComment;
import org.sweble.wikitext.parser.nodes.WtXmlElement;
import org.sweble.wikitext.parser.nodes.WtXmlEmptyTag;
import org.sweble.wikitext.parser.nodes.WtXmlEndTag;
import org.sweble.wikitext.parser.nodes.WtXmlEntityRef;
import org.sweble.wikitext.parser.nodes.WtXmlStartTag;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.utils.PrinterBase;
import de.fau.cs.osr.utils.StringTools;

public class WtPrettyPrinter
		extends
			AstVisitor<WtNode>
{
	// --[ WtInnerNode1 ]-------------------------------------------------------

	public void visit(WtLinkOptionLinkTarget n)
	{
		p.print("|link=");
		switch (n.getTargetType())
		{
			case PAGE:
			case URL:
				dispatch(n.getTarget());
				break;
			case DEFAULT:
				// This should NOT happen ...
				throw new AssertionError();
			case NO_LINK:
				break;
		}
	}

	public void visit(WtRedirect n)
	{
		p.print("#REDIRECT[[");
		dispatch(n.getTarget());
		p.print("]]");
	}

	public void visit(WtTableImplicitTableBody n)
	{
		dispatch(n.getBody());
	}

	public void visit(WtXmlAttribute n)
	{
		p.print(' ');
		dispatch(n.getName());
		if (n.hasValue())
		{
			boolean needQuotes = true;
			if (n.getValue().size() == 1)
			{
				switch (n.getValue().get(0).getNodeType())
				{
					case WtNode.NT_TEMPLATE:
					case WtNode.NT_TEMPLATE_PARAMETER:
						needQuotes = false;
				}
			}

			p.print(needQuotes ? "=\"" : "=");
			dispatch(n.getValue());
			if (needQuotes)
				p.print('"');
		}
	}

	public void visit(WtXmlEmptyTag n)
	{
		if (n.getName().startsWith("#"))
			return;
		p.print('<');
		p.print(n.getName());
		dispatch(n.getXmlAttributes());
		p.print(" />");
	}

	public void visit(WtXmlStartTag n)
	{
		if (n.getName().startsWith("#"))
			return;
		p.print('<');
		p.print(n.getName());
		dispatch(n.getXmlAttributes());
		p.print(">");
	}

	public void visit(WtImStartTag n)
	{
		// Should not appear in post-processed wikitext
	}

	public void visit(WtLctRule n)
	{
		if (!n.isDirectConvert())
		{
			p.print(n.getSearch());
			p.print("=>");
		}
		p.print(n.getVariant());
		p.print(":");
		dispatch(n.getReplace());
	}

	// --[ WtInnerNode2 ]-------------------------------------------------------

	public void visit(WtExternalLink n)
	{
		scope.push(n);
		p.print("[");
		dispatch(n.getTarget());
		if (n.hasTitle())
			dispatch(n.getTitle());
		p.print("]");
		scope.pop();
	}

	public void visit(WtInternalLink n)
	{
		scope.push(n);
		p.print(n.getPrefix());
		p.print("[[");
		dispatch(n.getTarget());
		if (n.hasTitle())
			dispatch(n.getTitle());
		p.print("]]");
		p.print(n.getPostfix());
		scope.pop();
	}

	public void visit(WtSection n)
	{
		scope.push(n);
		p.clearEatNewlinesAndIndents();
		p.needNewlines(2);
		iterate(n);
		p.needNewlines(2);
		scope.pop();
	}

	public void visit(WtTable n)
	{
		p.clearEatNewlinesAndIndents();
		p.needNewlines(2);
		p.print("{|");

		dispatch(n.getXmlAttributes());
		p.println();

		dispatch(n.getBody());

		p.clearEatNewlinesAndIndents();
		p.capNewlines(1, 1);
		p.println(" |}");
		p.needNewlines(2);
	}

	public void visit(WtTableCaption n)
	{
		p.clearEatNewlinesAndIndents();
		p.capNewlines(1, 1);
		p.print(" |+");

		if (!n.getXmlAttributes().isEmpty())
		{
			dispatch(n.getXmlAttributes());
			p.print(" |");
		}

		p.eatNewlinesAndIndents(2);
		dispatch(n.getBody());
		p.capNewlines(1, 1);
	}

	public void visit(WtTableCell n)
	{
		p.clearEatNewlinesAndIndents();
		p.capNewlines(1, 1);
		p.print(" |");

		if (!n.getXmlAttributes().isEmpty())
		{
			dispatch(n.getXmlAttributes());
			p.print(" |");
		}

		p.eatNewlinesAndIndents(2);
		dispatch(n.getBody());
		p.capNewlines(1, 1);
	}

	public void visit(WtTableHeader n)
	{
		p.clearEatNewlinesAndIndents();
		p.capNewlines(1, 1);
		p.print(" !");

		if (!n.getXmlAttributes().isEmpty())
		{
			dispatch(n.getXmlAttributes());
			p.print(" |");
		}

		p.eatNewlinesAndIndents(2);
		dispatch(n.getBody());
		p.capNewlines(1, 1);
	}

	public void visit(WtTableRow n)
	{
		if (!n.isImplicit())
		{
			p.clearEatNewlinesAndIndents();
			p.capNewlines(1, 1);
			p.print(" |-");

			dispatch(n.getXmlAttributes());
			p.println();
		}
		dispatch(n.getBody());
	}

	public void visit(WtTagExtension n)
	{
		p.print('<');
		p.print(n.getName());
		dispatch(n.getXmlAttributes());
		p.print(n.hasBody() ? ">" : "/>");
		if (n.hasBody())
		{
			dispatch(n.getBody());
			p.print("</");
			p.print(n.getName());
			p.print('>');
		}
	}

	public void visit(WtTemplate n)
	{
		if (n.isPrecededByNewline())
		{
			p.clearEatNewlinesAndIndents();
			p.println();
		}
		p.print("{{");
		iterate(n);
		p.print("}}");
	}

	public void visit(WtTemplateArgument n)
	{
		p.print('|');
		if (n.hasName())
		{
			dispatch(n.getName());
			p.print('=');
		}
		dispatch(n.getValue());
	}

	public void visit(WtXmlElement n)
	{
		p.print('<');
		p.print(n.getName());
		dispatch(n.getXmlAttributes());
		p.print(n.hasBody() ? ">" : " />");
		if (n.hasBody())
		{
			dispatch(n.getBody());
			p.print("</");
			p.print(n.getName());
			p.print('>');
		}
	}

	public void visit(WtLctVarConv n)
	{
		p.print("-{");
		if (n.hasFlags())
		{
			dispatch(n.getFlags());
			p.print('|');
		}
		dispatch(n.getText());
		p.print("}-");
	}

	public void visit(WtLctRuleConv n)
	{
		p.print("-{");
		if (n.hasFlags())
		{
			dispatch(n.getFlags());
			p.print('|');
		}
		dispatch(n.getRules());
		p.print("}-");
	}

	// --[ WtInnerNode3 ]-------------------------------------------------------

	public void visit(WtImageLink n)
	{
		scope.push(n);
		p.print("[[");
		dispatch(n.getTarget());
		dispatch(n.getOptions());
		p.print("]]");
		scope.pop();
	}

	public void visit(WtTemplateParameter n)
	{
		p.print("{{{");
		dispatch(n.getName());
		if (n.hasDefault())
		{
			p.print('|');
			dispatch(n.getDefault());
		}
		p.print("}}}");
	}

	// --[ WtLeafNode ]---------------------------------------------------------

	public void visit(WtHorizontalRule n)
	{
		p.clearEatNewlinesAndIndents();
		p.needNewlines(2);
		p.println("----");
		p.needNewlines(2);
	}

	public void visit(WtIllegalCodePoint n)
	{
		p.print(n.getCodePoint());
	}

	public void visit(WtLinkOptionKeyword n)
	{
		p.print('|');
		p.print(n.getKeyword());
	}

	public void visit(WtLinkOptionResize n)
	{
		p.print('|');
		if (n.getHeight() >= 0)
		{
			if (n.getWidth() >= 0)
				p.print(String.valueOf(n.getWidth()));
			p.print('x');
			p.print(String.valueOf(n.getHeight()));
		}
		else
		{
			p.print(String.valueOf(n.getWidth()));
		}
		p.print("px");
	}

	public void visit(WtPageSwitch n)
	{
		p.print("__");
		p.print(n.getName());
		p.print("__");
	}

	public void visit(WtSignature n)
	{
		p.print(StringTools.strrep('~', n.getTildeCount()));
	}

	public void visit(WtTicks n)
	{
		p.print(StringTools.strrep('\'', n.getTickCount()));
	}

	public void visit(WtUrl n)
	{
		if (!n.getProtocol().isEmpty())
		{
			p.print(n.getProtocol());
			p.print(':');
		}
		p.print(n.getPath());
	}

	public void visit(WtXmlCharRef n)
	{
		p.print("&#");
		p.print(String.valueOf(n.getCodePoint()));
		p.print(';');
	}

	public void visit(WtXmlEndTag n)
	{
		if (n.getName().startsWith("#"))
			return;
		p.print("</");
		p.print(n.getName());
		p.print('>');
	}

	public void visit(WtImEndTag n)
	{
		// Should not appear in post-processed wikitext
	}

	public void visit(WtXmlEntityRef n)
	{
		p.print('&');
		p.print(n.getName());
		p.print(';');
	}

	public void visit(WtLctFlags n)
	{
		List<String> flags = new ArrayList<String>();
		flags.addAll(n.getFlags());
		flags.addAll(n.getVariants());
		for (int i = 0; i < flags.size(); ++i)
		{
			if (i > 0)
				p.print(';');
			p.print(flags.get(i));
		}
	}

	// --[ WtNodeList ]---------------------------------------------------------

	public void visit(WtNodeList n)
	{
		iterate(n);
	}

	// --[ WtContentNode ]------------------------------------------------------

	public void visit(WtBody n)
	{
		iterate(n);
	}

	public void visit(WtBold n)
	{
		p.print("'''");
		iterate(n);
		p.print("'''");
	}

	public void visit(WtDefinitionList n)
	{
		++insideList;
		scope.push(n);
		if (insideList <= 1)
			p.needNewlines(2);
		iterate(n);
		p.needNewlines(insideList > 1 ? 1 : 2);
		scope.pop();
		--insideList;
	}

	public void visit(WtDefinitionListDef n)
	{
		scope.push(n);
		printListPrefix(n);
		iterate(n);
		p.println();
		scope.pop();
	}

	public void visit(WtDefinitionListTerm n)
	{
		scope.push(n);
		printListPrefix(n);
		iterate(n);
		p.println();
		scope.pop();
	}

	public void visit(WtHeading n)
	{
		int level = ((WtSection) scope.peek()).getLevel();
		String equals = StringTools.strrep('=', level);

		p.print(equals);
		iterate(n);
		p.println(equals);
	}

	public void visit(WtItalics n)
	{
		p.print("''");
		iterate(n);
		p.print("''");
	}

	public void visit(WtLinkOptionAltText n)
	{
		p.print("|alt=");
		iterate(n);
	}

	public void visit(WtLinkOptions n)
	{
		for (WtNode a : n)
		{
			switch (a.getNodeType())
			{
				case WtNode.NT_LINK_OPTION_ALT_TEXT:
				case WtNode.NT_LINK_OPTION_GARBAGE:
				case WtNode.NT_LINK_OPTION_KEYWORD:
				case WtNode.NT_LINK_OPTION_LINK_TARGET:
				case WtNode.NT_LINK_OPTION_RESIZE:
					break;
				default:
					// Only link options and garbage know that it has to leave a 
					// pipe in front. We have to fix this for all other 
					// elements (e.g. templates).
					p.print('|');
					break;
			}
			dispatch(a);
		}
	}

	public void visit(WtLinkTitle n)
	{
		switch (scope.peek().getNodeType())
		{
			case WtNode.NT_INTERNAL_LINK:
				p.print('|');
				break;
			case WtNode.NT_EXTERNAL_LINK:
				p.print(' ');
				break;
			case WtNode.NT_IMAGE_LINK:
			default:
				break;
		}
		iterate(n);
	}

	public void visit(WtListItem n)
	{
		scope.push(n);
		printListPrefix(n);
		iterate(n);
		p.println();
		scope.pop();
	}

	private void printListPrefix(WtNode item)
	{
		String prefix = "";

		boolean multipleNewLevels = false;

		WtNode parent = scope.get(1);
		if (scope.size() > 2 && !parent.isEmpty() && parent.get(0) == item)
		{
			// We're the first item in a list
			WtNode parentContainer = scope.get(2);
			if (!parentContainer.isEmpty() && parentContainer.get(0) == parent)
			{
				// The list to which we belong opened a new level itself.
				multipleNewLevels = true;
			}
		}

		Iterator<WtNode> i = scope.iterator();
		outer: while (i.hasNext())
		{
			parent = i.next();
			switch (parent.getNodeType())
			{
				case WtNode.NT_LIST_ITEM:
					// One more iteration to add prefix for (un-)ordered list
					if (multipleNewLevels)
						continue;
				case WtNode.NT_DEFINITION_LIST:
					break;
				case WtNode.NT_ORDERED_LIST:
					prefix = "#" + prefix;
					break;
				case WtNode.NT_UNORDERED_LIST:
					prefix = "*" + prefix;
					break;
				case WtNode.NT_DEFINITION_LIST_DEF:
					prefix = ":" + prefix;
					break;
				case WtNode.NT_DEFINITION_LIST_TERM:
					prefix = ";" + prefix;
					break;
				default:
					break outer;
			}

			if (multipleNewLevels)
				break;
		}

		if (!multipleNewLevels)
		{
			p.clearEatNewlinesAndIndents();
			p.println();
		}
		p.print(prefix);
	}

	public void visit(WtName n)
	{
		iterate(n);
	}

	public void visit(WtOnlyInclude n)
	{
		p.print("<onlyinclude>");
		iterate(n);
		p.print("</onlyinclude>");
	}

	public void visit(WtOrderedList n)
	{
		++insideList;
		scope.push(n);
		if (insideList <= 1)
			p.needNewlines(2);
		iterate(n);
		p.needNewlines(insideList > 1 ? 1 : 2);
		scope.pop();
		--insideList;
	}

	public void visit(WtParsedWikitextPage n)
	{
		iterate(n);
	}

	public void visit(WtPreproWikitextPage n)
	{
		iterate(n);
	}

	public void visit(WtParagraph n)
	{
		p.needNewlines(2);
		iterate(n);
		p.needNewlines(2);
	}

	public void visit(WtSemiPre n)
	{
		p.needNewlines(2);
		iterate(n);
		p.needNewlines(2);
	}

	public void visit(WtSemiPreLine n)
	{
		p.print(' ');
		iterate(n);
		p.println();
	}

	public void visit(WtTemplateArguments n)
	{
		iterate(n);
	}

	public void visit(WtUnorderedList n)
	{
		++insideList;
		scope.push(n);
		if (insideList <= 1)
			p.needNewlines(2);
		iterate(n);
		p.needNewlines(insideList > 1 ? 1 : 2);
		scope.pop();
		--insideList;
	}

	public void visit(WtValue n)
	{
		iterate(n);
	}

	public void visit(WtWhitespace n)
	{
		iterate(n);
	}

	public void visit(WtXmlAttributes n)
	{
		for (WtNode a : n)
		{
			switch (a.getNodeType())
			{
				case WtNode.NT_XML_ATTRIBUTE:
				case WtNode.NT_XML_ATTRIBUTE_GARBAGE:
					break;
				default:
					// Only attributes and garbage know that it has to leave a 
					// space in front. We have to fix this for all other 
					// elements (e.g. templates).
					p.print(' ');
					break;
			}
			dispatch(a);
		}
	}

	public void visit(WtLctRules n)
	{
		int i = 0;
		for (WtNode rule : n)
		{
			if (rule instanceof WtLctRuleGarbage)
				// Don't print garbage!
				continue;
			if (i++ > 0)
				p.print(";");
			dispatch(rule);
		}
	}

	// --[ WtStringNode ]-------------------------------------------------------

	public void visit(WtIgnored n)
	{
		p.print(n.getContent());
	}

	public void visit(WtLinkOptionGarbage n)
	{
		// Don't print garbage!
	}

	public void visit(WtNewline n)
	{
		p.println();
	}

	public void visit(WtPageName n)
	{
		iterate(n);
	}

	public void visit(WtLinkTarget.WtNoLink n)
	{
	}

	public void visit(WtXmlAttributeGarbage n)
	{
		// Don't print garbage!
	}

	public void visit(WtXmlComment n)
	{
		p.print(n.getPrefix());
		p.print("<!--");
		p.print(n.getContent());
		p.print("-->");
		p.print(n.getSuffix());
	}

	public void visit(WtTagExtensionBody n)
	{
		p.verbatim(n.getContent());
	}

	public void visit(WtLctRuleGarbage n)
	{
		// Don't print garbage!
	}

	public void visit(WtText n)
	{
		p.print(n.getContent());
	}

	// =========================================================================

	public static <T extends WtNode> String print(T node)
	{
		return print(new StringWriter(), node).toString();
	}

	public static <T extends WtNode> Writer print(Writer writer, T node)
	{
		new WtPrettyPrinter(writer).go(node);
		return writer;
	}

	// =========================================================================

	protected final PrinterBase p;

	private final LinkedList<WtNode> scope = new LinkedList<WtNode>();

	private boolean newlineAtEof = false;

	private int insideList;

	// =========================================================================

	public WtPrettyPrinter(Writer writer)
	{
		this.p = new PrinterBase(writer);
		this.p.setMemoize(false);
	}

	// =========================================================================

	public void setNewlineAtEof(boolean newlineAtEof)
	{
		this.newlineAtEof = newlineAtEof;
	}

	public boolean isNewlineAtEof()
	{
		return newlineAtEof;
	}

	// =========================================================================

	@Override
	protected WtNode before(WtNode node)
	{
		p.eatNewlinesAndIndents(2);
		return super.before(node);
	}

	@Override
	protected Object after(WtNode node, Object result)
	{
		p.ignoreNewlines();
		if (newlineAtEof)
			p.println();
		p.flush();
		return result;
	}
}
