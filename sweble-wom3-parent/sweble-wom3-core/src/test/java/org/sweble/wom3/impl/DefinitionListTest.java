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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.sweble.wom3.Wom3Bold;
import org.sweble.wom3.Wom3DefinitionList;
import org.sweble.wom3.Wom3DefinitionListDef;
import org.sweble.wom3.Wom3DefinitionListItem;
import org.sweble.wom3.Wom3DefinitionListTerm;
import org.sweble.wom3.Wom3Node;

public class DefinitionListTest
{
	private Wom3DefinitionList list;

	private Wom3DefinitionListTerm[] t;

	private Wom3DefinitionListDef[] d;

	private Wom3Node[] b;

	@Before
	public void setUp()
	{
		//ArticleImpl page = new ArticleImpl("", "", "DefListTest");
		//this.list = new DefinitionListImpl();
		//page.getBody().appendChild(this.list);
		this.list = (Wom3DefinitionList) TestHelperDoc.genElem("dl");

		t = new Wom3DefinitionListTerm[8];
		for (int i = 0; i < t.length; ++i)
			t[i] = term(i);

		d = new Wom3DefinitionListDef[8];
		for (int i = 0; i < d.length; ++i)
			d[i] = def(i);

		b = new Wom3Node[8];
		for (int i = 0; i < b.length; ++i)
			b[i] = genBold(i);
	}

	@Test
	public void testNewListIsEmpty() throws Exception
	{
		assertFalse(list.hasChildNodes());
		assertEquals(0, list.getTermNum());
		assertEquals(0, list.getItemNum());
	}

	@Test
	public void testCollectionsWorkForNewList() throws Exception
	{
		assertTrue(list.getItems().isEmpty());
		assertTrue(list.getTerms().isEmpty());
	}

	@Test
	public void testAddTerm() throws Exception
	{
		list.appendTerm(t[0]);
		assertTrue(list.hasChildNodes());
		assertEquals(1, list.getTermNum());
		assertEquals(1, list.getItemNum());
		assertTrue(list.getTerm(0) == t[0]);
		assertTrue(list.getItem(0) == t[0]);
		assertTrue(list.getFirstChild() == t[0]);
		assertTrue(list.getLastChild() == t[0]);
	}

	@Test
	public void testAddTermWithDefs() throws Exception
	{
		t[0].appendDef(d[0]);
		t[0].appendDef(d[1]);
		list.appendTerm(t[0]);
		assertEquals(3, list.getWomChildNodes().size());
		assertEquals(1, list.getTermNum());
		assertEquals(3, list.getItemNum());
		cmpChildren(t[0], d[0], d[1]);
	}

