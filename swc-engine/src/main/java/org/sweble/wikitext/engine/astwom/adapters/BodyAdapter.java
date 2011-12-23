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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.sweble.wikitext.engine.astwom.AstToWomNodeFactory;
import org.sweble.wikitext.engine.astwom.AttributeDescriptor;
import org.sweble.wikitext.engine.astwom.ChildManager;
import org.sweble.wikitext.engine.astwom.FullElement;
import org.sweble.wikitext.engine.wom.WomBody;
import org.sweble.wikitext.engine.wom.WomNodeType;

import de.fau.cs.osr.ptk.common.ast.NodeList;

public class BodyAdapter
		extends
			FullElement
		implements
			WomBody
{
	private static final long serialVersionUID = 1L;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private ChildManager childManager = ChildManager.emptyManager();
	
	// =========================================================================
	
	public BodyAdapter()
	{
		super(new NodeList());
	}
	
	public BodyAdapter(AstToWomNodeFactory womNodeFactory, NodeList content)
	{
		super(content);
		
		if (content == null)
			throw new NullPointerException();
		
		if (!content.isEmpty())
			super.addContent(
					womNodeFactory,
					content,
					getChildManagerForModificationOrFail(),
					false);
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "body";
	}
	
	@Override
	public NodeList getAstNode()
	{
		return (NodeList) super.getAstNode();
	}
	
	@Override
	public WomNodeType getNodeType()
	{
		return WomNodeType.ELEMENT;
	}
	
	// =========================================================================
	
	@Override
	public NodeList getAstChildContainer()
	{
		return getAstNode();
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(String name)
	{
		return null;
	}
}
