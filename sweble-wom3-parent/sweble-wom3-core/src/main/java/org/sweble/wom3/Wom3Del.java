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

import org.joda.time.DateTime;

/**
 * Denotes text or a block that has been removed.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "del".
 * 
 * <b>Child elements:</b> Mixed, [Flow elements]*
 */
public interface Wom3Del
		extends
			Wom3ElementNode,
			Wom3UniversalAttributes
{
	/**
	 * Get the url of a document that specifies the reasons for the change.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cite".
	 * 
	 * @return The url or <code>null</code> if the attribute is not specified.
	 */
	public URL getCite();

	/**
	 * Set the url of a document that specifies the reasons for the change.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cite".
	 * 
	 * @param url
	 *            The new url or <code>null</code> to remove the attribute.
	 * @return The The old url.
	 */
	public URL setCite(URL url);

	/**
	 * Get the timestamp when the text or block was deleted.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cite".
	 * 
	 * @return The date and time of the deletion or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public DateTime getDatetime();

	/**
	 * Set the timestamp when the text or block was deleted.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cite".
	 * 
	 * @param timestamp
	 *            The new timestamp or <code>null</code> to remove the
	 *            attribute.
	 * @return The old timestamp.
	 */
	public DateTime setDatetime(DateTime timestamp);
}
