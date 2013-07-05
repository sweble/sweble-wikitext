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
package org.sweble.wikitext.wom.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.sweble.wikitext.engine.AstToWomVisitor;
import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.engine.ExpansionCallback;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.FullPage;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.PfnArgumentMode;
import org.sweble.wikitext.engine.WtEngine;
import org.sweble.wikitext.engine.config.I18nAliasImpl;
import org.sweble.wikitext.engine.config.ParserFunctionGroup;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wom.WomNode;

import de.fau.cs.osr.ptk.common.ParserInterface;
import de.fau.cs.osr.ptk.common.test.FileCompare;
import de.fau.cs.osr.ptk.common.test.FileContent;
import de.fau.cs.osr.ptk.common.test.IntegrationTestBase;
import de.fau.cs.osr.ptk.common.test.TestResourcesFixture;

public abstract class WomIntegrationTestBase
		extends
			IntegrationTestBase<WtNode>
{
	private final WikiConfigImpl config;
	
	private final WtEngine engine;
	
	// =========================================================================
	
	public WomIntegrationTestBase()
	{
		this.config = DefaultConfigEnWp.generate();
		this.config.getEngineConfig().setTrimTransparentBeforeParsing(false);
		this.engine = new WtEngine(config);
		
		// TODO: Improve default config!
		this.config.addI18nAlias(new I18nAliasImpl("notoc", true, Arrays.asList("NOTOC")));
		ParserFunctionGroup pfnGroup = new ParserFunctionGroup("myGroup");
		pfnGroup.addParserFunction(new ParserFunctionBase(config, PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, true, "notoc")
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public WtNode invoke(
					WtNode template,
					ExpansionFrame preprocessorFrame,
					List<? extends WtNode> argsValues)
			{
				// TODO Auto-generated method stub
				return null;
			}
		});
		this.config.addParserFunctionGroup(pfnGroup);
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
	
	public void parsePrintAndCompare(
			File inputFile,
			String inputSubDir,
			String expectedSubDir,
			ExpansionCallback callback) throws IOException, LinkTargetException, EngineException
	{
		FileContent inputFileContent = new FileContent(inputFile);
		
		String fileTitle = inputFile.getName();
		int i = fileTitle.lastIndexOf('.');
		if (i != -1)
			fileTitle = fileTitle.substring(0, i);
		
		PageTitle title = PageTitle.make(config, fileTitle);
		PageId pageId = new PageId(title, -1);
		EngProcessedPage ast = engine.parse(
				pageId,
				inputFileContent.getContent(),
				callback);
		
		//System.out.println(AstPrinter.print((WtNode) ast.getPage()));
		
		AstToWomVisitor astToWomVisitor = new AstToWomVisitor(
				config,
				title,
				"Mr. Tester",
				DateTime.parse("2012-12-07T12:15:30.000+01:00"));
		
		WomNode wom = (WomNode) astToWomVisitor.go(ast.getPage());
		
		TypedWomPrettyPrinter pp = new TypedWomPrettyPrinter(config);
		
		String actual = printToString(wom, pp);
		
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
	
	public void parsePrintAndCompare(
			File inputFile,
			String inputSubDir,
			String expectedSubDir) throws IOException, LinkTargetException, EngineException
	{
		ExpansionCallback callback = new TestExpansionCallback();
		
		parsePrintAndCompare(
				inputFile,
				inputSubDir,
				expectedSubDir,
				callback);
	}
	
	// =========================================================================
	
	public void expandPrintAndCompare(
			File inputFile,
			String inputSubDir,
			String expectedSubDir,
			ExpansionCallback callback) throws IOException, LinkTargetException, EngineException
	{
		FileContent inputFileContent = new FileContent(inputFile);
		
		String fileTitle = inputFile.getName();
		int i = fileTitle.lastIndexOf('.');
		if (i != -1)
			fileTitle = fileTitle.substring(0, i);
		
		if (fileTitle.startsWith("raw-"))
			fileTitle = fileTitle.substring("raw-".length());
		if (fileTitle.startsWith("exp-"))
			fileTitle = fileTitle.substring("exp-".length());
		
		PageTitle title = PageTitle.make(config, fileTitle);
		PageId pageId = new PageId(title, -1);
		EngProcessedPage ast = engine.postprocess(
				pageId,
				inputFileContent.getContent(),
				callback);
		
		//System.out.println(AstPrinter.print((WtNode) ast.getPage()));
		
		AstToWomVisitor astToWomVisitor = new AstToWomVisitor(
				config,
				title,
				"Mr. Tester",
				DateTime.parse("2012-12-07T12:15:30.000+01:00"));
		
		WomNode wom = (WomNode) astToWomVisitor.go(ast.getPage());
		
		TypedWomPrettyPrinter pp = new TypedWomPrettyPrinter(config);
		
		String actual = printToString(wom, pp);
		
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
			String expectedSubDir) throws IOException, LinkTargetException, EngineException
	{
		ExpansionCallback callback = new TestExpansionCallback();
		
		expandPrintAndCompare(
				inputFile,
				inputSubDir,
				expectedSubDir,
				callback);
	}
	
	// =========================================================================
	
	private final class TestExpansionCallback
			implements
				ExpansionCallback
	{
		@Override
		public FullPage retrieveWikitext(
				ExpansionFrame expansionFrame,
				PageTitle pageTitle) throws Exception
		{
			return null;
		}
		
		@Override
		public String fileUrl(PageTitle pageTitle, int width, int height) throws Exception
		{
			return null;
		}
	}
}
