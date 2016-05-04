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
import org.sweble.wikitext.parser.utils.NonExpandingParserConfig;
import org.sweble.wikitext.parser.utils.TypedWtAstPrinter;

import de.fau.cs.osr.utils.NamedParametrized;
import de.fau.cs.osr.utils.TestNameAnnotation;
import de.fau.cs.osr.utils.TestResourcesFixture;

@RunWith(value = NamedParametrized.class)
public class NoFosterParentingForTransclusionsTest
		extends
			ParserIntegrationTestBase
{
	private static final String FILTER_RX = ".*?\\.wikitext";

	private static final String INPUT_SUB_DIR = "nopkg-foster/input.wikitext";

	private static final String EXPECTED_WITH_FP_AST_SUB_DIR = "nopkg-foster/after-postprocessing-with-fp.ast";

	private static final String EXPECTED_NO_TC_FP_AST_SUB_DIR = "nopkg-foster/after-postprocessing-no-tc-fp.ast";

	private static final String EXPECTED_NO_FP_AST_SUB_DIR = "nopkg-foster/after-postprocessing-no-fp.ast";

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

	public NoFosterParentingForTransclusionsTest(
			String title,
			TestResourcesFixture resources,
			File inputFile)
	{
		super(resources, new NonExpandingParser());
		this.inputFile = inputFile;
	}

	// =========================================================================

	@Test
	@TestNameAnnotation(annotation = "Expected in dir: " + EXPECTED_WITH_FP_AST_SUB_DIR)
	public void testAstAfterPostprocessingMatchesReferenceWithFp() throws Exception
	{
		getConfig().setFosterParenting(true);
		getConfig().setFosterParentingForTransclusions(true);
		parsePrintAndCompare(
				inputFile,
				INPUT_SUB_DIR,
				EXPECTED_WITH_FP_AST_SUB_DIR,
				new TypedWtAstPrinter());
	}

	@Test
	@TestNameAnnotation(annotation = "Expected in dir: " + EXPECTED_NO_TC_FP_AST_SUB_DIR)
	public void testAstAfterPostprocessingMatchesReferenceNoTcFp() throws Exception
	{
		getConfig().setFosterParenting(true);
		getConfig().setFosterParentingForTransclusions(false);
		parsePrintAndCompare(
				inputFile,
				INPUT_SUB_DIR,
				EXPECTED_NO_TC_FP_AST_SUB_DIR,
				new TypedWtAstPrinter());
	}

	@Test
	@TestNameAnnotation(annotation = "Expected in dir: " + EXPECTED_NO_FP_AST_SUB_DIR)
	public void testAstAfterPostprocessingMatchesReferenceNoFp() throws Exception
	{
		getConfig().setFosterParenting(false);
		getConfig().setFosterParentingForTransclusions(true);
		parsePrintAndCompare(
				inputFile,
				INPUT_SUB_DIR,
				EXPECTED_NO_FP_AST_SUB_DIR,
				new TypedWtAstPrinter());
	}

	protected NonExpandingParserConfig getConfig()
	{
		return (NonExpandingParserConfig) super.getConfig();
	}
}
