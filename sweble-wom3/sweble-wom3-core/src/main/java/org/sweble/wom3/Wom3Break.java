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
 * Denotes a single line break.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "br".
 * 
 * <b>Child elements:</b> -
 */
public interface Wom3Break
		extends
			Wom3ElementNode,
			Wom3CoreAttributes
{
	/**
	 * Get the sides on which floating elements are not allowed.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "clear".
	 * 
	 * @return The sides on which floating elements are not allowed.
	 */
	public Wom3Clear getClear();

	/**
	 * Set the sides on which floating elements are not allowed.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "clear".
	 * 
	 * @param clear
	 *            The new sides on which floating elements are not allowed or
	 *            <code>null</code> if the attribute is not specified.
	 * @return The old sides on which floating elements are not allowed.
	 */
	public Wom3Clear setClear(Wom3Clear clear);
}
