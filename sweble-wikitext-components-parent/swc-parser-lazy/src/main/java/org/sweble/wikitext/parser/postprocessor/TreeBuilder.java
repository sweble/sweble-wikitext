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

import static org.sweble.wikitext.parser.nodes.WtNode.NT_EXTERNAL_LINK;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_IMAGE_LINK;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_INTERNAL_LINK;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_LCT_VAR_CONV;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_TABLE;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_TABLE_CAPTION;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_TABLE_CELL;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_TABLE_HEADER;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_TABLE_IMPLICIT_TBODY;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_TABLE_ROW;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_ELEMENT;
import static org.sweble.wikitext.parser.postprocessor.ElementType.DD;
import static org.sweble.wikitext.parser.postprocessor.ElementType.DT;
import static org.sweble.wikitext.parser.postprocessor.ElementType.LCT_VAR_CONV;
import static org.sweble.wikitext.parser.postprocessor.ElementType.LI;
import static org.sweble.wikitext.parser.postprocessor.ElementType.P;
import static org.sweble.wikitext.parser.postprocessor.ElementType.PAGE;
import static org.sweble.wikitext.parser.postprocessor.ElementType.TABLE;
import static org.sweble.wikitext.parser.postprocessor.ElementType.TBODY;
import static org.sweble.wikitext.parser.postprocessor.ElementType.TFOOT;
import static org.sweble.wikitext.parser.postprocessor.ElementType.THEAD;
import static org.sweble.wikitext.parser.postprocessor.ElementType.TR;
import static org.sweble.wikitext.parser.postprocessor.ElementType.UNKNOWN;
import static org.sweble.wikitext.parser.postprocessor.StackScope.BUTTON_SCOPE;
import static org.sweble.wikitext.parser.postprocessor.StackScope.GENERAL_SCOPE;
import static org.sweble.wikitext.parser.postprocessor.StackScope.LIST_ITEM_SCOPE;
import static org.sweble.wikitext.parser.postprocessor.StackScope.TABLE_SCOPE;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WtRtData;
import org.sweble.wikitext.parser.comparer.WtComparer;
import org.sweble.wikitext.parser.nodes.WikitextNodeFactory;
import org.sweble.wikitext.parser.nodes.WtBody;
import org.sweble.wikitext.parser.nodes.WtContentNode.WtAbsentContentNode;
import org.sweble.wikitext.parser.nodes.WtExternalLink;
import org.sweble.wikitext.parser.nodes.WtImEndTag;
import org.sweble.wikitext.parser.nodes.WtImStartTag;
import org.sweble.wikitext.parser.nodes.WtImageLink;
import org.sweble.wikitext.parser.nodes.WtImageLink.ImageHorizAlign;
import org.sweble.wikitext.parser.nodes.WtInternalLink;
import org.sweble.wikitext.parser.nodes.WtLctVarConv;
import org.sweble.wikitext.parser.nodes.WtLeafNode;
import org.sweble.wikitext.parser.nodes.WtLinkTitle;
import org.sweble.wikitext.parser.nodes.WtNamedXmlElement;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.nodes.WtTable;
import org.sweble.wikitext.parser.nodes.WtTableCaption;
import org.sweble.wikitext.parser.nodes.WtTableCell;
import org.sweble.wikitext.parser.nodes.WtTableHeader;
import org.sweble.wikitext.parser.nodes.WtTableImplicitTableBody;
import org.sweble.wikitext.parser.nodes.WtTableRow;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtXmlAttribute;
import org.sweble.wikitext.parser.nodes.WtXmlElement;
import org.sweble.wikitext.parser.postprocessor.TreeBuilderInTable.TreeBuilderInCaption;
import org.sweble.wikitext.parser.postprocessor.TreeBuilderInTable.TreeBuilderInCell;
import org.sweble.wikitext.parser.postprocessor.TreeBuilderInTable.TreeBuilderInColumnGroup;
import org.sweble.wikitext.parser.postprocessor.TreeBuilderInTable.TreeBuilderInRow;
import org.sweble.wikitext.parser.postprocessor.TreeBuilderInTable.TreeBuilderInTableBody;
import org.sweble.wikitext.parser.postprocessor.TreeBuilderInTable.TreeBuilderInTableText;
import org.sweble.wikitext.parser.utils.WtRtDataPrinter;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.Warning;
import de.fau.cs.osr.utils.StringTools;
import de.fau.cs.osr.utils.visitor.VisitorInterface;
import de.fau.cs.osr.utils.visitor.VisitorLogic;

/**
 * Some of the comments in this class are taken from the page
 * http://whatwg.org/html, which is copyright 2004-2011 Apple Computer, Inc.,
 * Mozilla Foundation, and Opera Software ASA. However, we are granted a license
 * to use, reproduce and create derivative works of this document.
 * 
 * TODO: Round trip information has to get fixed!
 */
public class TreeBuilder
{
	static final boolean DEBUG = false;

	private static final WtNode MARKER = null;

	private static final WtNode BOOKMARK = new Bookmark();

	private final VisitorLogic<WtNode> logic = new VisitorLogic<WtNode>(null);

