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

public class WtXmlCharRef
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtXmlCharRef()
	{
	}

	protected WtXmlCharRef(int codePoint)
	{
		setCodePoint(codePoint);
	}

	@Override
	public int getNodeType()
	{
		return NT_XML_CHAR_REF;
	}

	// =========================================================================
	// Properties

	private int codePoint;

	public final int getCodePoint()
	{
		return this.codePoint;
	}

	public final void setCodePoint(int codePoint)
	{
		this.codePoint = codePoint;
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
		return new WtLeafNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtXmlCharRef.this.getPropertyCount();
			}

			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "codePoint";

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
						return WtXmlCharRef.this.getCodePoint();

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
						int old = WtXmlCharRef.this.getCodePoint();
						WtXmlCharRef.this.setCodePoint((Integer) value);
						return old;
					}

					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
