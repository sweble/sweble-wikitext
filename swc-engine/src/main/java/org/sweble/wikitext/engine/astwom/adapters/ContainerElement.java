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
import org.sweble.wikitext.engine.astwom.ChildManagerBase;
import org.sweble.wikitext.engine.astwom.FullElement;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.parser.nodes.WtNodeList;

public abstract class ContainerElement
		extends
			FullElement
{
	private static final long serialVersionUID = 1L;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private ChildManagerBase childManager = ChildManagerBase.emptyManager();
	
	// =========================================================================
	
	public ContainerElement()
	{
		super(new WtNodeList());
	}
	
	public ContainerElement(
			FullElementContentType contentType,
			AstToWomNodeFactory factory,
			WtNodeList content)
	{
		super(content);
		
		if (content == null)
			throw new NullPointerException();
		
		addContent(contentType, factory, content);
	}
	
	// =========================================================================
	
	@Override
	public WtNodeList getAstNode()
	{
		return (WtNodeList) super.getAstNode();
	}
	
	@Override
	public WomNodeType getNodeType()
	{
		return WomNodeType.ELEMENT;
	}
	
	// =========================================================================
	
	@Override
	public WtNodeList getAstChildContainer()
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
