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
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.sweble.wom3.Wom3ListItem;
import org.sweble.wom3.Wom3Node;

public class ListTest
{
	private OrderedListImpl list;

	@Before
	public void setUp()
	{
		this.list = (OrderedListImpl) TestHelperDoc.genElem("ol");
	}

	@Test
	public void testEmptyListIsEmpty() throws Exception
	{
		assertEquals(0, list.getItemNum());
		assertTrue(list.getItems().isEmpty());
	}

	@Test
	public void testAddListItemShowsInList() throws Exception
	{
		list.appendItem(gen(1));
		assertEquals(1, list.getItemNum());
		assertEquals(1, list.getItems().size());
	}

	@Test
	public void testAddNonListItemDoesNotShowInList() throws Exception
	{
		assertFalse(list.hasChildNodes());
		list.appendChild(genBold(1));
		assertEquals(0, list.getItemNum());
		assertEquals(0, list.getItems().size());
		assertTrue(list.hasChildNodes());
	}

	@Test
	public void testAddItemWithAppendChildShowsInList() throws Exception
	{
		list.appendChild(gen(1));
		assertEquals(1, list.getItemNum());
		assertEquals(1, list.getItems().size());
	}

	@Test
	public void testGetByIndex() throws Exception
	{
		Wom3ListItem l1 = gen(1);
		list.appendChild(l1);
		list.appendChild(genBold(2));
		Wom3ListItem l2 = gen(3);
		list.appendChild(l2);
		list.appendChild(genBold(4));
		Wom3ListItem l3 = gen(5);
		list.appendChild(l3);
		list.appendChild(genBold(6));

		assertEquals(3, list.getItemNum());
		assertEquals(3, list.getItems().size());
		assertTrue(list.getItem(0) == l1);
		assertTrue(list.getItem(1) == l2);
		assertTrue(list.getItem(2) == l3);
	}

	@Test
	public void testRemoveByIndex() throws Exception
	{
		list.appendChild(gen(1));
		list.appendChild(genBold(2));
		list.appendChild(gen(3));
		list.appendChild(genBold(3));
		list.appendChild(gen(4));
		list.appendChild(genBold(5));

		list.removeItem(1);

		assertEquals(2, list.getItemNum());
		assertEquals(2, list.getItems().size());
		int i = 1;
		Iterator<Wom3Node> j = list.iterator();
		while (j.hasNext())
			assertEquals(i++, Integer.parseInt(j.next().getFirstChild().getTextContent()));
	}

	@Test
	public void testRemoveChild() throws Exception
	{
		Wom3ListItem l1 = gen(1);
		list.appendChild(l1);
		list.appendChild(gen(1));
		list.appendChild(genBold(2));
		Wom3ListItem l2 = gen(3);
		list.appendChild(l2);
		list.appendChild(genBold(3));
		list.appendChild(gen(4));
		list.appendChild(gen(5));
		Wom3Node b5 = genBold(6);
		list.appendChild(b5);
		list.appendChild(genBold(6));

		list.removeChild(l2);
		list.removeChild(b5);
		list.removeChild(l1);

		assertEquals(3, list.getItemNum());
		assertEquals(3, list.getItems().size());
		int i = 1;
		Iterator<Wom3Node> j = list.iterator();
		while (j.hasNext())
			assertEquals(i++, Integer.parseInt(j.next().getFirstChild().getTextContent()));
	}

	@Test
	public void testReplaceItem() throws Exception
	{
		list.appendChild(genBold(1));
		Wom3ListItem l2 = gen(2);
		list.appendChild(l2);
		list.appendChild(genBold(3));

		list.replaceItem(0, gen(2));

		assertEquals(1, list.getItemNum());
		assertEquals(1, list.getItems().size());
		int i = 1;
		Iterator<Wom3Node> j = list.iterator();
		while (j.hasNext())
		{
			Wom3Node next = j.next();
			assertFalse(next == l2);
			assertEquals(i++, Integer.parseInt(next.getFirstChild().getTextContent()));
		}
	}

	@Test
	public void testInsertItem() throws Exception
	{
		list.appendChild(genBold(1));
		Wom3ListItem l1 = gen(2);
		list.insertItem(0, l1);
		Wom3ListItem l3 = gen(4);
		list.insertItem(1, l3);
		list.appendChild(genBold(5));
		Wom3ListItem l2 = gen(3);
		list.insertItem(1, l2);

		assertEquals(3, list.getItemNum());
		assertEquals(3, list.getItems().size());
		assertTrue(list.getItem(0) == l1);
		assertTrue(list.getItem(1) == l2);
		assertTrue(list.getItem(2) == l3);

		int i = 1;
		Iterator<Wom3Node> j = list.iterator();
		while (j.hasNext())
			assertEquals(i++, Integer.parseInt(j.next().getFirstChild().getTextContent()));
	}

	@Test
	public void testCompactAttribute() throws Exception
	{
		TestHelperAttribute.testBooleanAttribute(list, "compact", "isCompact", "setCompact");
	}

	// =========================================================================

	private Wom3Node genBold(int i)
	{
		BoldImpl bold = (BoldImpl) TestHelperDoc.genElem("b");
		bold.appendChild(genText(i));
		return bold;
	}

	private Wom3ListItem gen(int i)
	{
		ListItemImpl item = (ListItemImpl) TestHelperDoc.genElem("li");
		item.appendChild(genText(i));
		return item;
	}

	private TextImpl genText(int i)
	{
		TextImpl text = (TextImpl) TestHelperDoc.genElem("text");
		text.setTextContent(String.valueOf(i));
		return text;
	}
}
