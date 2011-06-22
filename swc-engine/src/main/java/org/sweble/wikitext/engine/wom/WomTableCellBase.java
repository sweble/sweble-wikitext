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
 * Interface exposing attributes common to table cells and table header cells.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "th" or "td".
 * 
 * <b>Child elements:</b> [Block elements]*
 */
public interface WomTableCellBase
        extends
            WomNode
{
	/**
	 * Get the zero-based index of the row in which this cell is located.
	 * 
	 * @return The zero-based index of the row in which this cell is located.
	 */
	public int getRow();
	
	/**
	 * Get the zero-based index of the row in which this cell is located.
	 * 
	 * @return The zero-based index of the row in which this cell is located.
	 */
	public int getCol();
	
	// ==[ The XHTML Attributes ]===============================================
	
	/**
	 * Get an abbreviation of the cell's content.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "abbr".
	 * 
	 * @return The abbreviation or <code>null</code> if the attribute is not
	 *         specified.
	 */
	public String getAbbr();
	
	/**
	 * Set an abbreviation of the cell's content.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "abbr".
	 * 
	 * @param abbr
	 *            The new abbreviation or <code>null</code> to remove the
	 *            attribute.
	 * @return The old abbreviation.
	 */
	public String setAbbr(String abbr);
	
	/**
	 * Get categories assigned to this cell.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "axis".
	 * 
	 * @return The categories or <code>null</code> if the attribute is not
	 *         specified.
	 */
	public String getAxis();
	
	/**
	 * Assign categories to this cell.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "axis".
	 * 
	 * @param axis
	 *            The new categories or <code>null</code> to remove the
	 *            attribute.
	 * @return The old categories.
	 */
	public String setAxis(String axis);
	
	/**
	 * Get the scope of this cell.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "scope".
	 * 
	 * @return The scope of this cell or <code>null</code> if the attribute is
	 *         not specified.
	 */
	public WomTableCellScope getScope();
	
	/**
	 * Set the scope of this cell.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "scope".
	 * 
	 * @param scope
	 *            The new scope or <code>null</code> to remove the attribute.
	 * @return The old scope.
	 */
	public WomTableCellScope setScope(WomTableCellScope scope);
	
	/**
	 * Get the horizontal alignment of the cell's content.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @return The horizontal alignment or <code>null</code> if the attribute is
	 *         not specified.
	 */
	public WomHorizAlign getAlign();
	
	/**
	 * Set the horizontal alignment of the cell's content.
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
	 * Get the vertical alignment of the cell's content.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "valign".
	 * 
	 * @return The vertical alignment or <code>null</code> if the attribute is
	 *         not specified.
	 */
	public WomTableVAlign getVAlign();
	
	/**
	 * Set the vertical alignment of the cell's content.
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
	 * Get background color of the cell.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "bgcolor".
	 * 
	 * @return The background color or <code>null</code> if the attribute is not
	 *         specified.
	 */
	public WomColor getBgColor();
	
	/**
	 * Set the background color of the cell.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "bgcolor".
	 * 
	 * @param color
	 *            The new background color or <code>null</code> to remove the
	 *            attribute.
	 * @return The old background color.
	 */
	public WomColor setBgColor(WomColor color);
	
	/**
	 * Get the cell's alignment character.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "char".
	 * 
	 * @return The alignment character or <code>null</code> if the attribute is
	 *         not specified.
	 */
	public Character getChar();
	
	/**
	 * Set the cell's alignment character.
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
	
	/**
	 * Get number of columns this cell spans.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "colspan".
	 * 
	 * @return The number of columns this cell spans or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public Integer getColspan();
	
	/**
	 * Set the number of columns this cell spans.
	 * 
	 * If the cell covers other cells after the change the covered cells will be
	 * removed. If the cell covers less cells after the change new, empty cells
	 * will be created for the uncovered cells.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "colspan".
	 * 
	 * @param span
	 *            The new number of columns or <code>null</code> to remove the
	 *            attribute.
	 * @return The old number of columns.
	 * @throws IllegalArgumentException
	 *             Thrown if the cell is spanning beyond the table's dimensions.
	 */
	public Integer setColspan(Integer span) throws IllegalArgumentException;
	
	/**
	 * Get the number of rows this cell spans.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "rowspan".
	 * 
	 * @return The number of rows this cell spans or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public Integer getRowspan();
	
	/**
	 * Set the number of rows this cell spans.
	 * 
	 * If the cell covers other cells after the change the covered cells will be
	 * removed. If the cell covers less cells after the change new, empty cells
	 * will be created for the uncovered cells.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "rowspan".
	 * 
	 * @param span
	 *            The new number of rows or <code>null</code> to remove the
	 *            attribute.
	 * @return The old number of rows.
	 * @throws IllegalArgumentException
	 *             Thrown if the cell is spanning beyond the table's dimensions.
	 */
	public Integer setRowspan(Integer span) throws IllegalArgumentException;
	
	/**
	 * Tell whether content inside a cell should not wrap.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "nowrap".
	 * 
	 * @return <code>True</code> if the cell's content should not wrap,
	 *         <code>false</code> otherwise.
	 */
	public boolean isNowrap();
	
	/**
	 * Set whether the content inside a cell should not wrap.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "nowrap".
	 * 
	 * @param nowrap
	 *            The new setting.
	 * @return The old setting.
	 */
	public boolean setNowrap(boolean nowrap);
	
	/**
	 * Get the width of the cell.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "width".
	 * 
	 * @return The width of the cell in pixels or percent or <code>null</code>
	 *         if the attribute is not specified.
	 */
	public WomValueWithUnit getWidth();
	
	/**
	 * Set the cell's width.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "width".
	 * 
	 * @param width
	 *            The new width of the cell or <code>null</code> to remove the
	 *            attribute.
	 * @return The old width of the cell.
	 */
	public WomValueWithUnit setWidth(WomValueWithUnit width);
	
	/**
	 * Get the height of the cell.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "height".
	 * 
	 * @return The height of the cell in pixels or percent or <code>null</code>
	 *         if the attribute is not specified.
	 */
	public WomValueWithUnit getHeight();
	
	/**
	 * Set the height of the cell.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "height".
	 * 
	 * @param height
	 *            The new height of the cell or <code>null</code> to remove the
	 *            attribute.
	 * @return The old height of the cell.
	 */
	public WomValueWithUnit setHeight(WomValueWithUnit height);
}
