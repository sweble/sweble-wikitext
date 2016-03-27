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
 * Denotes a table caption.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "caption".
 * 
 * <b>Child elements:</b> Mixed, [Inline elements]*
 */
public interface Wom3TableCaption
		extends
			Wom3ElementNode,
			Wom3UniversalAttributes
{
	/**
	 * Get the alignment of the caption.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @return The alignment of the caption or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public Wom3TableCaptionAlign getAlign();

	/**
	 * Set the alignment of the caption.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @param align
	 *            The new alignment of the caption or <code>null</code> to
	 *            remove the attribute.
	 * @return The old alignment of the caption.
	 */
	public Wom3TableCaptionAlign setAlign(Wom3TableCaptionAlign align);
}
