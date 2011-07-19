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

/**
 * Denotes a magic word.
 */
public interface DomMagicWord
        extends
            DomProcessingInstruction
{
	/**
	 * Get the name of the magic word.
	 * 
	 * @return The name of the magic word.
	 */
	public String getName();
	
	/**
	 * Set the name of the magic word.
	 * 
	 * @param name
	 *            The new name of the magic word.
	 * 
	 * @return The old name of the magic word.
	 */
	public String setName(String name);
}
