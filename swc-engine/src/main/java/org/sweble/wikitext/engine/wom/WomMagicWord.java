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
 * Denotes a magic word.
 * 
 * Corresponds to the WXML 1.0 element "magicword".
 * 
 * <b>Child elements:</b> -
 */
public interface WomMagicWord
		extends
			WomProcessingInstruction
{
	/**
	 * Get the name of the magic word.
	 * 
	 * Corresponds to the XWML 1.0 attribute "name".
	 * 
	 * @return The name of the magic word.
	 */
	public String getName();
	
	/**
	 * Set the name of the magic word.
	 * 
	 * Corresponds to the XWML 1.0 attribute "name".
	 * 
	 * @param name
	 *            The new name of the magic word.
	 * @return The old name of the magic word.
	 * @throws IllegalArgumentException
	 *             Thrown if name is not a valid name for a magic word or if
	 *             <code>null</code> is passed as name.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is passed as name.
	 */
	public String setName(String name) throws IllegalArgumentException, NullPointerException;
}
