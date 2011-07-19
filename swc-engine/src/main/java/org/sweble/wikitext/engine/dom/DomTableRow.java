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

public interface DomTableRow
        extends
            DomNode,
            DomUniversalAttributes
{
	/**
	 * Get the number of cells (including colspan calculations) in this row.
	 * 
	 * @return The number of cells.
	 */
	public int getNumCols();
	
	/**
	 * Append cell to the right of the row.
	 * 
	 * @param cell
	 *            The cell to append.
	 */
	public void appendCell(DomTableCellBase cell);
	
	/**
	 * Insert a cell in front of another cell.
	 * 
	 * @param before
	 *            The index of the cell in front of which the new cell should be
	 *            inserted.
	 * @param cell
	 *            The cell to insert.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>before &lt; 0</code> or
	 *             <code>before > getNumCols()</code>.
	 */
	public void insertCell(int before, DomTableCellBase cell) throws IndexOutOfBoundsException;
	
	/**
	 * Insert a cell in front of another cell.
	 * 
	 * @param before
	 *            The cell in front of which the new cell should be inserted.
	 * @param cell
	 *            The cell to insert.
	 * @throws IllegalArgumentException
	 *             Thrown if the given cell <code>before</code> is not a cell of
	 *             this row.
	 */
	public void insertCell(DomTableCellBase before, DomTableCellBase cell) throws IllegalArgumentException;
	
	/**
	 * Replace a cell with another cell.
	 * 
	 * @param col
	 *            The zero-based index of the cell to replace.
	 * @param replace
	 *            The replacement cell.
	 * @return The replaced cell.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>before &lt; 0</code> or
	 *             <code>before >= getNumCols()</code>.
	 */
	public DomTableCellBase replaceCell(int col, DomTableCellBase replace) throws IndexOutOfBoundsException;
	
	/**
	 * Replace a cell with another cell.
	 * 
	 * @param search
	 *            The cell to replace.
	 * @param replace
	 *            The replacement cell.
	 * @throws IllegalArgumentException
	 *             Thrown if the given cell <code>before</code> is not a cell of
	 *             this row.
	 */
	public void replaceCell(DomTableCellBase search, DomTableCellBase replace) throws IllegalArgumentException;
	
	/**
	 * Remoave a cell.
	 * 
	 * @param col
	 *            The zero-based index of the cell to remove.
	 * @return The removed cell.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>before &lt; 0</code> or
	 *             <code>before >= getNumCols()</code>.
	 */
	public DomTableCellBase removeCell(int col) throws IndexOutOfBoundsException;
	
	/**
	 * Remove a cell.
	 * 
	 * @param cell
	 *            The cell to remove.
	 * @throws IllegalArgumentException
	 *             Thrown if the given cell <code>before</code> is not a cell of
	 *             this row.
	 */
	public void removeCell(DomTableCellBase cell) throws IllegalArgumentException;
	
	// ==[ The XHTML Attributes ]===============================================
	
	/**
	 * Get the horizontal alignment of the row's content.
	 * 
	 * @return The horizontal alignment.
	 */
	public DomAlign getAlign();
	
	/**
	 * Set the horizontal alignment of the row's content.
	 * 
	 * @param align
	 *            The new horizontal alignment.
	 * @return The old horizontal alignment.
	 */
	public DomAlign setAlign(DomAlign align);
	
	/**
	 * Get the vertical alignment of the row's content.
	 * 
	 * @return The vertical alignment.
	 */
	public DomTableVAlign getVAlign();
	
	/**
	 * Set the vertical alignment of the row's content.
	 * 
	 * @param valign
	 *            The new vertical alignment.
	 * @return The old vertical alignment.
	 */
	public DomTableVAlign setTableVAlign(DomAlign valign);
	
	/**
	 * Get background color of the row.
	 * 
	 * @return The background color.
	 */
	public DomColor getBgColor();
	
	/**
	 * Set the background color of the row.
	 * 
	 * @param color
	 *            The new background color.
	 * @return The old background color.
	 */
	public DomColor setBgColor(DomColor color);
	
	/**
	 * Get the row's alignment character.
	 * 
	 * @return The alignment character.
	 */
	public char getChar();
	
	/**
	 * Set the row's alignment character.
	 * 
	 * @param ch
	 *            The new alignment character.
	 * @return The old alignment character.
	 */
	public char setChar(char ch);
	
	/**
	 * Get the position of the alignment character.
	 * 
	 * @return The position of the alignment character.
	 */
	public int getCharoff();
	
	/**
	 * Set the position of the alignment character.
	 * 
	 * @param charoff
	 *            The new position.
	 * @return The old position.
	 */
	public int setCharoff(int charoff);
}
