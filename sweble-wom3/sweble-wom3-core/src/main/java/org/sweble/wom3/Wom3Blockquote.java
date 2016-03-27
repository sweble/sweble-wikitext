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

import java.net.URL;

/**
 * Denotes a long quotation.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "blockquote".
 * 
 * <b>Child elements:</b> [Block elements]*
 */
public interface Wom3Blockquote
		extends
			Wom3ElementNode,
			Wom3UniversalAttributes
{
	/**
	 * Get source of the quotation.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cite".
	 * 
	 * @return The source of the citation or <code>null</code> if the attribute
	 *         is not specified.
	 */
	public URL getCite();

	/**
	 * Set the source of the quotation.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cite".
	 * 
	 * @param source
	 *            The source of the citation or <code>null</code> to remove the
	 *            attribute.
	 * @return The source of the citation.
	 */
	public URL setCite(URL source);
}
