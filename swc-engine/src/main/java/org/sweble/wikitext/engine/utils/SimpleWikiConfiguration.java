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

package org.sweble.wikitext.engine.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.TagExtensionBase;
import org.sweble.wikitext.engine.config.Interwiki;
import org.sweble.wikitext.engine.config.MagicWord;
import org.sweble.wikitext.engine.config.Namespace;
import org.sweble.wikitext.engine.config.WikiConfigurationInterface;
import org.sweble.wikitext.engine.ext.ParserFunctionFullPagename;
import org.sweble.wikitext.engine.ext.ParserFunctionFullurl;
import org.sweble.wikitext.engine.ext.ParserFunctionIf;
import org.sweble.wikitext.engine.ext.ParserFunctionIfError;
import org.sweble.wikitext.engine.ext.ParserFunctionIfeq;
import org.sweble.wikitext.engine.ext.ParserFunctionLc;
import org.sweble.wikitext.engine.ext.ParserFunctionNamespace;
import org.sweble.wikitext.engine.ext.ParserFunctionNs;
import org.sweble.wikitext.engine.ext.ParserFunctionSwitch;
import org.sweble.wikitext.engine.ext.TagExtensionMath;
import org.sweble.wikitext.engine.ext.TagExtensionNoWiki;
import org.sweble.wikitext.engine.ext.TagExtensionPre;
import org.sweble.wikitext.engine.ext.TagExtensionRef;
import org.sweble.wikitext.engine.ext.UnimplementedParserFunction;
import org.sweble.wikitext.lazy.LinkTargetException;
import org.sweble.wikitext.lazy.LinkTargetParser;
import org.sweble.wikitext.lazy.utils.SimpleParserConfig;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import de.fau.cs.osr.utils.FmtIllegalArgumentException;

/**
 * This is a simple wiki config that is ONLY suited for test purposes!
 */
