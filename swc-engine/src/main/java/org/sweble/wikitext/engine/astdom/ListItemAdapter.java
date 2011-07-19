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

import org.sweble.wikitext.engine.dom.DomListItem;
import org.sweble.wikitext.engine.dom.DomNode;
import org.sweble.wikitext.engine.dom.DomNodeType;

import de.fau.cs.osr.ptk.common.ast.ContentNode;

public class ListItemAdapter
        extends
            DomBackbone
        implements
            DomListItem
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
	public DomNodeType getNodeType()
	{
		return DomNodeType.ELEMENT;
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
	public Collection<DomNode> childNodes()
	{
		return children.childNodes();
	}
	
	@Override
	public DomNode getFirstChild()
	{
		return children.getFirstChild();
	}
	
	@Override
	public DomNode getLastChild()
	{
		return children.getLastChild();
	}
	
	@Override
	public void appendChild(DomNode child)
	{
		children.appendChild(child, this, getAstNode().getContent());
	}
	
	@Override
	public void insertBefore(DomNode before, DomNode child) throws IllegalArgumentException
	{
		children.insertBefore(before, child, this, getAstNode().getContent());
	}
	
	@Override
	public void removeChild(DomNode child)
	{
		children.removeChild(child, this, getAstNode().getContent());
	}
	
	@Override
	public void replaceChild(DomNode search, DomNode replace)
	{
		children.replaceChild(search, replace, this, getAstNode().getContent());
	}
}
