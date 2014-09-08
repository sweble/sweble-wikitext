/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

/**
 * Denotes an ordered list.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "ol".
 * 
 * See WomList for details.
 */
public interface Wom3OrderedList
		extends
			Wom3List
{
	/**
	 * Get the number of the first item in the list.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "start".
	 * 
	 * @return The number of the first item or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public Integer getStart();
	
	/**
	 * Set the number of the first list item.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "start".
	 * 
	 * @param start
	 *            The new number of the first list item or <code>null</code> to
	 *            remove the attribute.
	 * @return The old number of the first list item.
	 */
	public Integer setStart(Integer start);
	
	/**
	 * Get the type of bullet point the list items use.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "type".
	 * 
	 * @return The type of bullet point or <code>null</code> if the attribute is
	 *         not specified.
	 */
	public String getItemType();
	
	/**
	 * Set the type of bullet point the list items should use.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "type".
	 * 
	 * @param type
	 *            The new type of bullet point or <code>null</code> to remove
	 *            the attribute.
	 * @return The old type of bullet point.
	 */
	public String setItemType(String type);
}
