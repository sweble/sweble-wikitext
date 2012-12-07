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

@RunWith(value = NamedParametrized.class)
public class WomParsedIntegrationTest
		extends
			WomIntegrationTestBase
{
	private static final String FILTER_RX = ".*?\\.wikitext";
	
	private static final String INPUT_SUB_DIR = "wom/unexp/wikitext";
	
	private static final String EXPECTED_WOM_SUB_DIR = "wom/unexp/wom";
	
	// =========================================================================
	
	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		return WomIntegrationTestBase.gather(INPUT_SUB_DIR, FILTER_RX, true);
	}
	
	// =========================================================================
	
	private final File inputFile;
	
	// =========================================================================
	
	public WomParsedIntegrationTest(String title, File inputFile)
	{
		this.inputFile = inputFile;
	}
	
	// =========================================================================
	
	@Test
	public void testAstAfterParsingWithoutExpansionMatchesReference() throws Exception
	{
		parsePrintAndCompare(
				inputFile,
				INPUT_SUB_DIR,
				EXPECTED_WOM_SUB_DIR,
				null);
	}
}
