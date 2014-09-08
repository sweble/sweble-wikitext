/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.swcadapter;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.io.FilenameUtils;
import org.sweble.wikitext.engine.ExpansionCallback;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wom3.swcadapter.utils.WtWom3Toolbox;
import org.sweble.wom3.util.Wom3Toolbox;

import de.fau.cs.osr.utils.FileCompare;
import de.fau.cs.osr.utils.TestResourcesFixture;
import de.fau.cs.osr.utils.WrappedException;

public class WtWom3IntegrationTestBase
		extends
			WtWom3Toolbox
{
	private final TestResourcesFixture resources;
	
	// =========================================================================
	
	protected static TestResourcesFixture getTestResourcesFixture()
	{
		try
		{
			File path = TestResourcesFixture.resourceNameToFile(
					WtWom3IntegrationTestBase.class, "/");
			
			return new TestResourcesFixture(path);
		}
		catch (FileNotFoundException e)
		{
			throw new WrappedException(e);
		}
	}
	
	// =========================================================================
	
	protected WtWom3IntegrationTestBase(TestResourcesFixture resources)
	{
		this.resources = resources;
		
		// For testing we don't want stuff to be removed
		getWikiConfig().getEngineConfig().setTrimTransparentBeforeParsing(false);
	}
	
	// =========================================================================
	
	protected TestResourcesFixture getResources()
	{
		return resources;
	}
	
	// =========================================================================
	
	public void convertPrintAndCompare(
			EngProcessedPage ast,
			File fakeInputFile,
			String inputSubDir,
			String expectedSubDir) throws Exception
	{
		ExpansionCallback callback = new TestExpansionCallback();
		
		convertPrintAndCompare(
				ast,
				fakeInputFile,
				inputSubDir,
				expectedSubDir,
				callback);
	}
	
	public void parsePrintAndCompare(
			File inputFile,
			String inputSubDir,
			String expectedSubDir) throws Exception
	{
		ExpansionCallback callback = new TestExpansionCallback();
		
		parsePrintAndCompare(
				inputFile,
				inputSubDir,
				expectedSubDir,
				callback);
	}
	
	/**
	 * Parse Wikitext to an AST and convert this AST to a WOM tree. Print the
	 * WOM tree as XML and compare with expected XML from file.
	 */
	public void parsePrintAndCompare(
			File inputFile,
			String inputSubDir,
			String expectedSubDir,
			ExpansionCallback callback) throws Exception
	{
		String fileTitle = FilenameUtils.getBaseName(inputFile.getName());
		
		PageId pageId = makePageId(fileTitle);
		
		Artifacts afs = wmToWom(inputFile, pageId, callback);
		
		String actual = Wom3Toolbox.printWom(afs.womDoc);
		
		File expectedFile = TestResourcesFixture.rebase(
				inputFile,
				inputSubDir,
				expectedSubDir,
				"wom.xml",
				true /* don't throw if file doesn't exist */);
		
		FileCompare cmp = new FileCompare(getResources());
		cmp.compareWithExpectedOrGenerateExpectedFromActual(expectedFile, actual);
	}
	
	/**
	 * Convert an AST to a WOM tree. Print the WOM tree as XML and compare with
	 * expected XML from file.
	 */
	public void convertPrintAndCompare(
			EngProcessedPage ast,
			File fakeInputFile,
			String inputSubDir,
			String expectedSubDir,
			ExpansionCallback callback) throws Exception
	{
		PageId pageId = makePageId("-");
		
		Artifacts afs = astToWom(ast, pageId, callback);
		
		String actual = Wom3Toolbox.printWom(afs.womDoc);
		
		File expectedFile = TestResourcesFixture.rebase(
				fakeInputFile,
				inputSubDir,
				expectedSubDir,
				"wom.xml",
				true /* don't throw if file doesn't exist */);
		
		FileCompare cmp = new FileCompare(getResources());
		cmp.compareWithExpectedOrGenerateExpectedFromActual(expectedFile, actual);
	}
	
	/**
	 * Parse Wikitext to an AST and convert this AST to a WOM tree. Restore the
	 * original Wiki from the WOM tree using an XPath expression. Compare the
	 * resulting Wikitext with expected Wikitext from file.
	 */
	public void parsePrintExtractRtdAndCompare(
			File inputFile,
			String inputSubDir) throws Exception
	{
		ExpansionCallback callback = new TestExpansionCallback();
		
		parsePrintExtractRtdAndCompare(
				inputFile,
				inputSubDir,
				callback);
	}
	
	/**
	 * Parse Wikitext to an AST and convert this AST to a WOM tree. Restore the
	 * original Wiki from the WOM tree using an XPath expression. Compare the
	 * resulting Wikitext with expected Wikitext from file.
	 */
	public void parsePrintExtractRtdAndCompare(
			File inputFile,
			String inputSubDir,
			ExpansionCallback callback) throws Exception
	{
		String fileTitle = FilenameUtils.getBaseName(inputFile.getName());
		
		PageId pageId = makePageId(fileTitle);
		
		Artifacts afs = wmToWom(inputFile, pageId, callback);
		
		String actual = Wom3Toolbox.womToWmXPath(afs.womDoc);
		
		FileCompare cmp = new FileCompare(getResources());
		cmp.compareWithExpectedOrGenerateExpectedFromActual(inputFile, actual);
	}
}
