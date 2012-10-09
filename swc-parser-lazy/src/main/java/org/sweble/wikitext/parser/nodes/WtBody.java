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

public interface WtBody
		extends
			WtContentNode
{
	public static final WtNoBody NO_BODY = new WtNoBody();
	
	public static final WtEmptyBody EMPTY = new WtEmptyBody();
	
	// =========================================================================
	
	public static final class WtNoBody
			extends
				WtAbsentContentNode
			implements
				WtBody
	{
		private static final long serialVersionUID = -1064749733891892633L;
		
		@Override
		public int getNodeType()
		{
			return NT_BODY;
		}
		
		@Override
		public String getNodeName()
		{
			return WtBody.class.getSimpleName();
		}
	}
	
	// =========================================================================
	
	public static final class WtEmptyBody
			extends
				WtEmptyContentNode
			implements
				WtBody
	{
		private static final long serialVersionUID = -1064749733891892633L;
		
		@Override
		public int getNodeType()
		{
			return NT_BODY;
		}
		
		@Override
		public String getNodeName()
		{
			return WtBody.class.getSimpleName();
		}
	}
	
	// =========================================================================
	
	public class WtBodyImpl
			extends
				WtContentNodeImpl
			implements
				WtBody
	{
		private static final long serialVersionUID = 1L;
		
		// =====================================================================
		
		protected WtBodyImpl()
		{
		}
		
		protected WtBodyImpl(WtNodeList content)
		{
			super(content);
		}
		
		@Override
		public int getNodeType()
		{
			return NT_BODY;
		}
		
		@Override
		public String getNodeName()
		{
			return WtBody.class.getSimpleName();
		}
	}
}
