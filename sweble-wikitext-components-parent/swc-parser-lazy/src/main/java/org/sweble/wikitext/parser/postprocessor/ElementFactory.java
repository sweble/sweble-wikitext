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

import static org.sweble.wikitext.parser.nodes.WtNode.NT_IM_END_TAG;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_IM_START_TAG;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_ELEMENT;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_EMPTY_TAG;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_END_TAG;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_START_TAG;
import static org.sweble.wikitext.parser.postprocessor.ElementType.getType;
import static org.sweble.wikitext.parser.postprocessor.TreeBuilder.getNodeType;

import org.sweble.wikitext.parser.WtRtData;
import org.sweble.wikitext.parser.nodes.WikitextNodeFactory;
import org.sweble.wikitext.parser.nodes.WtBody;
import org.sweble.wikitext.parser.nodes.WtContentNode;
import org.sweble.wikitext.parser.nodes.WtEmptyImmutableNode;
import org.sweble.wikitext.parser.nodes.WtExternalLink;
import org.sweble.wikitext.parser.nodes.WtHeading;
import org.sweble.wikitext.parser.nodes.WtImageLink;
import org.sweble.wikitext.parser.nodes.WtIntermediate;
import org.sweble.wikitext.parser.nodes.WtInternalLink;
import org.sweble.wikitext.parser.nodes.WtLctFlags;
import org.sweble.wikitext.parser.nodes.WtLctVarConv;
import org.sweble.wikitext.parser.nodes.WtLinkOptions;
import org.sweble.wikitext.parser.nodes.WtLinkTitle;
import org.sweble.wikitext.parser.nodes.WtNamedXmlElement;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtPageName;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.nodes.WtSection;
import org.sweble.wikitext.parser.nodes.WtTable;
import org.sweble.wikitext.parser.nodes.WtTableCaption;
import org.sweble.wikitext.parser.nodes.WtTableCell;
import org.sweble.wikitext.parser.nodes.WtTableHeader;
import org.sweble.wikitext.parser.nodes.WtTableRow;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtUrl;
import org.sweble.wikitext.parser.nodes.WtXmlAttributes;
import org.sweble.wikitext.parser.nodes.WtXmlElement;
import org.sweble.wikitext.parser.nodes.WtXmlEmptyTag;
import org.sweble.wikitext.parser.nodes.WtXmlEndTag;
import org.sweble.wikitext.parser.nodes.WtXmlStartTag;

import de.fau.cs.osr.ptk.common.ast.RtData;

public class ElementFactory
{
	private final TreeBuilder tb;

	private final WikitextNodeFactory nf;

	// =========================================================================

	public ElementFactory(TreeBuilder treeBuilder)
	{
		this.tb = treeBuilder;
		this.nf = treeBuilder.getConfig().getNodeFactory();
	}

	// =========================================================================

	public WtText text(String content)
	{
		return nf.text(content);
	}

	// =========================================================================

	/**
	 * This method is always called if we first have to open a container node in
	 * order to process the node we are currently dealing with. For example we
	 * are dealing with a &lt;td> tag but haven't seen a &lt;tbody> yet. In this
	 * case this method is called to generate a synthetic &lt;tbody> element.
	 */
	public WtNode createMissingRepairStartTag(ElementType type)
	{
		return WtNodeFlags.setRepairNode(createStartTag(type));
	}

	/**
	 * This method is called when a native wiki markup element (for example an
	 * internal link) which really only exists as one element is trying to
	 * emulate an "end tag event". These tags should not be repair tags since
	 * they stem from a "real" element with valid RTD information attached to
	 * it.
	 */
	public WtNode createSyntheticEndTag(WtNode node)
	{
		return createSyntheticEndTag(node, getType(node));
	}

	/**
	 * Same as createSyntheticEndTag(WtNode). It's only used by image links
	 * which can be closed as framed image or inline image. However, which one
	 * it is cannot easily be determined from the WtImageLink node type alone.
	 */
	public WtNode createSyntheticEndTag(WtNode node, ElementType type)
	{
		WtXmlEndTag endTag = createEndTag(type);

		/**
		 * Copy only the last field. We assume that every element is built like
		 * "RTD (CHILD RTD)* (BODY RTD)?". By only copying the last RTD field
		 * (only if there's at least two RTD fields) we hope to always only copy
		 * the RTD portion that belongs to the "end tag".
		 */
		WtRtData rtd = node.getRtd();
		if ((rtd != null) && (rtd.size() >= 2))
			endTag.setRtd(rtd.getField(rtd.size() - 1));

		return endTag;
	}

