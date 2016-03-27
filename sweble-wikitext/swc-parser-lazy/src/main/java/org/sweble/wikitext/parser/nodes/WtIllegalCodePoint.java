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

public class WtIllegalCodePoint
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtIllegalCodePoint()
	{
	}

	protected WtIllegalCodePoint(String codePoint, IllegalCodePointType type)
	{
		setCodePoint(codePoint);
		setType(type);
	}

	@Override
	public int getNodeType()
	{
		return NT_ILLEGAL_CODE_POINT;
	}

	// =========================================================================
	// Properties

	private String codePoint;

	public final String getCodePoint()
	{
		return this.codePoint;
	}

	public final void setCodePoint(String codePoint)
	{
		if (codePoint == null)
			throw new NullPointerException();
		this.codePoint = codePoint;
	}

	private IllegalCodePointType type;

	public final IllegalCodePointType getType()
	{
		return this.type;
	}

	public final void setType(IllegalCodePointType type)
	{
		if (type == null)
			throw new NullPointerException();
		this.type = type;
	}

	@Override
	public final int getPropertyCount()
	{
		return 2 + getSuperPropertyCount();
	}

	public int getSuperPropertyCount()
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
				return WtIllegalCodePoint.this.getPropertyCount();
			}

			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "codePoint";
					case 1:
						return "type";

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
						return WtIllegalCodePoint.this.getCodePoint();
					case 1:
						return WtIllegalCodePoint.this.getType();

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
						String old = WtIllegalCodePoint.this.getCodePoint();
						WtIllegalCodePoint.this.setCodePoint((String) value);
						return old;
					}
					case 1:
					{
						IllegalCodePointType old = WtIllegalCodePoint.this.getType();
						WtIllegalCodePoint.this.setType((IllegalCodePointType) value);
						return old;
					}

					default:
						return super.setValue(index, value);
				}
			}
		};
	}

	// =========================================================================

	public enum IllegalCodePointType
	{
		ISOLATED_SURROGATE,
		NON_CHARACTER,
		PRIVATE_USE_CHARACTER,
		CONTROL_CHARACTER
	}
}
