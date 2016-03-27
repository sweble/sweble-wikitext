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

package org.sweble.wikitext.engine.serialization;

import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.parser.nodes.WtBody.WtBodyImpl;
import org.sweble.wikitext.parser.nodes.WtBody.WtEmptyBody;
import org.sweble.wikitext.parser.nodes.WtBody.WtNoBody;
import org.sweble.wikitext.parser.nodes.WtBold;
import org.sweble.wikitext.parser.nodes.WtDefinitionList;
import org.sweble.wikitext.parser.nodes.WtDefinitionListDef;
import org.sweble.wikitext.parser.nodes.WtDefinitionListTerm;
import org.sweble.wikitext.parser.nodes.WtExternalLink;
import org.sweble.wikitext.parser.nodes.WtHeading;
import org.sweble.wikitext.parser.nodes.WtHorizontalRule;
import org.sweble.wikitext.parser.nodes.WtIgnored;
import org.sweble.wikitext.parser.nodes.WtIllegalCodePoint;
import org.sweble.wikitext.parser.nodes.WtImageLink;
import org.sweble.wikitext.parser.nodes.WtInternalLink;
import org.sweble.wikitext.parser.nodes.WtItalics;
import org.sweble.wikitext.parser.nodes.WtLctFlags.WtLctFlagsImpl;
import org.sweble.wikitext.parser.nodes.WtLctFlags.WtNoLctFlags;
import org.sweble.wikitext.parser.nodes.WtLctRule;
import org.sweble.wikitext.parser.nodes.WtLctRuleConv;
import org.sweble.wikitext.parser.nodes.WtLctRuleGarbage;
import org.sweble.wikitext.parser.nodes.WtLctRuleText;
import org.sweble.wikitext.parser.nodes.WtLctRules;
import org.sweble.wikitext.parser.nodes.WtLctVarConv;
import org.sweble.wikitext.parser.nodes.WtLinkOptionAltText.WtLinkOptionAltTextImpl;
import org.sweble.wikitext.parser.nodes.WtLinkOptionAltText.WtNoLinkOptionAltText;
import org.sweble.wikitext.parser.nodes.WtLinkOptionGarbage;
import org.sweble.wikitext.parser.nodes.WtLinkOptionKeyword;
import org.sweble.wikitext.parser.nodes.WtLinkOptionLinkTarget;
import org.sweble.wikitext.parser.nodes.WtLinkOptionResize;
import org.sweble.wikitext.parser.nodes.WtLinkOptions.WtEmptyLinkOptions;
import org.sweble.wikitext.parser.nodes.WtLinkOptions.WtLinkOptionsImpl;
import org.sweble.wikitext.parser.nodes.WtLinkTarget.WtNoLink;
import org.sweble.wikitext.parser.nodes.WtLinkTitle.WtLinkTitleImpl;
import org.sweble.wikitext.parser.nodes.WtLinkTitle.WtNoLinkTitle;
import org.sweble.wikitext.parser.nodes.WtListItem;
import org.sweble.wikitext.parser.nodes.WtName.WtNameImpl;
import org.sweble.wikitext.parser.nodes.WtName.WtNoName;
import org.sweble.wikitext.parser.nodes.WtNewline;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList.WtEmptyNodeList;
import org.sweble.wikitext.parser.nodes.WtNodeList.WtNodeListImpl;
import org.sweble.wikitext.parser.nodes.WtOnlyInclude;
import org.sweble.wikitext.parser.nodes.WtOrderedList;
import org.sweble.wikitext.parser.nodes.WtPageName;
import org.sweble.wikitext.parser.nodes.WtPageSwitch;
import org.sweble.wikitext.parser.nodes.WtParagraph;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.nodes.WtParserEntity;
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
import org.sweble.wikitext.parser.nodes.WtTagExtensionBody.WtNoTagExtensionBody;
import org.sweble.wikitext.parser.nodes.WtTagExtensionBody.WtTagExtensionBodyImpl;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtTemplateArgument;
import org.sweble.wikitext.parser.nodes.WtTemplateArguments.WtEmptyTemplateArguments;
import org.sweble.wikitext.parser.nodes.WtTemplateArguments.WtTemplateArgumentsImpl;
import org.sweble.wikitext.parser.nodes.WtTemplateParameter;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtTicks;
import org.sweble.wikitext.parser.nodes.WtUnorderedList;
import org.sweble.wikitext.parser.nodes.WtUrl;
import org.sweble.wikitext.parser.nodes.WtValue.WtNoValue;
import org.sweble.wikitext.parser.nodes.WtValue.WtValueImpl;
import org.sweble.wikitext.parser.nodes.WtWhitespace;
import org.sweble.wikitext.parser.nodes.WtXmlAttribute;
import org.sweble.wikitext.parser.nodes.WtXmlAttributeGarbage;
import org.sweble.wikitext.parser.nodes.WtXmlAttributes.WtEmptyXmlAttributes;
import org.sweble.wikitext.parser.nodes.WtXmlAttributes.WtXmlAttributesImpl;
import org.sweble.wikitext.parser.nodes.WtXmlCharRef;
import org.sweble.wikitext.parser.nodes.WtXmlComment;
import org.sweble.wikitext.parser.nodes.WtXmlElement;
import org.sweble.wikitext.parser.nodes.WtXmlEmptyTag;
import org.sweble.wikitext.parser.nodes.WtXmlEndTag;
import org.sweble.wikitext.parser.nodes.WtXmlEntityRef;
import org.sweble.wikitext.parser.nodes.WtXmlStartTag;

