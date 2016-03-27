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

import org.sweble.wom3.Wom3Node;

public class ChildrenSubset<T extends Wom3Node>
		extends
			ArrayList<T>
{
	private static final long serialVersionUID = 1L;

	public ChildrenSubset()
	{
	}

	public ChildrenSubset(int initialCapacity)
	{
		super(initialCapacity);
	}

	public Wom3Node getFirstOrNull()
	{
		if (isEmpty())
			return null;
		return get(0);
	}

	public void insertAfter(
			Backbone prev,
			Class<T> type,
			T newChild)
	{
		if (prev == null)
		{
			// The new was inserted as the very first node of the parent.
			add(0, newChild);
		}
		else
		{
			Backbone p = prev;
			while ((p != null) && !type.isInstance(p))
				p = p.getPreviousSibling();

			if (p == null)
			{
				add(0, newChild);
			}
			else
			{
				// We found the node preceding the new node that must be part
				// of this list due to its type.
				int i = indexOf(p);
				if (i == -1)
					throw new InternalError();
				add(i + 1, newChild);
			}
		}
	}
}
