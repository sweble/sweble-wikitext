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
import org.sweble.wikitext.parser.utils.AstCompressor;
import org.sweble.wikitext.parser.utils.FullParser;
import org.sweble.wikitext.parser.utils.TypedWtAstPrinter;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ParserInterface;
import de.fau.cs.osr.ptk.common.test.IntegrationTestBase;
import de.fau.cs.osr.utils.NamedParametrized;

@RunWith(value = NamedParametrized.class)
public class ComplexIntegrationTest
		extends
			IntegrationTestBase
{
	private static final String FILTER_RX = ".*?\\.wikitext";
	
	private static final String INPUT_SUB_DIR = "complex/wikitext";
	
	private static final String EXPECTED_SUB_DIR = "complex/ast";
	
	// =========================================================================
	
	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		return IntegrationTestBase.gather(INPUT_SUB_DIR, FILTER_RX, true);
	}
	
	// =========================================================================
	
	private final File inputFile;
	
	// =========================================================================
	
	public ComplexIntegrationTest(String title, File inputFile)
	{
		this.inputFile = inputFile;
	}
	
	@Override
	protected ParserInterface instantiateParser()
	{
		return new FullParser();
	}
	
	// =========================================================================
	
	@Test
	public void testAstAfterPostprocessingMatchesReferenceAst() throws Exception
	{
		AstVisitor[] visitors = { new AstCompressor() };
		
		parsePrintAndCompare(
				inputFile,
				visitors,
				INPUT_SUB_DIR,
				EXPECTED_SUB_DIR,
				new TypedWtAstPrinter());
	}
}
