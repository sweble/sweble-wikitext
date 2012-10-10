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
package org.sweble.wikitext.engine.wom;

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
public interface WomTablePartition
		extends
			WomNode
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
	public WomTableRow getRow(int row) throws IndexOutOfBoundsException;
	
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
	public WomTableColumn getCol(int col) throws IndexOutOfBoundsException;
	
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
	public WomTableCellBase getCell(int row, int col) throws IndexOutOfBoundsException;
	
	// ==[ Row modification ]===================================================
	
	/**
	 * Append a new row to the end of the table.
	 * 
	 * @param row
	 *            The row to append.
	 */
	public void appendRow(WomTableRow row);
	
	/**
	 * Insert a row in front of another specified row.
	 * 
	 * @param before
	 *            The index of the row in front of which the new row is to be
	 *            inserted.
	 * @param row
	 *            The row to insert.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>before &lt; 0</code> or
	 *             <code>before > getNumRows()</code>.
	 */
	public void insertRow(int before, WomTableRow row) throws IndexOutOfBoundsException;
	
	/**
	 * Insert a row in front of another specified row.
	 * 
	 * @param before
	 *            The row in front of which the new row is to be inserted.
	 * @param row
	 *            The row to insert.
	 * @throws IllegalArgumentException
	 *             Thrown if <code>before</code> is not a row of this table.
	 */
	public void insertRow(WomTableRow before, WomTableRow row) throws IllegalArgumentException;
	
	/**
	 * Replace a row with another row.
	 * 
	 * @param row
	 *            The index of the row to replace.
	 * @param replace
	 *            The replacement row.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>row &lt; 0</code> or
	 *             <code>row > getNumRows()</code>.
	 */
	public void replaceRow(int row, WomTableRow replace) throws IndexOutOfBoundsException;
	
	/**
	 * Replace a row with another row.
	 * 
	 * @param search
	 *            The row to replace.
	 * @param replace
	 *            The replacement row.
	 * @throws IllegalArgumentException
	 *             Thrown if <code>search</code> is not a row of this table.
	 */
	public void replaceRow(WomTableRow search, WomTableRow replace) throws IllegalArgumentException;
	
	/**
	 * Remove a row from this table.
	 * 
	 * @param row
	 *            The index of the row to remove.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>row &lt; 0</code> or
	 *             <code>row > getNumRows()</code>.
	 */
	public void removeRow(int row) throws IndexOutOfBoundsException;
	
	/**
	 * Remove a row from this table.
	 * 
	 * @param row
	 *            The row to remove.
	 * @throws IllegalArgumentException
	 *             Thrown if <code>row</code> is not a row of this table.
	 */
	public void removeRow(WomTableRow row) throws IllegalArgumentException;
	
	// ==[ Column modification ]================================================
	
	/**
	 * Append a new column to the end of the table.
	 * 
	 * @param col
	 *            The column to append.
	 */
	public void appendCol(WomTableColumn col);
	
	/**
	 * Insert a column in front of another specified column.
	 * 
	 * @param before
	 *            The index of the column in front of which the new column is to
	 *            be inserted.
	 * @param col
	 *            The column to insert.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>before &lt; 0</code> or
	 *             <code>before > getNumCols()</code>.
	 */
	public void insertCol(int before, WomTableColumn col) throws IndexOutOfBoundsException;
	
	/**
	 * Insert a column in front of another specified column.
	 * 
	 * @param before
	 *            The column in front of which the new column is to be inserted.
	 * @param col
	 *            The column to insert.
	 * @throws IllegalArgumentException
	 *             Thrown if <code>before</code> is not a column of this table.
	 */
	public void insertCol(WomTableColumn before, WomTableColumn col) throws IllegalArgumentException;
	
	/**
	 * Replace a column with another column.
	 * 
	 * @param col
	 *            The index of the column to replace.
	 * @param replace
	 *            The replacement column.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>col &lt; 0</code> or
	 *             <code>col > getNumCols()</code>.
	 */
	public void replaceCol(int col, WomTableColumn replace) throws IndexOutOfBoundsException;
	
	/**
	 * Replace a column with another column.
	 * 
	 * @param search
	 *            The column to replace.
	 * @param replace
	 *            The replacement column.
	 * @throws IllegalArgumentException
	 *             Thrown if <code>search</code> is not a column of this table.
	 */
	public void replaceCol(WomTableColumn search, WomTableColumn replace) throws IllegalArgumentException;
	
	/**
	 * Remove a column from this table.
	 * 
	 * @param col
	 *            The index of the column to remove.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>col &lt; 0</code> or
	 *             <code>col > getNumCols()</code>.
	 */
	public void removeCol(int col) throws IndexOutOfBoundsException;
	
	/**
	 * Remove a column from this table.
	 * 
	 * @param col
	 *            The column to remove.
	 * @throws IllegalArgumentException
	 *             Thrown if <code>col</code> is not a column of this table.
	 */
	public void removeCol(WomTableColumn col) throws IllegalArgumentException;
	
	// ==[ The XHTML Attributes ]===============================================
	
	/**
	 * Get the horizontal alignment of the row's content.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @return The horizontal alignment or <code>null</code> if the attribute is
	 *         not specified.
	 */
	public WomHorizAlign getAlign();
	
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
	public WomHorizAlign setAlign(WomHorizAlign align);
	
	/**
	 * Get the vertical alignment of the row's content.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "valign".
	 * 
	 * @return The vertical alignment or <code>null</code> if the attribute is
	 *         not specified.
	 */
	public WomTableVAlign getVAlign();
	
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
	public WomTableVAlign setTableVAlign(WomHorizAlign valign);
	
	/**
	 * Get the row's alignment character.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "char".
	 * 
	 * @return The alignment character or <code>null</code> if the attribute is
	 *         not specified.
	 */
	public Character getChar();
	
	/**
	 * Set the row's alignment character.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "char".
	 * 
	 * @param ch
	 *            The new alignment character or <code>null</code> to remove the
	 *            attribute.
	 * @return The old alignment character.
	 */
	public Character setChar(Character ch);
	
	/**
	 * Get the position of the alignment character.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "charoff".
	 * 
	 * @return The position of the alignment character or <code>null</code> if
	 *         the attribute is not specified.
	 */
	public Integer getCharoff();
	
	/**
	 * Set the position of the alignment character.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "charoff".
	 * 
	 * @param charoff
	 *            The new position or <code>null</code> to remove the attribute.
	 * @return The old position.
	 */
	public Integer setCharoff(Integer charoff);
}
