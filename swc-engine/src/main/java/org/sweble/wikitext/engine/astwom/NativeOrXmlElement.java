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
package org.sweble.wikitext.engine.astwom;

import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.parser.AstNodeTypes;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtXmlElement;

public abstract class NativeOrXmlElement
		extends
			FullElement
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public NativeOrXmlElement(WtNode astNode)
	{
		super(astNode);
		
		if (astNode == null)
			throw new NullPointerException();
	}
	
	public NativeOrXmlElement(String tagName, WtXmlElement astNode)
	{
		super(astNode);
		
		if (astNode == null)
			throw new NullPointerException();
		
		if (!astNode.getName().equalsIgnoreCase(tagName))
			throw new IllegalArgumentException("Given WtXmlElement node is not a `" + tagName + "' element!");
	}
	
	// =========================================================================
	
	@Override
	public final WomNodeType getNodeType()
	{
		return WomNodeType.ELEMENT;
	}
	
	// =========================================================================
	
	protected abstract WtXmlElement convertToXmlElement();
	
	// =========================================================================
	
	@Override
	protected final WtNodeList getAstAttribContainer()
	{
		if (isXml())
			return xml().getXmlAttributes();
		
		return null;
	}
	
	@Override
	protected final WtNodeList addAstAttribSupport() throws UnsupportedOperationException
	{
		convertToXml();
		return xml().getXmlAttributes();
	}
	
	// =========================================================================
	
	@Override
	public WtNodeList getAstChildContainer()
	{
		if (isXml())
			return xml().getBody();
		
		return null;
	}
	
	@Override
	protected final WtNodeList addAstChildrenSupport() throws UnsupportedOperationException
	{
		convertToXml();
		return xml().getBody();
	}
	
	@Override
	protected void appendToAst(WtNodeList container, WtNode child)
	{
		if (isXml())
		{
			if (container.isEmpty())
			{
				WtXmlElement e = xml();
				e.setEmpty(false);
				Toolbox.addRtData(e);
			}
		}
		
		super.appendToAst(container, child);
	}
	
	@Override
	protected void removeFromAst(WtNodeList container, WtNode removeNode)
	{
		super.removeFromAst(container, removeNode);
		
		if (isXml())
		{
			if (container.isEmpty())
			{
				WtXmlElement e = xml();
				e.setEmpty(true);
				Toolbox.addRtData(e);
			}
		}
	}
	
	// =========================================================================
	
	protected final boolean isXml()
	{
		return getAstNode().getNodeType() == WtNode.NT_XML_ELEMENT;
	}
	
	protected final WtXmlElement xml()
	{
		return (WtXmlElement) getAstNode();
	}
	
	protected final void convertToXml()
	{
		if (!isXml())
		{
			WtXmlElement converted = convertToXmlElement();
			
			WomBackbone parent = getParent();
			if (parent != null)
			{
				WtNodeList astChildContainer = null;
				if (parent instanceof FullElement)
					astChildContainer = ((FullElement) parent).getAstChildContainer();
				
				Toolbox.replaceAstNode(astChildContainer, getAstNode(), converted);
			}
			
			setAstNode(converted);
		}
	}
}
