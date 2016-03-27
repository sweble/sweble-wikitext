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
 * Denotes a definition list.
 * 
 * Definition lists can be accessed in two different ways. First by addressing
 * individual terms and treating the definitions following each term as
 * belonging to the respective term. Second by addressing terms and definitions
 * through a common index.
 * 
 * <b>Term-oriented interface:</b><br />
 * Terms are accessed via an integer index where <code>0</code> denotes the
 * first term. Only terms are counted using this index. All other child elements
 * are ignored.However, they can be iterated using the methods provided by the
 * WomNode interface.
 * 
 * <b>Item-oriented interface:</b><br />
 * Terms and definitions can also be accessed via an integer where
 * <code>0</code> denotes the first term <b>or</b> definition. <b>Only terms and
 * definitions are counted.</b> All other child elements are skipped in the
 * enumeration and are not accessible through this interface. However, they can
 * be iterated using the methods provided by the WomNode interface.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "dl".
 * 
 * <b>Child elements:</b> ([Preprocessor elements]|dd|dt)*
 */
public interface Wom3DefinitionList
		extends
			Wom3ElementNode,
			Wom3UniversalAttributes
{
	// ==[ Term-oriented interface ]============================================

	/**
	 * Get the number of terms in this list.
	 * 
	 * @return The number of terms in this list.
	 */
	public int getTermNum();

	/**
	 * Get a collection containing all terms.
	 * 
	 * @return A collection containing all terms.
	 */
	public Collection<Wom3DefinitionListTerm> getTerms();

	/**
	 * Get a certain term from the list.
	 * 
	 * @param index
	 *            The zero-based index of the term to retrieve. Only terms are
	 *            counted by this index!
	 * @return The term with the given index.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if the given index is out of range.
	 */
	public Wom3DefinitionListTerm getTerm(int index) throws IndexOutOfBoundsException;

	/**
	 * Replace a certain term and all its definitions by another term and its
	 * definitions.
	 * 
	 * @param index
	 *            The zero-based index of the term to replace. Only terms are
	 *            counted by this index!
	 * @param term
	 *            The replacement term.
	 * @return The term previously stored at the given index.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if the given index is out of range.
	 */
	public Wom3DefinitionListTerm replaceTerm(
			int index,
			Wom3DefinitionListTerm term) throws IndexOutOfBoundsException;

	/**
	 * Remove a term and all its definitions from the list.
	 * 
	 * @param index
	 *            The zero-based index of the term to remove. Only terms are
	 *            counted by this index!
	 * @throws IndexOutOfBoundsException
	 *             Thrown if the given index is out of range.
	 */
	public Wom3DefinitionListTerm removeTerm(int index) throws IndexOutOfBoundsException;

	/**
	 * Append a term and its definitions to the list.
	 * 
	 * @param term
	 *            The term to append.
	 */
	public void appendTerm(Wom3DefinitionListTerm term);

	/**
	 * Insert a term and its definitions at the given index into the list.
	 * 
	 * @param beforeIndex
	 *            The index of the term in front of which the new term and its
	 *            definitions is to be inserted. This index only counts terms!
	 * @param term
	 *            The term to insert. The term will have the given index
	 *            <code>beforeIndex</code> after insertion.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if <code>0 <= beforeIndex <= getItemNum()</code> does
	 *             not hold.
	 */
	public void insertTerm(int beforeIndex, Wom3DefinitionListTerm term) throws IndexOutOfBoundsException;

	// ==[ Item-oriented interface ]===========================================

	/**
	 * Get the number of terms and definitions in this list.
	 * 
	 * @return The number of terms and definitions in this list.
	 */
	public int getItemNum();

	/**
	 * Get a collection containing all terms and definitions.
	 * 
	 * @return A collection with all items of the list.
	 */
	public Collection<Wom3DefinitionListItem> getItems();

	/**
	 * Get a certain term or definition from the list.
	 * 
	 * @param index
	 *            The zero-based index of the item to retrieve.
	 * @return The item with the given index.
	 * @throws IndexOutOfBoundsException
	 *             If the given index is out of range.
	 */
	public Wom3DefinitionListItem getItem(int index) throws IndexOutOfBoundsException;

	/**
	 * Replace a certain term or definition in the list.
	 * 
	 * If the replacement item is a term with definitions attached to it,
	 * definitions <b>will not</b> be replaced. Instead the definitions attached
	 * to the replacement term will be discarded!
	 * 
	 * @param index
	 *            The zero-based index of the item to replace.
	 * @param item
	 *            The replacement item.
	 * @return The old item with the given index.
	 * @throws IndexOutOfBoundsException
	 *             If the given index is out of range.
	 */
	public Wom3DefinitionListItem replaceItem(
			int index,
			Wom3DefinitionListItem item) throws IndexOutOfBoundsException;

	/**
	 * Remove a term or definition from the list.
	 * 
	 * If the item that should be removed is a term, <b>only the term</b> will
	 * be removed. Its definitions are left untouched and become the definitions
	 * of the preceding term (if there is a preceding term).
	 * 
	 * @param index
	 *            The zero-based index of the item to remove.
	 * @return The removed item.
	 * @throws IndexOutOfBoundsException
	 *             If the given index is out of range.
	 */
	public Wom3DefinitionListItem removeItem(int index) throws IndexOutOfBoundsException;

	/**
	 * Append term or definition to the list.
	 * 
	 * If the item that should be appended is a term with definitions attached,
	 * the definitions <b>will not</b> be inserted into the list. Instead the
	 * term's definitions will be discarded.
	 * 
	 * @param item
	 *            The item to append.
	 */
	public void appendItem(Wom3DefinitionListItem item);

	/**
	 * Insert a term or definition at the given index into the list.
	 * 
	 * If the item that should be appended is a term with definitions attached,
	 * the definitions <b>will not</b> be inserted into the list. Instead the
	 * term's definitions will be discarded.
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
	public void insertItem(int beforeIndex, Wom3DefinitionListItem item) throws IndexOutOfBoundsException;

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
