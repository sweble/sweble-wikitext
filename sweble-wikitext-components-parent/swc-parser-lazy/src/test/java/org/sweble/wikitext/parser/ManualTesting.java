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
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wikitext.parser.encval.ValidatedWikitext;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.nodes.WtPreproWikitextPage;
import org.sweble.wikitext.parser.parser.PreprocessorToParserTransformer;
import org.sweble.wikitext.parser.postprocessor.TicksAnalyzer;
import org.sweble.wikitext.parser.postprocessor.TreeBuilder;
import org.sweble.wikitext.parser.preprocessor.PreprocessedWikitext;
import org.sweble.wikitext.parser.utils.NonExpandingParser;
import org.sweble.wikitext.parser.utils.SimpleParserConfig;
import org.sweble.wikitext.parser.utils.TypedPrettyPrinter;
import org.sweble.wikitext.parser.utils.TypedRtDataPrinter;
import org.sweble.wikitext.parser.utils.TypedWtAstPrinter;

import de.fau.cs.osr.ptk.common.PrinterInterface;
import de.fau.cs.osr.utils.FileCompare;
import de.fau.cs.osr.utils.FileContent;
import de.fau.cs.osr.utils.NamedParametrized;
import de.fau.cs.osr.utils.TestResourcesFixture;
import xtc.parser.ParseException;

@RunWith(value = NamedParametrized.class)
public class ManualTesting
		extends
			ParserIntegrationTestBase
{
	private static final String FILTER_RX = ".*?\\.wikitext";

	private static final String INPUT_SUB_DIR = "nopkg-manual";

	// =========================================================================

	public static void main(String[] args) throws Exception
	{
		for (Object[] input : enumerateInputs())
		{
			String title = (String) input[0];
			TestResourcesFixture resources = (TestResourcesFixture) input[1];
			File inputFile = (File) input[2];
			ManualTesting test = new ManualTesting(title, resources, inputFile);
			test.runTests();
		}
	}

	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		TestResourcesFixture resources = getTestResourcesFixture();

		File dir = new File(resources.getBaseDirectory(), INPUT_SUB_DIR);
		if (!dir.exists() || !dir.isDirectory())
			return Collections.emptyList();

		return resources.gatherAsParameters(INPUT_SUB_DIR, FILTER_RX, false);
	}

	// =========================================================================

	private final File inputFile;

	private SimpleParserConfig parserConfig;

	// =========================================================================

	public ManualTesting(
			String title,
			TestResourcesFixture resources,
			File inputFile)
	{
		super(resources, new NonExpandingParser() /* not used */);
		this.inputFile = inputFile;
		this.parserConfig = new SimpleParserConfig();

	}

	// =========================================================================

	@Test
	public void runTests() throws Exception
	{
		FileContent inputFileContent = new FileContent(inputFile);

		WtParsedWikitextPage ast = parseArticle(
				inputFileContent.getContent(),
				inputFile.getAbsolutePath());

		printRtdComparison(ast);
		prettyPrintWikiMarkupComparison(ast);
	}

	// =========================================================================

	public WtParsedWikitextPage parseArticle(String source, String title) throws IOException, ParseException
	{
		// Encoding validation

		WikitextEncodingValidator v = new WikitextEncodingValidator();

		ValidatedWikitext validated = v.validate(parserConfig, source, title);

		// Pre-processing

		WikitextPreprocessor prep = new WikitextPreprocessor(parserConfig);

		WtPreproWikitextPage prepArticle =
				(WtPreproWikitextPage) prep.parseArticle(validated, title, false);

		// Parsing

		PreprocessedWikitext ppw = PreprocessorToParserTransformer
				.transform(prepArticle);

		WikitextParser p = new WikitextParser(parserConfig);

		WtParsedWikitextPage parsedArticle =
				(WtParsedWikitextPage) p.parseArticle(ppw, title);

		printAst(parsedArticle, "-parsed.ast");

		// Post-processing

		WtNode ticksAnalyzed = TicksAnalyzer.process(parserConfig, parsedArticle);

		printAst(ticksAnalyzed, "-ticks-analyzed.ast");

		WtParsedWikitextPage treeBuilt = TreeBuilder.process(parserConfig, ticksAnalyzed);

		printAst(treeBuilt, "-tree-built.ast");

		return treeBuilt;
	}

	private void printAst(WtNode ast, String tag) throws IOException
	{
		File output = new File(inputFile.getName() + tag);
		PrinterInterface printer = new TypedWtAstPrinter();
		String actual = printToString(ast, printer);
		FileUtils.writeStringToFile(output, actual, "UTF8");
	}

	private void printRtdComparison(WtParsedWikitextPage ast) throws IOException
	{
		File expectedFile = inputFile;
		PrinterInterface printer = new TypedRtDataPrinter();
		String actual = printToString(ast, printer);
		File output = new File(inputFile.getName() + "-printed-from-rtd.wikitext");
		FileUtils.writeStringToFile(output, actual, "UTF8");
		FileCompare cmp = new FileCompare(getResources());
		cmp.compareWithExpectedOrGenerateExpectedFromActual(expectedFile, actual);
	}

	private void prettyPrintWikiMarkupComparison(WtParsedWikitextPage ast) throws IOException
	{
		File expectedFile = inputFile;
		PrinterInterface printer = new TypedPrettyPrinter();
		String actual = printToString(ast, printer);
		File output = new File(inputFile.getName() + "-pretty-printed.wikitext");
		FileUtils.writeStringToFile(output, actual, "UTF8");
		FileCompare cmp = new FileCompare(getResources());
		cmp.compareWithExpectedOrGenerateExpectedFromActual(expectedFile, actual);
	}
}
