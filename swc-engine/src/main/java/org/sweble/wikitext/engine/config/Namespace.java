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

public class Namespace
		implements
			Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final int id;
	
	private final String name;
	
	private final String canonical;
	
	private final boolean subpages;
	
	private final boolean isFileNs;
	
	private final TreeSet<String> aliases = new TreeSet<String>();
	
	// =========================================================================
	
	public Namespace(
			int id,
			String name,
			String canonical,
			boolean subpages,
			boolean isFileNs,
			Collection<String> aliases)
	{
		this.id = id;
		this.name = name;
		this.subpages = subpages;
		this.canonical = canonical;
		this.isFileNs = isFileNs;
		this.aliases.addAll(aliases);
	}
	
	// =========================================================================
	
	public String getName()
	{
		return name;
	}
	
	public int getId()
	{
		return id;
	}
	
	public boolean isSubpages()
	{
		return subpages;
	}
	
	public String getCanonical()
	{
		return canonical;
	}
	
	public TreeSet<String> getAliases()
	{
		return aliases;
	}
	
	// TODO: Implement properly
	public boolean isCapitalized()
	{
		return true;
	}
	
	// TODO: Think this through once more ...
	public boolean isFileNs()
	{
		return isFileNs;
	}
	
	public boolean isTalkNamespace()
	{
		return (id > 0) && (id % 2 == 1);
	}
	
	public boolean isSubjectNamespace()
	{
		return (id >= 0) && (id % 2 == 0);
	}
	
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
		Namespace other = (Namespace) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
