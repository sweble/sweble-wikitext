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
 * A Wikitext call to a tag extension.
 * 
 * Corresponds to the XWML 1.0 element "signature".
 * 
 * <b>Child elements:</b> attr* tagextbody?
 */
public interface WomTagExtension
		extends
			WomProcessingInstruction
{
	/**
	 * Get the name of the tag extension.
	 * 
	 * Corresponds to the XWML 1.0 attribute "name".
	 * 
	 * @return The name of the tag extension.
	 */
	public String getName();
	
	/**
	 * Set the name of the tag extension.
	 * 
	 * Corresponds to the XWML 1.0 attribute "name".
	 * 
	 * @param name
	 *            The new name of the tag extension.
	 * @return The old name of the tag extension.
	 * @throws IllegalArgumentException
	 *             If the given name is empty or not a valid XML name.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is given as name.
	 */
	public String setName(String name) throws IllegalArgumentException, NullPointerException;
	
	/**
	 * Returns the attributes attached to the tag extension.
	 * 
	 * @return The attributes attached to the tag extension.
	 */
	public Collection<WomAttr> getTagAttributes();
	
	/**
	 * Retrieve a tag attribute.
	 * 
	 * @param name
	 *            The name of the attribute to retrieve.
	 * @return The attribute or <code>null</code> if no attribute with the given
	 *         name exists.
	 */
	public WomAttr getTagAttribute(String name);
	
	/**
	 * Add or replace an attribute of the tag extension call.
	 * 
	 * @param attribute
	 *            The attribute to add or replace.
	 * @return The old attribute with the same name or <code>null</code> if
	 *         there was no attribute with the same.
	 */
	public WomAttr setTagAttribute(WomAttr attribute);
	
	/**
	 * Remove a attribute from the tag extension call.
	 * 
	 * @param name
	 *            The name of the attribute to remove.
	 * @return The removed attribute or <code>null</code> if no attribute with
	 *         the given name exists.
	 */
	public WomAttr removeTagAttribute(String name);
	
	/**
	 * Get the body of the element.
	 * 
	 * @return The body of the element or <code>null</code> if the element only
	 *         consists of an empty tag. An empty element that consists of a
	 *         start tag and an end tag returns an empty body.
	 */
	public WomTagExtBody getBody();
	
	/**
	 * Set the body of the element.
	 * 
	 * @param body
	 *            The new body of the element or <code>null</code> to turn the
	 *            element into an empty tag.
	 * @return The old body of the element.
	 */
	public WomTagExtBody setBody(WomTagExtBody body);
}
