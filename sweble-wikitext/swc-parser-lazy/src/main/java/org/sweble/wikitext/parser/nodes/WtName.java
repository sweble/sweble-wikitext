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

public interface WtName
		extends
			WtContentNode
{
	public static final WtNoName NO_NAME = new WtNoName();

	// =========================================================================

	public boolean isResolved();

	public String getAsString();

	// =========================================================================

	public static final class WtNoName
			extends
				WtAbsentContentNode
			implements
				WtName
	{
		private static final long serialVersionUID = -1064749733891892633L;

		private WtNoName()
		{
		}

		@Override
		public int getNodeType()
		{
			return NT_NAME;
		}

		@Override
		public String getNodeName()
		{
			return WtName.class.getSimpleName();
		}

		protected Object readResolve() throws ObjectStreamException
		{
			return WtName.NO_NAME;
		}

		// =====================================================================

		public boolean isResolved()
		{
			return false;
		}

		public String getAsString()
		{
			throw new UnsupportedOperationException(genMsg());
		}
	}

	// =========================================================================

	public static class WtNameImpl
			extends
				WtContentNodeImpl
			implements
				WtName
	{
		private static final long serialVersionUID = 1L;

		// =====================================================================

		protected WtNameImpl()
		{
		}

		protected WtNameImpl(WtNodeList content)
		{
			super(content);
		}

		@Override
		public int getNodeType()
		{
			return NT_NAME;
		}

		@Override
		public String getNodeName()
		{
			return WtName.class.getSimpleName();
		}

		// =====================================================================

		public boolean isResolved()
		{
			return (size() == 1) && get(0).isNodeType(NT_TEXT);
		}

		public String getAsString()
		{
			if (!isResolved())
				throw new IllegalStateException("Cannot return unresolved name as string.");
			return ((WtText) get(0)).getContent();
		}
	}
}
