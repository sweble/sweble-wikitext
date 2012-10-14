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
import java.util.HashMap;
import java.util.HashSet;

import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WikitextWarning.WarningSeverity;
import org.sweble.wikitext.parser.nodes.WikitextNodeFactory;
import org.sweble.wikitext.parser.nodes.WikitextNodeFactoryImpl;
import org.sweble.wikitext.parser.parser.LinkBuilder.LinkType;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wikitext.parser.parser.LinkTargetParser;
import org.sweble.wikitext.parser.postprocessor.ScopeType;

/**
 * This is a simple parser config that is ONLY suited for test purposes!
 */
public class SimpleParserConfig
		implements
			ParserConfig
{
	private final static HashSet<String> allowed;
	
	private final static HashSet<String> emptyOnly;
	
	private final static HashMap<String, ScopeType> elementTypes;
	
	// =========================================================================
	
	private final boolean warningsEnabled;
	
	private final boolean gatherRtd;
	
	private final boolean autoCorrect;
	
	private final WikitextNodeFactory nodeFactory;
	
	// =========================================================================
	
	static
	{
		allowed = new HashSet<String>();
		allowed.addAll(Arrays.asList(
				"abbr", "b", "big", "blockquote", "br", "caption",
				"center", "cite", "code", "dd", "del", "dfn", "div", "dl",
				"dt", "em", "font", "h1", "h2", "h3", "h4", "h5", "h6", "hr",
				"i", "ins", "kbd", "li", "ol", "p", "pre", "s", "samp",
				"small", "span", "strike", "strong", "sub", "sup", "table",
				"td", "th", "tr", "tt", "u", "ul", "var"));
		
		emptyOnly = new HashSet<String>();
		emptyOnly.addAll(Arrays.asList(
				"area", "base", "basefont", "br", "col", "frame", "hr",
				"img", "input", "isindex", "link", "meta", "param"));
		
		elementTypes = new HashMap<String, ScopeType>();
		
		elementTypes.put("p", ScopeType.XML_PARAGRAPH);
		
		for (String e : Arrays.asList("abbr", "b", "big", "br", "cite", "code",
				"em", "font", "i", "s", "samp", "small", "span", "strike", "strong",
				"sub", "sup", "tt", "u", "var"))
			elementTypes.put(e, ScopeType.XML_INLINE);
		
		for (String e : Arrays.asList("blockquote", "center", "del", "dfn", "div",
				"h1", "h2", "h3", "h4", "h5", "h6", "hr", "ins", "kbd", "ol", "pre",
				"ul"))
			elementTypes.put(e, ScopeType.XML_BLOCK);
		
		for (String e : Arrays.asList("dd", "dl", "dt", "li"))
			elementTypes.put(e, ScopeType.XML_ITEM);
		
		for (String e : Arrays.asList("caption", "td", "tr", "th"))
			elementTypes.put(e, ScopeType.XML_TABLE_ITEM);
		
		for (String e : Arrays.asList("table"))
			elementTypes.put(e, ScopeType.XML_TABLE);
	}
	
	// =========================================================================
	
	public SimpleParserConfig()
	{
		this(true, true, false);
	}
	
	public SimpleParserConfig(
			boolean warningsEnabled,
			boolean gatherRtd,
			boolean autoCorrect)
	{
		this.warningsEnabled = warningsEnabled;
		this.gatherRtd = gatherRtd;
		this.autoCorrect = autoCorrect;
		this.nodeFactory = new WikitextNodeFactoryImpl(this);
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
	public AstTextUtils createAstTextUtils()
	{
		return new AstTextUtilsImpl(this);
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
	
	// ==[ Parsing XML elements ]===============================================
	
	@Override
	public boolean isXmlElementAllowed(String name)
	{
		return allowed.contains(name.toLowerCase());
	}
	
	@Override
	public boolean isXmlElementEmptyOnly(String name)
	{
		return emptyOnly.contains(name.toLowerCase());
	}
	
	@Override
	public ScopeType getXmlElementType(String name)
	{
		ScopeType type = elementTypes.get(name);
		if (type == null)
			return ScopeType.XML_BLOCK;
		return type;
	}
	
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
}
