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
package org.sweble.wom3.swcadapter.nodes;

import org.sweble.wom3.Wom3Node;

/**
 * MWW30 == MediaWiki Wom 3.0
 */
public interface SwcNode
		extends
			Wom3Node
{
	public static final String MWW_NS_URI = "http://sweble.org/schema/mww30";

	public static final String DEFAULT_MWW_NS_PREFIX = "mww";
}
