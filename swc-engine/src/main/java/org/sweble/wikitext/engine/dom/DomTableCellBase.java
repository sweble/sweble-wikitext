package org.sweble.wikitext.engine.dom;

/**
 * Interface exposing attributes common to table cells and table heder cells.
 */
public interface DomTableCellBase
        extends
            DomNode,
            DomUniversalAttributes
{
	/**
	 * Get an abbreviation of the cell's content.
	 * 
	 * @return The abbreviation.
	 */
	public String getAbbr();
	
	/**
	 * Set an abbreviation of the cell's content.
	 * 
	 * @param abbr
	 *            The new abbreviation.
	 * @return The old abbreviation.
	 */
	public String setAbbr(String abbr);
	
	/**
	 * Get categories assigned to this cell.
	 * 
	 * @return The categories.
	 */
	public String getAxis();
	
	/**
	 * Assign categories to this cell.
	 * 
	 * @param axis
	 *            The new categories.
	 * @return The old categories.
	 */
	public String setAxis(String axis);
	
	/**
	 * Get the scope of this cell.
	 * 
	 * @return The scope of this cell.
	 */
	public DomCellScope getScope();
	
	/**
	 * Set the scope of this cell.
	 * 
	 * @param scope
	 *            The new scope.
	 * @return The old scope.
	 */
	public DomCellScope setScope(DomCellScope scope);
	
	/**
	 * Get the horizontal alignment of the cell's content.
	 * 
	 * @return The horizontal alignment.
	 */
	public DomAlign getAlign();
	
	/**
	 * Set the horizontal alignment of the cell's content.
	 * 
	 * @param align
	 *            The new horizontal alignment.
	 * @return The old horizontal alignment.
	 */
	public DomAlign setAlign(DomAlign align);
	
	/**
	 * Get the vertical alignment of the cell's content.
	 * 
	 * @return The vertical alignment.
	 */
	public DomTableVAlign getVAlign();
	
	/**
	 * Set the vertical alignment of the cell's content.
	 * 
	 * @param valign
	 *            The new vertical alignment.
	 * @return The old vertical alignment.
	 */
	public DomTableVAlign setTableVAlign(DomAlign valign);
	
	/**
	 * Get background color of the cell.
	 * 
	 * @return The background color.
	 */
	public DomColor getBgColor();
	
	/**
	 * Set the background color of the cell.
	 * 
	 * @param color
	 *            The new background color.
	 * @return The old background color.
	 */
	public DomColor setBgColor(DomColor color);
	
	/**
	 * Get the cell's alignment character.
	 * 
	 * @return The alignment character.
	 */
	public char getChar();
	
	/**
	 * Set the cell's alignment character.
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
	
	/**
	 * Get number of columns this cell spans.
	 * 
	 * @return The number of columns this cell spans.
	 */
	public int getColspan();
	
	/**
	 * Set the number of columns this cell spans.
	 * 
	 * @param span
	 *            The new number of columns.
	 * @return The old number of columns.
	 */
	public int setColspan(int span);
	
	/**
	 * Get the number of rows this cell spans.
	 * 
	 * @return The number of rows this cell spans.
	 */
	public int getRowspan();
	
	/**
	 * Set the number of rows this cell spans.
	 * 
	 * @param span
	 *            The new number of rows.
	 * @return The old number of rows.
	 */
	public int setRowspan(int span);
	
	/**
	 * Tell whether content inside a cell should not wrap.
	 * 
	 * @return <code>True</code> if the cell's content should not wrap,
	 *         <code>false</code> otherwise.
	 */
	public boolean isNowrap();
	
	/**
	 * Set whether the content inside a cell should not wrap.
	 * 
	 * @param nowrap
	 *            The new setting.
	 * @return The old setting.
	 */
	public boolean setNowrap(boolean nowrap);
	
	/**
	 * Get the width of the cell.
	 * 
	 * @return The width of the cell in pixels or percent.
	 */
	public DomValueWithUnit getWidth();
	
	/**
	 * Set the cell's width.
	 * 
	 * @param width
	 *            The new width of the cell.
	 * @return The old width of the cell.
	 */
	public DomValueWithUnit setWidth(DomValueWithUnit width);
	
	/**
	 * Get the height of the cell.
	 * 
	 * @return The height of the cell in pixels or percent.
	 */
	public DomValueWithUnit getHeight();
	
	/**
	 * Set the height of the cell.
	 * 
	 * @param height
	 *            The new height of the cell.
	 * @return The old height of the cell.
	 */
	public DomValueWithUnit setHeight(DomValueWithUnit height);
}
