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
 * An argument to a Wikitext transclusion statement.
 * 
 * Corresponds to the WXML 1.0 element "arg".
 * 
 * <b>Child elements:</b> name? value
 */
public interface WomArg
        extends
            WomNode
{
	/**
	 * Get the name of the argument.
	 * 
	 * Operates on the first &lt;name> element found among this node's children.
	 * 
	 * @return The name of the argument or <code>null</code> if the argument
	 *         does not have a name.
	 */
	public WomName getName();
	
	/**
	 * Set the name of the argument.
	 * 
	 * Operates on the first &lt;name> element found among this node's children.
	 * If no name node is found, the name will be added as the first child.
	 * 
	 * @param name
	 *            The new name of the argument or <code>null</code> to turn the
	 *            argument into an anonymous argument.
	 * @return The old name of the argument.
	 */
	public WomName setName(WomName name);
	
	/**
	 * Get the value of the argument.
	 * 
	 * Operates on the first &lt;value> element found among this node's
	 * children.
	 * 
	 * @return The name of the value.
	 */
	public WomValue getArgValue();
	
	/**
	 * Set the value of the argument.
	 * 
	 * Operates on the first &lt;value> element found among this node's
	 * children. If no value node is found, the value will be added as the first
	 * child.
	 * 
	 * @param value
	 *            The new value of the argument.
	 * @return The old value of the argument.
	 * @throws NullPointerException
	 *             Thrown when <code>null</code> is passed as value.
	 */
	public WomValue setArgValue(WomValue value) throws NullPointerException;
}
