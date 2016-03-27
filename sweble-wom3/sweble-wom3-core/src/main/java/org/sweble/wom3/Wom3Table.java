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

import java.util.Collection;

/**
 * Denotes a table.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "table".
 * 
 * <b>Child elements:</b> ([Preprocessor elements]|caption)? ([Preprocessor
 * elements]|tbody)?
 */
public interface Wom3Table
		extends
			Wom3ElementNode,
			Wom3UniversalAttributes
{
	/**
	 * Get the caption of the table.
	 * 
	 * Operates on the first &lt;caption> element found among the table's
	 * children.
	 * 
	 * @return The caption of the table.
	 */
	public Wom3TableCaption getCaption();

	/**
	 * Set the caption of the table.
	 * 
	 * Operates on the first &lt;caption> element found among the table's
	 * children. If no caption is found, the caption will be added as the first
	 * child of the table.
	 * 
	 * @param caption
	 *            The new caption of the table.
	 * @return The old caption of the table.
	 */
	public Wom3TableCaption setCaption(Wom3TableCaption caption);

	//	/**
	//	 * Get the body of the table.
	//	 * 
	//	 * Operates on the first &lt;tbody> element found among the table's
	//	 * children.
	//	 * 
	//	 * @return The body of the table.
	//	 */
	//	public Wom3TableBody getBody();
	//	
	//	/**
	//	 * Set the body of the table.
	//	 * 
	//	 * Operates on the first &lt;tbody> element found among the table's
	//	 * children. If no body is found, the body will be added as the first child
	//	 * of the table.
	//	 * 
	//	 * @param body
	//	 *            The new body of the table.
	//	 * @return The old body of the table.
	//	 */
	//	public Wom3TableBody setBody(Wom3TableBody body);

	public Collection<Wom3TablePartition> getPartitions();

	// ==[ The XHTML Attributes ]===============================================

	/**
	 * Get the alignment of the table.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @return The alignment of the table or <code>null</code> if the attribute
	 *         is not specified.
	 */
	public Wom3HorizAlign getAlign();

	/**
	 * Set the alignment of the table.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @param align
	 *            The alignment or <code>null</code> to remove the attribute.
	 *            Only the values <code>left</code>, <code>center</code> and
	 *            <code>right</code> are allowed.
	 * @return The old alignment of the table.
	 */
	public Wom3HorizAlign setAlign(Wom3HorizAlign align);

	/**
	 * Get the thickness of the table border.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "border".
	 * 
	 * @return The thickness of the table border in pixels or <code>null</code>
	 *         if the attribute is not given.
	 */
	public Integer getBorder();

	/**
	 * The the thickness of the table border.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "border".
	 * 
	 * @param thickness
	 *            the new thickness of the table border in pixels or
	 *            <code>null</code> to remove the attribute.
	 * @return The old thickness in pixels.
	 */
	public Integer setBorder(Integer thickness);

	/**
	 * Get the background color of the table.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "bgcolor".
	 * 
	 * @return The background color of the table or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public Wom3Color getBgColor();

	/**
	 * Set the background color of the table.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "bgcolor".
	 * 
	 * @param color
	 *            The new background color of the table or <code>null</code> to
	 *            remove the attribute.
	 * @return The old background color of the table.
	 */
	public Wom3Color setBgColor(Wom3Color color);

	/**
	 * Get the spacing between cell wall and cell content.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cellpadding".
	 * 
	 * @return The spacing between cell wall and content in pixels or
	 *         <code>null</code> if the attribute is not specified.
	 */
	public Wom3ValueWithUnit getCellPadding();

	/**
	 * Set the spacing between cell wall and cell content.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cellpadding".
	 * 
	 * @param padding
	 *            The new spacing between cell wall and content in pixels or
	 *            <code>null</code> to remove the attribute.
	 * @return The old spacing between cell wall and content in pixels.
	 */
	public Wom3ValueWithUnit setCellPadding(Wom3ValueWithUnit padding);

	/**
	 * Get the space between cells.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cellspacing".
	 * 
	 * @return The space between cells in pixels or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public Wom3ValueWithUnit getCellSpacing();

	/**
	 * Set the space between cells.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cellspacing".
	 * 
	 * @param spacing
	 *            The new space between cells in pixels or <code>null</code> to
	 *            remove the attribute.
	 * @return The old space between cells in pixels.
	 */
	public Wom3ValueWithUnit setCellSpacing(Wom3ValueWithUnit spacing);

	/**
	 * Get the outer border parts that will be rendered.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "frame".
	 * 
	 * @return The outer border parts that will be rendered or <code>null</code>
	 *         if the attribute is not specified.
	 */
	public Wom3TableFrame getFrame();

	/**
	 * Set the outer border parts to be rendered.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "frame".
	 * 
	 * @param frame
	 *            The new outer border parts to render or <code>null</code> to
	 *            remove the attribute.
	 * @return The old setting.
	 */
	public Wom3TableFrame setFrame(Wom3TableFrame frame);

	/**
	 * Get the inner border parts that will be rendered.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "rules".
	 * 
	 * @return The inner border parts that will be rendered or <code>null</code>
	 *         if the attribute is not specified.
	 */
	public Wom3TableRules getRules();

	/**
	 * Set the inner border parts to be rendered.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "rules".
	 * 
	 * @param rules
	 *            The new inner border parts to render or <code>null</code> to
	 *            remove the attribute.
	 * @return The old setting.
	 */
	public Wom3TableRules setRules(Wom3TableRules rules);

	/**
	 * Get a textual summary of the table's content.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "summary".
	 * 
	 * @return A summary or <code>null</code> if the attribute is not specified.
	 */
	public String getSummary();

	/**
	 * Set a textual summary of the table's content.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "summary".
	 * 
	 * @param summary
	 *            The new summary or <code>null</code> to remove the attribute.
	 * @return The old summary.
	 */
	public String setSummary(String summary);

	/**
	 * Get the table width.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "width".
	 * 
	 * @return The table width in pixels or percent or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public Wom3ValueWithUnit getWidth();

	/**
	 * Set the table width.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "width".
	 * 
	 * @param width
	 *            The new table width in pixels or percent or <code>null</code>
	 *            to remove the attribute.
	 * @return The old setting.
	 */
	public Wom3ValueWithUnit setWidth(Wom3ValueWithUnit width);
}
