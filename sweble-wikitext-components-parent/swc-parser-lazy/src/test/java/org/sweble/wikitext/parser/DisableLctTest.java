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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.NonExpandingParser;

public class DisableLctTest
{
	@Test
	public void testForLctRecognition() throws Exception
	{
		NonExpandingParser parser = new NonExpandingParser(
				true /*warningsEnabled*/,
				true /*gatherRtd*/,
				false /*autoCorrect*/,
				true /*langConvTagsEnabled*/);

		WtNode article = parser.parseArticle(
				"IUPACName=<small>4-(2-{4-[(11''R'')-3,10-dibromo-8-chloro-6,11-dihydro-5H-benzo[5,6]cyclohepta[1,2-''b'']pyridin-11-yl]piperidin-1-yl}-2-oxoethyl)piperidine-1-carboxamide</small>",
				"title");

		assertNotNull(scanForLct(article));
	}

	@Test
	public void testForNoLctRecognition() throws Exception
	{
		NonExpandingParser parser = new NonExpandingParser(
				true /*warningsEnabled*/,
				true /*gatherRtd*/,
				false /*autoCorrect*/,
				false /*langConvTagsEnabled*/);

		WtNode article = parser.parseArticle(
				"IUPACName=<small>4-(2-{4-[(11''R'')-3,10-dibromo-8-chloro-6,11-dihydro-5H-benzo[5,6]cyclohepta[1,2-''b'']pyridin-11-yl]piperidin-1-yl}-2-oxoethyl)piperidine-1-carboxamide</small>",
				"title");

		/*
		PrinterInterface printer = new TypedWtAstPrinter();
		System.out.println(printToString(article, printer));
		*/

		assertNull(scanForLct(article));
	}

	private WtNode scanForLct(WtNode node)
	{
		if (node.getClass().getSimpleName().toLowerCase().startsWith("wtlct"))
			return node;
		for (WtNode child : node)
		{
			WtNode lct = scanForLct(child);
			if (lct != null)
				return lct;
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
