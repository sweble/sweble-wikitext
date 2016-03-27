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
package org.sweble.wikitext.engine.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.engine.ExpansionCallback;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.FullPage;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.parser.parser.LinkTargetException;

import de.fau.cs.osr.ptk.common.PrinterInterface;
import de.fau.cs.osr.utils.FileCompare;
import de.fau.cs.osr.utils.FileContent;
import de.fau.cs.osr.utils.FileTools;
import de.fau.cs.osr.utils.StringTools;
import de.fau.cs.osr.utils.TestResourcesFixture;
import de.fau.cs.osr.utils.WrappedException;

public abstract class EngineIntegrationTestBase
{
	private static final Logger logger = LoggerFactory.getLogger(EngineIntegrationTestBase.class);

	private final TestResourcesFixture resources;

	private final WikiConfigImpl config;

	private final WtEngineImpl engine;

	// =========================================================================

	protected static TestResourcesFixture getTestResourcesFixture()
	{
		try
		{
			File path = TestResourcesFixture.resourceNameToFile(
					EngineIntegrationTestBase.class, "/");

			return new TestResourcesFixture(path);
		}
		catch (FileNotFoundException e)
		{
			throw new WrappedException(e);
		}
	}

	// =========================================================================

	public EngineIntegrationTestBase(TestResourcesFixture resources)
	{
		this.resources = resources;
		this.config = DefaultConfigEnWp.generate();
		this.engine = new WtEngineImpl(config);
	}

	// =========================================================================

	public TestResourcesFixture getResources()
	{
		return resources;
	}

	public WikiConfigImpl getConfig()
	{
		return config;
	}

	public WtEngineImpl getEngine()
	{
		return engine;
	}

	// =========================================================================

	public void expandPrintAndCompare(
			File inputFile,
			String inputSubDir,
			String expectedSubDir,
			ExpansionCallback callback,
			boolean forInclusion,
			PrinterInterface printer) throws IOException, LinkTargetException, EngineException
	{
		FileContent inputFileContent = new FileContent(inputFile);

		String fileTitle = inputFile.getName();
		int i = fileTitle.lastIndexOf('.');
		if (i != -1)
			fileTitle = fileTitle.substring(0, i);

		PageTitle title = PageTitle.make(config, fileTitle);
		PageId pageId = new PageId(title, -1);
		EngProcessedPage ast = engine.expand(
				pageId,
				inputFileContent.getContent(),
				forInclusion,
				callback);

		String actual = printToString(ast.getPage(), printer);

		File expectedFile = TestResourcesFixture.rebase(
				inputFile,
				inputSubDir,
				expectedSubDir,
				printer.getPrintoutType(),
				true /* don't throw if file doesn't exist */);

		FileCompare cmp = new FileCompare(getResources());
		cmp.compareWithExpectedOrGenerateExpectedFromActual(expectedFile, actual);
	}

	public void expandPrintAndCompare(
			File inputFile,
			String inputSubDir,
			String expectedSubDir,
			ExpansionCallback callback,
			boolean forInclusion) throws IOException, LinkTargetException, EngineException
	{
		TypedEnginePrettyPrinter printer = new TypedEnginePrettyPrinter();
		expandPrintAndCompare(inputFile, inputSubDir, expectedSubDir, callback, forInclusion, printer);
	}

	public void expandPrintAndCompare(
			File inputFile,
			String inputSubDir,
			String expectedSubDir) throws IOException, LinkTargetException, EngineException
	{
		TypedEnginePrettyPrinter printer = new TypedEnginePrettyPrinter();
		expandPrintAndCompare(
				inputFile,
				inputSubDir,
				expectedSubDir,
				printer);
	}

	public void expandPrintAndCompare(
			File inputFile,
			String inputSubDir,
			String expectedSubDir,
			PrinterInterface printer) throws IOException, LinkTargetException, EngineException
	{
		ExpansionCallback callback = new TestExpansionCallback(inputSubDir);

		boolean forInclusion = false;

		expandPrintAndCompare(
				inputFile,
				inputSubDir,
				expectedSubDir,
				callback,
				forInclusion,
				printer);
	}

	// =========================================================================

	public String printToString(Object ast, PrinterInterface printer) throws IOException
	{
		StringWriter writer = new StringWriter();

		printer.print(ast, writer);

		String result = writer.toString();

		// We always operate with UNIX line end '\n':
		result = FileTools.lineEndToUnix(result);

		return resources.stripBaseDirectoryAndFixPath(result);
	}

	// =========================================================================

	private final class TestExpansionCallback
			implements
				ExpansionCallback
	{
		private final String searchDir;

		public TestExpansionCallback(String searchDir)
		{
			this.searchDir = searchDir;
		}

		@Override
		public FullPage retrieveWikitext(
				ExpansionFrame expansionFrame,
				PageTitle pageTitle)
		{
			String fileTitle = pageTitle.getNormalizedFullTitle();
			File base = new File(getResources().getBaseDirectory(), searchDir);
			File file = new File(base, StringTools.safeFilename(fileTitle));
			if (!file.exists())
			{
				logger.warn("Could not find page " + pageTitle + " at " + file);
				return null;
			}
			else
			{
				logger.trace("Retrieving wikitext: " + file);
				PageId pageId = new PageId(pageTitle, -1);
				try
				{
					String text = FileUtils.readFileToString(file, "UTF8");
					return new FullPage(pageId, text);
				}
				catch (IOException e)
				{
					logger.warn("Failed to retrieve wikitext for page " + pageTitle + " at " + file, e);
					return null;
				}
			}
		}

		@Override
		public String fileUrl(PageTitle pageTitle, int width, int height)
		{
			return null;
		}
	}
}
