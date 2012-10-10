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
 * A Wikitext transclusion statement.
 * 
 * Also this node is called transclusion statement it can also represent a
 * parser function invocation or a parser variable substitution. Which of the
 * above applies depends on the name the transclusion statement specifies and
 * what that name represents in the context of the wiki in which the statement
 * is evaluated.
 * 
 * Corresponds to the WXML 1.0 element "transclusion".
 * 
 * <b>Child elements:</b> name arg*
 */
public interface WomTransclusion
		extends
			WomProcessingInstruction
{
	/**
	 * Get the name of the (template) page to transclude.
	 * 
	 * Operates on the first &lt;name> element found among this node's children.
	 * 
	 * @return The name of template page to transclude.
	 */
	public WomName getName();
	
	/**
	 * Set the namem of the (template) page to transclude.
	 * 
	 * Operates on the first &lt;name> element found among this node's children.
	 * 
	 * @param page
	 *            The name of the template page to transclude.
	 * @return The old page.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is given as name.
	 */
	public WomName setName(WomName name) throws NullPointerException;
	
	/**
	 * Returns the arguments of the transclusion statement.
	 * 
	 * @return The arguments of the transclusion statement.
	 */
	public Collection<WomArg> getArguments();
	
	/**
	 * Retrieve a transclusion argument.
	 * 
	 * @param name
	 *            The name of the argument to retrieve.
	 * @return The argument or <code>null</code> if no argument with the given
	 *         name exists.
	 */
	public WomArg getArgument(String name);
	
	/**
	 * Add or replace an argument to the transclusion statement.
	 * 
	 * @param argument
	 *            The argument to add.
	 * @return The old argument with the same name or <code>null</code> if there
	 *         was no argument with the same.
	 */
	public WomArg setArgument(WomArg argument);
	
	/**
	 * Remove an argument from the transclusion statement.
	 * 
	 * @param name
	 *            The name of the argument to remove.
	 * @return The removed argument or <code>null</code> if no argument with the
	 *         given name exists.
	 */
	public WomArg removeArgument(String name);
}
