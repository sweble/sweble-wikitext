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

import de.fau.cs.osr.ptk.common.serialization.AstNodeConverterBase;
import de.fau.cs.osr.ptk.common.serialization.SimpleTypeNameMapper;

public class EngineAstNodeConverter
{
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
		SimpleTypeNameMapper typeNameMapper = new SimpleTypeNameMapper();
		converter.setTypeNameMapper(typeNameMapper);
		
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
	
	private static void setupTypeNameMapper(
			AstNodeConverterBase<WtNode> converter)
	{
		SimpleTypeNameMapper tnm = new SimpleTypeNameMapper();
		converter.setTypeNameMapper(tnm);
		
		tnm.add(WtLctRule.class, "lct-rule");
		tnm.add(WtLinkOptionLinkTarget.class, "lo-target");
		tnm.add(WtRedirect.class, "redirect");
		tnm.add(WtTableImplicitTableBody.class, "tbody-implicit");
		tnm.add(WtXmlAttribute.class, "attr");
		tnm.add(WtXmlEmptyTag.class, "emptytag");
		tnm.add(WtXmlStartTag.class, "starttag");
		
		tnm.add(WtExternalLink.class, "extlink");
		tnm.add(WtInternalLink.class, "intlink");
		tnm.add(WtLctRuleConv.class, "lct-ruleconv");
		tnm.add(WtLctVarConv.class, "lct-varconv");
		tnm.add(WtSection.class, "section");
		tnm.add(WtTable.class, "table");
		tnm.add(WtTableCaption.class, "caption");
		tnm.add(WtTableCell.class, "td");
		tnm.add(WtTableHeader.class, "th");
		tnm.add(WtTableRow.class, "tr");
		tnm.add(WtTagExtension.class, "tagext");
		tnm.add(WtTemplate.class, "template");
		tnm.add(WtTemplateArgument.class, "arg");
		tnm.add(WtXmlElement.class, "elem");
		
		tnm.add(WtImageLink.class, "image");
		tnm.add(WtTemplateParameter.class, "param");
		
		tnm.add(WtHorizontalRule.class, "hr");
		tnm.add(WtIllegalCodePoint.class, "icp");
		tnm.add(WtLctFlagsImpl.class, "lct-flags");
		tnm.add(WtLinkOptionKeyword.class, "lo-keyword");
		tnm.add(WtLinkOptionResize.class, "lo-resize");
		tnm.add(WtPageSwitch.class, "pageswitch");
		tnm.add(WtSignature.class, "signature");
		tnm.add(WtTicks.class, "ticks");
		tnm.add(WtUrl.class, "url");
		tnm.add(WtXmlCharRef.class, "cref");
		tnm.add(WtXmlEndTag.class, "endtag");
		tnm.add(WtXmlEntityRef.class, "eref");
		
		tnm.add(WtParserEntity.class, "entity");
		
		tnm.add(WtNodeListImpl.class, "list");
		tnm.add(WtBodyImpl.class, "body");
		tnm.add(WtBold.class, "b");
		tnm.add(WtDefinitionList.class, "dl");
		tnm.add(WtDefinitionListDef.class, "dd");
		tnm.add(WtDefinitionListTerm.class, "dt");
		tnm.add(WtHeading.class, "heading");
		tnm.add(WtItalics.class, "i");
		tnm.add(WtLctRules.class, "lct-rules");
		tnm.add(WtLinkOptionAltTextImpl.class, "lo-alt");
		tnm.add(WtLinkOptionsImpl.class, "los");
		tnm.add(WtLinkTitleImpl.class, "title");
		tnm.add(WtListItem.class, "li");
		tnm.add(WtNameImpl.class, "name");
		tnm.add(WtOnlyInclude.class, "onlyinclude");
		tnm.add(WtOrderedList.class, "ol");
		tnm.add(WtParsedWikitextPage.class, "parsed");
		tnm.add(WtPreproWikitextPage.class, "prepro");
		tnm.add(WtParagraph.class, "p");
		tnm.add(WtSemiPre.class, "spre");
		tnm.add(WtSemiPreLine.class, "spre-line");
		tnm.add(WtTemplateArgumentsImpl.class, "args");
		tnm.add(WtUnorderedList.class, "ul");
		tnm.add(WtValueImpl.class, "value");
		tnm.add(WtWhitespace.class, "ws");
		tnm.add(WtXmlAttributesImpl.class, "attrs");
		
		tnm.add(WtIgnored.class, "ignored");
		tnm.add(WtLctRuleGarbage.class, "lct-rule-garbage");
		tnm.add(WtLinkOptionGarbage.class, "lo-garbage");
		tnm.add(WtNewline.class, "nl");
		tnm.add(WtPageName.class, "pagename");
		tnm.add(WtTagExtensionBodyImpl.class, "tagext-body");
		tnm.add(WtXmlAttributeGarbage.class, "attr-garbage");
		tnm.add(WtXmlComment.class, "comment");
		
		tnm.add(WtText.class, "text");
	}
}
