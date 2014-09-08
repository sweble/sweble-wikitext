/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
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
 * representation) works for complex Wikitext articles. The parsed Wikitext is
 * not expanded.
 */
@RunWith(value = NamedParametrized.class)
public class ComplexIntegrationTest
		extends
			WtWom3IntegrationTestBase
{
	private static final String FILTER_RX = ".*?\\.wikitext";
	
	private static final String INPUT_SUB_DIR = "complex-no-exp/wikitext";
	
	private static final String EXPECTED_WOM_SUB_DIR = "complex-no-exp/wom";
	
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
	
	public ComplexIntegrationTest(
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
	public void testConvertAstToWomAndCompareWithReferenceWom() throws Exception
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
	public void testRecoverWikitextFromWomAndCompareWithInputWikitext() throws Exception
	{
		parsePrintExtractRtdAndCompare(
				inputFile,
				INPUT_SUB_DIR,
				null);
	}
}
