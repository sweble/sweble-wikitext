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

import static org.sweble.wikitext.engine.astwom.adapters.FullElementContentType.MIXED_ANY;

import org.sweble.wikitext.engine.astwom.AstToWomNodeFactory;
import org.sweble.wikitext.engine.wom.WomElementBody;
import org.sweble.wikitext.parser.nodes.WtList;

public class ElementBodyAdapter
		extends
			ContainerElement
		implements
			WomElementBody
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public ElementBodyAdapter()
	{
	}
	
	public ElementBodyAdapter(AstToWomNodeFactory factory, WtList content)
	{
		super(MIXED_ANY, factory, content);
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "elembody";
	}
}
