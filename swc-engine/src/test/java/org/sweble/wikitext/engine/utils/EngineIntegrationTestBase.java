package org.sweble.wikitext.engine.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.sweble.wikitext.engine.CompilerException;
import org.sweble.wikitext.engine.ExpansionCallback;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.FullPage;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngine;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngCompiledPage;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wikitext.parser.utils.TypedPrettyPrinter;

import de.fau.cs.osr.ptk.common.ParserInterface;
import de.fau.cs.osr.ptk.common.test.FileCompare;
import de.fau.cs.osr.ptk.common.test.FileContent;
import de.fau.cs.osr.ptk.common.test.IntegrationTestBase;
import de.fau.cs.osr.ptk.common.test.TestResourcesFixture;

public abstract class EngineIntegrationTestBase
		extends
			IntegrationTestBase<WtNode>
{
	private static final Logger logger = Logger.getLogger(EngineIntegrationTestBase.class);
	
	private final WikiConfig config;
	
	private final WtEngine engine;
	
	// =========================================================================
	
	public EngineIntegrationTestBase()
	{
		this.config = DefaultConfigEn.generate();
		this.engine = new WtEngine(config);
	}
	
	// =========================================================================
	
	public WikiConfig getConfig()
	{
		return config;
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
		
		String actual = printToString(ast.getPage(), new TypedPrettyPrinter());
		
		File expectedFile = TestResourcesFixture.rebase(
				inputFile,
				inputSubDir,
				expectedSubDir,
				new TypedPrettyPrinter().getPrintoutType(),
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
			File file = new File(base, encodeFileTitle(fileTitle));
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
		
		private String encodeFileTitle(String fileTitle) throws UnsupportedEncodingException
		{
			StringBuilder b = new StringBuilder();
			for (int i = 0; i < fileTitle.length(); ++i)
			{
				char ch = fileTitle.charAt(i);
				switch (ch)
				{
					case ' ':
					case '_':
						b.append(ch);
						break;
					
					default:
						if ((ch >= '0' && ch <= '9')
								|| (ch >= 'A' && ch <= 'Z')
								|| (ch >= 'a' && ch <= 'z')
								|| (ch == ' ') || (ch == '_'))
						{
							b.append(ch);
						}
						else
						{
							b.append(String.format("%%%02X", (int) ch));
						}
				}
			}
			return b.toString();
		}
		
		@Override
		public String fileUrl(PageTitle pageTitle, int width, int height) throws Exception
		{
			return null;
		}
	}
}
