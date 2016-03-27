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

public class WtTableRow
		extends
			WtInnerNode2
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtTableRow()
	{
		super(Uninitialized.X);
	}

	protected WtTableRow(WtXmlAttributes xmlAttributes, WtBody body)
	{
		super(xmlAttributes, body);
	}

	@Override
	public int getNodeType()
	{
		return NT_TABLE_ROW;
	}

	// =========================================================================
	// Properties

	private boolean implicit;

	public final boolean isImplicit()
	{
		return this.implicit;
	}

	public final void setImplicit(boolean implicit)
	{
		this.implicit = implicit;
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
				return WtTableRow.this.getPropertyCount();
			}

			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "implicit";

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
						return WtTableRow.this.isImplicit();

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
						boolean old = WtTableRow.this.isImplicit();
						WtTableRow.this.setImplicit((Boolean) value);
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

	public final void setXmlAttributes(WtXmlAttributes xmlAttributes)
	{
		set(0, xmlAttributes);
	}

	public final WtXmlAttributes getXmlAttributes()
	{
		return (WtXmlAttributes) get(0);
	}

	public final void setBody(WtBody body)
	{
		set(1, body);
	}

	public final WtBody getBody()
	{
		return (WtBody) get(1);
	}

	private static final String[] CHILD_NAMES = new String[] { "xmlAttributes", "body" };

	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
