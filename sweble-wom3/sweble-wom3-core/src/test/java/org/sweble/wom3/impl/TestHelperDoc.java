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

import org.sweble.wom3.Wom3Attribute;
import org.sweble.wom3.Wom3ElementNode;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3XmlComment;
import org.sweble.wom3.Wom3XmlText;

public class TestHelperDoc
{
	private static DocumentImpl doc;

	static
	{
		DomImplementationImpl domImpl = DomImplementationImpl.get();
		doc = domImpl.createDocument(Wom3Node.WOM_NS_URI, "article", null);
	}

	public static Wom3Attribute genAttr(String name, String value)
	{
		Wom3Attribute a = doc.createAttribute(name);
		a.setValue(value);
		return a;
	}

	public static Wom3ElementNode genElem(String name)
	{
		return (Wom3ElementNode) doc.createElementNS(Wom3Node.WOM_NS_URI, name);
	}

	public static Wom3XmlText genXmlText(String text)
	{
		return (Wom3XmlText) doc.createTextNode(text);
	}

	public static Wom3XmlComment genComment(String text)
	{
		return (Wom3XmlComment) doc.createComment(text);
	}
}
