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
package org.sweble.wikitext.wom.utils;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.wom.utils.StringSetMatcher.Match;
import org.sweble.wom.WomAttribute;
import org.sweble.wom.WomNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WomPrettyPrinter
{
	private static final String SWC_WOM_URI = "http://sweble.org/schema/wom";
	
	// =========================================================================
	
	public static void print(WikiConfig config, Writer out, WomNode wom) throws ParserConfigurationException, IOException
	{
		Document doc = new WomPrettyPrinter(config).womToDom(wom);
		
		OutputFormat format = new OutputFormat(doc);
		format.setLineWidth(80);
		format.setIndenting(true);
		format.setIndent(2);
		
		XMLSerializer serializer = new XMLSerializer(out, format);
		serializer.serialize(doc);
	}
	
	// =========================================================================
	
	private final Document doc;
	
	private final Map<String, String> reverseEntityMap;
	
	private final StringSetMatcher reverseEntityMatcher;
	
	public WomPrettyPrinter(WikiConfig config) throws ParserConfigurationException
	{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		this.doc = docBuilder.newDocument();
		
		Map<String, String> entities = config.getParserConfig().getXmlEntities();
		reverseEntityMatcher = new StringSetMatcher(entities.values());
		reverseEntityMap = new HashMap<String, String>();
		for (Entry<String, String> e : entities.entrySet())
			reverseEntityMap.put(e.getValue(), e.getKey());
	}
	
	public Document womToDom(WomNode wom)
	{
		visit(doc, wom);
		return doc;
	}
	
	private void visit(Node parent, WomNode n)
	{
		switch (n.getNodeType())
		{
			case COMMENT:
				transformComment(parent, n);
				break;
			case DOCUMENT:
				transformDocument(parent, n);
				break;
			case ELEMENT:
				transformElement(parent, n);
				break;
			case TEXT:
				transformText(parent, n);
				break;
			case ATTRIBUTE:
				// These are handled directly.
				// FALL THROUGH!
			default:
				throw new InternalError();
		}
	}
	
	private void transformDocument(Node parent, WomNode n)
	{
		Element elem = doc.createElement(n.getNodeName());
		elem.setAttribute("xmlns", SWC_WOM_URI);
		//elem.setAttribute("xmlns:" + SWC_WOM_PREFIX, SWC_WOM_URI);
		transformElement(n, elem);
		parent.appendChild(elem);
	}
	
	private void transformElement(Node parent, WomNode n)
	{
		Element elem = doc.createElement(n.getNodeName());
		transformElement(n, elem);
		parent.appendChild(elem);
	}
	
	private void transformElement(WomNode n, Element elem)
	{
		if (n.isPreserveSpace())
			elem.setAttribute("xml:space", "preserve");
		for (WomAttribute a : n.getAttributes())
			transformAttribute(elem, a);
		for (WomNode c : n)
			visit(elem, c);
	}
	
	private void transformAttribute(Element parent, WomAttribute n)
	{
		parent.setAttribute(n.getName(), n.getValue());
	}
	
	private void transformText(Node parent, WomNode n)
	{
		/*
		Element elem = doc.createElement("text");
		//elem.setAttribute("xml:space", "preserve");
		elem.setTextContent(n.getText());
		return elem;
		*/
		
		String text = n.getText();
		
		int len = text.length();
		int from = 0;
		int to = 0;
		while (from < len)
		{
			Match m = reverseEntityMatcher.find(text, from);
			to = (m == null) ? len : m.getPos();
			if (to > from)
			{
				String part = text.substring(from, to);
				parent.appendChild(doc.createTextNode(part));
			}
			if (m != null)
			{
				String name = reverseEntityMap.get(m.getMatch());
				parent.appendChild(doc.createEntityReference(name));
				to += m.getMatch().length();
			}
			from = to;
		}
	}
	
	private void transformComment(Node parent, WomNode n)
	{
		/*
		Element elem = doc.createElement("comment");
		//elem.setAttribute("xml:space", "preserve");
		elem.setTextContent(n.getValue());
		return elem;
		*/
		
		parent.appendChild(doc.createComment(n.getValue()));
	}
}
