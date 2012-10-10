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
 * A section in Wikitext.
 * 
 * Corresponds to the XWML 1.0 element "section".
 * 
 * <b>Child elements:</b> heading body
 */
public interface WomSection
		extends
			WomBlockElement
{
	/**
	 * Get the level of the section. Ranges from 1 (most important) to 6 (least
	 * important).
	 * 
	 * Corresponds to the XWML 1.0 attribute "level".
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
	 * Corresponds to the XWML 1.0 attribute "level".
	 * 
	 * @param level
	 *            The new level of the section.
	 * @return The old level of the section.
	 * @throws IllegalArgumentException
	 *             Thrown if this section is contained in a section with level
	 *             <code>y</code> or any of its children and
	 *             <code>level <= y</code> or if the given level does not lie in
	 *             the range [1,6].
	 */
	public int setLevel(int level) throws IllegalArgumentException;
	
	/**
	 * Return the heading of this section.
	 * 
	 * Operates on the first &lt;heading> element found among this node's
	 * children.
	 * 
	 * @return This heading of this section.
	 */
	public WomHeading getHeading();
	
	/**
	 * Set the heading of this section.
	 * 
	 * Operates on the first &lt;heading> element found among this node's
	 * children.
	 * 
	 * @param heading
	 *            The new heading.
	 * @return The old heading.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is given as heading.
	 */
	public WomHeading setHeading(WomHeading heading) throws NullPointerException;
	
	/**
	 * Return the body of this section.
	 * 
	 * Operates on the first &lt;body> element found among this node's children.
	 * 
	 * @return The body of this section.
	 */
	public WomBody getBody();
	
	/**
	 * Set the body of this section.
	 * 
	 * Operates on the first &lt;body> element found among this node's children.
	 * 
	 * @param body
	 *            The new body.
	 * @return The old body.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is given as body.
	 */
	public WomBody setBody(WomBody body) throws NullPointerException;
}
