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

import java.io.ObjectStreamException;
import java.util.Collection;

import org.sweble.wikitext.parser.WtRtData;
import org.sweble.wikitext.parser.nodes.WtContentNode.WtEmptyContentNode;

import de.fau.cs.osr.ptk.common.ast.AstNodeImpl;
import de.fau.cs.osr.ptk.common.ast.AstNodeList;
import de.fau.cs.osr.ptk.common.ast.AstNodeListImpl;
import xtc.util.Pair;

public interface WtNodeList
		extends
			WtNode,
			AstNodeList<WtNode>
{
	public static final WtEmptyNodeList EMPTY = new WtEmptyNodeList();

	// =========================================================================

	public class WtEmptyNodeList
			extends
				WtEmptyContentNode
			implements
				WtNodeList
	{
		private static final long serialVersionUID = 2465445739660029292L;

		private WtEmptyNodeList()
		{
		}

		@Override
		public int getNodeType()
		{
			return NT_NODE_LIST;
		}

		@Override
		public String getNodeName()
		{
			return WtNodeList.class.getSimpleName();
		}

		@Override
		public boolean equals(Object other)
		{
			if (this == other)
				return true;
			if (other instanceof WtNodeListImpl)
				return AstNodeImpl.equalsNoTypeCheck(this, (WtNodeListImpl) other);
			return super.equals(other);
		}

		protected Object readResolve() throws ObjectStreamException
		{
			return WtNodeList.EMPTY;
		}
	}

	// =========================================================================

	public class WtNodeListImpl
			extends
				AstNodeListImpl<WtNode>
			implements
				WtNodeList
	{
		private static final long serialVersionUID = 6285729315278264384L;

		// =====================================================================

		protected WtNodeListImpl()
		{
		}

		protected WtNodeListImpl(Collection<? extends WtNode> list)
		{
			super(list);
		}

		protected WtNodeListImpl(Pair<? extends WtNode> list)
		{
			super(list);
		}

		protected WtNodeListImpl(WtNode child)
		{
			super(child);
		}

		protected WtNodeListImpl(Object... content)
		{
			for (Object o : content)
			{
				if (o == null)
				{
					continue;
				}
				else if (o instanceof WtNode)
				{
					add((WtNode) o);
				}
				else if (o instanceof Pair)
				{
					@SuppressWarnings("unchecked")
					Pair<? extends WtNode> cast = (Pair<? extends WtNode>) o;
					addAll(cast);
				}
				else if (o instanceof Collection)
				{
					@SuppressWarnings("unchecked")
					Collection<? extends WtNode> cast = (Collection<? extends WtNode>) o;
					addAll(cast);
				}
				else
				{
					throw new IllegalArgumentException("Can't add object of type: " + o.getClass().getName());
				}
			}
		}

		// =====================================================================

		@Override
		public String getNodeName()
		{
			return (getClass() == WtNodeListImpl.class) ?
					WtNodeList.class.getSimpleName() :
					super.getNodeName();
		}

		// =====================================================================

		@Override
		public void setRtd(WtRtData rtd)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRtd(Object... glue)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRtd(String... glue)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public WtRtData getRtd()
		{
			return null;
		}

		@Override
		public void clearRtd()
		{
		}

		@Override
		public void suppressRtd()
		{
			throw new UnsupportedOperationException();
		}

		// =====================================================================

		@Override
		public boolean equals(Object other)
		{
			if (this == other)
				return true;
			if (other instanceof WtEmptyNodeList)
				return AstNodeImpl.equalsNoTypeCheck(this, (WtEmptyNodeList) other);
			return super.equals(other);
		}
	}
}
