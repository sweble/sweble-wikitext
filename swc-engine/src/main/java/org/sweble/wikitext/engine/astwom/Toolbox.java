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
package org.sweble.wikitext.engine.astwom;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.regex.Pattern;

import lombok.Getter;

import org.sweble.wikitext.lazy.AstNodeTypes;
import org.sweble.wikitext.lazy.parser.Newline;
import org.sweble.wikitext.lazy.parser.XmlElement;
import org.sweble.wikitext.lazy.utils.TextUtils;
import org.sweble.wikitext.lazy.utils.XmlAttribute;
import org.sweble.wikitext.lazy.utils.XmlCharRef;
import org.sweble.wikitext.lazy.utils.XmlEntityRef;
import org.sweble.wikitext.lazy.utils.XmlEntityResolver;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;

public class Toolbox
{
	public static void replaceAstNode(
			NodeList container,
			AstNode oldAstNode,
			AstNode newAstNode) throws AssertionError
	{
		ListIterator<AstNode> i = container.listIterator();
		while (i.hasNext())
		{
			AstNode node = i.next();
			if (node == oldAstNode)
			{
				i.set(newAstNode);
				i = null;
				break;
			}
		}
		if (i != null)
			throw new AssertionError();
	}
	
	public static void removeAstNode(NodeList container, AstNode astNode)
			throws AssertionError
	{
		Iterator<AstNode> i = container.iterator();
		while (i.hasNext())
		{
			AstNode node = i.next();
			if (node == astNode)
			{
				i.remove();
				i = null;
				break;
			}
		}
		if (i != null)
			throw new AssertionError();
	}
	
	public static void insertAstNode(
			NodeList container,
			AstNode astNode,
			AstNode beforeAstNode) throws AssertionError
	{
		ListIterator<AstNode> i = container.listIterator();
		while (i.hasNext())
		{
			AstNode n = i.next();
			if (n == beforeAstNode)
			{
				i.previous();
				i.add(astNode);
				i = null;
				break;
			}
		}
		
		if (i != null)
			throw new AssertionError();
	}
	
	public static void insertAstNodeAfter(
			NodeList container,
			AstNode astNode,
			AstNode afterAstNode) throws AssertionError
	{
		ListIterator<AstNode> i = container.listIterator();
		while (i.hasNext())
		{
			AstNode n = i.next();
			if (n == afterAstNode)
			{
				i.add(astNode);
				i = null;
				break;
			}
		}
		
		if (i != null)
			throw new AssertionError();
	}
	
	public static void prependAstNode(NodeList container, AstNode astNode)
	{
		container.add(0, astNode);
	}
	
	public static void appendAstNode(NodeList container, AstNode astNode)
	{
		container.add(astNode);
	}
	
	public static ListIterator<AstNode> advanceAfter(
			NodeList container,
			AstNode node)
	{
		ListIterator<AstNode> i = container.listIterator();
		while (i.hasNext())
		{
			if (i.next() == node)
				return i;
		}
		return null;
	}
	
	public static ListIterator<AstNode> advanceBefore(
			NodeList container,
			AstNode node)
	{
		ListIterator<AstNode> i = container.listIterator();
		while (i.hasNext())
		{
			if (i.next() == node)
			{
				i.previous();
				return i;
			}
		}
		return null;
	}
	
	public static void removeAstNode(ListIterator<AstNode> i, AstNode astNode)
	{
		while (i.hasNext())
		{
			if (i.next() == astNode)
			{
				i.remove();
				return;
			}
		}
		throw new InternalError();
	}
	
	// =========================================================================
	
