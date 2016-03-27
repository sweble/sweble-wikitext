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
 * Denotes a horizontal rule.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "hr".
 * 
 * <b>Child elements:</b> -
 */
public interface Wom3HorizontalRule
		extends
			Wom3ElementNode,
			Wom3UniversalAttributes
{
	/**
	 * Get the alignment of the horizontal rule.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @return The alignment of the horizontal rule or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public Wom3HorizAlign getAlign();

	/**
	 * Set the alignment of the horizontal rule.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @param align
	 *            The alignment. Only the values <code>left</code>,
	 *            <code>center</code> and <code>right</code> are allowed.
	 * @return The old alignment of the horizontal rule.
	 */
	public Wom3HorizAlign setAlign(Wom3HorizAlign align);

	/**
	 * Get whether the horizontal rule is display with a 3-D effect (shade) or
	 * without (no-shade).
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "noshade".
	 * 
	 * @return <code>True</true> for no 3-D effect, <code>false</code> for a 3-D
	 *         effect.
	 */
	public boolean isNoshade();

	/**
	 * Set whether the horizontal rule is display with a 3-D effect (shade) or
	 * without (no-shade).
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "noshade".
	 * 
	 * @param noshade
	 *            The new setting.
	 * @return The old setting.
	 */
	public boolean setNoshade(boolean noshade);

	/**
	 * Get the thickness of the horizontal rule in pixels.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "size".
	 * 
	 * @return The thickness in pixels or <code>null</code> if the attribute is
	 *         not specified.
	 */
	public Integer getSize();

	/**
	 * Set the thickness of the horizontal rule in pixels.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "size".
	 * 
	 * @param size
	 *            The new thickness in pixels or <code>null</code> to remove the
	 *            attribute.
	 * @return The old thickness in pixels.
	 */
	public Integer setSize(Integer size);

	/**
	 * Get the width of the horizontal rule.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "width".
	 * 
	 * @return The width in pixels or percent or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public Wom3ValueWithUnit getWidth();

	/**
	 * Set the width of the horizontal rule.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "width".
	 * 
	 * @param width
	 *            The new width in pixels or percent or <code>null</code> to
	 *            remove the attribute.
	 * @return The old width in pixels or percent.
	 */
	public Wom3ValueWithUnit setWidth(Wom3ValueWithUnit width);
}
