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
package org.sweble.wikitext.lazy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.junit.Test;

import xtc.parser.ParseException;
import de.fau.cs.osr.ptk.common.AstPrinter;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.jxpath.AstNodePointerFactory;
import de.fau.cs.osr.utils.StringUtils;

public class XPathTest
{
	private static final String PATH = "/xpath";
	
	private static final boolean WARNINGS_ENABLED = false;
	
	private static final boolean GATHER_RTD = true;
	
	private static final boolean AUTO_CORRECT = false;
	
	private final FullParser parser;
	
	public XPathTest()
	{
		JXPathContextReferenceImpl.addNodePointerFactory(
		        new AstNodePointerFactory());
		
		parser = new FullParser(WARNINGS_ENABLED, GATHER_RTD, AUTO_CORRECT);
	}
	
	@Test
	public void testFrance() throws IOException, ParseException
	{
		String title = "raw-France";
		
		AstNode ast = parse(title);
		
		JXPathContext context = JXPathContext.newContext(ast);
		
		StringBuilder b = new StringBuilder();
		
		doQuery(context, b, "/*[1]/Paragraph[3]");
		
		doQuery(context, b, "(//Section[@level=3])[1]");
		
		doQuery(context, b, "//Template[contains(name//Text[@content],\"Infobox Country\")]//TemplateArgument[contains(name//Text[@content],\"capital\")]/value");
		
		String actual = b.toString().replace("\r\n", "\n");
		
		String expected = null;
		try
		{
			expected = load(PATH + "/ast/" + title + ".ast");
		}
		catch (IOException e)
		{
		}
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testGermany() throws IOException, ParseException
	{
		String title = "raw-Germany";
		
		AstNode ast = parse(title);
		
		JXPathContext context = JXPathContext.newContext(ast);
		
		StringBuilder b = new StringBuilder();
		
		doQuery(context, b, "//Template[contains(name//Text[@content],\"Infobox country\")]//TemplateArgument[contains(name//Text[@content],\"capital\")]/value");
		
		String actual = b.toString().replace("\r\n", "\n");
		
		String expected = null;
		try
		{
			expected = load(PATH + "/ast/" + title + ".ast");
		}
		catch (IOException e)
		{
		}
		
		Assert.assertEquals(expected, actual);
	}

	private AstNode parse(String title) throws IOException, ParseException
	{
		AstNode ast = parser.parseArticle(
		        load(PATH + "/wikitext/" + title + ".wikitext"),
		        title);
		
		return ast;
	}
	
	private String load(String path) throws IOException
	{
		InputStream in = getClass().getResourceAsStream(path);
		if (in == null)
			return null;
		return IOUtils.toString(in, "UTF-8");
	}
	
	private void doQuery(JXPathContext context, StringBuilder b, final String query)
	{
		b.append(StringUtils.strrep('-', 80));
		b.append("\n  ");
		b.append(query);
		b.append('\n');
		b.append(StringUtils.strrep('-', 80));
		b.append('\n');
		
		int j = 1;
		for (Iterator<?> i = context.iterate(query); i.hasNext();)
		{
			if (j > 1)
			{
				b.append(StringUtils.strrep('-', 80));
				b.append('\n');
			}
			b.append(AstPrinter.print((AstNode) i.next()));
			b.append('\n');
			++j;
		}
		
		b.append(StringUtils.strrep('=', 80));
		b.append('\n');
	}
}
