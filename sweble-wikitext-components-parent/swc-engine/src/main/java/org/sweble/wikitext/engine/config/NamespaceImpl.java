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
		name = "namespace",
		propOrder = { "id", "name", "canonical", "canHaveSubpages", "fileNs", "aliases" })
public class NamespaceImpl
		implements
			Namespace,
			Serializable,
			Comparable<Namespace>
{
	private static final long serialVersionUID = 1L;

	private int id;

	private String name;

	private String canonical;

	private boolean canHaveSubpages;

	private boolean fileNs;

	private TreeSet<String> aliases = new TreeSet<String>();

	// =========================================================================

	private static final int NS_MEDIA = -2;

	/*
	private static final int NS_SPECIAL = -1;
	
	private static final int NS_MAIN = 0;
	
	private static final int NS_TALK = 1;
	
	private static final int NS_USER = 2;
	
	private static final int NS_USER_TALK = 3;
	
	private static final int NS_PROJECT = 4;
	
	private static final int NS_PROJECT_TALK = 5;
	
	private static final int NS_FILE = 6;
	
	private static final int NS_FILE_TALK = 7;
	
	private static final int NS_MEDIAWIKI = 8;
	
	private static final int NS_MEDIAWIKI_TALK = 9;
	
	private static final int NS_TEMPLATE = 10;
	
	private static final int NS_TEMPLATE_TALK = 11;
	
	private static final int NS_HELP = 12;
	
	private static final int NS_HELP_TALK = 13;
	
	private static final int NS_CATEGORY = 14;
	
	private static final int NS_CATEGORY_TALK = 15;
	*/

	// =========================================================================

	protected NamespaceImpl()
	{
	}

	public NamespaceImpl(
			int id,
			String name,
			String canonical,
			boolean subpages,
			boolean isFileNs,
			Collection<String> aliases)
	{
		this.id = id;
		this.name = name;
		this.canHaveSubpages = subpages;
		this.canonical = canonical;
		this.fileNs = isFileNs;
		this.aliases.addAll(aliases);
	}

	// =========================================================================

	@Override
	@XmlAttribute
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	@XmlAttribute
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	@Override
	@XmlAttribute
	public boolean isCanHaveSubpages()
	{
		return canHaveSubpages;
	}

	public void setCanHaveSubpages(boolean canHaveSubpages)
	{
		this.canHaveSubpages = canHaveSubpages;
	}

	@Override
	@XmlAttribute
	public String getCanonical()
	{
		return canonical;
	}

	public void setCanonical(String canonical)
	{
		this.canonical = canonical;
	}

	@Override
	@XmlAttribute
	public boolean isFileNs()
	{
		return fileNs;
	}

	public void setFileNs(boolean fileNs)
	{
		this.fileNs = fileNs;
	}

	@Override
	public boolean isMediaNs()
	{
		return this.id == NS_MEDIA;
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

	@Override
	public boolean isTalkNamespace()
	{
		return (id > 0) && (id % 2 == 1);
	}

	@Override
	public boolean isSubjectNamespace()
	{
		return (id >= 0) && (id % 2 == 0);
	}

	@Override
	public int getTalkspaceId()
	{
		if (isTalkNamespace())
			return id;
		return id + 1;
	}

	@Override
	public int getSubjectspaceId()
	{
		if (isSubjectNamespace())
			return id;
		return id - 1;
	}

	// =========================================================================

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		NamespaceImpl other = (NamespaceImpl) obj;
		if (id != other.id)
			return false;
		return true;
	}

	// =========================================================================

	@Override
	public String toString()
	{
		return "NamespaceImpl [id=" + id + ", name=" + name + ", canonical=" + canonical + ", canHaveSubpages=" + canHaveSubpages + ", fileNs=" + fileNs + ", aliases=" + aliases + "]";
	}

	// =========================================================================

	@Override
	public int compareTo(Namespace o)
	{
		return ((Integer) getId()).compareTo(o.getId());
	}
}
