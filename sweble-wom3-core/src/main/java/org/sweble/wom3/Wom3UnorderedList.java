/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

/**
 * Denotes an unordered list.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "ul".
 * 
 * See WomList for details.
 */
public interface Wom3UnorderedList
		extends
			Wom3List
{
	/**
	 * Get the type of bullet point the list items use.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "type".
	 * 
	 * @return The type of bullet point or <code>null</code> if the attribute is
	 *         not specified.
	 */
	public Wom3BulletStyle getItemType();
	
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
	public Wom3BulletStyle setItemType(Wom3BulletStyle type);
}
