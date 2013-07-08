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

import org.sweble.wikitext.parser.nodes.WtContentNode.WtContentNodeImpl;

public class WtPageName
		extends
			WtContentNodeImpl
		implements
			WtLinkTarget
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtPageName()
	{
	}
	
	protected WtPageName(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_PAGE_NAME;
	}
	
	@Override
	public LinkTargetType getTargetType()
	{
		return LinkTargetType.PAGE;
	}
	
	// =========================================================================
	
	public boolean isResolved()
	{
		return (size() == 1) && get(0).isNodeType(NT_TEXT);
	}
	
	public String getAsString()
	{
		if (!isResolved())
			throw new IllegalStateException("Cannot return unresolved link target as string.");
		return ((WtText) get(0)).getContent();
	}
}
