/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

/**
 * Denotes a list item.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "li".
 * 
 * <b>Child elements:</b> [Block elements]*
 */
public interface Wom3ListItem
		extends
			Wom3ElementNode,
			Wom3UniversalAttributes
{
	/**
	 * Get the type of bullet point the list item uses.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "type".
	 * 
	 * @return The type of bullet point or <code>null</code> if the attribute is
	 *         not specified.
	 */
	public String getItemType();
	
	/**
	 * Get the type of bullet point the list item uses.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "type".
	 * 
	 * @param type
	 *            The new type of bullet point or <code>null</code> to remove
	 *            the attribute.
	 * @return The old type of bullet point.
	 */
	public String setItemType(String type);
	
	/**
	 * Get the number of the list item.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "value".
	 * 
	 * @return The number of the list item or <code>null</code> if the attribute
	 *         is not specified.
	 */
	public Integer getItemValue();
	
	/**
	 * Set the number of the list item.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "value".
	 * 
	 * @param number
	 *            The new number of the list item or <code>null</code> to remove
	 *            the attribute.
	 * @return The old number of the list item.
	 */
	public Integer setItemValue(Integer number);
}