import de.fau.cs.osr.ptk.common.serialization.AstConverterBase;
import de.fau.cs.osr.ptk.common.serialization.AstNodeConverterBase;
import de.fau.cs.osr.ptk.common.serialization.SimpleTypeNameMapper;

public class EngineAstNodeConverter
{
	private static final ReadOnlyTypeNameMapper tnm = new ReadOnlyTypeNameMapper();

	// =========================================================================

	/**
	 * The returned type name mapper is read-only!
	 */
	public static SimpleTypeNameMapper getTypeNameMapper()
	{
		return tnm;
	}

	// =========================================================================

	public static void setup(AstConverterBase converter)
	{
		setupTypeNameMapper(converter);
	}

	public static void setup(
			WikiConfig config,
			AstNodeConverterBase<WtNode> converter)
	{
		setupBasicTypes(converter);
		setupDefaultNodeFactory(config, converter);
		setupOutputMinification(converter);
		setupTypeNameMapper(converter);
	}

	public static void setupBasicTypes(AstNodeConverterBase<WtNode> converter)
	{
		converter.setStringNodeType(WtText.class);
	}

	public static void setupDefaultNodeFactory(
			WikiConfig config,
			AstNodeConverterBase<WtNode> converter)
	{
		converter.setNodeFactory(config.getNodeFactory());
	}

	public static void setupOutputMinification(
			AstNodeConverterBase<WtNode> converter)
	{
		converter.suppressNode(WtNoTagExtensionBody.class);
		converter.suppressTypeInfo(WtTagExtensionBodyImpl.class);

		converter.suppressNode(WtNoLinkOptionAltText.class);
		converter.suppressTypeInfo(WtLinkOptionAltTextImpl.class);

		converter.suppressNode(WtNoLinkTitle.class);
		converter.suppressTypeInfo(WtLinkTitleImpl.class);

		converter.suppressNode(WtNoLctFlags.class);
		converter.suppressTypeInfo(WtLctFlagsImpl.class);

		converter.suppressNode(WtNoLink.class);

		converter.suppressNode(WtNoName.class);
		converter.suppressTypeInfo(WtNameImpl.class);

		converter.suppressNode(WtNoValue.class);
		converter.suppressTypeInfo(WtValueImpl.class);

		converter.suppressNode(WtNoBody.class);
		converter.suppressTypeInfo(WtEmptyBody.class);
		converter.suppressTypeInfo(WtBodyImpl.class);

		converter.suppressTypeInfo(WtEmptyLinkOptions.class);
		converter.suppressTypeInfo(WtLinkOptionsImpl.class);

		converter.suppressTypeInfo(WtEmptyNodeList.class);
		converter.suppressTypeInfo(WtNodeListImpl.class);

		converter.suppressTypeInfo(WtEmptyTemplateArguments.class);
		converter.suppressTypeInfo(WtTemplateArgumentsImpl.class);

		converter.suppressTypeInfo(WtEmptyXmlAttributes.class);
		converter.suppressTypeInfo(WtXmlAttributesImpl.class);

		converter.setSuppressEmptyStringNodes(true);
		converter.setSuppressEmptyStringProperties(true);
	}

	private static void setupTypeNameMapper(AstConverterBase converter)
	{
		converter.setTypeNameMapper(tnm);
	}

	// =========================================================================

	private static final class ReadOnlyTypeNameMapper
			extends
				SimpleTypeNameMapper
	{
		@Override
		public void add(Class<?> type, String name)
		{
			throw new UnsupportedOperationException("This is a read-only type name mapper!");
		}

		protected void privateAdd(Class<?> type, String name)
		{
			super.add(type, name);
		}
	}

	// =========================================================================

