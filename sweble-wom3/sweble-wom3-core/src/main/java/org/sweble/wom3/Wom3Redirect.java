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

public interface Wom3Redirect
		extends
			Wom3ElementNode,
			Wom3Link
{
	public String getDisplacementId();

	public String setDisplacementId(String did);

	/**
	 * Return the target page of the redirection.
	 * 
	 * Corresponds to the XWML 1.0 attribute "target".
	 * 
	 * @return The target page to redirect to.
	 */
	public String getTarget();

	/**
	 * Set the target page of the redirection.
	 * 
	 * Corresponds to the XWML 1.0 attribute "target".
	 * 
	 * @param page
	 *            The new target of the redirection.
	 * @return The old target of the redirection.
	 */
	public String setTarget(String page);

	// ==[ Link interface ]=====================================================

	/**
	 * Returns an empty title since redirection statements do not provide a
	 * title.
	 * 
	 * @return An empty title.
	 */
	@Override
	public Wom3Title getLinkTitle();

	/**
	 * Return the target page of the redirection.
	 * 
	 * @return The target page to redirect to.
	 */
	@Override
	public String getLinkTarget();
}
