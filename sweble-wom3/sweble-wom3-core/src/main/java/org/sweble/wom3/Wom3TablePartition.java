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
package org.sweble.wom3;

/**
 * The interface to access the cells, rows and columns of table head, body or
 * foot.
 * 
 * Rows and cells are accessed via integer indices. <b>Only valid items are
 * counted.</b> If a table partition is given in HTML that contains invalid
 * content (e.g.: text or elements other than <code>&lt;tr></code>), these
 * elements are skipped in the enumeration and are not accessible through this
 * interface. However, they can be iterated using the methods provided by the
 * WomNode interface.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "thead", "tbody" or
 * "tfoot".
 * 
 * <b>Child elements:</b> ([Preprocessor elements]|tr)*
 */
public interface Wom3TablePartition
		extends
			Wom3ElementNode
{
	/**
	 * Get the number of columns.
	 * 
	 * @return The number of columns.
	 */
	public int getNumCols();

	/**
	 * Get the number of rows.
	 * 
	 * @return The number of rows.
	 */
	public int getNumRows();

	/**
	 * Get the i'th row.
	 * 
	 * @param row
	 *            The zero-based index of the row to retrieve.
	 * @return The i'th row.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>row &lt; 0</code> or
	 *             <code>row >= getNumRows()</code>.
	 */
	public Wom3TableRow getRow(int row) throws IndexOutOfBoundsException;

	/**
	 * Get the i'th column.
	 * 
	 * @param col
	 *            The zero-based index of the column to retrieve.
	 * @return The i'th column.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>row &lt; 0</code> or
	 *             <code>col >= getNumCols()</code>.
	 */
	public Wom3TableColumn getCol(int col) throws IndexOutOfBoundsException;

	/**
	 * Retrieve the specified cell.
	 * 
	 * If the specified cell does not exist but is part of a rowspan/colspan
	 * cell, then the respective rowspan/colspan cell will be returned.
	 * 
	 * @param row
	 *            The zero-based index of the row in which the cell is found.
	 * @param col
	 *            The zero-based index of the column in which the cell is found.
	 * @return The specified cell.
	 * @throws IndexOutOfBoundsException
	 *             If the specified cell does not exist.
	 */
	public Wom3TableCellBase getCell(int row, int col) throws IndexOutOfBoundsException;

	// ==[ The XHTML Attributes ]===============================================

	/**
	 * Get the horizontal alignment of the row's content.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @return The horizontal alignment or <code>null</code> if the attribute is
	 *         not specified.
	 */
	public Wom3HorizAlign getAlign();

	/**
	 * Set the horizontal alignment of the row's content.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @param align
	 *            The new horizontal alignment or <code>null</code> to remove
	 *            the attribute.
	 * @return The old horizontal alignment.
	 */
	public Wom3HorizAlign setAlign(Wom3HorizAlign align);

	/**
	 * Get the vertical alignment of the row's content.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "valign".
	 * 
	 * @return The vertical alignment or <code>null</code> if the attribute is
	 *         not specified.
	 */
	public Wom3TableVAlign getVAlign();

	/**
	 * Set the vertical alignment of the row's content.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "valign".
	 * 
	 * @param valign
	 *            The new vertical alignment or <code>null</code> to remove the
	 *            attribute.
	 * @return The old vertical alignment.
	 */
	public Wom3TableVAlign setTableVAlign(Wom3TableVAlign valign);
}
