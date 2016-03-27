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
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3NamedNodeMap;
import org.sweble.wom3.Wom3Node;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class AttributeMap
		extends
			NodeListImpl
		implements
			Wom3NamedNodeMap
{
	private BackboneElement element;

	// =========================================================================

	public AttributeMap(BackboneElement element)
	{
		super(element);
		this.element = element;
	}

	// =========================================================================

	@Override
	public Wom3Node getNamedItem(String name)
	{
		for (Backbone n = element.getFirstAttr(); n != null; n = n.getNextSibling())
		{
			if (n.getNodeName().equals(name))
				return n;
		}
		return null;
	}

	@Override
	public Wom3Node getNamedItemNS(String namespaceURI, String localName) throws DOMException
	{
		if (namespaceURI.equals(Wom3Node.WOM_NS_URI))
			return getNamedItem(localName);
		return null;
	}

	@Override
	public int getLength()
	{
		int len = 0;
		for (Wom3Node n = element.getFirstAttr(); n != null; n = n.getNextSibling())
			++len;
		return len;
	}

	@Override
	public Wom3Node item(int index)
	{
		int cnt = 0;
		Wom3Node n = element.getFirstAttr();
		while (n != null && cnt++ < index)
			n = n.getNextSibling();
		return n;
	}

	@Override
	public Wom3Node setNamedItem(Node arg) throws DOMException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Wom3Node setNamedItemNS(Node arg) throws DOMException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Wom3Node removeNamedItem(String name) throws DOMException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Wom3Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
}
