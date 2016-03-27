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

public interface Wom3Pre
		extends
			Wom3ElementNode,
			Wom3UniversalAttributes
{
	/**
	 * Get the number of characters per line.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "width".
	 * 
	 * @return The number of characters per line or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public Integer getWidth();

	/**
	 * Set the number of characters per line.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "width".
	 * 
	 * @param width
	 *            The new number of characters per line or <code>null</code> to
	 *            remove the attribute.
	 * @return The old number of characters per line.
	 */
	public Integer setWidth(Integer width);
}
