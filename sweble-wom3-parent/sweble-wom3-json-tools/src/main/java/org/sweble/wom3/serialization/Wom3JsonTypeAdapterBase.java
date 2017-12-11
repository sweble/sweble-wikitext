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

import com.google.gson.*;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.impl.DomImplementationImpl;
import org.sweble.wom3.serialization.ScopeStack.Scope;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.lang.reflect.Type;
import java.util.Map.Entry;
import java.util.Set;

public abstract class Wom3JsonTypeAdapterBase
		implements
		Wom3JsonTypeAdapterInterface
{
	protected static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";

	protected static final String XMLNS_PREFIX = "xmlns";

	protected static final String XMLNS_COLON_PREFIX = XMLNS_PREFIX + ":";

	protected static final String ATTRIBUTE_PREFIX = "@";

	protected static final String SPECIAL_TYPE_PREFIX = "#";

	// =========================================================================

	protected Document doc;

	@Override
	public Document getDoc()
	{
		if (doc == null)
			// Create a generic document
			doc = DomImplementationImpl.get().createDocument(null, null, null);

		return doc;
	}

	@Override
	public void setDoc(Document doc)
	{
		this.doc = doc;
	}

	// =========================================================================

	@Override
	public abstract JsonElement serialize(
			Node src,
			Type typeOfSrc,
			JsonSerializationContext context);

	@Override
	public abstract Node deserialize(
			JsonElement json,
			Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException;

	// =========================================================================

	protected static Scope registerNsDecls(
			ScopeStack scopeStack,
			Scope scope,
			NamedNodeMap attrs)
	{
		for (int i = 0, len = attrs.getLength(); i < len; ++i)
		{
			Node attr = attrs.item(i);
			String attrName = attr.getNodeName();
			if (attrName.startsWith(XMLNS_COLON_PREFIX))
			{
				String prefix = attrName.substring(XMLNS_COLON_PREFIX.length());
				if (scope == null)
					scope = scopeStack.push();
				scope.put(prefix, attr.getNodeValue());
			}
		}
		return scope;
	}

	protected static Scope addPrefixDecls(
			ScopeStack scopeStack,
			Scope scope,
			JsonObject o,
			Node node)
	{
		String localName = node.getLocalName();
		if (localName != null)
		{
			String prefix = node.getPrefix();
			if (XMLNS_PREFIX.equals(prefix))
				// No need to declare xmlns: prefix itself
				return scope;

			String nsUri = node.getNamespaceURI();
			if (nsUri == null)
				nsUri = "";

			if (XMLNS_PREFIX.equals(localName) && XMLNS_URI.equals(nsUri))
				// No need to declare xmlns' namespace as default namespace
				return scope;

			if (prefix == null || prefix.isEmpty())
			{
				if (node.getNodeType() == Node.ATTRIBUTE_NODE)
				{
					// Attributes without prefix are in no namespace (also not the default namespace)
					if (!nsUri.isEmpty())
						throw new JsonParseException(
								"Attribute has namespace URI but no prefix: " + node.getNodeName());
				}
				else
				{
					String xmlns = scopeStack.getXmlns();
					if (!nsUri.equals(xmlns))
					{
						if (scope == null)
							scope = scopeStack.push();
						scope.setXmlns(nsUri);
						String declName = ATTRIBUTE_PREFIX + XMLNS_PREFIX;
						o.add(declName, new JsonPrimitive(nsUri));
					}
				}
			}
			else
			{
				String curNsUri = scopeStack.getNsUriForPrefix(prefix);
				if (!nsUri.equals(curNsUri))
				{
					if (scope == null)
						scope = scopeStack.push();
					scope.put(prefix, nsUri);
					String declName = ATTRIBUTE_PREFIX + XMLNS_COLON_PREFIX + prefix;
					o.add(declName, new JsonPrimitive(nsUri));
				}
			}
		}
		return scope;
	}

	// =========================================================================

	protected static Scope registerNsDecls(
			Set<Entry<String, JsonElement>> entries,
			ScopeStack scopeStack,
			Scope scope)
	{
		for (Entry<String, JsonElement> e : entries)
		{
			String entryName = e.getKey();
			JsonElement entryValue = e.getValue();
			if (entryName.startsWith(ATTRIBUTE_PREFIX))
				scope = registerNsDecl(scopeStack, scope, entryName, entryValue);
		}
		return scope;
	}

	protected static Scope registerNsDecl(
			ScopeStack scopeStack,
			Scope scope,
			String entryName,
			JsonElement entryValue)
	{
		if (!entryValue.isJsonPrimitive())
			throw new JsonParseException("Expected attribute '" + entryName + "' to be a string");

		entryName = entryName.substring(1);
		String valueString = entryValue.getAsString();
		if (entryName.equals(XMLNS_PREFIX))
		{
			if (scope == null)
				scope = scopeStack.push();
			scope.setXmlns(valueString);
		}
		else if (entryName.startsWith(XMLNS_COLON_PREFIX))
		{
			String prefix = entryName.substring(XMLNS_COLON_PREFIX.length());
			if (scope == null)
				scope = scopeStack.push();
			scope.put(prefix, valueString);
		}
		return scope;
	}

	protected static void parseAttribute(
			ScopeStack scopeStack,
			Element elem,
			String entryName,
			JsonElement entryValue)
	{
		if (!entryValue.isJsonPrimitive())
			throw new JsonParseException("Expected attribute '" + entryName + "' to be a string");
		entryName = entryName.substring(1);
		String valueString = entryValue.getAsString();
		int i = entryName.indexOf(':');
		if (i == -1)
		{
			if (XMLNS_PREFIX.equals(entryName))
			{
				elem.setAttributeNS(XMLNS_URI, entryName, valueString);
			}
			else
			{
				elem.setAttribute(entryName, valueString);
			}
		}
		else
		{
			String prefix = entryName.substring(0, i);
			if (XMLNS_PREFIX.equals(prefix))
			{
				elem.setAttributeNS(XMLNS_URI, entryName, valueString);
			}
			else
			{
				String nsUri = scopeStack.getNsUriForPrefix(prefix);
				if (nsUri == null)
					throw new NamespaceException("Namespace URI for prefix '" + prefix + "' not declared");
				elem.setAttributeNS(nsUri, entryName, valueString);
			}
		}
	}

	protected static Element createElement(
			Document doc,
			ScopeStack scopeStack,
			String defaultNsUri,
			String typeQName)
	{
		Element elem;
		int i = typeQName.indexOf(':');
		if (i != -1)
		{
			String prefix = typeQName.substring(0, i);
			String nsUri = scopeStack.getNsUriForPrefix(prefix);
			if (nsUri == null)
				throw new NamespaceException("Namespace URI for prefix '" + prefix + "' not declared");
			elem = doc.createElementNS(nsUri, typeQName);
		}
		else
		{
			if (defaultNsUri == null)
			{
				elem = doc.createElement(typeQName);
			}
			else
			{
				elem = doc.createElementNS(defaultNsUri, typeQName);
			}
		}
		return elem;
	}

	// =========================================================================

	protected static enum ValueTypes
	{
		XML_TEXT
				{
					@Override
					public Node create(Document doc, String value)
					{
						return doc.createTextNode(value);
					}
				},
		ENTITY_REF
				{
					@Override
					public Node create(Document doc, String value)
					{
						return doc.createEntityReference(value);
					}
				},
		CDATA
				{
					@Override
					public Node create(Document doc, String value)
					{
						return doc.createCDATASection(value);
					}
				},
		COMMENT
				{
					@Override
					public Node create(Document doc, String value)
					{
						return doc.createComment(value);
					}
				},
		RTD
				{
					@Override
					public Node create(Document doc, String value)
					{
						Element elem = doc.createElementNS(Wom3Node.WOM_NS_URI, "rtd");
						elem.appendChild(doc.createTextNode(value));
						return elem;
					}
				},
		TEXT
				{
					@Override
					public Node create(Document doc, String value)
					{
						Element elem = doc.createElementNS(Wom3Node.WOM_NS_URI, "text");
						elem.appendChild(doc.createTextNode(value));
						return elem;
					}
				};

		public abstract Node create(Document doc, String value);
	}
}
