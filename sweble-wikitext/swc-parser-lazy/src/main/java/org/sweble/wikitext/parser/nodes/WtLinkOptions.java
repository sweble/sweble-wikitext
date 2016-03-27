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

import java.io.ObjectStreamException;

import de.fau.cs.osr.ptk.common.ast.AstNodeImpl;

public interface WtLinkOptions
		extends
			WtContentNode
{
	public static final WtLinkOptions EMPTY = new WtEmptyLinkOptions();

	// =========================================================================

	public static final class WtEmptyLinkOptions
			extends
				WtEmptyContentNode
			implements
				WtLinkOptions
	{
		private static final long serialVersionUID = -1064749733891892633L;

		private WtEmptyLinkOptions()
		{
		}

		@Override
		public int getNodeType()
		{
			return NT_LINK_OPTIONS;
		}

		@Override
		public String getNodeName()
		{
			return WtLinkOptions.class.getSimpleName();
		}

		@Override
		public boolean equals(Object other)
		{
			if (this == other)
				return true;
			if (other instanceof WtLinkOptionsImpl)
				return AstNodeImpl.equalsNoTypeCheck(this, (WtLinkOptionsImpl) other);
			return super.equals(other);
		}

		protected Object readResolve() throws ObjectStreamException
		{
			return WtLinkOptions.EMPTY;
		}
	}

	// =========================================================================

	public static final class WtLinkOptionsImpl
			extends
				WtContentNodeImpl
			implements
				WtLinkOptions
	{
		private static final long serialVersionUID = 1L;

		// =====================================================================

		protected WtLinkOptionsImpl()
		{
		}

		protected WtLinkOptionsImpl(WtNodeList content)
		{
			super(content);
		}

		@Override
		public int getNodeType()
		{
			return NT_LINK_OPTIONS;
		}

		@Override
		public String getNodeName()
		{
			return WtLinkOptions.class.getSimpleName();
		}

		@Override
		public boolean equals(Object other)
		{
			if (this == other)
				return true;
			if (other instanceof WtEmptyLinkOptions)
				return AstNodeImpl.equalsNoTypeCheck(this, (WtEmptyLinkOptions) other);
			return super.equals(other);
		}
	}
}
