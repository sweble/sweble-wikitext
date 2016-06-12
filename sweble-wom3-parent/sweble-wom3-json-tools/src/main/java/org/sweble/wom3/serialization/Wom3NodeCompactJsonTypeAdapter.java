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
package org.sweble.wom3.serialization;

import java.lang.reflect.Type;
import java.util.Map.Entry;
import java.util.Set;

import org.sweble.wom3.serialization.ScopeStack.Scope;
import org.sweble.wom3.util.Wom3Toolbox;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

public class Wom3NodeCompactJsonTypeAdapter
		extends
			Wom3JsonTypeAdapterBase
{
	private static final String ELEMENT_NAME_PREFIX = "!";

	private static final String TYPE_COMMENT = SPECIAL_TYPE_PREFIX + "c";

	private static final String TYPE_CDATA = SPECIAL_TYPE_PREFIX + "cd";

	public static final String TYPE_ENTITY_REF = SPECIAL_TYPE_PREFIX + "er";

	public static final String TYPE_TEXT = SPECIAL_TYPE_PREFIX + "t";

	public static final String TYPE_RTD = SPECIAL_TYPE_PREFIX + "r";

	// =========================================================================

	@Override
	public JsonElement serialize(
			Node src,
			Type typeOfSrc,
			JsonSerializationContext context)
	{
		return toJson(src, true, new ScopeStack());
	}

	private static JsonElement toJson(
			Node node,
			boolean first,
			ScopeStack scopeStack)
	{
		short nodeType = node.getNodeType();
		switch (nodeType)
		{
			case Node.ELEMENT_NODE:
			{
				JsonObject o = null;

				if (Wom3Toolbox.isRtdOrText(node))
					o = textOrRtdToJson(node);

				if (o == null)
					o = elemToJson(node, scopeStack);

				return o;
			}

			case Node.TEXT_NODE:
				return new JsonPrimitive(node.getNodeValue());

			case Node.ENTITY_REFERENCE_NODE:
				return valueNodeToJson(node.getNodeValue(), TYPE_ENTITY_REF);

			case Node.CDATA_SECTION_NODE:
				return valueNodeToJson(node.getNodeValue(), TYPE_CDATA);

			case Node.COMMENT_NODE:
				return valueNodeToJson(node.getNodeValue(), TYPE_COMMENT);

			case Node.DOCUMENT_NODE:
				if (first)
					return toJson(((Document) node).getDocumentElement(), false, scopeStack);

				// FALL THROUGH

			case Node.ATTRIBUTE_NODE:
			case Node.ENTITY_NODE:
			case Node.PROCESSING_INSTRUCTION_NODE:
			case Node.DOCUMENT_TYPE_NODE:
			case Node.DOCUMENT_FRAGMENT_NODE:
			case Node.NOTATION_NODE:
			default:
				throw new UnsupportedNodeException(node);
		}
	}

	private static JsonObject elemToJson(Node node, ScopeStack scopeStack)
	{
		JsonObject o = new JsonObject();
		Scope scope = null;

		scope = addPrefixDecls(scopeStack, scope, o, node);

		NamedNodeMap attrs = node.getAttributes();
		if (attrs != null)
		{
			// Document fragments don't have attributes

			scope = registerNsDecls(scopeStack, scope, attrs);

			for (int i = 0, len = attrs.getLength(); i < len; ++i)
			{
				Node attr = attrs.item(i);
				scope = addPrefixDecls(scopeStack, scope, o, attr);
				o.add(ATTRIBUTE_PREFIX + attr.getNodeName(), new JsonPrimitive(attr.getNodeValue()));
			}
		}

		JsonArray jsonChildren = new JsonArray();
		NodeList children = node.getChildNodes();
		for (int i = 0, len = children.getLength(); i < len; ++i)
		{
			Node child = children.item(i);
			jsonChildren.add(toJson(child, false, scopeStack));
		}
		o.add(ELEMENT_NAME_PREFIX + node.getNodeName(), jsonChildren);

		if (scope != null)
			// Pop scope if we opened one
			scopeStack.pop();

		return o;
	}

	private static JsonObject textOrRtdToJson(Node node)
	{
		String name = null;
		if (Wom3Toolbox.isRtd(node))
			name = TYPE_RTD;
		else if (Wom3Toolbox.isText(node))
			name = TYPE_TEXT;
		else
			throw new InternalError();

		return valueNodeToJson(node.getTextContent(), name);
	}

	private static JsonObject valueNodeToJson(String value, String name)
	{
		JsonObject o = new JsonObject();
		o.addProperty(name, value);
		return o;
	}

	// =========================================================================

	@Override
	public Node deserialize(
			JsonElement json,
			Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException
	{
		Document doc = getDoc();
		DocumentFragment fragment = doc.createDocumentFragment();
		fragment.appendChild(fromJson(doc, json, new ScopeStack()));
		return fragment;
	}

	private static Node fromJson(
			Document doc,
			JsonElement json,
			ScopeStack scopeStack)
	{
		if (json.isJsonPrimitive())
			return doc.createTextNode(json.getAsString());

		// Get node object
		if (!json.isJsonObject())
			throw new JsonParseException("Expected JsonObject or JsonPrimitive");
		JsonObject o = json.getAsJsonObject();

		Scope scope = null;
		String elemQName = null;
		ValueTypes valueType = null;
		JsonElement nodeValue = null;

		// Get node type
		Set<Entry<String, JsonElement>> entries = o.entrySet();
		for (Entry<String, JsonElement> e : entries)
		{
			String entryName = e.getKey();
			final JsonElement entryValue = e.getValue();
			if (entryName.startsWith(ELEMENT_NAME_PREFIX))
			{
				if (elemQName != null)
					throw new JsonParseException("Node name field occurred repeatedly");

				elemQName = entryName.substring(1);
				nodeValue = entryValue;
			}
			else if (entryName.startsWith(SPECIAL_TYPE_PREFIX))
			{
				if (valueType != null)
					throw new JsonParseException("Type name field occurred repeatedly");

				if (entryName.equals(TYPE_RTD))
				{
					valueType = ValueTypes.RTD;
				}
				else if (entryName.equals(TYPE_TEXT))
				{
					valueType = ValueTypes.TEXT;
				}
				else if (entryName.equals(TYPE_ENTITY_REF))
				{
					valueType = ValueTypes.ENTITY_REF;
				}
				else if (entryName.equals(TYPE_COMMENT))
				{
					valueType = ValueTypes.COMMENT;
				}
				else if (entryName.equals(TYPE_CDATA))
				{
					valueType = ValueTypes.CDATA;
				}
				else
					throw new JsonParseException("Unknown special type '" + entryName + "'");

				nodeValue = entryValue;
			}
			else if (entryName.startsWith(ATTRIBUTE_PREFIX))
			{
				scope = registerNsDecl(scopeStack, scope, entryName, entryValue);
			}
			else
				throw new JsonParseException("Unexpected field: '" + entryName + "'");
		}

		Element elem = null;
		String defaultNsUri = null;

		if (elemQName != null)
		{
			defaultNsUri = scopeStack.getXmlns();
			elem = createElement(doc, scopeStack, defaultNsUri, elemQName);
		}
		else if (valueType == null)
			throw new JsonParseException("Missing type name or node name");

		for (Entry<String, JsonElement> e : entries)
		{
			String entryName = e.getKey();
			JsonElement entryValue = e.getValue();
			if (entryName.startsWith(ATTRIBUTE_PREFIX))
			{
				if (elem == null)
					throw new JsonParseException("Value type '" + valueType + "' cannot have attributes");

				parseAttribute(scopeStack, elem, entryName, entryValue);
			}
		}

		Node result;
		if (elem != null)
		{
			if (!nodeValue.isJsonArray())
				throw new JsonParseException("Expected member '" + ELEMENT_NAME_PREFIX + elemQName + "' to be an array.");

			JsonArray children = nodeValue.getAsJsonArray();
			for (JsonElement child : children)
				elem.appendChild(fromJson(doc, child, scopeStack));

			result = elem;
		}
		else
		{
			result = valueType.create(doc, nodeValue.getAsString());
		}

		if (scope != null)
			scopeStack.pop();

		return result;
	}
}
