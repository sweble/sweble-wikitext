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

import de.fau.cs.osr.ptk.common.ast.Uninitialized;

public interface WtTagExtensionBody
		extends
			WtStringNode,
			WtPreproNode
{
	public static final WtTagExtensionNullBody NO_BODY = new WtTagExtensionNullBody();
	
	// =========================================================================
	
	public static final class WtTagExtensionNullBody
			extends
				WtNullStringNode
			implements
				WtTagExtensionBody
	{
		private static final long serialVersionUID = -1064749733891892633L;
	}
	
	// =========================================================================
	
	public class WtTagExtensionBodyImpl
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
		
		public WtTagExtensionBodyImpl(String content)
		{
			super(content);
		}
	}
}