	/**
	 * We find ourselves forced to prematurely close the element that's
	 * currently on top of the scope stack. This might happen, for example, if
	 * we're inside a table cell but encounter a table row start tag. In this
	 * case we'll have to close the cell before we've actually encountered a
	 * cell end tag.
	 */
	public WtNode createMissingRepairEndTag(ElementType type)
	{
		return WtNodeFlags.setRepairNode(createEndTag(type));
	}

	// =========================================================================

	private WtXmlStartTag createStartTag(ElementType type)
	{
		return nf.startTag(getTagNameOrFail(type), nf.emptyAttrs());
	}

	private WtXmlEndTag createEndTag(final ElementType type)
	{
		return nf.endTag(getTagNameOrFail(type));
	}

	// =========================================================================

	/**
	 * Called when a formatting element is reopened after it had to be closed
	 * forcibly previously.
	 */
	public WtNode createRepairFormattingElement(WtNode template)
	{
		return create(template, true);
	}

	/**
	 * Called to create a &lt;X> element when what we really encountered was a
	 * isolated &lt;/X> end tag.
	 */
	public WtNode createElementRepair(WtNode template)
	{
		WtXmlEndTag tag = (WtXmlEndTag) template;

		WtXmlElement newElement = createEmptyElement(tag.getName());
		copyNodeAttributes(tag, newElement);

		/**
		 * Don't set repair flag. We have original RTD information on such a
		 * &lt;br> element.
		 */
		//setRepairNode(element);

		WtRtData rtd = tag.getRtd();
		if (rtd != null)
			newElement.setRtd(rtd.getField(0));

		return newElement;
	}

	/**
	 * Only used by adoption agency algorithm to create the adopting node.
	 * 
	 * I'm not sure if the adopter is a repair element. I think it is, but we
	 * probably have to render it fully, otherwise things could get messy. After
	 * all the adoption agency algorithm completely rearranges nodes thus
	 * messing up faulty wikitext.
	 */
	public WtNode createAdopterElement(WtNode template)
	{
		WtNode newElement = createNewElement(template);

		/**
		 * Don't treat it as repair element. Those won't be rendered which might
		 * cause havoc in the case of adopter nodes (I'm not sure about
		 * this...).
		 */
		//setRepairNode(element);

		return newElement;
	}

	/**
	 * Called when a new element is put on the scope stack.
	 */
	public WtNode createNewElement(WtNode template)
	{
		return create(template, false);
	}

	// =========================================================================

	private WtNode create(WtNode template, boolean suppressRtd)
	{
		ElementType elementType = getNodeType(template);

		switch (template.getNodeType())
		{
			case NT_IM_START_TAG:
				return createFromIntermediate(elementType, (WtIntermediate) template, suppressRtd);

			case NT_XML_START_TAG:
				switch (elementType)
				{
					case TR:
						if (WtNodeFlags.isImplicit(template))
							return createImplicitTableRow();
						break;

					case TBODY:
						if (WtNodeFlags.isImplicit(template))
							return createImplicitTableBody();
						break;

					default:
						break;
				}
				// FALL THROUGH

			case NT_XML_EMPTY_TAG:
			case NT_XML_ELEMENT:
				return createFromTag(elementType, (WtNamedXmlElement) template, suppressRtd);

			case NT_IM_END_TAG:
			case NT_XML_END_TAG:
				throw new AssertionError();

			default:
				return createFromNative(elementType, template, suppressRtd);
		}
	}

	// =========================================================================

	private WtNode createFromIntermediate(
			ElementType elementType,
			WtIntermediate template,
			boolean suppressRtd)
	{
		WtNode newElement;

		/**
		 * Unlike other nodes, intermediate nodes (@i and @b) can already be
		 * marked as repair nodes by the TicksAnalyzer.
		 */
		//suppressRtd |= WtNodeFlags.isRepairNode(template);

		switch (getNodeType(template))
		{
			case B:
				newElement = nf.b(nf.list());
				copyIntermediateRtd(template, suppressRtd, newElement);
				break;

			case I:
				newElement = nf.i(nf.list());
				copyIntermediateRtd(template, suppressRtd, newElement);
				break;

			case P:
				newElement = nf.p(nf.list());
				break;

			default:
				throw new AssertionError("Don't know how to create intermediate element: " + elementType);
		}

		copyNodeAttributes(template, newElement);
		return newElement;
	}