	private final AstVisitor<WtNode> inBodyMode = new TreeBuilderInBody(logic, this);

	private final AstVisitor<WtNode> inTableMode = new TreeBuilderInTable(logic, this);

	private final AstVisitor<WtNode> inTableTextMode = new TreeBuilderInTableText(logic, this);

	private final AstVisitor<WtNode> inCaptionMode = new TreeBuilderInCaption(logic, this);

	private final AstVisitor<WtNode> inColumnGroupMode = new TreeBuilderInColumnGroup(logic, this);

	private final AstVisitor<WtNode> inTableBodyMode = new TreeBuilderInTableBody(logic, this);

	private final AstVisitor<WtNode> inRowMode = new TreeBuilderInRow(logic, this);

	private final AstVisitor<WtNode> inCellMode = new TreeBuilderInCell(logic, this);

	private final LinkedList<WtNode> stack = new LinkedList<WtNode>();

	private final LinkedList<WtNode> activeFormattingElements = new LinkedList<WtNode>();

	private final LinkedList<Warning> errors = new LinkedList<Warning>();

	private final ElementFactory factory;

	private final ParserConfig config;

	private WtParsedWikitextPage rootNode = null;

	private WtNode formPointer = null;

	private VisitorInterface<WtNode> originalInsertionMode;

	private String pendingTableCharTokens = null;

	private boolean fosterParentingMode = false;

	private final WikitextNodeFactory nf;

	// =========================================================================

	public static WtParsedWikitextPage process(
			ParserConfig config,
			WtNode ast)
	{
		return new TreeBuilder(config).go(ast);
	}

	// =========================================================================

	public TreeBuilder(ParserConfig config)
	{
		this.config = config;
		this.factory = new ElementFactory(this);
		nf = getConfig().getNodeFactory();
	}

	// =========================================================================

	private int dbgIndent = 0;

	void dbgIn(String format, Object... args)
	{
		System.out.println(StringTools.indent(
				String.format(format, args),
				StringTools.strrep(' ', dbgIndent * 4)));
		++dbgIndent;
	}

	void dbg(String format, Object... args)
	{
		System.out.println(StringTools.indent(
				String.format(format, args),
				StringTools.strrep(' ', dbgIndent * 4)));
	}

	void dbgOut(String format, Object... args)
	{
		--dbgIndent;
		System.out.println(StringTools.indent(
				String.format(format, args),
				StringTools.strrep(' ', dbgIndent * 4)));
	}

	// =========================================================================

	private WtParsedWikitextPage go(WtNode ast)
	{
		if (DEBUG)
			dbgIn("> TreeBuilder.go");

		switchInsertionMode(InsertionMode.IN_BODY);

		logic.go(ast);
		if (getRootNode() == null)
			throw new InternalError("No root node set after processing!");

		if (!errors.isEmpty())
		{
			if (getRootNode().getWarnings().isEmpty())
				getRootNode().setWarnings(errors);
			else
				getRootNode().getWarnings().addAll(errors);
		}

		if (DEBUG)
			dbgOut("< TreeBuilder.go");
		return getRootNode();
	}

	// =========================================================================

	void processInInsertionMode(InsertionMode mode, WtNode n)
	{
		VisitorLogic.dispatchTo(getModeImpl(mode), n);
	}

	void switchInsertionMode(InsertionMode mode)
	{
		if (DEBUG)
			dbg("!! %s", mode);
		logic.setImpl(getModeImpl(mode));
	}

	VisitorInterface<WtNode> getModeImpl(InsertionMode mode)
	{
		switch (mode)
		{
			case IN_BODY:
				return inBodyMode;
			case IN_TABLE:
				return inTableMode;
			case IN_CAPTION:
				return inCaptionMode;
			case IN_CELL:
				return inCellMode;
			case IN_COLUMN_GROUP:
				return inColumnGroupMode;
			case IN_ROW:
				return inRowMode;
			case IN_TABLE_BODY:
				return inTableBodyMode;
			case IN_TABLE_TEXT:
				return inTableTextMode;
			default:
				throw new InternalError();
		}
	}

	void setOriginalInsertionMode()
	{
		this.originalInsertionMode = logic.getImpl();
	}

	void resetToOriginalInsertionMode()
	{
		if (DEBUG)
			dbg("!! %s", this.originalInsertionMode.getClass().getSimpleName());
		logic.setImpl(this.originalInsertionMode);
	}

