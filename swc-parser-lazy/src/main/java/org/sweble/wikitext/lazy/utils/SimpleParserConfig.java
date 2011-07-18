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

package org.sweble.wikitext.lazy.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.sweble.wikitext.lazy.LinkTargetException;
import org.sweble.wikitext.lazy.LinkTargetParser;
import org.sweble.wikitext.lazy.ParserConfigInterface;
import org.sweble.wikitext.lazy.parser.WarningSeverity;
import org.sweble.wikitext.lazy.postprocessor.ScopeType;

/**
 * This is a simple parser config that is ONLY suited for test purposes!
 */
public class SimpleParserConfig
        implements
            ParserConfigInterface
{
	private final static HashSet<String> allowed;
	
	private final static HashSet<String> emptyOnly;
	
	private final static HashMap<String, ScopeType> elementTypes;
	
	private final boolean warningsEnabled;
	
	private final boolean gatherRtd;
	
	private final boolean autoCorrect;
	
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
		this.warningsEnabled = true;
		this.gatherRtd = true;
		this.autoCorrect = false;
	}
	
	public SimpleParserConfig(
	        boolean warningsEnabled,
	        boolean gatherRtd,
	        boolean autoCorrect)
	{
		this.warningsEnabled = warningsEnabled;
		this.gatherRtd = gatherRtd;
		this.autoCorrect = autoCorrect;
	}
	
	// =========================================================================
	
	@Override
	public boolean isWarningsEnabled()
	{
		return warningsEnabled;
	}
	
	@Override
	public boolean isGatherRtData()
	{
		return gatherRtd;
	}
	
	@Override
	public boolean isAutoCorrect()
	{
		return autoCorrect;
	}
	
	@Override
	public boolean isWarningLevelEnabled(WarningSeverity severity)
	{
		return true;
	}
	
	@Override
	public boolean isValidXmlEntityRef(String name)
	{
		return true;
	}
	
	@Override
	public boolean isUrlProtocol(String proto)
	{
		return "http://".equalsIgnoreCase(proto) ||
		        "https://".equalsIgnoreCase(proto) ||
		        "mail:".equalsIgnoreCase(proto);
	}
	
	@Override
	public boolean isMagicWord(String word)
	{
		return "NOTOC".equalsIgnoreCase(word);
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
	public TargetType classifyTarget(String target)
	{
		LinkTargetParser ltp = new LinkTargetParser();
		try
		{
			ltp.parse(this, target);
		}
		catch (LinkTargetException e)
		{
			return TargetType.INVALID;
		}
		
		String ns = ltp.getNamespace();
		if ("file".equalsIgnoreCase(ns) || "image".equalsIgnoreCase(ns))
			return TargetType.IMAGE;
		
		return TargetType.PAGE;
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
	public boolean isInterwikiName(String name)
	{
		return "mediawiki".equalsIgnoreCase(name);
	}
	
	@Override
	public boolean isLocalInterwikiName(String name)
	{
		return false;
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
	
	@Override
	public String resolveXmlEntity(String name)
	{
		// keep it simple ...
		if ("amp".equalsIgnoreCase("amp"))
		{
			return "&";
		}
		else if ("lt".equalsIgnoreCase("lt"))
		{
			return "<";
		}
		else if ("nbsp".equalsIgnoreCase("nbsp"))
		{
			return "\u00A0";
		}
		else
		{
			return null;
		}
	}
	
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
}
