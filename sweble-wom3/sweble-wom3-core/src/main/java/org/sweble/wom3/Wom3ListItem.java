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
