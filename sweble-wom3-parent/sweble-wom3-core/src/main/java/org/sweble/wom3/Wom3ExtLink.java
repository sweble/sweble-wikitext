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
 * Denotes a Wikitext bracketed external link.
 * 
 * Corresponds to the WXML 1.0 element "extlink".
 * 
 * <b>Child elements:</b> title?
 */
public interface Wom3ExtLink
		extends
			Wom3ElementNode,
			Wom3Link
{
	/**
	 * Set the title of the external link.
	 * 
	 * @param title
	 *            The new title of the external link or <code>null</code> to
	 *            remove the title.
	 * @return The old link title node.
	 */
	public Wom3Title setLinkTitle(Wom3Title title);

	/**
	 * Retrieve the target of this link.
	 * 
	 * Corresponds to the XWML 1.0 attribute "target".
	 * 
	 * @return The target of this link.
	 */
	public URL getTarget();

	/**
	 * Set a new target for this external link.
	 * 
	 * Corresponds to the XWML 1.0 attribute "target".
	 * 
	 * @param target
	 *            The new target of the external link.
	 * @return The old target of the external link.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code>is passed as URL.
	 */
	public URL setTarget(URL target) throws NullPointerException;

	public boolean isPlainUrl();

	public boolean setPlainUrl(boolean plainUrl);

	// ==[ Link interface ]=====================================================

	/**
	 * Returns the title of the external link. If the external link does not
	 * specify a title, an empty title is returned.
	 * 
	 * @return The title of the external link.
	 */
	@Override
	public Wom3Title getLinkTitle();

	/**
	 * Retrieve the target of this link.
	 * 
	 * @return The target of this link.
	 */
	@Override
	public URL getLinkTarget();
}
