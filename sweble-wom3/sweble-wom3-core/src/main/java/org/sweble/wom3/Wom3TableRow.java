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
 * A table row.
 * 
 * Cells are accessed via integer indices. <b>Only valid items are counted.</b>
 * If a table row is given in HTML that contains invalid content (e.g.: text or
 * elements other than <code>&lt;th></code> or <code>&lt;td></code>), these
 * elements are skipped in the enumeration and are not accessible through this
 * interface. However, they can be iterated using the methods provided by the
 * WomNode interface.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "tr".
 * 
 * <b>Child elements:</b> ([Preprocessor elements]|th|td)*
 */
public interface Wom3TableRow
		extends
			Wom3ElementNode,
			Wom3UniversalAttributes
{
	/**
	 * Get the zero-based index of this row.
	 * 
	 * @return The zero-based index of this row.
	 */
	public int getRowIndex();

	/**
	 * Get the number of cells (including colspan calculations) in this row.
	 * 
	 * @return The number of cells.
	 */
	public int getNumCols();

	/**
	 * Get a cell from this row.
	 * 
	 * @param col
	 *            The column in which the cell is located. If the addressed cell
	 *            doesn't exist in itself but is part of a spanning cell then
	 *            the spanning cell will be returned instead.
	 * @return The requested cell.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>col &lt; 0</code> or
	 *             <code>col >= getNumCols()</code>.
	 */
	public Wom3TableCellBase getCell(int col) throws IndexOutOfBoundsException;

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
	 *            The new horizontal alignment.
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
	 *            The new vertical alignment.
	 * @return The old vertical alignment.
	 */
	public Wom3TableVAlign setVAlign(Wom3TableVAlign valign);

	/**
	 * Get background color of the row.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "bgcolor".
	 * 
	 * @return The background color or <code>null</code> if the attribute is not
	 *         specified.
	 */
	public Wom3Color getBgColor();

	/**
	 * Set the background color of the row.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "bgcolor".
	 * 
	 * @param color
	 *            The new background color.
	 * @return The old background color.
	 */
	public Wom3Color setBgColor(Wom3Color color);
}
