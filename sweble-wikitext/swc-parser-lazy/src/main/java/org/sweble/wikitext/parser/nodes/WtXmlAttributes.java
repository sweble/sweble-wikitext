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

public interface WtXmlAttributes
		extends
			WtContentNode
{
	public static final WtXmlAttributes EMPTY = new WtEmptyXmlAttributes();

	// =========================================================================

	public static final class WtEmptyXmlAttributes
			extends
				WtEmptyContentNode
			implements
				WtXmlAttributes
	{
		private static final long serialVersionUID = -1064749733891892633L;

		private WtEmptyXmlAttributes()
		{
		}

		@Override
		public int getNodeType()
		{
			return NT_XML_ATTRIBUTES;
		}

		@Override
		public String getNodeName()
		{
			return WtXmlAttributes.class.getSimpleName();
		}

		@Override
		public boolean equals(Object other)
		{
			if (this == other)
				return true;
			if (other instanceof WtXmlAttributesImpl)
				return AstNodeImpl.equalsNoTypeCheck(this, (WtXmlAttributesImpl) other);
			return super.equals(other);
		}

		protected Object readResolve() throws ObjectStreamException
		{
			return WtXmlAttributes.EMPTY;
		}
	}

	// =========================================================================

	public static final class WtXmlAttributesImpl
			extends
				WtContentNodeImpl
			implements
				WtXmlAttributes
	{
		private static final long serialVersionUID = 1L;

		// =====================================================================

		protected WtXmlAttributesImpl()
		{
		}

		protected WtXmlAttributesImpl(WtNodeList content)
		{
			super(content);
		}

		@Override
		public int getNodeType()
		{
			return NT_XML_ATTRIBUTES;
		}

		@Override
		public String getNodeName()
		{
			return WtXmlAttributes.class.getSimpleName();
		}

		@Override
		public boolean equals(Object other)
		{
			if (this == other)
				return true;
			if (other instanceof WtEmptyXmlAttributes)
				return AstNodeImpl.equalsNoTypeCheck(this, (WtEmptyXmlAttributes) other);
			return super.equals(other);
		}
	}
}
