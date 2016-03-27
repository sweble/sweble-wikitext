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

public class WtLinkOptionKeyword
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtLinkOptionKeyword()
	{
	}

	protected WtLinkOptionKeyword(String keyword)
	{
		setKeyword(keyword);
	}

	@Override
	public int getNodeType()
	{
		return NT_LINK_OPTION_KEYWORD;
	}

	// =========================================================================
	// Properties

	private String keyword;

	public final String getKeyword()
	{
		return this.keyword;
	}

	public final void setKeyword(String keyword)
	{
		if (keyword == null)
			throw new NullPointerException();
		this.keyword = keyword;
	}

	@Override
	public final int getPropertyCount()
	{
		return 1 + getSuperPropertyCount();
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
				return WtLinkOptionKeyword.this.getPropertyCount();
			}

			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "keyword";

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
						return WtLinkOptionKeyword.this.getKeyword();

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
						String old = WtLinkOptionKeyword.this.getKeyword();
						WtLinkOptionKeyword.this.setKeyword((String) value);
						return old;
					}

					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
