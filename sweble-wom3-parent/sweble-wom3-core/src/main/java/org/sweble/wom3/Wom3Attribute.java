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

import org.w3c.dom.Attr;

/**
 * An attribute node.
 * 
 * Objects of this class represent attributes that can be attached to other
 * nodes that support attributes. An attribute node can only have other
 * attribute nodes as siblings. An attribute node cannot have children or
 * attributes of its own.
 * 
 * <b>Child elements:</b> -
 */
public interface Wom3Attribute
		extends
			Wom3Node,
			Attr
{
	/**
	 * Retrieve the name of the attribute. Attribute names are case-insensitive.
	 * 
	 * @return The name of the attribute.
	 */
	public String getName();

	/**
	 * Set the name of the attribute. Attribute names are case-insensitive.
	 * 
	 * @param name
	 *            The new name of the attribute.
	 * @return The old name of the attribute.
	 * @throws IllegalArgumentException
	 *             If an attribute with the given name already exists or the
	 *             given name was empty or not a valid XML name.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> was specified as name.
	 */
	public String setName(String name)
			throws IllegalArgumentException,
			NullPointerException;

	/**
	 * Return the value of the attribute.
	 * 
	 * @return The value of the attribute.
	 */
	@Override
	public String getValue();

	/**
	 * Set the value of the attribute.
	 * 
	 * @param value
	 *            The new value of the attribute.
	 * @return The old value of the attribute.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> was specified as value.
	 */
	@Override
	public void setValue(String value) throws NullPointerException;
}
