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

import java.io.Serializable;
import java.util.AbstractSequentialList;
import java.util.Deque;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class SiblingRangeCollection<U extends BackboneWithChildren, T extends Backbone>
		extends
			AbstractSequentialList<T>
		implements
			Serializable,
			Deque<T>
{
	private static final long serialVersionUID = 1L;

	private T first;

	private T last;

	private final U container;

	private final SiblingCollectionBounds bounds;

	// =========================================================================

	public SiblingRangeCollection(U container, SiblingCollectionBounds bounds)
	{
		this.container = container;
		this.bounds = bounds;
	}

	@Override
	public ListIterator<T> listIterator(int index)
	{
		return new ListIter(index);
	}

	@Override
	public int size()
	{
		int count = 0;
		for (T i = first; i != null; i = advance(i))
			++count;
		return count;
	}

	// =========================================================================

	public boolean add(T e)
	{
		addLast(e);
		return true;
	}

	// =========================================================================

	@Override
	public T peek()
	{
		return peekFirst();
	}

	@Override
	public T peekFirst()
	{
		return first;
	}

	@Override
	public T peekLast()
	{
		return last;
	}

	@Override
	public T getFirst()
	{
		if (first == null)
			throw new NoSuchElementException();
		return first;
	}

	@Override
	public T getLast()
	{
		if (last == null)
			throw new NoSuchElementException();
		return last;
	}

	@Override
	public T element()
	{
		return getFirst();
	}

	@Override
	public void addFirst(T e)
	{
		Backbone n = first;
		if (n == null)
			n = bounds.getSucc();
		Backbone p = bounds.getPred();

		checkBeforeAdd(p, e);

		e.link(container, p, n);
		if (last == null)
			last = e;
		first = e;

		container.childInserted(p, e);
	}

	@Override
	public void addLast(T e)
	{
		Backbone p = last;

		if (p == null)
			p = bounds.getPred();

		checkBeforeAdd(p, e);

		e.link(container, p, bounds.getSucc());
		if (first == null)
			first = e;
		last = e;

		container.childInserted(p, e);
	}

	@Override
	public boolean offer(T e)
	{
		return add(e);
	}

	@Override
	public boolean offerFirst(T e)
	{
		addFirst(e);
		return true;
	}

	@Override
	public boolean offerLast(T e)
	{
		addLast(e);
		return true;
	}

	@Override
	public T removeFirst()
	{
		checkBeforeRemove(first);

		Backbone prev = first.getPreviousSibling();
		T n = advance(first);
		T removed = first;
		removed.unlink();
		first = n;
		if (n == null)
			last = null;

		container.childRemoved(prev, removed);
		return removed;
	}

	@Override
	public T removeLast()
	{
		checkBeforeRemove(last);

		Backbone prev = last.getPreviousSibling();
		T p = retreat(last);
		T removed = last;
		removed.unlink();
		last = p;
		if (p == null)
			first = null;

		container.childRemoved(prev, removed);
		return removed;
	}

	@Override
	public T poll()
	{
		return pollFirst();
	}

	@Override
	public T pollFirst()
	{
		if (first != null)
			return removeFirst();
		return null;
	}

	@Override
	public T pollLast()
	{
		if (last != null)
			return removeLast();
		return null;
	}

	@Override
	public void push(T e)
	{
		addFirst(e);
	}

	@Override
	public T pop()
	{
		return removeFirst();
	}

	@Override
	public T remove()
	{
		return removeFirst();
	}

	@Override
	public boolean removeFirstOccurrence(Object o)
	{
		return remove(o);
	}

	@Override
	public boolean removeLastOccurrence(Object o)
	{
		if (o == null)
			throw new NullPointerException();
		Iterator<T> i = descendingIterator();
		while (i.hasNext())
		{
			if (o.equals(i.next()))
			{
				i.remove();
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterator<T> descendingIterator()
	{
		return new DescIter();
	}

	// =========================================================================

	private void checkBeforeAdd(Backbone p, T e)
	{
		if (e == null)
			throw new IllegalArgumentException("Argument `e' is null.");

		if (e.isLinked())
			throw new IllegalStateException(
					"Given node `e' is still child of another WOM node.");

		container.allowsInsertion(p, e);
		//e.acceptsParent(container);
	}

	private void checkBeforeRemove(T e)
	{
		if (e == null)
			throw new NoSuchElementException();

		container.allowsRemoval(e);
		//e.childAllowsRemoval(container);
	}

	private T retreat(T i)
	{
		@SuppressWarnings("unchecked")
		T p = (T) i.getPreviousSibling();
		if (p == bounds.getPred())
			p = null;
		return p;
	}

	private T advance(T i)
	{
		@SuppressWarnings("unchecked")
		T n = (T) i.getNextSibling();
		if (n == bounds.getSucc())
			n = null;
		return n;
	}

	// =========================================================================

	private final class ListIter
			implements
				ListIterator<T>
	{
		private T lastReturned = null;

		private T next;

		private int nextIndex;

		public ListIter(int index)
		{
			if (index < 0)
				throw new IndexOutOfBoundsException();

			T i = first;
			for (int count = 0;; ++count)
			{
				if (count == index)
					break;
				if (i == null)
					throw new IndexOutOfBoundsException();
				i = advance(i);
			}

			next = i;
			nextIndex = index;
		}

		@Override
		public boolean hasNext()
		{
			return next != null;
		}

		@Override
		public T next()
		{
			if (!hasNext())
				throw new NoSuchElementException();

			lastReturned = next;
			next = advance(next);
			++nextIndex;
			return lastReturned;
		}

		@Override
		public boolean hasPrevious()
		{
			return nextIndex > 0;
		}

		@Override
		public T previous()
		{
			if (!hasPrevious())
				throw new NoSuchElementException();

			lastReturned = last;
			if (next != null)
				lastReturned = retreat(next);
			next = lastReturned;
			--nextIndex;
			return lastReturned;
		}

		@Override
		public int nextIndex()
		{
			return nextIndex;
		}

		@Override
		public int previousIndex()
		{
			return nextIndex - 1;
		}

		@Override
		public void remove()
		{
			T removed = lastReturned;
			if (removed == null)
				throw new IllegalStateException();

			container.allowsRemoval(removed);
			//removed.childAllowsRemoval(container);
			Backbone realPrev = removed.getPreviousSibling();

			T lastPrev = retreat(removed);
			T lastNext = advance(removed);

			// Fix tree
			removed.unlink();
			if (removed == first)
			{
				first = lastNext;
				if (first == null)
					last = null;
			}
			else if (removed == last)
			{
				last = lastPrev;
				// If lastPrev were zero and we only had one item left, the 
				// previous if case would have been executed. No need to also 
				// set first to null since last cannot become null here.
			}

			// Fix iterator
			if (next == removed)
			{
				next = lastNext;
			}
			else
			{
				nextIndex--;
			}
			lastReturned = null;

			//  Let the parent know
			container.childRemoved(realPrev, removed);
		}

		@Override
		public void set(T e)
		{
			T replaced = lastReturned;
			if (replaced == null)
				throw new IllegalStateException();

			if (e.isLinked())
				throw new IllegalStateException(
						"Given node `e' is still child of another WOM node.");

			container.allowsRemoval(replaced);
			//replaced.childAllowsRemoval(container);

			Backbone lastPrev = replaced.getPreviousSibling();
			Backbone lastNext = replaced.getNextSibling();

			//container.acceptsChild(lastPrev, e);
			//replaced.acceptsParent(container);
			container.allowsReplacement(replaced, e);

			// Fix tree
			replaced.unlink();
			e.link(container, lastPrev, lastNext);
			if (first == replaced)
				first = e;
			if (last == replaced)
				last = e;

			// Fix iterator
			if (next == replaced)
				next = e;
			lastReturned = e;

			container.childRemoved(lastPrev, replaced);
			container.childInserted(lastPrev, e);
		}

		@Override
		public void add(T e)
		{
			lastReturned = null;
			if (next == null)
			{
				addLast(e);
			}
			else
			{
				if (e == null)
					throw new IllegalArgumentException("Argument `e' is null.");

				if (e.isLinked())
					throw new IllegalStateException(
							"Given node `e' is still child of another WOM node.");

				Backbone p = retreat(next);
				boolean becomesFirst = p == null;
				if (becomesFirst)
					p = bounds.getPred();

				container.allowsInsertion(p, e);
				//e.acceptsParent(container);

				if (becomesFirst)
					first = e;
				e.link(container, p, next);

				container.childInserted(p, e);
			}
			nextIndex++;
		}
	}

	// =========================================================================

	private class DescIter
			implements
				Iterator<T>
	{
		private final ListIter i = new ListIter(size());

		public boolean hasNext()
		{
			return i.hasPrevious();
		}

		public T next()
		{
			return i.previous();
		}

		public void remove()
		{
			i.remove();
		}
	}
}
