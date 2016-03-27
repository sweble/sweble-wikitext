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

public class WtTemplate
		extends
			WtInnerNode2
		implements
			WtPreproNode
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtTemplate()
	{
		super(Uninitialized.X);
	}

	protected WtTemplate(WtName name, WtTemplateArguments args)
	{
		super(name, args);
	}

	@Override
	public int getNodeType()
	{
		return NT_TEMPLATE;
	}

	// =========================================================================
	// Properties

	private boolean precededByNewline;

	/**
	 * @deprecated
	 */
	public final boolean getPrecededByNewline()
	{
		return this.precededByNewline;
	}

	public final boolean isPrecededByNewline()
	{
		return this.precededByNewline;
	}

	public final void setPrecededByNewline(boolean precededByNewline)
	{
		this.precededByNewline = precededByNewline;
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
		return new WtInnerNode2PropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtTemplate.this.getPropertyCount();
			}

			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "precededByNewline";

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
						return WtTemplate.this.isPrecededByNewline();

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
						boolean old = WtTemplate.this.isPrecededByNewline();
						WtTemplate.this.setPrecededByNewline((Boolean) value);
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

	public final void setName(WtName name)
	{
		set(0, name);
	}

	public final WtName getName()
	{
		return (WtName) get(0);
	}

	public final void setArgs(WtTemplateArguments args)
	{
		set(1, args);
	}

	public final WtTemplateArguments getArgs()
	{
		return (WtTemplateArguments) get(1);
	}

	private static final String[] CHILD_NAMES = new String[] { "name", "args" };

	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
