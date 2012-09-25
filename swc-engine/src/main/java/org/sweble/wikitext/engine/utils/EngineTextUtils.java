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

package org.sweble.wikitext.engine.utils;

import java.util.ListIterator;

import org.sweble.wikitext.parser.AstNodeTypes;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;
import de.fau.cs.osr.utils.StringUtils;

public class EngineTextUtils
{
	
	public static AstNode trim(AstNode n)
	{
		return trimRight(trimLeft(n));
	}
	
	public static AstNode trimLeft(AstNode n)
	{
		switch (n.getNodeType())
		{
			case AstNode.NT_NODE_LIST:
			{
				NodeList l = (NodeList) n;
				ListIterator<AstNode> i = l.listIterator();
				outer: while (i.hasNext())
				{
					AstNode item = i.next();
					switch (item.getNodeType())
					{
						case AstNode.NT_TEXT:
							Text t = (Text) item;
							String text = t.getContent();
							if (text.isEmpty())
							{
								i.remove();
								continue;
							}
							
							String trimmed = StringUtils.trimLeft(text);
							if (trimmed.equals(text))
								break outer;
							
							if (trimmed.isEmpty())
							{
								i.remove();
								continue;
							}
							else
							{
								t.setContent(trimmed);
								break outer;
							}
							
						case AstNodeTypes.NT_IGNORED:
						case AstNodeTypes.NT_XML_COMMENT:
							continue;
							
						default:
							break outer;
					}
				}
				return n;
			}
			case AstNode.NT_TEXT:
			{
				Text t = (Text) n;
				t.setContent(StringUtils.trimLeft(t.getContent()));
				return n;
			}
			default:
				return n;
		}
	}
	
	public static AstNode trimRight(AstNode n)
	{
		switch (n.getNodeType())
		{
			case AstNode.NT_NODE_LIST:
			{
				NodeList l = (NodeList) n;
				ListIterator<AstNode> i = l.listIterator(l.size());
				outer: while (i.hasPrevious())
				{
					AstNode item = i.previous();
					switch (item.getNodeType())
					{
						case AstNode.NT_TEXT:
							Text t = (Text) item;
							String text = t.getContent();
							if (text.isEmpty())
							{
								i.remove();
								continue;
							}
							
							String trimmed = StringUtils.trimRight(text);
							if (trimmed.equals(text))
								break outer;
							
							if (trimmed.isEmpty())
							{
								i.remove();
								continue;
							}
							else
							{
								t.setContent(trimmed);
								break outer;
							}
							
						case AstNodeTypes.NT_IGNORED:
						case AstNodeTypes.NT_XML_COMMENT:
							continue;
							
						default:
							break outer;
					}
				}
				return n;
			}
			case AstNode.NT_TEXT:
			{
				Text t = (Text) n;
				t.setContent(StringUtils.trimRight(t.getContent()));
				return n;
			}
			default:
				return n;
		}
	}
}