	static
	{
		tnm.privateAdd(WtBodyImpl.class, "body");
		tnm.privateAdd(WtBold.class, "b");
		tnm.privateAdd(WtDefinitionList.class, "dl");
		tnm.privateAdd(WtDefinitionListDef.class, "dd");
		tnm.privateAdd(WtDefinitionListTerm.class, "dt");
		tnm.privateAdd(WtExternalLink.class, "extlink");
		tnm.privateAdd(WtHeading.class, "heading");
		tnm.privateAdd(WtHorizontalRule.class, "hr");
		tnm.privateAdd(WtIgnored.class, "ignored");
		tnm.privateAdd(WtIllegalCodePoint.class, "icp");
		tnm.privateAdd(WtImageLink.class, "image");
		tnm.privateAdd(WtInternalLink.class, "intlink");
		tnm.privateAdd(WtItalics.class, "i");
		tnm.privateAdd(WtLctFlagsImpl.class, "lct-flags");
		tnm.privateAdd(WtLctRule.class, "lct-rule");
		tnm.privateAdd(WtLctRuleConv.class, "lct-ruleconv");
		tnm.privateAdd(WtLctRuleGarbage.class, "lct-rule-garbage");
		tnm.privateAdd(WtLctRuleText.class, "lrt");
		tnm.privateAdd(WtLctRules.class, "lct-rules");
		tnm.privateAdd(WtLctVarConv.class, "lct-varconv");
		tnm.privateAdd(WtLinkOptionAltTextImpl.class, "lo-alt");
		tnm.privateAdd(WtLinkOptionGarbage.class, "lo-garbage");
		tnm.privateAdd(WtLinkOptionKeyword.class, "lo-keyword");
		tnm.privateAdd(WtLinkOptionLinkTarget.class, "lo-target");
		tnm.privateAdd(WtLinkOptionResize.class, "lo-resize");
		tnm.privateAdd(WtLinkOptionsImpl.class, "los");
		tnm.privateAdd(WtLinkTitleImpl.class, "title");
		tnm.privateAdd(WtListItem.class, "li");
		tnm.privateAdd(WtNameImpl.class, "name");
		tnm.privateAdd(WtNewline.class, "nl");
		tnm.privateAdd(WtNodeListImpl.class, "list");
		tnm.privateAdd(WtOnlyInclude.class, "onlyinclude");
		tnm.privateAdd(WtOrderedList.class, "ol");
		tnm.privateAdd(WtPageName.class, "pagename");
		tnm.privateAdd(WtPageSwitch.class, "pageswitch");
		tnm.privateAdd(WtParagraph.class, "p");
		tnm.privateAdd(WtParsedWikitextPage.class, "parsed");
		tnm.privateAdd(WtParserEntity.class, "entity");
		tnm.privateAdd(WtPreproWikitextPage.class, "prepro");
		tnm.privateAdd(WtRedirect.class, "redirect");
		tnm.privateAdd(WtSection.class, "section");
		tnm.privateAdd(WtSemiPre.class, "spre");
		tnm.privateAdd(WtSemiPreLine.class, "spre-line");
		tnm.privateAdd(WtSignature.class, "signature");
		tnm.privateAdd(WtTable.class, "table");
		tnm.privateAdd(WtTableCaption.class, "caption");
		tnm.privateAdd(WtTableCell.class, "td");
		tnm.privateAdd(WtTableHeader.class, "th");
		tnm.privateAdd(WtTableImplicitTableBody.class, "tbody-implicit");
		tnm.privateAdd(WtTableRow.class, "tr");
		tnm.privateAdd(WtTagExtension.class, "tagext");
		tnm.privateAdd(WtTagExtensionBodyImpl.class, "tagext-body");
		tnm.privateAdd(WtTemplate.class, "template");
		tnm.privateAdd(WtTemplateArgument.class, "arg");
		tnm.privateAdd(WtTemplateArgumentsImpl.class, "args");
		tnm.privateAdd(WtTemplateParameter.class, "param");
		tnm.privateAdd(WtText.class, "text");
		tnm.privateAdd(WtTicks.class, "ticks");
		tnm.privateAdd(WtUnorderedList.class, "ul");
		tnm.privateAdd(WtUrl.class, "url");
		tnm.privateAdd(WtValueImpl.class, "value");
		tnm.privateAdd(WtWhitespace.class, "ws");
		tnm.privateAdd(WtXmlAttribute.class, "attr");
		tnm.privateAdd(WtXmlAttributeGarbage.class, "attr-garbage");
		tnm.privateAdd(WtXmlAttributesImpl.class, "attrs");
		tnm.privateAdd(WtXmlCharRef.class, "cref");
		tnm.privateAdd(WtXmlComment.class, "comment");
		tnm.privateAdd(WtXmlElement.class, "elem");
		tnm.privateAdd(WtXmlEmptyTag.class, "emptytag");
		tnm.privateAdd(WtXmlEndTag.class, "endtag");
		tnm.privateAdd(WtXmlEntityRef.class, "eref");
		tnm.privateAdd(WtXmlStartTag.class, "starttag");
	}
}
