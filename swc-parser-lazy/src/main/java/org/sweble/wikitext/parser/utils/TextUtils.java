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

package org.sweble.wikitext.parser.utils;

import java.util.ArrayList;

import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WikitextNodeFactoryImpl;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtXmlCharRef;
import org.sweble.wikitext.parser.nodes.WtXmlEntityRef;

import de.fau.cs.osr.utils.StringUtils;

public final class TextUtils
{
	public static WtNodeList stringToAst(String text)
	{
		return stringToAst(text, true);
	}
	
	public static WtNodeList stringToAst(String text, boolean forAttribute)
	{
		WtNodeList list = WikitextNodeFactoryImpl.list_();
		
		if (text == null)
			return list;
		
		int n = text.length();
		int i = 0;
		int j = 0;
		for (; j < n; ++j)
		{
			char ch = text.charAt(j);
			switch (ch)
			{
				case ' ':
				case '\n':
				case '\t':
					break;
				case '<':
					if (j > i)
						list.add(WikitextNodeFactoryImpl.text_(text.substring(i, j)));
					list.add(xmlEntity("lt", "<"));
					i = j + 1;
					break;
				case '>':
					if (!forAttribute)
						break;
					if (j > i)
						list.add(WikitextNodeFactoryImpl.text_(text.substring(i, j)));
					list.add(xmlEntity("gt", ">"));
					i = j + 1;
					break;
				case '&':
					if (j > i)
						list.add(WikitextNodeFactoryImpl.text_(text.substring(i, j)));
					list.add(xmlEntity("amp", "&"));
					i = j + 1;
					break;
				case '\'':
					// &apos; cannot safely be used, see wikipedia
					if (j > i)
						list.add(WikitextNodeFactoryImpl.text_(text.substring(i, j)));
					list.add(wtXmlCharRef(39));
					i = j + 1;
					break;
				case '"':
					if (!forAttribute)
						break;
					if (j > i)
						list.add(WikitextNodeFactoryImpl.text_(text.substring(i, j)));
					list.add(xmlEntity("quot", "\""));
					i = j + 1;
					break;
				default:
					if ((ch >= 0 && ch < 0x20) || (ch == 0xFE))
					{
						if (j > i)
							list.add(WikitextNodeFactoryImpl.text_(text.substring(i, j)));
						list.add(wtXmlCharRef(ch));
						i = j + 1;
						continue;
					}
					else if (Character.isHighSurrogate(ch))
					{
						++i;
						if (i < n)
						{
							char ch2 = text.charAt(i);
							if (Character.isLowSurrogate(ch2))
							{
								int codePoint = Character.toCodePoint(ch, ch2);
								switch (Character.getType(codePoint))
								{
									case Character.CONTROL:
									case Character.PRIVATE_USE:
									case Character.UNASSIGNED:
										if (j > i)
											list.add(WikitextNodeFactoryImpl.text_(text.substring(i, j)));
										list.add(wtXmlCharRef(codePoint));
										i = j + 1;
										break;
									
									default:
										break;
								}
								
								continue;
							}
						}
					}
					else if (!Character.isLowSurrogate(ch))
					{
						continue;
					}
					
					// No low surrogate followed or only low surrogate
					throw new IllegalArgumentException("String contains isolated surrogates!");
			}
		}
		
		if (i != j)
			list.add(WikitextNodeFactoryImpl.text_(text.substring(i, j)));
		
		return list;
	}
	
	// =========================================================================
	
	/**
	 * @deprecated
	 */
	public static WtXmlCharRef wtXmlCharRef(int codePoint)
	{
		WtXmlCharRef wtXmlCharRef = WikitextNodeFactoryImpl.charRef_(codePoint);
		setXmlCharRef(wtXmlCharRef, codePoint);
		return wtXmlCharRef;
	}
	
	/**
	 * @deprecated
	 */
	public static void setXmlCharRef(WtXmlCharRef wtXmlCharRef, int codePoint)
	{
		wtXmlCharRef.setCodePoint(codePoint);
		wtXmlCharRef.setRtd(StringUtils.hexCharRef(codePoint));
	}
	
	/**
	 * @deprecated
	 */
	public static WtXmlEntityRef xmlEntity(String name, String resolved)
	{
		WtXmlEntityRef xmlEntityRef = WikitextNodeFactoryImpl.entityRef_(name, resolved);
		setXmlEntityRef(xmlEntityRef, name);
		return xmlEntityRef;
	}
	
