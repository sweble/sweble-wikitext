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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.sweble.wom3.Wom3ElementNode;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3TableCellBase;
import org.sweble.wom3.Wom3TableColumn;
import org.sweble.wom3.Wom3TablePartition;
import org.sweble.wom3.Wom3TableRow;

public class TableAddressingTest
{
	private TableImpl t = (TableImpl) TestHelperDoc.genElem("table");

	private TableBodyImpl b;

	@Before
	public void before()
	{
		/*

			  <table border="1px" cellspacing="0px" cellpadding="12px" id="myTable">
			    <tbody>
			      <tr>
			        <td colspan="6">1.1 (,6)</td><td>1.2</td>
			      </tr>
			      <tr>
			        <td>2.1</td>
			        <td>2.2</td>
			        <td>2.3</td>
			        <td rowspan="2">2.4 (2,)</td>
			        <td>2.5</td>
			        <td>2.6</td>
			        <td>2.7</td>
			      </tr>

		*/

		TableRowImpl r;
		TableCellImpl c;

		b = (TableBodyImpl) TestHelperDoc.genElem("tbody");

		// -- row 1 ----

		r = (TableRowImpl) TestHelperDoc.genElem("tr");
		// add before finishing row
		b.appendChild(r);

		c = (TableCellImpl) TestHelperDoc.genElem("td");
		c.setColspan(6);
		addText(c, "1.1 (,6)");
		r.appendChild(c);

		addCell(r, "1.2");

		// -- row 2 ----

		r = (TableRowImpl) TestHelperDoc.genElem("tr");

		addCell(r, "2.1");
		addCell(r, "2.2");
		addCell(r, "2.3");

		c = (TableCellImpl) TestHelperDoc.genElem("td");
		c.setRowspan(2);
		addText(c, "2.4 (2,)");
		r.appendChild(c);

		addCell(r, "2.5");
		addCell(r, "2.6");
		addCell(r, "2.7");

		// add after finishing row
		b.appendChild(r);

		/*

			      <tr>
			        <td colspan="2" rowspan="2">3.1 (2,2)</td><td rowspan="2">3.2 (2,)</td><td>3.3</td>
			      </tr>
			      <tr>
			        <td>4.1</td><td>4.2</td><td>4.3</td><!--<td>X</td><td>X</td><td>X</td>-->
			      </tr>
			      <tr>
			        <td>5.1</td><td>5.2</td>
			      </tr>
			    </tbody>
			  </table>

		 */

		// why not attach?
		t.appendChild(b);

		// -- row 3 ----

		r = (TableRowImpl) TestHelperDoc.genElem("tr");
		// add before finishing row
		b.appendChild(r);

		c = (TableCellImpl) TestHelperDoc.genElem("td");
		c.setRowspan(2);
		c.setColspan(2);
		addText(c, "3.1 (2,2)");
		r.appendChild(c);

		c = (TableCellImpl) TestHelperDoc.genElem("td");
		c.setRowspan(2);
		addText(c, "3.2 (2,)");
		r.appendChild(c);

		addCell(r, "3.3");

		// -- row 4 ----

		r = (TableRowImpl) TestHelperDoc.genElem("tr");

		addCell(r, "4.1");
		addCell(r, "4.2");
		addCell(r, "4.3");
		Wom3ElementNode comment = TestHelperDoc.genElem("comment");
		Wom3ElementNode commentText = TestHelperDoc.genElem("text");
		commentText.setTextContent("<td>X</td><td>X</td><td>X</td>");
		comment.appendChild(commentText);
		r.appendChild(comment);

		// add after finishing row
		b.appendChild(r);

		// -- row 5 ----

		r = (TableRowImpl) TestHelperDoc.genElem("tr");

		addCell(r, "5.1");
		addCell(r, "5.2");

		// add after finishing row
		b.appendChild(r);
	}

	// =========================================================================

	/*
	           0  |  1  |    2     |    3     |  4  |  5  |  6

	         ---------------------------------------------------
		0   | 1.1 (,6)                                    | 1.2 |   0
		    |-----------|---------------------------------------|
		1   | 2.1 | 2.2 | 2.3      |          | 2.5 | 2.6 | 2.7 |   1
		    |-----------|----------| 2.4 (2,) |-----------------|
		2   |           |          |          | 3.3 |           |   2
		    | 3.1 (2,2) | 3.2 (2,) |----------|-----------------|
		3   |           |          | 4.1      | 4.2 | 4.3 |     |   3
		    |-----------|---------------------------------------|
		4   | 5.1 | 5.2 |                                       |   4
		     --------------------------------------------------

	           0  |  1  |    2     |    3     |  4  |  5  |  6

	 */

