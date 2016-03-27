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
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngineNodeFactory;
import org.sweble.wikitext.engine.utils.EngineAstTextUtils;
import org.sweble.wikitext.parser.nodes.WtNode;

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

	/**
	 * Can't be final since it cannot be set during un-marshaling.
	 */
	private WikiConfig wikiConfig;

	private EngineAstTextUtils tu;

	private EngineNodeFactory nf;

	// =========================================================================

	/**
	 * For un-marshaling only.
	 */
	public ParserFunctionBase(String id)
	{
		this(PfnArgumentMode.UNEXPANDED_VALUES, false, id);
	}

	/**
	 * For un-marshaling only.
	 */
	public ParserFunctionBase(PfnArgumentMode argMode, String id)
	{
		this(argMode, false, id);
	}

	/**
	 * For un-marshaling only.
	 */
	public ParserFunctionBase(
			PfnArgumentMode argMode,
			boolean pageSwitch,
			String id)
	{
		if (id == null || id.isEmpty())
			throw new IllegalArgumentException();

		this.argMode = argMode;
		this.pageSwitch = pageSwitch;
		this.id = id;
	}

	public ParserFunctionBase(WikiConfig wikiConfig, String id)
	{
		this(wikiConfig, PfnArgumentMode.UNEXPANDED_VALUES, false, id);
	}

	public ParserFunctionBase(
			WikiConfig wikiConfig,
			PfnArgumentMode argMode,
			String id)
	{
		this(wikiConfig, argMode, false, id);
	}

	public ParserFunctionBase(
			WikiConfig wikiConfig,
			PfnArgumentMode argMode,
			boolean pageSwitch,
			String id)
	{
		this(argMode, pageSwitch, id);
		setWikiConfig(wikiConfig);
	}

	// =========================================================================

	/**
	 * For internal use only!
	 */
	public void setWikiConfig(WikiConfig wikiConfig)
	{
		if (wikiConfig == null)
			throw new IllegalArgumentException();

		this.wikiConfig = wikiConfig;
		this.nf = wikiConfig.getNodeFactory();
		this.tu = wikiConfig.getAstTextUtils();
	}

	public WikiConfig getWikiConfig()
	{
		return wikiConfig;
	}

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

	protected EngineNodeFactory nf()
	{
		return nf;
	}

	protected EngineAstTextUtils tu()
	{
		return tu;
	}

	/**
	 * WtNode can either be a WtTemplate or a WtPageSwitch
	 */
	public abstract WtNode invoke(
			WtNode template,
			ExpansionFrame preprocessorFrame,
			List<? extends WtNode> argsValues);

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
		public ParserFunctionAdapter()
		{
		}

		@Override
		public ParserFunctionRef marshal(ParserFunctionBase v)
		{
			return new ParserFunctionRef(v.getClass().getName());
		}

		@Override
		public ParserFunctionBase unmarshal(ParserFunctionRef v) throws ClassNotFoundException, InstantiationException, IllegalAccessException
		{
			Class<?> clazz = Class.forName(v.className);
			/*
			Constructor<?> ctor = clazz.getDeclaredConstructor(WikiConfig.class);
			// We don't have a wiki config object yet :(
			return (ParserFunctionBase) ctor.newInstance((WikiConfig) null);
			*/
			return (ParserFunctionBase) clazz.newInstance();
		}
	}
}
