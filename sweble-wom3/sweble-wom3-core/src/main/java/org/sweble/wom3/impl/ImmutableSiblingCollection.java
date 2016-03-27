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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.sweble.wom3.Wom3Node;

public class ImmutableSiblingCollection<T extends Wom3Node>
		implements
			Collection<T>
{
	private final int size;

	private final T first;

	// =========================================================================

	public ImmutableSiblingCollection(T first)
	{
		this.first = first;
		size = count(first);
	}

	@Override
	public int size()
	{
		return size;
	}

	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}

	@Override
	public boolean contains(Object o)
	{
		for (T i = first; i != null; i = advance(i))
		{
			if (o == null ? i == null : o.equals(i))
				return true;
		}
		return false;
	}

	@Override
	public Iterator<T> iterator()
	{
		return new SiblingIterator<T>(first);
	}

	@Override
	public Object[] toArray()
	{
		Object[] array = new Object[size];
		int j = 0;
		for (T i = first; i != null; i = advance(i))
			array[j++] = i;
		return array;
	}

	@Override
	public <S> S[] toArray(S[] a)
	{
		if (a.length < size)
			a = makeArray(a);

		int j = 0;
		Object[] result = a;
		for (T i = first; i != null; i = advance(i))
			result[j++] = i;

		if (a.length > size)
			a[size] = null;

		return a;
	}

	@Override
	public boolean add(T e)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		for (Object o : c)
		{
			if (!contains(o))
				return false;
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> c)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear()
	{
		throw new UnsupportedOperationException();
	}

	// =========================================================================

	private final static class SiblingIterator<T extends Wom3Node>
			implements
				Iterator<T>
	{
		T i;

		public SiblingIterator(T first)
		{
			this.i = first;
		}

		@Override
		public boolean hasNext()
		{
			return i != null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T next()
		{
			if (!hasNext())
				throw new NoSuchElementException();

			T n = i;
			i = (T) i.getNextSibling();
			return n;
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}

	// =========================================================================

	private int count(T first)
	{
		int count = 0;
		for (T i = first; i != null; i = advance(i))
			++count;
		return count;
	}

	@SuppressWarnings("unchecked")
	private T advance(T i)
	{
		return (T) i.getNextSibling();
	}

	@SuppressWarnings("unchecked")
	private <S> S[] makeArray(S[] a)
	{
		return (S[]) Array.newInstance(a.getClass().getComponentType(), size);
	}
}
