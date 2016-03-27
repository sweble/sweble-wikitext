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

public class WtUrl
		extends
			WtLeafNode
		implements
			WtLinkTarget
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtUrl()
	{
	}

	protected WtUrl(String protocol, String path)
	{
		setProtocol(protocol);
		setPath(path);
	}

	@Override
	public int getNodeType()
	{
		return NT_URL;
	}

	@Override
	public LinkTargetType getTargetType()
	{
		return LinkTargetType.URL;
	}

	// =========================================================================
	// Properties

	private String protocol;

	public final String getProtocol()
	{
		return this.protocol;
	}

	public final void setProtocol(String protocol)
	{
		if (protocol == null)
			throw new NullPointerException();
		this.protocol = protocol;
	}

	private String path;

	public final String getPath()
	{
		return this.path;
	}

	public final void setPath(String path)
	{
		if (path == null)
			throw new NullPointerException();
		this.path = path;
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
				return WtUrl.this.getPropertyCount();
			}

			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "protocol";
					case 1:
						return "path";

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
						return WtUrl.this.getProtocol();
					case 1:
						return WtUrl.this.getPath();

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
						String old = WtUrl.this.getProtocol();
						WtUrl.this.setProtocol((String) value);
						return old;
					}
					case 1:
					{
						String old = WtUrl.this.getPath();
						WtUrl.this.setPath((String) value);
						return old;
					}

					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
