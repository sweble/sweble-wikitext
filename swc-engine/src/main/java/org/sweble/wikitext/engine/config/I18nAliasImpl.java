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

import java.io.Serializable;
import java.util.Collection;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(
		name = "alias",
		propOrder = { "id", "caseSensitive", "aliases" })
public class I18nAliasImpl
		implements
			I18nAlias,
			Serializable,
			Comparable<I18nAlias>
{
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private Boolean caseSensitive;
	
	private TreeSet<String> aliases = new TreeSet<String>();
	
	// =========================================================================
	
	protected I18nAliasImpl()
	{
	}
	
	public I18nAliasImpl(
			String id,
			Boolean caseSensitive,
			Collection<String> aliases)
	{
		this.id = id;
		this.caseSensitive = caseSensitive;
		this.aliases = new TreeSet<String>();
		this.aliases.addAll(aliases);
	}
	
	// =========================================================================
	
	@Override
	@XmlAttribute
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	@Override
	@XmlAttribute
	public Boolean isCaseSensitive()
	{
		return caseSensitive;
	}
	
	public void setCaseSensitive(Boolean caseSensitive)
	{
		this.caseSensitive = caseSensitive;
	}
	
	@Override
	@XmlElement(name = "alias")
	public TreeSet<String> getAliases()
	{
		return aliases;
	}
	
	public void setAliases(TreeSet<String> aliases)
	{
		this.aliases = aliases;
	}
	
	// =========================================================================
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aliases == null) ? 0 : aliases.hashCode());
		result = prime * result + ((caseSensitive == null) ? 0 : caseSensitive.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		I18nAliasImpl other = (I18nAliasImpl) obj;
		if (aliases == null)
		{
			if (other.aliases != null)
				return false;
		}
		else if (!aliases.equals(other.aliases))
			return false;
		if (caseSensitive == null)
		{
			if (other.caseSensitive != null)
				return false;
		}
		else if (!caseSensitive.equals(other.caseSensitive))
			return false;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	// =========================================================================
	
	@Override
	public String toString()
	{
		return "I18nAliasImpl [name=" + id + ", caseSensitive=" + caseSensitive + ", aliases=" + aliases + "]";
	}
	
	// =========================================================================
	
	@Override
	public int compareTo(I18nAlias o)
	{
		return this.id.compareTo(o.getId());
	}
}
