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

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.Uninitialized;

public class WtSection
		extends
			WtInnerNode2
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtSection()
	{
		super(Uninitialized.X);

	}

	protected WtSection(int level, WtHeading heading)
	{
		super(heading, WtBody.NO_BODY);
		setLevel(level);

	}

	protected WtSection(int level, WtHeading heading, WtBody body)
	{
		super(heading, body);
		setLevel(level);

	}

	@Override
	public int getNodeType()
	{
		return NT_SECTION;
	}

	// =========================================================================
	// Properties

	private int level;

	public final int getLevel()
	{
		return this.level;
	}

	public final void setLevel(int level)
	{
		this.level = level;
	}

	@Override
	public final int getPropertyCount()
	{
		return 1 + getSuperPropertyCount();
	}

	public final int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}

	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new WtInnerNode2PropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtSection.this.getPropertyCount();
			}

			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "level";

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
						return WtSection.this.getLevel();

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
						int old = WtSection.this.getLevel();
						WtSection.this.setLevel((Integer) value);
						return old;
					}

					default:
						return super.setValue(index, value);
				}
			}
		};
	}

	// =========================================================================
	// Children

	public final void setHeading(WtHeading title)
	{
		set(0, title);
	}

	public final WtHeading getHeading()
	{
		return (WtHeading) get(0);
	}

	public final boolean hasBody()
	{
		return getBody() != WtBody.NO_BODY;
	}

	public final void setBody(WtBody body)
	{
		set(1, body);
	}

	public final WtBody getBody()
	{
		return (WtBody) get(1);
	}

	private static final String[] CHILD_NAMES = new String[] { "heading", "body" };

	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
