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

package org.sweble.wikitext.parser;

import java.util.Map;

import org.sweble.wikitext.parser.WikitextWarning.WarningSeverity;
import org.sweble.wikitext.parser.nodes.WikitextNodeFactory;
import org.sweble.wikitext.parser.parser.LinkBuilder.LinkType;
import org.sweble.wikitext.parser.utils.AstTextUtils;

import de.fau.cs.osr.utils.XmlEntityResolver;

public interface ParserConfig
		extends
			XmlEntityResolver
{
	// ==[ Encoding validation features ]=======================================

	boolean isConvertIllegalCodePoints();

	// ==[ Parser features ]====================================================

	boolean isWarningsEnabled();

	boolean isWarningLevelEnabled(WarningSeverity severity);

	boolean isAutoCorrect();

	boolean isGatherRtData();

	// ==[ AST creation/processing ]============================================

	WikitextNodeFactory getNodeFactory();

	AstTextUtils getAstTextUtils();

	// ==[ Link classification and parsing ]====================================

	boolean isUrlProtocol(String proto);

	String getInternalLinkPrefixPattern();

	String getInternalLinkPostfixPattern();

	LinkType classifyTarget(String target);

	boolean isNamespace(String nsName);

	boolean isTalkNamespace(String nsName);

	boolean isInterwikiName(String iwPrefix);

	boolean isIwPrefixOfThisWiki(String iwPrefix);

	// ==[ Names ]==============================================================

	boolean isValidPageSwitchName(String name);

	boolean isValidExtensionTagName(String name);

	boolean isRedirectKeyword(String keyword);

	// ==[ Parsing XML elements ]===============================================

	boolean isValidXmlEntityRef(String name);

	Map<String, String> getXmlEntities();

	NonStandardElementBehavior getNonStandardElementBehavior(String elementName);

	boolean isFosterParenting();

	boolean isFosterParentingForTransclusions();

	// ==[ Language Conversion Tags ]===========================================

	boolean isLangConvTagsEnabled();

	boolean isLctFlag(String flag);

	String normalizeLctFlag(String flag);

	boolean isLctVariant(String variant);

	String normalizeLctVariant(String variant);
}
