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
 * The XHTML 1.0 Transitional core attributes.
 */
public interface WomCoreAttributes
{
	/**
	 * Get the unique id of the element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "id".
	 * 
	 * @return The unique id of the element or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public String getId();
	
	/**
	 * Set the unique id of the element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "id".
	 * 
	 * @param id
	 *            The new id of the element or <code>null</code> to remove the
	 *            attribute.
	 * @return The old unique id of the element.
	 */
	public String setId(String id) throws IllegalArgumentException;
	
	/**
	 * Get the stylesheet classes assigned to this element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "class".
	 * 
	 * @return A string containing the names of the stylesheet classes,
	 *         separated by space. <code>null</code> if the attribute is not
	 *         specified.
	 */
	public String getClasses();
	
	/**
	 * Set the stylesheet classes assigned to this element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "class".
	 * 
	 * @param classes
	 *            A string containing the new classes or <code>null</code> to
	 *            remove the attribute.
	 * @return A string containing the old classes.
	 */
	public String setClasses(String classes);
	
	/**
	 * Get CSS styles directly associated with this element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "style".
	 * 
	 * @return The CSS styles directly associated with this element or
	 *         <code>null</code> if the attribute is not specified.
	 */
	public String getStyle();
	
	/**
	 * Directly associate CSS styles with this element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "style".
	 * 
	 * @param style
	 *            The new CSS styles to associate with this element or
	 *            <code>null</code> to remove the attribute.
	 * @return The old CSS styles that were associated with this element.
	 */
	public String setStyle(String style);
	
	/**
	 * Get the title of the element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "title".
	 * 
	 * @return The title of the element or <code>null</code> if the attribute is
	 *         not specified.
	 */
	public String getTitlte();
	
	/**
	 * Get the title of the element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "title".
	 * 
	 * @param title
	 *            The new title of this element or <code>null</code> to remove
	 *            the attribute.
	 * @return The old title of this element.
	 */
	public String setTitlte(String title);
}
