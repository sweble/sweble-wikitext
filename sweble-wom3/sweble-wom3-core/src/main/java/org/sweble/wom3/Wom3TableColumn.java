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
 * A table column.
 * 
 * This is an auxiliary interface that has no representation in XWML but is
 * provides easier handling of table column.
 * 
 * <b>Child elements:</b> -
 */
public interface Wom3TableColumn
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
	public Wom3TableCellBase getCell(int row) throws IndexOutOfBoundsException;
}
