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

public class WtOnlyInclude
		extends
			WtContentNodeImpl
		implements
			WtPreproNode
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtOnlyInclude()
	{
	}

	protected WtOnlyInclude(WtNodeList content, XmlElementType elementType)
	{
		super(content);
		setElementType(elementType);
	}

	@Override
	public int getNodeType()
	{
		return NT_ONLY_INCLUDE;
	}

	// =========================================================================
	// Properties

	private XmlElementType elementType;

	public final XmlElementType getElementType()
	{
		return this.elementType;
	}

	public final void setElementType(XmlElementType elementType)
	{
		this.elementType = elementType;
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
				return WtOnlyInclude.this.getPropertyCount();
			}

			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "elementType";

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
						return WtOnlyInclude.this.getElementType();

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
						XmlElementType old = WtOnlyInclude.this.getElementType();
						WtOnlyInclude.this.setElementType((XmlElementType) value);
						return old;
					}

					default:
						return super.setValue(index, value);
				}
			}
		};
	}

	// =========================================================================

	public static enum XmlElementType
	{
		FULL_ELEMENT,
		EMPTY_ELEMENT,
		UNCLOSED_ELEMENT,
	}
}
