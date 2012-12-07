package org.sweble.wikitext.wom.utils;

import java.io.IOException;
import java.io.Writer;

import javax.xml.parsers.ParserConfigurationException;

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
	}
}
