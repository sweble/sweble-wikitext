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

package org.sweble.wikitext.engine.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.sweble.wikitext.engine.nodes.EngineNodeFactory;
import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WikitextWarning.WarningSeverity;
import org.sweble.wikitext.parser.parser.LinkBuilder.LinkType;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wikitext.parser.parser.LinkTargetParser;
import org.sweble.wikitext.parser.postprocessor.ScopeType;
import org.sweble.wikitext.parser.utils.AstTextUtils;

import de.fau.cs.osr.utils.XmlGrammar;

@XmlRootElement(
		name = "ParserConfig",
		namespace = "org.sweble.wikitext.engine")
@XmlType(propOrder = {
		"warningsEnabled",
		"minSeverity",
		"autoCorrect",
		"gatherRtData",
		"internalLinkPrefixPattern",
		"internalLinkPostfixPattern",
		"jaxbAllowedUrlProtocols",
		"jaxbXmlElementBehavior",
		"jaxbXmlEntities" })
@XmlAccessorType(XmlAccessType.NONE)
public class ParserConfigImpl
		implements
			ParserConfig
{
	private static final Pattern URL_PROTOCOL_SYNTAX = Pattern.compile("^\\w+(:|://)$");
	
	private final WikiConfigImpl wikiConfig;
	
	private final Map<String, String> xmlEntities = new HashMap<String, String>();
	
	@XmlElement
	private boolean warningsEnabled;
	
	@XmlElement
	private WarningSeverity minSeverity;
	
	@XmlElement
	private boolean autoCorrect;
	
	@XmlElement
	private boolean gatherRtData;
	
	private final Set<String> allowedUrlProtocols = new HashSet<String>();
	
	@XmlElement
	private String internalLinkPrefixPattern;
	
	@XmlElement
	private String internalLinkPostfixPattern;
	
	private final Set<String> allowedXmlElements = new HashSet<String>();
	
	private final Set<String> emptyOnlyXmlElements = new HashSet<String>();
	
	private final Map<String, ScopeType> xmlElementTypes = new HashMap<String, ScopeType>();
	
	// =========================================================================
	
	protected ParserConfigImpl()
	{
		this(null);
	}
	
	public ParserConfigImpl(WikiConfigImpl wikiConfig)
	{
		this.wikiConfig = wikiConfig;
	}
	
	// ==[ XML Entity resolution ]==============================================
	
	public void addXmlEntity(String name, String resolvesTo)
	{
		String old = xmlEntities.get(name);
		if (old != null)
			throw new IllegalArgumentException("XML entity `" + name + "' already registered.");
		xmlEntities.put(name, resolvesTo);
	}
	
	@Override
	public String resolveXmlEntity(String name)
	{
		return xmlEntities.get(name);
	}
	
	// ==[ Parser features ]====================================================
	
	public void setWarningsEnabled(boolean warningsEnabled)
	{
		this.warningsEnabled = warningsEnabled;
	}
	
	@Override
	public boolean isWarningsEnabled()
	{
		return warningsEnabled;
	}
	
	public void setMinSeverity(WarningSeverity minSeverity)
	{
		this.minSeverity = minSeverity;
	}
	
	@Override
	public boolean isWarningLevelEnabled(WarningSeverity severity)
	{
		return severity.getLevel() >= this.minSeverity.getLevel();
	}
	
	public void setAutoCorrect(boolean autoCorrect)
	{
		this.autoCorrect = autoCorrect;
	}
	
	@Override
	public boolean isAutoCorrect()
	{
		return autoCorrect;
	}
	
	public void setGatherRtData(boolean gatherRtData)
	{
		this.gatherRtData = gatherRtData;
	}
	
	@Override
	public boolean isGatherRtData()
	{
		return gatherRtData;
	}
	
	// ==[ AST creation ]=======================================================
	
	@Override
	public EngineNodeFactory getNodeFactory()
	{
		return wikiConfig.getNodeFactory();
	}
	
	@Override
	public AstTextUtils createAstTextUtils()
	{
		return wikiConfig.createAstTextUtils();
	}
	
	// ==[ Link classification and parsing ]====================================
	
	public void addUrlProtocol(String protocol)
	{
		if (allowedUrlProtocols.contains(protocol))
			throw new IllegalArgumentException("URL Protocol `" + protocol + "' already registered.");
		if (!URL_PROTOCOL_SYNTAX.matcher(protocol).matches())
			throw new IllegalArgumentException("Invalid URL protocol syntax `" + protocol + "'.");
		allowedUrlProtocols.add(protocol);
	}
	
	@Override
	public boolean isUrlProtocol(String protocol)
	{
		return allowedUrlProtocols.contains(protocol.toLowerCase());
	}
	
	public void setInternalLinkPrefixPattern(String pat)
	{
		try
		{
			Pattern.compile("(" + pat + ")$");
		}
		catch (PatternSyntaxException e)
		{
			throw new IllegalArgumentException("Not a valid internal link prefix pattern: `" + pat + "'.", e);
		}
		this.internalLinkPrefixPattern = pat;
	}
	
	@Override
	public String getInternalLinkPrefixPattern()
	{
		return this.internalLinkPrefixPattern;
	}
	
	public void setInternalLinkPostfixPattern(String pat)
	{
		try
		{
			Pattern.compile(pat);
		}
		catch (PatternSyntaxException e)
		{
			throw new IllegalArgumentException("Not a valid internal link postfix pattern: `" + pat + "'.", e);
		}
		this.internalLinkPostfixPattern = pat;
	}
	
	@Override
	public String getInternalLinkPostfixPattern()
	{
		return this.internalLinkPostfixPattern;
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
		
		String nsStr = ltp.getNamespace();
		if (nsStr != null)
		{
			NamespaceImpl ns = this.wikiConfig.getNamespace(nsStr);
			if (ns != null && ns.isFileNs() && !ltp.isInitialColon())
				return LinkType.IMAGE;
		}
		
		return LinkType.PAGE;
	}
	
	@Override
	public boolean isNamespace(String nsName)
	{
		return this.wikiConfig.getNamespace(nsName) != null;
	}
	
	@Override
	public boolean isTalkNamespace(String nsName)
	{
		NamespaceImpl ns = this.wikiConfig.getNamespace(nsName);
		return ns != null && ns.isTalkNamespace();
	}
	
	@Override
	public boolean isInterwikiName(String iwName)
	{
		return this.wikiConfig.getInterwiki(iwName) != null;
	}
	
	@Override
	public boolean isIwPrefixOfThisWiki(String iwPrefix)
	{
		return iwPrefix.equals(this.wikiConfig.getInterwikiPrefix());
	}
	
	// ==[ Names ]==============================================================
	
	@Override
	public boolean isValidPageSwitchName(String name)
	{
		return this.wikiConfig.getPageSwitch(name) != null;
	}
	
	@Override
	public boolean isValidExtensionTagName(String name)
	{
		return this.wikiConfig.getTagExtension(name) != null;
	}
	
	// ==[ Parsing XML elements ]===============================================
	
	public void setXmlElementBehavior(
			String name,
			ScopeType scope,
			boolean emptyOnly)
	{
		if (!XmlGrammar.xmlName().matcher(name).matches())
			throw new IllegalArgumentException("Given name is not a valid XML element name: `" + name + "'.");
		
		name = name.toLowerCase();
		
		allowedXmlElements.add(name);
		
		xmlElementTypes.put(name, scope);
		
		if (emptyOnly)
			emptyOnlyXmlElements.add(name);
		else
			emptyOnlyXmlElements.remove(name);
	}
	
	@Override
	public boolean isXmlElementAllowed(String name)
	{
		return allowedXmlElements.contains(name.toLowerCase());
	}
	
	@Override
	public boolean isXmlElementEmptyOnly(String name)
	{
		return emptyOnlyXmlElements.contains(name.toLowerCase());
	}
	
	@Override
	public ScopeType getXmlElementType(String name)
	{
		return xmlElementTypes.get(name);
	}
	
	@Override
	public boolean isValidXmlEntityRef(String name)
	{
		return resolveXmlEntity(name) != null;
	}
	
	// =========================================================================
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((allowedUrlProtocols == null) ? 0 : allowedUrlProtocols.hashCode());
		result = prime * result + ((allowedXmlElements == null) ? 0 : allowedXmlElements.hashCode());
		result = prime * result + (autoCorrect ? 1231 : 1237);
		result = prime * result + ((emptyOnlyXmlElements == null) ? 0 : emptyOnlyXmlElements.hashCode());
		result = prime * result + (gatherRtData ? 1231 : 1237);
		result = prime * result + ((internalLinkPostfixPattern == null) ? 0 : internalLinkPostfixPattern.hashCode());
		result = prime * result + ((internalLinkPrefixPattern == null) ? 0 : internalLinkPrefixPattern.hashCode());
		result = prime * result + ((minSeverity == null) ? 0 : minSeverity.hashCode());
		result = prime * result + (warningsEnabled ? 1231 : 1237);
		result = prime * result + ((xmlElementTypes == null) ? 0 : xmlElementTypes.hashCode());
		result = prime * result + ((xmlEntities == null) ? 0 : xmlEntities.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParserConfigImpl other = (ParserConfigImpl) obj;
		if (allowedUrlProtocols == null)
		{
			if (other.allowedUrlProtocols != null)
				return false;
		}
		else if (!allowedUrlProtocols.equals(other.allowedUrlProtocols))
			return false;
		if (allowedXmlElements == null)
		{
			if (other.allowedXmlElements != null)
				return false;
		}
		else if (!allowedXmlElements.equals(other.allowedXmlElements))
			return false;
		if (autoCorrect != other.autoCorrect)
			return false;
		if (emptyOnlyXmlElements == null)
		{
			if (other.emptyOnlyXmlElements != null)
				return false;
		}
		else if (!emptyOnlyXmlElements.equals(other.emptyOnlyXmlElements))
			return false;
		if (gatherRtData != other.gatherRtData)
			return false;
		if (internalLinkPostfixPattern == null)
		{
			if (other.internalLinkPostfixPattern != null)
				return false;
		}
		else if (!internalLinkPostfixPattern.equals(other.internalLinkPostfixPattern))
			return false;
		if (internalLinkPrefixPattern == null)
		{
			if (other.internalLinkPrefixPattern != null)
				return false;
		}
		else if (!internalLinkPrefixPattern.equals(other.internalLinkPrefixPattern))
			return false;
		if (minSeverity != other.minSeverity)
			return false;
		if (warningsEnabled != other.warningsEnabled)
			return false;
		if (xmlElementTypes == null)
		{
			if (other.xmlElementTypes != null)
				return false;
		}
		else if (!xmlElementTypes.equals(other.xmlElementTypes))
			return false;
		if (xmlEntities == null)
		{
			if (other.xmlEntities != null)
				return false;
		}
		else if (!xmlEntities.equals(other.xmlEntities))
			return false;
		return true;
	}
	
	// =========================================================================
	
	private static final class XmlEntityMapEntry
			implements
				Comparable<XmlEntityMapEntry>
	{
		@XmlAttribute
		private String name;
		
		@XmlAttribute
		private String value;
		
		private XmlEntityMapEntry()
		{
		}
		
		private XmlEntityMapEntry(String name, String value)
		{
			this.name = name;
			this.value = value;
		}
		
		@Override
		public int compareTo(XmlEntityMapEntry o)
		{
			return name.compareTo(o.name);
		}
	}
	
	@XmlElement(name = "entity")
	@XmlElementWrapper(name = "xmlEntities")
	@SuppressWarnings("unused")
	private XmlEntityMapEntry[] getJaxbXmlEntities()
	{
		XmlEntityMapEntry[] array = new XmlEntityMapEntry[xmlEntities.size()];
		int i = 0;
		for (Entry<String, String> e : xmlEntities.entrySet())
			array[i++] = new XmlEntityMapEntry(e.getKey(), e.getValue());
		Arrays.sort(array);
		return array;
	}
	
	@SuppressWarnings("unused")
	private void setJaxbXmlEntities(XmlEntityMapEntry[] xmlEntities)
	{
		for (XmlEntityMapEntry e : xmlEntities)
			addXmlEntity(e.name, e.value);
	}
	
	// =========================================================================
	
	@SuppressWarnings("unused")
	private static final class XmlElementBehaviorEntry
			implements
				Comparable<XmlElementBehaviorEntry>
	{
		@XmlAttribute
		private String name;
		
		@XmlAttribute
		private ScopeType type;
		
		@XmlAttribute
		boolean emptyOnly;
		
		private XmlElementBehaviorEntry()
		{
		}
		
		public XmlElementBehaviorEntry(
				String name,
				ScopeType type,
				boolean emptyOnly)
		{
			super();
			this.name = name;
			this.type = type;
			this.emptyOnly = emptyOnly;
		}
		
		@Override
		public int compareTo(XmlElementBehaviorEntry o)
		{
			return name.compareTo(o.name);
		}
	}
	
	@XmlElement(name = "element")
	@XmlElementWrapper(name = "xmlElementBehavior")
	@SuppressWarnings("unused")
	private XmlElementBehaviorEntry[] getJaxbXmlElementBehavior()
	{
		XmlElementBehaviorEntry[] array = new XmlElementBehaviorEntry[xmlElementTypes.size()];
		int i = 0;
		for (Entry<String, ScopeType> e : xmlElementTypes.entrySet())
			array[i++] = new XmlElementBehaviorEntry(
					e.getKey(),
					e.getValue(),
					isXmlElementEmptyOnly(e.getKey()));
		Arrays.sort(array);
		return array;
	}
	
	@SuppressWarnings("unused")
	private void setJaxbXmlElementBehavior(
			XmlElementBehaviorEntry[] xmlElementTypes)
	{
		for (XmlElementBehaviorEntry e : xmlElementTypes)
			setXmlElementBehavior(e.name, e.type, e.emptyOnly);
	}
	
	// =========================================================================
	
	@SuppressWarnings("unused")
	private static final class UrlProtocolEntry
	{
		@XmlAttribute
		private String name;
		
		public UrlProtocolEntry()
		{
		}
		
		public UrlProtocolEntry(String name)
		{
			this.name = name;
		}
	}
	
	@XmlElement(name = "protocol")
	@XmlElementWrapper(name = "allowedUrlProtocols")
	@SuppressWarnings("unused")
	private UrlProtocolEntry[] getJaxbAllowedUrlProtocols()
	{
		UrlProtocolEntry[] array = new UrlProtocolEntry[xmlElementTypes.size()];
		int i = 0;
		for (String protocol : allowedUrlProtocols)
			array[i++] = new UrlProtocolEntry(protocol);
		return array;
	}
	
	@SuppressWarnings("unused")
	private void setJaxbAllowedUrlProtocols(UrlProtocolEntry[] protocols)
	{
		for (UrlProtocolEntry protocol : protocols)
			addUrlProtocol(protocol.name);
	}
}
