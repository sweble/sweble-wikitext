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
import static org.sweble.wikitext.parser.nodes.WtNode.NT_IM_END_TAG;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_IM_START_TAG;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_INTERNAL_LINK;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_URL;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_ELEMENT;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_EMPTY_TAG;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_END_TAG;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_START_TAG;
import static org.sweble.wikitext.parser.postprocessor.ElementType.*;
import static org.sweble.wikitext.parser.postprocessor.TreeBuilder.*;

import org.sweble.wikitext.parser.WtRtData;
import org.sweble.wikitext.parser.nodes.WikitextNodeFactory;
import org.sweble.wikitext.parser.nodes.WtBody;
import org.sweble.wikitext.parser.nodes.WtExternalLink;
import org.sweble.wikitext.parser.nodes.WtHeading;
import org.sweble.wikitext.parser.nodes.WtImageLink;
import org.sweble.wikitext.parser.nodes.WtInternalLink;
import org.sweble.wikitext.parser.nodes.WtLinkTitle;
import org.sweble.wikitext.parser.nodes.WtNamedXmlElement;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.nodes.WtSection;
import org.sweble.wikitext.parser.nodes.WtTable;
import org.sweble.wikitext.parser.nodes.WtTableCaption;
import org.sweble.wikitext.parser.nodes.WtTableCell;
import org.sweble.wikitext.parser.nodes.WtTableHeader;
import org.sweble.wikitext.parser.nodes.WtTableRow;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtUrl;
import org.sweble.wikitext.parser.nodes.WtXmlElement;
import org.sweble.wikitext.parser.nodes.WtXmlEmptyTag;
import org.sweble.wikitext.parser.nodes.WtXmlEndTag;
import org.sweble.wikitext.parser.nodes.WtXmlStartTag;

import de.fau.cs.osr.ptk.common.ast.RtData;

// TODO: Remove all that commented code
public class ElementFactory {

	private final TreeBuilder tb;

	private final WikitextNodeFactory nf;

	// =========================================================================

	public ElementFactory(TreeBuilder treeBuilder) {
		this.tb = treeBuilder;
		this.nf = treeBuilder.getConfig().getNodeFactory();
	}

	// =========================================================================

	public WtText text(String content) {
		return nf.text(content);
	}

	// TODO: Remove me?
	private WtNodeList list() {
		return nf.list();
	}

	// =========================================================================

	public WtNode create(WtNode node, boolean synthetic) {
		ElementType nodeType = getNodeType(node);

		switch (node.getNodeType()) {
		case NT_XML_START_TAG:
			switch (nodeType) {
			case TR:
				if (node.getBooleanAttribute("implicit"))
					return createImplicitTableRow();
				break;

			case TBODY:
				if (node.getBooleanAttribute("implicit"))
					return createImplicitTableBody();
				break;
			}
		case NT_XML_EMPTY_TAG:
		case NT_XML_ELEMENT:
			return create(nodeType, (WtNamedXmlElement) node, synthetic);

		case NT_XML_END_TAG: {
			if (getNodeType(node) != BR)
				throw new InternalError();

			WtXmlElement newNode = nf.elem(((WtXmlEndTag) node).getName(),
					nf.emptyAttrs());

			copyNodeAttributes(node, newNode);

			return newNode;
		}

		case NT_IM_START_TAG:
		case NT_IM_END_TAG:
		default:
			return create(nodeType, node, synthetic);
		}
	}

	private WtNode create(ElementType elemType, WtNamedXmlElement node,
			boolean synthetic) {
		WtXmlElement newNode = nf.elem(node.getName(), nf.emptyAttrs());

		int nodeType = node.getNodeType();
		switch (nodeType) {
		case NT_XML_START_TAG:
			newNode.setXmlAttributes(((WtXmlStartTag) node).getXmlAttributes());
			if (!synthetic && node.getRtd() != null) {
				WtRtData rtd = node.getRtd();
				newNode.setRtd(rtd.getField(0), RtData.SEP, rtd.getField(1),
						RtData.SEP);
			}
			break;

		case NT_XML_EMPTY_TAG:
			newNode.setXmlAttributes(((WtXmlEmptyTag) node).getXmlAttributes());
			if (!synthetic && node.getRtd() != null) {
				WtRtData rtd = node.getRtd();
				newNode.setRtd(rtd.getField(0), RtData.SEP, rtd.getField(1),
						RtData.SEP);
			}
			break;

		case NT_XML_ELEMENT:
			newNode.setXmlAttributes(((WtXmlElement) node).getXmlAttributes());
			if (!synthetic)
				newNode.setRtd(node.getRtd());
			break;

		}

		switch (elemType) {
		case AREA:
		case BR:
		case IMG:
		case HR:
		case COL:
			if (nodeType != NT_XML_EMPTY_TAG)
				tb.error((WtNode) node,
						"12.2.4 Element should be an empty tag!");
			break;

		default:
			newNode.setBody(nf.body(nf.list()));
			if ((nodeType == NT_XML_EMPTY_TAG)
					|| (nodeType == NT_XML_ELEMENT && !((WtXmlElement) node)
							.hasBody())) {
				tb.error((WtNode) node,
						"12.2.4 Element should not be an empty tag!");
			}
		}

		if (newNode.getRtd() == null) {
			for (WtNode attr : newNode.getXmlAttributes())
				attr.removeAttribute("RTD");
		}

		return newNode;
	}

