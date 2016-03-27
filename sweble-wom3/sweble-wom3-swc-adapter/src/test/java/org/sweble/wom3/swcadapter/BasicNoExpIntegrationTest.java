/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3.swcadapter;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import de.fau.cs.osr.utils.NamedParametrized;
import de.fau.cs.osr.utils.TestNameAnnotation;
import de.fau.cs.osr.utils.TestResourcesFixture;

/**
 * Tests if conversion from Wikitext to WOM (using an AST as intermediate
 * representation) works for all basic elements of Wikitext. The parsed Wikitext
 * is expanded. Some elements are also tested with an unexpanded AST.
 */
@RunWith(value = NamedParametrized.class)
public class BasicNoExpIntegrationTest
		extends
			WtWom3IntegrationTestBase
{
	private static final String FILTER_RX = ".*?\\.wikitext";

	private static final String INPUT_SUB_DIR = "basic-no-exp/wikitext";

	private static final String EXPECTED_WOM_SUB_DIR = "basic-no-exp/wom";

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

	public BasicNoExpIntegrationTest(
			String title,
			TestResourcesFixture resources,
			File inputFile)
	{
		super(resources);
		this.inputFile = inputFile;
	}

	// =========================================================================

	/**
	 * Wikitext -( no expansion )-> AST -> WOM == expected WOM?
	 */
	@Test
	@TestNameAnnotation(annotation = "Expected in dir: " + EXPECTED_WOM_SUB_DIR)
	public void testConvertUnexpandedAstToWomAndCompareWithReferenceWom() throws Exception
	{
		parsePrintAndCompare(
				inputFile,
				INPUT_SUB_DIR,
				EXPECTED_WOM_SUB_DIR,
				null);
	}

	/**
	 * Wikitext -( no expansion )-> AST -> WOM -> Wikitext == original Wikitext?
	 */
	@Test
	@TestNameAnnotation(annotation = "Expected in dir: " + INPUT_SUB_DIR)
	public void testRecoverWikitextFromUnexpandedWomAndCompareWithInputWikitext() throws Exception
	{
		parsePrintExtractRtdAndCompare(
				inputFile,
				INPUT_SUB_DIR,
				null);
	}
}
