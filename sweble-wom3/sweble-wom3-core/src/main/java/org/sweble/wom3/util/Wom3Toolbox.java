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

import org.sweble.wom3.Wom3Document;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3Repl;
import org.sweble.wom3.Wom3Rtd;
import org.sweble.wom3.Wom3Text;
import org.sweble.wom3.impl.DocumentImpl;
import org.sweble.wom3.impl.DomImplementationImpl;

public class Wom3Toolbox
{
	private Wom3Toolbox()
	{
	}

	// =========================================================================
	//  Singletons
	// =========================================================================

	public static DomImplementationImpl getWomDomImpl()
	{
		return DomImplementationImpl.get();
	}

	// =========================================================================
	//  WOM helpers
	// =========================================================================

	public static Wom3Document createDocument()
	{
		return new DocumentImpl(getWomDomImpl());
	}

	public static Wom3Document createDocument(String docElemTagName)
	{
		return getWomDomImpl().createDocument(
				Wom3Node.WOM_NS_URI, docElemTagName, null);
	}

	public static String womToWmFast(Wom3Node wom)
	{
		StringBuilder sb = new StringBuilder();
		womToWmFast(sb, wom);
		return sb.toString();
	}

	public static void womToWmFast(StringBuilder sb, Wom3Node wom)
	{
		if (wom instanceof Wom3Rtd || wom instanceof Wom3Text)
		{
			sb.append(wom.getTextContent());
		}
		else if (wom instanceof Wom3Repl)
		{
			// Ignore <repl>...</repl> stuff
		}
		else
		{
			for (Wom3Node c : wom)
				womToWmFast(sb, c);
		}
	}

	// =========================================================================
	//  WOM query & manipulation
	// =========================================================================

	public static void insertBefore(
			Wom3Node parent,
			Wom3Node insertBefore,
			Wom3Node child)
	{
		if (insertBefore == null)
			parent.appendChild(child);
		else
			parent.insertBefore(child, insertBefore);
	}

	public static Wom3Node[] getChildrenByTagName(Wom3Node wom, String name)
	{
		ArrayList<Wom3Node> result = new ArrayList<Wom3Node>();
		getChildrenByTagName(wom, name, result);
		return result.toArray(new Wom3Node[0]);
	}

	public static void getChildrenByTagName(
			Wom3Node node,
			String name,
			ArrayList<Wom3Node> result)
	{
		if (node.getNodeName().equals(name))
			result.add(node);
		for (Wom3Node c : node)
			getChildrenByTagName(c, name, result);
	}
}
