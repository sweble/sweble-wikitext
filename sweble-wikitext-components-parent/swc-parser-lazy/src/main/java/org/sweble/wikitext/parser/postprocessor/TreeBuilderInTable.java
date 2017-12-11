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

import static org.sweble.wikitext.parser.postprocessor.ElementType.CAPTION;
import static org.sweble.wikitext.parser.postprocessor.ElementType.COL;
import static org.sweble.wikitext.parser.postprocessor.ElementType.COLGROUP;
import static org.sweble.wikitext.parser.postprocessor.ElementType.TABLE;
import static org.sweble.wikitext.parser.postprocessor.ElementType.TBODY;
import static org.sweble.wikitext.parser.postprocessor.ElementType.TD;
import static org.sweble.wikitext.parser.postprocessor.ElementType.TH;
import static org.sweble.wikitext.parser.postprocessor.ElementType.TR;

import org.sweble.wikitext.parser.nodes.WtNewline;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTable;
import org.sweble.wikitext.parser.nodes.WtTableCaption;
import org.sweble.wikitext.parser.nodes.WtTableCell;
import org.sweble.wikitext.parser.nodes.WtTableHeader;
import org.sweble.wikitext.parser.nodes.WtTableRow;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtXmlComment;
import org.sweble.wikitext.parser.nodes.WtXmlEndTag;
import org.sweble.wikitext.parser.nodes.WtXmlStartTag;

import de.fau.cs.osr.utils.StringTools;
import de.fau.cs.osr.utils.visitor.VisitorLogic;

