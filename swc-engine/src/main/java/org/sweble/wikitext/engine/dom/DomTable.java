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

/**
 * Denotes a table.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "table".
 */
public interface DomTable
        extends
            DomBlockElement,
            DomUniversalAttributes
{
	/**
	 * Get the caption of the table.
	 * 
	 * @return The caption of the table.
	 */
	public DomTableCaption getCaption();
	
	///**
	// * Get the head of the table.
	// * 
	// * @return The head of the table.
	// */
	//public DomTableHead getHead();
	
	/**
	 * Get the body of the table.
	 * 
	 * @return The body of the table.
	 */
	public DomTableBody getBody();
	
	///**
	// * Get the foot of the table.
	// * 
	// * @return The foot of the table.
	// */
	//public DomTableFoot getFoot();
	
	/**
	 * Get the alignment of the table.
	 * 
	 * @return The alignment of the table.
	 */
	public DomAlign getAlign();
	
	/**
	 * Set the alignment of the table.
	 * 
	 * @param align
	 *            The alignment. Only the values <code>left</code>,
	 *            <code>center</code> and <code>right</code> are allowed.
	 * @return The old alignment of the table.
	 */
	public DomAlign setAlign(DomAlign align);
	
	/**
	 * Get the thickness of the table border.
	 * 
	 * @return The thickness of the table border in pixels.
	 */
	public int getBorder();
	
	/**
	 * The the thickness of the table border.
	 * 
	 * @param thickneess
	 *            the new thickness of the table border in pixels.
	 * @return The old thicksness in pixels.
	 */
	public int setBorder(int thickneess);
	
	/**
	 * Get the background color of the table.
	 * 
	 * @return The background color of the table.
	 */
	public DomColor getBgColor();
	
	/**
	 * Set the background color of the table.
	 * 
	 * @param color
	 *            The new background color of the table.
	 * @return The old background color of the table.
	 */
	public DomColor setBgColor(DomColor color);
	
	/**
	 * Get the spacing between cell wall and cell content.
	 * 
	 * @return The spacing between cell wall and content in pixels.
	 */
	public int getCellPadding();
	
	/**
	 * Set the spacing between cell wall and cell content.
	 * 
	 * @param padding
	 *            The new spacing between cell wall and content in pixels.
	 * @return The old spacing between cell wall and content in pixels.
	 */
	public int setCellPadding(int padding);
	
	/**
	 * Get the space between cells.
	 * 
	 * @return The space between cells in pixels.
	 */
	public int getCellSpacing();
	
	/**
	 * Set the space between cells.
	 * 
	 * @param spacing
	 *            The new space between cells in pixels.
	 * @return The old space between cells in pixels.
	 */
	public int setCellSpacing(int spacing);
	
	/**
	 * Get the outer border parts that will be rendered.
	 * 
	 * @return The outer border parts that will be rendered.
	 */
	public DomTableFrame getFrame();
	
	/**
	 * Set the outer border parts to be rendered.
	 * 
	 * @param frame
	 *            The new outer border parts to render.
	 * @return The old setting.
	 */
	public DomTableFrame setFrame(DomTableFrame frame);
	
	/**
	 * Get the inner border parts that will be rendered.
	 * 
	 * @return The inner border parts that will be rendered.
	 */
	public DomTableRules getRules();
	
	/**
	 * Set the inner border parts to be rendered.
	 * 
	 * @param rules
	 *            The new inner border parts to render.
	 * @return The old setting.
	 */
	public DomTableRules setRules(DomTableRules rules);
	
	/**
	 * Get a textual summary of the table's content.
	 * 
	 * @return A summary.
	 */
	public String getSummary();
	
	/**
	 * Set a textual summary of the table's content.
	 * 
	 * @param summary
	 *            The new summary.
	 * @return The old summary.
	 */
	public String setSummary(String summary);
	
	/**
	 * Get the table width.
	 * 
	 * @return The table width in pixels or percent.
	 */
	public DomValueWithUnit getWidth();
	
	/**
	 * Set the table width.
	 * 
	 * @param width
	 *            The new table width in pixels or percent.
	 * @return The old setting.
	 */
	public DomValueWithUnit setWidth(DomValueWithUnit width);
}
