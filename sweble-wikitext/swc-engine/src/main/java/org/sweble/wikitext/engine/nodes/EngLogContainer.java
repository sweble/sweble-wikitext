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

package org.sweble.wikitext.engine.nodes;

import org.sweble.wikitext.parser.nodes.WtContentNode;
import org.sweble.wikitext.parser.nodes.WtNodeList.WtNodeListImpl;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public abstract class EngLogContainer
		extends
			WtNodeListImpl
		implements
			WtContentNode,
			EngLogNode
{
	private static final long serialVersionUID = -1365120120054529928L;

	// =========================================================================
	// Properties

	private Long timeNeeded;

	public final Long getTimeNeeded()
	{
		return this.timeNeeded;
	}

	public final void setTimeNeeded(Long timeNeeded)
	{
		this.timeNeeded = timeNeeded;
	}

	@Override
	public int getPropertyCount()
	{
		return 1;
	}

	@Override
	public AstNodePropertyIterator propertyIterator()
	{
		return new EngLogContainerPropertyIterator();
	}

	protected class EngLogContainerPropertyIterator
			extends
				AstNodePropertyIterator
	{
		@Override
		protected int getPropertyCount()
		{
			return EngLogContainer.this.getPropertyCount();
		}

		@Override
		protected String getName(int index)
		{
			switch (index)
			{
				case 0:
					return "timeNeeded";

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
					return EngLogContainer.this.getTimeNeeded();

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
					Long old = EngLogContainer.this.getTimeNeeded();
					EngLogContainer.this.setTimeNeeded((Long) value);
					return old;
				}

				default:
					throw new IndexOutOfBoundsException();
			}
		}
	}
}
