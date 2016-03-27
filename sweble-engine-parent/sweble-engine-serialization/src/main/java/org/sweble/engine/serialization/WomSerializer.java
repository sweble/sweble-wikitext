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
package org.sweble.engine.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.xerces.parsers.DOMParser;
import org.sweble.engine.serialization.CompressorFactory.CompressionFormat;
import org.sweble.wom3.Wom3Document;
import org.sweble.wom3.Wom3DocumentFragment;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.serialization.Wom3NodeCompactJsonTypeAdapter;
import org.sweble.wom3.serialization.Wom3NodeJsonTypeAdapter;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WomSerializer
{
	private static final Charset CHARSET = Charset.forName("UTF8");

	// =========================================================================

	public static enum SerializationFormat
	{
		JAVA,
		JSON,
		XML
	}

	// =========================================================================

	private Transformer prettyXmlTransformer;

	private Transformer normalXmlTransformer;

	// =========================================================================

	public byte[] serialize(
			Wom3Node wom,
			SerializationFormat serializationFormat,
			boolean compact,
			boolean pretty) throws IOException, SerializationException
	{
		final byte[] result;
		switch (serializationFormat)
		{
			case JAVA:
			{
				ByteArrayOutputStream baos = null;
				ObjectOutputStream oos = null;
				try
				{
					baos = new ByteArrayOutputStream();
					oos = new ObjectOutputStream(baos);
					oos.writeObject(wom);
				}
				finally
				{
					IOUtils.closeQuietly(baos);
					IOUtils.closeQuietly(oos);
				}
				result = baos.toByteArray();
				break;
			}
			case JSON:
			{
				result = getGson(compact, pretty).toJson(wom).getBytes(CHARSET);
				break;
			}
			case XML:
			{
				ByteArrayOutputStream baos = null;
				try
				{
					baos = new ByteArrayOutputStream();
					getXmlTransformer(pretty).transform(
							new DOMSource(wom),
							new StreamResult(baos));
				}
				catch (TransformerException e)
				{
					throw new SerializationException(e);
				}
				finally
				{
					IOUtils.closeQuietly(baos);
				}
				result = baos.toByteArray();
				break;
			}
			default:
				throw new IllegalArgumentException();
		}
		return result;
	}

	public Wom3Node deserialize(
			byte[] serialized,
			SerializationFormat serializationFormat,
			boolean compact) throws IOException, DeserializationException
	{
		Wom3Node result;
		switch (serializationFormat)
		{
			case JAVA:
			{
				ByteArrayInputStream bais = null;
				ObjectInputStream ois = null;
				try
				{
					bais = new ByteArrayInputStream(serialized);
					ois = new ObjectInputStream(bais);
					result = (Wom3Node) ois.readObject();
				}
				catch (ClassNotFoundException e)
				{
					throw new DeserializationException(e);
				}
				finally
				{
					IOUtils.closeQuietly(bais);
					IOUtils.closeQuietly(ois);
				}
				break;
			}
			case JSON:
			{
				Gson gson = getGson(compact, false);
				Wom3DocumentFragment fragment = (Wom3DocumentFragment)
						gson.fromJson(new String(serialized, CHARSET), Wom3Node.class);
				result = fragment.getOwnerDocument();
				Wom3Node root = fragment.getFirstChild();
				fragment.removeChild(root);
				result.replaceChild(root, result.getFirstChild());
				break;
			}
			case XML:
			{
				ByteArrayInputStream bais = null;
				try
				{
					bais = new ByteArrayInputStream(serialized);
					InputSource is = new InputSource(bais);
					DOMParser parser = getXmlParser();
					parser.parse(is);
					result = (Wom3Node) parser.getDocument();
				}
				catch (SAXException e)
				{
					throw new DeserializationException(e);
				}
				finally
				{
					IOUtils.closeQuietly(bais);
				}
				break;
			}
			default:
				throw new IllegalArgumentException();
		}
		return result;
	}

	public byte[] compress(
			byte[] serialized,
			CompressionFormat compressionFormat) throws IOException, CompressionException
	{
		ByteArrayOutputStream out = null;
		OutputStream cos = null;
		InputStream in = null;
		try
		{
			out = new ByteArrayOutputStream();
			cos = CompressorFactory.createCompressorOutputStream(compressionFormat, out);
			in = new ByteArrayInputStream(serialized);
			IOUtils.copy(in, cos);
		}
		catch (CompressorFactoryException e)
		{
			throw new CompressionException(e);
		}
		finally
		{
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(cos);
			IOUtils.closeQuietly(out);
		}
		return out.toByteArray();
	}

	public byte[] decompress(
			byte[] compressed,
			CompressionFormat compressionFormat) throws IOException, CompressionException
	{
		InputStream in = null;
		InputStream cin = null;
		ByteArrayOutputStream out = null;
		try
		{
			in = new ByteArrayInputStream(compressed);
			cin = CompressorFactory.createCompressorInputStream(compressionFormat, in);
			out = new ByteArrayOutputStream(compressed.length * 2);
			IOUtils.copy(cin, out);
		}
		catch (CompressorFactoryException e)
		{
			throw new CompressionException(e);
		}
		finally
		{
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(cin);
			IOUtils.closeQuietly(in);
		}
		return out.toByteArray();
	}

	public byte[] serializeAndCompress(
			Wom3Document wom,
			CompressionFormat compressionFormat,
			SerializationFormat serializationFormat,
			boolean compact,
			boolean pretty) throws IOException, CompressionException, SerializationException
	{
		ByteArrayOutputStream out = null;
		OutputStream cos = null;
		try
		{
			out = new ByteArrayOutputStream();
			cos = CompressorFactory.createCompressorOutputStream(compressionFormat, out);
			switch (serializationFormat)
			{
				case JAVA:
				{
					ObjectOutputStream oos = null;
					try
					{
						oos = new ObjectOutputStream(cos);
						oos.writeObject(wom);
					}
					finally
					{
						IOUtils.closeQuietly(oos);
					}
					break;
				}
				case JSON:
				{
					OutputStreamWriter osw = null;
					try
					{
						osw = new OutputStreamWriter(cos, CHARSET);
						Gson gson = getGson(compact, pretty);
						gson.toJson(wom, osw);
					}
					finally
					{
						IOUtils.closeQuietly(osw);
					}
					break;
				}
				case XML:
				{
					getXmlTransformer(pretty).transform(
							new DOMSource(wom),
							new StreamResult(cos));
					break;
				}
				default:
					throw new IllegalArgumentException();
			}
		}
		catch (CompressorFactoryException e)
		{
			throw new CompressionException(e);
		}
		catch (TransformerException e)
		{
			throw new SerializationException(e);
		}
		finally
		{
			IOUtils.closeQuietly(cos);
			IOUtils.closeQuietly(out);
		}
		return out.toByteArray();
	}

	public Wom3Node decompressAndDeserialize(
			byte[] compressed,
			CompressionFormat compressionFormat,
			SerializationFormat serializationFormat,
			boolean compact) throws IOException, DeserializationException, CompressionException
	{
		ByteArrayInputStream in = null;
		InputStream cin = null;
		try
		{
			in = new ByteArrayInputStream(compressed);
			cin = CompressorFactory.createCompressorInputStream(compressionFormat, in);

			Wom3Node result;
			switch (serializationFormat)
			{
				case JAVA:
				{
					ObjectInputStream ois = null;
					try
					{
						ois = new ObjectInputStream(cin);
						result = (Wom3Node) ois.readObject();
					}
					catch (ClassNotFoundException e)
					{
						throw new DeserializationException(e);
					}
					finally
					{
						IOUtils.closeQuietly(ois);
					}
					break;
				}
				case JSON:
				{
					InputStreamReader isr = null;
					try
					{
						isr = new InputStreamReader(cin, CHARSET);
						Gson gson = getGson(compact, false);
						result = gson.fromJson(isr, Wom3Node.class);
					}
					finally
					{
						IOUtils.closeQuietly(isr);
					}
					break;
				}
				case XML:
				{
					InputSource is = new InputSource(cin);
					DOMParser parser = getXmlParser();
					parser.parse(is);
					result = (Wom3Node) parser.getDocument();
					break;
				}
				default:
					throw new IllegalArgumentException();
			}
			return result;
		}
		catch (CompressorFactoryException e)
		{
			throw new CompressionException(e);
		}
		catch (SAXException e)
		{
			throw new DeserializationException(e);
		}
		finally
		{
			IOUtils.closeQuietly(cin);
			IOUtils.closeQuietly(in);
		}
	}

	// =========================================================================

	private Transformer getNormalXmlTransformer() throws TransformerConfigurationException
	{
		if (normalXmlTransformer == null)
		{
			TransformerFactory tf = TransformerFactory.newInstance();

			normalXmlTransformer = tf.newTransformer();
		}
		return normalXmlTransformer;
	}

	private Transformer getPrettyXmlTransformer() throws TransformerConfigurationException
	{
		if (prettyXmlTransformer == null)
		{
			TransformerFactory tf = TransformerFactory.newInstance(
					"net.sf.saxon.TransformerFactoryImpl",
					null);

			InputStream xslt = getClass().getResourceAsStream("/org/sweble/wom3/pretty-print.xslt");

			prettyXmlTransformer = tf.newTransformer(new StreamSource(xslt));
		}
		return prettyXmlTransformer;
	}

	private Transformer getXmlTransformer(boolean pretty) throws TransformerConfigurationException
	{
		return pretty ?
				getPrettyXmlTransformer() :
				getNormalXmlTransformer();
	}

	private DOMParser getXmlParser() throws SAXNotRecognizedException, SAXNotSupportedException
	{
		DOMParser parser = new DOMParser();
		parser.setProperty(
				"http://apache.org/xml/properties/" + "dom/document-class-name",
				"org.sweble.wom3.impl.DocumentImpl");
		return parser;
	}

	// =========================================================================

	private Gson getGson(boolean compact, boolean pretty)
	{
		GsonBuilder builder = new GsonBuilder();

		builder.registerTypeHierarchyAdapter(
				Node.class,
				(compact ?
						(new Wom3NodeCompactJsonTypeAdapter()) :
						(new Wom3NodeJsonTypeAdapter())));

		builder.serializeNulls();

		if (pretty)
			builder.setPrettyPrinting();

		return builder.create();
	}
}