	/**
	 * 12.2.3.1
	 * 
	 * When the steps below require the UA to reset the insertion mode
	 * appropriately, it means the UA must follow these steps:
	 */
	void resetInsertionMode()
	{
		// 1. Let last be false.
		// 2. Let node be the last node in the stack of open elements.
		for (WtNode node : stack)
		{
			// We have no fragment case:
			//
			// 3. Loop: If node is the first node in the stack of open elements, 
			// then set last to true and set node to the context element. 
			// (fragment case)
			//
			// 4. If node is a select element, then switch the insertion mode to
			// "in select" and abort these steps. 
			// (fragment case)

			switch (getNodeType(node))
			{
				case TD:
				case TH:
					// 5. If node is a td or th element and last is false, then switch the
					// insertion mode to "in cell" and abort these steps.
					switchInsertionMode(InsertionMode.IN_CELL);
					return;

				case TR:
					// 6. If node is a tr element, then switch the insertion mode to "in row"
					// and abort these steps.
					switchInsertionMode(InsertionMode.IN_ROW);
					return;

				case TBODY:
				case THEAD:
				case TFOOT:
					// 7. If node is a tbody, thead, or tfoot element, then switch the insertion
					// mode to "in table body" and abort these steps.
					switchInsertionMode(InsertionMode.IN_TABLE_BODY);
					return;

				case CAPTION:
					// 8. If node is a caption element, then switch the insertion mode to
					// "in caption" and abort these steps.
					switchInsertionMode(InsertionMode.IN_CAPTION);
					return;

					/* We have no Fragment case:
					case COLGROUP:
					// 9. If node is a colgroup element, then switch the insertion mode to
					// "in column group" and abort these steps. (fragment case)
					setInsertionMode(InsertionMode.IN_COLUMN_GROUP);
					return;
					*/

				case TABLE:
					// 10. If node is a table element, then switch the insertion mode to
					// "in table" and abort these steps.
					switchInsertionMode(InsertionMode.IN_TABLE);
					return;

					/* We have no Fragment case:
					case HEAD:
					// 11. 1. If node is a head element, then switch the insertion mode to
					// "in body" ("in body"! not "in head"!) and abort these steps. (fragment
					// case)
					setInsertionMode(InsertionMode.IN_BODY);
					return;
					*/

				case PAGE:
					// 12. If node is a body element, then switch the insertion mode to
					// "in body" and abort these steps.
					switchInsertionMode(InsertionMode.IN_BODY);
					return;

					/* We have no Fragment case:
					// 13. If node is a frameset element, then switch the insertion mode to
					// "in frameset" and abort these steps. (fragment case)
					// 14. If node is an html element, then switch the insertion mode to
					// "before head" Then, abort these steps. (fragment case)
					// 15. If last is true, then switch the insertion mode to "in body" and
					// abort these steps. (fragment case)
					*/

				default:
					break;
			}
		}
	}

	// =========================================================================

	ParserConfig getConfig()
	{
		return config;
	}

	ElementFactory getFactory()
	{
		return factory;
	}

	WtParsedWikitextPage getRootNode()
	{
		return rootNode;
	}

	void setRootNode(WtParsedWikitextPage rootNode)
	{
		this.rootNode = rootNode;
	}

	LinkedList<WtNode> getStack()
	{
		return stack;
	}

	WtNode getFormPointer()

	{
		return formPointer;
	}

	void setFormPointer(WtNode formPointer)
	{
		this.formPointer = formPointer;
	}

	// =========================================================================

	void resetPendingTableCharTokens()
	{
		this.pendingTableCharTokens = "";
	}

	void appendToPendingTableCharTokens(String content)
	{
		this.pendingTableCharTokens += content;
	}

	String getPendingTableCharTokens()
	{
		String text = this.pendingTableCharTokens;
		this.pendingTableCharTokens = null;
		return text;
	}

	// =========================================================================

	WtNodeList getContentOfNode(WtNode node)
	{
		if (node instanceof WtNodeList)
		{
			return (WtNodeList) node;
		}
		else
		{
			switch (node.getNodeType())
			{
				case NT_INTERNAL_LINK:
					return ((WtInternalLink) node).getTitle();

				case NT_EXTERNAL_LINK:
					return ((WtExternalLink) node).getTitle();

				case NT_IMAGE_LINK:
					return ((WtImageLink) node).getTitle();

				case NT_XML_ELEMENT:
					return ((WtXmlElement) node).getBody();

				case NT_TABLE:
					return ((WtTable) node).getBody();

				case NT_TABLE_IMPLICIT_TBODY:
					return ((WtTableImplicitTableBody) node).getBody();

				case NT_TABLE_CAPTION:
					return ((WtTableCaption) node).getBody();

				case NT_TABLE_ROW:
					return ((WtTableRow) node).getBody();

				case NT_TABLE_HEADER:
					return ((WtTableHeader) node).getBody();

				case NT_TABLE_CELL:
					return ((WtTableCell) node).getBody();

				case NT_LCT_VAR_CONV:
					return ((WtLctVarConv) node).getText();

				default:
					throw new InternalError();
			}
		}
	}

	WtNodeList getContentOfNodeForModification(WtNode node)
	{
		WtNodeList content = getContentOfNode(node);
		if (content instanceof WtAbsentContentNode)
		{
			WtNodeList originalContent = content;
			switch (node.getNodeType())
			{
				case NT_INTERNAL_LINK:
					content = nf.linkTitle(nf.list());
					((WtInternalLink) node).setTitle((WtLinkTitle) content);
					break;

				case NT_EXTERNAL_LINK:
					content = nf.linkTitle(nf.list());
					((WtExternalLink) node).setTitle((WtLinkTitle) content);
					break;

				case NT_IMAGE_LINK:
					content = nf.linkTitle(nf.list());
					((WtImageLink) node).setTitle((WtLinkTitle) content);
					break;

				case NT_XML_ELEMENT:
					content = nf.body(nf.list());
					((WtXmlElement) node).setBody((WtBody) content);
					break;

				case NT_TABLE:
					content = nf.body(nf.list());
					((WtTable) node).setBody((WtBody) content);
					break;

				default:
					throw new InternalError();
			}
			content.setRtd(originalContent.getRtd());
		}
		return content;
	}

