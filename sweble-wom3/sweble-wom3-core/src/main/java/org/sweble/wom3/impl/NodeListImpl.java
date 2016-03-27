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

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3NodeList;

public class NodeListImpl
		implements
			Wom3NodeList
{
	private final Backbone parent;

	private int last = -1;

	private int length = -1;

	private Wom3Node lastNode = null;

	private int childrenChanges;

	// =========================================================================

	public NodeListImpl(Backbone parent)
	{
		this.parent = parent;
		this.childrenChanges = getChildrenChanges();
	}

	// =========================================================================

	@Override
	public Wom3Node item(int index)
	{
		testInvalidate();

		int cnt;
		Wom3Node n;
		if (last == -1)
		{
			cnt = 0;
			n = parent.getFirstChild();
			while (n != null && cnt++ < index)
				n = n.getNextSibling();
		}
		else
		{
			cnt = index - last;
			n = lastNode;
			if (cnt > 0)
			{
				while (n != null && cnt-- > 0)
					n = n.getNextSibling();
			}
			else
			{
				while (n != null && cnt++ < 0)
					n = n.getPreviousSibling();
			}
		}
		last = index;
		lastNode = n;
		return n;
	}

	@Override
	public int getLength()
	{
		testInvalidate();
		if (length == -1)
		{
			int len = 0;
			for (Wom3Node n = parent.getFirstChild(); n != null; n = n.getNextSibling())
				++len;
			length = len;
		}
		return length;
	}

	// =========================================================================

	@Override
	public Iterator<Wom3Node> iterator()
	{
		return new Iterator<Wom3Node>()
		{
			private int index = 0;

			private int childrenChanges = getChildrenChanges();

			@Override
			public boolean hasNext()
			{
				if (hasModification())
					throw new ConcurrentModificationException();
				return item(index) != null;
			}

			@Override
			public Wom3Node next()
			{
				if (hasModification())
					throw new ConcurrentModificationException();
				Wom3Node item = item(index);
				if (item == null)
					throw new NoSuchElementException();
				++index;
				return item;
			}

			@Override
			public void remove()
			{
				if (hasModification())
					throw new ConcurrentModificationException();
				throw new UnsupportedOperationException();
			}

			private boolean hasModification()
			{
				return childrenChanges != getChildrenChanges();
			}
		};
	}

	// =========================================================================

	private void testInvalidate()
	{
		if (hasModification())
		{
			childrenChanges = getChildrenChanges();
			length = -1;
			last = -1;
		}
	}

	private boolean hasModification()
	{
		return childrenChanges != getChildrenChanges();
	}

	private int getChildrenChanges()
	{
		return parent.childrenChanges;
	}
}
