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
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.sweble.wikitext.engine.TagExtensionBase.TagExtensionAdapter;
import org.sweble.wikitext.parser.preprocessor.TagExtension;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

@XmlTransient
@XmlJavaTypeAdapter(value = TagExtensionAdapter.class)
public abstract class TagExtensionBase
		implements
			Serializable,
			Comparable<TagExtensionBase>
{
	private static final long serialVersionUID = 1L;
	
	private final String id;
	
	// =========================================================================
	
	public TagExtensionBase(String id)
	{
		super();
		this.id = id;
	}
	
	// =========================================================================
	
	public String getId()
	{
		return id;
	}
	
	public abstract AstNode invoke(
			ExpansionFrame preprocessorFrame,
			TagExtension tagExtension,
			Map<String, NodeList> attributes,
			String body);
	
	// =========================================================================
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
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
		return true;
	}
	
	// =========================================================================
	
	@Override
	public int compareTo(TagExtensionBase o)
	{
		return this.id.compareTo(o.getId());
	}
	
	// =========================================================================
	
	@Override
	public String toString()
	{
		return "TagExtensionBase [id=" + id + "]";
	}
	
	// =========================================================================
	
	protected static final class TagExtensionRef
	{
		@XmlAttribute(name = "class")
		public String className;
		
		public TagExtensionRef()
		{
		}
		
		public TagExtensionRef(String name)
		{
			this.className = name;
		}
	}
	
	public static final class TagExtensionAdapter
			extends
				XmlAdapter<TagExtensionRef, TagExtensionBase>
	{
		@Override
		public TagExtensionRef marshal(TagExtensionBase v) throws Exception
		{
			return new TagExtensionRef(v.getClass().getName());
		}
		
		@Override
		public TagExtensionBase unmarshal(TagExtensionRef v) throws Exception
		{
			return (TagExtensionBase) Class.forName(v.className).newInstance();
		}
	}
}
