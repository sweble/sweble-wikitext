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

import de.fau.cs.osr.ptk.common.ast.Uninitialized;

public interface WtTagExtensionBody
		extends
			WtStringNode,
			WtPreproNode
{
	public static final WtNoTagExtensionBody NO_BODY = new WtNoTagExtensionBody();

	// =========================================================================

	public static final class WtNoTagExtensionBody
			extends
				WtNoStringNode
			implements
				WtTagExtensionBody
	{
		private static final long serialVersionUID = -1064749733891892633L;

		private WtNoTagExtensionBody()
		{
		}

		@Override
		public int getNodeType()
		{
			return NT_TAG_EXTENSION_BODY;
		}

		@Override
		public String getNodeName()
		{
			return WtTagExtensionBody.class.getSimpleName();
		}

		protected Object readResolve() throws ObjectStreamException
		{
			return WtTagExtensionBody.NO_BODY;
		}
	}

	// =========================================================================

	public static final class WtTagExtensionBodyImpl
			extends
				WtStringNodeImpl
			implements
				WtTagExtensionBody
	{
		private static final long serialVersionUID = -6588373105033239206L;

		// =====================================================================

		/**
		 * Only for use by de-serialization code.
		 */
		protected WtTagExtensionBodyImpl()
		{
			super(Uninitialized.X);
		}

		protected WtTagExtensionBodyImpl(String content)
		{
			super(content);
		}

		@Override
		public int getNodeType()
		{
			return NT_TAG_EXTENSION_BODY;
		}

		@Override
		public String getNodeName()
		{
			return WtTagExtensionBody.class.getSimpleName();
		}
	}
}
