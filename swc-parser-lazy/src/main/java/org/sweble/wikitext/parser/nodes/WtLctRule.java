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

public class WtLctRule
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtLctRule()
	{
	}
	
	protected WtLctRule(String search, String variant, String replace)
	{
		if (search == null || search.isEmpty())
			throw new IllegalArgumentException();
		setSearch(search);
		setVariant(variant);
		setReplace(replace);
	}
	
	protected WtLctRule(String variant, String replace)
	{
		setSearch("");
		setVariant(variant);
		setReplace(replace);
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
	
	public final String setSearch(String search)
	{
		this.directConvert = search.isEmpty();
		String old = this.search;
		this.search = search;
		return old;
	}
	
	private String variant;
	
	public final String getVariant()
	{
		return this.variant;
	}
	
	public final String setVariant(String variant)
	{
		String old = this.variant;
		this.variant = variant;
		return old;
	}
	
	private String replace;
	
	public final String getReplace()
	{
		return this.replace;
	}
	
	public final String setReplace(String replace)
	{
		String old = this.replace;
		this.replace = replace;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 4 + getSuperPropertyCount();
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
					case 3:
						return "replace";
						
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
					case 3:
						return WtLctRule.this.getReplace();
						
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
						return WtLctRule.this.setSearch((String) value);
					case 2:
						return WtLctRule.this.setVariant((String) value);
					case 3:
						return WtLctRule.this.setReplace((String) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
