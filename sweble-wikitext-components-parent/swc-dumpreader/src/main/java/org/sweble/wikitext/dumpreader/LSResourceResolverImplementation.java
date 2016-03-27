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
package org.sweble.wikitext.dumpreader;

import java.io.InputStream;
import java.io.Reader;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

final class LSResourceResolverImplementation
		implements
			LSResourceResolver
{
	@Override
	public LSInput resolveResource(
			String type,
			String namespaceURI,
			String publicId,
			final String systemId,
			final String baseURI)
	{
		if ("http://www.w3.org/2001/XMLSchema".equals(type)
				&& (publicId == null)
				&& isExportXsd(getClass(), systemId))
		{
			return new LSInputImplementation(
					getExportXsdFileNameIfExists(getClass(), systemId),
					systemId,
					baseURI);
		}
		else if ("http://www.w3.org/2001/XMLSchema".equals(type)
				&& (publicId == null)
				&& "http://www.w3.org/2001/xml.xsd".equals(systemId))
		{
			return new LSInputImplementation(
					"/xml.xsd",
					systemId,
					baseURI);
		}
		else
		{
			System.err.println(String.format(
					"Cannot resolve: type = '''%s''', namespaceURI = '''%s''', publicId = '''%s''', systemId = '''%s''', baseURI = '''%s'''",
					type,
					namespaceURI,
					publicId,
					systemId,
					baseURI));

			return null;
		}
	}

	private static boolean isExportXsd(Class<?> clazz, String systemId)
	{
		return (getExportXsdFileNameIfExists(clazz, systemId) != null);
	}

	private static String getExportXsdFileNameIfExists(
			Class<?> clazz,
			String systemId)
	{
		if (systemId != null)
		{
			String string = "http://www.mediawiki.org/xml";
			if (systemId.startsWith(string + "/export-")
					&& systemId.endsWith(".xsd"))
			{
				String fileName = systemId.substring(string.length());
				if (clazz.getResource(fileName) != null)
					return fileName;
			}
		}
		return null;
	}

	// =========================================================================

	private final class LSInputImplementation
			implements
				LSInput
	{
		private final String xsdPath;

		private final String systemId;

		private final String baseURI;

		private LSInputImplementation(
				String xsdPath,
				String systemId,
				String baseURI)
		{
			this.xsdPath = xsdPath;
			this.systemId = systemId;
			this.baseURI = baseURI;
		}

		@Override
		public void setSystemId(String systemId)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void setStringData(String stringData)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPublicId(String publicId)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEncoding(String encoding)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCharacterStream(Reader characterStream)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCertifiedText(boolean certifiedText)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void setByteStream(InputStream byteStream)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBaseURI(String baseURI)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public String getSystemId()
		{
			return systemId;
		}

		@Override
		public String getStringData()
		{
			return null;
		}

		@Override
		public String getPublicId()
		{
			return null;
		}

		@Override
		public String getEncoding()
		{
			return "UTF-8";
		}

		@Override
		public Reader getCharacterStream()
		{
			return null;
		}

		@Override
		public boolean getCertifiedText()
		{
			return false;
		}

		@Override
		public InputStream getByteStream()
		{
			return getClass().getResourceAsStream(xsdPath);
		}

		@Override
		public String getBaseURI()
		{
			return baseURI;
		}
	}
}