	private WtNode create(ElementType type, WtNode node, boolean synthetic)
			throws InternalError {
		WtNode newNode;
		switch (type) {
		case PAGE: {
			WtParsedWikitextPage n = (WtParsedWikitextPage) node;
			WtParsedWikitextPage newNode2 = nf.parsedPage(nf.list());
			newNode2.setEntityMap(n.getEntityMap());
			newNode2.setWarnings(n.getWarnings());
			newNode = newNode2;
			break;
		}

		case DL:
			newNode = nf.dl(nf.list());
			break;

		case OL:
			newNode = nf.ol(nf.list());
			break;

		case UL:
			newNode = nf.ul(nf.list());
			break;

		case LI:
			newNode = nf.li(nf.list());
			break;

		case DD:
			newNode = nf.dd(nf.list());
			break;

		case DT:
			newNode = nf.dt(nf.list());
			break;

		case P:
			newNode = nf.p(nf.list());
			break;

		case A:
			switch (node.getNodeType()) {
			case NT_INTERNAL_LINK: {
				WtInternalLink n = (WtInternalLink) node;
				newNode = nf.intLink(n.getPrefix(), n.getTarget(),
						n.getPostfix());
				if (n.hasTitle()) {
					WtLinkTitle title = nf.linkTitle(nf.list());
					title.setRtd(n.getTitle().getRtd());
					((WtInternalLink) newNode).setTitle(title);
				}
				break;
			}
			case NT_EXTERNAL_LINK: {
				WtExternalLink n = (WtExternalLink) node;
				newNode = nf.extLink(n.getTarget());
				if (n.hasTitle()) {
					WtLinkTitle title = nf.linkTitle(nf.list());
					title.setRtd(n.getTitle().getRtd());
					((WtExternalLink) newNode).setTitle(title);
				}
				break;
			}
			case NT_URL: {
				WtUrl n = (WtUrl) node;
				newNode = nf.url(n.getProtocol(), n.getPath());
				break;
			}
			default:
				throw new InternalError();
			}
			break;

		case B:
			newNode = nf.b(nf.list());
			break;

		case I:
			newNode = nf.i(nf.list());
			break;

		case SEMIPRE:
			newNode = nf.semiPre(nf.list());
			break;

		case HR:
			newNode = nf.hr();
			break;

		case SECTION: {
			WtSection n = (WtSection) node;
			WtHeading heading = nf.heading(nf.list());
			heading.setRtd(n.getHeading().getRtd());
			WtBody body = nf.noBody();
			if (n.hasBody()) {
				body = nf.body(nf.list());
				body.setRtd(n.getBody().getRtd());
			}
			newNode = nf.section(n.getLevel(), heading, body);
			break;
		}

		case SECTION_HEADING:
			newNode = nf.heading(nf.list());
			break;

		case SECTION_BODY:
			newNode = nf.body(nf.list());
			break;

		case TABLE: {
			WtTable n = (WtTable) node;
			WtBody body = nf.noBody();
			if (n.hasBody()) {
				body = nf.body(nf.list());
				body.setRtd(n.getBody().getRtd());
			}
			newNode = nf.table(n.getXmlAttributes(), body);
			break;
		}

		case TBODY:
			newNode = nf.itbody(nf.body(nf.list()));
			break;

		case CAPTION:
			newNode = nf.caption(((WtTableCaption) node).getXmlAttributes(),
					nf.body(nf.list()));
			break;

		case TR:
			newNode = nf.tr(((WtTableRow) node).getXmlAttributes(),
					nf.body(nf.list()));
			break;

		case TD:
			newNode = nf.td(((WtTableCell) node).getXmlAttributes(),
					nf.body(nf.list()));
			break;

		case TH:
			newNode = nf.th(((WtTableHeader) node).getXmlAttributes(),
					nf.body(nf.list()));
			break;

		case FRAMED_IMG:
		case INLINE_IMG: {
			WtImageLink n = (WtImageLink) node;
			WtLinkTitle title = nf.noLinkTitle();
			if (n.hasTitle()) {
				title = nf.linkTitle(nf.list());
				title.setRtd(n.getTitle().getRtd());
			}
			newNode = nf.img(n.getTarget(), n.getOptions(), title);
			break;
		}

		default:
			throw new InternalError(
					"Don't know how to generate a node of the specified node type: "
							+ type);
		}

		newNode.setRtd(node.getRtd());
		copyNodeAttributes(node, newNode);
		return newNode;
	}

	// =========================================================================

	public WtNode synCreate(WtNode node) {
		return setSynFlag(create(node, true));
	}

	public WtNode synStartTag(ElementType type) {
		String tagName = type.getXmlTagName();
		if (tagName == null)
			throw new InternalError(
					"Don't know how to generate start tag for given type: "
							+ type);

		return setSynFlag(nf.startTag(tagName, nf.emptyAttrs()));
	}

	public WtNode synEndTag(WtNode node) {
		return synEndTag(getType(node));
	}

	public WtNode synEndTag(ElementType type) {
		String tagName = type.getXmlTagName();
		if (tagName == null)
			throw new InternalError(
					"Don't know how to generate end tag for given type: "
							+ type);

		return setSynFlag(nf.endTag(tagName));
	}

	public WtNode synEmptyTag(ElementType type) {
		String tagName = type.getXmlTagName();
		if (tagName == null)
			throw new InternalError(
					"Don't know how to generate empty tag for given type: "
							+ type);

		return setSynFlag(nf.startTag(tagName, nf.emptyAttrs()));
	}

	private WtNode setSynFlag(WtNode node) {
		node.setAttribute("synthetic", true);
		return node;
	}

	// =========================================================================

	public WtNode createImplicitTableRow() {
		WtTableRow tr = nf.tr(nf.emptyAttrs(), nf.body(nf.list()));
		tr.setImplicit(true);
		return tr;
	}

	public WtNode createImplicitTableBody() {
		return nf.itbody(nf.body(nf.list()));
	}

	// =========================================================================

	private static void copyNodeAttributes(WtNode from, WtNode to) {
		if (from.hasAttributes())
			to.setAttributes(from.getAttributes());
	}
}
