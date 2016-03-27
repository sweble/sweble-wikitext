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

import java.util.Collection;
import java.util.TimeZone;

import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.TagExtensionBase;
import org.sweble.wikitext.engine.nodes.EngineNodeFactory;
import org.sweble.wikitext.engine.utils.EngineAstTextUtils;
import org.sweble.wikitext.parser.ParserConfig;

public interface WikiConfig
{
	// ==[ Parser Configuration ]===============================================

	public ParserConfig getParserConfig();

	// ==[ Engine Configuration ]===============================================

	public EngineConfig getEngineConfig();

	// ==[ AST creation/processing ]============================================

	/**
	 * Should return the same instance as getParserConfig().getNodeFactory().
	 */
	public EngineNodeFactory getNodeFactory();

	/**
	 * Should return the same instance as getParserConfig().getAstTextUtils().
	 */
	public EngineAstTextUtils getAstTextUtils();

	// ==[ Namespaces ]=========================================================

	public Namespace getNamespace(String name);

	public Namespace getNamespace(int id);

	public Collection<Namespace> getNamespaces();

	public Namespace getDefaultNamespace();

	public Namespace getTemplateNamespace();

	public Namespace getFileNamespace();

	public Namespace getSubjectNamespaceFor(Namespace namespace);

	public Namespace getTalkNamespaceFor(Namespace namespace);

	// ==[ Known Wikis ]========================================================

	public Interwiki getInterwiki(String prefix);

	public Collection<Interwiki> getInterwikis();

	// ==[ Internationalization ]===============================================

	public I18nAlias getI18nAlias(String name);

	public Collection<I18nAlias> getI18nAliases();

	// ==[ Tag extensions, parser functions and page switches ]=================

	/**
	 * Returns parser functions and page switches.
	 */
	public Collection<ParserFunctionBase> getParserFunctions();

	/**
	 * Cannot retrieve page switches.
	 */
	public ParserFunctionBase getParserFunction(String name);

	/**
	 * Page switches are a special form of parser function. They are therefore
	 * listed in getParserFunctions(). However, retrieval works only over
	 * getPageSwitch().
	 */
	public ParserFunctionBase getPageSwitch(String name);

	public Collection<TagExtensionBase> getTagExtensions();

	public TagExtensionBase getTagExtension(String name);

	// ==[ Properties of the wiki instance ]====================================

	/**
	 * Name of this wiki.
	 */
	public String getSiteName();

	/**
	 * The equivalent of MediaWiki's $wgScript.
	 */
	public String getWikiUrl();

	/**
	 * The equivalent of MediaWiki's $wgArticlePath.
	 */
	public String getArticlePath();

	public String getContentLanguage();

	/**
	 * Returns the interwiki prefix for the configured wiki instance or null if
	 * this wiki itself does not have an interwiki prefix.
	 */
	public Object getInterwikiPrefix();

	// ==[ Runtime information ]================================================

	public WikiRuntimeInfo getRuntimeInfo();

	public TimeZone getTimezone();
}
