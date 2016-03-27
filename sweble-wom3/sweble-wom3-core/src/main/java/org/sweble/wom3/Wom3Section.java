/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3;

/**
 * A section in Wikitext.
 * 
 * Corresponds to the XWML 1.0 element "section".
 * 
 * <b>Child elements:</b> heading body
 */
public interface Wom3Section
		extends
			Wom3ElementNode
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
	public Wom3Heading getHeading();

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
	public Wom3Heading setHeading(Wom3Heading heading) throws NullPointerException;

	/**
	 * Return the body of this section.
	 * 
	 * Operates on the first &lt;body> element found among this node's children.
	 * 
	 * @return The body of this section.
	 */
	public Wom3Body getBody();

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
	public Wom3Body setBody(Wom3Body body) throws NullPointerException;
}
