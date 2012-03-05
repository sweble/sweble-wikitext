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

import org.sweble.wikitext.engine.astwom.AstToWomNodeFactory;
import org.sweble.wikitext.engine.wom.WomStrong;
import org.sweble.wikitext.lazy.parser.XmlElement;

public class StrongAdapter
		extends
			XmlElementWithUniversalAttributes
		implements
			WomStrong
{
	private static final long serialVersionUID = 1L;
	
	private static final String TAG_AND_NODE_NAME = "strong";
	
	// =========================================================================
	
	public StrongAdapter()
	{
		super(TAG_AND_NODE_NAME);
	}
	
	public StrongAdapter(AstToWomNodeFactory womNodeFactory, XmlElement astNode)
	{
		super(TAG_AND_NODE_NAME, womNodeFactory, astNode);
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return TAG_AND_NODE_NAME;
	}
}
