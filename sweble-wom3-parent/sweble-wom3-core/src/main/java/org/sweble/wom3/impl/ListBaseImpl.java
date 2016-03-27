/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.sweble.wom3.Wom3List;
import org.sweble.wom3.Wom3ListItem;
import org.sweble.wom3.Wom3Node;

public abstract class ListBaseImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3List
{
	private static final long serialVersionUID = 1L;

	private final ArrayList<Wom3ListItem> items =
			new ArrayList<Wom3ListItem>();

	// =========================================================================

	public ListBaseImpl(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	@Override
	public boolean isCompact()
	{
		return getBoolAttr("compact");
	}

	@Override
	public boolean setCompact(boolean compact)
	{
		return setBoolAttr(CommonAttributeDescriptors.ATTR_DESC_COMPACT, "compact", compact);
	}

	// =========================================================================

	@Override
	public int getItemNum()
	{
		return items.size();
	}

	@Override
	public Collection<Wom3ListItem> getItems()
	{
		return Collections.unmodifiableCollection(items);
	}

	@Override
	public Wom3ListItem getItem(int index) throws IndexOutOfBoundsException
	{
		return items.get(index);
	}

	@Override
	public Wom3ListItem replaceItem(int index, Wom3ListItem item) throws IndexOutOfBoundsException
	{
		Wom3ListItem old = getItem(index);
		replaceChild(item, old);
		return old;
	}

	@Override
	public Wom3ListItem removeItem(int index) throws IndexOutOfBoundsException
	{
		Wom3ListItem old = getItem(index);
		removeChild(old);
		return old;
	}

	@Override
	public void appendItem(Wom3ListItem item)
	{
		appendChild(item);
	}

	@Override
	public void insertItem(int beforeIndex, Wom3ListItem item) throws IndexOutOfBoundsException
	{
		int size = getItemNum();
		if (beforeIndex < size)
		{
			insertBefore(item, getItem(beforeIndex));
		}
		else if (beforeIndex == size)
		{
			appendItem(item);
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}

	// =========================================================================

	@Override
	public void childInserted(Backbone prev, Backbone added)
	{
		if (added instanceof Wom3ListItem)
		{
			int i = (prev == null) ? 0 : indexOf(prev) + 1;
			items.add(i, (Wom3ListItem) added);
		}
	}

	@Override
	public void childRemoved(Backbone prev, Backbone removed)
	{
		if (removed instanceof Wom3ListItem)
		{
			int i = (prev == null) ? 0 : indexOf(prev) + 1;
			items.remove(i);
		}
	}

	private int indexOf(Wom3Node node)
	{
		int i = -1;
		Wom3Node child = getFirstChild();
		while (true)
		{
			if (child instanceof Wom3ListItem)
				++i;
			if (child == node)
				return i;
			child = child.getNextSibling();
		}
	}
}
