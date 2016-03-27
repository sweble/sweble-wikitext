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

import org.sweble.wikitext.parser.WtRtData;

import de.fau.cs.osr.ptk.common.ast.AstLeafNodeImpl;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public abstract class WtLeafNode
		extends
			AstLeafNodeImpl<WtNode>
		implements
			WtNode
{
	private static final long serialVersionUID = -2024251471331960556L;

	private WtRtData rtd = null;

	// =========================================================================

	@Override
	public void setRtd(WtRtData rtd)
	{
		if (rtd != null && rtd.size() != this.size() + 1)
			throw new IllegalArgumentException();
		this.rtd = rtd;
	}

	@Override
	public void setRtd(Object... glue)
	{
		rtd = new WtRtData(this, glue);
	}

	@Override
	public void setRtd(String... glue)
	{
		rtd = new WtRtData(this, glue);
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

	// =========================================================================

	@Override
	public int getPropertyCount()
	{
		return 1;
	}

	@Override
	public AstNodePropertyIterator propertyIterator()
	{
		return new WtLeafNodePropertyIterator();
	}

	protected class WtLeafNodePropertyIterator
			extends
				AstNodePropertyIterator
	{
		@Override
		protected int getPropertyCount()
		{
			return WtLeafNode.this.getPropertyCount();
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
					return WtLeafNode.this.getRtd();

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
					WtRtData old = WtLeafNode.this.getRtd();
					WtLeafNode.this.setRtd((WtRtData) value);
					return old;
				}

				default:
					throw new IndexOutOfBoundsException();
			}
		}
	}
}
