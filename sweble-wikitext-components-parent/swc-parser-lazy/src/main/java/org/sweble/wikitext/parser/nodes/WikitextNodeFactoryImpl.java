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

package org.sweble.wikitext.parser.nodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.WtRtData;
import org.sweble.wikitext.parser.nodes.WtBody.WtBodyImpl;
import org.sweble.wikitext.parser.nodes.WtBody.WtEmptyBody;
import org.sweble.wikitext.parser.nodes.WtBody.WtNoBody;
import org.sweble.wikitext.parser.nodes.WtIllegalCodePoint.IllegalCodePointType;
import org.sweble.wikitext.parser.nodes.WtImageLink.ImageHorizAlign;
import org.sweble.wikitext.parser.nodes.WtImageLink.ImageVertAlign;
import org.sweble.wikitext.parser.nodes.WtImageLink.ImageViewFormat;
import org.sweble.wikitext.parser.nodes.WtLctFlags.WtLctFlagsImpl;
import org.sweble.wikitext.parser.nodes.WtLctFlags.WtNoLctFlags;
import org.sweble.wikitext.parser.nodes.WtLinkOptionAltText.WtLinkOptionAltTextImpl;
import org.sweble.wikitext.parser.nodes.WtLinkOptionAltText.WtNoLinkOptionAltText;
import org.sweble.wikitext.parser.nodes.WtLinkOptionLinkTarget.WtLinkOptionLinkTargetImpl;
import org.sweble.wikitext.parser.nodes.WtLinkOptions.WtEmptyLinkOptions;
import org.sweble.wikitext.parser.nodes.WtLinkOptions.WtLinkOptionsImpl;
import org.sweble.wikitext.parser.nodes.WtLinkTarget.LinkTargetType;
import org.sweble.wikitext.parser.nodes.WtLinkTarget.WtNoLink;
import org.sweble.wikitext.parser.nodes.WtLinkTitle.WtLinkTitleImpl;
import org.sweble.wikitext.parser.nodes.WtLinkTitle.WtNoLinkTitle;
import org.sweble.wikitext.parser.nodes.WtName.WtNameImpl;
import org.sweble.wikitext.parser.nodes.WtName.WtNoName;
import org.sweble.wikitext.parser.nodes.WtNodeList.WtEmptyNodeList;
import org.sweble.wikitext.parser.nodes.WtNodeList.WtNodeListImpl;
import org.sweble.wikitext.parser.nodes.WtOnlyInclude.XmlElementType;
import org.sweble.wikitext.parser.nodes.WtTagExtensionBody.WtNoTagExtensionBody;
import org.sweble.wikitext.parser.nodes.WtTagExtensionBody.WtTagExtensionBodyImpl;
import org.sweble.wikitext.parser.nodes.WtTemplateArguments.WtEmptyTemplateArguments;
import org.sweble.wikitext.parser.nodes.WtTemplateArguments.WtTemplateArgumentsImpl;
import org.sweble.wikitext.parser.nodes.WtValue.WtNoValue;
import org.sweble.wikitext.parser.nodes.WtValue.WtValueImpl;
import org.sweble.wikitext.parser.nodes.WtXmlAttributes.WtEmptyXmlAttributes;
import org.sweble.wikitext.parser.nodes.WtXmlAttributes.WtXmlAttributesImpl;
import org.sweble.wikitext.parser.parser.LinkBuilder;
import org.sweble.wikitext.parser.postprocessor.IntermediateTags;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.AstNodeList;
import de.fau.cs.osr.ptk.common.serialization.NodeFactory;
import de.fau.cs.osr.ptk.common.serialization.SimpleNodeFactory;
import xtc.util.Pair;

