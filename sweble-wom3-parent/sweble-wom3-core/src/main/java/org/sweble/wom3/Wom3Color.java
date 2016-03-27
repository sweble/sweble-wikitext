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

import java.io.Serializable;

/**
 * A color value.
 */
public interface Wom3Color
		extends
			Cloneable,
			Serializable
{
	/**
	 * Get the red color component.
	 * 
	 * @return A value between 0 and 255.
	 */
	public int getRed();

	/**
	 * Get the green color component.
	 * 
	 * @return A value between 0 and 255.
	 */
	public int getGreen();

	/**
	 * Get the blue color component.
	 * 
	 * @return A value between 0 and 255.
	 */
	public int getBlue();

	/**
	 * Format color as #RRGGBB.
	 */
	@Override
	public String toString();
}
