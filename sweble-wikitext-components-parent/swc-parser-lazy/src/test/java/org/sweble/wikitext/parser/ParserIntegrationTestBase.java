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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;

import org.junit.Assert;
import org.sweble.wikitext.parser.nodes.WtBody;
import org.sweble.wikitext.parser.nodes.WtEmptyImmutableNode;
import org.sweble.wikitext.parser.nodes.WtLinkOptionGarbage;
import org.sweble.wikitext.parser.nodes.WtNewline;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtPage;
import org.sweble.wikitext.parser.nodes.WtParagraph;
import org.sweble.wikitext.parser.nodes.WtTableCaption;
import org.sweble.wikitext.parser.nodes.WtTableCell;
import org.sweble.wikitext.parser.nodes.WtTableHeader;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtXmlAttributeGarbage;
import org.sweble.wikitext.parser.utils.TypedPrettyPrinter;
import org.sweble.wikitext.parser.utils.TypedWtAstPrinter;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ParserCommon;
import de.fau.cs.osr.ptk.common.PrinterInterface;
import de.fau.cs.osr.ptk.common.Warning;
import de.fau.cs.osr.utils.FileCompare;
import de.fau.cs.osr.utils.FileContent;
import de.fau.cs.osr.utils.FileTools;
import de.fau.cs.osr.utils.TestResourcesFixture;
import de.fau.cs.osr.utils.WrappedException;
import xtc.parser.ParseException;

public abstract class ParserIntegrationTestBase
{
	private final TestResourcesFixture resources;

	private final ParserConfig config;

	private final ParserCommon<WtNode> parser;

	// =========================================================================

	protected static TestResourcesFixture getTestResourcesFixture()
	{
		try
		{
			File path = TestResourcesFixture.resourceNameToFile(
					ParserIntegrationTestBase.class, "/");

			return new TestResourcesFixture(path);
		}
		catch (FileNotFoundException e)
		{
			throw new WrappedException(e);
		}
	}

	// =========================================================================

	protected ParserIntegrationTestBase(
			TestResourcesFixture resources,
			ParserCommon<WtNode> parser)
	{
		this.resources = resources;
		this.parser = parser;
		this.config = (ParserConfig) parser.getConfig();
	}

	// =========================================================================

	protected TestResourcesFixture getResources()
	{
		return resources;
	}

	protected ParserConfig getConfig()
	{
		return config;
	}

	protected ParserCommon<WtNode> getParser()
	{
		return parser;
	}

	// =========================================================================

	protected void parsePrintAndCompare(
			File inputFile,
			String inputSubDir,
			String expectedSubDir,
			PrinterInterface printer) throws IOException, ParseException
	{
		@SuppressWarnings({ "unchecked" })
		AstVisitor<WtNode>[] visitors = new AstVisitor[] {};

		parsePrintAndCompare(
				inputFile,
				visitors,
				inputSubDir,
				expectedSubDir,
				printer);
	}

	/**
	 * Parse an input file, use the specified visitors for post-processing,
	 * print the resulting AST as text using the given printer instance and
	 * compare the result with a file with the expected output.
	 * 
	 * The expected output has to be located in a file that has the same name as
	 * the input file but possibly resides in a different sub-directory.
	 * 
	 * <b>Important:</b> Always use the UNIX file separator '/'.
	 */
	protected void parsePrintAndCompare(
			File inputFile,
			AstVisitor<WtNode>[] visitors,
			String inputSubDir,
			String expectedSubDir,
			PrinterInterface printer) throws IOException, ParseException
	{
		Object ast = parse(inputFile, visitors);

		String actual = printToString(ast, printer);

		File expectedFile = TestResourcesFixture.rebase(
				inputFile,
				inputSubDir,
				expectedSubDir,
				printer.getPrintoutType(),
				true /* don't throw if file doesn't exist */);

		FileCompare cmp = new FileCompare(resources);
		cmp.compareWithExpectedOrGenerateExpectedFromActual(expectedFile, actual);
	}

	protected void prettyPrintAstAndCompare(
			File inputFile,
			String inputSubDir,
			String expectedSubDir) throws IOException, ParseException
	{
		// -- pretty print

		@SuppressWarnings("unchecked")
		AstVisitor<WtNode>[] visitors = new AstVisitor[] {};

		Object originalAst = parse(inputFile, visitors);

		String ppOriginalAst = printToString(originalAst, new TypedPrettyPrinter());

		// -- generate cleaned ASTs from original and pretty printed wikitext

		@SuppressWarnings("unchecked")
		AstVisitor<WtNode>[] cleaningVisitors = new AstVisitor[] { new CleanupAst(config) };

		for (AstVisitor<WtNode> v : cleaningVisitors)
			parser.addVisitor(v);

		Object cleanedReparsedAst = parser.parseArticle(ppOriginalAst, "pp");

		Object cleanedOriginalAst = parse(inputFile, cleaningVisitors);

		// -- print and compare ASTs

		TypedWtAstPrinter astPrinter = new TypedWtAstPrinter();

		String reparsedAstPrinted = printToString(cleanedReparsedAst, astPrinter);

		try
		{
			File expectedFile = TestResourcesFixture.rebase(
					inputFile,
					inputSubDir,
					expectedSubDir,
					astPrinter.getPrintoutType(),
					false /* throw if file doesn't exist */);

			FileCompare cmp = new FileCompare(resources);
			reparsedAstPrinted = cmp.fixActualText(reparsedAstPrinted);
			cmp.assertEquals(expectedFile, reparsedAstPrinted);
		}
		catch (IllegalArgumentException e)
		{
			String originalAstPrinted = printToString(cleanedOriginalAst, astPrinter);

			Assert.assertEquals(originalAstPrinted, reparsedAstPrinted);
		}
	}

