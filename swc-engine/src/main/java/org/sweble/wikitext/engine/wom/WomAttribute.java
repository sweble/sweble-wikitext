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
 * An attribute node.
 * 
 * Objects of this class represent attributes that can be attached to other
 * nodes that support attributes. An attribute node can only have other
 * attribute nodes as siblings. An attribute node cannot have children or
 * attributes of its own.
 * 
 * <b>Child elements:</b> -
 */
public interface WomAttribute
		extends
			WomNode
{
	/**
	 * Retrieve the name of the attribute. Attribute names are case-insensitive.
	 * 
	 * @return The name of the attribute.
	 */
	public String getName();
	
	/**
	 * Set the name of the attribute. Attribute names are case-insensitive.
	 * 
	 * @param name
	 *            The new name of the attribute.
	 * @return The old name of the attribute.
	 * @throws IllegalArgumentException
	 *             If an attribute with the given name already exists or the
	 *             given name was empty or not a valid XML name.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> was specified as name.
	 */
	public String setName(String name)
			throws IllegalArgumentException,
			NullPointerException;
	
	/**
	 * Return the value of the attribute.
	 * 
	 * @return The value of the attribute.
	 */
	@Override
	public String getValue();
	
	/**
	 * Set the value of the attribute.
	 * 
	 * @param value
	 *            The new value of the attribute.
	 * @return The old value of the attribute.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> was specified as value.
	 */
	public String setValue(String value) throws NullPointerException;
}
