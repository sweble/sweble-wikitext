/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.swcadapter;

import java.io.File;
import java.util.Collections;

import org.junit.Test;
import org.sweble.wikitext.engine.nodes.EngPage;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.nodes.EngineNodeFactory;

import de.fau.cs.osr.ptk.common.Warning;

/**
 * Tests if conversion from an AST to WOM works for special cases.
 */
public class CustomAstIntegrationTest
		extends
			WtWom3IntegrationTestBase
{
	private static final String INPUT_SUB_DIR = "nopkg-custom/wikitext";
	
	private static final String EXPECTED_WOM_SUB_DIR = "nopkg-custom/wom";
	
	private final EngineNodeFactory nf;
	
	private EngPage page;
	
	private EngProcessedPage procPage;
	
	// =========================================================================
	
	public CustomAstIntegrationTest()
	{
		super(getTestResourcesFixture());
		this.nf = getWikiConfig().getNodeFactory();
		this.page = nf.page(nf.list());
		this.procPage = nf.processedPage(
				page,
				nf.logProcessingPass(),
				Collections.<Warning> emptyList());
	}
	
	// =========================================================================
	
	@Test
	public void testIllegalUrlSyntaxShouldNotThrow() throws Exception
	{
		page.add(nf.url("", "//a-url-must-have-a-protocol"));
		runTest();
	}
	
	// =========================================================================
	
	private void runTest() throws Exception
	{
		File fakeInputFile = new File(
				new File(getResources().getBaseDirectory(), INPUT_SUB_DIR),
				"illegal-url-syntax");
		
		convertPrintAndCompare(
				this.procPage,
				fakeInputFile,
				INPUT_SUB_DIR,
				EXPECTED_WOM_SUB_DIR);
	}
}
