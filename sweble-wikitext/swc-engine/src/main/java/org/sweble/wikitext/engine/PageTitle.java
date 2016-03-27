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

package org.sweble.wikitext.engine;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.sweble.wikitext.engine.config.Interwiki;
import org.sweble.wikitext.engine.config.Namespace;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.config.WikiConfigurationException;
import org.sweble.wikitext.engine.utils.UrlService;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wikitext.parser.parser.LinkTargetParser;

public class PageTitle
		implements
			Serializable
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	private final WikiConfig config;

	// =========================================================================

	private final String title;

	private final String fragment;

	private final Namespace namespace;

	private final String namespaceAlias;

	private final Interwiki interwiki;

	private final boolean initialColon;

	// =========================================================================

	private final boolean isDefaultNs;

	// =========================================================================

	/**
	 * Returns the normalized title (" " replaced by "_").
	 */
	public String getTitle()
	{
		return title;
	}

	public String getFragment()
	{
		return fragment;
	}

	public Namespace getNamespace()
	{
		return namespace;
	}

	/**
	 * Returns the name of the namespace that was specified by the user. If an
	 * alias or the canonical name was used, the alias or cannonical name is
	 * returned. The alias or name as specified in the configuration is
	 * returned, which might differ in case from what the user specified.
	 */
	public String getNamespaceAlias()
	{
		if (namespaceAlias == null)
			return namespace.getName();
		return namespaceAlias;
	}

	public Interwiki getInterwikiLink()
	{
		return interwiki;
	}

	public boolean isLocal()
	{
		return interwiki == null;
	}

	public boolean isInterwiki()
	{
		return !isLocal();
	}

	public boolean hasInitialColon()
	{
		return initialColon;
	}

	/**
	 * Get the full title:
	 * 
	 * <pre>
	 * &quot;[IW_PREFIX:][NS_PREFIX:]TITLE&quot;
	 * </pre>
	 * 
	 * The TITLE itself will be in de-normalized form ("_" replaced by " ").
	 */
	public String getDenormalizedFullTitle()
	{
		String result = "";

		if (interwiki != null)
			result += interwiki.getPrefix() + ":";

		if (namespace != null && !isDefaultNs)
			result += getNamespaceAlias() + ":";

		result += getDenormalizedTitle();

		return result;
	}

	/**
	 * Returns the de-normalized title ("_" replaced by " ").
	 */
	public String getDenormalizedTitle()
	{
		return title.replace('_', ' ');
	}

	/**
	 * Get title string suitable for links:
	 * 
	 * <pre>
	 * &quot;[IW_PREFIX:][NS_PREFIX:]TITLE&quot;
	 * </pre>
	 * 
	 * The TITLE itself will be in normalized form (" " replaced by "_").
	 */
	public String getNormalizedFullTitle()
	{
		String result = "";

		if (interwiki != null)
			result += interwiki.getPrefix() + ":";

		if (namespace != null && !isDefaultNs)
			result += getNamespaceAlias() + ":";

		result += title;

		return result;
	}

	// =========================================================================

	public URL getUrl()
	{
		if (this.interwiki != null)
		{
			return this.interwiki.getUrl(this);
		}
		else
		{
			try
			{
				return UrlService.makeUrlToArticle(this.config.getArticlePath(), this);
			}
			catch (MalformedURLException e)
			{
				/* This should not happen: If the URL is correctly formatted, 
				 * appending a title must not cause a MalformedURLException. 
				 */
				throw new WikiConfigurationException(e);
			}
		}
	}

	public URL getUrl(String urlEncodedQuery) throws MalformedURLException
	{
		return (urlEncodedQuery != null) ?
				UrlService.appendQuery(getUrl(), urlEncodedQuery) :
				getUrl();
	}

	public URL getUrl(Map<String, String> query) throws MalformedURLException
	{
		return (query != null && !query.isEmpty()) ?
				UrlService.appendQuery(getUrl(), query) :
				getUrl();
	}

	// =========================================================================

	public PageTitle getBaseTitle()
	{
		if (!namespace.isCanHaveSubpages())
			return this;

		int i = title.lastIndexOf('/');
		if (i < 0)
			return this;

		String baseTitle = title.substring(0, i);

		return new PageTitle(
				config,
				baseTitle,
				null,
				namespace,
				namespaceAlias,
				interwiki,
				initialColon,
				isDefaultNs);
	}

	// =========================================================================

	public PageTitle newWithNamespace(Namespace ns)
	{
		return new PageTitle(
				config,
				title,
				fragment,
				ns,
				null,
				interwiki,
				initialColon,
				ns.equals(config.getDefaultNamespace()));
	}

	public PageTitle newWithTitle(String title)
	{
		return new PageTitle(
				config,
				title,
				fragment,
				namespace,
				namespaceAlias,
				interwiki,
				initialColon,
				isDefaultNs);
	}

	// =========================================================================

	protected PageTitle(
			WikiConfig config,
			String title,
			String fragment,
			Namespace namespace,
			String namespaceAlias,
			Interwiki interwiki,
			boolean initialColon,
			boolean isDefaultNs)
	{
		this.config = config;
		this.title = title;
		this.fragment = fragment;
		this.namespace = namespace;
		this.namespaceAlias = namespaceAlias;
		this.interwiki = interwiki;
		this.initialColon = initialColon;
		this.isDefaultNs = isDefaultNs;
	}

	public static PageTitle make(
			WikiConfig config,
			String target) throws LinkTargetException
	{
		return make(config, target, null);
	}

	public static PageTitle make(
			WikiConfig config,
			String target,
			Namespace defaultNamespace) throws LinkTargetException
	{
		// FIXME: Review the implementation!

		LinkTargetParser parser = new LinkTargetParser();
		parser.parse(config.getParserConfig(), target);

		String title = parser.getTitle();
		String fragment = parser.getFragment();
		boolean initialColon = parser.isInitialColon();

		Namespace namespace = null;
		if (parser.getNamespace() != null)
			namespace = config.getNamespace(parser.getNamespace());

		if (namespace == null)
		{
			namespace = defaultNamespace;
			if (namespace == null)
				namespace = config.getDefaultNamespace();
		}

		// If it's an alias, convert to the exact alias name.
		String namespaceAlias = null;
		if (namespace != null && parser.getNamespace() != null)
		{
			String lcNs = parser.getNamespace().toLowerCase();
			if (!lcNs.equals(namespace.getName()))
			{
				for (String a : namespace.getAliases())
				{
					if (lcNs.equals(a.toLowerCase()))
					{
						namespaceAlias = a;
						break;
					}
				}
			}
		}

		Interwiki interwiki = config.getInterwiki(parser.getInterwiki());

		// TODO: MediaWiki limits the length of title names

		// Don't capitalize the first letter of a interwiki link
		if (interwiki == null)
			title = StringUtils.capitalize(title);

		// TODO: MediaWiki normalizes IPv6 titles

		boolean isDefaultNs = namespace.equals(config.getDefaultNamespace());

		return new PageTitle(
				config,
				title,
				fragment,
				namespace,
				namespaceAlias,
				interwiki,
				initialColon,
				isDefaultNs);
	}

	// =========================================================================

	@Override
	public String toString()
	{
		return getDenormalizedFullTitle();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fragment == null) ? 0 : fragment.hashCode());
		result = prime * result + (initialColon ? 1231 : 1237);
		result = prime * result + ((interwiki == null) ? 0 : interwiki.hashCode());
		result = prime * result + (isDefaultNs ? 1231 : 1237);
		result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		PageTitle other = (PageTitle) obj;
		if (fragment == null)
		{
			if (other.fragment != null)
				return false;
		}
		else if (!fragment.equals(other.fragment))
			return false;
		if (initialColon != other.initialColon)
			return false;
		if (interwiki == null)
		{
			if (other.interwiki != null)
				return false;
		}
		else if (!interwiki.equals(other.interwiki))
			return false;
		if (isDefaultNs != other.isDefaultNs)
			return false;
		if (namespace == null)
		{
			if (other.namespace != null)
				return false;
		}
		else if (!namespace.equals(other.namespace))
			return false;
		if (title == null)
		{
			if (other.title != null)
				return false;
		}
		else if (!title.equals(other.title))
			return false;
		return true;
	}
}
