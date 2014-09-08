/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.util;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.sweble.wom3.Wom3Document;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3Repl;
import org.sweble.wom3.Wom3Rtd;
import org.sweble.wom3.Wom3Text;
import org.sweble.wom3.impl.DocumentImpl;
import org.sweble.wom3.impl.DomImplementationImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.fau.cs.osr.utils.WrappedException;

public class Wom3Toolbox
{
	public static final String WOM_NS_DEFAULT_PREFIX = "wom";
	
	// =========================================================================
	
	private Wom3Toolbox()
	{
	}
	
	// =========================================================================
	//  Singletons
	// =========================================================================
	
	public static DomImplementationImpl getWomDomImpl()
	{
		return DomImplementationImpl.get();
	}
	
	public static TransformerFactory getXalanTransformerFactory() throws TransformerFactoryConfigurationError
	{
		return TransformerFactory.newInstance(
				"org.apache.xalan.processor.TransformerFactoryImpl",
				null);
	}
	
	public static TransformerFactory getSaxonTransformerFactory() throws TransformerFactoryConfigurationError
	{
		return TransformerFactory.newInstance(
				"net.sf.saxon.TransformerFactoryImpl",
				null);
	}
	
	// =========================================================================
	//  WOM helpers
	// =========================================================================
	
	public static Wom3Document createDocument()
	{
		return new DocumentImpl(getWomDomImpl());
	}
	
	public static Wom3Document createDocument(String docElemTagName)
	{
		return getWomDomImpl().createDocument(
				Wom3Node.WOM_NS_URI, docElemTagName, null);
	}
	
	public static String printWom(Wom3Node node)
	{
		try
		{
			StringWriter sw = new StringWriter();
			printWom(node, sw);
			return sw.toString();
		}
		catch (Exception e)
		{
			throw new WrappedException(e);
		}
	}
	
	public static void printWom(Wom3Node node, Writer out) throws Exception
	{
		try
		{
			InputStream xslt = Wom3Toolbox.class
					.getResourceAsStream("/org/sweble/wom3/pretty-print.xslt");
			
			Transformer transformer =
					getSaxonTransformerFactory().newTransformer(new StreamSource(xslt));
			
			transformer.setOutputProperty(OutputKeys.VERSION, "1.1");
			
			transformer.transform(
					new DOMSource(node),
					new StreamResult(out));
		}
		catch (Exception e)
		{
			throw new WrappedException(e);
		}
	}
	
	public static void womToDom(Wom3Node node, Node parent) throws Exception
	{
		try
		{
			Transformer transformer =
					getSaxonTransformerFactory().newTransformer();
			
			DOMResult result = new DOMResult(parent);
			transformer.transform(
					new DOMSource(node),
					result);
		}
		catch (Exception e)
		{
			throw new WrappedException(e);
		}
	}
	
	public static String womToWmXPath(Wom3Node doc) throws Exception
	{
		return womToWmXPath(new NamespaceContextImpl(), doc);
		
	}
	
	public static String womToWmXPath(NamespaceContext nsContext, Wom3Node doc) throws Exception
	{
		NodeList nodes = evalXPath(doc, nsContext, "" +
				"//wom:text[not(ancestor::wom:repl)]|" +
				"//wom:rtd[not(ancestor::wom:repl)]");
		
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < nodes.getLength(); ++j)
			sb.append(((Element) nodes.item(j)).getTextContent());
		
		return sb.toString();
	}
	
	public static String womToWmFast(Wom3Node wom)
	{
		StringBuilder sb = new StringBuilder();
		womToWmFast(sb, wom);
		return sb.toString();
	}
	
	public static void womToWmFast(StringBuilder sb, Wom3Node wom)
	{
		if (wom instanceof Wom3Rtd || wom instanceof Wom3Text)
		{
			sb.append(wom.getTextContent());
		}
		else if (wom instanceof Wom3Repl)
		{
			// Ignore <repl>...</repl> stuff
		}
		else
		{
			for (Wom3Node c : wom)
				womToWmFast(sb, c);
		}
	}
	
	// =========================================================================
	//  WOM query & manipulation
	// =========================================================================
	
	public static void insertBefore(
			Wom3Node parent,
			Wom3Node insertBefore,
			Wom3Node child)
	{
		if (insertBefore == null)
			parent.appendChild(child);
		else
			parent.insertBefore(child, insertBefore);
	}
	
	public static Wom3Node[] getChildrenByTagName(Wom3Node wom, String name)
	{
		ArrayList<Wom3Node> result = new ArrayList<Wom3Node>();
		getChildrenByTagName(wom, name, result);
		return result.toArray(new Wom3Node[0]);
	}
	
	public static void getChildrenByTagName(
			Wom3Node node,
			String name,
			ArrayList<Wom3Node> result)
	{
		if (node.getNodeName().equals(name))
			result.add(node);
		for (Wom3Node c : node)
			getChildrenByTagName(c, name, result);
	}
	
	// =========================================================================
	//  XPath helpers
	// =========================================================================
	
	public static XPathFactory getSaxonXPathFactory() throws XPathFactoryConfigurationException
	{
		return XPathFactory.newInstance(
				net.sf.saxon.xpath.XPathFactoryImpl.DEFAULT_OBJECT_MODEL_URI,
				"net.sf.saxon.xpath.XPathFactoryImpl",
				null);
	}
	
	public static NodeList evalXPath(
			Node node,
			NamespaceContext nsContext,
			String query) throws Exception
	{
		XPath xPath = getSaxonXPathFactory().newXPath();
		xPath.setNamespaceContext(nsContext);
		
		NodeList nodes = (NodeList)
				xPath.evaluate(query, node, XPathConstants.NODESET);
		
		return nodes;
	}
	
	// =========================================================================
	
	public static final class NamespaceContextImpl
			implements
				NamespaceContext
	{
		@Override
		public Iterator<String> getPrefixes(String namespaceURI)
		{
			final String result = getPrefix(namespaceURI);
			
			return new Iterator<String>()
			{
				private boolean first = (result != null);
				
				public boolean hasNext()
				{
					return first;
				}
				
				public String next()
				{
					if (first)
					{
						first = false;
						return result;
					}
					return null;
				}
				
				public void remove()
				{
					throw new UnsupportedOperationException();
				}
			};
		}
		
		@Override
		public String getPrefix(String namespaceURI)
		{
			if (namespaceURI == null)
				throw new IllegalArgumentException();
			
			if (namespaceURI.equals(XMLConstants.XML_NS_URI))
				return XMLConstants.XML_NS_PREFIX;
			else if (namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI))
				return XMLConstants.XMLNS_ATTRIBUTE;
			else if (namespaceURI.equals(Wom3Node.WOM_NS_URI))
				return WOM_NS_DEFAULT_PREFIX;
			else
				return null;
		}
		
		@Override
		public String getNamespaceURI(String prefix)
		{
			if (prefix == null)
				throw new IllegalArgumentException();
			
			if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX))
				return Wom3Node.WOM_NS_URI;
			else if (prefix.equals(XMLConstants.XML_NS_PREFIX))
				return XMLConstants.XML_NS_URI;
			else if (prefix.equals(XMLConstants.XMLNS_ATTRIBUTE))
				return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
			else if (prefix.equals(WOM_NS_DEFAULT_PREFIX))
				return Wom3Node.WOM_NS_URI;
			else
				return XMLConstants.NULL_NS_URI;
		}
	}
}
