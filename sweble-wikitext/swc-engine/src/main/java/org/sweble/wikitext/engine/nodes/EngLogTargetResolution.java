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

public abstract class EngLogTargetResolution
		extends
			EngLogResolution
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	public EngLogTargetResolution()
	{
	}

	public EngLogTargetResolution(String target, boolean success)
	{
		super(success);
		setTarget(target);
	}

	// =========================================================================
	// Properties

	private String target;

	public final String getTarget()
	{
		return this.target;
	}

	public final void setTarget(String target)
	{
		this.target = target;
	}

	private String canonical;

	public final String getCanonical()
	{
		return this.canonical;
	}

	public final void setCanonical(String canonical)
	{
		this.canonical = canonical;
	}

	@Override
	public int getPropertyCount()
	{
		return 2 + getSuperPropertyCount();
	}

	private final int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}

	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new EngLogResolutionPropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return EngLogTargetResolution.this.getPropertyCount();
			}

			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "target";
					case 1:
						return "canonical";

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
						return EngLogTargetResolution.this.getTarget();
					case 1:
						return EngLogTargetResolution.this.getCanonical();

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
						String old = EngLogTargetResolution.this.getTarget();
						EngLogTargetResolution.this.setTarget((String) value);
						return old;
					}
					case 1:
					{
						String old = EngLogTargetResolution.this.getCanonical();
						EngLogTargetResolution.this.setCanonical((String) value);
						return old;
					}

					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
