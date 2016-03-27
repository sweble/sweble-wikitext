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

import org.sweble.wom3.Wom3Comment;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3Rtd;
import org.sweble.wom3.Wom3XmlComment;
import org.sweble.wom3.Wom3XmlText;

/**
 * The description arrays are always expected to contain every kind of node only
 * once over the whole array. An element that can accept first X, then Y and
 * then an element of type X again is not supported.
 */
public final class BackboneChildOperationChecker
{
	private final BackboneWithChildren node;

	private ChildDescriptor current = null;

	private int pos = 0;

	// =========================================================================

	BackboneChildOperationChecker(BackboneWithChildren node)
	{
		this.node = node;
	}

	// =========================================================================

	protected boolean isIgnored(Backbone child)
	{
		return isIgnoredDefault(child);
	}

	/**
	 * By default we ignore RTD node, content whitespace, XML comments and
	 * Wom3Comment nodes as well as every non-WOM node (namespace URI test).
	 */
	protected static boolean isIgnoredDefault(Backbone child)
	{
		// Sorted by likelihood
		return (child instanceof Wom3Rtd) ||
				((child instanceof XmlTextBase) && (((XmlTextBase) child).isContentWhitespace())) ||
				(!Wom3Node.WOM_NS_URI.equals(child.getNamespaceURI())) ||
				(child instanceof Wom3Comment) ||
				(child instanceof Wom3XmlComment) ||
				((child instanceof Wom3XmlText) && (((Wom3XmlText) child).isElementContentWhitespace()));
	}

	// =========================================================================

	/**
	 * This method is not supposed to make sure that the body is valid. It's
	 * supposed to make sure that the body does not become invalid. Therefore,
	 * if the body is invalid to begin with, the result is undefined.
	 */
	protected void checkInsertion(
			Backbone prev,
			Backbone child,

			ChildDescriptor[] desc)
	{
		if (isIgnored(child))
			// Ignored nodes can always be inserted anywhere
			return;

		forwardDescriptor(prev, desc);

		if (current != null && current.isMultiple() && current.matches(child))
		{
			// Yay!
			return;
		}
		else
		{
			while (true)
			{
				if (pos < desc.length)
				{
					current = desc[pos];
					if (current.matches(child))
					{
						// Yay!
						return;
					}
					else
					/*if (!current.isRequired())*/// <- the order in which we insert should not matter
					{
						++pos;
						continue;
					}
				}

				this.node.doesNotAllowInsertion(prev, child);
			}

		}
	}

	/**
	 * This method is not supposed to make sure that the body is valid. It's
	 * supposed to make sure that the body does not become invalid. Therefore,
	 * if the body is invalid to begin with, the result is undefined.
	 */
	protected void checkRemoval(Backbone child, ChildDescriptor[] desc)
	{
		forwardDescriptor(child, desc);

		if (current != null)
		{
			// We're removing a node that we cannot ignore
			if (current.isRequired())
				// A required node cannot be removed
				this.node.doesNotAllowRemoval(child);
		}
	}

	/**
	 * This method is not supposed to make sure that the body is valid. It's
	 * supposed to make sure that the body does not become invalid. Therefore,
	 * if the body is invalid to begin with, the result is undefined.
	 */
	protected void checkReplacement(
			Backbone oldChild,
			Backbone newChild,
			ChildDescriptor[] desc)
	{
		forwardDescriptor(oldChild, desc);

		if (current != null)
		{
			// We're replacing a node that we cannot ignore
			if (current.matches(newChild))
			{
				// Yay!
				return;
			}
			else if (current.isRequired())
			{
				// A required node cannot be replaced with a node of different 
				// type
				this.node.doesNotAllowReplacement(oldChild, newChild);
			}
		}

		// Find out if the new node fits in
		if (isIgnored(newChild))
		{
			// It was ok to remove the old node and the new node is an ignored
			// node. Those can be put anywhere
			return;
		}
		/*
		else if (active != null && active.matches(newChild))
		{
			// Yay!
			return;
		}
		*/
		else
		{
			while (true)
			{
				if (pos < desc.length)
				{
					current = desc[pos];
					if (current.matches(newChild))
					{
						// Yay!
						return;
					}
					else
					/*if (!current.isRequired())*/// <- the order in which we insert should not matter
					{
						++pos;
						continue;
					}
				}

				this.node.doesNotAllowReplacement(oldChild, newChild);
			}
		}
	}

	private void forwardDescriptor(Backbone to, ChildDescriptor[] desc) throws AssertionError
	{
		ChildDescriptor active = null;

		if (to == null)
			return;

		for (Backbone n = this.node.getFirstChild();; n = n.getNextSibling())
		{
			if (n == null)
			{
				break;
			}
			else if (isIgnored(n))
			{
				current = null;
			}
			else if (active != null && active.matches(n))
			{
			}
			else
			{
				active = null;
				while (true)
				{
					if (pos >= desc.length)
						throw new AssertionError("Children already messed up!");
					current = desc[pos];
					if (current.matches(n))
					{
						if (current.isMultiple())
							active = current;
						++pos;
						break;
					}
					else
						/*if (!current.isRequired())*/// <- the order in which we insert should not matter
						++pos;
					/*
					else
						throw new AssertionError("Children already messed up!");
					*/
				}
			}

			if (n == to)
				break;
		}
	}
}
