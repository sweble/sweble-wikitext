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

package org.sweble.wikitext.wom;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wikitext.wom.utils.WomIntegrationTestBase;

import de.fau.cs.osr.utils.NamedParametrized;
import de.fau.cs.osr.utils.TestResourcesFixture;

@RunWith(value = NamedParametrized.class)
public class WomPostpdIntegrationTest
		extends
			WomIntegrationTestBase
{
	private static final String FILTER_RX = ".*?\\.wikitext";
	
	private static final String INPUT_SUB_DIR = "wom/basic/wikitext";
	
	private static final String EXPECTED_WOM_SUB_DIR = "wom/basic/wom";
	
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
	
	public WomPostpdIntegrationTest(
			String title,
			TestResourcesFixture resources,
			File inputFile)
	{
		super(resources);
		this.inputFile = inputFile;
	}
	
	// =========================================================================
	
	@Test
	public void testAstAfterPostprocessingMatchesReference() throws Exception
	{
		expandPrintAndCompare(
				inputFile,
				INPUT_SUB_DIR,
				EXPECTED_WOM_SUB_DIR);
	}
}
