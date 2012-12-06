package org.sweble.wikitext.wom.utils;

import java.io.IOException;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.sweble.wom.WomAttribute;
import org.sweble.wom.WomNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WomPrettyPrinter
{
	private static final String SWC_WOM_PREFIX = "wom";
	
	private static final String SWC_WOM_URI = "http://sweble.org/schema/wom";
	
	// =========================================================================
	
	public static void print(Writer out, WomNode wom) throws ParserConfigurationException, IOException
	{
		Document doc = new WomPrettyPrinter().womToDom(wom);
		
		OutputFormat format = new OutputFormat(doc);
		format.setLineWidth(80);
		format.setIndenting(true);
		format.setIndent(2);
		
		XMLSerializer serializer = new XMLSerializer(out, format);
		serializer.serialize(doc);
	}
	
	// =========================================================================
	
	private final Document doc;
	
	public WomPrettyPrinter() throws ParserConfigurationException
	{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		this.doc = docBuilder.newDocument();
	}
	
	public Document womToDom(WomNode wom)
	{
		doc.appendChild(visit(wom));
		return doc;
	}
	
	private Node visit(WomNode n)
	{
		switch (n.getNodeType())
		{
			case COMMENT:
				return transformComment(n);
			case DOCUMENT:
				return transformDocument(n);
			case ELEMENT:
				return transformElement(n);
			case TEXT:
				return transformText(n);
			case ATTRIBUTE:
				// These are handled directly.
			default:
				throw new InternalError();
		}
	}
	
	private Node transformDocument(WomNode n)
	{
		Element elem = doc.createElement(n.getNodeName());
		elem.setAttribute("xmlns", SWC_WOM_URI);
		//elem.setAttribute("xmlns:" + SWC_WOM_PREFIX, SWC_WOM_URI);
		return transformElement(n, elem);
	}
	
	private Node transformElement(WomNode n)
	{
		Element elem = doc.createElement(n.getNodeName());
		return transformElement(n, elem);
	}
	
	private Node transformElement(WomNode n, Element elem)
	{
		for (WomAttribute a : n.getAttributes())
			elem.setAttributeNode(transformAttribute(a));
		for (WomNode c : n)
			elem.appendChild(visit(c));
		return elem;
	}
	
	private Attr transformAttribute(WomAttribute n)
	{
		Attr attr = doc.createAttribute(n.getName());
		attr.setValue(n.getValue());
		return attr;
	}
	
	private Node transformText(WomNode n)
	{
		Element elem = doc.createElement("text");
		elem.setTextContent(n.getText());
		return elem;
	}
	
	private Node transformComment(WomNode n)
	{
		Element elem = doc.createElement("comment");
		elem.setTextContent(n.getValue());
		return elem;
	}
}
