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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.sweble.wikitext.parser.nodes.WtIllegalCodePoint;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtXmlAttribute;
import org.sweble.wikitext.parser.utils.NonExpandingParser;

public class ReplaceIllegalCodePointsTest
{
	@Test
	public void testForIllegalCodePoint() throws Exception
	{
		NonExpandingParser parser = new NonExpandingParser(
				false /*convertIllegalCodePoints*/,
				true /*warningsEnabled*/,
				true /*gatherRtd*/,
				false /*autoCorrect*/,
				true /*langConvTagsEnabled*/);

		WtNode article = parser.parseArticle("<ref foo=\"\u007f\" />", "title");

		WtNode attr = findAttribute(article);
		assertNotNull(attr);

		/*
		PrinterInterface printer = new TypedWtAstPrinter();
		System.out.println(printToString(attr, printer));
		*/

		assertEquals("\u007f", ((WtIllegalCodePoint) ((WtXmlAttribute) attr).getValue().get(0)).getCodePoint());
	}

	@Test
	public void testForReplacedIllegalCodePoint() throws Exception
	{
		NonExpandingParser parser = new NonExpandingParser(
				true /*convertIllegalCodePoints*/,
				true /*warningsEnabled*/,
				true /*gatherRtd*/,
				false /*autoCorrect*/,
				true /*langConvTagsEnabled*/);

		WtNode article = parser.parseArticle("<ref foo=\"\u007F\" />", "title");

		WtNode attr = findAttribute(article);
		assertNotNull(attr);

		/*
		PrinterInterface printer = new TypedWtAstPrinter();
		System.out.println(printToString(attr, printer));
		*/

		assertEquals("\ufffd", ((WtText) ((WtXmlAttribute) attr).getValue().get(0)).getContent());
	}

	private WtNode findAttribute(WtNode node)
	{
		if (node instanceof WtXmlAttribute)
			return node;
		for (WtNode child : node)
		{
			WtNode found = findAttribute(child);
			if (found != null)
				return found;
		}
		return null;
	}

	/*
	protected String printToString(Object ast, PrinterInterface printer) throws IOException
	{
		StringWriter writer = new StringWriter();
		printer.print(ast, writer);
		return writer.toString();
	}
	*/
}
