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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import org.sweble.wikitext.engine.wom.WomNode;

public class SiblingCollection<T extends WomNode>
        implements
            Collection<T>
{
	private final int size;
	
	private final T first;
	
	// =========================================================================
	
	public SiblingCollection(T first)
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
	
	private final static class SiblingIterator<T extends WomNode>
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
