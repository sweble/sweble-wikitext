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
package org.sweble.wikitext.engine.dom;

import java.util.Collection;

/**
 * A Wikitext call to a tag extension.
 * 
 * The content of the tag extension call can be obtained through the getValue()
 * method of DomNode.
 */
public interface DomTagExtensionCall
        extends
            DomProcessingInstruction
{
	/**
	 * Get the name of the tag extension.
	 * 
	 * @return The name of the tag extension.
	 */
	public String getName();
	
	/**
	 * Set the name of the tag extension.
	 * 
	 * @param name
	 *            The new name of the tag extension.
	 * @return The old name of the tag extension.
	 */
	public String setName(String name);
	
	/**
	 * Returns the attributes attached to the tag extension.
	 * 
	 * @return The attributes attached to the tag extension.
	 */
	public Collection<DomAttr> getTagAttributes();
	
	/**
	 * Retrieve a tag attribute.
	 * 
	 * @param name
	 *            The name of the attribute to retrieve.
	 * @return The attribute or <code>null</code> if no attribute with the given
	 *         name exists.
	 */
	public DomAttr getTagAttribute(String name);
	
	/**
	 * Add or replace an attribute of the tag extension call.
	 * 
	 * @param attribute
	 *            The attribute to add or replace.
	 * @return The old attribute with the same name or <code>null</code> if
	 *         there was no attribute with the same.
	 */
	public DomAttr setTagAttribute(DomAttr attribute);
	
	/**
	 * Remove a attribute from the tag extension call.
	 * 
	 * @param name
	 *            The name of the attribute to remove.
	 * @return The removed attribute or <code>null</code> if no attribute with
	 *         the given name exists.
	 */
	public DomAttr removeTagAttribute(String name);
	
	/**
	 * Remove an attribute from the tag extension call.
	 * 
	 * @param attribute
	 *            The attribute to remove.
	 * @throws IllegalArgumentException
	 *             Thrown if the given attribute is not assigned to this tag
	 *             extension.
	 */
	public void removeTagAttribute(DomAttr attribute) throws IllegalArgumentException;
}
