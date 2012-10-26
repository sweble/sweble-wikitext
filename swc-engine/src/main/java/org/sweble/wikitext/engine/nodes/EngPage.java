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
package org.sweble.wikitext.engine.nodes;

import org.sweble.wikitext.parser.nodes.WtContentNode.WtContentNodeImpl;
import org.sweble.wikitext.parser.nodes.WtNodeList;

public class EngPage
		extends
			WtContentNodeImpl
		implements
			EngNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	protected EngPage()
	{
	}
	
	/**
	 * Swaps content from received WtNodeList object into this object and
	 * therefore EMPTIES the received list!
	 */
	protected EngPage(WtNodeList content)
	{
		exchange(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_PAGE;
	}
}
