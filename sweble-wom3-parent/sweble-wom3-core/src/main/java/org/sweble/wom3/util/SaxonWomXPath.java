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
package org.sweble.wom3.util;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.sweble.wom3.Wom3Node;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.fau.cs.osr.utils.WrappedException;
import net.sf.saxon.pull.NamespaceContextImpl;
import net.sf.saxon.sxpath.IndependentContext;

/**
 * Requires dependency:
 * 
 * <pre>
 *   &lt;dependency>
 *     &lt;groupId>net.sf.saxon&lt;/groupId>
 *     &lt;artifactId>Saxon-HE&lt;/artifactId>
 *   &lt;/dependency>
 * </pre>
 */
public class SaxonWomXPath
{
	public static XPathFactory getSaxonXPathFactory() throws XPathFactoryConfigurationException
	{
		return XPathFactory.newInstance(
				net.sf.saxon.xpath.XPathFactoryImpl.DEFAULT_OBJECT_MODEL_URI,
				net.sf.saxon.xpath.XPathFactoryImpl.class.getName(),
				null);
	}

	public static NodeList evalXPath(
			Node node,
			NamespaceContext nsContext,
			String query) throws XPathFactoryConfigurationException, XPathExpressionException
	{
		XPath xPath = getSaxonXPathFactory().newXPath();
		xPath.setNamespaceContext(nsContext);

		NodeList nodes = (NodeList)
				xPath.evaluate(query, node, XPathConstants.NODESET);

		return nodes;
	}

	public static String womToWmXPath(Wom3Node doc)
	{
		IndependentContext context = new IndependentContext();
		context.declareNamespace(XMLConstants.DEFAULT_NS_PREFIX, Wom3Node.WOM_NS_URI);
		context.declareNamespace(XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI);
		context.declareNamespace(XMLConstants.XMLNS_ATTRIBUTE, XMLConstants.XMLNS_ATTRIBUTE_NS_URI);
		context.declareNamespace(Wom3Node.DEFAULT_WOM_NS_PREFIX, Wom3Node.WOM_NS_URI);

		return womToWmXPath(new NamespaceContextImpl(context), doc);

	}

	public static String womToWmXPath(NamespaceContext nsContext, Wom3Node doc)
	{
		NodeList nodes;
		try
		{
			nodes = evalXPath(doc, nsContext, "" +
					"//wom:text[not(ancestor::wom:repl)]|" +
					"//wom:rtd[not(ancestor::wom:repl)]");
		}
		catch (XPathFactoryConfigurationException e)
		{
			throw new WrappedException(e);
		}
		catch (XPathExpressionException e)
		{
			throw new WrappedException(e);
		}

		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < nodes.getLength(); ++j)
			sb.append(((Element) nodes.item(j)).getTextContent());

		return sb.toString();
	}

	// =========================================================================

	/*
	public static final class NamespaceContextImpl
			implements
				NamespaceContext,
				NamespaceResolver
	{
		private static final Map<String, String> prefixMap = new HashMap<String, String>();

		static
		{
			prefixMap.put(XMLConstants.DEFAULT_NS_PREFIX, Wom3Node.WOM_NS_URI);
			prefixMap.put(XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI);
			prefixMap.put(XMLConstants.XMLNS_ATTRIBUTE, XMLConstants.XMLNS_ATTRIBUTE_NS_URI);
			prefixMap.put(Wom3Node.DEFAULT_WOM_NS_PREFIX, Wom3Node.WOM_NS_URI);
		}

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
				return Wom3Node.DEFAULT_WOM_NS_PREFIX;
			else
				return null;
		}

		@Override
		public String getNamespaceURI(String prefix)
		{
			if (prefix == null)
				throw new IllegalArgumentException();

			String ns = prefixMap.get(prefix);
			return (ns != null) ? ns : XMLConstants.NULL_NS_URI;
		}

		@Override
		public String getURIForPrefix(String prefix, boolean useDefault)
		{
			return getNamespaceURI(prefix);
		}

		@Override
		public Iterator<String> iteratePrefixes()
		{
			return prefixMap.keySet().iterator();
		}
	}
	*/
}