	static boolean isNodeTypeOneOf(WtNode node, ElementType... types)
	{
		return isTypeOneOf(getNodeType(node), types);
	}

	static ElementType getNodeType(WtNode node)
	{
		ElementType nodeType = ElementType.getType(node);
		/*
		if (nodeType == null)
		{
			if (node instanceof WtNamedXmlElement)
				throw new InternalError("Unknown node type: <" + ((WtNamedXmlElement) node).getName() + ">");
			else
				throw new InternalError("Unknown node type: " + node.getClass().getSimpleName());
		}
		*/
		return nodeType;
	}

	static boolean isTypeOneOf(ElementType nodeType, ElementType... types)
	{
		for (ElementType type : types)
		{
			if (type == nodeType)
				return true;
		}
		return false;
	}

	static boolean isSameFormattingElement(WtNode e0, WtNode e1)
	{
		if (e0 == e1)
			return true;

		ElementType t0 = getNodeType(e0);
		ElementType t1 = getNodeType(e1);
		if (t0 != t1)
			return false;

		if (e0.getNodeType() == WtNode.NT_XML_ELEMENT)
		{
			WtNodeList a0 = ((WtXmlElement) e0).getXmlAttributes();

			if (e1.getNodeType() == WtNode.NT_XML_ELEMENT)
			{
				return isSameAttributes(
						a0, ((WtXmlElement) e1).getXmlAttributes());
			}
			else if (a0 != null && !a0.isEmpty())
				return false;
		}
		else
		{
			if (e1.getNodeType() == WtNode.NT_XML_ELEMENT)
			{
				WtNodeList a1 = ((WtXmlElement) e1).getXmlAttributes();
				if (a1 != null && !a1.isEmpty())
					return false;
			}
		}

		return true;
	}

	static boolean isSameAttributes(WtNodeList a0, WtNodeList a1)
	{
		if (a0 == a1)
			return true;

		Map<String, WtNodeList> m0 = new HashMap<String, WtNodeList>();
		for (WtNode n : a0)
		{
			if (n.getNodeType() != WtNode.NT_XML_ATTRIBUTE)
				continue;

			WtXmlAttribute a = (WtXmlAttribute) n;

			// TODO: This cannot handle unresolved attribute names:
			if (!a.getName().isResolved())
				return false;

			m0.put(a.getName().getAsString(), a.getValue());
		}

		for (WtNode n : a1)
		{
			if (n.getNodeType() != WtNode.NT_XML_ATTRIBUTE)
				continue;

			WtXmlAttribute a = (WtXmlAttribute) n;
			if (!a.getName().isResolved())
				return false;

			WtNodeList v0 = m0.remove(a.getName().getAsString());
			WtNodeList v1 = a.getValue();

			if (v0 == v1)
				return true;

			if (!WtComparer.compareNoThrow(v0, v1, false, false))
				return false;
		}

		return m0.isEmpty();
	}

	// =========================================================================

	void ignore(WtNode node)
	{
		switch (node.getNodeType())
		{
			case WtNode.NT_XML_START_TAG:
			case WtNode.NT_XML_END_TAG:
			case WtNode.NT_XML_EMPTY_TAG:
			case WtNode.NT_IM_START_TAG:
			case WtNode.NT_IM_END_TAG:
				if (!WtNodeFlags.isRepairNode(node) && hasNonEmptyRtd(node))
					appendToCurrentNode(node);
				break;

			default:
				((TreeBuilderModeBase) logic.getImpl())
						.dispatch(getFactory().text(WtRtDataPrinter.print(node)));

				//throw new InternalError();
		}
	}

	private boolean hasNonEmptyRtd(WtNode node)
	{
		WtRtData rtd = node.getRtd();
		if (rtd == null)
			return false;
		for (Object[] glue : rtd.getFields())
		{
			if (glue.length != 0)
				return true;
		}
		return false;
	}

	void error(WtNode node, String message)
	{
		if ((node instanceof WtImStartTag) || (node instanceof WtImEndTag))
			return;

		errors.add(new TreeBuilderWarning(node, message));
	}

	// =========================================================================

	/**
	 * 12.2.5.1
	 * 
	 * When the steps below require the UA to insert an HTML element for a
	 * token, the UA must first create an element for the token in the HTML
	 * namespace, and then append this node to the current node, and push it
	 * onto the stack of open elements so that it is the new current node.
	 * 
	 * We don't create an element (unless we are dealing with only a tag instead
	 * of a scoped native wikitext element). We use the specified element
	 * instead and assume, that it has been properly prepared (i.e. children
	 * have been stripped and replaced with EMPTY_LIST).
	 */
	WtNode insertAnHtmlElement(WtNode sample)
	{
		WtNode newNode = factory.createNewElement(sample);
		appendToCurrentNode(newNode);
		getStack().push(newNode);
		return newNode;
	}

