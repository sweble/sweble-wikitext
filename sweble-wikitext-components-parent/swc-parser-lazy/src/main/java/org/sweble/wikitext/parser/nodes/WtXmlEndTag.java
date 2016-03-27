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

import java.io.IOException;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class WtXmlEndTag
		extends
			WtLeafNode
		implements
			WtNamedXmlElement,
			WtIntermediate
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtXmlEndTag()
	{
	}

	protected WtXmlEndTag(String name)
	{
		setName(name);
	}

	@Override
	public int getNodeType()
	{
		return NT_XML_END_TAG;
	}

	// =========================================================================
	// Properties

	private String name;

	public final String getName()
	{
		return this.name;
	}

	public final void setName(String name)
	{
		if (name == null)
			throw new NullPointerException();
		this.name = name;
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
		return new WtXmlEndTagPropertyIterator();
	}

	// =========================================================================

	protected class WtXmlEndTagPropertyIterator
			extends
				WtLeafNodePropertyIterator
	{
		@Override
		protected int getPropertyCount()
		{
			return WtXmlEndTag.this.getPropertyCount();
		}

		@Override
		protected String getName(int index)
		{
			switch (index - getSuperPropertyCount())
			{
				case 0:
					return "name";

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
					return WtXmlEndTag.this.getName();

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
					String old = WtXmlEndTag.this.getName();
					WtXmlEndTag.this.setName((String) value);
					return old;
				}

				default:
					return super.setValue(index, value);
			}
		}
	}

	// =========================================================================

	@Override
	public void toString(Appendable out) throws IOException
	{
		out.append(getClass().getSimpleName());
		out.append('[');
		out.append(name);
		out.append(']');
		out.append('(');

		boolean first = true;
		for (WtNode node : this)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				out.append(", ");
			}

			node.toString(out);

		}

		out.append(')');
	}
}
