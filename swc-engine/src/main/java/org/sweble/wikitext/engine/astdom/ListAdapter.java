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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.sweble.wikitext.engine.dom.DomList;
import org.sweble.wikitext.engine.dom.DomListItem;
import org.sweble.wikitext.engine.dom.DomNode;
import org.sweble.wikitext.engine.dom.DomNodeType;

import de.fau.cs.osr.ptk.common.ast.ContentNode;

public abstract class ListAdapter<T>
        extends
            DomBackbone
        implements
            DomList
{
	private static final long serialVersionUID = 1L;
	
	private final Class<T> itemClass;
	
	private final DomAstChildren children = new DomAstChildren();
	
	private final ArrayList<DomListItem> items = new ArrayList<DomListItem>();
	
	// =========================================================================
	
	public ListAdapter(Class<T> itemClass, ContentNode astNode)
	{
		super(astNode);
		this.itemClass = itemClass;
	}
	
	// =========================================================================
	
	@Override
	public abstract String getNodeName();
	
	@Override
	public final DomNodeType getNodeType()
	{
		return DomNodeType.ELEMENT;
	}
	
	@Override
	protected final ContentNode getAstNode()
	{
		return (ContentNode) super.getAstNode();
	}
	
	// =========================================================================
	
	@Override
	public int getItemNum()
	{
		return items.size();
	}
	
	@Override
	public Collection<DomListItem> getItems()
	{
		return Collections.unmodifiableList(items);
	}
	
	@Override
	public DomListItem getItem(int index)
	{
		return items.get(index);
	}
	
	@Override
	public DomListItem setItem(int index, DomListItem item)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public DomListItem removeItem(int index)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void appendItem(DomListItem item)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void insertItem(int beforeIndex, DomListItem item)
	{
		throw new UnsupportedOperationException();
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
		if (!itemClass.isInstance(child))
			throw new IllegalArgumentException(
			        "Expected argument `child' be of type " + itemClass.getName());
		
		children.appendChild(child, this, getAstNode().getContent());
		
		// appendChild should have done all checks for us
		items.add((DomListItem) child);
	}
	
	@Override
	public void insertBefore(DomNode before, DomNode child) throws IllegalArgumentException
	{
		if (before == null || child == null)
			throw new IllegalArgumentException("Argument `before' and/or `child' is null.");
		
		if (!itemClass.isInstance(child))
			throw new IllegalArgumentException(
			        "Expected argument `child' be of type " + itemClass.getName());
		
		if (before.getParent() != this)
			throw new IllegalArgumentException("Given node `before' is not a child of this node.");
		
		int beforeIndex = 0;
		for (DomNode cur = getFirstChild(); cur != before; cur = cur.getNextSibling())
			++beforeIndex;
		
		children.insertBefore(before, child, this, getAstNode().getContent());
		
		items.add(beforeIndex, (DomListItem) child);
	}
	
	@Override
	public void removeChild(DomNode child)
	{
		children.removeChild(child, this, getAstNode().getContent());
		
		// removeChild should have done all checks for us
		items.remove(child);
	}
	
	@Override
	public void replaceChild(DomNode search, DomNode replace)
	{
		if (search == null || replace == null)
			throw new IllegalArgumentException("Argument `before' and/or `child' is null.");
		
		if (!itemClass.isInstance(replace))
			throw new IllegalArgumentException(
			        "Expected argument `replace' be of type " + itemClass.getName());
		
		if (search.getParent() != this)
			throw new IllegalArgumentException("Given node `before' is not a child of this node.");
		
		int index = 0;
		for (DomNode cur = getFirstChild(); cur != search; cur = cur.getNextSibling())
			++index;
		
		children.replaceChild(search, replace, this, getAstNode().getContent());
		
		items.set(index, (DomListItem) replace);
	}
}
