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