	WtNode insertAnHtmlRepairFormattingElement(WtNode sample)
	{
		WtNode newNode = factory.createRepairFormattingElement(sample);
		appendToCurrentNode(newNode);
		getStack().push(newNode);
		return newNode;
	}

	/**
	 * 12.2.5
	 * 
	 * When the steps below require the UA to insert a character into a node, if
	 * that node has a child immediately before where the character is to be
	 * inserted, and that child is a WtText node, then the character must be
	 * appended to that WtText node; otherwise, a new WtText node whose data is
	 * just that character must be inserted in the appropriate place.
	 */
	void insertText(WtText text)
	{
		WtNode last = pollLastChildOfCurrentNode();
		// We don't want to merge text nodes with attributes, thus loosing/smearing the attributes.
		if (!text.hasAttributes() && last != null && !last.hasAttributes() && last instanceof WtText)
		{
			WtText lastText = (WtText) last;
			lastText.setContent(lastText.getContent() + text.getContent());
		}
		else
		{
			appendToCurrentNode(text);
		}
	}

	// =========================================================================

	WtNode getCurrentNode()
	{
		assert !getStack().isEmpty();
		return getStack().peek();
	}

	void appendToCurrentNode(WtNode e)
	{
		if (fosterParentingMode
				&& getConfig().isFosterParenting()
				&& isCurrentNodeTypeOneOf(TABLE, TBODY, TFOOT, THEAD, TR)
				&& (getConfig().isFosterParentingForTransclusions()
				|| !isTransclusionTypeNode(e)))
		{
			insertInFosterParent(e);
		}
		else
		{
			getContentOfNodeForModification(getCurrentNode()).add(e);
		}
	}

	private boolean isTransclusionTypeNode(WtNode e)
	{
		switch (e.getNodeType())
		{
			case WtNode.NT_TEMPLATE:
			case WtNode.NT_TEMPLATE_PARAMETER:
				return true;
			default:
				return false;
		}
	}

	WtNode pollLastChildOfCurrentNode()
	{
		WtNodeList content = getContentOfNode(getCurrentNode());
		if (content.isEmpty())
			return null;
		return content.get(content.size() - 1);
	}

	boolean isCurrentNodeTypeOneOf(ElementType... nodeTypes)
	{
		return isNodeTypeOneOf(getCurrentNode(), nodeTypes);
	}

	// =========================================================================

	/**
	 * The stack of open elements is said to have an element in a specific scope
	 * consisting of a list of element types list when the following algorithm
	 * terminates in a match state:
	 * 
	 * Initialize node to be the current node (the bottommost node of the
	 * stack).
	 * 
	 * If node is the target node, terminate in a match state.
	 * 
	 * Otherwise, if node is one of the element types in list, terminate in a
	 * failure state.
	 * 
	 * Otherwise, set node to the previous entry in the stack of open elements
	 * and return to step 2. (This will never fail, since the loop will always
	 * terminate in the previous step if the top of the stack — an html element
	 * — is reached.)
	 */
	boolean isElementTypeInSpecificScope(
			StackScope scope,
			ElementType targetType)
	{
		Iterator<WtNode> i = getStack().iterator();
		while (i.hasNext())
		{
			ElementType nodeType = getNodeType(i.next());
			if (nodeType == targetType)
				return true;

			if (scope.isInList(nodeType))
				return false;
		}
		throw new InternalError("This should never happen!");
	}

	boolean isElementTypeInScope(ElementType elementType)
	{
		return isElementTypeInSpecificScope(GENERAL_SCOPE, elementType);
	}

	boolean isElementTypeInListScope(ElementType elementType)
	{
		return isElementTypeInSpecificScope(LIST_ITEM_SCOPE, elementType);
	}

	boolean isElementTypeInButtonScope(ElementType elementType)
	{
		return isElementTypeInSpecificScope(BUTTON_SCOPE, elementType);
	}

	boolean isElementTypeInTableScope(ElementType elementType)
	{
		return isElementTypeInSpecificScope(TABLE_SCOPE, elementType);
	}

	boolean isOneOfElementTypesInSpecificScope(
			StackScope scope,
			ElementType... targetTypes)
	{
		Iterator<WtNode> i = getStack().iterator();
		while (i.hasNext())
		{
			ElementType nodeType = getNodeType(i.next());
			if (isTypeOneOf(nodeType, targetTypes))
				return true;

			if (scope.isInList(nodeType))
				return false;
		}
		throw new InternalError("This should never happen!");
	}

	boolean isOneOfElementTypesInScope(ElementType... targetTypes)
	{
		return isOneOfElementTypesInSpecificScope(GENERAL_SCOPE, targetTypes);
	}

	boolean isNodeInSpecificScope(StackScope scope, WtNode targetNode)
	{
		Iterator<WtNode> i = getStack().iterator();
		while (true)
		{
			WtNode node = i.next();
			if (isSameTag(node, targetNode))
				return true;

			if (scope.isInList(getNodeType(node)))
				return false;
		}
	}

