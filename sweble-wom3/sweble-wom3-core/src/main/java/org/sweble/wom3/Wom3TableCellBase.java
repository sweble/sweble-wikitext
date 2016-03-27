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
 * Interface exposing attributes common to table cells and table header cells.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "th" or "td".
 * 
 * <b>Child elements:</b> [Block elements]*
 */
public interface Wom3TableCellBase
		extends
			Wom3ElementNode
{
	/**
	 * Get the zero-based index of the row in which this cell is located.
	 * 
	 * @return The zero-based index of the row in which this cell is located.
	 */
	public int getRowIndex();

	/**
	 * Get the zero-based index of the row in which this cell is located.
	 * 
	 * @return The zero-based index of the row in which this cell is located.
	 */
	public int getColIndex();

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
	public Wom3TableCellScope getScope();

	/**
	 * Set the scope of this cell.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "scope".
	 * 
	 * @param scope
	 *            The new scope or <code>null</code> to remove the attribute.
	 * @return The old scope.
	 */
	public Wom3TableCellScope setScope(Wom3TableCellScope scope);

	/**
	 * Get the horizontal alignment of the cell's content.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @return The horizontal alignment or <code>null</code> if the attribute is
	 *         not specified.
	 */
	public Wom3HorizAlign getAlign();

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
	public Wom3HorizAlign setAlign(Wom3HorizAlign align);

	/**
	 * Get the vertical alignment of the cell's content.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "valign".
	 * 
	 * @return The vertical alignment or <code>null</code> if the attribute is
	 *         not specified.
	 */
	public Wom3TableVAlign getVAlign();

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
	public Wom3TableVAlign setVAlign(Wom3TableVAlign valign);

	/**
	 * Get background color of the cell.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "bgcolor".
	 * 
	 * @return The background color or <code>null</code> if the attribute is not
	 *         specified.
	 */
	public Wom3Color getBgColor();

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
	public Wom3Color setBgColor(Wom3Color color);

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
	public Wom3ValueWithUnit getWidth();

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
	public Wom3ValueWithUnit setWidth(Wom3ValueWithUnit width);

	/**
	 * Get the height of the cell.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "height".
	 * 
	 * @return The height of the cell in pixels or percent or <code>null</code>
	 *         if the attribute is not specified.
	 */
	public Wom3ValueWithUnit getHeight();

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
	public Wom3ValueWithUnit setHeight(Wom3ValueWithUnit height);
}
