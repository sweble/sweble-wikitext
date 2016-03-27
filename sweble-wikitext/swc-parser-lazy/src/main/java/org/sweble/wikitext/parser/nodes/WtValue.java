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

public interface WtValue
		extends
			WtContentNode
{
	public static final WtNoValue NO_VALUE = new WtNoValue();

	// =========================================================================

	public static final class WtNoValue
			extends
				WtAbsentContentNode
			implements
				WtValue
	{
		private static final long serialVersionUID = -1064749733891892633L;

		private WtNoValue()
		{
		}

		@Override
		public int getNodeType()
		{
			return NT_VALUE;
		}

		@Override
		public String getNodeName()
		{
			return WtValue.class.getSimpleName();
		}

		protected Object readResolve() throws ObjectStreamException
		{
			return WtValue.NO_VALUE;
		}
	}

	// =========================================================================

	public static final class WtValueImpl
			extends
				WtContentNodeImpl
			implements
				WtValue
	{
		private static final long serialVersionUID = 1L;

		// =====================================================================

		protected WtValueImpl()
		{
		}

		protected WtValueImpl(WtNodeList content)
		{
			super(content);
		}

		@Override
		public int getNodeType()
		{
			return NT_VALUE;
		}

		@Override
		public String getNodeName()
		{
			return WtValue.class.getSimpleName();
		}
	}
}
