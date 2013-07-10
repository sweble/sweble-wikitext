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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wom.WomNode;

import de.fau.cs.osr.ptk.common.PrinterInterface;
import de.fau.cs.osr.utils.WrappedException;

public class TypedWomPrettyPrinter
		implements
			PrinterInterface
{
	private final WikiConfig config;
	
	public TypedWomPrettyPrinter(WikiConfig config)
	{
		this.config = config;
	}
	
	@Override
	public String getPrintoutType()
	{
		return "wom.xml";
	}
	
	@Override
	public void print(Object wom, Writer out) throws IOException
	{
		try
		{
			WomPrettyPrinter.print(config, out, (WomNode) wom);
		}
		catch (ParserConfigurationException e)
		{
			throw new WrappedException(e);
		}
		catch (TransformerFactoryConfigurationError e)
		{
			throw new WrappedException(e);
		}
		catch (TransformerException e)
		{
			throw new WrappedException(e);
		}
	}
}
