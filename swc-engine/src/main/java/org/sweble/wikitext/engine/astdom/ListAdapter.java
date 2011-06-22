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

import org.sweble.wikitext.engine.wom.WomList;
import org.sweble.wikitext.engine.wom.WomListItem;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomNodeType;

import de.fau.cs.osr.ptk.common.ast.ContentNode;

public abstract class ListAdapter<T>
        extends
            DomBackbone
        implements
            WomList
{
	private static final long serialVersionUID = 1L;
	
	private final Class<T> itemClass;
	
	private final DomAstChildren children = new DomAstChildren();
	
	private final ArrayList<WomListItem> items = new ArrayList<WomListItem>();
	
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
	public final WomNodeType getNodeType()
	{
		return WomNodeType.ELEMENT;
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
	public Collection<WomListItem> getItems()
	{
		return Collections.unmodifiableList(items);
	}
	
	@Override
	public WomListItem getItem(int index)
	{
		return items.get(index);
	}
	
	@Override
	public WomListItem setItem(int index, WomListItem item)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public WomListItem removeItem(int index)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void appendItem(WomListItem item)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void insertItem(int beforeIndex, WomListItem item)
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
		if (!itemClass.isInstance(child))
			throw new IllegalArgumentException(
			        "Expected argument `child' be of type " + itemClass.getName());
		
		children.appendChild(child, this, getAstNode().getContent());
		
		// appendChild should have done all checks for us
		items.add((WomListItem) child);
	}
	
	@Override
	public void insertBefore(WomNode before, WomNode child) throws IllegalArgumentException
	{
		if (before == null || child == null)
			throw new IllegalArgumentException("Argument `before' and/or `child' is null.");
		
		if (!itemClass.isInstance(child))
			throw new IllegalArgumentException(
			        "Expected argument `child' be of type " + itemClass.getName());
		
		if (before.getParent() != this)
			throw new IllegalArgumentException("Given node `before' is not a child of this node.");
		
		int beforeIndex = 0;
		for (WomNode cur = getFirstChild(); cur != before; cur = cur.getNextSibling())
			++beforeIndex;
		
		children.insertBefore(before, child, this, getAstNode().getContent());
		
		items.add(beforeIndex, (WomListItem) child);
	}
	
	@Override
	public void removeChild(WomNode child)
	{
		children.removeChild(child, this, getAstNode().getContent());
		
		// removeChild should have done all checks for us
		items.remove(child);
	}
	
	@Override
	public void replaceChild(WomNode search, WomNode replace)
	{
		if (search == null || replace == null)
			throw new IllegalArgumentException("Argument `before' and/or `child' is null.");
		
		if (!itemClass.isInstance(replace))
			throw new IllegalArgumentException(
			        "Expected argument `replace' be of type " + itemClass.getName());
		
		if (search.getParent() != this)
			throw new IllegalArgumentException("Given node `before' is not a child of this node.");
		
		int index = 0;
		for (WomNode cur = getFirstChild(); cur != search; cur = cur.getNextSibling())
			++index;
		
		children.replaceChild(search, replace, this, getAstNode().getContent());
		
		items.set(index, (WomListItem) replace);
	}
}
