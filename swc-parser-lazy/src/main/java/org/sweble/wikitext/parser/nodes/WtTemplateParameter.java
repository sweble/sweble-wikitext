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

import org.sweble.wikitext.parser.nodes.WtValue.WtNullValue;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.Uninitialized;

public class WtTemplateParameter
		extends
			WtInnerNode3
		implements
			WtPreproNode
{
	private static final long serialVersionUID = 1L;
	
	public static final WtNullValue NO_DEFAULT_VALUE = WtValue.NULL;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtTemplateParameter()
	{
		super(Uninitialized.X);
	}
	
	public WtTemplateParameter(WtName name)
	{
		super(name, NO_DEFAULT_VALUE, WtTemplateArguments.EMPTY);
	}
	
	public WtTemplateParameter(WtName name, WtValue defaultValue)
	{
		super(name, defaultValue, WtTemplateArguments.EMPTY);
	}
	
	public WtTemplateParameter(
			WtName name,
			WtValue defaultValue,
			WtTemplateArguments garbage)
	{
		super(name, defaultValue, garbage);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_TEMPLATE_PARAMETER;
	}
	
	// =========================================================================
	// Properties
	
	private boolean precededByNewline;
	
	public final boolean getPrecededByNewline()
	{
		return this.precededByNewline;
	}
	
	public final boolean setPrecededByNewline(boolean precededByNewline)
	{
		boolean old = this.precededByNewline;
		this.precededByNewline = precededByNewline;
		return old;
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
		return new WtInnerNode3PropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtTemplateParameter.this.getPropertyCount();
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
						return WtTemplateParameter.this.getPrecededByNewline();
						
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
						return WtTemplateParameter.this.setPrecededByNewline((Boolean) value);
						
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
	
	public final boolean hasDefaultValue()
	{
		return getDefaultValue() != NO_DEFAULT_VALUE;
	}
	
	public final void setDefaultValue(WtValue defaultValue)
	{
		set(1, defaultValue);
	}
	
	public final WtValue getDefaultValue()
	{
		return (WtValue) get(1);
	}
	
	public final void setGarbage(WtTemplateArguments garbage)
	{
		set(2, garbage);
	}
	
	public final WtTemplateArguments getGarbage()
	{
		return (WtTemplateArguments) get(2);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "name", "defaultValue", "garbage" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
