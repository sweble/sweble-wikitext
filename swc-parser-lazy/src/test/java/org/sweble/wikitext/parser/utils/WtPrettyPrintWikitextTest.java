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

package org.sweble.wikitext.parser.utils;

import java.io.File;
import java.io.IOException;

import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.nodes.WtNode;

import xtc.parser.ParseException;
import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.test.FileCompare;
import de.fau.cs.osr.ptk.common.test.IntegrationTestBase;
import de.fau.cs.osr.ptk.common.test.TestResourcesFixture;

public class WtPrettyPrintWikitextTest
{
	public static void test(
			ParserConfig config,
			IntegrationTestBase<WtNode> test,
			File inputFile,
			String inputSubDir,
			String expectedSubDir,
			TestResourcesFixture resources,
			AstVisitor<WtNode>[] visitors) throws IOException, ParseException
	{
		// -- pretty print
		
		Object ast = test.parse(inputFile, visitors);
		
		TypedPrettyPrinter printer = new TypedPrettyPrinter();
		String actual = test.printToString(ast, printer);
		
		// -- print and compare ASTs
		
		FileCompare cmp = new FileCompare(resources);
		actual = cmp.fixActualText(actual);
		try
		{
			File expectedFile = TestResourcesFixture.rebase(
					inputFile,
					inputSubDir,
					expectedSubDir,
					printer.getPrintoutType(),
					false /* throw if file doesn't exist */);
			
			cmp.assertEquals(expectedFile, actual);
		}
		catch (IllegalArgumentException e)
		{
			cmp.assertEquals(inputFile, actual);
		}
	}
}
