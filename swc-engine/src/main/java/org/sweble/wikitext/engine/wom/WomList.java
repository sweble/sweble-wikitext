/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sweble.wikitext.engine.wom;

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
public interface WomList
        extends
            WomBlockElement,
            WomUniversalAttributes
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
	public Collection<WomListItem> getItems();
	
	/**
	 * Get a certain item from the list.
	 * 
	 * @param index
	 *            The zero-based index of the item to retrieve.
	 * @return The item with the given index.
	 * @throws IndexOutOfBoundsException
	 *             If the given index is out of range.
	 */
	public WomListItem getItem(int index) throws IndexOutOfBoundsException;
	
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
	public WomListItem replaceItem(int index, WomListItem item) throws IndexOutOfBoundsException;
	
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
	public WomListItem removeItem(int index) throws IndexOutOfBoundsException;
	
	/**
	 * Append an item to the list.
	 * 
	 * @param item
	 *            The item to append.
	 */
	public void appendItem(WomListItem item);
	
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
	public void insertItem(int beforeIndex, WomListItem item) throws IndexOutOfBoundsException;
	
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
}