	protected void prettyPrintWikitextAndCompare(
			AstVisitor<WtNode>[] visitors,
			File inputFile,
			String inputSubDir,
			String expectedSubDir) throws IOException, ParseException
	{
		// -- pretty print

		Object ast = parse(inputFile, visitors);

		TypedPrettyPrinter printer = new TypedPrettyPrinter();
		String actual = printToString(ast, printer);

		// -- print and compare ASTs

		FileCompare cmp = new FileCompare(resources);
		actual = cmp.fixActualText(actual);
		try
		{
			File expectedFile = TestResourcesFixture.rebase(
					inputFile,
					inputSubDir,
					expectedSubDir,
					printer.getPrintoutType(),
					false /* throw if file doesn't exist */);

			cmp.assertEquals(expectedFile, actual);
		}
		catch (IllegalArgumentException e)
		{
			cmp.assertEquals(inputFile, actual);
		}
	}

	// =========================================================================

	protected Object parse(File inputFile) throws IOException, ParseException
	{
		FileContent inputFileContent = new FileContent(inputFile);

		return parser.parseArticle(
				inputFileContent.getContent(),
				inputFile.getAbsolutePath());
	}

	protected Object parse(File inputFile, AstVisitor<WtNode>[] visitors) throws IOException, ParseException
	{
		for (AstVisitor<WtNode> visitor : visitors)
			parser.addVisitor(visitor);

		FileContent inputFileContent = new FileContent(inputFile);

		return parser.parseArticle(
				inputFileContent.getContent(),
				inputFile.getAbsolutePath());
	}

	protected String printToString(Object ast, PrinterInterface printer) throws IOException
	{
		StringWriter writer = new StringWriter();

		printer.print(ast, writer);

		String result = writer.toString();

		// We always operate with UNIX line end '\n':
		result = FileTools.lineEndToUnix(result);

		return resources.stripBaseDirectoryAndFixPath(result);
	}

	// =========================================================================

	protected static final class CleanupAst
			extends
				AstVisitor<WtNode>
	{
		private final ParserConfig config;

		public CleanupAst(ParserConfig config)
		{
			this.config = config;
		}

		public WtPage visit(WtPage n)
		{
			n.setEntityMap(WtEntityMap.EMPTY_ENTITY_MAP);
			n.setWarnings(Collections.<Warning> emptyList());
			clearNode(n);

			mapInPlace(n);
			int last = n.size() - 1;
			if (last >= 0 && n.get(last).isNodeType(WtNode.NT_NEWLINE))
				n.remove(last);

			return n;
		}

		public Object visit(WtNewline n)
		{
			return REMOVE;
		}

		public WtNode visit(WtTableCaption n)
		{
			clearNode(n);
			mapInPlace(n);
			removeParagraph(n.getBody());
			return n;
		}

		public WtNode visit(WtTableHeader n)
		{
			clearNode(n);
			mapInPlace(n);
			removeParagraph(n.getBody());
			return n;
		}

		public WtNode visit(WtTableCell n)
		{
			clearNode(n);
			mapInPlace(n);
			removeParagraph(n.getBody());
			return n;
		}

		private void removeParagraph(WtBody n)
		{
			if (n.isEmpty())
				return;

			WtNode first = n.get(0);
			if (first.isNodeType(WtNode.NT_PARAGRAPH))
			{
				n.remove(0);
				n.addAll(0, first);
			}
		}

		public Object visit(WtParagraph n)
		{
			WtNodeList l = config.getNodeFactory().list();
			l.exchange(n);
			mapInPlace(l);
			return l;
		}

		public Object visit(WtText n)
		{
			clearNode(n);
			n.setContent(n.getContent().trim());
			if (n.getContent().isEmpty())
				return REMOVE;
			return n;
		}

		public Object visit(WtLinkOptionGarbage n)
		{
			return REMOVE;
		}

		public Object visit(WtXmlAttributeGarbage n)
		{
			return REMOVE;
		}

		public WtNode visit(WtNode n)
		{
			if (!(n instanceof WtEmptyImmutableNode))
				clearNode(n);

			mapInPlace(n);
			return n;
		}

		private void clearNode(WtNode n)
		{
			n.clearRtd();
			n.clearAttributes();
		}
	}
}