	@Test
	public void testNumRowsCols() throws Exception
	{
		assertEquals(5, b.getNumRows());
		assertEquals(7, b.getNumCols());
	}

	@Test
	public void testCorrectMatrix() throws Exception
	{
		// -- row 1 ----

		Wom3TableCellBase c00 = b.getCell(0, 0);
		assertEquals("1.1 (,6)", c00.getFirstChild().getTextContent());
		assertTrue(c00 == b.getCell(0, 1));
		assertTrue(c00 == b.getCell(0, 2));
		assertTrue(c00 == b.getCell(0, 3));
		assertTrue(c00 == b.getCell(0, 4));
		assertTrue(c00 == b.getCell(0, 5));

		assertEquals("1.2", b.getCell(0, 6).getFirstChild().getTextContent());

		// -- row 2 ----

		assertEquals("2.1", b.getCell(1, 0).getFirstChild().getTextContent());
		assertEquals("2.2", b.getCell(1, 1).getFirstChild().getTextContent());
		assertEquals("2.3", b.getCell(1, 2).getFirstChild().getTextContent());

		Wom3TableCellBase c13 = b.getCell(1, 3);
		assertEquals("2.4 (2,)", c13.getFirstChild().getTextContent());

		assertEquals("2.5", b.getCell(1, 4).getFirstChild().getTextContent());
		assertEquals("2.6", b.getCell(1, 5).getFirstChild().getTextContent());
		assertEquals("2.7", b.getCell(1, 6).getFirstChild().getTextContent());

		// -- row 3 ----

		Wom3TableCellBase c20 = b.getCell(2, 0);
		assertEquals("3.1 (2,2)", c20.getFirstChild().getTextContent());
		assertTrue(c20 == b.getCell(2, 1));

		Wom3TableCellBase c22 = b.getCell(2, 2);
		assertEquals("3.2 (2,)", c22.getFirstChild().getTextContent());

		assertTrue(c13 == b.getCell(2, 3));

		assertEquals("3.3", b.getCell(2, 4).getFirstChild().getTextContent());
		assertNull(b.getCell(2, 5));
		assertNull(b.getCell(2, 6));

		/*

		2   |           |          |          | 3.3 |           |   2
		    | 3.1 (2,2) | 3.2 (2,) |----------|-----------------|
		3   |           |          | 4.1      | 4.2 | 4.3 |     |   3
		    |-----------|---------------------------------------|
		4   | 5.1 | 5.2 |                                       |   4
		     --------------------------------------------------

		       0  |  1  |    2     |    3     |  4  |  5  |  6

		 */

		// -- row 4 ----

		assertTrue(c20 == b.getCell(3, 0));
		assertTrue(c20 == b.getCell(3, 1));

		assertTrue(c22 == b.getCell(3, 2));

		assertEquals("4.1", b.getCell(3, 3).getFirstChild().getTextContent());
		assertEquals("4.2", b.getCell(3, 4).getFirstChild().getTextContent());
		assertEquals("4.3", b.getCell(3, 5).getFirstChild().getTextContent());

		assertNull(b.getCell(3, 6));

		// -- row 5 ----

		assertEquals("5.1", b.getCell(4, 0).getFirstChild().getTextContent());
		assertEquals("5.2", b.getCell(4, 1).getFirstChild().getTextContent());

		assertNull(b.getCell(4, 2));
		assertNull(b.getCell(4, 3));
		assertNull(b.getCell(4, 4));
		assertNull(b.getCell(4, 5));
		assertNull(b.getCell(4, 6));
	}

	// =========================================================================

	@Test
	public void testTableIndexGetters() throws Exception
	{
		assertEquals(2, b.getCell(2, 0).getRowIndex());
		assertEquals(2, b.getCell(2, 1).getRowIndex());
		assertEquals(2, b.getCell(3, 0).getRowIndex());
		assertEquals(2, b.getCell(3, 1).getRowIndex());
		assertEquals(0, b.getCell(2, 0).getColIndex());
		assertEquals(0, b.getCell(2, 1).getColIndex());
		assertEquals(0, b.getCell(3, 0).getColIndex());
		assertEquals(0, b.getCell(3, 1).getColIndex());
	}

	@Test(expected = IllegalStateException.class)
	public void testIndexGettersDontWorkForDetachedCells() throws Exception
	{
		Wom3TableCellBase cell = b.getCell(2, 0);
		b.getRow(cell.getRowIndex()).removeChild(cell);
		cell.getRowIndex();
	}

