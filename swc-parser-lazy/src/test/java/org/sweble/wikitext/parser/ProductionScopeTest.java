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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.AstCompressor;
import org.sweble.wikitext.parser.utils.FullParser;
import org.sweble.wikitext.parser.utils.TypedPrettyPrinter;
import org.sweble.wikitext.parser.utils.TypedWtAstPrinter;
import org.sweble.wikitext.parser.utils.WtPrettyPrinterTest;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ParserInterface;
import de.fau.cs.osr.ptk.common.test.IntegrationTestBase;
import de.fau.cs.osr.utils.NamedParametrized;

@RunWith(value = NamedParametrized.class)
public class ProductionScopeTest
		extends
			IntegrationTestBase<WtNode>
{
	private static final String FILTER_RX = ".*?\\.wikitext";
	
	private static final String INPUT_SUB_DIR = "scopes/wikitext";
	
	private static final String EXPECTED_SUB_DIR = "scopes/ast";
	
	private static final String EXPECTED_PP_SUB_DIR = "scopes/pp";
	
	private static final String EXPECTED_PPAST_SUB_DIR = "scopes/ppast";
	
	// =========================================================================
	
	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		return IntegrationTestBase.gather(INPUT_SUB_DIR, FILTER_RX, true);
	}
	
	// =========================================================================
	
	private final File inputFile;
	
	private final ParserConfig config;
	
	private final FullParser fullParser;
	
	// =========================================================================
	
	public ProductionScopeTest(String title, File inputFile)
	{
		this.inputFile = inputFile;
		this.fullParser = new FullParser();
		this.config = fullParser.getParserConfig();
	}
	
	@Override
	public ParserInterface<WtNode> instantiateParser()
	{
		return fullParser;
	}
	
	// =========================================================================
	
	@Test
	public void testAstAfterPostprocessingMatchesReferenceAst() throws Exception
	{
		@SuppressWarnings("unchecked")
		AstVisitor<WtNode>[] visitors = new AstVisitor[] { new AstCompressor() };
		
		parsePrintAndCompare(
				inputFile,
				visitors,
				INPUT_SUB_DIR,
				EXPECTED_SUB_DIR,
				new TypedWtAstPrinter());
	}
	
	@Test
	public void testPrettyPrintedWikitextMatchesReference() throws Exception
	{
		@SuppressWarnings("unchecked")
		AstVisitor<WtNode>[] visitors = new AstVisitor[] { new AstCompressor() };
		
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
		WtPrettyPrinterTest.test(
				config,
				this,
				inputFile,
				INPUT_SUB_DIR,
				EXPECTED_PPAST_SUB_DIR,
				getResources());
	}
}
