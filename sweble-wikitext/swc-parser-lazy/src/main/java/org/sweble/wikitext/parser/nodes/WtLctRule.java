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

public class WtLctRule
		extends
			WtInnerNode1
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtLctRule()
	{
		super(Uninitialized.X);
	}

	protected WtLctRule(String search, String variant, WtLctRuleText replace)
	{
		super(replace);
		if (search == null || search.isEmpty())
			throw new IllegalArgumentException();
		setSearch(search);
		setVariant(variant);
	}

	protected WtLctRule(String variant, WtLctRuleText replace)
	{
		super(replace);
		setSearch("");
		setVariant(variant);
	}

	@Override
	public int getNodeType()
	{
		return NT_LCT_RULE;
	}

	// =========================================================================
	// Properties

	private boolean directConvert;

	public final boolean isDirectConvert()
	{
		return this.directConvert;
	}

	private String search;

	public final String getSearch()
	{
		return this.search;
	}

	public final void setSearch(String search)
	{
		this.directConvert = search.isEmpty();
		this.search = search;
	}

	private String variant;

	public final String getVariant()
	{
		return this.variant;
	}

	public final void setVariant(String variant)
	{
		this.variant = variant;
	}

	@Override
	public final int getPropertyCount()
	{
		return 3 + getSuperPropertyCount();
	}

	private final int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}

	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new WtInnerNode1PropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtLctRule.this.getPropertyCount();
			}

			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "directConvert";
					case 1:
						return "search";
					case 2:
						return "variant";

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
						return WtLctRule.this.isDirectConvert();
					case 1:
						return WtLctRule.this.getSearch();
					case 2:
						return WtLctRule.this.getVariant();

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
						throw new UnsupportedOperationException(
								"You cannot set variable `directConvert' directly! " +
										"Use setSearch() instead.");
					case 1:
					{
						String old = WtLctRule.this.getSearch();
						WtLctRule.this.setSearch((String) value);
						return old;
					}
					case 2:
					{
						String old = WtLctRule.this.getVariant();
						WtLctRule.this.setVariant((String) value);
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

	public final void setReplace(WtLctRuleText replace)
	{
		set(0, replace);
	}

	public final WtLctRuleText getReplace()
	{
		return (WtLctRuleText) get(0);
	}

	private static final String[] CHILD_NAMES = new String[] { "replace" };

	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
