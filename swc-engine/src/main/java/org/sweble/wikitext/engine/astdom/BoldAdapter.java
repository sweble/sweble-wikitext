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
package org.sweble.wikitext.engine.astdom;

import org.sweble.wikitext.engine.dom.DomBold;
import org.sweble.wikitext.engine.dom.DomNodeType;
import org.sweble.wikitext.lazy.parser.Bold;

import de.fau.cs.osr.ptk.common.ast.NodeList;

public class BoldAdapter
        extends
            DomFullElement
        implements
            DomBold
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public BoldAdapter(Bold astNode)
	{
		super(astNode);
	}
	
	// =========================================================================
	
	@Override
	protected Bold getAstNode()
	{
		return (Bold) super.getAstNode();
	}
	
	@Override
	public String getNodeName()
	{
		return "b";
	}
	
	@Override
	public DomNodeType getNodeType()
	{
		return DomNodeType.ELEMENT;
	}
	
	@Override
	protected NodeList getAttributeContainer()
	{
		return null;
	}
	
	@Override
	protected NodeList getChildContainer()
	{
		return getAstNode().getContent();
	}
}