	@Test(expected = IllegalStateException.class)
	public void testIndexGettersDontWorkForCellsInDetachedRows() throws Exception
	{
		Wom3TableCellBase cell = b.getCell(2, 0);
		b.removeChild(b.getRow(cell.getRowIndex()));
		cell.getRowIndex();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCannotSetInvalidColspan() throws Exception
	{
		b.getCell(0, 0).setColspan(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCannotSetInvalidRowspan() throws Exception
	{
		b.getCell(0, 0).setRowspan(0);
	}

	@Test
	public void testChangeColspanChangedTableLayout() throws Exception
	{
		b.getCell(0, 0).setColspan(5);
		assertEquals("1.2", b.getCell(0, 5).getFirstChild().getTextContent());
		assertNull(b.getCell(0, 6));
	}

	@Test
	public void testChangesReduceTableCols() throws Exception
	{
		assertEquals(7, b.getNumCols());
		b.getCell(0, 0).setColspan(5);
		Wom3TableCellBase c15 = b.getCell(1, 5);
		b.getRow(c15.getRowIndex()).removeChild(c15);
		assertEquals("2.7", b.getCell(1, 5).getFirstChild().getTextContent());
		assertEquals(6, b.getNumCols());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testIllegalCellsRowIndexThrows() throws Exception
	{
		b.getCell(5, 0);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testIllegalCellsColIndexThrows() throws Exception
	{
		b.getCell(0, 7);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testIllegalRowIndexThrows() throws Exception
	{
		b.getRow(5);
	}

	@Test
	public void testDetachModifyAndAttach() throws Exception
	{
		assertEquals(7, b.getNumCols());

		Wom3TableCellBase c00 = b.getCell(0, 0);
		Wom3TableRow r0 = b.getRow(0);
		b.removeChild(r0);
		c00.setColspan(5);

		Wom3TableCellBase c15 = b.getCell(0, 5);
		Wom3TableRow r1 = b.getRow(0);
		b.removeChild(r1);
		r1.removeChild(c15);

		b.insertBefore(r1, b.getRow(0));
		b.insertBefore(r0, b.getRow(0));

		assertEquals("2.7", b.getCell(1, 5).getFirstChild().getTextContent());
		assertEquals(6, b.getNumCols());
	}

	@Test
	public void testAddressingInRow() throws Exception
	{
		for (int r = 0; r < b.getNumRows(); ++r)
		{
			Wom3TableRow row = b.getRow(r);
			assertEquals(r, row.getRowIndex());
			assertEquals(b.getNumCols(), row.getNumCols());
			for (int c = 0; c < b.getNumCols(); ++c)
				assertTrue(b.getCell(r, c) == row.getCell(c));
		}
	}

	@Test(expected = IllegalStateException.class)
	public void testIndexGettersDontWorkForDetachedRow() throws Exception
	{
		Wom3TableRow r = b.getRow(1);
		b.removeChild(r);
		r.getRowIndex();
	}

	@Test(expected = IllegalStateException.class)
	public void testRowGettersDontWorkForDetachedPartition() throws Exception
	{
		Wom3TableRow r = b.getRow(1);
		for (Wom3TablePartition part : new ArrayList<Wom3TablePartition>(t.getPartitions()))
			t.removeChild(part);
		r.getRowIndex();
	}

	@Test(expected = IllegalStateException.class)
	public void testGettersDontWorkForDetachedPartition() throws Exception
	{
		for (Wom3TablePartition part : new ArrayList<Wom3TablePartition>(t.getPartitions()))
			t.removeChild(part);
		b.getCol(1);
	}

	@Test
	public void testColAddressing() throws Exception
	{
		for (int c = 0; c < b.getNumCols(); ++c)
		{
			Wom3TableColumn col = b.getCol(c);
			assertEquals(c, col.getColIndex());
			assertEquals(b.getNumRows(), col.getNumRows());
			for (int r = 0; r < b.getNumRows(); ++r)
				assertTrue(b.getCell(r, c) == col.getCell(r));
		}
	}

	// =========================================================================

	private void addCell(TableRowImpl r, String text)
	{
		TableCellImpl c = (TableCellImpl) TestHelperDoc.genElem("td");
		addText(c, text);
		r.appendChild(c);
	}

	private void addText(Wom3Node node, String text)
	{
		node.appendChild(TestHelperDoc.genXmlText(text));
	}
}
