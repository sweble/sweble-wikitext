package org.sweble.wikitext.engine.astwom;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.sweble.wikitext.engine.CompiledPage;
import org.sweble.wikitext.engine.Compiler;
import org.sweble.wikitext.engine.CompilerException;
import org.sweble.wikitext.engine.FullPage;
import org.sweble.wikitext.engine.Page;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.utils.SimpleWikiConfiguration;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomPage;
import org.sweble.wikitext.engine.wom.tools.WomPrinter;
import org.sweble.wikitext.lazy.utils.AstPrinter;
import org.sweble.wikitext.lazy.utils.RtWikitextPrinter;

import de.fau.cs.osr.ptk.common.test.FileCompare;
import de.fau.cs.osr.ptk.common.test.FileContent;
import de.fau.cs.osr.ptk.common.test.TestResourcesFixture;

public class AstWomTestFixture
{
	private static final String WIKI_CONFIGURATION =
			"classpath:/org/sweble/wikitext/engine/SimpleWikiConfiguration.xml";
	
	private static FileCompare compare;
	
	private static TestResourcesFixture resources;
	
	// =========================================================================
	
	public static void womTestFixtureInitialize(
			ClassLoader classLoader,
			String resourceBase,
			String pathPatternToReplace,
			String pathToReplacePatternWith)
	{
		try
		{
			resources = new TestResourcesFixture(classLoader, resourceBase);
			
			compare = new FileCompare(
					resources,
					pathPatternToReplace,
					pathToReplacePatternWith,
					false);
		}
		catch (FileNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static List<File> womTestFixtureGather(
			String subDirectory,
			String pattern,
			boolean recursive)
	{
		return resources.gather(subDirectory, pattern, recursive);
	}
	
	public static AstWomTestFixture womTestFixtureGet()
	{
		return new AstWomTestFixture();
	}
	
	// =========================================================================
	
	private final SimpleWikiConfiguration config;
	
	private final Compiler compiler;
	
	private boolean explicitTextNodes = false;
	
	// =========================================================================
	
	public AstWomTestFixture()
	{
		try
		{
			this.config = new SimpleWikiConfiguration(WIKI_CONFIGURATION);
			
			this.compiler = new Compiler(this.config);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	// =========================================================================
	
	public void setExplicitTextNodes(boolean explicitTextNodes)
	{
		this.explicitTextNodes = explicitTextNodes;
	}
	
	public SimpleWikiConfiguration getConfig()
	{
		return config;
	}
	
	// =========================================================================
	
	public DefaultAstToWomNodeFactory makeFactory() throws Exception
	{
		PageTitle title = PageTitle.make(this.config, "-");
		return makeFactory(title);
	}
	
	public DefaultAstToWomNodeFactory makeFactory(PageTitle title)
	{
		return new DefaultAstToWomNodeFactory(
				this.config,
				title.getFullTitle());
	}
	
	public static WomPage quickPostprocessToWom(String wikitext) throws Exception
	{
		AstWomTestFixture f = new AstWomTestFixture();
		return f.postprocessToWom(wikitext);
	}
	
	public WomPage postprocessToWom(String wikitext) throws Exception
	{
		final PageTitle title = PageTitle.make(this.config, "-" /* title */);
		
		final FullPage fullPage = new FullPage(
				new PageId(title, -1 /* revision */),
				wikitext);
		
		return postprocessToWom(title, fullPage);
	}
	
	public WomPage postprocessToWom(File wikitextFile) throws Exception
	{
		final PageTitle title = PageTitle.make(config, wikitextFile.getName());
		
		final FileContent wikitext = new FileContent(wikitextFile);
		
		final FullPage fullPage = new FullPage(
				new PageId(title, -1 /* revision */),
				wikitext.getContent());
		
		return postprocessToWom(title, fullPage);
	}
	
	public WomPage postprocessToWom(PageTitle title, FullPage fullPage) throws CompilerException, Exception
	{
		final CompiledPage astPage = this.compiler.postprocess(
				fullPage.getId(),
				fullPage.getText(),
				null);
		
		final DefaultAstToWomNodeFactory wnf = makeFactory(title);
		
		return (WomPage) wnf.create(null, astPage.getPage());
	}
	
	public static CompiledPage quickPreprocessToAst(
			String wikitext,
			boolean forInclusion) throws Exception
	{
		AstWomTestFixture f = new AstWomTestFixture();
		return f.preprocessToAst(wikitext, forInclusion);
	}
	
	public CompiledPage preprocessToAst(String wikitext, boolean forInclusion) throws Exception
	{
		final PageTitle title = PageTitle.make(this.config, "-" /* title */);
		
		final FullPage fullPage = new FullPage(
				new PageId(title, -1 /* revision */),
				wikitext);
		
		return preprocessToAst(fullPage, forInclusion);
	}
	
	public CompiledPage preprocessToAst(FullPage fullPage, boolean forInclusion) throws Exception
	{
		return this.compiler.preprocess(
				fullPage.getId(),
				fullPage.getText(),
				forInclusion,
				null);
	}
	
	public CompiledPage postprocessToAst(String wikitext) throws Exception
	{
		final PageTitle title = PageTitle.make(this.config, "-" /* title */);
		
		final FullPage fullPage = new FullPage(
				new PageId(title, -1 /* revision */),
				wikitext);
		
		return postprocessToAst(fullPage);
	}
	
	public CompiledPage postprocessToAst(File wikitextFile) throws Exception
	{
		final PageTitle title = PageTitle.make(config, wikitextFile.getName());
		
		final FileContent wikitext = new FileContent(wikitextFile);
		
		final FullPage fullPage = new FullPage(
				new PageId(title, -1 /* revision */),
				wikitext.getContent());
		
		return postprocessToAst(fullPage);
	}
	
	public CompiledPage postprocessToAst(FullPage fullPage) throws CompilerException, Exception
	{
		return this.compiler.postprocess(
				fullPage.getId(),
				fullPage.getText(),
				null);
	}
	
	public void printAstTest(
			String inputDir,
			String outputDir,
			Page page,
			File inputFile) throws Exception
	{
		File expectedFile = TestResourcesFixture.rebase(
				inputFile,
				inputDir,
				outputDir,
				"ast",
				true /* don't throw if file doesn't exist */);
		
		String actual = AstPrinter.print(page);
		
		compare.printTest(expectedFile, actual);
	}
	
	public void printRtWtTest(
			String inputDir,
			String outputDir,
			Page page,
			File inputFile) throws Exception
	{
		File expectedFile = TestResourcesFixture.rebase(
				inputFile,
				inputDir,
				outputDir,
				"rtwt",
				true /* don't throw if file doesn't exist */);
		
		String actual = RtWikitextPrinter.print(page);
		
		compare.printTest(expectedFile, actual);
	}
	
	public void printWomTest(
			String inputDir,
			String outputDir,
			WomNode wom,
			File inputFile) throws Exception
	{
		File expectedFile = TestResourcesFixture.rebase(
				inputFile,
				inputDir,
				outputDir,
				"wom",
				true /* don't throw if file doesn't exist */);
		
		String actual = WomPrinter.print(wom, explicitTextNodes);
		
		compare.printTest(expectedFile, actual);
	}
}
