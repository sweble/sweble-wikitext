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
package org.sweble.wikitext.engine.astwom.adapters;

import static org.sweble.wikitext.engine.astwom.adapters.FullElementContentType.BLOCK_ELEMENTS;

import org.sweble.wikitext.engine.astwom.AstToWomNodeFactory;
import org.sweble.wikitext.engine.wom.WomBody;
import org.sweble.wikitext.parser.nodes.WtNodeList;

public class BodyAdapter
		extends
			ContainerElement
		implements
			WomBody
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public BodyAdapter()
	{
	}
	
	public BodyAdapter(AstToWomNodeFactory factory, WtNodeList content)
	{
		super(BLOCK_ELEMENTS, factory, content);
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "body";
	}
}
