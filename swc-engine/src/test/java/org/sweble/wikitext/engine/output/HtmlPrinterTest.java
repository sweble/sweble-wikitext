package org.sweble.wikitext.engine.output;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wikitext.engine.ExpansionCallback;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngCompiledPage;
import org.sweble.wikitext.engine.utils.EngineIntegrationTestBase;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.AstCompressor;
import org.sweble.wikitext.parser.utils.FullParser;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ParserInterface;
import de.fau.cs.osr.ptk.common.test.FileCompare;
import de.fau.cs.osr.ptk.common.test.FileContent;
import de.fau.cs.osr.ptk.common.test.TestResourcesFixture;
import de.fau.cs.osr.utils.NamedParametrized;

@RunWith(value = NamedParametrized.class)
public class HtmlPrinterTest
		extends
			EngineIntegrationTestBase
{
	private static final String FILTER_RX = ".*?\\.wikitext";
	
	private static final String INPUT_SUB_DIR = "engine/output/wikitext";
	
	private static final String EXPECTED_AST_SUB_DIR = "engine/output/rendered";
	
	// =========================================================================
	
	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		return EngineIntegrationTestBase.gather(INPUT_SUB_DIR, FILTER_RX, true);
	}
	
	// =========================================================================
	
	private final File inputFile;
	
	// =========================================================================
	
	public HtmlPrinterTest(String title, File inputFile)
	{
		this.inputFile = inputFile;
	}
	
	@Override
	public ParserInterface<WtNode> instantiateParser()
	{
		return new FullParser();
	}
	
	// =========================================================================
	
	@Test
	public void testRenderHtml() throws Exception
	{
		@SuppressWarnings("unchecked")
		AstVisitor<WtNode>[] visitors = new AstVisitor[] { new AstCompressor() };
		
		FileContent inputFileContent = new FileContent(inputFile);
		
		WikiConfig wikiConfig = getConfig();
		PageTitle pageTitle = PageTitle.make(wikiConfig, inputFile.getName());
		PageId pageId = new PageId(pageTitle, -1);
		String wikitext = inputFileContent.getContent();
		ExpansionCallback expCallback = null;
		
		EngCompiledPage ast = getEngine().postprocess(pageId, wikitext, expCallback);
		
		TestCallback rendererCallback = new TestCallback();
		
		String actual = HtmlRenderer.print(rendererCallback, wikiConfig, pageTitle, ast);
		
		File expectedFile = TestResourcesFixture.rebase(
				inputFile,
				INPUT_SUB_DIR,
				EXPECTED_AST_SUB_DIR,
				"html",
				true /* don't throw if file doesn't exist */);
		
		FileCompare cmp = new FileCompare(getResources());
		cmp.compareWithExpectedOrGenerateExpectedFromActual(expectedFile, actual);
	}
	
	private static final class TestCallback
			implements
				HtmlRendererCallback
	{
		@Override
		public boolean resourceExists(PageTitle target)
		{
			/*
			boolean exists = true;
			FullPage page = dbService.fetch(target);
			if (page == null)
			{
				if (mwService.getFilePath(target.getNormalizedFullTitle(), -1, -1) == null)
					exists = false;
			}
			return exists;
			*/
			return false;
		}
		
		@Override
		public MediaInfo getMediaInfo(String title, int width, int height) throws Exception
		{
			return null;
		}
	}
}
