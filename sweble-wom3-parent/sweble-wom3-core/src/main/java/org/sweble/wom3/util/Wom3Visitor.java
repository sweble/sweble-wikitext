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
package org.sweble.wom3.util;

import java.util.ArrayList;
import java.util.List;

import org.sweble.wom3.Wom3Node;

import de.fau.cs.osr.utils.visitor.VisitorBase;
import de.fau.cs.osr.utils.visitor.VisitorLogic;

public class Wom3Visitor
		extends
			VisitorBase<Wom3Node>
{
	public static final Object REMOVE = new Object();

	// =========================================================================

	public Wom3Visitor()
	{
	}

	public Wom3Visitor(VisitorLogic<Wom3Node> logic)
	{
		super(logic);
	}

	// =========================================================================

	/**
	 * Dispatches to the appropriate visit() method and returns the result of
	 * the visitation. If the given node is <code>null</code> this method
	 * returns immediately with <code>null</code> as result.
	 */
	protected Object dispatch(Wom3Node node)
	{
		if (node == null)
			throw new NullPointerException();
		return resolveAndVisit(node);
	}

	// =========================================================================

	protected void iterate(Wom3Node node)
	{
		if (node == null)
			throw new NullPointerException();
		Wom3Node n = node.getFirstChild();
		while (n != null)
		{
			dispatch(n);
			n = n.getNextSibling();
		}
	}

	/**
	 * Continues iteration after the current node was deleted. The caller has to
	 * remember the next node before deletion and pass it to this method.
	 */
	protected void continueAfterDelete(Wom3Node next)
	{
		while (next != null)
		{
			dispatch(next);
			next = next.getNextSibling();
		}
	}

	protected List<Object> map(Wom3Node node)
	{
		if (node == null)
			throw new NullPointerException();

		List<Object> result = new ArrayList<Object>();
		Wom3Node n = node.getFirstChild();
		while (n != null)
		{
			result.add(dispatch(n));
			n = n.getNextSibling();
		}
		return result;
	}

	/**
	 * Iterates over the children of an WOM node and replaces each child node
	 * with the result of the visitation of the respective child.
	 */
	protected void mapInPlace(Wom3Node node)
	{
		if (node == null)
			throw new NullPointerException();

		Wom3Node n = node.getFirstChild();
		while (n != null)
		{
			Object result = dispatch(n);
			if (result == null)
			{
				throw new NullPointerException();
			}
			else if (result == REMOVE)
			{
				Wom3Node next = n.getNextSibling();
				node.removeChild(n);
				n = next;
			}
			else if (result != n)
			{
				Wom3Node resultNode = (Wom3Node) result;
				node.replaceChild(resultNode, n);
			}
		}
	}
}
