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

public interface WtLinkTarget
		extends
			WtNode
{
	public static final WtLinkTarget NO_LINK = new WtNoLink();

	// =========================================================================

	public LinkTargetType getTargetType();

	// =========================================================================

	public static enum LinkTargetType
	{
		/** The "link=X" argument was not present */
		DEFAULT,
		PAGE,
		URL,
		/** The "link=" argument was empty */
		NO_LINK
	}

	// =========================================================================

	public static final class WtNoLink
			extends
				WtEmptyImmutableNode
			implements
				WtLinkTarget
	{
		private static final long serialVersionUID = 4433767404703646519L;

		private WtNoLink()
		{
		}

		@Override
		public LinkTargetType getTargetType()
		{
			return LinkTargetType.NO_LINK;
		}

		@Override
		public int getNodeType()
		{
			return NT_UNTYPED;
		}

		@Override
		public String getNodeName()
		{
			return WtLinkTarget.class.getSimpleName();
		}

		@Override
		public boolean indicatesAbsence()
		{
			return true;
		}

		protected Object readResolve() throws ObjectStreamException
		{
			return WtLinkTarget.NO_LINK;
		}
	}
}
