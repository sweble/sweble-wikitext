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

public class WtXmlEntityRef
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtXmlEntityRef()
	{
	}

	/**
	 * @param resolved
	 *            <code>null</code> allowed to indicate failed resolution.
	 */
	protected WtXmlEntityRef(String name, String resolved)
	{
		setName(name);
		setResolved(resolved);
	}

	@Override
	public int getNodeType()
	{
		return NT_XML_ENTITY_REF;
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

	private String resolved;

	public final String getResolved()
	{
		return this.resolved;
	}

	public final void setResolved(String resolved)
	{
		this.resolved = resolved;
	}

	@Override
	public final int getPropertyCount()
	{
		return 2 + getSuperPropertyCount();
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
				return WtXmlEntityRef.this.getPropertyCount();
			}

			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "name";
					case 1:
						return "resolved";

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
						return WtXmlEntityRef.this.getName();
					case 1:
						return WtXmlEntityRef.this.getResolved();

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
						String old = WtXmlEntityRef.this.getName();
						WtXmlEntityRef.this.setName((String) value);
						return old;
					}
					case 1:
					{
						String old = WtXmlEntityRef.this.getResolved();
						WtXmlEntityRef.this.setResolved((String) value);
						return old;
					}

					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
