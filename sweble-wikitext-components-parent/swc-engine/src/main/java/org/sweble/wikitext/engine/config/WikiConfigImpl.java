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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;

import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.TagExtensionBase;
import org.sweble.wikitext.engine.nodes.EngineNodeFactoryImpl;
import org.sweble.wikitext.engine.utils.EngineAstTextUtils;
import org.sweble.wikitext.engine.utils.EngineAstTextUtilsImpl;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

@XmlRootElement(
		name = "WikiConfig",
		namespace = "org.sweble.wikitext.engine")
@XmlType(propOrder = {
		"siteName",
		"wikiUrl",
		"contentLang",
		"iwPrefix",
		"jaxbNamespaces",
		"jaxbInterwikis",
		"jaxbAliases",
		"jaxbPfnGroups",
		"jaxbTagExtGroups",
		"parserConfig",
		"engineConfig" })
@XmlAccessorType(XmlAccessType.NONE)
public class WikiConfigImpl
		implements
			WikiConfig
{

	@XmlElement()
	private final ParserConfigImpl parserConfig;

	@XmlElement()
	private final EngineConfigImpl engineConfig;

	// -- AST generation/processing --

	private EngineNodeFactoryImpl nodeFactory;

	private EngineAstTextUtilsImpl textUtils;

	// -- General Information --

	@XmlElement()
	private String siteName;

	@XmlElement()
	private String wikiUrl;

	@XmlElement()
	private String contentLang;

	@XmlElement()
	private String iwPrefix;

	// -- Aliases --

	private final Map<String, I18nAliasImpl> aliasesById = new HashMap<String, I18nAliasImpl>();

	/** Keys are lower-case for case-insensitive lookups. */
	private transient final Map<String, I18nAliasImpl> nameToAliasMap = new HashMap<String, I18nAliasImpl>();

	// -- Parser Functions --

	private final Map<String, ParserFunctionGroup> pfnGroups = new HashMap<String, ParserFunctionGroup>();

	private transient final Map<String, ParserFunctionBase> parserFunctions = new HashMap<String, ParserFunctionBase>();

	private transient final Map<I18nAliasImpl, ParserFunctionBase> aliasToPfnMap = new HashMap<I18nAliasImpl, ParserFunctionBase>();

	// -- Tag Extensions --

	private final Map<String, TagExtensionGroup> tagExtGroups = new HashMap<String, TagExtensionGroup>();

	private transient final Map<String, TagExtensionBase> tagExtensions = new HashMap<String, TagExtensionBase>();

	// -- Interwikis --

	private final Map<String, InterwikiImpl> prefixToInterwikiMap = new HashMap<String, InterwikiImpl>();

	// -- Namespaces --

	private final Map<Integer, NamespaceImpl> namespaceById = new HashMap<Integer, NamespaceImpl>();

	/** Keys are lower-case for case-insensitive lookups. */
	private transient final Map<String, NamespaceImpl> namespaceByName = new HashMap<String, NamespaceImpl>();

	private NamespaceImpl templateNamespace;

	private NamespaceImpl defaultNamespace;

	// -- Runtime information --

	private WikiRuntimeInfo runtimeInfo;

	// =========================================================================

	public WikiConfigImpl()
	{
		this.parserConfig = new ParserConfigImpl(this);
		this.nodeFactory = new EngineNodeFactoryImpl(this.parserConfig);
		this.textUtils = new EngineAstTextUtilsImpl(this.parserConfig);
		this.runtimeInfo = new WikiRuntimeInfoImpl(this);
		this.engineConfig = new EngineConfigImpl();
	}

	// ==[ Parser Configuration ]===============================================

	@Override
	public ParserConfigImpl getParserConfig()
	{
		return parserConfig;
	}

	// ==[ Engine Configuration ]===============================================

	public EngineConfigImpl getEngineConfig()
	{
		return engineConfig;
	}

	// ==[ AST creation/processing ]============================================

	public EngineNodeFactoryImpl getNodeFactory()
	{
		return nodeFactory;
	}

	@Override
	public EngineAstTextUtils getAstTextUtils()
	{
		return textUtils;
	}

	// ==[ Namespaces ]=========================================================

	public void addNamespace(NamespaceImpl ns)
	{
		NamespaceImpl old = namespaceById.get(ns.getId());

		if (old == ns)
			throw new IllegalArgumentException("The namespace with id `" + ns.getId() + "' is already registered.");

		if (old != null)
			throw new IllegalArgumentException("A namespace with the same id `" + ns.getId() + "' is already registered.");

		ArrayList<String> names = new ArrayList<String>(ns.getAliases().size() + 2);
		for (String name : ns.getAliases())
			names.add(name.toLowerCase());
		names.add(ns.getName().toLowerCase());
		names.add(ns.getCanonical().toLowerCase());

		for (String name : names)
		{
			old = namespaceByName.get(name);

			// old == ns would have been caught by the id search above

			if (old != null)
				throw new IllegalArgumentException("Another namespace already registered the name `" + name + "'.");
		}

		namespaceById.put(ns.getId(), ns);
		for (String name : names)
			namespaceByName.put(name, ns);
	}

	public void setDefaultNamespace(NamespaceImpl defaultNamespace)
	{
		if (this.namespaceById.get(defaultNamespace.getId()) != defaultNamespace)
			throw new IllegalArgumentException("Given namespace unknown in this configuration");

		this.defaultNamespace = defaultNamespace;
	}

	public void setTemplateNamespace(NamespaceImpl templateNamespace)
	{
		if (this.namespaceById.get(templateNamespace.getId()) != templateNamespace)
			throw new IllegalArgumentException("Given namespace unknown in this configuration");

		this.templateNamespace = templateNamespace;
	}

	@Override
	public NamespaceImpl getNamespace(String name)
	{
		return namespaceByName.get(name.toLowerCase());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Collection<Namespace> getNamespaces()
	{
		return (Collection) Collections.unmodifiableCollection(namespaceById.values());
	}

	@Override
	public NamespaceImpl getNamespace(int id)
	{
		return namespaceById.get(id);
	}

	@Override
	public NamespaceImpl getDefaultNamespace()
	{
		return defaultNamespace;
	}

	@Override
	public NamespaceImpl getTemplateNamespace()
	{
		return templateNamespace;
	}

	@Override
	public Namespace getFileNamespace()
	{
		return getNamespace("File");
	}

	@Override
	public Namespace getSubjectNamespaceFor(Namespace namespace)
	{
		if (namespace.isSubjectNamespace())
			return namespace;
		return getNamespace(namespace.getSubjectspaceId());
	}

	@Override
	public Namespace getTalkNamespaceFor(Namespace namespace)
	{
		if (namespace.isTalkNamespace())
			return namespace;
		return getNamespace(namespace.getTalkspaceId());
	}

	// ==[ Known Wikis ]========================================================

	public void addInterwiki(InterwikiImpl iw)
	{
		InterwikiImpl old = prefixToInterwikiMap.get(iw.getPrefix());

		if (old == iw)
			throw new IllegalArgumentException("The wiki with interwiki prefix `" + iw.getPrefix() + "' is already registered.");

		if (old != null)
			throw new IllegalArgumentException("A wiki with the same interwiki prefix `" + iw.getPrefix() + "' is already registered.");

		prefixToInterwikiMap.put(iw.getPrefix(), iw);
	}

	@Override
	public InterwikiImpl getInterwiki(String prefix)
	{
		return prefixToInterwikiMap.get(prefix);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection<Interwiki> getInterwikis()
	{
		return (Collection) Collections.unmodifiableCollection(prefixToInterwikiMap.values());
	}

	// ==[ Internationalization ]===============================================

	/**
	 * Aliases apply to the following things:
	 * <ul>
	 * <li>Page switches, e.g. {@code __NOTOC__}. They can be queried using
	 * getPageSwitch(). The full name (e.g. " {@code __NOTOC__}") has to be
	 * specified as alias when specifying an alias as well as when querying the
	 * page switch in the expansion process.</li>
	 * <li>Parser functions, e.g. {@code lc:}. They can be queried using
	 * getParserFunction(). The full name plus the colon (e.g. " {@code lc:}")
	 * has to be specified as alias when specifying an alias as well as when
	 * querying the parser function in the expansion process. A parser function
	 * that can also be called without arguments is treated as magic word
	 * instead (e.g. {@code NAMESPACE} instead of {@code NAMESPACE:})!</li>
	 * <li>Magic words, e.g. {@code CURRENTDAY}. They can be queried using
	 * getParserFunction(). The full name (e.g. " {@code CURRENTDAY}") has to be
	 * specified as alias when specifying an alias as well as when querying the
	 * magic word in the expansion process.</li>
	 * <li>Redirect keyword</li>
	 * </ul>
	 */
	public void addI18nAlias(I18nAliasImpl alias)
	{
		I18nAliasImpl old = aliasesById.get(alias.getId());

		if (old == alias || (old != null && old.equals(alias)))
			throw new IllegalArgumentException("This alias is already registered: " + alias.getId());

		if (old != null)
			throw new IllegalArgumentException("An alias with the same id `" + alias.getId() + "' is already registered.");

		for (String a : alias.getAliases())
		{
			String lcAlias = a.toLowerCase();
			I18nAliasImpl old2 = nameToAliasMap.get(lcAlias);

			if (old2 == alias)
				throw new IllegalArgumentException("This alias (`" + alias.getId() + "') registeres the name `" + a + "' twice.");

			if (old2 != null)
				throw new IllegalArgumentException("The name `" + a + "' was already registered by the alias `" + old2.getId() + "' when trying to register it for alias `" + alias.getId() + "'.");

			nameToAliasMap.put(lcAlias, alias);
		}

		aliasesById.put(alias.getId(), alias);
	}

	@Override
	public I18nAliasImpl getI18nAlias(String name)
	{
		if (name == null)
			throw new NullPointerException();
		I18nAliasImpl alias = nameToAliasMap.get(name.toLowerCase());
		if (alias != null && alias.isCaseSensitive() && !alias.getAliases().contains(name))
			alias = null;
		return alias;
	}

	public I18nAliasImpl getI18nAliasById(String id)
	{
		return aliasesById.get(id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection<I18nAlias> getI18nAliases()
	{
		return (Collection) Collections.unmodifiableCollection(aliasesById.values());
	}

	// ==[ Tag extensions, parser functions and page switches ]=================

	public void addParserFunctionGroup(ParserFunctionGroup pfnGroup)
	{
		ParserFunctionGroup old = pfnGroups.get(pfnGroup.getName());

		if (old == pfnGroup)
			throw new IllegalArgumentException("The parser function group `" + pfnGroup.getName() + "' is already registered.");

		if (old != null)
			throw new IllegalArgumentException("A parser function group with the same name `" + pfnGroup.getName() + "' is already registered.");

		for (ParserFunctionBase pfn : pfnGroup.getParserFunctions())
			addParserFunction(pfn);

		this.pfnGroups.put(pfnGroup.getName(), pfnGroup);
	}

	protected void addParserFunction(ParserFunctionBase pfn)
	{
		ParserFunctionBase old = parserFunctions.get(pfn.getId());

		if (old == pfn)
			throw new IllegalArgumentException("The parser function `" + pfn.getId() + "' is already registered.");

		if (old != null)
			throw new IllegalArgumentException("A parser function with the same id `" + pfn.getId() + "' is already registered.");

		parserFunctions.put(pfn.getId(), pfn);

		I18nAliasImpl alias = aliasesById.get(pfn.getId());
		if (alias == null)
			throw new IllegalArgumentException("No alias registered for parser function `" + pfn.getId() + "'.");

		if (aliasToPfnMap.put(alias, pfn) != null)
			throw new InternalError("Alias collision should not be possible...");
	}

	@Override
	public Collection<ParserFunctionBase> getParserFunctions()
	{
		return Collections.unmodifiableCollection(parserFunctions.values());
	}

	@Override
	public ParserFunctionBase getParserFunction(String name)
	{
		I18nAliasImpl alias = getI18nAlias(name);
		if (alias == null)
			return null;
		ParserFunctionBase pfn = aliasToPfnMap.get(alias);
		if (pfn != null && pfn.isPageSwitch())
			return null;
		return pfn;
	}

	@Override
	public ParserFunctionBase getPageSwitch(String name)
	{
		I18nAliasImpl alias = getI18nAlias(name);
		if (alias == null)
			return null;
		ParserFunctionBase pfn = aliasToPfnMap.get(alias);
		if (pfn != null && !pfn.isPageSwitch())
			return null;
		return pfn;
	}

	// --------

	public void addTagExtensionGroup(TagExtensionGroup tagExtGroup)
	{
		TagExtensionGroup old = tagExtGroups.get(tagExtGroup.getName());

		if (old == tagExtGroup)
			throw new IllegalArgumentException("The tag extension group `" + tagExtGroup.getName() + "' is already registered.");

		if (old != null)
			throw new IllegalArgumentException("A tag extension group with the same name `" + tagExtGroup.getName() + "' is already registered.");

		for (TagExtensionBase tagExt : tagExtGroup.getTagExtensions())
			addTagExtension(tagExt);

		this.tagExtGroups.put(tagExtGroup.getName(), tagExtGroup);
	}

	protected void addTagExtension(TagExtensionBase tagExt)
	{
		TagExtensionBase old = tagExtensions.get(tagExt.getId());

		if (old == tagExt)
			throw new IllegalArgumentException("The tag extension `" + tagExt.getId() + "' is already registered.");

		if (old != null)
			throw new IllegalArgumentException("A tag extension with the same id `" + tagExt.getId() + "' is already registered.");

		tagExtensions.put(tagExt.getId(), tagExt);
	}

	@Override
	public Collection<TagExtensionBase> getTagExtensions()
	{
		return Collections.unmodifiableCollection(tagExtensions.values());
	}

	@Override
	public TagExtensionBase getTagExtension(String name)
	{
		return tagExtensions.get(name);
	}

	// ==[ Properties of the wiki instance ]====================================

	public void setSiteName(String siteName)
	{
		this.siteName = siteName;
	}

	@Override
	public String getSiteName()
	{
		return this.siteName;
	}

	public void setWikiUrl(String wikiUrl)
	{
		this.wikiUrl = wikiUrl;
	}

	@Override
	public String getWikiUrl()
	{
		return this.wikiUrl;
	}

	@Override
	public String getArticlePath()
	{
		return getWikiUrl() + "?title=$1";
	}

	public void setContentLang(String contentLang)
	{
		this.contentLang = contentLang;
	}

	@Override
	public String getContentLanguage()
	{
		return contentLang;
	}

	public void setIwPrefix(String iwPrefix)
	{
		this.iwPrefix = iwPrefix;
	}

	@Override
	public String getInterwikiPrefix()
	{
		return iwPrefix;
	}

	@Override
	public TimeZone getTimezone()
	{
		// TODO: Make variable and save to / read from XML
		return TimeZone.getDefault();
	}

	// ==[ Runtime information ]================================================

	@Override
	public WikiRuntimeInfo getRuntimeInfo()
	{
		return runtimeInfo;
	}

	public void setRuntimeInfo(WikiRuntimeInfo runtimeInfo)
	{
		this.runtimeInfo = runtimeInfo;
	}

	// =========================================================================

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aliasesById == null) ? 0 : aliasesById.hashCode());
		result = prime * result + ((engineConfig == null) ? 0 : engineConfig.hashCode());
		result = prime * result + ((contentLang == null) ? 0 : contentLang.hashCode());
		result = prime * result + ((defaultNamespace == null) ? 0 : defaultNamespace.hashCode());
		result = prime * result + ((iwPrefix == null) ? 0 : iwPrefix.hashCode());
		result = prime * result + ((namespaceById == null) ? 0 : namespaceById.hashCode());
		result = prime * result + ((parserConfig == null) ? 0 : parserConfig.hashCode());
		result = prime * result + ((pfnGroups == null) ? 0 : pfnGroups.hashCode());
		result = prime * result + ((prefixToInterwikiMap == null) ? 0 : prefixToInterwikiMap.hashCode());
		result = prime * result + ((tagExtGroups == null) ? 0 : tagExtGroups.hashCode());
		result = prime * result + ((templateNamespace == null) ? 0 : templateNamespace.hashCode());
		result = prime * result + ((wikiUrl == null) ? 0 : wikiUrl.hashCode());
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
		WikiConfigImpl other = (WikiConfigImpl) obj;
		if (aliasesById == null)
		{
			if (other.aliasesById != null)
				return false;
		}
		else if (!aliasesById.equals(other.aliasesById))
			return false;
		if (engineConfig == null)
		{
			if (other.engineConfig != null)
				return false;
		}
		else if (!engineConfig.equals(other.engineConfig))
			return false;
		if (contentLang == null)
		{
			if (other.contentLang != null)
				return false;
		}
		else if (!contentLang.equals(other.contentLang))
			return false;
		if (defaultNamespace == null)
		{
			if (other.defaultNamespace != null)
				return false;
		}
		else if (!defaultNamespace.equals(other.defaultNamespace))
			return false;
		if (iwPrefix == null)
		{
			if (other.iwPrefix != null)
				return false;
		}
		else if (!iwPrefix.equals(other.iwPrefix))
			return false;
		if (namespaceById == null)
		{
			if (other.namespaceById != null)
				return false;
		}
		else if (!namespaceById.equals(other.namespaceById))
			return false;
		if (parserConfig == null)
		{
			if (other.parserConfig != null)
				return false;
		}
		else if (!parserConfig.equals(other.parserConfig))
			return false;
		if (pfnGroups == null)
		{
			if (other.pfnGroups != null)
				return false;
		}
		else if (!pfnGroups.equals(other.pfnGroups))
			return false;
		if (prefixToInterwikiMap == null)
		{
			if (other.prefixToInterwikiMap != null)
				return false;
		}
		else if (!prefixToInterwikiMap.equals(other.prefixToInterwikiMap))
			return false;
		if (tagExtGroups == null)
		{
			if (other.tagExtGroups != null)
				return false;
		}
		else if (!tagExtGroups.equals(other.tagExtGroups))
			return false;
		if (templateNamespace == null)
		{
			if (other.templateNamespace != null)
				return false;
		}
		else if (!templateNamespace.equals(other.templateNamespace))
			return false;
		if (wikiUrl == null)
		{
			if (other.wikiUrl != null)
				return false;
		}
		else if (!wikiUrl.equals(other.wikiUrl))
			return false;
		return true;
	}

	// =========================================================================

	public void save(File file) throws JAXBException
	{
		createMarshaller().marshal(this, file);
	}

	public void save(Writer writer) throws JAXBException
	{
		createMarshaller().marshal(this, writer);
	}

	public void save(OutputStream out) throws JAXBException
	{
		createMarshaller().marshal(this, out);
	}

	public JAXBSource getAsJAXBSource() throws JAXBException
	{
		return new JAXBSource(createMarshaller(), this);
	}

	private Marshaller createMarshaller() throws JAXBException
	{
		JAXBContext context = JAXBContext.newInstance(WikiConfigImpl.class);

		Marshaller m = context.createMarshaller();

		m.setEventHandler(new ValidationEventHandler()
		{
			@Override
			public boolean handleEvent(ValidationEvent event)
			{
				System.err.println(event);
				return true;
			}
		});

		try
		{
			m.setProperty(
					"com.sun.xml.bind.namespacePrefixMapper",
					new NamespaceMapper());

			m.setProperty(
					Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
		}
		catch (PropertyException e)
		{
		}

		return m;
	}

	public static WikiConfigImpl load(File file) throws JAXBException
	{
		return finishImport((WikiConfigImpl) createUnmarshaller().unmarshal(file));
	}

	public static WikiConfigImpl load(Reader reader) throws JAXBException
	{
		return finishImport((WikiConfigImpl) createUnmarshaller().unmarshal(reader));
	}

	public static WikiConfigImpl load(InputStream in) throws JAXBException
	{
		return finishImport((WikiConfigImpl) createUnmarshaller().unmarshal(in));
	}

	public static WikiConfigImpl load(Source in) throws JAXBException
	{
		return finishImport((WikiConfigImpl) createUnmarshaller().unmarshal(in));
	}

	private static WikiConfigImpl finishImport(WikiConfigImpl config)
	{
		for (ParserFunctionBase pf : config.getParserFunctions())
			pf.setWikiConfig(config);

		for (TagExtensionBase te : config.getTagExtensions())
			te.setWikiConfig(config);

		config.parserConfig.setWikiConfig(config);

		config.nodeFactory = new EngineNodeFactoryImpl(config.parserConfig);

		return config;
	}

	private static Unmarshaller createUnmarshaller() throws JAXBException
	{
		JAXBContext context = JAXBContext.newInstance(WikiConfigImpl.class);

		Unmarshaller m = context.createUnmarshaller();

		m.setEventHandler(new ValidationEventHandler()
		{
			@Override
			public boolean handleEvent(ValidationEvent event)
			{
				// We don't want to recover!
				return false;
			}
		});

		return m;
	}

	// =========================================================================

	private static final class NamespaceMapper
			extends
				NamespacePrefixMapper
	{
		private static final String SWC_ENGINE_PREFIX = "swc-engine";

		private static final String SWC_ENGINE_URI = "org.sweble.wikitext.engine";

		@Override
		public String getPreferredPrefix(
				String namespaceUri,
				String suggestion,
				boolean requirePrefix)
		{
			if (SWC_ENGINE_URI.equals(namespaceUri))
				return SWC_ENGINE_PREFIX;
			else
				return suggestion;
		}

		@Override
		public String[] getPreDeclaredNamespaceUris()
		{
			return new String[] { SWC_ENGINE_URI, };
		}
	}

	// =========================================================================

	@XmlElement(name = "i18nAlias")
	@XmlElementWrapper(name = "i18nAliases")
	private I18nAliasImpl[] getJaxbAliases()
	{
		I18nAliasImpl[] jaxbAliases = this.aliasesById.values().toArray(
				new I18nAliasImpl[this.aliasesById.size()]);
		Arrays.sort(jaxbAliases);
		return jaxbAliases;
	}

	@SuppressWarnings("unused")
	private void setJaxbAliases(I18nAliasImpl[] aliases)
	{
		for (I18nAliasImpl alias : aliases)
			addI18nAlias(alias);
	}

	// =========================================================================

	@XmlElement(name = "pfnGroup")
	@XmlElementWrapper(name = "pfnGroups")
	private ParserFunctionGroup[] getJaxbPfnGroups()
	{
		ParserFunctionGroup[] jaxbPfnGroups = this.pfnGroups.values().toArray(
				new ParserFunctionGroup[this.pfnGroups.size()]);
		Arrays.sort(jaxbPfnGroups);
		return jaxbPfnGroups;
	}

	@SuppressWarnings("unused")
	private void setJaxbPfnGroups(ParserFunctionGroup[] pfnGroups)
	{
		for (ParserFunctionGroup pfnGroup : pfnGroups)
			addParserFunctionGroup(pfnGroup);
	}

	// =========================================================================

	@XmlElement(name = "tagExtGroup")
	@XmlElementWrapper(name = "tagExtGroups")
	private TagExtensionGroup[] getJaxbTagExtGroups()
	{
		TagExtensionGroup[] jaxbTagExtGroups = this.tagExtGroups.values().toArray(
				new TagExtensionGroup[this.tagExtGroups.size()]);
		Arrays.sort(jaxbTagExtGroups);
		return jaxbTagExtGroups;
	}

	@SuppressWarnings("unused")
	private void setJaxbTagExtGroups(TagExtensionGroup[] tagExtGroups)
	{
		for (TagExtensionGroup tagExtGroup : tagExtGroups)
			addTagExtensionGroup(tagExtGroup);
	}

	// =========================================================================

	@XmlElement(name = "interwiki")
	@XmlElementWrapper(name = "interwikis")
	private InterwikiImpl[] getJaxbInterwikis()
	{
		InterwikiImpl[] jaxbInterwikis = this.prefixToInterwikiMap.values().toArray(
				new InterwikiImpl[this.prefixToInterwikiMap.size()]);
		Arrays.sort(jaxbInterwikis);
		return jaxbInterwikis;
	}

	@SuppressWarnings("unused")
	private void setJaxbInterwikis(InterwikiImpl[] interwikis)
	{
		for (InterwikiImpl iw : interwikis)
			addInterwiki(iw);
	}

	// =========================================================================

	private static final class Namespaces
	{
		@XmlElement(name = "namespace")
		private NamespaceImpl[] namespaces;

		@XmlAttribute
		private int defaultNsId;

		@XmlAttribute
		private int templateNsId;

		@SuppressWarnings("unused")
		public Namespaces()
		{
		}

		public Namespaces(NamespaceImpl[] namespaces, int defId, int tmplId)
		{
			Arrays.sort(namespaces);
			this.namespaces = namespaces;
			this.defaultNsId = defId;
			this.templateNsId = tmplId;
		}
	}

	@XmlElement(name = "namespaces")
	private Namespaces getJaxbNamespaces()
	{
		return new Namespaces(
				this.namespaceById.values().toArray(
						new NamespaceImpl[this.namespaceById.size()]),
				defaultNamespace.getId(),
				templateNamespace.getId());
	}

	@SuppressWarnings("unused")
	private void setJaxbNamespaces(Namespaces namespaces)
	{
		for (NamespaceImpl ns : namespaces.namespaces)
			addNamespace(ns);
		setDefaultNamespace(getNamespace(namespaces.defaultNsId));
		setTemplateNamespace(getNamespace(namespaces.templateNsId));
	}
}