public class WikitextNodeFactoryImpl
		extends
			SimpleNodeFactory<WtNode>
		implements
			WikitextNodeFactory
{
	private final Map<Class<?>, WtNode> prototypes =
			new HashMap<Class<?>, WtNode>();

	private final Map<Class<?>, WtNode> immutables =
			new HashMap<Class<?>, WtNode>();

	private final Map<NodeFactory.NamedMemberId, Object> defaultValueImmutables =
			new HashMap<NodeFactory.NamedMemberId, Object>();

	private final ParserConfig parserConfig;

	// =========================================================================

	public WikitextNodeFactoryImpl(ParserConfig parserConfig)
	{
		if (parserConfig == null)
			throw new NullPointerException();

		this.parserConfig = parserConfig;

		this.prototypes.put(WtLctRule.class, new WtLctRule());
		this.prototypes.put(WtLinkOptionLinkTarget.class, new WtLinkOptionLinkTargetImpl());
		this.prototypes.put(WtRedirect.class, new WtRedirect());
		this.prototypes.put(WtTableImplicitTableBody.class, new WtTableImplicitTableBody());
		this.prototypes.put(WtXmlAttribute.class, new WtXmlAttribute());
		this.prototypes.put(WtXmlEmptyTag.class, new WtXmlEmptyTag());
		this.prototypes.put(WtXmlStartTag.class, new WtXmlStartTag());

		this.prototypes.put(WtExternalLink.class, new WtExternalLink());
		this.prototypes.put(WtInternalLink.class, new WtInternalLink());
		this.prototypes.put(WtLctRuleConv.class, new WtLctRuleConv());
		this.prototypes.put(WtLctVarConv.class, new WtLctVarConv());
		this.prototypes.put(WtSection.class, new WtSection());
		this.prototypes.put(WtTable.class, new WtTable());
		this.prototypes.put(WtTableCaption.class, new WtTableCaption());
		this.prototypes.put(WtTableCell.class, new WtTableCell());
		this.prototypes.put(WtTableHeader.class, new WtTableHeader());
		this.prototypes.put(WtTableRow.class, new WtTableRow());
		this.prototypes.put(WtTagExtension.class, new WtTagExtension());
		this.prototypes.put(WtTemplate.class, new WtTemplate());
		this.prototypes.put(WtTemplateArgument.class, new WtTemplateArgument());
		this.prototypes.put(WtXmlElement.class, new WtXmlElement());

		this.prototypes.put(WtImageLink.class, new WtImageLink());
		this.prototypes.put(WtTemplateParameter.class, new WtTemplateParameter());

		this.prototypes.put(WtHorizontalRule.class, new WtHorizontalRule());
		this.prototypes.put(WtIllegalCodePoint.class, new WtIllegalCodePoint());
		this.prototypes.put(WtLctFlags.class, new WtLctFlagsImpl());
		this.prototypes.put(WtLctFlagsImpl.class, new WtLctFlagsImpl());
		this.prototypes.put(WtLinkOptionKeyword.class, new WtLinkOptionKeyword());
		this.prototypes.put(WtLinkOptionResize.class, new WtLinkOptionResize());
		this.prototypes.put(WtPageSwitch.class, new WtPageSwitch());
		this.prototypes.put(WtSignature.class, new WtSignature());
		this.prototypes.put(WtTicks.class, new WtTicks());
		this.prototypes.put(WtUrl.class, new WtUrl());
		this.prototypes.put(WtXmlCharRef.class, new WtXmlCharRef());
		this.prototypes.put(WtXmlEndTag.class, new WtXmlEndTag());
		this.prototypes.put(WtXmlEntityRef.class, new WtXmlEntityRef());

		this.prototypes.put(WtParserEntity.class, new WtParserEntity());

		this.prototypes.put(WtNodeList.class, new WtNodeListImpl());
		this.prototypes.put(WtNodeListImpl.class, new WtNodeListImpl());
		this.prototypes.put(WtBody.class, new WtBodyImpl());
		this.prototypes.put(WtBodyImpl.class, new WtBodyImpl());
		this.prototypes.put(WtBold.class, new WtBold());
		this.prototypes.put(WtDefinitionList.class, new WtDefinitionList());
		this.prototypes.put(WtDefinitionListDef.class, new WtDefinitionListDef());
		this.prototypes.put(WtDefinitionListTerm.class, new WtDefinitionListTerm());
		this.prototypes.put(WtHeading.class, new WtHeading());
		this.prototypes.put(WtItalics.class, new WtItalics());
		this.prototypes.put(WtLctRules.class, new WtLctRuleText());
		this.prototypes.put(WtLinkOptionAltText.class, new WtLinkOptionAltTextImpl());
		this.prototypes.put(WtLinkOptionAltTextImpl.class, new WtLinkOptionAltTextImpl());
		this.prototypes.put(WtLinkOptions.class, new WtLinkOptionsImpl());
		this.prototypes.put(WtLinkOptionsImpl.class, new WtLinkOptionsImpl());
		this.prototypes.put(WtLinkTitle.class, new WtLinkTitleImpl());
		this.prototypes.put(WtLinkTitleImpl.class, new WtLinkTitleImpl());
		this.prototypes.put(WtListItem.class, new WtListItem());
		this.prototypes.put(WtName.class, new WtNameImpl());
		this.prototypes.put(WtNameImpl.class, new WtNameImpl());
		this.prototypes.put(WtOnlyInclude.class, new WtOnlyInclude());
		this.prototypes.put(WtOrderedList.class, new WtOrderedList());
		this.prototypes.put(WtParsedWikitextPage.class, new WtParsedWikitextPage());
		this.prototypes.put(WtPreproWikitextPage.class, new WtPreproWikitextPage());
		this.prototypes.put(WtParagraph.class, new WtParagraph());
		this.prototypes.put(WtSemiPre.class, new WtSemiPre());
		this.prototypes.put(WtSemiPreLine.class, new WtSemiPreLine());
		this.prototypes.put(WtTemplateArguments.class, new WtTemplateArgumentsImpl());
		this.prototypes.put(WtTemplateArgumentsImpl.class, new WtTemplateArgumentsImpl());
		this.prototypes.put(WtUnorderedList.class, new WtUnorderedList());
		this.prototypes.put(WtValue.class, new WtValueImpl());
		this.prototypes.put(WtValueImpl.class, new WtValueImpl());
		this.prototypes.put(WtWhitespace.class, new WtWhitespace());
		this.prototypes.put(WtXmlAttributes.class, new WtXmlAttributesImpl());
		this.prototypes.put(WtXmlAttributesImpl.class, new WtXmlAttributesImpl());

		this.prototypes.put(WtIgnored.class, new WtIgnored());
		this.prototypes.put(WtLctRuleGarbage.class, new WtLctRuleGarbage());
		this.prototypes.put(WtLinkOptionGarbage.class, new WtLinkOptionGarbage());
		this.prototypes.put(WtNewline.class, new WtNewline());
		this.prototypes.put(WtPageName.class, new WtPageName());
		this.prototypes.put(WtTagExtensionBody.class, new WtTagExtensionBodyImpl());
		this.prototypes.put(WtTagExtensionBodyImpl.class, new WtTagExtensionBodyImpl());
		this.prototypes.put(WtXmlAttributeGarbage.class, new WtXmlAttributeGarbage());
		this.prototypes.put(WtXmlComment.class, new WtXmlComment());

		this.prototypes.put(WtText.class, new WtText());

		this.immutables.put(WtNoTagExtensionBody.class, WtTagExtensionBody.NO_BODY);
		this.immutables.put(WtNoBody.class, WtBody.NO_BODY);
		this.immutables.put(WtNoLinkOptionAltText.class, WtLinkOptionAltText.NO_ALT);
		this.immutables.put(WtNoLinkTitle.class, WtLinkTitle.NO_TITLE);
		this.immutables.put(WtNoName.class, WtName.NO_NAME);
		this.immutables.put(WtNoValue.class, WtValue.NO_VALUE);

		this.immutables.put(WtEmptyBody.class, WtBody.EMPTY);
		this.immutables.put(WtEmptyLinkOptions.class, WtLinkOptions.EMPTY);
		this.immutables.put(WtEmptyNodeList.class, WtNodeList.EMPTY);
		this.immutables.put(WtEmptyTemplateArguments.class, WtTemplateArguments.EMPTY);
		this.immutables.put(WtEmptyXmlAttributes.class, WtXmlAttributes.EMPTY);

		this.immutables.put(WtNoLctFlags.class, WtLctFlags.NO_FLAGS);
		this.immutables.put(WtNoLink.class, WtLinkTarget.NO_LINK);

		// Default values for nodes that can have "NO_*" children

		this.defaultValueImmutables.put(new NamedMemberId(WtLctRuleConv.class, "flags"), WtLctFlags.NO_FLAGS);
		this.defaultValueImmutables.put(new NamedMemberId(WtLctVarConv.class, "flags"), WtLctFlags.NO_FLAGS);

		this.defaultValueImmutables.put(new NamedMemberId(WtLinkOptionLinkTargetImpl.class, "target"), WtLinkTarget.NO_LINK);

		this.defaultValueImmutables.put(new NamedMemberId(WtTable.class, "body"), WtBody.NO_BODY);
		this.defaultValueImmutables.put(new NamedMemberId(WtSection.class, "body"), WtBody.NO_BODY);
		this.defaultValueImmutables.put(new NamedMemberId(WtXmlElement.class, "body"), WtBody.NO_BODY);

		this.defaultValueImmutables.put(new NamedMemberId(WtImageLink.class, "alt"), WtLinkOptionAltText.NO_ALT);

		this.defaultValueImmutables.put(new NamedMemberId(WtImageLink.class, "title"), WtLinkTitle.NO_TITLE);
		this.defaultValueImmutables.put(new NamedMemberId(WtInternalLink.class, "title"), WtLinkTitle.NO_TITLE);
		this.defaultValueImmutables.put(new NamedMemberId(WtExternalLink.class, "title"), WtLinkTitle.NO_TITLE);

		this.defaultValueImmutables.put(new NamedMemberId(WtTemplateArgument.class, "name"), WtName.NO_NAME);

		this.defaultValueImmutables.put(new NamedMemberId(WtTemplateParameter.class, "default"), WtValue.NO_VALUE);
		this.defaultValueImmutables.put(new NamedMemberId(WtXmlAttribute.class, "value"), WtValue.NO_VALUE);

		this.defaultValueImmutables.put(new NamedMemberId(WtTagExtension.class, "body"), WtTagExtensionBody.NO_BODY);
	}

	// =========================================================================

	protected ParserConfig getParserConfig()
	{
		return parserConfig;
	}

	protected Map<Class<?>, WtNode> getPrototypes()
	{
		return prototypes;
	}

	protected Map<Class<?>, WtNode> getImmutables()
	{
		return immutables;
	}

	protected Map<NodeFactory.NamedMemberId, Object> getDefaultValueImmutables()
	{
		return defaultValueImmutables;
	}

	// =========================================================================

	@Override
	public WtNode instantiateNode(Class<?> clazz)
	{
		WtNode p = prototypes.get(clazz);
		try
		{
			if (p != null)
				return (WtNode) p.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		p = immutables.get(clazz);
		if (p != null)
			return p;
		return super.instantiateNode(clazz);
	}

	@Override
	public WtNode instantiateDefaultChild(
			NodeFactory.NamedMemberId id, Class<?> type)
	{
		WtNode p = (WtNode) defaultValueImmutables.get(id);
		if (p != null)
			return p;
		return super.instantiateDefaultChild(id, type);
	}

	@Override
	public Object instantiateDefaultProperty(
			NodeFactory.NamedMemberId id, Class<?> type)
	{
		if (type == WtRtData.class)
			return null;
		if (type == String.class)
			return "";
		Object p = defaultValueImmutables.get(id);
		if (p != null)
			return p;
		return super.instantiateDefaultProperty(id, type);
	}

	// =========================================================================

	@Override
	public WtLinkOptionLinkTarget loLinkTarget(
			WtLinkTarget target,
			LinkTargetType type)
	{
		return new WtLinkOptionLinkTargetImpl(target, type);
	}

	@Override
	public WtLinkOptionLinkTarget loLinkTargetNoLink()
	{
		return new WtLinkOptionLinkTargetImpl(WtLinkTarget.NO_LINK, LinkTargetType.NO_LINK);
	}

	@Override
	public WtTableImplicitTableBody itbody(WtBody body)
	{
		return new WtTableImplicitTableBody(body);
	}

	@Override
	public WtRedirect redirect(WtPageName target)
	{
		return new WtRedirect(target);
	}

	@Override
	public WtXmlAttribute attr(WtName name)
	{
		return new WtXmlAttribute(name);
	}

	@Override
	public WtXmlAttribute attr(WtName name, WtValue value)
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
	public WtImStartTag imStartTag(IntermediateTags type)
	{
		return new WtImStartTag(type);
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
	public WtLctVarConv lctVarConv(WtLctFlags flags, WtBody body)
	{
		return new WtLctVarConv(flags, body);
	}

	@Override
	public WtLctRuleConv lctRuleConv(WtLctFlags flags, WtLctRules rules)
	{
		return new WtLctRuleConv(flags, rules);
	}

	@Override
	public WtImageLink img(WtPageName target)
	{
		return img(target, emptyLinkOpts());
	}

	@Override
	public WtImageLink img(WtPageName target, WtLinkOptions options)
	{
		LinkBuilder b = new LinkBuilder(parserConfig, target);
		for (WtNode option : options)
		{
			switch (option.getNodeType())
			{
				case WtNode.NT_LINK_OPTION_ALT_TEXT:
				case WtNode.NT_LINK_OPTION_GARBAGE:
				case WtNode.NT_LINK_OPTION_LINK_TARGET:
					break;
				case WtNode.NT_LINK_OPTION_KEYWORD:
					b.addOption((WtLinkOptionKeyword) option);
					break;
				case WtNode.NT_LINK_OPTION_RESIZE:
					b.addOption((WtLinkOptionResize) option);
					break;
				default:
					// Unresolved templates and the like
					// We cannot translate those...
					//throw new InternalError("Is that an option?");
			}
		}

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
			boolean upright)
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
				upright);
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
	public WtImEndTag imEndTag(IntermediateTags type)
	{
		return new WtImEndTag(type);
	}

	@Override
	public WtXmlEntityRef entityRef(String name, String resolved)
	{
		return new WtXmlEntityRef(name, resolved);
	}

	@Override
	public WtLctFlags lctFlags(List<String> flags)
	{
		Set<String> lctFlags = new LinkedHashSet<String>();
		Set<String> lctVariants = new LinkedHashSet<String>();
		List<String> lctGarbage = new ArrayList<String>();
		for (String flag : flags)
		{
			if (this.parserConfig.isLctFlag(flag))
			{
				lctFlags.add(this.parserConfig.normalizeLctFlag(flag));
			}
			else if (this.parserConfig.isLctVariant(flag))
			{
				lctVariants.add(this.parserConfig.normalizeLctVariant(flag));
			}
			else
			{
				lctGarbage.add(flag);
			}
		}
		return new WtLctFlags.WtLctFlagsImpl(lctFlags, lctVariants, lctGarbage);
	}

	@Override
	public WtLctFlags noLctFlags()
	{
		return WtLctFlags.NO_FLAGS;
	}

	@Override
	public WtLctRule lctRule(
			String search,
			String variant,
			WtLctRuleText replace)
	{
		return new WtLctRule(search, variant, replace);
	}

	@Override
	public WtLctRule lctRule(String variant, WtLctRuleText replace)
	{
		return new WtLctRule(variant, replace);
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

	public WtNodeList toList(AstNode<WtNode> n)
	{
		WtNodeList list = list();
		list.addAll(n);
		return list;
	}

	@Override
	public WtNodeList unwrap(AstNodeList<WtNode> n)
	{
		WtNodeList list = list();
		list.exchange(n);
		return list;
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
	public WtLctRules lctRules(WtNodeList rules)
	{
		return new WtLctRules(rules);
	}

	@Override
	public WtLctRuleText lctRuleText(WtNodeList content)
	{
		return new WtLctRuleText(content);
	}

	@Override
	public WtPageName pageName(WtNodeList content)
	{
		return new WtPageName(content);
	}

	@Override
	public WtIgnored ignored(String content)
	{
		return new WtIgnored(content);
	}

	@Override
	public WtLinkOptionGarbage loGarbage(WtNodeList content)
	{
		return new WtLinkOptionGarbage(content);
	}

	@Override
	public WtNewline newline(String content)
	{
		return new WtNewline(content);
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
	public WtXmlAttributeGarbage attrGarbage(WtNodeList content)
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
	public WtLctRuleGarbage lctGarbage(String content)
	{
		return new WtLctRuleGarbage(content);
	}

	@Override
	public WtText text(String content)
	{
		return new WtText(content);
	}
}
