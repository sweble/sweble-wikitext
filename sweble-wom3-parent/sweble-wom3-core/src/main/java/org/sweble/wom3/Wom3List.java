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

import java.util.Collection;

/**
 * Interface to lists like <code>&lt;ul></code> or <code>&lt;ol></code>.
 * 
 * List items are accessed via an integer index where <code>0</code> denotes the
 * first list item. <b>Only valid list items are counted.</b> If a list is given
 * in HTML that contains invalid content (e.g.: text or elements other than
 * <code>&lt;li></code>), these elements are skipped in the enumeration and are
 * not accessible through this interface. However, they can be iterated using
 * the methods provided by the WomNode interface.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "ul" or "ol".
 * 
 * <b>Child elements:</b> ([Preprocessor elements]|li)*
 */
public interface Wom3List
		extends
			Wom3ElementNode,
			Wom3UniversalAttributes
{
	/**
	 * Get the number of items in the list.
	 * 
	 * @return The number of items in the list.
	 */
	public int getItemNum();

	/**
	 * Get a collection containing all items.
	 * 
	 * @return A collection with all items of the list.
	 */
	public Collection<Wom3ListItem> getItems();

	/**
	 * Get a certain item from the list.
	 * 
	 * @param index
	 *            The zero-based index of the item to retrieve.
	 * @return The item with the given index.
	 * @throws IndexOutOfBoundsException
	 *             If the given index is out of range.
	 */
	public Wom3ListItem getItem(int index) throws IndexOutOfBoundsException;

	/**
	 * Replace a certain item in the list.
	 * 
	 * @param index
	 *            The zero-based index of the item to replace.
	 * @param item
	 *            The replacement item.
	 * @return The old item with the given index.
	 * @throws IndexOutOfBoundsException
	 *             If the given index is out of range.
	 */
	public Wom3ListItem replaceItem(int index, Wom3ListItem item) throws IndexOutOfBoundsException;

	/**
	 * Remove an item from the list.
	 * 
	 * @param index
	 *            The zero-based index of the item to remove.
	 * 
	 * @return The removed item.
	 * @throws IndexOutOfBoundsException
	 *             If the given index is out of range.
	 */
	public Wom3ListItem removeItem(int index) throws IndexOutOfBoundsException;

	/**
	 * Append an item to the list.
	 * 
	 * @param item
	 *            The item to append.
	 */
	public void appendItem(Wom3ListItem item);

	/**
	 * Insert an item at the given index into the list.
	 * 
	 * @param beforeIndex
	 *            The index of the item in front of which the new item is to be
	 *            inserted.
	 * @param item
	 *            The item to insert. The item will have the given index
	 *            <code>beforeIndex</code> after insertion.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>0 <= beforeIndex <= getItemNum()</code> does
	 *             not hold.
	 */
	public void insertItem(int beforeIndex, Wom3ListItem item) throws IndexOutOfBoundsException;

	// ==[ The XHTML Attributes ]===============================================

	/**
	 * Tells whether the list should be displayed as compact list.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "compact".
	 * 
	 * @return Whether the "compact" flag is given or not.
	 */
	public boolean isCompact();

	/**
	 * Set whether the list should be displayed as compact list.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "compact".
	 * 
	 * @param compact
	 *            Set (<code>true</code>) or remove (<code>false</code>) the
	 *            "compact" flag.
	 * @return The old state.
	 */
	public boolean setCompact(boolean compact);
}
