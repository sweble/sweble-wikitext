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
public interface WomDefinitionList
        extends
            WomBlockElement,
            WomUniversalAttributes
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
	public Collection<WomDefinitionListTerm> getTerms();
	
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
	public WomDefinitionListTerm getTerm(int index) throws IndexOutOfBoundsException;
	
	/**
	 * Replace a certain term and all its definitions by another term and its
	 * definitions.
	 * 
	 * @param index
	 *            The zero-based index of the term to replace. Only terms are
	 *            counted by this index!
	 * @param item
	 *            The replacement term.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if the given index is out of range.
	 */
	public void replaceTerm(int index, WomDefinitionListTerm term) throws IndexOutOfBoundsException;
	
	/**
	 * Replace a certain term and all its definitions by another term and its
	 * definitions.
	 * 
	 * @param search
	 *            The term to replace.
	 * @param item
	 *            The replacement term.
	 * @throws IllegalArgumentException
	 *             Thrown if the given term <code>term</code> is not a term of
	 *             this list.
	 */
	public void replaceTerm(WomDefinitionListTerm search, WomDefinitionListTerm replace) throws IllegalArgumentException;
	
	/**
	 * Remove a term and all its definitions from the list.
	 * 
	 * @param index
	 *            The zero-based index of the term to remove. Only terms are
	 *            counted by this index!
	 * @throws IndexOutOfBoundsException
	 *             Thrown if the given index is out of range.
	 */
	public void removeTerm(int index) throws IndexOutOfBoundsException;
	
	/**
	 * Remove a term and all its definitions from the list.
	 * 
	 * @param term
	 *            The term to remove.
	 * @throws IllegalArgumentException
	 *             Thrown if the given term <code>term</code> is not a term of
	 *             this list.
	 */
	public void removeTerm(WomDefinitionListTerm term) throws IllegalArgumentException;
	
	/**
	 * Append a term and its definitions to the list.
	 * 
	 * @param term
	 *            The term to append.
	 */
	public void appendTerm(WomDefinitionListTerm term);
	
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
	public void insertItem(int beforeIndex, WomDefinitionListTerm term) throws IndexOutOfBoundsException;
	
	/**
	 * Insert a term and its definitions at the given index into the list.
	 * 
	 * @param before
	 *            The term in front of which the new should be inserted. This
	 *            index only counts terms!
	 * @param term
	 *            The term to insert.
	 * @throws IllegalArgumentException
	 *             Thrown if the given term <code>term</code> is not a term of
	 *             this list.
	 */
	public void insertItem(WomDefinitionListTerm before, WomDefinitionListTerm term) throws IllegalArgumentException;
	
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
	public Collection<WomDefinitionListItem> getItems();
	
	/**
	 * Get a certain term or definition from the list.
	 * 
	 * @param index
	 *            The zero-based index of the item to retrieve.
	 * @return The item with the given index.
	 * @throws IndexOutOfBoundsException
	 *             If the given index is out of range.
	 */
	public WomDefinitionListItem getItem(int index) throws IndexOutOfBoundsException;
	
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
	public WomDefinitionListItem replaceItem(int index, WomDefinitionListItem item) throws IndexOutOfBoundsException;
	
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
	public WomDefinitionListItem removeItem(int index) throws IndexOutOfBoundsException;
	
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
	public void appendItem(WomDefinitionListItem item);
	
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
	public void insertItem(int beforeIndex, WomDefinitionListItem item) throws IndexOutOfBoundsException;
	
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
