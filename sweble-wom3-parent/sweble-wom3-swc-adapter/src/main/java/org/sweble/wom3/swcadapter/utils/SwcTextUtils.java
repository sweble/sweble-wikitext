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
package org.sweble.wom3.swcadapter.utils;

import org.sweble.wom3.Wom3Comment;
import org.sweble.wom3.Wom3DocumentFragment;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3Rtd;
import org.sweble.wom3.Wom3Text;
import org.sweble.wom3.Wom3XmlComment;
import org.sweble.wom3.Wom3XmlText;
import org.sweble.wom3.swcadapter.nodes.SwcValue;

public class SwcTextUtils
{
	public static String womToText(Wom3Node node) throws StringConversionException
	{
		Wom3Node child = node.getFirstChild();
		if (child == null)
			return "";

		if (child == node.getLastChild())
		{
			if (isIgnorable(child))
			{
				return "";
			}
			else if (isContainer(child))
			{
				return womToText(new StringBuilder(), child).toString();
			}
			else if (isText(child))
			{
				return child.getTextContent();
			}
			else
			{
				throw new StringConversionException(child);
			}
		}
		else
		{
			return womToText(new StringBuilder(), node).toString();
		}
	}

	protected static StringBuilder womToText(StringBuilder sb, Wom3Node node) throws StringConversionException
	{
		for (Wom3Node child : node)
		{
			if (isIgnorable(child))
			{
				continue;
			}
			else if (isContainer(child))
			{
				for (Wom3Node containerChild : child)
					womToText(sb, containerChild);
			}
			else if (isText(child))
			{
				sb.append(child.getTextContent());
			}
			else
			{
				throw new StringConversionException(child);
			}
		}
		return sb;
	}

	protected static boolean isText(Wom3Node child)
	{
		return (child instanceof Wom3Text);
	}

	protected static boolean isContainer(Wom3Node child)
	{
		return (child instanceof Wom3DocumentFragment) ||
				(child instanceof SwcValue);
	}

	protected static boolean isIgnorable(Wom3Node child)
	{
		return (child instanceof Wom3Rtd) ||
				(child instanceof Wom3Comment) ||
				(child instanceof Wom3XmlComment) ||
				((child instanceof Wom3XmlText) && (((Wom3XmlText) child).isElementContentWhitespace()));
	}
}
