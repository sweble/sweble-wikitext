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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wom3.Wom3Node;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

@RunWith(Parameterized.class)
public class SiblingRangeCollectionTest
{
	@Parameters
	public static Collection<Object[]> data()
	{
		return Arrays.asList(new Object[][] {
				{ false, false },
				{ false, true },
				{ true, false },
				{ true, true },
		});
	}

	// =========================================================================

	private final SiblingRangeCollection<Container, ChildNode> c;

	private final LinkedList<ChildNode> nodes;

	private DocumentImpl doc;

	// =========================================================================

	public SiblingRangeCollectionTest(boolean hasPred, boolean hasSucc)
	{
		DomImplementationImpl domImpl = new DomImplementationImpl();
		this.doc = domImpl.createDocument(Wom3Node.WOM_NS_URI, "article", null);

		BoundaryChild pred = hasPred ? new BoundaryChild(doc) : null;
		BoundaryChild succ = hasSucc ? new BoundaryChild(doc) : null;
		Container c = new Container(doc, pred, succ);
		this.c = c.getC();
		this.nodes = new LinkedList<ChildNode>();
		for (int i = 0; i < 10; ++i)
			nodes.add(gen(i));
	}

	// =========================================================================

	@Test
	public void testEmptyContainerHasSize0() throws Exception
	{
		assertEquals(0, c.size());
	}

