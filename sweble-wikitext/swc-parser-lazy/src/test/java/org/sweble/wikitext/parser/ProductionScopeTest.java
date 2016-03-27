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
import org.sweble.wikitext.parser.utils.NonExpandingParser;
import org.sweble.wikitext.parser.utils.TypedPrettyPrinter;
import org.sweble.wikitext.parser.utils.TypedWtAstPrinter;

import de.fau.cs.osr.utils.NamedParametrized;
import de.fau.cs.osr.utils.TestNameAnnotation;
import de.fau.cs.osr.utils.TestResourcesFixture;

@RunWith(value = NamedParametrized.class)
public class ProductionScopeTest
		extends
			ParserIntegrationTestBase
{
	private static final String FILTER_RX = ".*?\\.wikitext";

	private static final String INPUT_SUB_DIR = "nopkg-scopes/input.wikitext";

	private static final String EXPECTED_SUB_DIR = "nopkg-scopes/after-postprocessing.ast";

	private static final String EXPECTED_PP_SUB_DIR = "nopkg-scopes/pretty-printed.wikitext";

	private static final String EXPECTED_PPAST_SUB_DIR = "nopkg-scopes/pretty-printed.ast";

	// =========================================================================

	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		TestResourcesFixture resources = getTestResourcesFixture();
		return resources.gatherAsParameters(INPUT_SUB_DIR, FILTER_RX, false);
	}

	// =========================================================================

	private final File inputFile;

	// =========================================================================

	public ProductionScopeTest(
			String title,
			TestResourcesFixture resources,
			File inputFile)
	{
		super(resources, new NonExpandingParser());
		this.inputFile = inputFile;
	}

	// =========================================================================

	@Test
	@TestNameAnnotation(annotation = "Expected in dir: " + EXPECTED_SUB_DIR)
	public void testAstAfterPostprocessingMatchesReferenceAst() throws Exception
	{
		parsePrintAndCompare(
				inputFile,
				INPUT_SUB_DIR,
				EXPECTED_SUB_DIR,
				new TypedWtAstPrinter());
	}

	@Test
	@TestNameAnnotation(annotation = "Expected in dir: " + EXPECTED_PP_SUB_DIR)
	public void testPrettyPrintedWikitextMatchesReference() throws Exception
	{
		parsePrintAndCompare(
				inputFile,
				INPUT_SUB_DIR,
				EXPECTED_PP_SUB_DIR,
				new TypedPrettyPrinter());
	}

	@Test
	@TestNameAnnotation(annotation = "Expected in dir: " + EXPECTED_PPAST_SUB_DIR)
	public void testParsedPrettyPrintedWikitextMatchesOriginal() throws Exception
	{
		prettyPrintAstAndCompare(
				inputFile,
				INPUT_SUB_DIR,
				EXPECTED_PPAST_SUB_DIR);
	}
}
