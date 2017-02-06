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

import static org.sweble.wikitext.parser.nodes.WtNode.NT_IGNORED;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_ILLEGAL_CODE_POINT;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_PAGE_SWITCH;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_REDIRECT;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_SEMI_PRE;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_SIGNATURE;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_TABLE_CAPTION;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_TABLE_CELL;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_TABLE_HEADER;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_TABLE_ROW;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_CHAR_REF;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_COMMENT;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_ELEMENT;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_EMPTY_TAG;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_ENTITY_REF;
import static org.sweble.wikitext.parser.postprocessor.ElementType.ADDRESS;
import static org.sweble.wikitext.parser.postprocessor.ElementType.CAPTION;
import static org.sweble.wikitext.parser.postprocessor.ElementType.DD;
import static org.sweble.wikitext.parser.postprocessor.ElementType.DIV;
import static org.sweble.wikitext.parser.postprocessor.ElementType.DT;
import static org.sweble.wikitext.parser.postprocessor.ElementType.EXT_LINK;
import static org.sweble.wikitext.parser.postprocessor.ElementType.FRAMED_IMG;
import static org.sweble.wikitext.parser.postprocessor.ElementType.H1;
import static org.sweble.wikitext.parser.postprocessor.ElementType.H2;
import static org.sweble.wikitext.parser.postprocessor.ElementType.H3;
import static org.sweble.wikitext.parser.postprocessor.ElementType.H4;
import static org.sweble.wikitext.parser.postprocessor.ElementType.H5;
import static org.sweble.wikitext.parser.postprocessor.ElementType.H6;
import static org.sweble.wikitext.parser.postprocessor.ElementType.INLINE_IMG;
import static org.sweble.wikitext.parser.postprocessor.ElementType.INT_LINK;
import static org.sweble.wikitext.parser.postprocessor.ElementType.LI;
import static org.sweble.wikitext.parser.postprocessor.ElementType.P;
import static org.sweble.wikitext.parser.postprocessor.ElementType.PAGE;
import static org.sweble.wikitext.parser.postprocessor.ElementType.SECTION;
import static org.sweble.wikitext.parser.postprocessor.ElementType.SECTION_BODY;
import static org.sweble.wikitext.parser.postprocessor.ElementType.SECTION_HEADING;
import static org.sweble.wikitext.parser.postprocessor.ElementType.TABLE;
import static org.sweble.wikitext.parser.postprocessor.ElementType.TBODY;
import static org.sweble.wikitext.parser.postprocessor.ElementType.TD;
import static org.sweble.wikitext.parser.postprocessor.ElementType.TFOOT;
import static org.sweble.wikitext.parser.postprocessor.ElementType.TH;
import static org.sweble.wikitext.parser.postprocessor.ElementType.THEAD;
import static org.sweble.wikitext.parser.postprocessor.ElementType.TR;
import static org.sweble.wikitext.parser.postprocessor.ElementType.UNKNOWN;
import static org.sweble.wikitext.parser.postprocessor.ElementType.URL;
import static org.sweble.wikitext.parser.postprocessor.StackScope.GENERAL_SCOPE;
import static org.sweble.wikitext.parser.postprocessor.StackScope.GENERAL_SCOPE_WITHOUT_LAZY_PARSED_PAGE;

import java.util.ListIterator;

import org.sweble.wikitext.parser.WtRtData;
import org.sweble.wikitext.parser.nodes.WtBody;
import org.sweble.wikitext.parser.nodes.WtBold;
import org.sweble.wikitext.parser.nodes.WtDefinitionList;
import org.sweble.wikitext.parser.nodes.WtDefinitionListDef;
import org.sweble.wikitext.parser.nodes.WtDefinitionListTerm;
import org.sweble.wikitext.parser.nodes.WtExternalLink;
import org.sweble.wikitext.parser.nodes.WtHeading;
import org.sweble.wikitext.parser.nodes.WtHorizontalRule;
import org.sweble.wikitext.parser.nodes.WtImEndTag;
import org.sweble.wikitext.parser.nodes.WtImStartTag;
import org.sweble.wikitext.parser.nodes.WtImageLink;
import org.sweble.wikitext.parser.nodes.WtInternalLink;
import org.sweble.wikitext.parser.nodes.WtItalics;
import org.sweble.wikitext.parser.nodes.WtLctVarConv;
import org.sweble.wikitext.parser.nodes.WtLinkTitle;
import org.sweble.wikitext.parser.nodes.WtListItem;
import org.sweble.wikitext.parser.nodes.WtNamedXmlElement;
import org.sweble.wikitext.parser.nodes.WtNewline;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtOrderedList;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.nodes.WtSection;
import org.sweble.wikitext.parser.nodes.WtSemiPre;
import org.sweble.wikitext.parser.nodes.WtSemiPreLine;
import org.sweble.wikitext.parser.nodes.WtTable;
import org.sweble.wikitext.parser.nodes.WtTagExtension;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtTemplateParameter;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtUnorderedList;
import org.sweble.wikitext.parser.nodes.WtUrl;
import org.sweble.wikitext.parser.nodes.WtXmlEmptyTag;
import org.sweble.wikitext.parser.nodes.WtXmlEndTag;
import org.sweble.wikitext.parser.nodes.WtXmlStartTag;

import de.fau.cs.osr.utils.visitor.VisitorLogic;