	/**
	 * @deprecated
	 */
	private static void setXmlEntityRef(WtXmlEntityRef xmlEntityRef, String name)
	{
		xmlEntityRef.setName(name);
		xmlEntityRef.setRtd(StringUtils.entityRef(name));
	}
	
	// =========================================================================
	
	public static WtNodeList trim(WtNodeList nodes)
	{
		ArrayList<WtNode> result = new ArrayList<WtNode>(nodes);
		
		trimLeft(result);
		trimRight(result);
		
		return WikitextNodeFactoryImpl.list_(result);
	}
	
	public static WtNodeList trimLeft(WtNodeList nodes)
	{
		ArrayList<WtNode> result = new ArrayList<WtNode>(nodes);
		
		trimLeft(result);
		
		return WikitextNodeFactoryImpl.list_(result);
	}
	
	public static WtNodeList trimRight(WtNodeList nodes)
	{
		ArrayList<WtNode> result = new ArrayList<WtNode>(nodes);
		
		trimRight(result);
		
		return WikitextNodeFactoryImpl.list_(result);
	}
	
	public static WtNodeList trimAndPad(WtNodeList nodes, int spaces)
	{
		ArrayList<WtNode> result = new ArrayList<WtNode>(nodes);
		
		trimLeft(result);
		trimRight(result);
		
		if (spaces <= 0)
			return WikitextNodeFactoryImpl.list_(result);
		
		return pad(result, spaces);
	}
	
	public static void trimLeft(ArrayList<WtNode> result)
	{
		int i = 0;
		while (i < result.size())
		{
			switch (result.get(i).getNodeType())
			{
				case WtNode.NT_TEXT:
				{
					WtText stringNode = (WtText) result.get(i);
					String trimmed = StringUtils.trimLeft(stringNode.getContent());
					if (trimmed != stringNode.getContent())
					{
						if (trimmed.isEmpty())
						{
							result.remove(i);
							continue;
						}
						else
						{
							result.set(i, WikitextNodeFactoryImpl.text_(trimmed));
							break;
						}
					}
					else
					{
						break;
					}
				}
				
				case WtNode.NT_XML_COMMENT:
					++i;
					continue;
					
				default:
					break;
			}
			
			break;
		}
	}
	
	public static void trimRight(ArrayList<WtNode> result)
	{
		int i = result.size() - 1;
		while (i >= 0)
		{
			switch (result.get(i).getNodeType())
			{
				case WtNode.NT_TEXT:
				{
					WtText stringNode = (WtText) result.get(i);
					String trimmed = StringUtils.trimRight(stringNode.getContent());
					if (trimmed != stringNode.getContent())
					{
						if (trimmed.isEmpty())
						{
							result.remove(i--);
							continue;
						}
						else
						{
							result.set(i, WikitextNodeFactoryImpl.text_(trimmed));
							break;
						}
					}
					else
					{
						break;
					}
				}
				
				case WtNode.NT_XML_COMMENT:
					--i;
					continue;
					
				default:
					break;
			}
			
			break;
		}
	}
	
	// =========================================================================
	
	public static WtNodeList pad(ArrayList<WtNode> result, int spaces)
	{
		if (spaces <= 0)
			return WikitextNodeFactoryImpl.list_(result);
		
		if (result.isEmpty())
		{
			result.add(WikitextNodeFactoryImpl.text_(StringUtils.strrep(' ', spaces * 2)));
		}
		else
		{
			String spaced;
			String spacesString = StringUtils.strrep(' ', spaces);
			
			// -- before
			
			WtText before = null;
			if (result.get(0).isNodeType(WtNode.NT_TEXT))
				before = (WtText) result.remove(0);
			spaced = "";
			if (before != null)
				spaced = before.getContent();
			
			spaced = spacesString + spaced;
			result.add(0, WikitextNodeFactoryImpl.text_(spaced));
			
			// -- after
			
			WtText after = null;
			int i = result.size() - 1;
			if (result.get(i).isNodeType(WtNode.NT_TEXT))
				after = (WtText) result.remove(i);
			spaced = "";
			if (after != null)
				spaced = after.getContent();
			
			spaced = spaced + spacesString;
			result.add(WikitextNodeFactoryImpl.text_(spaced));
		}
		
		return WikitextNodeFactoryImpl.list_(result);
	}
}