	boolean isNodeRefInSpecificScope(StackScope scope, WtNode targetNode)
	{
		Iterator<WtNode> i = getStack().iterator();
		while (true)
		{
			WtNode node = i.next();
			if (node == targetNode)
				return true;

			if (scope.isInList(getNodeType(node)))
				return false;
		}
	}

	boolean isNodeRefInScope(WtNode targetNode)
	{
		return isNodeRefInSpecificScope(GENERAL_SCOPE, targetNode);
	}

	// =========================================================================

	void removeFromStack(WtNode node)
	{
		Iterator<WtNode> i = getStack().iterator();
		while (i.hasNext())
		{
			if (i.next() == node)
			{
				i.remove();
				return;
			}
		}
		throw new InternalError("Could not remove node from stack!");
	}

	boolean isInStackOfOpenElements(WtNode node)
	{
		for (WtNode e : getStack())
		{
			if (e == node)
				return true;
		}
		return false;
	}

	WtNode getFromStack(ElementType nodeType)
	{
		for (WtNode e : getStack())
		{
			if (getNodeType(e) == nodeType)
				return e;
		}
		return null;
	}

	WtNode popFromStackUntilIncluding(WtNode nodeExample)
	{
		while (!getStack().isEmpty())
		{
			WtNode found = popFromStack();
			if (isSameTag(nodeExample, found))
				return found;
		}
		throw new InternalError("Everything's gone :(");
	}

	WtNode popFromStackUntilIncluding(ElementType nodeType)
	{
		while (!getStack().isEmpty())
		{
			WtNode found = popFromStack();
			if (getNodeType(found) == nodeType)
				return found;
		}
		throw new InternalError("Everything's gone :(");
	}

	WtNode popFromStackUntilIncluding(ElementType... nodeTypes)
	{
		while (!getStack().isEmpty())
		{
			WtNode found = popFromStack();
			if (isTypeOneOf(getNodeType(found), nodeTypes))
				return found;
		}
		throw new InternalError("Everything's gone :(");
	}

	void popFromStackUntilIncludingRef(WtNode node)
	{
		while (popFromStack() != node)
			;
		if (getStack().isEmpty())
			throw new InternalError("Everything's gone :(");
	}

	void popFromStackUntilExcluding(ElementType... nodeTypes)
	{
		while (!isTypeOneOf(getNodeType(getCurrentNode()), nodeTypes))
			popFromStack();
		if (getStack().isEmpty())
			throw new InternalError("Everything's gone :(");
	}

	WtNode popFromStack()
	{
		return getStack().pop();
	}

	void clearStackBackToTableContext()
	{
		popFromStackUntilExcluding(PAGE, /*SECTION_HEADING, SECTION_BODY, */TABLE);
	}

	void clearStackBackToTableBodyContext()
	{
		popFromStackUntilExcluding(PAGE, /*SECTION_HEADING, SECTION_BODY, */TBODY, TFOOT, THEAD);
	}

	void clearStackBackToTableRowContext()
	{
		popFromStackUntilExcluding(PAGE, /*SECTION_HEADING, SECTION_BODY, */TR);
	}

	WtNode getAboveOnStack(WtNode node)
	{
		Iterator<WtNode> i = getStack().iterator();
		while (i.hasNext())
		{
			if (i.next() == node)
			{
				if (i.hasNext())
					return i.next();
			}
		}
		return null;
	}

	void insertOnStackBelow(WtNode marker, WtNode node)
	{
		ListIterator<WtNode> i = getStack().listIterator();
		while (i.hasNext())
		{
			if (i.next() == marker)
			{
				i.previous();
				i.add(node);
				return;
			}
		}
		throw new InternalError("Marker MUST exist in stack!");
	}

	void removeFromParent(WtNode node, WtNode parent)
	{
		WtNodeList content = getContentOfNode(parent);
		ListIterator<WtNode> i = content.listIterator(content.size());
		while (i.hasPrevious())
		{
			WtNode p = i.previous();
			if (p == node)
			{
				i.remove();
				return;
			}
		}
		throw new InternalError("Node given as parent IS NOT parent of other node!");
	}

	// =========================================================================

	/**
	 * 12.2.5.2
	 * 
	 * When the steps below require the UA to generate implied end tags, then,
	 * while the current node is a dd element, a dt element, an li element, an
	 * option element, an optgroup element, a p element, an rp element, or an rt
	 * element, the UA must pop the current node off the stack of open elements.
	 * 
	 * If a step requires the UA to generate implied end tags but lists an
	 * element to exclude from the process, then the UA must perform the above
	 * steps as if that element was not in the above list.
	 */
	void generateImpliedEndTags(ElementType excludedType)
	{
		while (true)
		{
			ElementType nodeType = getNodeType(getCurrentNode());

			// OPTION, OPTGROUP, RP, RT
			if (nodeType == excludedType || !isTypeOneOf(nodeType, DD, DT, LI, P))
				break;

			popFromStack();
		}
	}

	void generateImpliedEndTags()
	{
		generateImpliedEndTags((ElementType) null);
	}

	void generateImpliedEndTags(WtNode node)
	{
		while (true)
		{
			WtNode currentNode = getCurrentNode();
			ElementType nodeType = getNodeType(currentNode);

			// OPTION, OPTGROUP, RP, RT
			if (!isTypeOneOf(nodeType, DD, DT, LI, P) || isSameTag(currentNode, node))
				break;

			popFromStack();
		}
	}