public class SimpleWikiConfiguration
        extends
            SimpleParserConfig
        implements
            WikiConfigurationInterface
{
	private static final long serialVersionUID = 1L;
	
	private final HashMap<Integer, Namespace> namespacesById =
	        new HashMap<Integer, Namespace>();
	
	private final HashMap<String, Namespace> namespacesByAlias =
	        new HashMap<String, Namespace>();
	
	private Namespace defaultNamespace = null;
	
	private Namespace templateNamespace = null;
	
	private final HashMap<String, Interwiki> interwikiMap =
	        new HashMap<String, Interwiki>();
	
	private Interwiki localInterwiki = null;
	
	private final HashMap<String, MagicWord> magicWordsByAlias =
	        new HashMap<String, MagicWord>();
	
	private final HashMap<String, ParserFunctionBase> parserFunctions =
	        new HashMap<String, ParserFunctionBase>();
	
	private final HashMap<String, TagExtensionBase> tagExtensions =
	        new HashMap<String, TagExtensionBase>();
	
	// =========================================================================
	
	public SimpleWikiConfiguration()
	{
		staticSetup();
	}
	
	public SimpleWikiConfiguration(String xmlConfig) throws FileNotFoundException, IOException
	{
		String file = xmlConfig.trim();
		if (file.startsWith("classpath:"))
		{
			file = file.substring(10);
			InputStream in = getClass().getResourceAsStream(file);
			if (in == null)
				throw new FileNotFoundException(xmlConfig);
			
			deserialize(in);
		}
		else
		{
			deserialize(new FileInputStream(file));
		}
		
		staticSetup();
	}
	
	// =========================================================================
	
	private void staticSetup()
	{
		// ====[ Native MediaWiki Features ]====================================
		
		// Magic Words - Behavior Switches
		//__END__
		//__FORCETOC__
		//__HIDDENCAT__
		//__INDEX__
		//__NEWSECTIONLINK__
		//__NOCC__
		//__NOCONTENTCONVERT__
		//__NOEDITSECTION__
		//__NOGALLERY__
		//__NOINDEX__
		//__NONEWSECTIONLINK__
		//__NOTC__
		//__NOTITLECONVERT__
		//__NOTOC__
		//__START__
		//__STATICREDIRECT__
		//__TOC__
		
		// Variables - Date and Time
		addParserFunction(new UnimplementedParserFunction("CURRENTDAY2"));
		addParserFunction(new UnimplementedParserFunction("CURRENTDAYNAME"));
		addParserFunction(new UnimplementedParserFunction("CURRENTDAY"));
		addParserFunction(new UnimplementedParserFunction("CURRENTDOW"));
		addParserFunction(new UnimplementedParserFunction("CURRENTHOUR"));
		addParserFunction(new UnimplementedParserFunction("CURRENTMONTHABBREV"));
		addParserFunction(new UnimplementedParserFunction("CURRENTMONTHNAMEGEN"));
		addParserFunction(new UnimplementedParserFunction("CURRENTMONTHNAME"));
		addParserFunction(new UnimplementedParserFunction("CURRENTMONTH"));
		addParserFunction(new UnimplementedParserFunction("CURRENTTIMESTAMP"));
		addParserFunction(new UnimplementedParserFunction("CURRENTTIME"));
		addParserFunction(new UnimplementedParserFunction("CURRENTWEEK"));
		addParserFunction(new UnimplementedParserFunction("CURRENTYEAR"));
		addParserFunction(new UnimplementedParserFunction("LOCALDAY2"));
		addParserFunction(new UnimplementedParserFunction("LOCALDAYNAME"));
		addParserFunction(new UnimplementedParserFunction("LOCALDAY"));
		addParserFunction(new UnimplementedParserFunction("LOCALDOW"));
		addParserFunction(new UnimplementedParserFunction("LOCALHOUR"));
		addParserFunction(new UnimplementedParserFunction("LOCALMONTHABBREV"));
		addParserFunction(new UnimplementedParserFunction("LOCALMONTHNAMEGEN"));
		addParserFunction(new UnimplementedParserFunction("LOCALMONTHNAME"));
		addParserFunction(new UnimplementedParserFunction("LOCALMONTH"));
		addParserFunction(new UnimplementedParserFunction("LOCALTIMESTAMP"));
		addParserFunction(new UnimplementedParserFunction("LOCALTIME"));
		addParserFunction(new UnimplementedParserFunction("LOCALWEEK"));
		addParserFunction(new UnimplementedParserFunction("LOCALYEAR"));
		
		// Variables - Page Names
		addParserFunction(new UnimplementedParserFunction("BASEPAGENAMEE"));
		addParserFunction(new UnimplementedParserFunction("BASEPAGENAME"));
		//addParserFunction(new ParserFunctionBasePagename());
		addParserFunction(new UnimplementedParserFunction("FULLPAGENAMEE"));
		addParserFunction(new ParserFunctionFullPagename());
		addParserFunction(new UnimplementedParserFunction("PAGENAMEE"));
		addParserFunction(new UnimplementedParserFunction("PAGENAME"));
		addParserFunction(new UnimplementedParserFunction("SUBJECTPAGENAMEE"));
		//addParserFunction(new ParserFunctionSubPagename());
		addParserFunction(new UnimplementedParserFunction("SUBJECTPAGENAME"));
		addParserFunction(new UnimplementedParserFunction("SUBPAGENAMEE"));
		addParserFunction(new UnimplementedParserFunction("SUBPAGENAME"));
		addParserFunction(new UnimplementedParserFunction("TALKPAGENAMEE"));
		addParserFunction(new UnimplementedParserFunction("TALKPAGENAME"));
		
		// Variables - Namespaces
		addParserFunction(new UnimplementedParserFunction("ARTICLESPACE"));
		addParserFunction(new UnimplementedParserFunction("NAMESPACEE"));
		addParserFunction(new ParserFunctionNamespace());
		addParserFunction(new UnimplementedParserFunction("SUBJECTSPACEE"));
		addParserFunction(new UnimplementedParserFunction("SUBJECTSPACE"));
		addParserFunction(new UnimplementedParserFunction("TALKSPACEE"));
		addParserFunction(new UnimplementedParserFunction("TALKSPACE"));
		
		// Variables - Statistics
		addParserFunction(new UnimplementedParserFunction("NUMBERINGROUP"));
		addParserFunction(new UnimplementedParserFunction("NUMBEROFACTIVEUSERS"));
		addParserFunction(new UnimplementedParserFunction("NUMBEROFADMINS"));
		addParserFunction(new UnimplementedParserFunction("NUMBEROFARTICLES"));
		addParserFunction(new UnimplementedParserFunction("NUMBEROFEDITS"));
		addParserFunction(new UnimplementedParserFunction("NUMBEROFFILES"));
		addParserFunction(new UnimplementedParserFunction("NUMBEROFPAGES"));
		addParserFunction(new UnimplementedParserFunction("NUMBEROFUSERS"));
		addParserFunction(new UnimplementedParserFunction("NUMBEROFVIEWS"));
		addParserFunction(new UnimplementedParserFunction("NUMINGROUP"));
		addParserFunction(new UnimplementedParserFunction("PAGESINCAT"));
		addParserFunction(new UnimplementedParserFunction("PAGESINCATEGORY"));
		addParserFunction(new UnimplementedParserFunction("PAGESINNAMESPACE"));
		addParserFunction(new UnimplementedParserFunction("PAGESINNS"));
		addParserFunction(new UnimplementedParserFunction("PAGESIZE"));
		
		// Variables - Technical Metadata
		addParserFunction(new UnimplementedParserFunction("CONTENTLANGUAGE"));
		addParserFunction(new UnimplementedParserFunction("CONTENTLANG"));
		addParserFunction(new UnimplementedParserFunction("CURRENTVERSION"));
		addParserFunction(new UnimplementedParserFunction("DEFAULTCATEGORYSORT"));
		addParserFunction(new UnimplementedParserFunction("DEFAULTSORT"));
		addParserFunction(new UnimplementedParserFunction("DEFAULTSORTKEY"));
		addParserFunction(new UnimplementedParserFunction("DIRECTIONMARK"));
		addParserFunction(new UnimplementedParserFunction("DIRMARK"));
		addParserFunction(new UnimplementedParserFunction("DISPLAYTITLE"));
		addParserFunction(new UnimplementedParserFunction("PROTECTIONLEVEL"));
		addParserFunction(new UnimplementedParserFunction("REVISIONDAY2"));
		addParserFunction(new UnimplementedParserFunction("REVISIONDAY"));
		addParserFunction(new UnimplementedParserFunction("REVISIONID"));
		addParserFunction(new UnimplementedParserFunction("REVISIONMONTH"));
		addParserFunction(new UnimplementedParserFunction("REVISIONTIMESTAMP"));
		addParserFunction(new UnimplementedParserFunction("REVISIONUSER"));
		addParserFunction(new UnimplementedParserFunction("REVISIONYEAR"));
		addParserFunction(new UnimplementedParserFunction("SCRIPTPATH"));
		addParserFunction(new UnimplementedParserFunction("SERVERNAME"));
		addParserFunction(new UnimplementedParserFunction("SERVER"));
		addParserFunction(new UnimplementedParserFunction("SITENAME"));
		addParserFunction(new UnimplementedParserFunction("STYLEPATH"));
		
		// Parser Functions - Namespaces
		addParserFunction(new ParserFunctionNs());
		addParserFunction(new UnimplementedParserFunction("nse"));
		
		// Parser Functions - Formatting
		addParserFunction(new UnimplementedParserFunction("#dateformat"));
		addParserFunction(new UnimplementedParserFunction("#formatdate"));
		addParserFunction(new UnimplementedParserFunction("formatnum"));
		addParserFunction(new UnimplementedParserFunction("grammar"));
		addParserFunction(new ParserFunctionLc());
		addParserFunction(new UnimplementedParserFunction("lcfirst"));
		addParserFunction(new UnimplementedParserFunction("padleft"));
		addParserFunction(new UnimplementedParserFunction("padright"));
		addParserFunction(new UnimplementedParserFunction("plural"));
		addParserFunction(new UnimplementedParserFunction("uc"));
		addParserFunction(new UnimplementedParserFunction("ucfirst"));
		
		// Parser Functions - URL data
		addParserFunction(new UnimplementedParserFunction("anchorencode"));
		addParserFunction(new UnimplementedParserFunction("filepath"));
		addParserFunction(new ParserFunctionFullurl());
		addParserFunction(new UnimplementedParserFunction("localurl"));
		addParserFunction(new UnimplementedParserFunction("urlencode"));
		
		// Parser Functions - Miescellaneous
		addParserFunction(new UnimplementedParserFunction("#language"));
		addParserFunction(new UnimplementedParserFunction("#special"));
		addParserFunction(new UnimplementedParserFunction("#tag"));
		addParserFunction(new UnimplementedParserFunction("int"));
		
		// ====[ Extensions ]===================================================
		
		// Extension - Parser Functions
		addParserFunction(new UnimplementedParserFunction("#expr"));
		addParserFunction(new ParserFunctionIf());
		addParserFunction(new ParserFunctionIfeq());
		addParserFunction(new ParserFunctionIfError());
		addParserFunction(new UnimplementedParserFunction("#ifexpr"));
		addParserFunction(new UnimplementedParserFunction("#ifexist"));
		addParserFunction(new UnimplementedParserFunction("#rel2abs"));
		addParserFunction(new ParserFunctionSwitch());
		addParserFunction(new UnimplementedParserFunction("#time"));
		addParserFunction(new UnimplementedParserFunction("#timel"));
		addParserFunction(new UnimplementedParserFunction("#titleparts"));
		
		// Tag Extensions
		addTagExtension(new TagExtensionMath());
		addTagExtension(new TagExtensionNoWiki());
		addTagExtension(new TagExtensionPre());
		addTagExtension(new TagExtensionRef());
	}
	
	// =========================================================================
	
	public Interwiki addInterwiki(Interwiki interwiki)
	{
		if (interwikiMap.containsKey(interwiki.getPrefix()))
			throw new FmtIllegalArgumentException(
			        "An interwiki with prefix `%s' already exists",
			        interwiki.getPrefix());
		
		interwikiMap.put(interwiki.getPrefix(), interwiki);
		
		return interwiki;
	}
	
	public void setLocalInterwiki(Interwiki localInterwiki)
	{
		this.localInterwiki = localInterwiki;
	}
	
	@Override
	public Interwiki getInterwiki(String prefix)
	{
		return interwikiMap.get(prefix);
	}
	
	@Override
	public Interwiki getLocalInterwiki()
	{
		return localInterwiki;
	}
	
	// =========================================================================
	
	// TODO: Should namespace comparisons be case insensitive?
	public Namespace addNamespace(Namespace ns)
	{
		if (namespacesById.containsKey(ns.getId()))
			throw new FmtIllegalArgumentException(
			        "A namespace with id `%2$d' already exists: `%1$s'",
			        ns.getName(),
			        ns.getId());
		
		if (namespacesByAlias.containsKey(ns.getName().toLowerCase()))
			throw new FmtIllegalArgumentException(
			        "A namespace with name `%1$s' already exists",
			        ns.getName());
		
		for (String alias : ns.getAliases())
			if (namespacesByAlias.containsKey(alias.toLowerCase()))
				throw new FmtIllegalArgumentException(
				        "A namespace with alias `%2$s' already exists: `%1$s'",
				        ns.getName(),
				        alias);
		
		namespacesById.put(ns.getId(), ns);
		
		namespacesByAlias.put(ns.getName().toLowerCase(), ns);
		
		for (String alias : ns.getAliases())
			namespacesByAlias.put(alias.toLowerCase(), ns);
		
		return ns;
	}
	
	public boolean isDefaultNamespaceSet()
	{
		return defaultNamespace != null;
	}
	
	public void setDefaultNamespace(Namespace defaultNamespace)
	{
		this.defaultNamespace = defaultNamespace;
	}
	
	public void setTemplateNamespace(Namespace templateNamespace)
	{
		this.templateNamespace = templateNamespace;
	}
	
	@Override
	public Namespace getNamespace(String name)
	{
		return namespacesByAlias.get(name.toLowerCase());
	}
	
	@Override
	public Namespace getNamespace(int id)
	{
		return namespacesById.get(id);
	}
	
	@Override
	public Namespace getDefaultNamespace()
	{
		if (defaultNamespace == null)
			throw new UnsupportedOperationException(
			            "The default namespace is not set");
		
		return defaultNamespace;
	}
	
	@Override
	public Namespace getTemplateNamespace()
	{
		if (templateNamespace == null)
			throw new UnsupportedOperationException(
			            "The template namespace is not set");
		
		return templateNamespace;
	}
	
	// =========================================================================
	
	public MagicWord addMagicWord(MagicWord magicWord)
	{
		/*
		if (magicWordsByAlias.containsKey(magicWord.getName()))
			throw new FormattedIllegalArgumentException(
			        "A magic word with name `%s' already exists",
			        magicWord.getName());
		
		for (String alias : magicWord.getAliases())
			if (magicWordsByAlias.containsKey(alias))
				throw new FormattedIllegalArgumentException(
				        "A magic word with alias `%2$s' already exists: `%1$s'",
				        magicWord.getName(),
				        alias);
		*/

		// FIXME: Is lower case comparison the right way for magic words?
		//        See also: getMagicWord()
		magicWordsByAlias.put(magicWord.getName().toLowerCase(), magicWord);
		
		// FIXME: Dito.
		for (String alias : magicWord.getAliases())
			magicWordsByAlias.put(alias.toLowerCase(), magicWord);
		
		return magicWord;
	}
	
	@Override
	public MagicWord getMagicWord(String name)
	{
		// FIXME: Is lower case comparison the right way for magic words?
		return magicWordsByAlias.get(name.toLowerCase());
	}
	
	// =========================================================================
	
	public ParserFunctionBase addParserFunction(ParserFunctionBase pfn)
	{
		if (parserFunctions.containsKey(pfn.getName()))
			throw new FmtIllegalArgumentException(
			        "A parser function with name `%s' already exists",
			        pfn.getName());
		
		parserFunctions.put(pfn.getName().toLowerCase(), pfn);
		
		return pfn;
	}
	
	@Override
	public ParserFunctionBase getParserFunction(String name)
	{
		return parserFunctions.get(name.toLowerCase());
	}
	
	// =========================================================================
	
	public TagExtensionBase addTagExtension(TagExtensionBase te)
	{
		if (tagExtensions.containsKey(te.getName()))
			throw new FmtIllegalArgumentException(
			        "A tag extensions with name `%s' already exists",
			        te.getName());
		
		tagExtensions.put(te.getName(), te);
		
		return te;
	}
	
	@Override
	public TagExtensionBase getTagExtension(String name)
	{
		return tagExtensions.get(name);
	}
	
	@Override
	public Collection<TagExtensionBase> getTagExtensions()
	{
		return tagExtensions.values();
	}
	
	// =========================================================================
	
	@Override
	public HashSet<String> getAllowedHtmlTags()
	{
		return new HashSet<String>(Arrays.asList(
		        "abbr", "b", "big", "blockquote", "br", "caption",
		        "center", "cite", "code", "dd", "del", "div", "dl", "dt",
		        "em", "font", "h1", "h2", "h3", "h4", "h5", "h6", "hr",
		        "i", "ins", "li", "ol", "p", "pre", "rb", "rp", "rt",
		        "ruby", "s", "small", "span", "strike", "strong", "sub",
		        "sup", "table", "td", "th", "tr", "tt", "u", "ul", "var"));
	}
	
	@Override
	public HashSet<String> getEmptyOnlyHtmlTags()
	{
		return new HashSet<String>(Arrays.asList(
		        "area", "base", "basefont", "br", "col", "frame", "hr",
		        "img", "input", "isindex", "link", "meta", "param"));
	}
	
	@Override
	public HashSet<String> getPropagatableHtmlTags()
	{
		return new HashSet<String>(Arrays.asList(
		        "b", "big", "del", "em", "i", "ins", "strong", "s",
		        "small", "span", "strike", "strong", "sub", "sup", "tt",
		        "u"));
	}
	
	// =========================================================================
	
	@Override
	public String getWikiUrl()
	{
		// FIXME: Properly implement this method
		return "http://localhost/wiki";
	}
	
	// =========================================================================
	
	@Override
	public boolean isValidXmlEntityRef(String name)
	{
		return EntityReferences.resolve(name) != null;
	}
	
	@Override
	public boolean isMagicWord(String word)
	{
		return getMagicWord(word) != null;
	}
	
	@Override
	public String getInternalLinkPrefixPattern()
	{
		return "";
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
		if (ns != null)
		{
			Namespace ns2 = getNamespace(ns);
			if (ns2.isFileNs())
				return TargetType.IMAGE;
		}
		
		return TargetType.PAGE;
	}
	
	@Override
	public boolean isNamespace(String nsName)
	{
		return getNamespace(nsName) != null;
	}
	
	@Override
	public boolean isInterwikiName(String iwName)
	{
		return getInterwiki(iwName) != null;
	}
	
	@Override
	public boolean isLocalInterwikiName(String iwName)
	{
		Interwiki iw = getInterwiki(iwName);
		if (iw == null)
			throw new FmtIllegalArgumentException(
			        "Unknown interwiki prefix: `%s'", iwName);
		return iw.isLocal();
	}
	
	@Override
	public boolean isValidExtensionTagName(String name)
	{
		return getTagExtension(name) != null;
	}
	
	@Override
	public String resolveXmlEntity(String name)
	{
		return EntityReferences.resolve(name);
	}
	
	// =========================================================================
	
	public String serialize()
	{
		Serialized serialized = new Serialized();
		
		serialized.namespaces =
		        new ArrayList<Namespace>(this.namespacesById.values());
		
		serialized.defaultNamespace = this.defaultNamespace;
		
		serialized.templateNamespace = this.templateNamespace;
		
		serialized.interwikiLinks =
		        new ArrayList<Interwiki>(this.interwikiMap.values());
		
		serialized.localInterwiki = this.localInterwiki;
		
		serialized.magicWords = new ArrayList<MagicWord>(
		        // remove duplicates
		        new HashSet<MagicWord>(this.magicWordsByAlias.values()));
		
		XStream xstream = setUpXStream();
		
		return xstream.toXML(serialized);
	}
	
	public void deserialize(InputStream xml) throws IOException
	{
		deserialize(IOUtils.toString(xml));
	}
	
	public void deserialize(String xml)
	{
		XStream xstream = setUpXStream();
		
		Serialized deserialized = (Serialized) xstream.fromXML(xml);
		
		for (Namespace namespace : deserialized.namespaces)
			addNamespace(namespace);
		
		this.defaultNamespace = deserialized.defaultNamespace;
		
		this.templateNamespace = deserialized.templateNamespace;
		
		for (Interwiki interwiki : deserialized.interwikiLinks)
			addInterwiki(interwiki);
		
		this.localInterwiki = deserialized.localInterwiki;
		
		for (MagicWord magicWord : deserialized.magicWords)
			addMagicWord(magicWord);
	}
	
	private XStream setUpXStream()
	{
		XStream xstream = new XStream(new DomDriver());
		
		xstream.alias("MediaWikiConfiguration", Serialized.class);
		xstream.alias("Namespace", Namespace.class);
		xstream.alias("Interwiki", Interwiki.class);
		xstream.alias("MagicWord", MagicWord.class);
		
		return xstream;
	}
	
	private static final class Serialized
	{
		public List<Namespace> namespaces;
		
		public Namespace defaultNamespace = null;
		
		public Namespace templateNamespace = null;
		
		public List<Interwiki> interwikiLinks;
		
		public Interwiki localInterwiki = null;
		
		public List<MagicWord> magicWords;
	}
}
