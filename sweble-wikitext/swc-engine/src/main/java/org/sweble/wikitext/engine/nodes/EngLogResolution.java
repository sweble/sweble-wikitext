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

public abstract class EngLogResolution
		extends
			EngLogContainer
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	protected EngLogResolution()
	{
	}

	protected EngLogResolution(boolean success)
	{
		setSuccess(success);
	}

	// =========================================================================
	// Properties

	private boolean success;

	public final boolean getSuccess()
	{
		return this.success;
	}

	public final void setSuccess(boolean success)
	{
		this.success = success;
	}

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
		return new EngLogResolutionPropertyIterator();
	}

	protected class EngLogResolutionPropertyIterator
			extends
				EngLogContainerPropertyIterator
	{
		@Override
		protected int getPropertyCount()
		{
			return EngLogResolution.this.getPropertyCount();
		}

		@Override
		protected String getName(int index)
		{
			switch (index - getSuperPropertyCount())
			{
				case 0:
					return "success";

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
					return EngLogResolution.this.getSuccess();

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
					boolean old = EngLogResolution.this.getSuccess();
					EngLogResolution.this.setSuccess((Boolean) value);
					return old;
				}

				default:
					return super.setValue(index, value);
			}
		}
	}
}
