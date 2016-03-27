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

import org.sweble.wom3.Wom3Document;
import org.sweble.wom3.serialization.ScopeStack.Scope;
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

public class Wom3NodeJsonTypeAdapter
		extends
			Wom3JsonTypeAdapterBase
{
	private static final String PROPERTY_PREFIX = "!";

	private static final String PROPERTY_TYPE = PROPERTY_PREFIX + "type";

	private static final String PROPERTY_VALUE = PROPERTY_PREFIX + "value";

	private static final String PROPERTY_CHILDREN = PROPERTY_PREFIX + "children";

	private static final String TYPE_ENTITY_REF = SPECIAL_TYPE_PREFIX + "entity-reference";

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
		switch (node.getNodeType())
		{
			case Node.ELEMENT_NODE:
				return elementToJson(node, scopeStack);

			case Node.TEXT_NODE:
			case Node.CDATA_SECTION_NODE:
			case Node.COMMENT_NODE:
			{
				JsonObject o = new JsonObject();
				o.add(PROPERTY_TYPE, new JsonPrimitive(node.getNodeName()));
				o.add(PROPERTY_VALUE, new JsonPrimitive(node.getNodeValue()));
				return o;
			}

			case Node.ENTITY_REFERENCE_NODE:
			{
				JsonObject o = new JsonObject();
				o.add(PROPERTY_TYPE, new JsonPrimitive(TYPE_ENTITY_REF));
				o.add(PROPERTY_VALUE, new JsonPrimitive(node.getNodeValue()));
				return o;
			}

			case Node.DOCUMENT_NODE:
				if (first)
					return toJson(((Document) node).getDocumentElement(), false, scopeStack);

				// FALL THROUGH

			case Node.ATTRIBUTE_NODE:
			case Node.ENTITY_NODE:
			case Node.PROCESSING_INSTRUCTION_NODE:
			case Node.DOCUMENT_TYPE_NODE:
			case Node.NOTATION_NODE:
			default:
				throw new UnsupportedNodeException(node);
		}
	}

	private static JsonElement elementToJson(Node node, ScopeStack scopeStack)
	{
		Scope scope = null;

		NamedNodeMap attrs = node.getAttributes();
		if (attrs != null)
			// Document fragments don't have attributes
			scope = registerNsDecls(scopeStack, scope, attrs);

		// Write node type and required required prefix declarations
		JsonObject o = new JsonObject();
		o.add(PROPERTY_TYPE, new JsonPrimitive(node.getNodeName()));
		scope = addPrefixDecls(scopeStack, scope, o, node);

		// Write node attributes and required prefix declarations
		if (attrs != null)
		{
			// Document fragments don't have attributes
			for (int i = 0, len = attrs.getLength(); i < len; ++i)
			{
				Node attr = attrs.item(i);
				scope = addPrefixDecls(scopeStack, scope, o, attr);
				o.add(ATTRIBUTE_PREFIX + attr.getNodeName(), new JsonPrimitive(attr.getNodeValue()));
			}
		}

		// Write children
		JsonArray jsonChildren = new JsonArray();
		NodeList children = node.getChildNodes();
		for (int i = 0, len = children.getLength(); i < len; ++i)
		{
			Node child = children.item(i);
			jsonChildren.add(toJson(child, false, scopeStack));
		}
		o.add(PROPERTY_CHILDREN, jsonChildren);

		if (scope != null)
			// Pop scope if we opened one
			scopeStack.pop();

		return o;
	}

	// =========================================================================

	@Override
	public Node deserialize(
			JsonElement json,
			Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException
	{
		final Wom3Document doc = getDoc();
		DocumentFragment fragment = doc.createDocumentFragment();
		fragment.appendChild(fromJson(doc, json, new ScopeStack()));
		return fragment;
	}

	private static Node fromJson(
			Wom3Document doc,
			JsonElement json,
			ScopeStack scopeStack)
	{
		// Get node object
		if (!json.isJsonObject())
			throw new JsonParseException("Expected JsonObject");
		JsonObject o = json.getAsJsonObject();
		Set<Entry<String, JsonElement>> entries = o.entrySet();

		// Get node type
		JsonElement type = o.get(PROPERTY_TYPE);
		if (type == null)
			throw new JsonParseException("Missing member '" + PROPERTY_TYPE + "'");
		if (!type.isJsonPrimitive())
			throw new JsonParseException("Expected member '" + PROPERTY_TYPE + "' to be a string");

		Element elem = null;
		ValueTypes valueType = null;
		Scope scope = null;
		String defaultNsUri = null;

		String typeQName = type.getAsString();
		if (typeQName.startsWith(SPECIAL_TYPE_PREFIX))
		{
			if (typeQName.equals("#text"))
			{
				valueType = ValueTypes.XML_TEXT;
			}
			else if (typeQName.equals(TYPE_ENTITY_REF))
			{
				valueType = ValueTypes.ENTITY_REF;
			}
			else if (typeQName.equals("#cdata-section"))
			{
				valueType = ValueTypes.CDATA;
			}
			else if (typeQName.equals("#comment"))
			{
				valueType = ValueTypes.COMMENT;
			}
			else
			{
				throw new JsonParseException("Unknown special type '" + typeQName + "'");
			}
		}
		else
		{
			scope = registerNsDecls(entries, scopeStack, scope);

			defaultNsUri = scopeStack.getXmlns();
			if (defaultNsUri != null && defaultNsUri.isEmpty())
				defaultNsUri = null;

			elem = createElement(doc, scopeStack, defaultNsUri, typeQName);
		}

		int countType = 0;
		int countChildren = 0;
		String nodeValue = null;

		for (Entry<String, JsonElement> e : entries)
		{
			String entryName = e.getKey();
			JsonElement entryValue = e.getValue();
			if (entryName.startsWith(ATTRIBUTE_PREFIX))
			{
				if (elem == null)
					throw new JsonParseException("Node type '" + typeQName + "' cannot have attributes");

				parseAttribute(scopeStack, elem, entryName, entryValue);
			}
			else if (entryName.startsWith(PROPERTY_PREFIX))
			{
				if (entryName.equals(PROPERTY_TYPE))
				{
					++countType;
					if (countType > 1)
						throw new JsonParseException("Member '" + PROPERTY_TYPE + "' occurred repeatedly");
					// Ignore, we handled that field already
				}
				else if (entryName.equals(PROPERTY_VALUE))
				{
					if (nodeValue != null)
						throw new JsonParseException("Member '" + PROPERTY_VALUE + "' occurred repeatedly");
					if (valueType == null)
						throw new JsonParseException("Type '" + typeQName + "' cannot have a value");
					nodeValue = entryValue.getAsString();
				}
				else if (entryName.equals(PROPERTY_CHILDREN))
				{
					++countChildren;
					if (countChildren > 1)
						throw new JsonParseException("Member '" + PROPERTY_CHILDREN + "' occurred repeatedly");
					if (valueType != null)
						throw new JsonParseException("Type '" + typeQName + "' cannot have children");
					if (!entryValue.isJsonArray())
						throw new JsonParseException("Expected member '" + entryName + "' to be an array.");
					JsonArray children = entryValue.getAsJsonArray();
					for (JsonElement child : children)
						elem.appendChild(fromJson(doc, child, scopeStack));
				}
			}
			else
				throw new JsonParseException("Unexpected field: '" + entryName + "'");
		}

		if (scope != null)
			scopeStack.pop();

		return (valueType != null) ?
				valueType.create(doc, nodeValue) :
				elem;
	}
}
