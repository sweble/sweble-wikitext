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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.sweble.wom3.Wom3ElementNode;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3NodeList;
import org.w3c.dom.Node;

/**
 * Warning: Behavior undefined if the document is changed during iteration.
 */
public class ElementsByTagNameNodeList
		implements
			Wom3NodeList
{
	private final String tagName;

	private final String namespaceUri;

	private Wom3Node root;

	private boolean withNs = false;

	private ArrayList<Wom3Node> nodes = null;

	// =========================================================================

	public ElementsByTagNameNodeList(Wom3Node root, String tagName)
	{
		this.root = root;
		this.namespaceUri = null;
		this.tagName = tagName;
	}

	public ElementsByTagNameNodeList(
			Wom3Node root,
			String namespaceURI,
			String localName)
	{
		this.withNs = true;
		this.root = root;
		if (namespaceURI != null && namespaceURI.isEmpty())
			namespaceURI = null;
		this.namespaceUri = namespaceURI;
		this.tagName = localName;
	}

	// =========================================================================

	@Override
	public int getLength()
	{
		item(Integer.MAX_VALUE);
		return (nodes != null) ? nodes.size() : 0;
	}

	@Override
	public Wom3Node item(int index)
	{
		if (nodes != null && nodes.size() > index)
		{
			// We already had that node
			return nodes.get(index);
		}
		else if (root == null)
		{
			// We've already found all matching nodes
			return null;
		}
		else
		{
			// Start the search at the root element...
			Wom3Node current = root;
			if (nodes != null && !nodes.isEmpty())
				// ... or continue
				current = nodes.get(nodes.size() - 1);

			while (true)
			{
				current = advance(current);
				if (current == null)
				{
					// We're through
					root = null;
					return null;
				}
				else if (isMatch(current))
				{
					if (nodes == null)
						nodes = new ArrayList<Wom3Node>();
					nodes.add(current);
					if (nodes.size() > index)
						return current;
				}
			}
		}
	}

	private boolean isMatch(Wom3Node current)
	{
		// We're searching for elements only
		if (current.getNodeType() != Node.ELEMENT_NODE)
			return false;

		Wom3ElementNode e = (Wom3ElementNode) current;

		if (withNs)
			return isNsMatch(e) && isLocalNameMatch(e);

		return isTagNameMatch(e);
	}

	private boolean isTagNameMatch(Wom3ElementNode e)
	{
		if (tagName.equals("*"))
			return true;
		return tagName.equals(e.getTagName());
	}

	private boolean isLocalNameMatch(Wom3ElementNode e)
	{
		if (tagName.equals("*"))
			return true;

		return tagName.equals(e.getLocalName());
	}

	private boolean isNsMatch(Wom3ElementNode e)
	{
		if (namespaceUri != null)
		{
			if (namespaceUri.equals("*"))
				return true;
			return (namespaceUri.equals(e.getNamespaceURI()));
		}
		return (e.getNamespaceURI() == null);
	}

	private Wom3Node advance(Wom3Node current)
	{
		Wom3Node next = current.getFirstChild();
		if (next != null)
		{
			// While descending process each node on the way
			return next;
		}
		else
		{
			// If we cannot descend any more, process each sibling 
			next = current.getNextSibling();
			while (next == null)
			{
				// If we have no siblings, ascend but don't process the parent
				// nodes on the way to the node that again has siblings
				current = current.getParentNode();
				if (current == this.root)
					// If we reach the root, we're done.
					return null;
				next = current.getNextSibling();
			}
			return next;
		}
	}

	// =========================================================================

	@Override
	public Iterator<Wom3Node> iterator()
	{
		return new Iterator<Wom3Node>()
		{
			private int index = 0;

			@Override
			public boolean hasNext()
			{
				return item(index) != null;
			}

			@Override
			public Wom3Node next()
			{
				Wom3Node item = item(index);
				if (item == null)
					throw new NoSuchElementException();
				++index;
				return item;
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}
}
