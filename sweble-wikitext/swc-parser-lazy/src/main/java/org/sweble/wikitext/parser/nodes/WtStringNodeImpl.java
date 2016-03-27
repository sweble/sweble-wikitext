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

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.AstStringNodeImpl;
import de.fau.cs.osr.ptk.common.ast.Uninitialized;

public abstract class WtStringNodeImpl
		extends
			AstStringNodeImpl<WtNode>
		implements
			WtStringNode
{
	private static final long serialVersionUID = -2087712873453224402L;

	private WtRtData rtd = null;

	// =========================================================================

	protected WtStringNodeImpl(Uninitialized u)
	{
		super(u);
	}

	protected WtStringNodeImpl(String content)
	{
		super(content);
	}

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
		return 1 + getSuperPropertyCount();
	}

	private final int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}

	@Override
	public AstNodePropertyIterator propertyIterator()
	{
		return new WtStringContentNodePropertyIterator();
	}

	protected class WtStringContentNodePropertyIterator
			extends
				StringContentNodePropertyIterator
	{
		@Override
		protected int getPropertyCount()
		{
			return WtStringNodeImpl.this.getPropertyCount();
		}

		@Override
		protected String getName(int index)
		{
			switch (index - getSuperPropertyCount())
			{
				case 0:
					return "rtd";

				default:
					return super.getName(index);
			}
		}

		@Override
		protected Object getValue(int index)
		{
			switch (index - getSuperPropertyCount())
			{
				case 0:
					return WtStringNodeImpl.this.getRtd();

				default:
					return super.getValue(index);
			}
		}

		@Override
		protected Object setValue(int index, Object value)
		{
			switch (index - getSuperPropertyCount())
			{
				case 0:
				{
					WtRtData old = WtStringNodeImpl.this.getRtd();
					WtStringNodeImpl.this.setRtd((WtRtData) value);
					return old;
				}

				default:
					return super.setValue(index, value);
			}
		}
	}
}
