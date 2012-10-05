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
import org.sweble.wikitext.engine.astwom.ChildManagerBase;
import org.sweble.wikitext.engine.astwom.FullElement;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtXmlElement;

public abstract class XmlElementWithChildren
		extends
			FullElement
{
	private static final long serialVersionUID = 1L;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private ChildManagerBase childManager = ChildManagerBase.emptyManager();
	
	// =========================================================================
	
	public XmlElementWithChildren(String tagName)
	{
		super(Toolbox.addRtData(new WtXmlElement(
				tagName,
				true,
				new WtNodeList(),
				new WtNodeList())));
	}
	
	public XmlElementWithChildren(
			FullElementContentType contentType,
			String tagName,
			AstToWomNodeFactory factory,
			WtXmlElement astNode)
	{
		super(astNode);
		
		if (astNode == null)
			throw new NullPointerException();
		
		if (!astNode.getName().equalsIgnoreCase(tagName))
			throw new IllegalArgumentException("Given WtXmlElement node is not a `" + tagName + "' element!");
		
		addAttributes(astNode.getXmlAttributes());
		addContent(contentType, factory, astNode.getBody());
	}
	
	// =========================================================================
	
	@Override
	public WomNodeType getNodeType()
	{
		return WomNodeType.ELEMENT;
	}
	
	@Override
	public WtXmlElement getAstNode()
	{
		return (WtXmlElement) super.getAstNode();
	}
	
	// =========================================================================
	
	@Override
	protected void appendToAst(WtNodeList container, WtNode child)
	{
		if (container.isEmpty())
		{
			WtXmlElement e = getAstNode();
			e.setEmpty(false);
			Toolbox.addRtData(e);
		}
		
		super.appendToAst(container, child);
	}
	
	@Override
	protected void removeFromAst(WtNodeList container, WtNode removeNode)
	{
		super.removeFromAst(container, removeNode);
		
		if (container.isEmpty())
		{
			WtXmlElement e = getAstNode();
			e.setEmpty(true);
			Toolbox.addRtData(e);
		}
	}
}
