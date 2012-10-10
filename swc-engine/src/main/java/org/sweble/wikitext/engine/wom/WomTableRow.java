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
public interface WomTableRow
		extends
			WomNode,
			WomUniversalAttributes
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
	public WomTableCellBase getCell(int col) throws IndexOutOfBoundsException;
	
	/**
	 * Replace a cell with another cell.
	 * 
	 * If a spanning cell is replaced with a cell that spans fewer cells, the
	 * remaining cells not covered by the replacement cell are filled with new,
	 * empty cells. If a cell is replaced by a cell that spans more cells, the
	 * cells covered by the replacement cell will be removed.
	 * 
	 * @param col
	 *            The zero-based index of the cell to replace.
	 * @param replace
	 *            The replacement cell.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>col &lt; 0</code> or
	 *             <code>before >= getNumCols()</code>.
	 * @throws IllegalArgumentException
	 *             Thrown if the replacement cell is spanning beyond the table's
	 *             dimensions.
	 */
	public void replaceCell(int col, WomTableCellBase replace) throws IndexOutOfBoundsException, IllegalArgumentException;
	
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
	 *             this row.
	 * @throws IllegalArgumentException
	 *             Thrown if the replacement cell is spanning beyond the table's
	 *             dimensions.
	 */
	public void replaceCell(WomTableCellBase search, WomTableCellBase replace) throws IllegalArgumentException;
	
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
	 *            The new horizontal alignment.
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
	 *            The new vertical alignment.
	 * @return The old vertical alignment.
	 */
	public WomTableVAlign setTableVAlign(WomHorizAlign valign);
	
	/**
	 * Get background color of the row.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "bgcolor".
	 * 
	 * @return The background color or <code>null</code> if the attribute is not
	 *         specified.
	 */
	public WomColor getBgColor();
	
	/**
	 * Set the background color of the row.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "bgcolor".
	 * 
	 * @param color
	 *            The new background color.
	 * @return The old background color.
	 */
	public WomColor setBgColor(WomColor color);
	
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
	 *            The new alignment character.
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
	 *            The new position.
	 * @return The old position.
	 */
	public Integer setCharoff(Integer charoff);
}
