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

package org.sweble.wikitext.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.NonExpandingParser;
import org.sweble.wikitext.parser.utils.WtAstPrinter;

import de.fau.cs.osr.ptk.common.jxpath.AstNodePointerFactory;
import de.fau.cs.osr.utils.FileTools;
import de.fau.cs.osr.utils.StringTools;
import xtc.parser.ParseException;

public class XPathTest
{
	private static final String PATH = "/nopkg-xpath";

	private static final boolean WARNINGS_ENABLED = false;

	private static final boolean GATHER_RTD = true;

	private static final boolean AUTO_CORRECT = false;

	// =========================================================================

	@Test
	public void testFrance() throws IOException, ParseException
	{
		String title = "raw-France";

		WtNode ast = parse(title);

		JXPathContext context = JXPathContext.newContext(ast);

		StringBuilder b = new StringBuilder();

		doQuery(context, b, "/WtParagraph[3]");

		doQuery(context, b, "(//WtSection[@level=3])[1]");

		doQuery(context, b, "//WtTemplate[contains(name//WtText[@content],\"Infobox Country\")]//WtTemplateArgument[contains(name//WtText[@content],\"capital\")]/value");

		String actual = FileTools.lineEndToUnix(b.toString());

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

	// =========================================================================

	@Test
	public void testGermany() throws IOException, ParseException
	{
		String title = "raw-Germany";

		WtNode ast = parse(title);

		JXPathContext context = JXPathContext.newContext(ast);

		StringBuilder b = new StringBuilder();

		doQuery(context, b, "//WtTemplate[contains(name//WtText[@content],\"Infobox country\")]//WtTemplateArgument[contains(name//WtText[@content],\"capital\")]/value");

		String actual = FileTools.lineEndToUnix(b.toString());

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

	// =========================================================================

	private final NonExpandingParser parser;

	public XPathTest()
	{
		JXPathContextReferenceImpl.addNodePointerFactory(
				new AstNodePointerFactory());

		parser = new NonExpandingParser(WARNINGS_ENABLED, GATHER_RTD, AUTO_CORRECT);
	}

	// =========================================================================

	private WtNode parse(String title) throws IOException, ParseException
	{
		WtNode ast = parser.parseArticle(
				load(PATH + "/wikitext/" + title + ".wikitext"),
				title);

		return ast;
	}

	private String load(String path) throws IOException
	{
		InputStream in = getClass().getResourceAsStream(path);
		if (in == null)
			return null;
		return FileTools.lineEndToUnix(
				IOUtils.toString(in, "UTF-8"));
	}

	private void doQuery(
			JXPathContext context,
			StringBuilder b,
			final String query)
	{
		b.append(StringTools.strrep('-', 80));
		b.append("\n  ");
		b.append(query);
		b.append('\n');
		b.append(StringTools.strrep('-', 80));
		b.append('\n');

		int j = 1;
		for (Iterator<?> i = context.iterate(query); i.hasNext();)
		{
			if (j > 1)
			{
				b.append(StringTools.strrep('-', 80));
				b.append('\n');
			}
			b.append(WtAstPrinter.print((WtNode) i.next()));
			b.append('\n');
			++j;
		}

		b.append(StringTools.strrep('=', 80));
		b.append('\n');
	}
}
