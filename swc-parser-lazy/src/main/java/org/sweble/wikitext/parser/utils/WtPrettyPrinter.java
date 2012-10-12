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
import java.util.Iterator;
import java.util.LinkedList;

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
import org.sweble.wikitext.parser.nodes.WtLinkOptionAltText;
import org.sweble.wikitext.parser.nodes.WtLinkOptionGarbage;
import org.sweble.wikitext.parser.nodes.WtLinkOptionKeyword;
import org.sweble.wikitext.parser.nodes.WtLinkOptionLinkTarget;
import org.sweble.wikitext.parser.nodes.WtLinkOptionResize;
import org.sweble.wikitext.parser.nodes.WtLinkOptions;
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
import de.fau.cs.osr.utils.StringUtils;

public class WtPrettyPrinter
		extends
			AstVisitor<WtNode>
{
	private final LinkedList<WtNode> scope = new LinkedList<WtNode>();
	
	private int insideList;
	
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
		}
	}
	
	public void visit(WtRedirect n)
	{
		p.print("#REDIRECT[[");
		dispatch(n.getTarget());
		p.print("]]");
	}
	
	public void visit(WtXmlAttribute n)
	{
		p.print(' ');
		p.print(n.getName());
		if (n.hasValue())
		{
			p.print("=\"");
			iterate(n.getValue());
			p.print('"');
		}
	}
	
	public void visit(WtXmlEmptyTag n)
	{
		p.print('<');
		p.print(n.getName());
		dispatch(n.getXmlAttributes());
		p.print(" />");
	}
	
	public void visit(WtXmlStartTag n)
	{
		p.print('<');
		p.print(n.getName());
		dispatch(n.getXmlAttributes());
		p.print(">");
	}
	
	public void visit(WtImStartTag n)
	{
		// Should not appear in post-processed wikitext
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
		p.clearEatNewlinesAndIndents();
		p.capNewlines(1, 1);
		p.print(" |-");
		
		dispatch(n.getXmlAttributes());
		p.println();
		
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
		if (n.getPrecededByNewline())
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
	
	// --[ WtInnerNode3 ]-------------------------------------------------------
	
	public void visit(WtImageLink n)
	{
		scope.push(n);
		p.print("[[");
		dispatch(n.getTarget());
		iterate(n.getOptions());
		if (n.hasTitle())
			dispatch(n.getTitle());
		p.print("]]");
		scope.pop();
	}
	
	public void visit(WtTemplateParameter n)
	{
		p.print("{{{");
		dispatch(n.getName());
		if (n.hasDefaultValue())
		{
			p.print('|');
			dispatch(n.getDefaultValue());
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
		p.print(StringUtils.strrep('~', n.getTildeCount()));
	}
	
	public void visit(WtTicks n)
	{
		p.print(StringUtils.strrep('\'', n.getTickCount()));
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
		String equals = StringUtils.strrep('=', level);
		
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
		iterate(n);
	}
	
	public void visit(WtLinkTitle n)
	{
		boolean isExt = (scope.peek().getNodeType() == WtNode.NT_EXTERNAL_LINK);
		p.print(isExt ? ' ' : '|');
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
		iterate(n);
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
		p.print(n.getContent());
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
	
	public WtPrettyPrinter(Writer writer)
	{
		this.p = new PrinterBase(writer);
		this.p.setMemoize(false);
	}
	
	@Override
	protected boolean before(WtNode node)
	{
		p.eatNewlinesAndIndents(2);
		return super.before(node);
	}
	
	@Override
	protected Object after(WtNode node, Object result)
	{
		p.ignoreNewlines();
		p.println();
		p.flush();
		return result;
	}
	
	// =========================================================================
	
	private boolean useRtd = false;
	
	public void setRespectRtData(boolean useRtd)
	{
		this.useRtd = useRtd;
	}
}
