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
 * An arbitrary XML element not part of the XHTML 1.0 Transitional
 * specification.
 * 
 * Corresponds to the WXML 1.0 element "element".
 * 
 * <b>Child elements:</b> attr* elembody?
 */
public interface WomElement
		extends
			WomInlineElement
{
	/**
	 * Get the name of the element.
	 * 
	 * Corresponds to the XWML 1.0 attribute "name".
	 * 
	 * @return The name of the element.
	 */
	public String getName();
	
	/**
	 * Set the name of the element.
	 * 
	 * Corresponds to the XWML 1.0 attribute "name".
	 * 
	 * @param name
	 *            The new name of the element.
	 * @return The old name of the element.
	 * @throws IllegalArgumentException
	 *             Thrown if an empty name is passed or the name is not a valid
	 *             XML name.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is passed as name.
	 */
	public String setName(String name) throws IllegalArgumentException, NullPointerException;
	
	/**
	 * Return a collection containing the XML attributes of the element.
	 * 
	 * @return A collection containing the XML attributes of the element.
	 */
	public Collection<WomAttr> getElemAttributes();
	
	/**
	 * Return the value of an attribute. If no attribute with the given name
	 * exists <code>null</code> is returned.
	 * 
	 * @return The attribute with the given name or <code>null</code>.
	 */
	public WomAttr getElemAttribute(String name);
	
	/**
	 * Remove an attribute.
	 * 
	 * @return The removed attribute node or <code>null</code> if no such
	 *         attribute exists.
	 */
	public WomAttr removeElemAttribute(String name);
	
	/**
	 * Sets an attribute node. If the attribute already exists, it will be
	 * replaced by the given attribute. Otherwise, a new attribute will be
	 * created.
	 * 
	 * @param value
	 *            Passing <code>null</code> as value will remove the attribute.
	 * @return The old attribute or <code>null</code> if the attribute did not
	 *         exist.
	 */
	public WomAttr setElemAttribute(String name, String value);
	
	/**
	 * Sets an attribute node. If the attribute already exists, it will be
	 * replaced by the given attribute.
	 * 
	 * @return The old attribute or <code>null</code> if the attribute did not
	 *         exist.
	 */
	public WomAttr setElemAttribute(WomAttr attr);
	
	/**
	 * Get the body of the element.
	 * 
	 * @return The body of the element or <code>null</code> if the element only
	 *         consists of an empty tag. An empty element that consists of a
	 *         start tag and an end tag returns an empty body.
	 */
	public WomElementBody getBody();
	
	/**
	 * Set the body of the element.
	 * 
	 * @param body
	 *            The new body of the element or <code>null</code> to turn the
	 *            element into an empty tag.
	 * @return The old body of the element.
	 */
	public WomElementBody setBody(WomElementBody body);
}
