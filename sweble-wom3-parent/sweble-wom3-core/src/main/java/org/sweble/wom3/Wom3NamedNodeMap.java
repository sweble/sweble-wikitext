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

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public interface Wom3NamedNodeMap
		extends
			NamedNodeMap
{
	public Wom3Node getNamedItem(String name);

	public Wom3Node setNamedItem(Node arg) throws DOMException;

	public Wom3Node removeNamedItem(String name) throws DOMException;

	public Wom3Node item(int index);

	public Wom3Node getNamedItemNS(String namespaceURI, String localName) throws DOMException;

	public Wom3Node setNamedItemNS(Node arg) throws DOMException;

	public Wom3Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException;
}