	private WtNode createFromTag(
			ElementType elementType,
			WtNamedXmlElement template,
			boolean suppressRtd)
	{
		WtXmlElement newElement = createEmptyElement(template.getName());
		WtRtData rtd = template.getRtd();
		WtXmlAttributes attribs;

		int nodeType = template.getNodeType();
		switch (nodeType)
		{
			case NT_XML_START_TAG:
				attribs = ((WtXmlStartTag) template).getXmlAttributes();
				copyStartTagRtd(suppressRtd, newElement, rtd);
				break;

			case NT_XML_EMPTY_TAG:
				attribs = ((WtXmlEmptyTag) template).getXmlAttributes();
				copyStartTagRtd(suppressRtd, newElement, rtd);
				break;

			case NT_XML_ELEMENT:
				attribs = ((WtXmlElement) template).getXmlAttributes();
				copyElementRtd(suppressRtd, newElement, rtd);
				break;

			default:
				throw new AssertionError();
		}

		if (suppressRtd)
			attribs = suppressRtdOnChildren(attribs);

		newElement.setXmlAttributes(attribs);

		setElementBody(elementType, nodeType, template, newElement);

		copyNodeAttributes(template, newElement);
		return newElement;
	}

	// =========================================================================

	private WtXmlElement createEmptyElement(String name)
	{
		return nf.elem(name, nf.emptyAttrs());
	}

	private void copyIntermediateRtd(
			WtIntermediate template,
			boolean suppressRtd,
			WtNode newElement)
	{
		if (!suppressRtd)
			copyAllExceptLastRtd(template, newElement, false);
		//newElement.setRtd(template.getRtd());
	}

	private void copyElementRtd(
			boolean suppressRtd,
			WtXmlElement newElement,
			WtRtData rtd)
	{
		copyStartTagRtd(suppressRtd, newElement, rtd);
		/*
		if ((!suppressRtd && (rtd != null)))
			newElement.setRtd(rtd);
		*/
	}

	private void copyStartTagRtd(
			boolean suppressRtd,
			WtXmlElement newElement,
			WtRtData rtd)
	{
		if ((!suppressRtd && (rtd != null)))
			newElement.setRtd(
					rtd.getField(0),
					RtData.SEP,
					rtd.getField(1),
					RtData.SEP);
	}

	private void setElementBody(
			ElementType elementType,
			int nodeType,
			WtNamedXmlElement template,
			WtXmlElement element)
	{
		switch (elementType)
		{
			case AREA:
			case BR:
			case IMG:
			case HR:
			case COL:
				if (nodeType != NT_XML_EMPTY_TAG)
					tb.error((WtNode) template, "12.2.4 Element should be an empty tag!");

				// Doesn't need body
				break;

			default:
				if ((nodeType == NT_XML_EMPTY_TAG)
						|| (nodeType == NT_XML_ELEMENT && !((WtXmlElement) template).hasBody()))
					tb.error((WtNode) template, "12.2.4 Element should not be an empty tag!");

				if (nodeType != NT_XML_EMPTY_TAG)
					element.setBody(createEmptyBody());

				break;
		}
	}

	// =========================================================================

