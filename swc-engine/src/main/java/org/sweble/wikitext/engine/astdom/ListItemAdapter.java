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

import java.util.Collection;

import org.sweble.wikitext.engine.wom.WomListItem;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomNodeType;

import de.fau.cs.osr.ptk.common.ast.ContentNode;

public class ListItemAdapter
        extends
            DomBackbone
        implements
            WomListItem
{
	private static final long serialVersionUID = 1L;
	
	private final DomAstChildren children = new DomAstChildren();
	
	// =========================================================================
	
	public ListItemAdapter(ContentNode astNode)
	{
		super(astNode);
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "li";
	}
	
	@Override
	public WomNodeType getNodeType()
	{
		return WomNodeType.ELEMENT;
	}
	
	@Override
	protected ContentNode getAstNode()
	{
		return (ContentNode) super.getAstNode();
	}
	
	// =========================================================================
	
	@Override
	public boolean hasChildNodes()
	{
		return children.hasChildNodes();
	}
	
	@Override
	public Collection<WomNode> childNodes()
	{
		return children.childNodes();
	}
	
	@Override
	public WomNode getFirstChild()
	{
		return children.getFirstChild();
	}
	
	@Override
	public WomNode getLastChild()
	{
		return children.getLastChild();
	}
	
	@Override
	public void appendChild(WomNode child)
	{
		children.appendChild(child, this, getAstNode().getContent());
	}
	
	@Override
	public void insertBefore(WomNode before, WomNode child) throws IllegalArgumentException
	{
		children.insertBefore(before, child, this, getAstNode().getContent());
	}
	
	@Override
	public void removeChild(WomNode child)
	{
		children.removeChild(child, this, getAstNode().getContent());
	}
	
	@Override
	public void replaceChild(WomNode search, WomNode replace)
	{
		children.replaceChild(search, replace, this, getAstNode().getContent());
	}
}
