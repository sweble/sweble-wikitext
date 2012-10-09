package org.sweble.wikitext.parser.nodes;

import java.util.Collection;

import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.nodes.WtBody.WtBodyImpl;
import org.sweble.wikitext.parser.nodes.WtIllegalCodePoint.IllegalCodePointType;
import org.sweble.wikitext.parser.nodes.WtImageLink.ImageHorizAlign;
import org.sweble.wikitext.parser.nodes.WtImageLink.ImageLinkTarget;
import org.sweble.wikitext.parser.nodes.WtImageLink.ImageVertAlign;
import org.sweble.wikitext.parser.nodes.WtImageLink.ImageViewFormat;
import org.sweble.wikitext.parser.nodes.WtLinkOptionAltText.WtLinkOptionAltTextImpl;
import org.sweble.wikitext.parser.nodes.WtLinkOptions.WtLinkOptionsImpl;
import org.sweble.wikitext.parser.nodes.WtLinkTarget.LinkTargetType;
import org.sweble.wikitext.parser.nodes.WtLinkTitle.WtLinkTitleImpl;
import org.sweble.wikitext.parser.nodes.WtName.WtNameImpl;
import org.sweble.wikitext.parser.nodes.WtNodeList.WtNodeListImpl;
import org.sweble.wikitext.parser.nodes.WtOnlyInclude.XmlElementType;
import org.sweble.wikitext.parser.nodes.WtTagExtensionBody.WtTagExtensionBodyImpl;
import org.sweble.wikitext.parser.nodes.WtTemplateArguments.WtTemplateArgumentsImpl;
import org.sweble.wikitext.parser.nodes.WtValue.WtValueImpl;
import org.sweble.wikitext.parser.nodes.WtXmlAttributes.WtXmlAttributesImpl;
import org.sweble.wikitext.parser.parser.LinkBuilder;
import org.sweble.wikitext.parser.postprocessor.IntermediateTags;

import xtc.util.Pair;

