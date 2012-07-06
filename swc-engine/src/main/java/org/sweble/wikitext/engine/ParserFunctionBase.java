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
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.sweble.wikitext.engine.ParserFunctionBase.ParserFunctionAdapter;

import de.fau.cs.osr.ptk.common.ast.AstNode;

@XmlTransient
@XmlJavaTypeAdapter(value = ParserFunctionAdapter.class)
public abstract class ParserFunctionBase
		implements
			Serializable,
			Comparable<ParserFunctionBase>
{
	private static final long serialVersionUID = 1L;
	
	private final String id;
	
	private final PfnArgumentMode argMode;
	
	private final boolean pageSwitch;
	
	// =========================================================================
	
	public ParserFunctionBase(String id)
	{
		this(PfnArgumentMode.UNEXPANDED_VALUES, false, id);
		
	}
	
	public ParserFunctionBase(PfnArgumentMode argMode, String id)
	{
		this(argMode, false, id);
		
	}
	
	public ParserFunctionBase(
			PfnArgumentMode argMode,
			boolean pageSwitch,
			String id)
	{
		this.argMode = argMode;
		this.pageSwitch = pageSwitch;
		this.id = id;
	}
	
	// =========================================================================
	
	public String getId()
	{
		return id;
	}
	
	public PfnArgumentMode getArgMode()
	{
		return argMode;
	}
	
	public boolean isPageSwitch()
	{
		return pageSwitch;
	}
	
	/**
	 * AstNode can either be a Template or a PageSwitch
	 */
	public abstract AstNode invoke(
			AstNode template,
			ExpansionFrame preprocessorFrame,
			List<? extends AstNode> argsValues);
	
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
	public int compareTo(ParserFunctionBase o)
	{
		return this.id.compareTo(o.getId());
	}
	
	// =========================================================================
	
	@Override
	public String toString()
	{
		return "ParserFunctionBase [id=" + id + ", argMode=" + argMode + ", pageSwitch=" + pageSwitch + "]";
	}
	
	// =========================================================================
	
	protected static final class ParserFunctionRef
	{
		@XmlAttribute(name = "class")
		public String className;
		
		public ParserFunctionRef()
		{
		}
		
		public ParserFunctionRef(String name)
		{
			this.className = name;
		}
	}
	
	public static final class ParserFunctionAdapter
			extends
				XmlAdapter<ParserFunctionRef, ParserFunctionBase>
	{
		@Override
		public ParserFunctionRef marshal(ParserFunctionBase v) throws Exception
		{
			return new ParserFunctionRef(v.getClass().getName());
		}
		
		@Override
		public ParserFunctionBase unmarshal(ParserFunctionRef v) throws Exception
		{
			return (ParserFunctionBase) Class.forName(v.className).newInstance();
		}
	}
}
