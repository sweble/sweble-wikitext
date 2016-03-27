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

package org.sweble.wikitext.parser;

import java.util.ArrayList;

import org.sweble.wikitext.parser.nodes.WtContentNode;
import org.sweble.wikitext.parser.nodes.WtNode;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.RtData;

public class WtRtData
		extends
			RtData
{
	private static final long serialVersionUID = 1L;

	public static final WtRtData SUPPRESS = new WtRtDataSuppressSingleton();

	// =========================================================================

	/**
	 * Constructor for SUPPRESS singleton.
	 */
	private WtRtData()
	{
	}

	public WtRtData(AstNode<?> node, Object... glue)
	{
		super(node, glue);
	}

	public WtRtData(AstNode<?> node, String... glue)
	{
		super(node, glue);
	}

	public WtRtData(AstNode<?> node)
	{
		super(node);
	}

	public WtRtData(int size, Object... glue)
	{
		super(size, glue);
	}

	public WtRtData(int size, String... glue)
	{
		super(size, glue);
	}

	public WtRtData(int size)
	{
		super(size);
	}

	public WtRtData(WtRtData rtData)
	{
		super(rtData);
	}

	// =========================================================================

	@Override
	public boolean isSuppress()
	{
		return this == SUPPRESS;
	}

	// =========================================================================

	@Override
	protected void addNodeOrObject(ArrayList<Object> result, Object o)
	{
		if (o instanceof WtContentNode)
		{
			for (WtNode c : (WtContentNode) o)
				addNodeOrObject(result, c);
		}
		else
		{
			super.addNodeOrObject(result, o);
		}
	}

	// =========================================================================

	@Override
	public String toString()
	{
		return super.toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return new WtRtData(this);
	}

	// =========================================================================

	protected static class WtRtDataSuppressSingleton
			extends
				WtRtData
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void set(Object... glue)
		{
			notSupported();
		}

		@Override
		public void set(String... glue)
		{
			notSupported();
		}

		@Override
		public void setField(int field, Object... glue)
		{
			notSupported();
		}

		@Override
		public void setField(int field, String glue)
		{
			notSupported();
		}

		@Override
		public void setField(int field, String... glue)
		{
			notSupported();
		}

		@Override
		public void prepend(String text)
		{
			notSupported();
		}

		@Override
		public void append(Object... glue)
		{
			notSupported();
		}

		@Override
		public void prepend(Object... glue)
		{
			notSupported();
		}

		private void notSupported()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			return false;
		}

		@Override
		public Object clone() throws CloneNotSupportedException
		{
			throw new CloneNotSupportedException();
		}
	}
}