	@SuppressWarnings("unchecked")
	public static <T> T expectType(Class<T> type, Object obj)
	{
		if (obj != null && !type.isInstance(obj))
			throw new IllegalArgumentException(
					"Expected object of type " + type.getName() + "!");
		return (T) obj;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T expectType(Class<T> type, Object obj, String argName)
	{
		if (obj != null && !type.isInstance(obj))
			throw new IllegalArgumentException(
					"Expected argument `" + argName + "' to be of type " + type.getName() + "!");
		return (T) obj;
	}
	
	// =========================================================================
	
	public static XmlElement addXmlRtData(XmlElement xmlElement)
	{
		if (xmlElement.isEmpty())
		{
			TextUtils.addRtData(
					(AstNode) xmlElement,
					TextUtils.joinRt('<', xmlElement.getName()),
					TextUtils.joinRt(" />"),
					null);
		}
		else
		{
			TextUtils.addRtData(
					(AstNode) xmlElement,
					TextUtils.joinRt('<', xmlElement.getName()),
					TextUtils.joinRt('>'),
					TextUtils.joinRt("</", xmlElement.getName(), '>'));
		}
		
		for (AstNode attr : xmlElement.getXmlAttributes())
			addXmlAttrRtData((XmlAttribute) attr);
		
		return xmlElement;
	}
	
	public static AstNode addXmlAttrRtData(XmlAttribute xmlAttribute)
	{
		if (xmlAttribute.getHasValue())
		{
			TextUtils.addRtData(
					xmlAttribute,
					TextUtils.joinRt(' ', xmlAttribute.getName(), "=\""),
					TextUtils.joinRt('"'));
		}
		else
		{
			TextUtils.addRtData(
					xmlAttribute,
					TextUtils.joinRt(' ', xmlAttribute.getName()),
					null);
		}
		
		return xmlAttribute;
	}
	
	// =========================================================================
	
	private static final String validTargetRxStr =
			"(?:[^\\u0000-\\u001F\\u007F\\uFFFD<>{}|\\[\\]/]+)";
	
	private static final String validTitleRxStr =
			"(?:[^\\u0000-\\u001F\\u007F\\uFFFD<>{}|\\[\\]:/]+)";
	
	@Getter(lazy = true)
	private static final Pattern validTargetRx = Pattern.compile(validTargetRxStr);
	
	@Getter(lazy = true)
	private static final Pattern validTitleRx = Pattern.compile(validTitleRxStr);
	
	private static final String validPathRxStr =
			"(?:(?:" + validTitleRxStr + "/)*" + validTitleRxStr + "?)";
	
	@Getter(lazy = true)
	private static final Pattern validPathRx = Pattern.compile(validPathRxStr);
	
	// =========================================================================
	
	public static String checkValidTitle(String title)
			throws UnsupportedOperationException,
			IllegalArgumentException
	{
		if (title == null)
			throw new UnsupportedOperationException("Cannot remove title attribute");
		
		if (!getValidTitleRx().matcher(title).matches())
			throw new IllegalArgumentException("Invalid title");
		
		return title;
	}
	
	public static String checkValidCategory(String category)
			throws UnsupportedOperationException,
			IllegalArgumentException
	{
		if (category == null)
			throw new UnsupportedOperationException("Cannot remove category attribute");
		
		if (!getValidTitleRx().matcher(category).matches())
			throw new IllegalArgumentException("Invalid category");
		
		return category;
	}
	
	public static String checkValidNamespace(String namespace)
			throws IllegalArgumentException
	{
		if (namespace == null || namespace.isEmpty())
			return null;
		
		if (!getValidTitleRx().matcher(namespace).matches())
			throw new IllegalArgumentException("Invalid namespace");
		
		return namespace;
	}
	
	public static String checkValidPath(String path)
			throws IllegalArgumentException
	{
		if (path == null || path.isEmpty())
			return null;
		
		if (path == null || !getValidPathRx().matcher(path).matches())
			throw new IllegalArgumentException("Invalid path");
		
		int l = path.length();
		if (path.charAt(l - 1) == '/')
		{
			--l;
			return path.substring(0, l);
		}
		else
			return path;
	}
	
	public static String checkValidTarget(String target)
	{
		if (target == null)
			throw new UnsupportedOperationException("Cannot remove target attribute");
		
		if (!getValidTargetRx().matcher(target).matches())
			throw new IllegalArgumentException("Invalid target");
		
		return target;
	}
	
	// =========================================================================
	
	public static String toText(XmlEntityResolver entityResolver, AstNode n)
	{
		switch (n.getNodeType())
		{
			case AstNode.NT_TEXT:
				return ((Text) n).getContent();
				
			case AstNodeTypes.NT_NEWLINE:
				return ((Newline) n).getContent();
				
			case AstNodeTypes.NT_XML_COMMENT:
			case AstNodeTypes.NT_IGNORED:
				return "";
				
			case AstNodeTypes.NT_XML_CHAR_REF:
				return new String(Character.toChars(((XmlCharRef) n).getCodePoint()));
				
			case AstNodeTypes.NT_XML_ENTITY_REF:
				return entityResolver.resolveXmlEntity(((XmlEntityRef) n).getName());
				
			default:
				throw new IllegalArgumentException();
		}
	}
}