	private WtNode createFromNative(
			ElementType elementType,
			WtNode template,
			boolean suppressRtd)
	{
		WtNode newElement;
		boolean copyAllRtd = false;

		switch (elementType)
		{
			case PAGE:
				newElement = createPage((WtParsedWikitextPage) template);
				break;

			case DL:
				newElement = nf.dl(nf.list());
				break;

			case OL:
				newElement = nf.ol(nf.list());
				break;

			case UL:
				newElement = nf.ul(nf.list());
				break;

			case LI:
				newElement = nf.li(nf.list());
				break;

			case DD:
				newElement = nf.dd(nf.list());
				break;

			case DT:
				newElement = nf.dt(nf.list());
				break;

			case P:
				newElement = nf.p(nf.list());
				break;

			case INT_LINK:
				newElement = createIntLink((WtInternalLink) template, suppressRtd);
				break;

			case EXT_LINK:
				newElement = createExtLink((WtExternalLink) template, suppressRtd);
				break;

			case URL:
				newElement = createUrl((WtUrl) template);
				break;

			case B:
				newElement = nf.b(nf.list());
				break;

			case I:
				newElement = nf.i(nf.list());
				break;

			case SEMIPRE:
				newElement = nf.semiPre(nf.list());
				break;

			case HR:
				newElement = nf.hr();
				break;

			case SECTION:
				newElement = createSection((WtSection) template, suppressRtd);
				break;

			case SECTION_HEADING:
				newElement = nf.heading(nf.list());
				/**
				 * Section headings cannot be interrupted and therefore cannot
				 * "loose" their closing RTD. They also do not generate
				 * synthetic end tags and would not get their closing RTD
				 * otherwise.
				 */
				copyAllRtd = true;
				break;

			case SECTION_BODY:
				newElement = createEmptyBody();
				break;

			case TABLE:
				newElement = createTable((WtTable) template, suppressRtd);
				break;

			case TBODY:
				newElement = nf.itbody(createEmptyBody());
				break;

			case CAPTION:
				newElement = createCaption((WtTableCaption) template, suppressRtd);
				break;

			case TR:
				newElement = createTr((WtTableRow) template, suppressRtd);
				break;

			case TD:
				newElement = createTd((WtTableCell) template, suppressRtd);
				break;

			case TH:
				newElement = createTh((WtTableHeader) template, suppressRtd);
				break;

			case FRAMED_IMG:
			case INLINE_IMG:
				newElement = createImage((WtImageLink) template, suppressRtd);
				break;

			case LCT_VAR_CONV:
				newElement = createLctVarConv((WtLctVarConv) template, suppressRtd);
				break;

			default:
				throw new AssertionError("Don't know how to create element for: " + elementType);
		}

		if (!suppressRtd)
		{
			/**
			 * Copy everything except the last field. We assume that every
			 * element is built like "RTD (CHILD RTD)* (BODY RTD)?". By
			 * excluding the last RTD field (only if there's at least two RTD
			 * fields) we hope to always "cut off" the RTD portion that belongs
			 * to the "end tag".
			 */
			copyAllExceptLastRtd(template, newElement, copyAllRtd);
		}

		copyNodeAttributes(template, newElement);
		return newElement;
	}

	private void copyAllExceptLastRtd(
			WtNode template,
			WtNode newElement,
			boolean copyAllRtd)
	{
		WtRtData rtd = template.getRtd();
		if (rtd != null)
		{
			if (copyAllRtd || rtd.size() == 1)
			{
				newElement.setRtd(rtd);
			}
			else
			{
				int size = rtd.size() - 1;
				WtRtData newRtd = new WtRtData(size + 1);
				for (int i = 0; i < size; ++i)
					newRtd.setField(i, template.getRtd().getField(i));
				newElement.setRtd(newRtd);
			}
		}
	}

	// =========================================================================

	private WtNode createLctVarConv(WtLctVarConv template, boolean suppressRtd)
	{
		WtLctFlags flags = template.getFlags();
		if (suppressRtd && !(flags instanceof WtEmptyImmutableNode))
		{
			flags = (WtLctFlags) template.getFlags().deepCloneWrapException();
			flags.suppressRtd();
		}
		return nf.lctVarConv(flags, createBody(template.getText(), suppressRtd));
	}

	private WtNode createTh(WtTableHeader template, boolean suppressRtd)
	{
		return nf.th(
				suppressRtdOnChildren(template.getXmlAttributes(), suppressRtd),
				createBody(template.getBody(), suppressRtd));
	}

	private WtNode createTd(WtTableCell template, boolean suppressRtd)
	{
		return nf.td(
				suppressRtdOnChildren(template.getXmlAttributes(), suppressRtd),
				createBody(template.getBody(), suppressRtd));
	}

	private WtNode createTr(WtTableRow template, boolean suppressRtd)
	{
		return nf.tr(
				suppressRtdOnChildren(template.getXmlAttributes(), suppressRtd),
				createBody(template.getBody(), suppressRtd));
	}

	private WtNode createCaption(WtTableCaption template, boolean suppressRtd)
	{
		return nf.caption(
				suppressRtdOnChildren(template.getXmlAttributes(), suppressRtd),
				createBody(template.getBody(), suppressRtd));
	}

	private WtNode createImage(WtImageLink template, boolean suppressRtd)
	{
		WtLinkTitle title = null;
		if (template.hasTitle())
			title = createLinkTitle(template.getTitle(), suppressRtd);

		WtLinkOptions options = template.getOptions();
		if (suppressRtd)
		{
			// Does the cloning for us
			options = suppressRtdOnChildren(options);
		}
		else if ((title != null) && !(options instanceof WtEmptyImmutableNode))
		{
			// We have to clone ourselves
			options = (WtLinkOptions) options.deepCloneWrapException();
		}

		WtImageLink newElement = nf.img(template.getTarget(), options);
		if (title != null)
			newElement.setTitle(title);

		return newElement;
	}