	@Test
	public void testCanGetListIteratorOfEmptyContainer() throws Exception
	{
		c.listIterator();
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testListIteratorAtNonZeroIndexForEmptyContainerFails() throws Exception
	{
		c.listIterator(1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testListIteratorWithNegIndexFails() throws Exception
	{
		c.listIterator(-1);
	}

	@Test(expected = NoSuchElementException.class)
	public void testGetFirstFailsForEmptyContainer() throws Exception
	{
		c.getFirst();
	}

	@Test(expected = NoSuchElementException.class)
	public void testGetLastFailsForEmptyContainer() throws Exception
	{
		c.getLast();
	}

	@Test
	public void testPeekWorksWithEmptyContainer() throws Exception
	{
		assertNull(c.peek());
		assertNull(c.peekFirst());
		assertNull(c.peekLast());
	}

	@Test
	public void testPollWorksWithEmptyContainer() throws Exception
	{
		assertNull(c.poll());
		assertNull(c.pollFirst());
		assertNull(c.pollLast());
	}

	@Test
	public void testHasNextAndHasPreviousReturnFalseForEmptyContainer() throws Exception
	{
		assertFalse(c.listIterator().hasNext());
		assertFalse(c.listIterator().hasPrevious());
	}

	@Test
	public void testNextIndexReturnsZeroForEmptyContainer() throws Exception
	{
		assertEquals(0, c.listIterator().nextIndex());
		assertEquals(-1, c.listIterator().previousIndex());
	}

	@Test
	public void testCanAddToEmptyContainer() throws Exception
	{
		ChildNode e = gen(0);
		c.add(e);
		assertFalse(c.isEmpty());
		assertEquals(1, c.size());
		assertEquals(e, c.getFirst());
		assertEquals(e, c.getLast());
	}

	@Test
	public void testCanAddTwoToEmptyContainer() throws Exception
	{
		ChildNode e0 = gen(0);
		c.add(e0);
		ChildNode e1 = gen(1);
		c.add(e1);

		assertFalse(c.isEmpty());
		assertEquals(2, c.size());
		assertEquals(e0, c.getFirst());
		assertEquals(e1, c.getLast());
	}

	@Test
	public void testAddingAndRetrievingItemsByIndex() throws Exception
	{
		for (ChildNode e : nodes)
			c.add(e);
		assertEquals(nodes.size(), c.size());
		for (int i = 0; i < nodes.size(); ++i)
			assertEquals(nodes.get(i), c.get(i));
	}

	@Test
	public void testPeekAndPollOnFilledContainer() throws Exception
	{
		for (ChildNode e : nodes)
			c.add(e);
		assertEquals(nodes.peek(), c.peek());
		assertEquals(nodes.peekFirst(), c.peekFirst());
		assertEquals(nodes.peekLast(), c.peekLast());
		assertEquals(nodes.poll(), c.poll());
		assertEquals(nodes.pollFirst(), c.pollFirst());
		assertEquals(nodes.pollLast(), c.pollLast());
		assertEquals(nodes.size(), c.size());
		Iterator<ChildNode> i = nodes.iterator();
		for (ChildNode e : c)
			assertEquals(i.next(), e);
	}

	@Test
	public void testRemoveWithIteratorInComplexPattern() throws Exception
	{
		for (ChildNode e : nodes)
			c.add(e);
		// Remove all even
		ListIterator<ChildNode> i = c.listIterator();
		while (i.hasNext())
		{
			i.next();
			i.remove();
			if (i.hasNext())
				i.next();
		}
		assertEquals(nodes.size() / 2, c.size());
		// Remove remaining
		while (i.hasPrevious())
		{
			i.previous();
			i.remove();
		}
	}

	@Test
	public void testRemoveWithIteratorInComplexPatternReverse() throws Exception
	{
		for (ChildNode e : nodes)
			c.add(e);
		// Remove all even
		ListIterator<ChildNode> i = c.listIterator(c.size());
		while (i.hasPrevious())
		{
			i.previous();
			i.remove();
			if (i.hasPrevious())
				i.previous();
		}
		assertEquals(nodes.size() / 2, c.size());
		// Remove remaining
		while (i.hasNext())
		{
			i.next();
			i.remove();
		}
	}

	@Test
	public void testRemoveAll() throws Exception
	{
		for (ChildNode e : nodes)
			c.add(e);
		while (!c.isEmpty())
			c.removeFirst();
		assertEquals(0, c.size());
	}

	@Test
	public void testRemoveAllReverse() throws Exception
	{
		for (ChildNode e : nodes)
			c.add(e);
		while (!c.isEmpty())
			c.removeLast();
		assertEquals(0, c.size());
	}

	@Test
	public void testFillContainerFromFront() throws Exception
	{
		for (ChildNode e : nodes)
			c.addFirst(e);
		Iterator<ChildNode> i = c.iterator();
		Iterator<ChildNode> j = nodes.descendingIterator();
		while (j.hasNext())
			assertEquals(j.next(), i.next());
		assertEquals(j.hasNext(), i.hasNext());
	}

	@Test
	public void testRemoveFirstOccurrence() throws Exception
	{
		ChildNode a = gen(42);
		ChildNode b = gen(42);
		c.add(gen(0));
		c.add(a);
		c.add(gen(1));
		c.add(gen(2));
		c.add(b);
		c.add(gen(3));

		assertEquals(42, c.get(1).getFakeId());
		assertTrue(c.removeFirstOccurrence(gen(42)));
		assertFalse(a.isLinked());
		assertTrue(b.isLinked());
		assertEquals(1, c.get(1).getFakeId());
		assertEquals(42, c.get(3).getFakeId());

		assertTrue(c.removeFirstOccurrence(gen(42)));
		assertFalse(b.isLinked());
		for (int i = 0; i < 4; ++i)
			assertEquals(i, c.get(i).getFakeId());

		assertFalse(c.removeFirstOccurrence(gen(42)));
	}

	@Test
	public void testRemoveLastOccurrence() throws Exception
	{
		ChildNode a = gen(42);
		ChildNode b = gen(42);
		c.add(gen(0));
		c.add(a);
		c.add(gen(1));
		c.add(gen(2));
		c.add(b);
		c.add(gen(3));

		assertEquals(42, c.get(4).getFakeId());
		assertTrue(c.removeLastOccurrence(gen(42)));
		assertTrue(a.isLinked());
		assertFalse(b.isLinked());
		assertEquals(3, c.get(4).getFakeId());
		assertEquals(42, c.get(1).getFakeId());

		assertTrue(c.removeLastOccurrence(gen(42)));
		assertFalse(b.isLinked());
		for (int i = 0; i < 4; ++i)
			assertEquals(i, c.get(i).getFakeId());

		assertFalse(c.removeLastOccurrence(gen(42)));
	}

	@Test
	public void testReplacingAllNodesWithHigherIdNodes() throws Exception
	{
		for (ChildNode e : nodes)
			c.add(e);
		ListIterator<ChildNode> i = c.listIterator();
		for (int j = 0; j < nodes.size(); ++j)
		{
			ChildNode e = i.next();
			assertEquals(j, e.getFakeId());
			i.set(gen(e.getFakeId() + nodes.size()));
		}
		for (int j = 0; j < 10; ++j)
			assertEquals(j + nodes.size(), c.get(j).getFakeId());
	}

	@Test
	public void testReplacingAllNodesWithHigherIdNodesReverse() throws Exception
	{
		for (ChildNode e : nodes)
			c.add(e);
		ListIterator<ChildNode> i = c.listIterator(c.size());
		for (int j = 0; j < nodes.size(); ++j)
		{
			ChildNode e = i.previous();
			assertEquals(nodes.size() - j - 1, e.getFakeId());
			i.set(gen(e.getFakeId() + nodes.size()));
		}
		for (int j = 0; j < 10; ++j)
			assertEquals(j + nodes.size(), c.get(j).getFakeId());
	}

	@Test
	public void testInsertNegativeIdsBeforeAndAfterAllNodes() throws Exception
	{
		for (ChildNode e : nodes)
			c.add(e);
		ListIterator<ChildNode> i = c.listIterator();
		int j = -1;
		i.add(gen(j--));
		while (i.hasNext())
		{
			i.next();
			i.add(gen(j--));
		}
		for (int k = 0; k <= nodes.size(); k++)
			assertEquals(-k - 1, c.get(k * 2).getFakeId());
		for (int k = 0; k < nodes.size(); k++)
			assertEquals(k, c.get(k * 2 + 1).getFakeId());
	}

	// =========================================================================

	private ChildNode gen(int id)
	{
		return new ChildNode(doc, id);
	}

	// =========================================================================

	protected static final class ChildNode
			extends
				Backbone
	{
		private static final long serialVersionUID = 1L;

		private final int id;

		// ---------------------------------------------------------------------

		public ChildNode(DocumentImpl owner, int id)
		{
			super(owner);
			this.id = id;
		}

		// ---------------------------------------------------------------------

		public int getFakeId()
		{
			return id;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ChildNode other = (ChildNode) obj;
			if (id != other.id)
				return false;
			return true;
		}

		@Override
		public String getNodeName()
		{
			return ChildNode.class.getSimpleName();
		}

		@Override
		public String getLocalName()
		{
			return getNodeName();
		}

		@Override
		public short getNodeType()
		{
			return Node.ELEMENT_NODE;
		}

		@Override
		public void setTextContent(String textContent) throws DOMException
		{
		}

		@Override
		public Backbone getParentNode()
		{
			return getParentNodeIntern();
		}
	}

	// =========================================================================

	protected static final class BoundaryChild
			extends
				Backbone
	{
		private static final long serialVersionUID = 1L;

		// ---------------------------------------------------------------------

		public BoundaryChild(DocumentImpl owner)
		{
			super(owner);
		}

		// ---------------------------------------------------------------------

		@Override
		public String getNodeName()
		{
			return ChildNode.class.getSimpleName();
		}

		@Override
		public String getLocalName()
		{
			return getNodeName();
		}

		@Override
		public short getNodeType()
		{
			return Node.ELEMENT_NODE;
		}

		@Override
		public void setTextContent(String textContent) throws DOMException
		{
		}

		@Override
		public Backbone getParentNode()
		{
			return getParentNodeIntern();
		}
	}

	// =========================================================================

	protected static final class Container
			extends
				BackboneElement
			implements
				SiblingCollectionBounds
	{
		private static final long serialVersionUID = 1L;

		private final SiblingRangeCollection<Container, ChildNode> c;

		private final BoundaryChild pred;

		private final BoundaryChild succ;

		// ---------------------------------------------------------------------

		public Container(
				DocumentImpl owner,
				BoundaryChild pred,
				BoundaryChild succ)
		{
			super(owner);

			c = new SiblingRangeCollection<Container, ChildNode>(this, new SiblingCollectionBounds()
			{
				@Override
				public Backbone getSucc()
				{
					return Container.this.succ;
				}

				@Override
				public Backbone getPred()
				{
					return Container.this.pred;
				}
			});

			this.pred = pred;
			if (pred != null)
				pred.link(this, null, null);

			this.succ = succ;
			if (succ != null)
				succ.link(this, pred, null);
		}

		// ---------------------------------------------------------------------

		@Override
		protected void allowsInsertion(Backbone prev, Backbone child)
		{
		}

		@Override
		protected void allowsRemoval(Backbone child)
		{
		}

		@Override
		protected void allowsReplacement(Backbone oldChild, Backbone newChild)
		{
		}

		// ---------------------------------------------------------------------

		public SiblingRangeCollection<Container, ChildNode> getC()
		{
			return c;
		}

		@Override
		public String getNodeName()
		{
			return Container.class.getSimpleName();
		}

		@Override
		public String getLocalName()
		{
			return getNodeName();
		}

		@Override
		public Backbone getPred()
		{
			return pred;
		}

		@Override
		public Backbone getSucc()
		{
			return succ;
		}

		@Override
		protected AttributeDescriptor getAttributeDescriptor(
				String namespaceUri,
				String localName,
				String qualifiedName)
		{
			return null;
		}
	}
}
