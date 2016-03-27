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

import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.sweble.wom3.Wom3Node;
import org.w3c.dom.Node;

import de.fau.cs.osr.utils.WrappedException;

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
public class SaxonWomTransformations
{
	public static TransformerFactory getSaxonTransformerFactory() throws TransformerFactoryConfigurationError
	{
		return TransformerFactory.newInstance(
				net.sf.saxon.TransformerFactoryImpl.class.getName(), null);
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

	public static void printWom(Wom3Node node, Writer out)
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

	public static void womToDom(Wom3Node node, Node parent)
	{
		try
		{
			Transformer transformer =
					getSaxonTransformerFactory().newTransformer();

			DOMResult result = new DOMResult(parent);
			transformer.transform(new DOMSource(node), result);
		}
		catch (Exception e)
		{
			throw new WrappedException(e);
		}
	}
}