	static boolean isSameTag(WtNode n0, WtNode n1)
	{
		ElementType t0 = getNodeType(n0);
		ElementType t1 = getNodeType(n1);
		if ((t0 == t1) && (t0 != UNKNOWN))
		{
			return true;
		}
		else if (t0 != t1)
		{
			// If only one node is UNKONWN they cannot be the same...
			return false;
		}
		else if (n0 instanceof WtNamedXmlElement)
		{
			String name0 = ((WtNamedXmlElement) n0).getName();
			if (n1 instanceof WtNamedXmlElement)
			{
				String name1 = ((WtNamedXmlElement) n1).getName();
				if (name0.equalsIgnoreCase(name1))
					return true;
			}
		}

		return false;
	}

	// =========================================================================

	void pushActiveFormattingElements(WtNode node)
	{
		int count = 0;
		Iterator<WtNode> i = activeFormattingElements.descendingIterator();
		while (i.hasNext())
		{
			WtNode fe = i.next();
			if (fe == MARKER)
				break;

			if (isSameFormattingElement(fe, node))
				++count;

			if (count == 3)
			{
				i.remove();
				break;
			}
		}
		activeFormattingElements.add(node);
	}

	boolean isInListOfActiveFormattingElements(WtNode node)
	{
		return activeFormattingElements.contains(node);
	}

	WtNode getActiveFormattingElement(ElementType nodeType)
	{
		Iterator<WtNode> i = activeFormattingElements.descendingIterator();
		while (i.hasNext())
		{
			WtNode node = i.next();
			if (node == MARKER)
				return null;

			if (getNodeType(node) == nodeType)
				return node;
		}
		return null;
	}

	void removeFromActiveFormattingElements(WtNode node)
	{
		Iterator<WtNode> i = activeFormattingElements.descendingIterator();
		while (i.hasNext())
		{
			if (i.next() == node)
			{
				i.remove();
				return;
			}
		}
		throw new InternalError("Could not remove formatting element");
	}

	void replaceInListOfActiveFormattingElements(
			WtNode replacee,
			WtNode replacement)
	{
		ListIterator<WtNode> i = activeFormattingElements.listIterator(activeFormattingElements.size());
		while (i.hasPrevious())
		{
			if (i.previous() == replacee)
			{
				i.set(replacement);
				return;
			}
		}
		throw new InternalError("Could not replace formatting element");
	}

	/**
	 * 12.2.3.3
	 * 
	 * When the steps below require the UA to reconstruct the active formatting
	 * elements, the UA must perform the following steps:
	 * 
	 * [[ Find the steps in the code ]]
	 * 
	 * This has the effect of reopening all the formatting elements that were
	 * opened in the current body, cell, or caption (whichever is youngest) that
	 * haven't been explicitly closed.
	 * 
	 * The way this specification is written, the list of active formatting
	 * elements always consists of elements in chronological order with the
	 * least recently added element first and the most recently added element
	 * last (except for while steps 8 to 11 of the above algorithm are being
	 * executed, of course).
	 */
	void reconstructActiveFormattingElements()
	{
		LinkedList<WtNode> list = activeFormattingElements;

		/* 1) If there are no entries in the list of active formatting elements, then
		 * there is nothing to reconstruct; stop this algorithm.
		 */
		if (list.isEmpty())
			return;

		/* 2) If the last (most recently added) entry in the list of active formatting
		 * elements is a marker, or if it is an element that is in the stack of open
		 * elements, then there is nothing to reconstruct; stop this algorithm.
		 */
		WtNode last = list.getLast();
		if (last == MARKER || isInStackOfOpenElements(last))
			return;

		/* 3) Let entry be the last (most recently added) element in the list of active
		 * formatting elements.
		 */
		int entryIndex = list.size() - 1;
		WtNode entry = list.get(entryIndex);

		while (true)
		{
			/* 4) If there are no entries before entry in the list of active formatting
			 * elements, then jump to step 8.
			 */
			if (entryIndex > 0)
			{
				/* 5) Let entry be the entry one earlier than entry in the list of active
				 * formatting elements.
				 */
				entry = list.get(--entryIndex);

				/* 6) If entry is neither a marker nor an element that is also in the stack of
				 * open elements, go to step 4.
				 */
				if (entry != MARKER && !isInStackOfOpenElements(entry))
					continue;

				/* 7) Let entry be the element one later than entry in the list of active
				 * formatting elements.
				 */
				entry = list.get(++entryIndex);
				break;
			}

			break;
		}

		while (true)
		{
			/* 8) Create an element for the token for which the element entry was created,
			 * to obtain new element.
			 * 
			 * 9) Append new element to the current node and push it onto the stack of open
			 * elements so that it is the new current node.
			 */
			WtNode newNode = insertAnHtmlRepairFormattingElement(entry);

			/* 10) Replace the entry for entry in the list with an entry for new element.
			 */
			list.set(entryIndex, newNode);

			/* 11) If the entry for new element in the list of active formatting elements is
			 * not the last entry in the list, return to step 7.
			 */
			if (entryIndex == list.size() - 1)
				break;
			entry = list.get(++entryIndex);
		}
	}

