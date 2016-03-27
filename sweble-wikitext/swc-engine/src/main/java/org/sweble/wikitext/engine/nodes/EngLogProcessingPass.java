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

public class EngLogProcessingPass
		extends
			EngLogPass
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	protected EngLogProcessingPass()
	{
	}

	// =========================================================================
	// Properties

	private String title;

	public final String getTitle()
	{
		return this.title;
	}

	public final void setTitle(String title)
	{
		this.title = title;
	}

	private Long revision;

	public final Long getRevision()
	{
		return this.revision;
	}

	public final void setRevision(Long revision)
	{
		this.revision = revision;
	}

	@Override
	public final int getPropertyCount()
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
		return new EngLogContainerPropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return EngLogProcessingPass.this.getPropertyCount();
			}

			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "title";
					case 1:
						return "revision";

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
						return EngLogProcessingPass.this.getTitle();
					case 1:
						return EngLogProcessingPass.this.getRevision();

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
						Object old = EngLogProcessingPass.this.getTitle();
						EngLogProcessingPass.this.setTitle((String) value);
						return old;
					}
					case 1:
					{
						Object old = EngLogProcessingPass.this.getRevision();
						EngLogProcessingPass.this.setRevision((Long) value);
						return old;
					}

					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