public final class TreeBuilderInTable
		extends
			TreeBuilderModeBase
{
	public TreeBuilderInTable(VisitorLogic<WtNode> logic,
			TreeBuilder treeBuilder)
	{
		super(logic, treeBuilder);
	}

	// =========================================================================

	public void visit(WtXmlStartTag n)
	{
		ElementType nodeType = getNodeType(n);
		if (nodeType == null)
		{
			anythingElseR16(n);
			return;
		}

		switch (nodeType)
		{
			case CAPTION:
				startTagR04(n);
				break;
			case COLGROUP:
				startTagR05(n);
				break;
			case COL:
				startTagR06(n);
				break;
			case TBODY:
			case TFOOT:
			case THEAD:
				startTagR07(n);
				break;
			case TD:
			case TH:
			case TR:
				startTagR08(n);
				break;
			case TABLE:
				startTagR09(n);
				break;
			case P:
				/**
				 * The parser generates intermediate paragraph tags inside HTML
				 * tables because it does not understand the scope. We can
				 * simply ignore those intermediate paragraphs which do not
				 * contain any other information that could get lost.
				 */
				if (n.getNodeType() != WtNode.NT_IM_START_TAG)
					anythingElseR16(n);
				break;
			default:
				anythingElseR16(n);
				return;
		}
	}

	public void visit(WtXmlEndTag n)
	{
		ElementType nodeType = getNodeType(n);
		if (nodeType == null)
		{
			anythingElseR16(n);
			return;
		}

		switch (nodeType)
		{
			case TABLE:
				endTagR10(n);
				break;
			case PAGE: // == HTML, BODY
			case CAPTION:
			case COL:
			case COLGROUP:
			case TBODY:
			case TD:
			case TFOOT:
			case TH:
			case THEAD:
			case TR:
				endTagR11(n);
				break;
			case P:
				/**
				 * The parser generates intermediate paragraph tags inside HTML
				 * tables because it does not understand the scope. We can
				 * simply ignore those intermediate paragraphs which do not
				 * contain any other information that could get lost.
				 */
				if (n.getNodeType() != WtNode.NT_IM_END_TAG)
					anythingElseR16(n);
				break;
			default:
				anythingElseR16(n);
				break;
		}
	}

	// =========================================================================

	public void visit(WtNode n)
	{
		anythingElseR16(n);
	}

	public void visit(WtText n)
	{
		ElementType nodeType = getNodeType(tb.getCurrentNode());
		if (nodeType == null)
		{
			anythingElseR16(n);
			return;
		}

		switch (nodeType)
		{
			case TABLE:
			case TBODY:
			case TFOOT:
			case THEAD:
			case TR:
				tokenR01(n);
				break;

			default:
				anythingElseR16(n);
				break;
		}
	}

	public void visit(WtNewline n)
	{
		dispatch(getFactory().text(n.getContent()));
	}

	public void visit(WtXmlComment n)
	{
		tokenR02(n);
	}

	/*
	public void visit(WtTableImplicitTableBody n)
	{
		startTagR07(n);
		iterate(n.getBody());
		dispatch(getFactory().synEndTag(TBODY));
	}
	*/

	public void visit(WtTableCaption n)
	{
		startTagR04(n);
		iterate(n.getBody());
		dispatch(getFactory().createSyntheticEndTag(n));
	}

	public void visit(WtTableCell n)
	{
		// startTagR08 re-dispatches
		startTagR08(n);
	}

	public void visit(WtTableHeader n)
	{
		// startTagR08 re-dispatches
		startTagR08(n);
	}

	public void visit(WtTableRow n)
	{
		// startTagR08 re-dispatches
		startTagR08(n);
	}

	public void visit(WtTable n)
	{
		// startTagR09 re-dispatches
		startTagR09(n);
	}

	// =========================================================================

	/**
	 * R01: A character token, if the current node is table, tbody, tfoot,
	 * thead, or tr element
	 */
	private void tokenR01(WtNode node)
	{
		tb.resetPendingTableCharTokens();
		tb.setOriginalInsertionMode();
		tb.switchInsertionMode(InsertionMode.IN_TABLE_TEXT);
		dispatch(node);
	}

	/**
	 * R02: A comment token
	 */
	private void tokenR02(WtNode n)
	{
		tb.appendToCurrentNode(n);
	}

	/**
	 * R04: A start tag whose tag name is caption
	 */
	private void startTagR04(WtNode n)
	{
		tb.clearStackBackToTableContext();
		tb.insertMarkerInActiveFormattingElements();
		tb.insertAnHtmlElement(n);
		tb.switchInsertionMode(InsertionMode.IN_CAPTION);
	}

	/**
	 * R05: A start tag whose tag name is colgroup
	 */
	private void startTagR05(WtNode n)
	{
		tb.clearStackBackToTableContext();
		tb.insertAnHtmlElement(n);
		tb.switchInsertionMode(InsertionMode.IN_COLUMN_GROUP);
	}

	/**
	 * R06: A start tag whose tag name is col
	 */
	private void startTagR06(WtNode n)
	{
		dispatch(getFactory().createMissingRepairStartTag(COLGROUP));
		dispatch(n);
	}

	/**
	 * R07: A start tag whose tag name is one of: tbody, tfoot, thead
	 */
	private void startTagR07(WtNode n)
	{
		tb.clearStackBackToTableContext();
		tb.insertAnHtmlElement(n);
		tb.switchInsertionMode(InsertionMode.IN_TABLE_BODY);
	}

	/**
	 * R08: A start tag whose tag name is one of: td, th, tr
	 */
	private void startTagR08(WtNode n)
	{
		WtNode itbody = getFactory().createMissingRepairStartTag(TBODY);
		WtNodeFlags.setImplicit(itbody);
		dispatch(itbody);
		dispatch(n);
	}

	/**
	 * R09: A start tag whose tag name is table
	 */
	private void startTagR09(WtNode n)
	{
		tb.error(n, "12.2.5.4.9 R09");
		dispatch(getFactory().createMissingRepairEndTag(TABLE));
		// Since we have no fragment case, the fake token cannot be ignored
		dispatch(n);
	}

	/**
	 * R10: An end tag whose tag name is table
	 */
	private void endTagR10(WtNode n)
	{
		// We don't have a fragment case
		// -> No scope checking

		addRtDataOfEndTag(
				tb.popFromStackUntilIncluding(TABLE),
				n);

		tb.resetInsertionMode();
	}

	/**
	 * R11: An end tag whose tag name is one of: body, caption, col, colgroup,
	 * html, tbody, td, tfoot, th, thead, tr
	 */
	private void endTagR11(WtNode n)
	{
		tb.error(n, "12.2.5.4.9 R11");
		tb.ignore(n);
	}

	/**
	 * R16: Anything else
	 */
	private void anythingElseR16(WtNode n)
	{
		tb.error(n, "12.2.5.4.9 R16");
		tb.setFosterParentingMode(true);
		tb.processInInsertionMode(InsertionMode.IN_BODY, n);
		tb.setFosterParentingMode(false);
	}

	// =========================================================================

	public static final class TreeBuilderInTableText
			extends
				TreeBuilderModeBase
	{
		public TreeBuilderInTableText(VisitorLogic<WtNode> logic,
				TreeBuilder treeBuilder)
		{
			super(logic, treeBuilder);
		}

		public void visit(WtNode n)
		{
			WtText textNode = getFactory().text(tb.getPendingTableCharTokens());
			if (StringTools.isWhitespace(textNode.getContent()))
			{
				tb.insertText(textNode);
			}
			else
			{
				((TreeBuilderInTable) tb.getModeImpl(InsertionMode.IN_TABLE))
						.anythingElseR16(textNode);
			}

			tb.resetToOriginalInsertionMode();

			dispatch(n);
		}

		public void visit(WtText n)
		{
			tb.appendToPendingTableCharTokens(n.getContent());
		}

		public void visit(WtNewline n)
		{
			dispatch(getFactory().text(n.getContent()));
		}
	}

	// =========================================================================

	public static final class TreeBuilderInCaption
			extends
				TreeBuilderModeBase
	{
		public TreeBuilderInCaption(VisitorLogic<WtNode> logic,
				TreeBuilder treeBuilder)
		{
			super(logic, treeBuilder);
		}

		public void visit(WtXmlStartTag n)
		{
			ElementType nodeType = getNodeType(n);
			if (nodeType == null)
			{
				anythingElse(n);
				return;
			}

			switch (nodeType)
			{
				case CAPTION:
				case COL:
				case COLGROUP:
				case TBODY:
				case TD:
				case TFOOT:
				case TH:
				case THEAD:
				case TR:
					rule02(n);
					break;
				default:
					anythingElse(n);
					break;
			}
		}

		public void visit(WtXmlEndTag n)
		{
			ElementType nodeType = getNodeType(n);
			if (nodeType == null)
			{
				anythingElse(n);
				return;
			}

			switch (nodeType)
			{
				case CAPTION:
				{
					// We don't have a fragment case
					// -> No scope checking

					tb.generateImpliedEndTags();

					if (!isNodeOneOf(tb.getCurrentNode(), CAPTION))
						tb.error(n, "12.2.5.4.11 R01 (2)");

					addRtDataOfEndTag(
							tb.popFromStackUntilIncluding(CAPTION),
							n);
					tb.clearActiveFormattingElementsToLastMarker();
					tb.switchInsertionMode(InsertionMode.IN_TABLE);
					break;
				}
				case TABLE:
					rule02(n);
					break;
				case PAGE: // == HTML, BODY
				case COL:
				case COLGROUP:
				case TBODY:
				case TD:
				case TFOOT:
				case TH:
				case THEAD:
				case TR:
					tb.error(n, "12.2.5.4.11 R03");
					tb.ignore(n);
					break;
				default:
					anythingElse(n);
					break;
			}
		}

		public void visit(WtNode n)
		{
			anythingElse(n);
			// throw new AssertionError();
		}

		private void rule02(WtNode n)
		{
			tb.error(n, "12.2.5.4.11 R02");
			dispatch(getFactory().createMissingRepairEndTag(CAPTION));
			// Since we have no fragment case, the fake token cannot be ignored
			dispatch(n);
		}

		public void visit(WtTableCaption n)
		{
			// rule02 re-dispatches
			rule02(n);
		}

		public void visit(WtTableCell n)
		{
			// rule02 re-dispatches
			rule02(n);
		}

		public void visit(WtTableHeader n)
		{
			// rule02 re-dispatches
			rule02(n);
		}

		public void visit(WtTableRow n)
		{
			// rule02 re-dispatches
			rule02(n);
		}

		private void anythingElse(WtNode n)
		{
			tb.processInInsertionMode(InsertionMode.IN_BODY, n);
		}
	}

	// =========================================================================

	public static final class TreeBuilderInColumnGroup
			extends
				TreeBuilderModeBase
	{
		public TreeBuilderInColumnGroup(VisitorLogic<WtNode> logic,
				TreeBuilder treeBuilder)
		{
			super(logic, treeBuilder);
		}

		public void visit(WtXmlStartTag n)
		{
			ElementType nodeType = getNodeType(n);
			if (nodeType == null)
			{
				anythingElse(n);
			}
			else if (nodeType == COL)
			{
				tb.insertAnHtmlElement(n);
				tb.popFromStack();
			}
			else
			{
				anythingElse(n);
			}
		}

		public void visit(WtXmlEndTag n)
		{
			ElementType nodeType = getNodeType(n);
			if (nodeType == null)
			{
				anythingElse(n);
			}
			else if (nodeType == COLGROUP)
			{
				// We have no fragment case!
				if (getNodeType(tb.popFromStack()) != COLGROUP)
					throw new AssertionError();
				tb.switchInsertionMode(InsertionMode.IN_TABLE);
			}
			else if (nodeType == COL)
			{
				tb.error(n, "12.2.5.4.12 R07");
				tb.ignore(n);
			}
			else
			{
				anythingElse(n);
			}
		}

		public void visit(WtNode n)
		{
			// anythingElse(n);
			throw new AssertionError();
		}

		public void visit(WtText n)
		{
			if (StringTools.isWhitespace(n.getContent()))
			{
				tb.insertText(n);
			}
			else
			{
				anythingElse(n);
			}
		}

		public void visit(WtNewline n)
		{
			dispatch(getFactory().text(n.getContent()));
		}

		public void visit(WtXmlComment n)
		{
			tb.appendToCurrentNode(n);
		}

		private void anythingElse(WtNode n)
		{
			dispatch(getFactory().createMissingRepairEndTag(COLGROUP));
			// Since we have no fragment case, the fake token cannot be ignored
			dispatch(n);
		}
	}

	// =========================================================================

	public static final class TreeBuilderInTableBody
			extends
				TreeBuilderModeBase
	{
		public TreeBuilderInTableBody(VisitorLogic<WtNode> logic,
				TreeBuilder treeBuilder)
		{
			super(logic, treeBuilder);
		}

		public void visit(WtXmlStartTag n)
		{
			ElementType nodeType = getNodeType(n);
			if (nodeType == null)
			{
				anythingElse(n);
				return;
			}

			switch (nodeType)
			{
				case TR:
					rule01(n);
					break;
				case TH:
				case TD:
					rule02(n);
					break;
				case CAPTION:
				case COL:
				case COLGROUP:
				case TBODY:
				case TFOOT:
				case THEAD:
					rule04(n);
					break;
				default:
					anythingElse(n);
					break;
			}
		}

		public void visit(WtXmlEndTag n)
		{
			ElementType nodeType = getNodeType(n);
			if (nodeType == null)
			{
				anythingElse(n);
				return;
			}

			switch (nodeType)
			{
				case TBODY:
				case TFOOT:
				case THEAD:
					if (!tb.isElementTypeInTableScope(nodeType))
					{
						tb.error(n, "12.2.5.4.13 R03");
						tb.ignore(n);
					}
					else
					{
						tb.clearStackBackToTableBodyContext();
						addRtDataOfEndTag(tb.popFromStack(), n);
						tb.switchInsertionMode(InsertionMode.IN_TABLE);
					}
					break;
				case TABLE:
					rule04(n);
					break;
				case PAGE: // == HTML,BODY
				case CAPTION:
				case COL:
				case COLGROUP:
				case TD:
				case TH:
				case TR:
					tb.error(n, "12.2.5.4.13 R05");
					tb.ignore(n);
					break;
				default:
					anythingElse(n);
					break;
			}
		}

		public void visit(WtNode n)
		{
			anythingElse(n);
			// throw new AssertionError();
		}

		public void visit(WtTableRow n)
		{
			rule01(n);
			iterate(n.getBody());
			dispatch(getFactory().createSyntheticEndTag(n));
		}

		public void visit(WtTableHeader n)
		{
			// rule02 re-dispatches
			rule02(n);
		}

		public void visit(WtTableCell n)
		{
			// rule02 re-dispatches
			rule02(n);
		}

		public void visit(WtTableCaption n)
		{
			// rule04 re-dispatches
			rule04(n);
		}

		private void rule01(WtNode n)
		{
			tb.clearStackBackToTableBodyContext();
			tb.insertAnHtmlElement(n);
			tb.switchInsertionMode(InsertionMode.IN_ROW);
		}

		private void rule02(WtNode n)
		{
			/*
			 * This is extra: It's valid in Wikitext for the first row to be
			 * implicit. Therefore we'll look for table to which this cell
			 * belongs and if there's no row yet, this is legal and will become
			 * the implicit first row.
			 */
			WtNode table = tb.getFromStack(TABLE);
			if (table.getNodeType() == WtNode.NT_TABLE
					&& !hasRows((WtTable) table))
			{
				// Pretend we saw the implicit row:
				WtNode tr = getFactory().createMissingRepairStartTag(TR);
				WtNodeFlags.setImplicit(tr);
				dispatch(tr);
				dispatch(n);
			}
			else
			{
				tb.error(n, "12.2.5.4.13 R02");
				dispatch(getFactory().createMissingRepairStartTag(TR));
				dispatch(n);
			}
		}

		/**
		 * Iterate through the elements of the "new" table that we already
		 * processed to see if the table already has a row attached.
		 */
		private boolean hasRows(WtTable table)
		{
			for (WtNode n : table.getBody())
			{
				switch (n.getNodeType())
				{
					case WtNode.NT_TABLE_IMPLICIT_TBODY:
						for (WtNode rows : n)
						{
							if (getNodeType(rows) == TR)
								return true;
						}
						return false;
					case WtNode.NT_TEXT:
						// Whitespace only text can be part of the table.
						// Non-whitespace text should have been hoisted in
						// front of the table already -> no need to check.
						continue;
					case WtNode.NT_TABLE_CAPTION:
						continue;
					case WtNode.NT_XML_ELEMENT:
						if (getNodeType(n) == CAPTION)
							continue;
						else
							; // FALL THROUGH
					default:
						// Any other garbage should have been hoisted in front
						// of the table already.
						throw new AssertionError();
				}
			}
			return false;
		}

		private void rule04(WtNode n)
		{
			// We have no fragment case!

			tb.clearStackBackToTableBodyContext();

			ElementType curNodeType = getNodeType(tb.getCurrentNode());
			switch (curNodeType)
			{
				case TBODY:
				case TFOOT:
				case THEAD:
					dispatch(getFactory().createMissingRepairEndTag(curNodeType));
					break;
				default:
					throw new AssertionError();
			}

			dispatch(n);
		}

		private void anythingElse(WtNode n)
		{
			tb.processInInsertionMode(InsertionMode.IN_TABLE, n);
		}
	}

	// =========================================================================

	public static final class TreeBuilderInRow
			extends
				TreeBuilderModeBase
	{
		public TreeBuilderInRow(VisitorLogic<WtNode> logic,
				TreeBuilder treeBuilder)
		{
			super(logic, treeBuilder);
		}

		public void visit(WtXmlStartTag n)
		{
			ElementType nodeType = getNodeType(n);
			if (nodeType == null)
			{
				anythingElse(n);
				return;
			}

			switch (nodeType)
			{
				case TH:
				case TD:
					rule01(n);
					break;
				case CAPTION:
				case COL:
				case COLGROUP:
				case TBODY:
				case TFOOT:
				case THEAD:
				case TR:
					rule03(n);
					break;
				default:
					anythingElse(n);
					break;
			}
		}

		public void visit(WtXmlEndTag n)
		{
			ElementType nodeType = getNodeType(n);
			if (nodeType == null)
			{
				anythingElse(n);
				return;
			}

			switch (nodeType)
			{
				case TR:
				{
					// We don't have a fragment case!
					tb.clearStackBackToTableRowContext();
					WtNode row = tb.popFromStack();
					if (getNodeType(row) != TR)
						throw new AssertionError();
					addRtDataOfEndTag(row, n);
					tb.switchInsertionMode(InsertionMode.IN_TABLE_BODY);
					break;
				}
				case TABLE:
					rule03(n);
					break;
				case TBODY:
				case TFOOT:
				case THEAD:
					if (tb.isElementTypeInTableScope(nodeType))
					{
						tb.error(n, "12.2.5.4.14 R04");
						tb.ignore(n);
					}
					else
					{
						dispatch(getFactory().createMissingRepairEndTag(TR));
						dispatch(n);
					}
					break;
				case PAGE: // == HTML,BODY
				case CAPTION:
				case COL:
				case COLGROUP:
				case TD:
				case TH:
					tb.error(n, "12.2.5.4.14 R05");
					tb.ignore(n);
					break;
				default:
					anythingElse(n);
					break;
			}
		}

		public void visit(WtNode n)
		{
			anythingElse(n);
			// throw new AssertionError(n.toString());
		}

		public void visit(WtTableCell n)
		{
			rule01(n);
			iterate(n.getBody());
			dispatch(getFactory().createSyntheticEndTag(n));
		}

		public void visit(WtTableHeader n)
		{
			rule01(n);
			iterate(n.getBody());
			dispatch(getFactory().createSyntheticEndTag(n));
		}

		public void visit(WtTableCaption n)
		{
			// rule03 re-dispatches
			rule03(n);
		}

		public void visit(WtTableRow n)
		{
			// rule03 re-dispatches
			rule03(n);
		}

		private void rule01(WtNode n)
		{
			tb.clearStackBackToTableRowContext();
			tb.insertAnHtmlElement(n);
			tb.switchInsertionMode(InsertionMode.IN_CELL);
			tb.insertMarkerInActiveFormattingElements();
		}

		private void rule03(WtNode n)
		{
			dispatch(getFactory().createMissingRepairEndTag(TR));
			// We don't have a fragment case!
			dispatch(n);
		}

		private void anythingElse(WtNode n)
		{
			tb.processInInsertionMode(InsertionMode.IN_TABLE, n);
		}
	}

	// =========================================================================

	public static final class TreeBuilderInCell
			extends
				TreeBuilderModeBase
	{
		public TreeBuilderInCell(VisitorLogic<WtNode> logic,
				TreeBuilder treeBuilder)
		{
			super(logic, treeBuilder);
		}

		public void visit(WtXmlStartTag n)
		{
			ElementType nodeType = getNodeType(n);
			if (nodeType == null)
			{
				anythingElse(n);
				return;
			}

			switch (nodeType)
			{
				case CAPTION:
				case COL:
				case COLGROUP:
				case TBODY:
				case TD:
				case TFOOT:
				case TH:
				case THEAD:
				case TR:
					rule02(n);
					break;
				default:
					anythingElse(n);
					break;
			}
		}

		public void visit(WtXmlEndTag n)
		{
			ElementType nodeType = getNodeType(n);
			if (nodeType == null)
			{
				anythingElse(n);
				return;
			}

			switch (nodeType)
			{
				case TD:
				case TH:
					if (!tb.isElementTypeInTableScope(nodeType))
					{
						tb.error(n, "12.2.5.4.15 R01 (1)");
						tb.ignore(n);
					}
					else
					{
						tb.generateImpliedEndTags();
						if (getNodeType(tb.getCurrentNode()) != nodeType)
							tb.error(n, "12.2.5.4.15 R01 (2)");
						addRtDataOfEndTag(
								tb.popFromStackUntilIncluding(nodeType),
								n);
						tb.clearActiveFormattingElementsToLastMarker();
						tb.switchInsertionMode(InsertionMode.IN_ROW);
					}
					break;
				case PAGE: // == HTML,BODY
				case CAPTION:
				case COL:
				case COLGROUP:
					tb.error(n, "12.2.5.4.15 R03");
					tb.ignore(n);
					break;
				case TABLE:
				case TBODY:
				case TFOOT:
				case THEAD:
				case TR:
					if (!tb.isElementTypeInTableScope(nodeType))
					{
						switch (nodeType)
						{
							case TBODY:
							case TFOOT:
							case THEAD:
								tb.error(n, "12.2.5.4.15 R04");
								tb.ignore(n);
								break;
							default:
								// We don't have a fragment case. Then,
								// according to spec, this can only happen
								// for TBODY, TFOOT and THEAD
								throw new AssertionError(nodeType.toString());
						}
					}
					else
					{
						closeCell();
						dispatch(n);
					}
					break;
				default:
					anythingElse(n);
					break;
			}
		}

		public void visit(WtNode n)
		{
			anythingElse(n);
			// throw new AssertionError(n.toString());
		}

		public void visit(WtTableCaption n)
		{
			// rule02 re-dispatches
			rule02(n);
		}

		public void visit(WtTableCell n)
		{
			// rule02 re-dispatches
			rule02(n);
		}

		public void visit(WtTableHeader n)
		{
			// rule02 re-dispatches
			rule02(n);
		}

		public void visit(WtTableRow n)
		{
			// rule02 re-dispatches
			rule02(n);
		}

		private void rule02(WtNode n)
		{
			// We don't have a fragment case!
			closeCell();
			dispatch(n);
		}

		private void anythingElse(WtNode n)
		{
			tb.processInInsertionMode(InsertionMode.IN_BODY, n);
		}

		/**
		 * This method is only called if a cell has to be closed forcefully.
		 */
		private void closeCell()
		{
			if (tb.isElementTypeInTableScope(TD))
			{
				dispatch(getFactory().createMissingRepairEndTag(TD));
			}
			else
			{
				if (!tb.isElementTypeInTableScope(TH))
					throw new AssertionError();
				dispatch(getFactory().createMissingRepairEndTag(TH));
			}
		}
	}
}