	void insertMarkerInActiveFormattingElements()
	{
		this.activeFormattingElements.add(MARKER);

		// LctVarConv tags must be effective over marker boundaries in order
		// to "leak" into tables

		LinkedList<WtNode> list = activeFormattingElements;
		ListIterator<WtNode> iter = list.listIterator(list.size());

		// Skip the just inserted marker
		iter.previous();

		while (iter.hasPrevious())
		{
			WtNode e = iter.previous();
			// We only look back to the last marker
			if (e == MARKER)
				break;
			if (getNodeType(e) == LCT_VAR_CONV)
			{
				// Copy the LctVarConv tag after the marker
				this.activeFormattingElements.add(e);
				break;
			}
		}
	}

	void clearActiveFormattingElementsToLastMarker()
	{
		Iterator<WtNode> i = this.activeFormattingElements.descendingIterator();
		while (i.hasNext())
		{
			WtNode fe = i.next();
			i.remove();
			if (fe == MARKER)
				break;
		}
	}

	void placeBookmarkAfter(WtNode node)
	{
		ListIterator<WtNode> i = this.activeFormattingElements.listIterator(
				this.activeFormattingElements.size());

		while (i.hasPrevious())
		{
			WtNode fe = i.previous();
			if (fe == node)
			{
				i.next();
				i.add(BOOKMARK);
				return;
			}
		}
		throw new InternalError("This method must only be called if there definitily is a bookmark!");
	}

	void moveBookmarkAfter(WtNode node)
	{
		ListIterator<WtNode> i = this.activeFormattingElements.listIterator(
				this.activeFormattingElements.size());

		while (i.hasPrevious())
		{
			WtNode fe = i.previous();
			if (fe == node)
			{
				i.next();
				i.add(BOOKMARK);
				i.previous();
				break;
			}
		}
		while (i.hasPrevious())
		{
			WtNode fe = i.previous();
			if (fe == BOOKMARK)
			{
				i.remove();
				return;
			}
		}
		throw new InternalError("This method must only be called if there definitily is a bookmark!");
	}

	public void replaceBookmarkWithAndRemove(WtNode replacement, WtNode remove)
	{
		ListIterator<WtNode> i = this.activeFormattingElements.listIterator(
				this.activeFormattingElements.size());

		while (i.hasPrevious())
		{
			WtNode fe = i.previous();
			if (fe == BOOKMARK)
			{
				i.remove();
				i.add(replacement);
				break;
			}
		}
		while (i.hasPrevious())
		{
			WtNode fe = i.previous();
			if (fe == remove)
			{
				i.remove();
				return;
			}
		}
		throw new InternalError("This method must only be called if there definitily is a bookmark!");
	}

	// =========================================================================

	/**
	 * 12.2.5.3
	 * 
	 * Foster parenting happens when content is misnested in tables.
	 * 
	 * When a node node is to be foster parented, the node node must be inserted
	 * into the foster parent element.
	 * 
	 * The foster parent element is the parent element of the last table element
	 * in the stack of open elements, if there is a table element and it has
	 * such a parent element.
	 * 
	 * It might have no parent or some other kind parent if a script manipulated
	 * the DOM after the element was inserted by the parser.
	 * 
	 * If there is no table element in the stack of open elements (fragment
	 * case), then the foster parent element is the first element in the stack
	 * of open elements (the html element). Otherwise, if there is a table
	 * element in the stack of open elements, but the last table element in the
	 * stack of open elements has no parent, or its parent node is not an
	 * element, then the foster parent element is the element before the last
	 * table element in the stack of open elements.
	 * 
	 * If the foster parent element is the parent element of the last table
	 * element in the stack of open elements, then node must be inserted into
	 * the foster parent element, immediately before the last table element in
	 * the stack of open elements; otherwise, node must be appended to the
	 * foster parent element.
	 */
	void insertInFosterParent(WtNode node)
	{
		WtNode lastTable = getFromStack(TABLE);
		if (lastTable != null)
		{
			// I believe it is not possible (only in our case, not in HTML in 
			// general) for the last table NOT to have a parent. Therefore, we
			// can skip the special treatment of tables without or with the 
			// wrong kind of parent.
			WtNode fosterParent = getAboveOnStack(lastTable);
			WtNodeList content = getContentOfNodeForModification(fosterParent);
			int i = content.indexOf(lastTable);
			content.add(i, node);
		}
		else
		{
			getContentOfNodeForModification(getStack().getLast()).add(node);
		}
	}

	void setFosterParentingMode(boolean fosterParentingMode)
	{
		this.fosterParentingMode = fosterParentingMode;
	}

	// =========================================================================

	static boolean isInlineImage(WtImageLink n)
	{
		switch (n.getFormat())
		{
			case FRAME:
			case THUMBNAIL:
				return false;

			default:
				return (n.getHAlign() == ImageHorizAlign.UNSPECIFIED);
		}
	}

	// =========================================================================

	private static final class Bookmark
			extends
				WtLeafNode
	{
		private static final long serialVersionUID = 1L;
	}
}
