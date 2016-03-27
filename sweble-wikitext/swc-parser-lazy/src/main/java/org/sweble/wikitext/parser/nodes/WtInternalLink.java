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

public class WtInternalLink
		extends
			WtInnerNode2
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtInternalLink()
	{
		super(Uninitialized.X);
	}

	protected WtInternalLink(
			String prefix,
			WtPageName target,
			String postfix)
	{
		super(target, WtLinkTitle.NO_TITLE);
		setPrefix(prefix);
		setPostfix(postfix);
	}

	@Override
	public int getNodeType()
	{
		return NT_INTERNAL_LINK;
	}

	// =========================================================================
	// Properties

	private String prefix;

	public final String getPrefix()
	{
		return this.prefix;
	}

	public final void setPrefix(String prefix)
	{
		if (prefix == null)
			throw new NullPointerException();
		this.prefix = prefix;
	}

	private String postfix;

	public final String getPostfix()
	{
		return this.postfix;
	}

	public final void setPostfix(String postfix)
	{
		if (postfix == null)
			throw new NullPointerException();
		this.postfix = postfix;
	}

	@Override
	public final int getPropertyCount()
	{
		return 2 + getSuperPropertyCount();
	}

	private final int getSuperPropertyCount()
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
				return WtInternalLink.this.getPropertyCount();
			}

			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "prefix";
					case 1:
						return "postfix";

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
						return WtInternalLink.this.getPrefix();
					case 1:
						return WtInternalLink.this.getPostfix();

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
						String old = WtInternalLink.this.getPrefix();
						WtInternalLink.this.setPrefix((String) value);
						return old;
					}
					case 1:
					{
						String old = WtInternalLink.this.getPostfix();
						WtInternalLink.this.setPostfix((String) value);
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

	public final void setTarget(WtPageName target)
	{
		set(0, target);
	}

	public final WtPageName getTarget()
	{
		return (WtPageName) get(0);
	}

	public boolean hasTitle()
	{
		return getTitle() != WtLinkTitle.NO_TITLE;
	}

	public final void setTitle(WtLinkTitle title)
	{
		set(1, title);
	}

	public final WtLinkTitle getTitle()
	{
		return (WtLinkTitle) get(1);
	}

	private static final String[] CHILD_NAMES = new String[] { "target", "title" };

	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
