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

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class EngLogPreprocessorPass
		extends
			EngLogPass
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	protected EngLogPreprocessorPass()
	{
	}

	// =========================================================================
	// Properties

	private boolean forInclusion;

	public final boolean getForInclusion()
	{
		return this.forInclusion;
	}

	public final void setForInclusion(boolean forInclusion)
	{
		this.forInclusion = forInclusion;
	}

	@Override
	public final int getPropertyCount()
	{
		return 1 + getSuperPropertyCount();
	}

	private final int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}

	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new EngLogContainerPropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return EngLogPreprocessorPass.this.getPropertyCount();
			}

			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "forInclusion";

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
						return EngLogPreprocessorPass.this.getForInclusion();

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
						boolean old = EngLogPreprocessorPass.this.getForInclusion();
						EngLogPreprocessorPass.this.setForInclusion((Boolean) value);
						return old;
					}

					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
