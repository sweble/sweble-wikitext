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

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.NonExpandingParser;
import org.sweble.wikitext.parser.utils.TypedPrettyPrinter;
import org.sweble.wikitext.parser.utils.TypedRtDataPrinter;
import org.sweble.wikitext.parser.utils.TypedWtAstPrinter;
import org.sweble.wikitext.parser.utils.WtPrettyPrintAstTest;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ParserInterface;
import de.fau.cs.osr.ptk.common.PrinterInterface;
import de.fau.cs.osr.ptk.common.test.FileCompare;
import de.fau.cs.osr.ptk.common.test.IntegrationTestBase;
import de.fau.cs.osr.ptk.common.test.TestResourcesFixture;
import de.fau.cs.osr.utils.NamedParametrized;

@RunWith(value = NamedParametrized.class)
public class BasicIntegrationTest
		extends
			IntegrationTestBase<WtNode>
{
	private static final String FILTER_RX = ".*?\\.wikitext";
	
	private static final String INPUT_SUB_DIR = "basic/wikitext";
	
	private static final String EXPECTED_AST_SUB_DIR = "basic/ast";
	
	private static final String EXPECTED_PP_SUB_DIR = "basic/pp";
	
	private static final String EXPECTED_RT_SUB_DIR = "basic/rt";
	
	private static final String EXPECTED_PPAST_SUB_DIR = "basic/ppast";
	
	// =========================================================================
	
	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		return IntegrationTestBase.gather(INPUT_SUB_DIR, FILTER_RX, true);
	}
	
	// =========================================================================
	
	private final File inputFile;
	
	private final ParserConfig config;
	
	private final NonExpandingParser fullParser;
	
	// =========================================================================
	
	public BasicIntegrationTest(String title, File inputFile)
	{
		this.inputFile = inputFile;
		this.fullParser = new NonExpandingParser();
		this.config = fullParser.getParserConfig();
	}
	
	@Override
	public ParserInterface<WtNode> instantiateParser()
	{
		return fullParser;
	}
	
	// =========================================================================
	
	@Test
	public void testAstAfterPostprocessingMatchesReference() throws Exception
	{
		@SuppressWarnings("unchecked")
		AstVisitor<WtNode>[] visitors = new AstVisitor[] { /*new AstCompressor()*/};
		
		parsePrintAndCompare(
				inputFile,
				visitors,
				INPUT_SUB_DIR,
				EXPECTED_AST_SUB_DIR,
				new TypedWtAstPrinter());
	}
	
	@Test
	public void testRestoredWikitextAfterPostprocessingMatchesOriginal() throws Exception
	{
		@SuppressWarnings("unchecked")
		AstVisitor<WtNode>[] visitors = new AstVisitor[] { /*new AstCompressor()*/};
		PrinterInterface printer = new TypedRtDataPrinter();
		
		Object ast = parse(inputFile, visitors);
		
		String actual = printToString(ast, printer);
		
		try
		{
			File expectedFile = TestResourcesFixture.rebase(
					inputFile,
					INPUT_SUB_DIR,
					EXPECTED_RT_SUB_DIR,
					printer.getPrintoutType(),
					false /* throw if file doesn't exist */);
			
			FileCompare cmp = new FileCompare(getResources());
			cmp.compareWithExpectedOrGenerateExpectedFromActual(expectedFile, actual);
		}
		catch (IllegalArgumentException e)
		{
			String expected = TestResourcesFixture.lineEndToUnix(
					FileUtils.readFileToString(inputFile));
			
			Assert.assertEquals(expected, actual);
		}
	}
	
	@Test
	public void testPrettyPrintedWikitextMatchesReference() throws Exception
	{
		@SuppressWarnings("unchecked")
		AstVisitor<WtNode>[] visitors = new AstVisitor[] { /*new AstCompressor()*/};
		
		parsePrintAndCompare(
				inputFile,
				visitors,
				INPUT_SUB_DIR,
				EXPECTED_PP_SUB_DIR,
				new TypedPrettyPrinter());
	}
	
	@Test
	public void testParsedPrettyPrintedWikitextMatchesOriginal() throws Exception
	{
		WtPrettyPrintAstTest.test(
				config,
				this,
				inputFile,
				INPUT_SUB_DIR,
				EXPECTED_PPAST_SUB_DIR,
				getResources());
	}
}