	@Test
	public void testAddTermWithDefsInBetween() throws Exception
	{
		t[0].appendDef(d[0]);
		t[0].appendDef(d[1]);
		list.appendTerm(t[0]);

		t[1].appendDef(d[2]);
		t[1].appendDef(d[3]);
		list.appendTerm(t[1]);

		t[2].appendDef(d[4]);
		t[2].appendDef(d[5]);
		list.insertTerm(1, t[2]);

		cmpChildren(t[0], d[0], d[1], t[2], d[4], d[5], t[1], d[2], d[3]);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInsertAtMinusOneThrows() throws Exception
	{
		list.insertTerm(-1, t[0]);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInsertAtXGreaterSizeThrows() throws Exception
	{
		list.insertTerm(1, t[0]);
	}

	@Test
	public void testInsertAt0InEmptyListWorks() throws Exception
	{
		list.insertTerm(0, t[0]);
		cmpChildren(t[0]);
	}

	@Test
	public void testReplaceTermWorks() throws Exception
	{
		t[0].appendDef(d[0]);
		t[0].appendDef(d[1]);
		list.appendTerm(t[0]);

		t[1].appendDef(d[2]);
		t[1].appendDef(d[3]);
		list.appendTerm(t[1]);

		t[2].appendDef(d[4]);
		t[2].appendDef(d[5]);
		list.appendTerm(t[2]);

		t[3].appendDef(d[6]);
		t[3].appendDef(d[7]);
		list.replaceTerm(1, t[3]);

		cmpChildren(t[0], d[0], d[1], t[3], d[6], d[7], t[2], d[4], d[5]);
	}

	@Test
	public void testRemoveTermWorks() throws Exception
	{
		t[0].appendDef(d[0]);
		t[0].appendDef(d[1]);
		list.appendTerm(t[0]);

		t[1].appendDef(d[2]);
		t[1].appendDef(d[3]);
		list.appendTerm(t[1]);

		t[2].appendDef(d[4]);
		t[2].appendDef(d[5]);
		list.appendTerm(t[2]);

		list.removeTerm(1);
		cmpChildren(t[0], d[0], d[1], t[2], d[4], d[5]);
	}

	@Test
	public void testGetItemsWithAllKindsOfChildrenPresent() throws Exception
	{
		list.appendChild(b[0]);
		t[0].appendDef(d[0]);
		t[0].appendDef(d[1]);
		list.appendTerm(t[0]);
		list.insertBefore(b[1], d[1]);
		t[1].appendDef(d[2]);
		t[1].appendDef(d[3]);
		list.appendTerm(t[1]);

		cmpChildren(b[0], t[0], d[0], b[1], d[1], t[1], d[2], d[3]);
		cmpItems(t[0], d[0], d[1], t[1], d[2], d[3]);
		cmpTerms(t[0], t[1]);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetItemFailsWithIndexEqualsSize() throws Exception
	{
		list.removeItem(0);
	}

	@Test
	public void testRemoveItemWorksWithDt() throws Exception
	{
		t[0].appendDef(d[0]);
		t[0].appendDef(d[1]);
		list.appendTerm(t[0]);
		t[1].appendDef(d[2]);
		t[1].appendDef(d[3]);
		list.appendTerm(t[1]);

		list.removeItem(3);

		cmpChildren(t[0], d[0], d[1]);
		cmpItems(t[0], d[0], d[1]);
		cmpTerms(t[0]);
	}

	@Test
	public void testRemoveItemWorksWithDd() throws Exception
	{
		t[0].appendDef(d[0]);
		t[0].appendDef(d[1]);
		list.appendTerm(t[0]);

		list.removeItem(2);

		cmpChildren(t[0], d[0]);
		cmpItems(t[0], d[0]);
		cmpTerms(t[0]);
	}

	@Test
	public void testReplaceItemWorksWithDt() throws Exception
	{
		t[0].appendDef(d[0]);
		t[0].appendDef(d[1]);
		list.appendTerm(t[0]);
		t[1].appendDef(d[2]);
		t[1].appendDef(d[3]);
		list.appendTerm(t[1]);
		t[2].appendDef(d[4]);
		t[2].appendDef(d[5]);
		list.appendTerm(t[2]);

		t[3].appendDef(d[6]);
		t[3].appendDef(d[7]);
		list.replaceItem(3, t[3]);

		cmpChildren(t[0], d[0], d[1], t[3], d[6], d[7], t[2], d[4], d[5]);
		cmpItems(t[0], d[0], d[1], t[3], d[6], d[7], t[2], d[4], d[5]);
		cmpTerms(t[0], t[3], t[2]);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testReplaceDtWithDdThrows() throws Exception
	{
		list.appendTerm(t[0]);
		list.replaceItem(0, d[0]);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetItemWithIndexGreaterSize() throws Exception
	{
		list.getItem(1);
	}

	@Test
	public void testAppendItemWorks() throws Exception
	{
		list.appendItem(d[0]);
		list.appendItem(d[1]);
		list.appendItem(t[0]);
		list.appendItem(d[2]);
		list.appendItem(d[3]);

		cmpChildren(d[0], d[1], t[0], d[2], d[3]);
		cmpItems(d[0], d[1], t[0], d[2], d[3]);
		cmpTerms(t[0]);
	}

	@Test
	public void testInsertItemAsFirst() throws Exception
	{
		list.insertItem(0, d[0]);

		cmpChildren(d[0]);
		cmpItems(d[0]);
		cmpTerms();
	}

	@Test
	public void testInsertItemAsLast() throws Exception
	{
		list.insertItem(0, d[0]);
		list.insertItem(1, d[1]);

		cmpChildren(d[0], d[1]);
		cmpItems(d[0], d[1]);
		cmpTerms();
	}

	@Test
	public void testInsertItemInTheMiddle() throws Exception
	{
		list.insertItem(0, d[0]);
		list.insertItem(1, d[1]);
		list.insertItem(1, d[2]);

		cmpChildren(d[0], d[2], d[1]);
		cmpItems(d[0], d[2], d[1]);
		cmpTerms();
	}

	@Test
	public void testRemoveNonItemChild() throws Exception
	{
		list.appendChild(d[0]);
		list.appendChild(b[0]);
		list.appendChild(t[0]);
		list.appendChild(d[1]);
		list.appendChild(b[1]);
		list.appendChild(d[2]);

		list.removeChild(b[0]);
		list.removeChild(b[1]);

		cmpChildren(d[0], t[0], d[1], d[2]);
		cmpItems(d[0], t[0], d[1], d[2]);
		cmpTerms(t[0]);
	}

	@Test
	public void testInsertDtAsFirstTerm() throws Exception
	{
		list.appendChild(d[0]);
		list.appendChild(b[0]);
		list.appendChild(t[0]);
		list.appendChild(d[1]);
		list.appendChild(b[1]);
		list.appendChild(d[2]);
		list.insertTerm(0, t[1]);
		cmpChildren(d[0], b[0], t[1], t[0], d[1], b[1], d[2]);
		cmpItems(d[0], t[1], t[0], d[1], d[2]);
		cmpTerms(t[1], t[0]);
	}

	@Test
	public void testRemoveFirstDt() throws Exception
	{
		list.appendChild(t[0]);
		list.appendChild(d[0]);
		list.appendChild(t[1]);
		list.appendChild(d[1]);

		list.removeItem(0);

		cmpChildren(t[1], d[1]);
		cmpItems(t[1], d[1]);
		cmpTerms(t[1]);
	}

	@Test
	public void testInsertDtAsFirstItem() throws Exception
	{
		list.appendChild(d[0]);
		list.appendChild(t[0]);
		list.appendChild(d[1]);
		list.appendChild(d[2]);

		t[1].appendDef(d[3]);
		list.insertItem(0, t[1]);

		cmpChildren(t[1], d[3], d[0], t[0], d[1], d[2]);
		cmpItems(t[1], d[3], d[0], t[0], d[1], d[2]);
		cmpTerms(t[1], t[0]);
	}

	@Test
	public void testInsertDtAsFirstItemWithGarbageInBetween() throws Exception
	{
		list.appendChild(b[2]);
		list.appendChild(d[0]);
		list.appendChild(b[0]);
		list.appendChild(t[0]);
		list.appendChild(d[1]);
		list.appendChild(b[1]);
		list.appendChild(d[2]);

		t[1].appendDef(d[3]);
		list.insertItem(0, t[1]);

		cmpChildren(b[2], t[1], d[3], d[0], b[0], t[0], d[1], b[1], d[2]);
		cmpItems(t[1], d[3], d[0], t[0], d[1], d[2]);
		cmpTerms(t[1], t[0]);
	}

	@Test
	public void testInsertDtAsFirstItemWithGarbageInBetween2() throws Exception
	{
		list.appendChild(t[0]);
		list.appendChild(b[0]);
		list.appendChild(d[0]);
		list.appendChild(b[1]);
		list.appendChild(d[1]);
		list.appendChild(b[2]);
		list.appendChild(d[2]);

		t[1].appendDef(d[3]);
		list.insertItem(2, t[1]);

		cmpChildren(t[0], b[0], d[0], b[1], t[1], d[3], d[1], b[2], d[2]);
		cmpItems(t[0], d[0], t[1], d[3], d[1], d[2]);
		cmpTerms(t[0], t[1]);
	}

	@Test
	public void testReplaceDefInUnlinkedTermAndLinkTerm() throws Exception
	{
		t[0].appendDef(d[0]);
		t[0].replaceDef(0, d[1]);
		list.appendTerm(t[0]);
		cmpChildren(t[0], d[1]);
		cmpItems(t[0], d[1]);
		cmpTerms(t[0]);
	}

	@Test
	public void testAppendAndReplaceDefInLinkedTerm() throws Exception
	{
		list.appendTerm(t[0]);
		t[0].appendDef(d[0]);
		t[0].replaceDef(0, d[1]);
		cmpChildren(t[0], d[1]);
		cmpItems(t[0], d[1]);
		cmpTerms(t[0]);
	}

	@Test
	public void testRemoveDefFromUnlinkedTermAndLinkTerm() throws Exception
	{
		t[0].appendDef(d[0]);
		t[0].appendDef(d[1]);
		t[0].appendDef(d[2]);

		t[0].removeDef(1);
		list.appendChild(t[0]);

		cmpChildren(t[0], d[0], d[2]);
		cmpItems(t[0], d[0], d[2]);
		cmpTerms(t[0]);
	}

	@Test
	public void testRemoveDefFromLinkedTerm() throws Exception
	{
		t[0].appendDef(d[0]);
		t[0].appendDef(d[1]);
		t[0].appendDef(d[2]);
		list.appendChild(t[0]);

		t[0].removeDef(1);

		cmpChildren(t[0], d[0], d[2]);
		cmpItems(t[0], d[0], d[2]);
		cmpTerms(t[0]);
	}

	@Test
	public void testAppendDefsToLinkedTermOutOfOrder() throws Exception
	{
		list.appendChild(t[0]);
		t[0].appendDef(d[0]);
		list.appendChild(t[1]);
		list.appendChild(t[2]);
		t[1].appendDef(d[1]);
		t[2].appendDef(d[3]);
		t[1].appendDef(d[2]);

		cmpChildren(t[0], d[0], t[1], d[1], d[2], t[2], d[3]);
		cmpItems(t[0], d[0], t[1], d[1], d[2], t[2], d[3]);
		cmpTerms(t[0], t[1], t[2]);
	}

	@Test
	public void testInsertDefsIntoUnlinkedTermThenLinkTerm() throws Exception
	{
		t[0].insertDef(0, d[0]);
		t[0].insertDef(0, d[1]);
		t[0].insertDef(2, d[2]);
		list.appendTerm(t[0]);

		cmpChildren(t[0], d[1], d[0], d[2]);
		cmpItems(t[0], d[1], d[0], d[2]);
		cmpTerms(t[0]);
	}

	@Test
	public void testInsertDefsIntoLinkedTerm() throws Exception
	{
		list.appendTerm(t[0]);
		t[0].insertDef(0, d[0]);
		t[0].insertDef(0, d[1]);
		t[0].insertDef(2, d[2]);

		cmpChildren(t[0], d[1], d[0], d[2]);
		cmpItems(t[0], d[1], d[0], d[2]);
		cmpTerms(t[0]);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInsertingAtNegativeIndexThrows() throws Exception
	{
		list.appendTerm(t[0]);
		t[0].insertDef(-1, d[0]);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInsertingAtTooBigIndexThrows() throws Exception
	{
		list.appendTerm(t[0]);
		t[0].insertDef(1, d[0]);
	}

	@Test
	public void testCompactAttribute() throws Exception
	{
		TestHelperAttribute.testBooleanAttribute(list, "compact", "isCompact", "setCompact");
	}

	// =========================================================================

	private void cmpChildren(Wom3Node... nodes)
	{
		Wom3Node i = list.getFirstChild();
		int j = 0;
		for (Wom3Node child : nodes)
		{
			assertNotNull(String.format("list.length < nodes.length", j, j), i);
			assertTrue(String.format("list[%d] != nodes[%d]", j, j), child == i);
			i = i.getNextSibling();
			++j;
		}
		assertNull("list.length > nodes.length", i);
	}

	private void cmpItems(Wom3Node... nodes)
	{
		int j = 0;
		Iterator<Wom3DefinitionListItem> i = list.getItems().iterator();
		for (Wom3Node child : nodes)
		{
			assertTrue(String.format("list.length < nodes.length", j, j), i.hasNext());
			assertTrue(String.format("list[%d] != nodes[%d]", j, j), child == i.next());
			++j;
		}
		assertFalse("list.length > nodes.length", i.hasNext());
	}

	private void cmpTerms(Wom3Node... nodes)
	{
		int j = 0;
		Iterator<Wom3DefinitionListTerm> i = list.getTerms().iterator();
		for (Wom3Node child : nodes)
		{
			assertTrue(String.format("list.length < nodes.length", j, j), i.hasNext());
			assertTrue(String.format("list[%d] != nodes[%d]", j, j), child == i.next());
			++j;
		}
		assertFalse("list.length > nodes.length", i.hasNext());
	}

	private Wom3Node genBold(int i)
	{
		Wom3Bold bold = (Wom3Bold) TestHelperDoc.genElem("b");
		bold.appendChild(genText(i));
		return bold;
	}

	private Wom3DefinitionListTerm term(int i)
	{
		Wom3DefinitionListTerm item =
				(Wom3DefinitionListTerm) TestHelperDoc.genElem("dt");
		item.appendChild(genText(i));
		return item;
	}

	private Wom3DefinitionListDef def(int i)
	{
		Wom3DefinitionListDef item =
				(Wom3DefinitionListDef) TestHelperDoc.genElem("dd");
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
