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

public class WtLinkOptionResize
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtLinkOptionResize()
	{
	}

	protected WtLinkOptionResize(int width, int height)
	{
		setWidth(width);
		setHeight(height);
	}

	@Override
	public int getNodeType()
	{
		return NT_LINK_OPTION_RESIZE;
	}

	// =========================================================================
	// Properties

	private int width;

	public final int getWidth()
	{
		return this.width;
	}

	public final void setWidth(int width)
	{
		this.width = width;
	}

	private int height;

	public final int getHeight()
	{
		return this.height;
	}

	public final void setHeight(int height)
	{
		this.height = height;
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
		return new WtLeafNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtLinkOptionResize.this.getPropertyCount();
			}

			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "width";
					case 1:
						return "height";

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
						return WtLinkOptionResize.this.getWidth();
					case 1:
						return WtLinkOptionResize.this.getHeight();

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
						int old = WtLinkOptionResize.this.getWidth();
						WtLinkOptionResize.this.setWidth((Integer) value);
						return old;
					}
					case 1:
					{
						int old = WtLinkOptionResize.this.getHeight();
						WtLinkOptionResize.this.setHeight((Integer) value);
						return old;
					}

					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
