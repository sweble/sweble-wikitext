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
 * A table column.
 * 
 * This is an auxiliary interface that has no representation in XWML but is
 * provides easier handling of table column.
 * 
 * <b>Child elements:</b> -
 */
public interface WomTableColumn
{
	/**
	 * Get the zero-based index of this column.
	 * 
	 * @return The zero-based index of this column.
	 */
	public int getColIndex();
	
	/**
	 * Get the number of cells (including rowspan calculations) in this column.
	 * 
	 * @return The number of cells.
	 */
	public int getNumRows();
	
	/**
	 * Get a cell from this column.
	 * 
	 * @param row
	 *            The row in which the cell is located. If the addressed cell
	 *            doesn't exist in itself but is part of a spanning cell then
	 *            the spanning cell will be returned instead.
	 * @return The requested cell.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>row &lt; 0</code> or
	 *             <code>row >= getNumCols()</code>.
	 */
	public WomTableCellBase getCell(int row) throws IndexOutOfBoundsException;
	
	/**
	 * Replace a cell with another cell.
	 * 
	 * If a spanning cell is replaced with a cell that spans fewer cells, the
	 * remaining cells not covered by the replacement cell are filled with new,
	 * empty cells. If a cell is replaced by a cell that spans more cells, the
	 * cells covered by the replacement cell will be removed.
	 * 
	 * @param row
	 *            The zero-based index of the cell to replace.
	 * @param replace
	 *            The replacement cell.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>before &lt; 0</code> or
	 *             <code>row >= getNumRows()</code>.
	 * @throws IllegalArgumentException
	 *             Thrown if the replacement cell is spanning beyond the table's
	 *             dimensions.
	 */
	public void replaceCell(int row, WomTableCellBase replace) throws IndexOutOfBoundsException, IllegalArgumentException;
	
	/**
	 * Replace a cell with another cell.
	 * 
	 * If a spanning cell is replaced with a cell that spans fewer cells, the
	 * remaining cells not covered by the replacement cell are filled with new,
	 * empty cells. If a cell is replaced by a cell that spans more cells, the
	 * cells covered by the replacement cell will be removed.
	 * 
	 * @param search
	 *            The cell to replace.
	 * @param replace
	 *            The replacement cell.
	 * @throws IllegalArgumentException
	 *             Thrown if the given cell <code>search</code> is not a cell of
	 *             this column.
	 * @throws IllegalArgumentException
	 *             Thrown if the replacement cell is spanning beyond the table's
	 *             dimensions.
	 */
	public void replaceCell(WomTableCellBase search, WomTableCellBase replace) throws IllegalArgumentException;
}
