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

package org.sweble.wikitext.engine;

import java.io.File;

import org.junit.Test;
import org.sweble.wikitext.engine.utils.EngineIntegrationTestBase;
import org.sweble.wikitext.engine.utils.TypedWtAstPrinter;

public class VariousTest
		extends
			EngineIntegrationTestBase
{
	private static final String INPUT_SUB_DIR = "various/wikitext";

	private static final String EXPECTED_AST_SUB_DIR = "various/ast";

	private File inputSubDir;

	// =========================================================================

	public VariousTest()
	{
		super(getTestResourcesFixture());
		inputSubDir = new File(getResources().getBaseDirectory(), INPUT_SUB_DIR);
	}

	// =========================================================================

	@Test
	public void testCaseInsensitiveTagExtensions() throws Exception
	{
		File inputFile = new File(
				inputSubDir,
				"uc-pre.wikitext");

		getConfig().setTagExtensionNamesCaseSensitive(false);

		expandPostprocessPrintAndCompare(
				inputFile,
				INPUT_SUB_DIR,
				EXPECTED_AST_SUB_DIR,
				new TypedWtAstPrinter());
	}
}
