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
 * Denotes a table.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "table".
 * 
 * <b>Child elements:</b> ([Preprocessor elements]|caption)? ([Preprocessor
 * elements]|tbody)?
 */
public interface WomTable
        extends
            WomBlockElement,
            WomUniversalAttributes
{
	/**
	 * Get the caption of the table.
	 * 
	 * Operates on the first &lt;caption> element found among the table's
	 * children.
	 * 
	 * @return The caption of the table.
	 */
	public WomTableCaption getCaption();
	
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
	public WomTableCaption setCaption(WomTableCaption caption);
	
	/**
	 * Get the body of the table.
	 * 
	 * Operates on the first &lt;tbody> element found among the table's
	 * children.
	 * 
	 * @return The body of the table.
	 */
	public WomTableBody getBody();
	
	/**
	 * Set the body of the table.
	 * 
	 * Operates on the first &lt;tbody> element found among the table's
	 * children. If no body is found, the body will be added as the first child
	 * of the table.
	 * 
	 * @param body
	 *            The new body of the table.
	 * @return The old body of the table.
	 */
	public WomTableBody setBody(WomTableBody body);
	
	// ==[ The XHTML Attributes ]===============================================
	
	/**
	 * Get the alignment of the table.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @return The alignment of the table or <code>null</code> if the attribute
	 *         is not specified.
	 */
	public WomHorizAlign getAlign();
	
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
	public WomHorizAlign setAlign(WomHorizAlign align);
	
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
	public int setBorder(int thickness);
	
	/**
	 * Get the background color of the table.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "bgcolor".
	 * 
	 * @return The background color of the table or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public WomColor getBgColor();
	
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
	public WomColor setBgColor(WomColor color);
	
	/**
	 * Get the spacing between cell wall and cell content.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cellpadding".
	 * 
	 * @return The spacing between cell wall and content in pixels or
	 *         <code>null</code> if the attribute is not specified.
	 */
	public int getCellPadding();
	
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
	public int setCellPadding(int padding);
	
	/**
	 * Get the space between cells.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cellspacing".
	 * 
	 * @return The space between cells in pixels or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public int getCellSpacing();
	
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
	public int setCellSpacing(int spacing);
	
	/**
	 * Get the outer border parts that will be rendered.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "frame".
	 * 
	 * @return The outer border parts that will be rendered or <code>null</code>
	 *         if the attribute is not specified.
	 */
	public WomTableFrame getFrame();
	
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
	public WomTableFrame setFrame(WomTableFrame frame);
	
	/**
	 * Get the inner border parts that will be rendered.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "rules".
	 * 
	 * @return The inner border parts that will be rendered or <code>null</code>
	 *         if the attribute is not specified.
	 */
	public WomTableRules getRules();
	
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
	public WomTableRules setRules(WomTableRules rules);
	
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
	public WomValueWithUnit getWidth();
	
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
	public WomValueWithUnit setWidth(WomValueWithUnit width);
}
