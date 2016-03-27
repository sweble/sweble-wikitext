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

import java.io.IOException;
import java.util.Iterator;

import org.sweble.wikitext.parser.WtRtData;

import de.fau.cs.osr.ptk.common.ast.AstNodeList;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public interface WtContentNode
		extends
			WtNodeList
{
	public abstract class WtAbsentContentNode
			extends
				WtEmptyImmutableNode
			implements
				WtContentNode
	{
		private static final long serialVersionUID = 2465445739660029292L;

		protected WtAbsentContentNode()
		{
		}

		@Override
		public abstract int getNodeType();

		@Override
		public abstract String getNodeName();

		@Override
		public void exchange(AstNodeList<WtNode> other)
		{
			throw new UnsupportedOperationException(genMsg());
		}

		@Override
		public boolean indicatesAbsence()
		{
			return true;
		}
	}

	// =====================================================================

	public abstract class WtEmptyContentNode
			extends
				WtEmptyImmutableNode
			implements
				WtContentNode
	{
		private static final long serialVersionUID = 2465445739660029292L;

		protected WtEmptyContentNode()
		{
		}

		@Override
		public abstract int getNodeType();

		@Override
		public abstract String getNodeName();

		@Override
		public void exchange(AstNodeList<WtNode> other)
		{
			throw new UnsupportedOperationException(genMsg());
		}

		@Override
		public boolean indicatesAbsence()
		{
			return false;
		}
	}

	// =====================================================================

	public abstract class WtContentNodeImpl
			extends
				WtNodeListImpl
			implements
				WtContentNode
	{
		private static final long serialVersionUID = 3407356901471138122L;

		private WtRtData rtd = null;

		// =====================================================================

		protected WtContentNodeImpl()
		{
		}

		protected WtContentNodeImpl(WtNodeList content)
		{
			super(content);
		}

		// =====================================================================

		@Override
		public void setRtd(WtRtData rtd)
		{
			if (rtd != null && rtd.size() != 2)
				throw new IllegalArgumentException();
			this.rtd = rtd;
		}

		@Override
		public void setRtd(Object... glue)
		{
			rtd = new WtRtData(2, glue);
		}

		@Override
		public void setRtd(String... glue)
		{
			rtd = new WtRtData(2, glue);
		}

		@Override
		public WtRtData getRtd()
		{
			return rtd;
		}

		@Override
		public void clearRtd()
		{
			rtd = null;
		}

		@Override
		public void suppressRtd()
		{
			rtd = WtRtData.SUPPRESS;
		}

		// =====================================================================

		@Override
		public int getPropertyCount()
		{
			return 1;
		}

		@Override
		public AstNodePropertyIterator propertyIterator()
		{
			return new WtContentNodePropertyIterator();
		}

		public class WtContentNodePropertyIterator
				extends
					AstNodePropertyIterator
		{
			@Override
			protected int getPropertyCount()
			{
				return WtContentNodeImpl.this.getPropertyCount();
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
						return WtContentNodeImpl.this.getRtd();

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
						WtRtData old = WtContentNodeImpl.this.getRtd();
						WtContentNodeImpl.this.setRtd((WtRtData) value);
						return old;
					}

					default:
						throw new IndexOutOfBoundsException();
				}
			}
		}

		// =====================================================================

		@Override
		public void toString(Appendable out) throws IOException
		{
			out.append(getNodeName());
			out.append('[');

			for (Iterator<WtNode> i = this.iterator(); i.hasNext();)
			{
				i.next().toString(out);
				if (i.hasNext())
					out.append(", ");
			}

			out.append(']');
		}
	}
}
