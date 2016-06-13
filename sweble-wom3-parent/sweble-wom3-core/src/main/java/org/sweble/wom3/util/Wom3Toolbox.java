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
import org.sweble.wom3.impl.DocumentImpl;
import org.sweble.wom3.impl.DomImplementationImpl;
import org.w3c.dom.Node;

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

	public static String womToWmFast(Node wom)
	{
		StringBuilder sb = new StringBuilder();
		womToWmFast(sb, wom);
		return sb.toString();
	}

	public static void womToWmFast(StringBuilder sb, Node wom)
	{
		if ((wom == null) || (sb == null))
			throw new NullPointerException();
		womToWmFastRec(sb, wom);
	}

	private static void womToWmFastRec(StringBuilder sb, Node wom)
	{
		if ((wom.getNodeType() == Node.ELEMENT_NODE)
				&& Wom3Node.WOM_NS_URI.equals(wom.getNamespaceURI()))
		{
			String localName = wom.getLocalName();
			if ("rtd".equals(localName) || "text".equals(localName))
			{
				sb.append(wom.getTextContent());
				return;
			}
			else if ("repl".equals(localName))
			{
				// Ignore <repl>...</repl> stuff
				return;
			}
		}

		for (Node child = wom.getFirstChild(); child != null; child = child.getNextSibling())
			womToWmFastRec(sb, child);
	}

	// =========================================================================
	//  WOM query & manipulation
	// =========================================================================

	public static boolean isRtdOrText(Node wom)
	{
		if ((wom != null)
				&& (wom.getNodeType() == Node.ELEMENT_NODE)
				&& Wom3Node.WOM_NS_URI.equals(wom.getNamespaceURI()))
		{
			String localName = wom.getLocalName();
			return ("rtd".equals(localName) || "text".equals(localName));
		}
		else
		{
			return false;
		}
	}

	public static boolean isText(Node wom)
	{
		return isWomElement(wom, "text");
	}

	public static boolean isRtd(Node wom)
	{
		return isWomElement(wom, "rtd");
	}

	public static boolean isWomElement(Node node, String localName)
	{
		return (node != null)
				&& (node.getNodeType() == Node.ELEMENT_NODE)
				&& Wom3Node.WOM_NS_URI.equals(node.getNamespaceURI())
				&& localName.equals(node.getLocalName());
	}

	public static void insertBefore(
			Node parent,
			Node insertBefore,
			Node child)
	{
		if (insertBefore == null)
			parent.appendChild(child);
		else
			parent.insertBefore(child, insertBefore);
	}

	public static Node[] getChildrenByTagName(Node wom, String name)
	{
		ArrayList<Node> result = new ArrayList<Node>();
		getChildrenByTagName(wom, name, result);
		return result.toArray(new Node[0]);
	}

	public static void getChildrenByTagName(
			Node node,
			String name,
			ArrayList<Node> result)
	{
		if (node.getNodeName().equals(name))
			result.add(node);
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
			getChildrenByTagName(child, name, result);
	}
}
