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
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.sweble.wikitext.engine.CompilerException;
import org.sweble.wikitext.engine.ExpansionCallback;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.FullPage;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngine;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.nodes.EngCompiledPage;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.parser.LinkTargetException;

import de.fau.cs.osr.ptk.common.ParserInterface;
import de.fau.cs.osr.ptk.common.test.FileCompare;
import de.fau.cs.osr.ptk.common.test.FileContent;
import de.fau.cs.osr.ptk.common.test.IntegrationTestBase;
import de.fau.cs.osr.ptk.common.test.TestResourcesFixture;
import de.fau.cs.osr.utils.StringUtils;

public abstract class EngineIntegrationTestBase
		extends
			IntegrationTestBase<WtNode>
{
	private static final Logger logger = Logger.getLogger(EngineIntegrationTestBase.class);
	
	private final WikiConfigImpl config;
	
	private final WtEngine engine;
	
	// =========================================================================
	
	public EngineIntegrationTestBase()
	{
		this.config = DefaultConfigEn.generate();
		this.engine = new WtEngine(config);
	}
	
	// =========================================================================
	
	public WikiConfigImpl getConfig()
	{
		return config;
	}
	
	public WtEngine getEngine()
	{
		return engine;
	}
	
	@Override
	public ParserInterface<WtNode> instantiateParser()
	{
		return null;
	}
	
	// =========================================================================
	
	public void expandPrintAndCompare(
			File inputFile,
			String inputSubDir,
			String expectedSubDir,
			ExpansionCallback callback,
			boolean forInclusion) throws IOException, LinkTargetException, CompilerException
	{
		FileContent inputFileContent = new FileContent(inputFile);
		
		String fileTitle = inputFile.getName();
		int i = fileTitle.lastIndexOf('.');
		if (i != -1)
			fileTitle = fileTitle.substring(0, i);
		
		PageTitle title = PageTitle.make(config, fileTitle);
		PageId pageId = new PageId(title, -1);
		EngCompiledPage ast = engine.expand(
				pageId,
				inputFileContent.getContent(),
				forInclusion,
				callback);
		
		TypedEnginePrettyPrinter pp = new TypedEnginePrettyPrinter();
		
		String actual = printToString(ast.getPage(), pp);
		
		File expectedFile = TestResourcesFixture.rebase(
				inputFile,
				inputSubDir,
				expectedSubDir,
				pp.getPrintoutType(),
				true /* don't throw if file doesn't exist */);
		
		FileCompare cmp = new FileCompare(getResources());
		cmp.compareWithExpectedOrGenerateExpectedFromActual(expectedFile, actual);
	}
	
	// =========================================================================
	
	public void expandPrintAndCompare(
			File inputFile,
			String inputSubDir,
			String expectedSubDir) throws IOException, LinkTargetException, CompilerException
	{
		ExpansionCallback callback = new TestExpansionCallback(inputSubDir);
		
		boolean forInclusion = false;
		
		expandPrintAndCompare(
				inputFile,
				inputSubDir,
				expectedSubDir,
				callback,
				forInclusion);
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
				PageTitle pageTitle) throws Exception
		{
			String fileTitle = pageTitle.getNormalizedFullTitle();
			File base = new File(getResources().getBaseDirectory(), searchDir);
			File file = new File(base, StringUtils.safeFilename(fileTitle));
			if (!file.exists())
			{
				logger.warn("Could not find page " + pageTitle + " at " + file);
				return null;
			}
			else
			{
				logger.trace("Retrieving wikitext: " + file);
				PageId pageId = new PageId(pageTitle, -1);
				String text = FileUtils.readFileToString(file, "UTF8");
				return new FullPage(pageId, text);
			}
		}
		
		@Override
		public String fileUrl(PageTitle pageTitle, int width, int height) throws Exception
		{
			return null;
		}
	}
}