	private WtNode createExtLink(WtExternalLink template, boolean suppressRtd)
	{
		WtUrl target = template.getTarget();
		if (suppressRtd)
		{
			target = (WtUrl) target.deepCloneWrapException();
			target.suppressRtd();
		}

		WtExternalLink newElement = nf.extLink(target);

		if (template.hasTitle())
			newElement.setTitle(createLinkTitle(template.getTitle(), suppressRtd));

		return newElement;
	}

	private WtNode createIntLink(WtInternalLink template, boolean suppressRtd)
	{
		WtPageName target = template.getTarget();
		if (suppressRtd)
		{
			target = (WtPageName) target.deepCloneWrapException();
			target.suppressRtd();
		}

		WtInternalLink newElement = nf.intLink(
				template.getPrefix(),
				target,
				template.getPostfix());

		if (template.hasTitle())
			newElement.setTitle(createLinkTitle(template.getTitle(), suppressRtd));

		return newElement;
	}

	private WtLinkTitle createLinkTitle(
			WtLinkTitle template,
			boolean suppressRtd)
	{
		WtLinkTitle newElement = createEmptyLinkTitle();
		if (!suppressRtd)
			newElement.setRtd(template.getRtd());
		/**
		 * LinkTitle is the "body" of a link, the part that gets repeated if the
		 * link was interrupted. Therefore, it must be rendered. However, we
		 * don't copy the RTD of the LinkTitle since the RTD is only the
		 * starting pipe which must not get repeated.
		 */
		/*
		else
			newElement.suppressRtd();
		*/
		return newElement;
	}

	private WtNode createUrl(WtUrl template)
	{
		return nf.url(template.getProtocol(), template.getPath());
	}

	private WtNode createTable(WtTable template, boolean suppressRtd)
	{
		WtBody body = createBody(
				template.hasBody() ? template.getBody() : null, suppressRtd);

		return nf.table(
				suppressRtdOnChildren(template.getXmlAttributes(), suppressRtd),
				body);
	}

	private WtNode createSection(WtSection template, boolean suppressRtd)
	{
		WtBody body = createBody(
				template.hasBody() ? template.getBody() : null, suppressRtd);

		WtHeading heading = nf.heading(nf.list());
		if (!suppressRtd)
			heading.setRtd(template.getHeading().getRtd());
		else
			heading.suppressRtd();

		return nf.section(template.getLevel(), heading, body);
	}

	private WtNode createPage(WtParsedWikitextPage template)
	{
		WtParsedWikitextPage newElement = nf.parsedPage(nf.list());
		newElement.setEntityMap(template.getEntityMap());
		newElement.setWarnings(template.getWarnings());
		return newElement;
	}

	private WtNode createImplicitTableRow()
	{
		WtTableRow tr = nf.tr(nf.emptyAttrs(), createEmptyBody());
		tr.setImplicit(true);
		return tr;
	}

	private WtNode createImplicitTableBody()
	{
		return nf.itbody(createEmptyBody());
	}

	// =========================================================================

	private WtBody createEmptyBody()
	{
		return nf.body(nf.list());
	}

	private WtLinkTitle createEmptyLinkTitle()
	{
		return nf.linkTitle(nf.list());
	}

	private WtBody createBody(WtBody template, boolean suppressRtd)
	{
		WtBody newBody;
		if (template != null)
		{
			newBody = createEmptyBody();
			if (!suppressRtd)
				newBody.setRtd(template.getRtd());
			else
				/**
				 * We never suppress RTD of the body node's children. Only the
				 * RTD information of the body node itself.
				 */
				;//newBody.suppressRtd();
		}
		else
			newBody = nf.noBody();
		return newBody;
	}

	// =========================================================================

	private String getTagNameOrFail(ElementType type)
	{
		String tagName = type.getXmlTagName();
		if (tagName == null)
			throw new IllegalArgumentException(
					"Don't know tag name for given node type: " + type);

		return tagName;
	}

	private static void copyNodeAttributes(WtNode from, WtNode to)
	{
		if (from.hasAttributes())
			to.setAttributes(from.getAttributes());
	}

	private <T extends WtContentNode> T suppressRtdOnChildren(
			T container,
			boolean suppressRtd)
	{
		return suppressRtd ? suppressRtdOnChildren(container) : container;
	}

	private <T extends WtContentNode> T suppressRtdOnChildren(T container)
	{
		if (container instanceof WtEmptyImmutableNode)
			return container;

		@SuppressWarnings("unchecked")
		T clone = (T) container.deepCloneWrapException();
		clone.suppressRtd();
		/*
		for (WtNode attr : clone)
			attr.suppressRtd();
		*/
		return clone;
	}
}
