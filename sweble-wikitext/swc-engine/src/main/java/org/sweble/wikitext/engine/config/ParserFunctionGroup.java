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

import org.sweble.wikitext.engine.ParserFunctionBase;

@XmlType(name = "pfnGroup")
public class ParserFunctionGroup
		implements
			Serializable,
			Comparable<ParserFunctionGroup>
{
	private static final long serialVersionUID = 1L;

	@XmlAttribute
	private String name;

	/** Just used to check for duplicates. */
	private transient final HashSet<String> pfnIds = new HashSet<String>();

	private final HashSet<ParserFunctionBase> pfns =
			new HashSet<ParserFunctionBase>();

	// =========================================================================

	public ParserFunctionGroup()
	{
	}

	public ParserFunctionGroup(String name)
	{
		this.name = name;
	}

	// =========================================================================

	public String getName()
	{
		return name;
	}

	public Collection<ParserFunctionBase> getParserFunctions()
	{
		return Collections.unmodifiableCollection(pfns);
	}

	public void addParserFunction(ParserFunctionBase pfn)
	{
		if (pfnIds.contains(pfn.getId()))
			throw new IllegalArgumentException("Parser function group already contains parser function with ID: " + pfn.getId());
		this.pfnIds.add(pfn.getId());
		this.pfns.add(pfn);
	}

	// =========================================================================

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pfns == null) ? 0 : pfns.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ParserFunctionGroup))
			return false;
		ParserFunctionGroup other = (ParserFunctionGroup) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (pfns == null)
		{
			if (other.pfns != null)
				return false;
		}
		else if (!pfns.equals(other.pfns))
			return false;
		return true;
	}

	// =========================================================================

	@Override
	public String toString()
	{
		return "ParserFunctionGroup [name=" + name + ", pfns=" + pfns + "]";
	}

	// =========================================================================

	@Override
	public int compareTo(ParserFunctionGroup o)
	{
		return this.name.compareTo(o.getName());
	}

	// =========================================================================

	@XmlElement(name = "pfn")
	private ArrayList<ParserFunctionBase> getPfns()
	{
		ArrayList<ParserFunctionBase> sorted = new ArrayList<ParserFunctionBase>(pfns);
		Collections.sort(sorted);
		return sorted;
	}

	@SuppressWarnings("unused")
	private void setPfns(ArrayList<ParserFunctionBase> pfns)
	{
		for (ParserFunctionBase pfn : pfns)
			addParserFunction(pfn);
	}
}
