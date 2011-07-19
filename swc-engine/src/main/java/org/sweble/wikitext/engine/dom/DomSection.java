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
 * A section in Wikitext.
 * 
 * A section consists of a heading and the body containing the actual content of
 * the section.
 */
public interface DomSection
        extends
            DomBlockElement
{
	/**
	 * Get the level of the section. Ranges from 1 (most important) to 6 (least
	 * important).
	 * 
	 * @return The level of the section.
	 */
	public int getLevel();
	
	/**
	 * Set the level of the section. Ranges from 1 (most important) to 6 (least
	 * important). A section with level <code>x</code> cannot be contained in a
	 * section with level <code>y</code> or any of its children if
	 * <code>x <= y</code>.
	 * 
	 * @param level
	 *            The new level of the section.
	 * @return The old level of the section.
	 * @throws IllegalArgumentException
	 *             Thrown if this section is contained in a section with level
	 *             <code>y</code> or any of its children and
	 *             <code>level <= y</code>
	 */
	public int setLevel(int level) throws IllegalArgumentException;
}
