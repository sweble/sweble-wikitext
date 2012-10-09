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

import de.fau.cs.osr.ptk.common.ast.AstStringNode;

public interface WtStringNode
		extends
			WtNode,
			AstStringNode<WtNode>
{
	public abstract class WtNullStringNode
			extends
				WtEmptyImmutableNode
			implements
				WtStringNode
	{
		private static final long serialVersionUID = 2465445739660029292L;
		
		@Override
		public String getContent()
		{
			return "";
		}
		
		@Override
		public String setContent(String content)
		{
			throw new UnsupportedOperationException(genMsg());
		}
		
		@Override
		public abstract int getNodeType();
		
		@Override
		public abstract String getNodeName();
		
		@Override
		public boolean indicatesAbsence()
		{
			return true;
		}
	}
}
