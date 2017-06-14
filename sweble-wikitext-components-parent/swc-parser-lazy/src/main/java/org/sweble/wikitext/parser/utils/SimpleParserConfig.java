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

package org.sweble.wikitext.parser.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.sweble.wikitext.parser.NonStandardElementBehavior;
import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WikitextWarning.WarningSeverity;
import org.sweble.wikitext.parser.nodes.WikitextNodeFactory;
import org.sweble.wikitext.parser.nodes.WikitextNodeFactoryImpl;
import org.sweble.wikitext.parser.parser.LinkBuilder.LinkType;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wikitext.parser.parser.LinkTargetParser;

/**
 * This is a simple parser config that is ONLY suited for test purposes!
 */
public class SimpleParserConfig
		implements
			ParserConfig
{
	private final boolean convertIllegalCodePoints;

	private final boolean warningsEnabled;

	private final boolean gatherRtd;

	private final boolean autoCorrect;

	private final boolean langConvTagsEnabled;

	private final WikitextNodeFactory nodeFactory;

	private final AstTextUtilsImpl textUtils;

	// =========================================================================

	public SimpleParserConfig()
	{
		this(
				false /*convertIllegalCodePoints*/,
				true /*warningsEnabled*/,
				true/*gatherRtd*/,
				false/*autoCorrect*/,
				true/*langConvTagsEnabled*/);
	}

	public SimpleParserConfig(
			boolean warningsEnabled,
			boolean gatherRtd,
			boolean autoCorrect)
	{
		this.convertIllegalCodePoints = false;
		this.warningsEnabled = warningsEnabled;
		this.gatherRtd = gatherRtd;
		this.autoCorrect = autoCorrect;
		// Issue #48: false would be the smarter setting but we stick with true
		// for downward compatibility.
		this.langConvTagsEnabled = true;
		this.nodeFactory = new WikitextNodeFactoryImpl(this);
		this.textUtils = new AstTextUtilsImpl(this);
	}

	public SimpleParserConfig(
			boolean warningsEnabled,
			boolean gatherRtd,
			boolean autoCorrect,
			boolean langConvTagsEnabled)
	{
		this.convertIllegalCodePoints = false;
		this.warningsEnabled = warningsEnabled;
		this.gatherRtd = gatherRtd;
		this.autoCorrect = autoCorrect;
		this.langConvTagsEnabled = langConvTagsEnabled;
		this.nodeFactory = new WikitextNodeFactoryImpl(this);
		this.textUtils = new AstTextUtilsImpl(this);
	}

	public SimpleParserConfig(
			boolean convertIllegalCodePoints,
			boolean warningsEnabled,
			boolean gatherRtd,
			boolean autoCorrect,
			boolean langConvTagsEnabled)
	{
		this.convertIllegalCodePoints = convertIllegalCodePoints;
		this.warningsEnabled = warningsEnabled;
		this.gatherRtd = gatherRtd;
		this.autoCorrect = autoCorrect;
		this.langConvTagsEnabled = langConvTagsEnabled;
		this.nodeFactory = new WikitextNodeFactoryImpl(this);
		this.textUtils = new AstTextUtilsImpl(this);
	}

	// ==[ Encoding validation features ]=======================================

	@Override
	public boolean isConvertIllegalCodePoints()
	{
		return convertIllegalCodePoints;
	}

	// ==[ Parser features ]====================================================

	@Override
	public boolean isWarningsEnabled()
	{
		return warningsEnabled;
	}

	@Override
	public boolean isWarningLevelEnabled(WarningSeverity severity)
	{
		return true;
	}

	@Override
	public boolean isAutoCorrect()
	{
		return autoCorrect;
	}

	@Override
	public boolean isGatherRtData()
	{
		return gatherRtd;
	}

	// ==[ AST creation ]=======================================================

	@Override
	public WikitextNodeFactory getNodeFactory()
	{
		return nodeFactory;
	}

	@Override
	public AstTextUtils getAstTextUtils()
	{
		return textUtils;
	}

	// ==[ Link classification and parsing ]====================================

	@Override
	public boolean isUrlProtocol(String proto)
	{
		return "http://".equalsIgnoreCase(proto) ||
				"https://".equalsIgnoreCase(proto) ||
				"mail:".equalsIgnoreCase(proto);
	}

	@Override
	public String getInternalLinkPrefixPattern()
	{
		// Doesn't make that much sense, but needed for testing ...
		return "[äöüßa-z]+";
	}

	@Override
	public String getInternalLinkPostfixPattern()
	{
		return "[äöüßa-z]+";
	}

	@Override
	public LinkType classifyTarget(String target)
	{
		LinkTargetParser ltp = new LinkTargetParser();
		try
		{
			ltp.parse(this, target);
		}
		catch (LinkTargetException e)
		{
			return LinkType.INVALID;
		}

		String ns = ltp.getNamespace();
		if ("file".equalsIgnoreCase(ns) || "image".equalsIgnoreCase(ns))
			return LinkType.IMAGE;

		return LinkType.PAGE;
	}

	@Override
	public boolean isNamespace(String name)
	{
		// keep it simple ...
		return "image".equalsIgnoreCase(name) ||
				"file".equalsIgnoreCase(name) ||
				"template".equals(name) ||
				"media".equals(name) ||
				"category".equals(name);
	}

	@Override
	public boolean isTalkNamespace(String resultNs)
	{
		return resultNs.toLowerCase().equals("talk");
	}

	@Override
	public boolean isInterwikiName(String name)
	{
		return "mediawiki".equalsIgnoreCase(name);
	}

	@Override
	public boolean isIwPrefixOfThisWiki(String iwPrefix)
	{
		return false;
	}

	// ==[ Names ]==============================================================

	@Override
	public boolean isValidPageSwitchName(String word)
	{
		return "NOTOC".equalsIgnoreCase(word);
	}

	@Override
	public boolean isValidExtensionTagName(String name)
	{
		// keep it simple ...
		return "ref".equalsIgnoreCase(name) ||
				"pre".equalsIgnoreCase(name) ||
				"nowiki".equalsIgnoreCase(name) ||
				"gallery".equalsIgnoreCase(name) ||
				"includeonly".equalsIgnoreCase(name) ||
				"noinclude".equalsIgnoreCase(name) ||
				"onlyinclude".equalsIgnoreCase(name);
	}

	public boolean isRedirectKeyword(String keyword)
	{
		return keyword.equalsIgnoreCase("#redirect");
	}

	// ==[ Parsing XML elements ]===============================================

	@Override
	public boolean isValidXmlEntityRef(String name)
	{
		return true;
	}

	@Override
	public String resolveXmlEntity(String name)
	{
		// keep it simple ...
		if ("amp".equalsIgnoreCase(name))
		{
			return "&";
		}
		else if ("lt".equalsIgnoreCase(name))
		{
			return "<";
		}
		else if ("gt".equalsIgnoreCase(name))
		{
			return ">";
		}
		else if ("nbsp".equalsIgnoreCase(name))
		{
			return "\u00A0";
		}
		else if ("middot".equalsIgnoreCase(name))
		{
			return "\u00B7";
		}
		else if ("mdash".equalsIgnoreCase(name))
		{
			return "\u2014";
		}
		else if ("ndash".equalsIgnoreCase(name))
		{
			return "\u2013";
		}
		else if ("equiv".equalsIgnoreCase(name))
		{
			return "\u2261";
		}
		else
		{
			return null;
		}
	}

	@Override
	public Map<String, String> getXmlEntities()
	{
		return Collections.emptyMap();
	}

	@Override
	public NonStandardElementBehavior getNonStandardElementBehavior(
			String elementName)
	{
		return NonStandardElementBehavior.UNSPECIFIED;
	}

	@Override
	public boolean isFosterParenting()
	{
		return true;
	}

	@Override
	public boolean isFosterParentingForTransclusions()
	{
		return true;
	}

	// ==[ Language Conversion Tags ]===========================================

	private static final Set<String> knownFlags = new HashSet<String>(Arrays.asList(
			"A",
			"T",
			"R",
			"D",
			"-",
			"H",
			"N"));

	@Override
	public boolean isLangConvTagsEnabled()
	{
		return langConvTagsEnabled;
	}

	public boolean isLctFlag(String flag)
	{
		flag = normalizeLctFlag(flag);
		return knownFlags.contains(flag);
	}

	public String normalizeLctFlag(String flag)
	{
		return flag.trim().toUpperCase();
	}

	private static final Set<String> knownVariants = new HashSet<String>(Arrays.asList(
			"zh",
			"zh-hans",
			"zh-hant",
			"zh-cn",
			"zh-tw",
			"zh-hk",
			"zh-sg"));

	public boolean isLctVariant(String variant)
	{
		variant = normalizeLctVariant(variant);
		return knownVariants.contains(variant);
	}

	public String normalizeLctVariant(String variant)
	{
		return variant.trim().toLowerCase();
	}
}
