package org.sweble.wikitext.wom.utils;

import java.io.IOException;
import java.io.Writer;

import javax.xml.parsers.ParserConfigurationException;

import org.sweble.wom.WomNode;

import de.fau.cs.osr.ptk.common.PrinterInterface;
import de.fau.cs.osr.utils.WrappedException;

public class TypedWomPrettyPrinter
		implements
			PrinterInterface
{
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
			WomPrettyPrinter.print(out, (WomNode) wom);
		}
		catch (ParserConfigurationException e)
		{
			throw new WrappedException(e);
		}
	}
}