public final class TreeBuilderInBody
		extends
			TreeBuilderModeBase
{
	public TreeBuilderInBody(
			VisitorLogic<WtNode> logic,
			TreeBuilder treeBuilder)
	{
		super(logic, treeBuilder);
	}

	// =====================================================================

	public void visit(WtImStartTag n)
	{
		switch (getNodeType(n))
		{
			case B:
			case I:
				startTagR28(n);
				break;
			case P:
				startTagR12(n);
				break;
			default:
				throw new AssertionError("Should not happen!");
		}
	}

	public void visit(WtImEndTag n)
	{
		switch (getNodeType(n))
		{
			case B:
			case I:
				endTagR30(n);
				break;

			case P:
				/**
				 * Synthetic paragraph closing tags can be ignored if they don't
				 * have a proper opening tag
				 */
				if (tb.isElementTypeInButtonScope(P))
				{
					/**
					 * An intermediate paragraph closing never has RTD
					 * information attached. We don't have to worry about
					 * whether RTD information is copied to the created element
					 * or not.
					 */
					WtNode pEndTag = getFactory().createSyntheticEndTag(n);
					WtNodeFlags.setParserRecognized(pEndTag);
					endTagR22(pEndTag);
				}
				break;

			default:
				throw new AssertionError("Should not happen!");
		}
	}

	// =====================================================================

	public void visit(WtXmlStartTag n)
	{
		handleStartTag(n);
	}

	public void visit(WtXmlEmptyTag n)
	{
		handleStartTag(n);
	}

	private void handleStartTag(WtNode n)
	{
		ElementType nodeType = getNodeType(n);
		if (nodeType == null)
		{
			startUnknownTag(n);
			return;
		}

		switch (nodeType)
		{
			case ADDRESS: //ARTICLE, ASIDE,
			case BLOCKQUOTE:
			case CENTER: //DETAILS, DIALOG,
			case DIR:
			case DIV:
			case DL:
				/*
				case FIELDSET: //FIGCAPTION, FIGURE, FOOTER, HEADER, HGROUP,
				*/
			case MENU: //NAV,
			case OL:
			case P: //SECTION, SUMMARY,
			case UL:
				startTagR12(n);
				break;
			case H1:
			case H2:
			case H3:
			case H4:
			case H5:
			case H6:
				startTagR13(n);
				break;
			case PRE: /*, "LISTING"*/
				/**
				 * Issue AssertionError #35: I assume that the original idea of the AssertionError was this: &lt;pre>
				 * elements are always handled as tag extension and should never show up here as elements. The problem
				 * is: Tag extension name matching is not done case insensitive while HTML element recognition on the
				 * other hand is case insensitive. An all uppercase PRE element will not be recognized as tag extension
				 * and cause an internal error here. We will now introduce a switch that turns on case insensitive tag
				 * extension name matching if requested but we still have to fix this issue for devs who do not want to
				 * turn on case insensitive matching. We'll treat the element like a real &lt;pre> element.
				 */
				startTagR14(n);
				break;

				// throw new AssertionError("This must not happen!");

				//startTagR14(n);
				//break;
				/*
				case FORM:
				startTagR15(n);
				break;
				*/
			case LI:
				startTagR16(n);
				break;
			case DD:
			case DT:
				startTagR17(n);
				break;
			/*
			case BUTTON:
			startTagR19(n);
			break;
			*/
			/*
			case A:
			throw new AssertionError("This must not happen!");
			*/
			case B:
			case BIG:
			case CODE:
			case EM:
			case FONT:
			case I:
			case S:
			case SMALL:
			case STRIKE:
			case STRONG:
			case TT:
			case U:
				startTagR28(n);
				break;
			case TABLE:
				startTagR33(n);
				break;
			case AREA:
			case BR:
				startTagR34(n);
				break;
			case IMG:
				throw new AssertionError("This must not happen!");
				/*
				case INPUT:
				startTagR35(n);
				break;
				*/
			case HR:
				startTagR37(n);
				break;
			/*
			case TEXTAREA:
			startTagR40(n);
			break;
			case SELECT:
			startTagR44(n);
			break;
			case OPTGROUP:
			case OPTION:
			startTag45(n);
			break;
			*/
			case CAPTION:
				//			case COL:
				//			case COLGROUP:
			case TD:
			case TH:
			case TR:
				startTagR50(n);
				break;
			default:
				if (nodeType == UNKNOWN)
					startUnknownTag(n);
				else
					startTagR51(n);
				break;
		}
	}

	private void startUnknownTag(WtNode n)
	{
		if (n instanceof WtNamedXmlElement)
		{
			String name = ((WtNamedXmlElement) n).getName().toLowerCase();
			switch (getNonStandardElementBehavior(name))
			{
				case LIKE_BR:
					// br, img, ...
					startTagR34(n);
					break;

				case LIKE_DIV:
					// div, ul, ...
					startTagR12(n);
					break;

				case LIKE_ANY_OTHER:
				case UNSPECIFIED:
				default:
					// any other tag
					startTagR51(n);
					break;
			}
		}
		else
		{
			// any other tag
			startTagR51(n);
		}
	}

	public void visit(WtXmlEndTag n)
	{
		ElementType nodeType = getNodeType(n);
		if (nodeType == null)
		{
			endUnknownTag(n);
			return;
		}

		switch (nodeType)
		{
			case ADDRESS: //ARTICLE, ASIDE,
			case BLOCKQUOTE:
				/*
				case BUTTON:
				*/
			case CENTER: //DETAILS, DIALOG,
			case DIR:
			case DIV:
			case DL:
				/*
				case FIELDSET: //FIGCAPTION, FIGURE, FOOTER, HEADER, HGROUP, LISTING,
				*/
			case MENU: //NAV,
			case OL: //PRE, SECTION, SUMMARY,
			case UL:
				endTagR20(n);
				break;
			/*
			case FORM:
			endTagR21(n);
			break;
			*/
			case P:
				endTagR22(n);
				break;
			case LI:
				endTagR23(n);
				break;
			case DD:
			case DT:
				endTagR24(n);
				break;
			case H1:
			case H2:
			case H3:
			case H4:
			case H5:
			case H6:
				endTagR25(n);
				break;
			/*
			case A:
			*/
			case INT_LINK:
			case EXT_LINK:
			case URL:

			case B:
			case BIG:
			case CODE:
			case EM:
			case FONT:
			case I: //NOBR
			case S:
			case SMALL:
			case STRIKE:
			case STRONG:
			case TT:
			case U:
				endTagR30(n);
				break;
			case BR:
				endTagR47(n);
				break;
			default:
				if (nodeType == UNKNOWN)
					endUnknownTag(n);
				else
					endTagR52(n);
		}
	}

	private void endUnknownTag(WtNode n)
	{
		if (n instanceof WtNamedXmlElement)
		{
			String name = ((WtNamedXmlElement) n).getName().toLowerCase();
			switch (getNonStandardElementBehavior(name))
			{
				case LIKE_BR:
					// br, img, ...
					endTagR47(n);
					break;

				case LIKE_DIV:
					// div, ul, ...
					endTagR20(n);
					break;

				case LIKE_ANY_OTHER:
				case UNSPECIFIED:
				default:
					// any other tag
					endTagR52(n);
					break;
			}
		}
		else
		{
			// any other tag
			endTagR52(n);
		}
	}

	// =========================================================================
	/* Nodes to consider:
	 * InnerNode
	 * - InnerNode1
	 *   - ContentNode
	 *     - WtBold						[X]
	 *     - DefinitionDefinition		[X]
	 *     - DefinitionList				[X]
	 *     - DefinitionTerm				[X]
	 *     - WtOrderedList				[X]
	 *     - WtItalics					[X]
	 *     - Itemization				[X]
	 *     - WtParsedWikitextPage				[X]
	 *     - ListItem					[X]
	 *     - WtLinkTitle					[X]
	 *     - Paragraph					[-]			// Not yet in AST
	 *     - SemiPre					[X]
	 *     - SemiPreLine				[-]
	 *   - InternalLink					[X]
	 *   - TagExtension					[X]
	 *   - WtXmlEmptyTag				[X]
	 *   - WtXmlStartTag				[X]
	 *     - WtImStartTag					[X]
	 * - InnerNode2
	 *   - ExternalLink					[X]
	 *   - WtImageLink					[X]
	 *   - Section						[ ]
	 *   - Table						[X]
	 *   - TableCaption					[X]
	 *   - TableCell					[X]
	 *   - TableHeader					[X]
	 *   - TabelRow						[X]
	 *   - Template						[X]
	 *   - WtXmlElement					[-]			// Not yet in AST
	 * - InnerNode3
	 *   - TemplateParameter			[X]
	 * WtLeafNode							[X]
	 * - HorizontalRule					[X]
	 * - IllegalCodePoint				[X]
	 * - PageSwitch						[X]
	 * - ParserEntity					[X]
	 * - Redirect						[X]
	 * - Signature						[X]
	 * - StringContentNode
	 *   - Ignored						[X]
	 *   - WtNewline						[X]
	 *   - WtText							[X]
	 *     - ProtectedText				[-]			// Not yet in AST
	 *   - XmlComment					[X]
	 * - Url							[X]
	 * - XmlCharRef						[X]
	 * - WtXmlEndTag				[X]
	 *   - WtImEndTag					[X]
	 * - XmlEntityRef					[X]
	 */

	public void visit(WtNode n)
	{
		switch (n.getNodeType())
		{
		// -- leaf nodes --

			case NT_ILLEGAL_CODE_POINT:
			case NT_SIGNATURE:
			case NT_XML_CHAR_REF:
			case NT_XML_ENTITY_REF:
				// Treat these like text
				tokenR01andR02(n);
				break;
			case WtNode.NT_PARSER_ENTITY:
			case NT_PAGE_SWITCH:
			case NT_REDIRECT:
			case NT_IGNORED:
			case NT_XML_COMMENT:
				// Treat these like comments
				tokenR03(n);
				break;

			// -- unexpected nodes --

			case NT_SEMI_PRE:
				throw new AssertionError("Node should only occur in SemiPre scope: " + n.getClass().getSimpleName());

			case NT_TABLE_CAPTION:
			case NT_TABLE_CELL:
			case NT_TABLE_HEADER:
			case NT_TABLE_ROW:
				//throw new AssertionError("Node should only occur in Table scope: " + n.getClass().getSimpleName());
				// Although native WtNode tables elements are always correctly 
				// nested by the parser, it is possible that the TreeBuilder
				// leaves the table/row/cell scope before all the tables/... 
				// children were processed. And in that case, we can end up 
				// here...
				startTagR50(n);
				break;

			case NT_XML_ELEMENT:
			default:
				//throw new AssertionError("Unhandled node: " + n.getClass().getSimpleName());
				// Treat these like comments
				tokenR03(n);
				break;
		}
	}

	public void visit(WtParsedWikitextPage n)
	{
		// insertAnHtmlElement
		WtNode newNode = getFactory().createNewElement(n);
		tb.getStack().push(newNode);
		tb.setRootNode((WtParsedWikitextPage) newNode);

		iterate(n);

		// 12.2.5.4.7  R10
		// 12.2.5.4.17 R05
		// 12.2.5.4.21 R05

		if (!tb.isElementTypeInSpecificScope(GENERAL_SCOPE_WITHOUT_LAZY_PARSED_PAGE, PAGE))
			tb.error(n, "12.2.5.4.7 R10 (1)");

		for (WtNode node : tb.getStack())
		{
			switch (getNodeType(node))
			{
				case DD:
				case DT:
				case LI:
					// optgroup
					// option
				case P:
					// rp
					// rt
				case TABLE: // == tbody, tfoot, thead
				case TD:
				case TH:
				case TR:
				case PAGE:
					break;
				default:
					tb.error(n, "12.2.5.4.7 R10 (2)");
					break;
			}
		}
	}

	public void visit(WtText n)
	{
		tokenR01andR02(n);
	}

	public void visit(WtNewline n)
	{
		tokenR01andR02(getFactory().text(n.getContent()));
	}

	public void visit(WtTemplate n)
	{
		tokenR03(n);
	}

	public void visit(WtTemplateParameter n)
	{
		tokenR03(n);
	}

	public void visit(WtTagExtension n)
	{
		tokenR03(n);
	}

	public void visit(WtHorizontalRule n)
	{
		startTagR37(n);
	}

	public void visit(WtBold n)
	{
		startTagR28(n);
		iterate(n);
		endTagR30(getFactory().createSyntheticEndTag(n));
	}

	public void visit(WtItalics n)
	{
		startTagR28(n);
		iterate(n);
		endTagR30(getFactory().createSyntheticEndTag(n));
	}

	public void visit(WtListItem n)
	{
		startTagR16(n);
		iterate(n);
		endTagR23(getFactory().createSyntheticEndTag(n));
	}

	public void visit(WtOrderedList n)
	{
		startTagR12(n);
		iterate(n);
		endTagR20(getFactory().createSyntheticEndTag(n));
	}

	public void visit(WtUnorderedList n)
	{
		startTagR12(n);
		iterate(n);
		endTagR20(getFactory().createSyntheticEndTag(n));
	}

	public void visit(WtDefinitionList n)
	{
		startTagR12(n);
		iterate(n);
		endTagR20(getFactory().createSyntheticEndTag(n));
	}

	public void visit(WtDefinitionListDef n)
	{
		startTagR17(n);
		iterate(n);
		endTagR24(getFactory().createSyntheticEndTag(n));
	}

	public void visit(WtDefinitionListTerm n)
	{
		startTagR17(n);
		iterate(n);
		endTagR24(getFactory().createSyntheticEndTag(n));
	}

	public void visit(WtInternalLink n)
	{
		startTagR27(n);

		if (n.hasTitle())
			dispatch(n.getTitle());

		dispatch(getFactory().createSyntheticEndTag(n));
	}

	public void visit(WtExternalLink n)
	{
		startTagR27(n);

		if (n.hasTitle())
			dispatch(n.getTitle());

		dispatch(getFactory().createSyntheticEndTag(n));
	}

	public void visit(WtUrl n)
	{
		startTagR27(n);
		dispatch(getFactory().createSyntheticEndTag(n));
	}

	public void visit(WtImageLink n)
	{
		if (TreeBuilder.isInlineImage(n))
		{
			startTagR51(n);

			if (n.hasTitle())
				dispatch(n.getTitle());

			dispatch(getFactory().createSyntheticEndTag(n, INLINE_IMG));
		}
		else
		{
			startTagR12(n);

			if (n.hasTitle())
				dispatch(n.getTitle());

			dispatch(getFactory().createSyntheticEndTag(n, FRAMED_IMG));
		}
	}

	public void visit(WtLinkTitle n)
	{
		iterate(n);
	}

	public void visit(WtSemiPre n)
	{
		startTagR12(n);

		if (!n.isEmpty())
			iterate(n);

		dispatch(getFactory().createSyntheticEndTag(n));
	}

	public void visit(WtSemiPreLine n)
	{
		// Lines are dissolved which is why we have to save RTD info.
		WtRtData rtd = n.getRtd();
		if (rtd != null)
			iterateSemiPreRtdField(rtd.getField(0));
		iterate(n);
		if (rtd != null)
			iterateSemiPreRtdField(rtd.getField(1));
	}

	private void iterateSemiPreRtdField(Object[] field)
	{
		//boolean sawFirstSpace = false;
		for (Object o : field)
		{
			if (o instanceof WtNode)
			{
				dispatch((WtNode) o);
			}
			else
			{
				String text = String.valueOf(o);
				/*
				if (!sawFirstSpace && !text.isEmpty() && text.charAt(0) == ' ')
				{
					sawFirstSpace = true;
					if (text.length() == 1)
						continue;
					
					text = text.substring(1);
				}
				*/
				dispatch(getFactory().text(text));
			}
		}
	}

	public void visit(WtTable n)
	{
		startTagR33(n);

		if (n.hasBody())
		{
			iterate(n.getBody());

			dispatch(getFactory().createSyntheticEndTag(n));
		}
	}

	public void visit(WtSection n)
	{
		// We treat the section title, body and the section itself as DIV
		// However, when it comes to stack scopes we treat those elements
		// like something impenetrable, like a PAGE.

		startTagR12(n);
		WtSection section = (WtSection) tb.getCurrentNode();

		// -- title ----

		if (visitSectionHeading(n.getHeading()))
		{

			if (tb.getCurrentNode() != section)
				throw new AssertionError("Stack of open elements corrupted!");

			// -- body ----

			boolean processedBody = false;
			if (n.hasBody())
			{
				if (visitSectionBody(n.getBody()))
				{
					processedBody = true;

					if (tb.getCurrentNode() != section)
						throw new AssertionError("Stack of open elements corrupted!");
				}
			}

			// -- done ----

			if (processedBody)
			{
				// Almost: endTagR20(getFactory().synEndTag(section));
				tb.generateImpliedEndTags();

				if (getNodeType(tb.getCurrentNode()) != SECTION)
					tb.error(n, "12.2.5.4.7 - R20 (2)");

				// We don't want native wiki markup section to interrupt table cells
				//tb.popFromStackUntilIncluding(SECTION);

				tb.popFromStackUntilExcluding(PAGE, SECTION, TABLE, TBODY, TFOOT, THEAD, TR, TD, TH, CAPTION);
				if (tb.getCurrentNode() == section)
					tb.popFromStack();
			}
		}
		else
		{
			// just iterate the body, don't treat it as part of a section
			if (n.hasBody())
				iterate(n.getBody());
		}
	}

	/**
	 * @return True if the heading was fully processed. False if the heading was
	 *         interrupted by another element that forced the heading to end
	 *         prematurely.
	 */
	public boolean visitSectionHeading(WtHeading heading)
	{
		if (tb.isElementTypeInButtonScope(P))
			dispatch(getFactory().createMissingRepairEndTag(P));

		// Almost: tb.insertAnHtmlElement(heading);
		WtHeading newNode = (WtHeading) getFactory().createNewElement(heading);
		((WtSection) tb.getCurrentNode()).setHeading(newNode);
		tb.getStack().push(newNode);
		// ---

		iterate(heading);
		if (!tb.isInStackOfOpenElements(newNode))
			//throw new AssertionError("Section heading was removed from stack prematurely!");
			return false;

		// Almost: endTagR20(getFactory().synEndTag(heading));
		tb.generateImpliedEndTags();

		if (getNodeType(tb.getCurrentNode()) != SECTION_HEADING)
			tb.error(heading, "12.2.5.4.7 - R20 (2)");

		// We don't want native wiki markup section to interrupt table cells
		//tb.popFromStackUntilIncluding(SECTION_HEADING);

		tb.popFromStackUntilExcluding(PAGE, SECTION_HEADING, TABLE, TBODY, TFOOT, THEAD, TR, TD, TH, CAPTION);
		if (tb.getCurrentNode() == newNode)
		{
			tb.popFromStack();
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @return True if the body was fully processed. False if the body was
	 *         interrupted by another element that forced the body to end
	 *         prematurely.
	 */
	public boolean visitSectionBody(WtBody body)
	{
		if (tb.isElementTypeInButtonScope(P))
			/**
			 * I don't think that's even possible. A section body can only be
			 * started after a section and a section cannot end with a P in
			 * scope
			 */
			dispatch(getFactory().createMissingRepairEndTag(P));

		// Almost: tb.insertAnHtmlElement(heading);
		WtBody newNode = (WtBody) getFactory().createNewElement(body);
		((WtSection) tb.getCurrentNode()).setBody(newNode);
		tb.getStack().push(newNode);
		// ---

		iterate(body);
		if (!tb.isInStackOfOpenElements(newNode))
			//throw new AssertionError("Section body was removed from stack prematurely!");
			return false;

		// Almost: endTagR20(getFactory().synEndTag(body));
		tb.generateImpliedEndTags();

		if (getNodeType(tb.getCurrentNode()) != SECTION_BODY)
			tb.error(body, "12.2.5.4.7 - R20 (2)");

		// We don't want native wiki markup section to interrupt table cells
		//tb.popFromStackUntilIncluding(SECTION_BODY);

		tb.popFromStackUntilExcluding(PAGE, SECTION_BODY, TABLE, TBODY, TFOOT, THEAD, TR, TD, TH, CAPTION);
		if (tb.getCurrentNode() == newNode)
		{
			tb.popFromStack();
			return true;
		}
		else
		{
			return false;
		}
	}

	public void visit(WtLctVarConv n)
	{
		startTagR28(n);
		iterate(n.getText());
		endTagR30(getFactory().createSyntheticEndTag(n));
	}

	// =====================================================================

	/**
	 * R01: Whitespace token
	 * 
	 * R02: Any other character token
	 */
	private void tokenR01andR02(WtText text)
	{
		tb.reconstructActiveFormattingElements();
		tb.insertText(text);
	}

	private void tokenR01andR02(WtNode n)
	{
		tb.reconstructActiveFormattingElements();
		tb.appendToCurrentNode(n);
	}

	/**
	 * R03: Comment token
	 */
	private void tokenR03(WtNode n)
	{
		tb.appendToCurrentNode(n);
	}

	/**
	 * R12: A start tag whose tag name is one of: address, article, aside,
	 * blockquote, center, details, dialog, dir, div, dl, fieldset, figcaption,
	 * figure, footer, header, hgroup, menu, nav, ol, p, section, summary, ul
	 */
	private void startTagR12(WtNode n)
	{
		if (tb.isElementTypeInButtonScope(P))
			dispatch(getFactory().createMissingRepairEndTag(P));
		tb.insertAnHtmlElement(n);
	}

	/**
	 * R13: A start tag whose tag name is one of: h1, h2, h3, h4, h5, h6
	 */
	private void startTagR13(WtNode n)
	{
		if (tb.isElementTypeInButtonScope(P))
			dispatch(getFactory().createMissingRepairEndTag(P));
		if (tb.isCurrentNodeTypeOneOf(H1, H2, H3, H4, H5, H6))
		{
			tb.error(n, "12.2.5.4.7 - R13");
			tb.popFromStack();
		}
		tb.insertAnHtmlElement(n);
	}

	/**
	 * R14: A start tag whose tag name is one of: "pre", "listing"
	 */
	private void startTagR14(WtNode n)
	{
		if (tb.isElementTypeInButtonScope(P))
			dispatch(getFactory().createMissingRepairEndTag(P));
		tb.insertAnHtmlElement(n);
		// FIXME: If the next token is a U+000A LINE FEED (LF) character token, then ignore that token and move on to
		// the next one. (Newlines at the start of pre blocks are ignored as an authoring convenience.)
	}
	
	/**
	 * R16: A start tag whose tag name is li
	 */
	private void startTagR16(WtNode n)
	{
		for (WtNode node : tb.getStack())
		{
			ElementType nodeType = getNodeType(node);
			if (nodeType == LI)
			{
				dispatch(getFactory().createMissingRepairEndTag(LI));
				break;
			}
			if (nodeType.isSpecial() && !isTypeOneOf(nodeType, ADDRESS, DIV, P))
				break;
		}
		if (tb.isElementTypeInButtonScope(P))
			dispatch(getFactory().createMissingRepairEndTag(P));
		tb.insertAnHtmlElement(n);
	}

	/**
	 * R17: A start tag whose tag name is one of: dd, dt
	 */
	private void startTagR17(WtNode n)
	{
		for (WtNode node : tb.getStack())
		{
			ElementType nodeType = getNodeType(node);
			if (isTypeOneOf(nodeType, DD, DT))
			{
				dispatch(getFactory().createMissingRepairEndTag(nodeType));
				break;
			}
			if (nodeType.isSpecial() && !isTypeOneOf(nodeType, ADDRESS, DIV, P))
				break;
		}
		if (tb.isElementTypeInButtonScope(P))
			dispatch(getFactory().createMissingRepairEndTag(P));
		tb.insertAnHtmlElement(n);
	}

	/**
	 * R20: An end tag whose tag name is one of: address, article, aside,
	 * blockquote, button, center, details, dialog, dir, div, dl, fieldset,
	 * figcaption, figure, footer, header, hgroup, listing, menu, nav, ol, pre,
	 * section, summary, ul
	 */
	private void endTagR20(WtNode n)
	{
		ElementType elementType = getNodeType(n);
		if (elementType == UNKNOWN)
		{
			unknownEndTagR20(n);
		}
		else
		{
			if (!tb.isElementTypeInScope(elementType))
			{
				tb.error(n, "12.2.5.4.7 - R20 (1)");
				tb.ignore(n);
			}
			else
			{
				tb.generateImpliedEndTags();

				if (getNodeType(tb.getCurrentNode()) != elementType)
					tb.error(n, "12.2.5.4.7 - R20 (2)");

				addRtDataOfEndTag(
						tb.popFromStackUntilIncluding(elementType),
						n);
			}
		}
	}

	/**
	 * Like R20 but for unknown tags.
	 */
	private void unknownEndTagR20(WtNode n)
	{
		if (!tb.isNodeInSpecificScope(GENERAL_SCOPE, n))
		{
			tb.error(n, "12.2.5.4.7 - R20 (1)");
			tb.ignore(n);
		}
		else
		{
			tb.generateImpliedEndTags();

			if (!TreeBuilder.isSameTag(tb.getCurrentNode(), n))
				tb.error(n, "12.2.5.4.7 - R20 (2)");

			addRtDataOfEndTag(
					tb.popFromStackUntilIncluding(n),
					n);
		}
	}

	/**
	 * R22: An end tag whose tag name is p
	 */
	private void endTagR22(WtNode n)
	{
		if (!tb.isElementTypeInButtonScope(P))
		{
			tb.error(n, "12.2.5.4.7 - R22 (1)");
			dispatch(getFactory().createMissingRepairStartTag(P));
			dispatch(n);
		}
		else
		{
			tb.generateImpliedEndTags(P);

			if (getNodeType(tb.getCurrentNode()) != P)
				tb.error(n, "12.2.5.4.7 - R22 (2)");

			WtNode p = tb.popFromStackUntilIncluding(P);
			addRtDataOfEndTag(p, n);

			if (!WtNodeFlags.isParserRecognized(n))
			{
				/* Hannes: When a paragraph is implicitly closed in front of
				 * another block element we have to manually fix newlines. If
				 * the parser guessed the end tag of a paragraph correctly, this
				 * is already done by the paser. If not, all newlines are
				 * located in front of the next block element and the implicit
				 * closing tag will come after those newlines. We'll have to
				 * move all but two newlines after the implicit closing node.
				 */

				/* Newlines are converted to text nodes and text nodes are
				 * merged. Therefore, if there is a newline at the end of the
				 * paragraph, the last child of the paragraph must be a text node
				 * which will contain the newlines.
				 */

				int last = p.size() - 1;
				if (last > 0)
				{
					WtNode t = p.get(last);
					if (t.getNodeType() == WtNode.NT_TEXT)
					{
						WtText tn = (WtText) t;
						String text = tn.getContent();

						// extract all but the first two newlines
						int count = 0;
						int lastNewline = -1;
						outer: for (int j = text.length() - 1; j >= 0; --j)
						{
							char ch = text.charAt(j);
							switch (ch)
							{
								case ' ':
								case '\t':
									continue;
								case '\n':
									++count;
									if (count <= 2)
										lastNewline = j;
									break;
								default:
									break outer;
							}
						}

						// if we found newlines...
						if (count > 0)
						{
							// remove at most 2 from paragraph
							String outerNewlines;
							if (lastNewline > 0)
							{
								tn.setContent(text.substring(0, lastNewline));
								outerNewlines = text.substring(lastNewline);
							}
							else
							{
								p.remove(last);
								outerNewlines = text;
							}

							// and insert them after paragraph
							tokenR01andR02(getFactory().text(outerNewlines));
						}
					}
				}
			}
		}
	}

	/**
	 * R23: An end tag whose tag name is li
	 */
	private void endTagR23(WtNode n)
	{
		if (!tb.isElementTypeInListScope(LI))
		{
			tb.error(n, "12.2.5.4.7 - R23 (1)");
			tb.ignore(n);
		}
		else
		{
			tb.generateImpliedEndTags(LI);

			if (getNodeType(tb.getCurrentNode()) != LI)
				tb.error(n, "12.2.5.4.7 - R23 (2)");

			addRtDataOfEndTag(
					tb.popFromStackUntilIncluding(LI),
					n);
		}
	}

	/**
	 * R24: An end tag whose tag name is one of: dd, dt
	 */
	private void endTagR24(WtNode n)
	{
		ElementType nodeType = getNodeType(n);
		if (!tb.isElementTypeInScope(nodeType))
		{
			tb.error(n, "12.2.5.4.7 - R24 (1)");
			tb.ignore(n);
		}
		else
		{
			tb.generateImpliedEndTags(nodeType);

			if (getNodeType(tb.getCurrentNode()) != nodeType)
				tb.error(n, "12.2.5.4.7 - R24 (2)");

			addRtDataOfEndTag(
					tb.popFromStackUntilIncluding(nodeType),
					n);
		}
	}

	/**
	 * R25: An end tag whose tag name is one of: h1, h2, h3, h4, h5, h6
	 */
	private void endTagR25(WtNode n)
	{
		if (!tb.isOneOfElementTypesInScope(H1, H2, H3, H4, H5, H6))
		{
			tb.error(n, "12.2.5.4.7 - R25 (1)");
			tb.ignore(n);
		}
		else
		{
			tb.generateImpliedEndTags();

			if (getNodeType(tb.getCurrentNode()) != getNodeType(n))
				tb.error(n, "12.2.5.4.7 - R25 (2)");

			addRtDataOfEndTag(
					tb.popFromStackUntilIncluding(H1, H2, H3, H4, H5, H6),
					n);
		}
	}

	/**
	 * R27: A start tag whose tag name is "a"
	 */
	private void startTagR27(WtNode n)
	{
		// Only one of those can actually happen -> short circuit logic
		if (forceCloseLink(n, INT_LINK) ||
				forceCloseLink(n, EXT_LINK) ||
				forceCloseLink(n, URL))
			/* do nothing */;

		tb.reconstructActiveFormattingElements();

		WtNode a = tb.insertAnHtmlElement(n);
		tb.pushActiveFormattingElements(a);
	}

	private boolean forceCloseLink(WtNode n, ElementType type)
	{
		WtNode active = tb.getActiveFormattingElement(type);
		if (active != null)
		{
			tb.error(n, "12.2.5.4.7 - R27");

			dispatch(getFactory().createMissingRepairEndTag(type));

			// Remove if not already done 
			// (can happen if not in table scope)
			if (tb.isInStackOfOpenElements(active))
				tb.removeFromStack(active);
			if (tb.isInListOfActiveFormattingElements(active))
				tb.removeFromActiveFormattingElements(active);

			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * R28: A start tag whose tag name is one of: b, big, code, em, font, i, s,
	 * small, strike, strong, tt, u
	 */
	private void startTagR28(WtNode n)
	{
		tb.reconstructActiveFormattingElements();

		WtNode e = tb.insertAnHtmlElement(n);
		tb.pushActiveFormattingElements(e);
	}

	/**
	 * R30: An end tag whose tag name is one of: a, b, big, code, em, font, i,
	 * nobr, s, small, strike, strong, tt, u
	 */
	private void endTagR30(WtNode n)
	{
		ElementType nodeType = getNodeType(n);
		for (int i = 0; i < 8;)
		{
			++i;

			WtNode fe = tb.getActiveFormattingElement(nodeType);
			if (fe == null)
			{
				// act as described in R52 -> call directly
				endTagR52(n);
				return;
			}
			else if (!tb.isInStackOfOpenElements(fe))
			{
				tb.error(n, "12.2.5.4.7 - R30 (1)");
				tb.removeFromActiveFormattingElements(fe);
				tb.ignore(n);
				return;
			}
			else if (!tb.isNodeRefInScope(fe))
			{
				tb.error(n, "12.2.5.4.7 - R30 (2)");
				tb.ignore(n);
				return;
			}
			else if (tb.getCurrentNode() != fe)
			{
				tb.error(n, "12.2.5.4.7 - R30 (3)");
			}

			ListIterator<WtNode> stackIter = tb.getStack().listIterator();

			WtNode commonAncestor = null;
			while (stackIter.hasNext())
			{
				WtNode e = stackIter.next();
				if (e == fe)
				{
					commonAncestor = stackIter.next();
					stackIter.previous();
					stackIter.previous();
					break;
				}
			}

			if (commonAncestor == null)
				throw new AssertionError();

			WtNode furthestBlock = null;
			WtNode furthestBlockParent = fe;
			while (stackIter.hasPrevious())
			{
				WtNode e = stackIter.previous();
				if (getNodeType(e).isSpecial())
				{
					furthestBlock = e;
					break;
				}
				furthestBlockParent = e;
			}

			if (furthestBlock == null)
			{
				tb.popFromStackUntilIncludingRef(fe);
				addRtDataOfEndTag(fe, n);

				tb.removeFromActiveFormattingElements(fe);
				return;
			}

			// Step 8
			tb.placeBookmarkAfter(fe);

			// Step 9
			// Note: stackIter last returned furthestBlock after call to 
			// previous. Make sure the next next() call will return the node 
			// "above" furthestBlock.
			stackIter.next();
			WtNode node = furthestBlock;
			WtNode lastNode = furthestBlock;
			WtNode lastNodeParent = furthestBlockParent;
			inner: for (int j = 0; j < 3;)
			{
				++j;

				// Step 9.4
				node = stackIter.next();

				// Step 9.5
				if (!tb.isInListOfActiveFormattingElements(node))
				{
					// Node is guaranteed to be on stack. From here we 
					// definitely get to step 9.4 next. Make sure, we know
					// which node was "above" node before node was removed.
					stackIter.remove();
					continue inner;
				}
				else if (node == fe)
				{
					break inner;
				}

				WtNode replacement = getFactory().createRepairFormattingElement(node);
				tb.replaceInListOfActiveFormattingElements(node, replacement);
				stackIter.set(replacement);
				node = replacement;

				// Step 9.8
				if (lastNode == furthestBlock)
					tb.moveBookmarkAfter(replacement);

				// Step 9.9
				if (lastNodeParent != null)
					tb.removeFromParent(lastNode, lastNodeParent);
				tb.getContentOfNodeForModification(node).add(lastNode);

				// Step 9.10
				lastNode = node;
				// Node is the replacement node that has just been created. 
				// Since lastNode is set to node and node has not yet been 
				// inserted into a parent element, lastNode's parent is set
				// to null.
				lastNodeParent = null;
			}

			// Step 10
			if (isNodeOneOf(commonAncestor, TABLE, TBODY, TFOOT, THEAD, TR))
			{
				if (lastNodeParent != null)
					tb.removeFromParent(lastNode, lastNodeParent);
				tb.insertInFosterParent(lastNode);
			}
			else
			{
				if (lastNodeParent != null)
					tb.removeFromParent(lastNode, lastNodeParent);
				tb.getContentOfNodeForModification(commonAncestor).add(lastNode);
			}

			WtNode adopter = getFactory().createAdopterElement(fe);

			WtNodeList contentOfFb = tb.getContentOfNodeForModification(furthestBlock);
			tb.getContentOfNodeForModification(adopter).addAll(contentOfFb);
			contentOfFb.clear();

			contentOfFb.add(adopter);

			tb.replaceBookmarkWithAndRemove(adopter, fe);

			tb.removeFromStack(fe);
			tb.insertOnStackBelow(furthestBlock, adopter);
		}
	}

	/**
	 * R33: A start tag whose tag name is table
	 */
	private void startTagR33(WtNode n)
	{
		// We don't support quirks mode!
		if (tb.isElementTypeInButtonScope(P))
			dispatch(getFactory().createMissingRepairEndTag(P));

		tb.insertAnHtmlElement(n);

		tb.switchInsertionMode(InsertionMode.IN_TABLE);
	}

	/**
	 * R34: A start tag whose tag name is one of: area, br, embed, img, keygen,
	 * wbr
	 */
	private void startTagR34(WtNode n)
	{
		tb.reconstructActiveFormattingElements();

		tb.insertAnHtmlElement(n);

		tb.popFromStack();
	}

	/**
	 * R37: A start tag whose tag name is hr
	 */
	private void startTagR37(WtNode n)
	{
		if (tb.isElementTypeInButtonScope(P))
			dispatch(getFactory().createMissingRepairEndTag(P));

		tb.insertAnHtmlElement(n);

		tb.popFromStack();
	}

	/**
	 * R47: An end tag whose tag name is br
	 */
	private void endTagR47(WtNode n)
	{
		tb.error(n, "12.2.5.4.7 - R47");
		// This is an exception for which the create() method knows a special rule
		handleStartTag(getFactory().createElementRepair(n));
	}

	/**
	 * R50: A start tag whose tag name is one of: caption, col, colgroup, frame,
	 * head, tbody, td, tfoot, th, thead, tr
	 */
	private void startTagR50(WtNode n)
	{
		tb.error(n, "12.2.5.4.7 - R50");
		tb.ignore(n);
	}

	/**
	 * R51: Any other start tag
	 */
	private void startTagR51(WtNode n)
	{
		tb.reconstructActiveFormattingElements();

		tb.insertAnHtmlElement(n);

		if (n.getNodeType() == NT_XML_EMPTY_TAG)
			tb.popFromStack();
	}

	/**
	 * R41: Any other end tag
	 */
	private void endTagR52(WtNode n)
	{
		for (WtNode node : tb.getStack())
		{
			if (TreeBuilder.isSameTag(node, n))
			{
				tb.generateImpliedEndTags(n);

				if (!TreeBuilder.isSameTag(tb.getCurrentNode(), n))
					tb.error(n, "12.2.5.4.7 - R52 (1)");

				tb.popFromStackUntilIncludingRef(node);
				addRtDataOfEndTag(node, n);
				return;
			}
			else if (getNodeType(node).isSpecial())
			{
				tb.error(n, "12.2.5.4.7 - R52 (2)");
				tb.ignore(n);
				return;
			}
		}
	}
}
