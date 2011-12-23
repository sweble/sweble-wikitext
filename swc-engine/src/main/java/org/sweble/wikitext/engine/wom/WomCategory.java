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

/**
 * A category statement.
 * 
 * Corresponds to the XWML 1.0 element "category".
 * 
 * <b>Child elements:</b> -
 */
public interface WomCategory
		extends
			WomProcessingInstruction,
			WomLink
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
	
	/**
	 * @deprecated Use getName() instead.
	 */
	public String getCategory();
	
	/**
	 * @deprecated Use setName() instead.
	 */
	public String setCategory(String category)
			throws NullPointerException,
			IllegalArgumentException;
	
	// ==[ Link interface ]=====================================================
	
	/**
	 * Returns an empty title since category statements do not provide a title.
	 * 
	 * @return An empty title.
	 */
	@Override
	public WomTitle getLinkTitle();
	
	/**
	 * Return the name of the category this statement is pointing to.
	 * 
	 * @return The name of the category including the category namespace
	 *         specifier.
	 */
	@Override
	public String getLinkTarget();
}
