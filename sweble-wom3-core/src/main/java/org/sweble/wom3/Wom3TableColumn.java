/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
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
