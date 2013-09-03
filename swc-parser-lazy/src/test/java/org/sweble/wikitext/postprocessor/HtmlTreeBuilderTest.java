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
package org.sweble.wikitext.postprocessor;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WikitextPostprocessor;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.utils.NonPostproParser;
import org.sweble.wikitext.parser.utils.TypedWtAstPrinter;
import org.sweble.wikitext.parser.utils.WtPrettyPrintWikitextTest;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ParserInterface;
import de.fau.cs.osr.ptk.common.test.IntegrationTestBase;
import de.fau.cs.osr.utils.NamedParametrized;
import de.fau.cs.osr.utils.TestNameAnnotation;

@RunWith(value = NamedParametrized.class)
public class HtmlTreeBuilderTest
		extends
			IntegrationTestBase<WtNode>
{
	private static final String FILTER_RX = ".*?\\.wikitext";
	
	private static final String INPUT_SUB_DIR = "tree/wikitext";
	
	private static final String EXPECTED_PARSED_AST_SUB_DIR = "tree/p-ast";
	
	private static final String EXPECTED_POSTP_AST_SUB_DIR = "tree/postp-ast";
	
	private static final String EXPECTED_PP_AST_SUB_DIR = "tree/pp-ast";
	
	// =========================================================================
	
	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		return IntegrationTestBase.gather(INPUT_SUB_DIR, FILTER_RX, true);
	}
	
	// =========================================================================
	
	private final File inputFile;
	
	private ParserConfig config;
	
	// =========================================================================
	
	public HtmlTreeBuilderTest(String title, File inputFile)
	{
		this.inputFile = inputFile;
	}
	
	@Override
	public ParserInterface<WtNode> instantiateParser()
	{
		NonPostproParser parser = new NonPostproParser();
		config = parser.getParserConfig();
		return parser;
	}
	
	// =========================================================================
	
	@Test
	@TestNameAnnotation(annotation = "Expected in dir: " + EXPECTED_POSTP_AST_SUB_DIR)
	public void testAstAfterPostprocessingMatchesReference() throws Exception
	{
		@SuppressWarnings("unchecked")
		AstVisitor<WtNode>[] visitors = new AstVisitor[] { new PostProcess() };
		
		parsePrintAndCompare(
				inputFile,
				visitors,
				INPUT_SUB_DIR,
				EXPECTED_POSTP_AST_SUB_DIR,
				new TypedWtAstPrinter());
	}
	
	@Test
	@TestNameAnnotation(annotation = "Expected in dir: " + EXPECTED_PARSED_AST_SUB_DIR)
	public void testAstAfterParsingMatchesReference() throws Exception
	{
		@SuppressWarnings("unchecked")
		AstVisitor<WtNode>[] visitors = new AstVisitor[] {};
		
		parsePrintAndCompare(
				inputFile,
				visitors,
				INPUT_SUB_DIR,
				EXPECTED_PARSED_AST_SUB_DIR,
				new TypedWtAstPrinter());
	}
	
	@Test
	@TestNameAnnotation(annotation = "Expected in dir: " + EXPECTED_PP_AST_SUB_DIR)
	public void testRestoredWikitextAfterPostprocessingMatchesOriginal() throws Exception
	{
		@SuppressWarnings("unchecked")
		AstVisitor<WtNode>[] visitors = new AstVisitor[] { new PostProcess() };
		
		WtPrettyPrintWikitextTest.test(
				config,
				this,
				inputFile,
				INPUT_SUB_DIR,
				EXPECTED_PP_AST_SUB_DIR,
				getResources(),
				visitors);
	}

	// =========================================================================
	
	public final class PostProcess
			extends
				AstVisitor<WtNode>
	{
		public WtNode visit(WtParsedWikitextPage n)
		{
			WikitextPostprocessor postp = new WikitextPostprocessor(config);
			
			return postp.postprocess(n, "-");
		}
	}
}
