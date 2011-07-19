package org.sweble.wikitext.engine.dom;

import java.util.Collection;

/**
 * Interface to lists like <code>&lt;ul></code> or <code>&lt;ol></code>.
 * 
 * List items are accessed via an integer index where <code>0</code> denotes the
 * first list item. <b>Only valid list items are counted.</b> If a list is given
 * in HTML that contains invalid content (e.g.: text or elements other than
 * <code>&lt;li></code>), these elements are skipped in the enumeration and not
 * accessible through this interface. However, they can be iterated using the
 * methods provided by the DomNode interface.
 */
public interface DomList
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
	public Collection<DomListItem> getItems();
	
	/**
	 * Get a certain item from the list.
	 * 
	 * @param index
	 *            The zero-based index of the item to retrieve.
	 * 
	 * @return The item with the given index.
	 * @throws IndexOutOfBoundsException
	 */
	public DomListItem getItem(int index) throws IndexOutOfBoundsException;
	
	/**
	 * Set a certain item in the list.
	 * 
	 * @param index
	 *            The zero-based index of the item to set.
	 * @param item
	 *            The new item to set at the given index.
	 * 
	 * @return The old item with the given index.
	 * @throws IndexOutOfBoundsException
	 */
	public DomListItem setItem(int index, DomListItem item) throws IndexOutOfBoundsException;
	
	/**
	 * Remove an item from the list.
	 * 
	 * @param index
	 *            The zero-based index of the item to remove.
	 * 
	 * @return The removed item.
	 * @throws IndexOutOfBoundsException
	 */
	public DomListItem removeItem(int index) throws IndexOutOfBoundsException;
	
	/**
	 * Append an item to the list.
	 * 
	 * @param item
	 *            The item to append.
	 */
	public void appendItem(DomListItem item);
	
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
	public void insertItem(int beforeIndex, DomListItem item) throws IndexOutOfBoundsException;
	
}