public class WikitextNodeFactoryImpl
		implements
			WikitextNodeFactory
{
	private ParserConfig parserConfig;
	
	// =========================================================================
	
	public WikitextNodeFactoryImpl(ParserConfig parserConfig)
	{
		super();
		this.parserConfig = parserConfig;
	}
	
	// =========================================================================
	
	@Override
	public WtLinkOptionLinkTarget loLinkTarget(ImageLinkTarget type)
	{
		return new WtLinkOptionLinkTarget(type);
	}
	
	@Override
	public WtLinkOptionLinkTarget loNoLinkTarget()
	{
		return new WtLinkOptionLinkTarget(
				new ImageLinkTarget(LinkTargetType.NO_LINK));
	}
	
	@Override
	public WtRedirect redirect(WtPageName target)
	{
		return new WtRedirect(target);
	}
	
	@Override
	public WtXmlAttribute attr(String name)
	{
		return new WtXmlAttribute(name);
	}
	
	@Override
	public WtXmlAttribute attr(String name, WtValue value)
	{
		return new WtXmlAttribute(name, value);
	}
	
	@Override
	public WtXmlEmptyTag emptyTag(String name, WtXmlAttributes xmlAttributes)
	{
		return new WtXmlEmptyTag(name, xmlAttributes);
	}
	
	@Override
	public WtXmlStartTag startTag(String name, WtXmlAttributes xmlAttributes)
	{
		return new WtXmlStartTag(name, xmlAttributes);
	}
	
	@Override
	public WtImStartTag imStartTag(IntermediateTags type, boolean synthetic)
	{
		return new WtImStartTag(type, synthetic);
	}
	
	@Override
	public WtExternalLink extLink(WtUrl target)
	{
		return new WtExternalLink(target);
	}
	
	@Override
	public WtExternalLink extLink(WtUrl target, WtLinkTitle title)
	{
		return new WtExternalLink(target, title);
	}
	
	@Override
	public WtInternalLink intLink(
			String prefix,
			WtPageName target,
			String postfix)
	{
		return new WtInternalLink(prefix, target, postfix);
	}
	
	@Override
	public WtSection section(int level, WtHeading heading, WtBody body)
	{
		return new WtSection(level, heading, body);
	}
	
	@Override
	public WtTable table(WtXmlAttributes xmlAttributes)
	{
		return new WtTable(xmlAttributes);
	}
	
	@Override
	public WtTable table(WtXmlAttributes xmlAttributes, WtBody body)
	{
		return new WtTable(xmlAttributes, body);
	}
	
	@Override
	public WtTableCaption caption(WtXmlAttributes xmlAttributes, WtBody body)
	{
		return new WtTableCaption(xmlAttributes, body);
	}
	
	@Override
	public WtTableCell td(WtXmlAttributes xmlAttributes, WtBody body)
	{
		return new WtTableCell(xmlAttributes, body);
	}
	
	@Override
	public WtTableHeader th(WtXmlAttributes xmlAttributes, WtBody body)
	{
		return new WtTableHeader(xmlAttributes, body);
	}
	
	@Override
	public WtTableRow tr(WtXmlAttributes xmlAttributes, WtBody body)
	{
		return new WtTableRow(xmlAttributes, body);
	}
	
	@Override
	public WtTagExtension tagExt(String name, WtXmlAttributes xmlAttributes)
	{
		return new WtTagExtension(name, xmlAttributes);
	}
	
	@Override
	public WtTagExtension tagExt(
			String name,
			WtXmlAttributes xmlAttributes,
			WtTagExtensionBody body)
	{
		return new WtTagExtension(name, xmlAttributes, body);
	}
	
	@Override
	public WtTemplate tmpl(WtName name, WtTemplateArguments args)
	{
		return new WtTemplate(name, args);
	}
	
	@Override
	public WtTemplateArgument tmplArg(WtValue value)
	{
		return new WtTemplateArgument(value);
	}
	
	@Override
	public WtTemplateArgument tmplArg(WtName name, WtValue value)
	{
		return new WtTemplateArgument(name, value);
	}
	
	@Override
	public WtXmlElement elem(String name, WtXmlAttributes xmlAttributes)
	{
		return new WtXmlElement(name, xmlAttributes);
	}
	
	@Override
	public WtXmlElement elem(
			String name,
			WtXmlAttributes xmlAttributes,
			WtBody body)
	{
		return new WtXmlElement(name, xmlAttributes, body);
	}
	
	@Override
	public WtImageLink img(WtPageName target)
	{
		return img(target, emptyLinkOpts(), null);
	}
	
	@Override
	public WtImageLink img(WtPageName target, WtLinkOptions options)
	{
		return img(target, options, null);
	}
	
	@Override
	public WtImageLink img(
			WtPageName target,
			WtLinkOptions options,
			WtLinkTitle title)
	{
		LinkBuilder b = new LinkBuilder(parserConfig, target);
		for (WtNode option : options)
		{
			switch (option.getNodeType())
			{
				case WtNode.NT_LINK_OPTION_ALT_TEXT:
					b.addOption((WtLinkOptionAltText) option);
					break;
				case WtNode.NT_LINK_OPTION_GARBAGE:
					break;
				case WtNode.NT_LINK_OPTION_KEYWORD:
					b.addOption((WtLinkOptionKeyword) option);
					break;
				case WtNode.NT_LINK_OPTION_LINK_TARGET:
					b.addOption((WtLinkOptionLinkTarget) option);
					break;
				case WtNode.NT_LINK_OPTION_RESIZE:
					b.addOption((WtLinkOptionResize) option);
					break;
				default:
					throw new InternalError("Is that an option?");
			}
		}
		
		b.setTitle(title);
		
		WtNode link = b.build(options, null);
		if (!(link instanceof WtImageLink))
			throw new IllegalArgumentException("Given target does not point to an image!");
		
		return (WtImageLink) link;
	}
	
	@Override
	public WtImageLink img(
			WtPageName target,
			WtLinkOptions options,
			ImageViewFormat format,
			boolean border,
			ImageHorizAlign hAlign,
			ImageVertAlign vAlign,
			int width,
			int height,
			boolean upright,
			ImageLinkTarget link,
			WtLinkOptionAltText alt)
	{
		return new WtImageLink(
				target,
				options,
				format,
				border,
				hAlign,
				vAlign,
				width,
				height,
				upright,
				link,
				alt);
	}
	
	@Override
	public WtTemplateParameter tmplParam(WtName name)
	{
		return new WtTemplateParameter(name);
	}
	
	@Override
	public WtTemplateParameter tmplParam(WtName name, WtValue defaultValue)
	{
		return new WtTemplateParameter(name, defaultValue);
	}
	
	@Override
	public WtTemplateParameter tmplParam(
			WtName name,
			WtValue defaultValue,
			WtTemplateArguments garbage)
	{
		return new WtTemplateParameter(name, defaultValue, garbage);
	}
	
	@Override
	public WtHorizontalRule hr()
	{
		return new WtHorizontalRule();
	}
	
	@Override
	public WtIllegalCodePoint illegalCp(
			String codePoint,
			IllegalCodePointType type)
	{
		return new WtIllegalCodePoint(codePoint, type);
	}
	
	@Override
	public WtLinkOptionKeyword loKeyword(String keyword)
	{
		return new WtLinkOptionKeyword(keyword);
	}
	
	@Override
	public WtLinkOptionResize loResize(int width, int height)
	{
		return new WtLinkOptionResize(width, height);
	}
	
	@Override
	public WtPageSwitch pageSwitch(String name)
	{
		return new WtPageSwitch(name);
	}
	
	@Override
	public WtSignature sig(int tildeCount)
	{
		return new WtSignature(tildeCount);
	}
	
	@Override
	public WtTicks ticks(int tickCount)
	{
		return new WtTicks(tickCount);
	}
	
	@Override
	public WtUrl url(String protocol, String path)
	{
		return new WtUrl(protocol, path);
	}
	
	@Override
	public WtXmlCharRef charRef(int codePoint)
	{
		return new WtXmlCharRef(codePoint);
	}
	
	@Override
	public WtXmlEndTag endTag(String name)
	{
		return new WtXmlEndTag(name);
	}
	
	@Override
	public WtImEndTag imEndTag(IntermediateTags type, boolean synthetic)
	{
		return new WtImEndTag(type, synthetic);
	}
	
	@Override
	public WtXmlEntityRef entityRef(String name, String resolved)
	{
		return new WtXmlEntityRef(name, resolved);
	}
	
	@Override
	public WtNodeList list()
	{
		return new WtNodeListImpl();
	}
	
	@Override
	public WtNodeList list(Collection<? extends WtNode> list)
	{
		return new WtNodeListImpl(list);
	}
	
	@Override
	public WtNodeList list(Pair<? extends WtNode> list)
	{
		return new WtNodeListImpl(list);
	}
	
	@Override
	public WtNodeList list(WtNode child)
	{
		return new WtNodeListImpl(child);
	}
	
	@Override
	public WtNodeList list(Object... content)
	{
		return new WtNodeListImpl(content);
	}
	
	@Override
	public WtNodeList emptyList()
	{
		return WtNodeList.EMPTY;
	}
	
	@Override
	public WtBody body(WtNodeList content)
	{
		return new WtBodyImpl(content);
	}
	
	@Override
	public WtBody emptyBody()
	{
		return WtBody.EMPTY;
	}
	
	@Override
	public WtBody noBody()
	{
		return WtBody.NO_BODY;
	}
	
	@Override
	public WtBold b(WtNodeList content)
	{
		return new WtBold(content);
	}
	
	@Override
	public WtDefinitionList dl(WtNodeList content)
	{
		return new WtDefinitionList(content);
	}
	
	@Override
	public WtDefinitionListDef dd(WtNodeList content)
	{
		return new WtDefinitionListDef(content);
	}
	
	@Override
	public WtDefinitionListTerm dt(WtNodeList content)
	{
		return new WtDefinitionListTerm(content);
	}
	
	@Override
	public WtHeading heading(WtNodeList content)
	{
		return new WtHeading(content);
	}
	
	@Override
	public WtItalics i(WtNodeList content)
	{
		return new WtItalics(content);
	}
	
	@Override
	public WtLinkOptionAltText loAlt(WtNodeList content)
	{
		return new WtLinkOptionAltTextImpl(content);
	}
	
	@Override
	public WtLinkOptionAltText noLoAlt()
	{
		return WtLinkOptionAltText.NO_ALT;
	}
	
	@Override
	public WtLinkOptions linkOpts(WtNodeList content)
	{
		return new WtLinkOptionsImpl(content);
	}
	
	@Override
	public WtLinkOptions emptyLinkOpts()
	{
		return WtLinkOptions.EMPTY;
	}
	
	@Override
	public WtLinkTitle linkTitle(WtNodeList content)
	{
		return new WtLinkTitleImpl(content);
	}
	
	@Override
	public WtLinkTitle noLinkTitle()
	{
		return WtLinkTitle.NO_TITLE;
	}
	
	@Override
	public WtListItem li(WtNodeList content)
	{
		return new WtListItem(content);
	}
	
	@Override
	public WtName name(WtNodeList content)
	{
		return new WtNameImpl(content);
	}
	
	@Override
	public WtName noName()
	{
		return WtName.NO_NAME;
	}
	
	@Override
	public WtOnlyInclude onlyInc(WtNodeList content, XmlElementType elementType)
	{
		return new WtOnlyInclude(content, elementType);
	}
	
	@Override
	public WtOrderedList ol(WtNodeList content)
	{
		return new WtOrderedList(content);
	}
	
	@Override
	public WtParsedWikitextPage parsedPage(WtNodeList content)
	{
		return new WtParsedWikitextPage(content);
	}
	
	@Override
	public WtParsedWikitextPage parsedPage(
			WtNodeList content,
			WtEntityMap entityMap)
	{
		return new WtParsedWikitextPage(content, entityMap);
	}
	
	@Override
	public WtPreproWikitextPage preproPage(WtNodeList content)
	{
		return new WtPreproWikitextPage(content);
	}
	
	@Override
	public WtPreproWikitextPage preproPage(
			WtNodeList content,
			WtEntityMap entityMap)
	{
		return new WtPreproWikitextPage(content, entityMap);
	}
	
	@Override
	public WtParagraph p(WtNodeList content)
	{
		return new WtParagraph(content);
	}
	
	@Override
	public WtSemiPre semiPre(WtNodeList content)
	{
		return new WtSemiPre(content);
	}
	
	@Override
	public WtSemiPreLine semiPreLine(WtNodeList content)
	{
		return new WtSemiPreLine(content);
	}
	
	@Override
	public WtTemplateArguments tmplArgs(WtNodeList content)
	{
		return new WtTemplateArgumentsImpl(content);
	}
	
	@Override
	public WtTemplateArguments emptyTmplArgs()
	{
		return WtTemplateArguments.EMPTY;
	}
	
	@Override
	public WtUnorderedList ul(WtNodeList content)
	{
		return new WtUnorderedList(content);
	}
	
	@Override
	public WtValue value(WtNodeList content)
	{
		return new WtValueImpl(content);
	}
	
	@Override
	public WtValue noValue()
	{
		return WtValue.NO_VALUE;
	}
	
	@Override
	public WtWhitespace ws(WtNodeList content, boolean hasNewline)
	{
		return new WtWhitespace(content, hasNewline);
	}
	
	@Override
	public WtXmlAttributes attrs(WtNodeList content)
	{
		return new WtXmlAttributesImpl(content);
	}
	
	@Override
	public WtXmlAttributes emptyAttrs()
	{
		return WtXmlAttributes.EMPTY;
	}
	
	@Override
	public WtIgnored ignored(String content)
	{
		return new WtIgnored(content);
	}
	
	@Override
	public WtLinkOptionGarbage loGarbage(String content)
	{
		return new WtLinkOptionGarbage(content);
	}
	
	@Override
	public WtNewline newline(String content)
	{
		return new WtNewline(content);
	}
	
	@Override
	public WtPageName pageName(String content)
	{
		return new WtPageName(content);
	}
	
	@Override
	public WtTagExtensionBody tagExtBody(String content)
	{
		return new WtTagExtensionBodyImpl(content);
	}
	
	@Override
	public WtTagExtensionBody noTagExtBody()
	{
		return WtTagExtensionBody.NO_BODY;
	}
	
	@Override
	public WtXmlAttributeGarbage attrGarbage(String content)
	{
		return new WtXmlAttributeGarbage(content);
	}
	
	@Override
	public WtXmlComment comment(String content)
	{
		return new WtXmlComment(content);
	}
	
	@Override
	public WtXmlComment comment(String prefix, String content, String suffix)
	{
		return new WtXmlComment(content, prefix, suffix);
	}
	
	@Override
	public WtText text(String content)
	{
		return new WtText(content);
	}
	
	// =========================================================================
	
	/**
	 * @deprecated
	 */
	public static WtItalics i_(WtNodeList body)
	{
		return new WtItalics(body);
	}
	
	/**
	 * @deprecated
	 */
	public static WtBold b_(WtNodeList body)
	{
		return new WtBold(body);
	}
	
	/**
	 * @deprecated
	 */
	public static WtParagraph p_(WtNodeList body)
	{
		return new WtParagraph(body);
	}
	
	/**
	 * @deprecated
	 */
	public static WtImStartTag imStartTag_(
			IntermediateTags type,
			boolean synthetic)
	{
		return new WtImStartTag(type, synthetic);
	}
	
	/**
	 * @deprecated
	 */
	public static WtImEndTag imEndTag_(
			IntermediateTags type,
			boolean synthetic)
	{
		return new WtImEndTag(type, synthetic);
	}
	
	/**
	 * @deprecated
	 */
	public static WtNodeList list_()
	{
		return new WtNodeListImpl();
	}
	
	/**
	 * @deprecated
	 */
	public static WtNodeList list_(WtNode child)
	{
		return new WtNodeListImpl(child);
	}
	
	/**
	 * @deprecated
	 */
	public static WtNodeList list_(Collection<WtNode> content)
	{
		return new WtNodeListImpl(content);
	}
	
	/**
	 * @deprecated
	 */
	public static WtText text_(String text)
	{
		return new WtText(text);
	}
	
	/**
	 * @deprecated
	 */
	public static WtXmlCharRef charRef_(int codePoint)
	{
		return new WtXmlCharRef(codePoint);
	}
	
	/**
	 * @deprecated
	 */
	public static WtXmlEntityRef entityRef_(String name, String resolved)
	{
		return new WtXmlEntityRef(name, resolved);
	}
}
