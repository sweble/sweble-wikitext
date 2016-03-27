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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.sweble.wikitext.engine.TagExtensionBase;

@XmlType(name = "tagExtGroup")
public class TagExtensionGroup
		implements
			Serializable,
			Comparable<TagExtensionGroup>
{
	private static final long serialVersionUID = 1L;

	@XmlAttribute
	private String name;

	/** Just used to check for duplicates. */
	private transient final HashSet<String> tagExtIds = new HashSet<String>();

	private final HashSet<TagExtensionBase> tagExtensions =
			new HashSet<TagExtensionBase>();

	// =========================================================================

	public TagExtensionGroup()
	{
	}

	public TagExtensionGroup(String name)
	{
		this.name = name;
	}

	// =========================================================================

	public String getName()
	{
		return name;
	}

	public Collection<TagExtensionBase> getTagExtensions()
	{
		return Collections.unmodifiableCollection(tagExtensions);
	}

	public void addTagExtension(TagExtensionBase tagExt)
	{
		if (tagExtIds.contains(tagExt.getId()))
			throw new IllegalArgumentException("Tag extensions group already contains tag extensions with ID: " + tagExt.getId());
		this.tagExtIds.add(tagExt.getId());
		this.tagExtensions.add(tagExt);
	}

	// =========================================================================

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tagExtensions == null) ? 0 : tagExtensions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TagExtensionGroup))
			return false;
		TagExtensionGroup other = (TagExtensionGroup) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (tagExtensions == null)
		{
			if (other.tagExtensions != null)
				return false;
		}
		else if (!tagExtensions.equals(other.tagExtensions))
			return false;
		return true;
	}

	// =========================================================================

	@Override
	public String toString()
	{
		return "TagExtensionGroup [name=" + name + ", tagExtensions=" + tagExtensions + "]";
	}

	// =========================================================================

	@Override
	public int compareTo(TagExtensionGroup o)
	{
		return this.name.compareTo(o.getName());
	}

	// =========================================================================

	@XmlElement(name = "tagExt")
	private ArrayList<TagExtensionBase> getJaxbTagExtensions()
	{
		ArrayList<TagExtensionBase> sorted = new ArrayList<TagExtensionBase>(tagExtensions);
		Collections.sort(sorted);
		return sorted;
	}

	@SuppressWarnings("unused")
	private void setJaxbTagExtensions(ArrayList<TagExtensionBase> tagExts)
	{
		for (TagExtensionBase tagExt : tagExts)
			addTagExtension(tagExt);
	}
}
