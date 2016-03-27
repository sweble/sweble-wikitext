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
 * A category statement.
 * 
 * Corresponds to the XWML 1.0 element "category".
 * 
 * <b>Child elements:</b> -
 */
public interface Wom3Category
		extends
			Wom3ElementNode,
			Wom3Link
{
	/**
	 * Return the name of the category.
	 * 
	 * Corresponds to the XWML 1.0 attribute "name".
	 * 
	 * @return The name of the category without the category namespace
	 *         specifier.
	 */
	public String getName();

	/**
	 * Set the name of the category.
	 * 
	 * Corresponds to the XWML 1.0 attribute "name".
	 * 
	 * @param name
	 *            The new name of the category without the category namespace
	 *            specifier.
	 * @return The old name of the category.
	 * @throws NullPointerException
	 *             Thrown if the given category is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             Thrown if another category with the given name is already
	 *             associated with this page.
	 */
	public String setName(String name) throws NullPointerException, IllegalArgumentException;

	// ==[ Link interface ]=====================================================

	/**
	 * Returns an empty title since category statements do not provide a title.
	 * 
	 * @return An empty title.
	 */
	@Override
	public Wom3Title getLinkTitle();

	/**
	 * Return the name of the category this statement is pointing to.
	 * 
	 * @return The name of the category including the category namespace
	 *         specifier.
	 */
	@Override
	public String getLinkTarget();
}
