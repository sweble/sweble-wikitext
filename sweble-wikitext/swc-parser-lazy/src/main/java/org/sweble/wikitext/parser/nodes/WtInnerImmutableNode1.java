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

package org.sweble.wikitext.parser.nodes;

import java.util.Collection;
import java.util.Map;

import org.sweble.wikitext.parser.WtRtData;

import de.fau.cs.osr.ptk.common.ast.AstAbstractInnerNode.AstInnerNode1;
import de.fau.cs.osr.ptk.common.ast.AstNodeImpl;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import xtc.tree.Locatable;
import xtc.tree.Location;
import xtc.util.Pair;

public abstract class WtInnerImmutableNode1
		extends
			AstInnerNode1<WtNode>
		implements
			WtNode
{

	private static final long serialVersionUID = -3023143947405463528L;

	// =========================================================================

	protected WtInnerImmutableNode1(WtNode n0)
	{
		super(n0);
	}

	// =========================================================================

	@Override
	public void setRtd(WtRtData rtd)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public void setRtd(Object... glue)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public void setRtd(String... glue)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public WtRtData getRtd()
	{
		return null;
	}

	@Override
	public void clearRtd()
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public void suppressRtd()
	{
		throw new UnsupportedOperationException(genMsg());
	}

	// =========================================================================

	protected String genMsg()
	{
		return "You are operating on an immutable " + getNodeName() + " object!";
	}

	// =========================================================================

	@Override
	public void setLocation(Location location)
	{
		// This is called by the parser, can't prevent that ...
	}

	@Override
	public void setLocation(Locatable locatable)
	{
		// This is called by the parser, can't prevent that ...
	}

	@Override
	public void setNativeLocation(
			de.fau.cs.osr.ptk.common.ast.AstLocation location)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	// =========================================================================

	@Override
	public void setAttributes(Map<String, Object> attrs)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public void clearAttributes()
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public Object setAttribute(String name, Object value)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public Object removeAttribute(String name)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public Integer setIntAttribute(String name, Integer value)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public boolean setBooleanAttribute(String name, boolean value)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public String setStringAttribute(String name, String value)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	// =========================================================================

	@Override
	public abstract int getNodeType();

	@Override
	public abstract String getNodeName();

	// =========================================================================

	@Override
	public abstract String[] getChildNames();

	// =========================================================================

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public AstNodeImpl<WtNode> cloneWrapException()
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public AstNodeImpl<WtNode> deepClone() throws CloneNotSupportedException
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public AstNodeImpl<WtNode> deepCloneWrapException()
	{
		throw new UnsupportedOperationException(genMsg());
	}

	// =========================================================================

	@Override
	public boolean addAll(Pair<? extends WtNode> p)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public void add(int index, WtNode element)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public boolean add(WtNode e)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public boolean addAll(Collection<? extends WtNode> c)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public boolean addAll(int index, Collection<? extends WtNode> c)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public WtNode remove(int index)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public WtNode set(int index, WtNode element)
	{
		if (element == null)
			throw new NullPointerException();
		// We allow one initial set call (which should be done by the
		// constructor). Since null values are not allowed as children, we can
		// identify the first "set" operation by seeing if the current value is
		// still null.
		if (get(index) != null)
			throw new UnsupportedOperationException(genMsg());
		return super.set(index, element);
	}

	@Override
	public void clear()
	{
		throw new UnsupportedOperationException(genMsg());
	}

	// =========================================================================

	@Override
	public int getPropertyCount()
	{
		return 1;
	}

	@Override
	public AstNodePropertyIterator propertyIterator()
	{
		return new WtInnerNode1PropertyIterator();
	}

	protected class WtInnerNode1PropertyIterator
			extends
				AstNodePropertyIterator
	{
		@Override
		protected int getPropertyCount()
		{
			return WtInnerImmutableNode1.this.getPropertyCount();
		}

		@Override
		protected String getName(int index)
		{
			switch (index)
			{
				case 0:
					return "rtd";

				default:
					throw new IndexOutOfBoundsException();
			}
		}

		@Override
		protected Object getValue(int index)
		{
			switch (index)
			{
				case 0:
					return WtInnerImmutableNode1.this.getRtd();

				default:
					throw new IndexOutOfBoundsException();
			}
		}

		@Override
		protected Object setValue(int index, Object value)
		{
			switch (index)
			{
				case 0:
				{
					WtRtData old = WtInnerImmutableNode1.this.getRtd();
					WtInnerImmutableNode1.this.setRtd((WtRtData) value);
					return old;
				}

				default:
					throw new IndexOutOfBoundsException();
			}
		}
	}
}
