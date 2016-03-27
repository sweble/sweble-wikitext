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

public interface WtLinkTitle
		extends
			WtContentNode
{
	public static final WtNoLinkTitle NO_TITLE = new WtNoLinkTitle();

	// =========================================================================

	public static final class WtNoLinkTitle
			extends
				WtAbsentContentNode
			implements
				WtLinkTitle
	{
		private static final long serialVersionUID = -1064749733891892633L;

		private WtNoLinkTitle()
		{
		}

		@Override
		public int getNodeType()
		{
			return NT_LINK_TITLE;
		}

		@Override
		public String getNodeName()
		{
			return WtLinkTitle.class.getSimpleName();
		}

		protected Object readResolve() throws ObjectStreamException
		{
			return WtLinkTitle.NO_TITLE;
		}
	}

	// =========================================================================

	public static final class WtLinkTitleImpl
			extends
				WtContentNodeImpl
			implements
				WtLinkTitle
	{
		private static final long serialVersionUID = 1L;

		// =====================================================================

		protected WtLinkTitleImpl()
		{
		}

		protected WtLinkTitleImpl(WtNodeList content)
		{
			super(content);
		}

		@Override
		public int getNodeType()
		{
			return NT_LINK_TITLE;
		}

		@Override
		public String getNodeName()
		{
			return WtLinkTitle.class.getSimpleName();
		}
	}
}
