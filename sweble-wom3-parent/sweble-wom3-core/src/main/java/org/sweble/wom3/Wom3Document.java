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
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface Wom3Document
		extends
			Wom3Node,
			Document
{
	@Override
	public Wom3ElementNode getDocumentElement();

	@Override
	public Wom3DocumentFragment createDocumentFragment();

	@Override
	public Wom3ElementNode createElement(String tagName) throws DOMException;

	@Override
	public Wom3ElementNode createElementNS(
			String namespaceURI,
			String qualifiedName) throws DOMException;

	@Override
	public Wom3Node adoptNode(Node source) throws DOMException;

	@Override
	public Wom3DomImplementation getImplementation();

	boolean getReadOnly();

	void setReadOnly(boolean readOnly);
}
