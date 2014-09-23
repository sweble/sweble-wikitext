/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sweble.wom3.swcadapter.utils;

import org.sweble.wom3.Wom3Comment;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3Rtd;
import org.sweble.wom3.Wom3Text;
import org.sweble.wom3.Wom3XmlComment;
import org.sweble.wom3.Wom3XmlText;

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
				return "";
			
			if (!isText(child))
				throw new StringConversionException(child);
			
			return child.getTextContent();
		}
		else
		{
			StringBuffer sb = new StringBuffer();
			for (; child != null; child = child.getNextSibling())
			{
				if (isIgnorable(child))
					continue;
				
				if (!isText(child))
					throw new StringConversionException(child);
				
				sb.append(child.getTextContent());
			}
			
			return sb.toString();
		}
	}
	
	protected static boolean isText(Wom3Node child)
	{
		return (child instanceof Wom3Text);
	}
	
	protected static boolean isIgnorable(Wom3Node child)
	{
		return (child instanceof Wom3Rtd) ||
				(child instanceof Wom3Comment) ||
				(child instanceof Wom3XmlComment) ||
				((child instanceof Wom3XmlText) && (((Wom3XmlText) child).isElementContentWhitespace()));
	}
}
