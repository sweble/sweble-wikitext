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

import org.sweble.wikitext.engine.config.Interwiki;
import org.sweble.wikitext.engine.config.Namespace;
import org.sweble.wikitext.engine.config.WikiConfigurationInterface;
import org.sweble.wikitext.lazy.LinkTargetException;
import org.sweble.wikitext.lazy.LinkTargetParser;

public class PageTitle
        implements
            Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final String title;
	
	private final String fragment;
	
	private final Namespace namespace;
	
	private final Interwiki interwiki;
	
	private final boolean initialColon;
	
	// =========================================================================
	
	private final boolean isDefaultNs;
	
	// =========================================================================
	
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
	
	public String getFullTitle()
	{
		// FIXME: Review the implementation!
		
		String result = "";
		
		if (interwiki != null)
			result += interwiki.getPrefix() + ":";
		
		if (namespace != null && !isDefaultNs)
			result += namespace.getCanonical() + ":";
		
		result += getDenormalizedTitle();
		
		return result;
	}

	public String getDenormalizedTitle()
	{
		return title.replace('_', ' ');
	}
	
	public String getLinkString()
	{
		// FIXME: Review the implementation!
		
		String result = "";
		
		if (interwiki != null)
			result += interwiki.getPrefix() + ":";
		
		if (namespace != null && !isDefaultNs)
			result += namespace.getCanonical() + ":";
		
		result += title;
		
		if (fragment != null)
			result += "#" + fragment;
		
		return result;
	}
	
	// =========================================================================
	
	protected PageTitle(
	        String title,
	        String fragment,
	        Namespace namespace,
	        Interwiki interwiki,
	        boolean initialColon,
	        boolean isDefaultNs)
	{
		this.title = title;
		this.fragment = fragment;
		this.namespace = namespace;
		this.interwiki = interwiki;
		this.initialColon = initialColon;
		this.isDefaultNs = isDefaultNs;
	}
	
	public static PageTitle make(WikiConfigurationInterface config, String target) throws LinkTargetException
	{
		return make(config, target, null);
	}
	
	public static PageTitle make(WikiConfigurationInterface config, String target, Namespace defaultNamespace) throws LinkTargetException
	{
		// FIXME: Review the implementation!
		
		LinkTargetParser parser = new LinkTargetParser();
		parser.parse(config, target);
		
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
		
		Interwiki interwiki = config.getInterwiki(parser.getInterwiki());
		
		// TODO: MediaWiki limits the length of title names
		
		// Don't capitalize the first letter of a interwiki link
		if (interwiki == null && title.length() > 0 && namespace.isCapitalized())
			title = Character.toUpperCase(title.charAt(0)) + title.substring(1);
		
		// TODO: MediaWiki normalizes IPv6 titles
		
		boolean isDefaultNs = namespace.equals(config.getDefaultNamespace());
		
		return new PageTitle(
		        title,
		        fragment,
		        namespace,
		        interwiki,
		        initialColon,
		        isDefaultNs);
	}
}
