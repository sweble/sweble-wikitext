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

import org.sweble.wikitext.parser.nodes.WtContentNode.WtContentNodeImpl;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class WtWhitespace
		extends
			WtContentNodeImpl
		implements
			WtIntermediate
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtWhitespace()
	{
	}

	protected WtWhitespace(WtNodeList content, boolean hasNewline)
	{
		super(content);
		setHasNewline(hasNewline);
	}

	@Override
	public int getNodeType()
	{
		return NT_WHITESPACE;
	}

	// =========================================================================
	// Properties

	private boolean hasNewline;

	public final boolean getHasNewline()
	{
		return this.hasNewline;
	}

	public final void setHasNewline(boolean hasNewline)
	{
		this.hasNewline = hasNewline;
	}

	@Override
	public final int getPropertyCount()
	{
		return 1 + getSuperPropertyCount();
	}

	public int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}

	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new WtContentNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtWhitespace.this.getPropertyCount();
			}

			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "hasNewline";

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
						return WtWhitespace.this.getHasNewline();

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
						boolean old = WtWhitespace.this.getHasNewline();
						WtWhitespace.this.setHasNewline((Boolean) value);
						return old;
					}

					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
