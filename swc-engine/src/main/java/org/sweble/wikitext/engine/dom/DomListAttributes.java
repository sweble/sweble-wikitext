package org.sweble.wikitext.engine.dom;

public interface DomListAttributes
        extends
            DomUniversalAttributes

{
	/**
	 * Get whether this list is displayed in a more compact way.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "compact".
	 * 
	 * @return <code>True</code> if the list is rendered in compact mode,
	 *         <code>false</code> otherwise.
	 */
	public boolean isCompact();
	
	/**
	 * Set whether this list should be displayed in a more compact way.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "compact".
	 * 
	 * @param compact
	 *            The new setting.
	 * @return The old setting.
	 */
	public boolean setCompact(boolean compact);
	
	/**
	 * Get the number of the first item in the list.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "start".
	 * 
	 * @return The number of the first item.
	 */
	public int getStart();
	
	/**
	 * Set the number of the first list item.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "start".
	 * 
	 * @param start
	 *            The new number of the first list item.
	 * @return The old number of the first list item.
	 */
	public int setStart(int start);
	
	/**
	 * Get the type of bullet point the list items use.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "type".
	 * 
	 * @return The type of bullet point.
	 */
	public String getItemType();
	
	/**
	 * Set the type of bullet point the list items should use.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "type".
	 * 
	 * @param type
	 *            The new type of bullet point.
	 * @return The old type of bullet point.
	 */
	public String setItemType(String type);
}
