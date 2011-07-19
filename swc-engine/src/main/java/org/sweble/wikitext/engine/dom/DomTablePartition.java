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
package org.sweble.wikitext.engine.dom;

import java.util.Collection;

import org.sweble.wikitext.lazy.parser.TableCell;

/**
 * The interface to access the cells, rows and columns of table head, body or
 * foot.
 */
public interface DomTablePartition
        extends
            DomNode
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
	public DomTableRow getRow(int row) throws IndexOutOfBoundsException;
	
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
	public DomTableCellBase getCell(int row, int col) throws IndexOutOfBoundsException;
	
	// ==[ Row modification ]===================================================
	
	/**
	 * Append a new row to the end of the table.
	 * 
	 * @param row
	 *            The row to append.
	 */
	public void appendRow(DomTableRow row);
	
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
	public void insertRow(int before, DomTableRow row) throws IndexOutOfBoundsException;
	
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
	public void insertRow(DomTableRow before, DomTableRow row) throws IllegalArgumentException;
	
	/**
	 * Replace a row with another row.
	 * 
	 * @param row
	 *            The index of the row to replace.
	 * @param replace
	 *            The replacement row.
	 * @return The replaced row.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>row &lt; 0</code> or
	 *             <code>row > getNumRows()</code>.
	 */
	public DomTableRow replaceRow(int row, DomTableRow replace) throws IndexOutOfBoundsException;
	
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
	public void replaceRow(DomTableRow search, DomTableRow replace) throws IllegalArgumentException;
	
	/**
	 * Remove a row from this table.
	 * 
	 * @param row
	 *            The index of the row to remove.
	 * @return The removed row.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>row &lt; 0</code> or
	 *             <code>row > getNumRows()</code>.
	 */
	public DomTableRow removeRow(int row) throws IndexOutOfBoundsException;
	
	/**
	 * Remove a row from this table.
	 * 
	 * @param row
	 *            The row to remove.
	 * @throws IllegalArgumentException
	 *             Thrown if <code>row</code> is not a row of this table.
	 */
	public void removeRow(DomTableRow row) throws IllegalArgumentException;
	
	// ==[ Column modification ]================================================
	
	/**
	 * Append a column to the right of the table.
	 * 
	 * @param column
	 *            A collection containing the cells of the new column. The
	 *            number of cells in the collection has to match the number of
	 *            rows in the table (rowspan included).
	 */
	public void appendColumn(Collection<TableCell> column);
	
	/**
	 * Insert a column into the table.
	 * 
	 * @param before
	 *            The index of the column in front of which the new column will
	 *            be inserted.
	 * @param column
	 *            A collection containing the cells of the new column. The
	 *            number of cells in the collection has to match the number of
	 *            rows in the table (rowspan included).
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>before &lt; 0</code> or
	 *             <code>before > getNumCols()</code>.
	 */
	public void insertColumn(int before, Collection<TableCell> column) throws IndexOutOfBoundsException;
	
	/**
	 * Replace a column with another column.
	 * 
	 * @param column
	 *            The index of the column to replace.
	 * @param replace
	 *            A collection containing the cells of the replacement column.
	 *            The number of cells in the collection has to match the number
	 *            of rows in the table (rowspan included).
	 * @return The cells of the replaced column.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>col &lt; 0</code> or
	 *             <code>col > getNumCols()</code>.
	 */
	public Collection<TableCell> replaceColumn(int col, Collection<TableCell> replace) throws IndexOutOfBoundsException;
	
	/**
	 * Remove a column.
	 * 
	 * @param col
	 *            The index of the column to remove.
	 * @return The cells of the removed column.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>col &lt; 0</code> or
	 *             <code>col > getNumCols()</code>.
	 */
	public Collection<TableCell> removeColumn(int col) throws IndexOutOfBoundsException;
}
