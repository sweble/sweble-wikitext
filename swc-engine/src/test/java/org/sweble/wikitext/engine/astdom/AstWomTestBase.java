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

package org.sweble.wikitext.engine.astdom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.sweble.wikitext.engine.CompiledPage;
import org.sweble.wikitext.engine.Compiler;
import org.sweble.wikitext.engine.CompilerException;
import org.sweble.wikitext.engine.FullPage;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.utils.SimpleWikiConfiguration;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.tools.WomPrinter;
import org.sweble.wikitext.lazy.LinkTargetException;
import org.sweble.wikitext.lazy.utils.AstPrinter;
import org.sweble.wikitext.lazy.utils.RtWikitextPrinter;

import xtc.parser.ParseException;
import de.fau.cs.osr.ptk.common.GenericPrinterInterface;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.test.FileContent;
import de.fau.cs.osr.ptk.common.test.ParserTestCommon;
import de.fau.cs.osr.ptk.common.test.ParserTestResources;

public class AstWomTestBase
        extends
            ParserTestCommon
{
	private final SimpleWikiConfiguration config;
	
	private final Compiler compiler;
	
	private final String wikitextDir;
	
	private final String asttextDir;
	
	private final String womtextDir;
	
	private final String rtwttextDir;
	
	private boolean explicitTextNodes = false;
	
	// =========================================================================
	
	public AstWomTestBase(
	        ParserTestResources resources,
	        String noRefReplace,
	        String noRefReplaceBy,
	        boolean ranwomRefName,
	        String wikitextDir,
	        String asttextDir,
	        String womtextDir,
	        String rtwttextDir) throws FileNotFoundException, IOException
	{
		super(resources, noRefReplace, noRefReplaceBy, ranwomRefName);
		
		this.config = new SimpleWikiConfiguration(
		        "classpath:/org/sweble/wikitext/engine/SimpleWikiConfiguration.xml");
		
		this.compiler = new Compiler(config);
		
		this.wikitextDir = wikitextDir;
		this.asttextDir = asttextDir;
		this.womtextDir = womtextDir;
		this.rtwttextDir = rtwttextDir;
	}
	
	// =========================================================================
	
	public Compiler getCompiler()
	{
		return compiler;
	}
	
	public SimpleWikiConfiguration getConfig()
	{
		return config;
	}
	
	public void setExplicitTextNodes(boolean explicitTextNodes)
	{
		this.explicitTextNodes = explicitTextNodes;
	}
	
	// =========================================================================
	
	public void gatherParseAndPrintTest(String title, TestCode testCode) throws IOException, ParseException, LinkTargetException, CompilerException
	{
		final List<File> input = resources.gather(
		        wikitextDir,
		        Pattern.quote(title) + "\\.wikitext",
		        true);
		
		Assert.assertEquals(1, input.size());
		
		File wikitextFile = input.get(0);
		
		System.out.println("Testing: " + wikitextDir + wikitextFile.getName());
		parseAndPrintTest(testCode, wikitextFile);
	}
	
	// =========================================================================
	
	private void parseAndPrintTest(TestCode testCode, File wikitextFile) throws LinkTargetException, IOException, CompilerException
	{
		String pageName = wikitextFile.getName();
		
		PageTitle title = PageTitle.make(config, pageName);
		
		// AST
		
		CompiledPage page = postprocess(title, wikitextFile, false);
		
		printTestAst(page, wikitextFile);
		
		// WOM
		
		DefaultWomNodeFactory wnf =
		        new DefaultWomNodeFactory(config, title.getTitle());
		
		WomNode wom = wnf.create(null, page.getPage());
		
		testCode.run(wom);
		
		printTestWom(wom, wikitextFile);
		
		printTestRtWt(wom, wikitextFile);
		
		explicitTextNodes = false;
	}
	
	private void printTestAst(CompiledPage page, File wikitextFile) throws IOException
	{
		GenericPrinterInterface printer = new GenericPrinterInterface()
		{
			@Override
			public void print(Object ast, Writer out) throws IOException
			{
				AstPrinter.print(out, (AstNode) ast);
			}
			
			@Override
			public String getPrintoutType()
			{
				return "ast";
			}
		};
		
		File reftextFile = ParserTestResources.rebase(
		        wikitextFile,
		        wikitextDir,
		        asttextDir,
		        printer.getPrintoutType(),
		        true /* don't throw if file doesn't exist */);
		
		printTest(page.getPage(), printer, reftextFile);
	}
	
	private void printTestWom(WomNode wom, File wikitextFile) throws IOException
	{
		GenericPrinterInterface printer = new GenericPrinterInterface()
		{
			@Override
			public void print(Object wom, Writer out) throws IOException
			{
				WomPrinter.print(out, (WomNode) wom, explicitTextNodes);
			}
			
			@Override
			public String getPrintoutType()
			{
				return "wom";
			}
		};
		
		File reftextFile = ParserTestResources.rebase(
		        wikitextFile,
		        wikitextDir,
		        womtextDir,
		        printer.getPrintoutType(),
		        true /* don't throw if file doesn't exist */);
		
		printTest(wom, printer, reftextFile);
	}
	
	private void printTestRtWt(WomNode wom, File wikitextFile) throws IOException
	{
		GenericPrinterInterface printer = new GenericPrinterInterface()
		{
			@Override
			public void print(Object wom, Writer out) throws IOException
			{
				RtWikitextPrinter.print(out, ((DomBackbone) wom).getAstNode());
			}
			
			@Override
			public String getPrintoutType()
			{
				return "rtwt";
			}
		};
		
		File reftextFile = ParserTestResources.rebase(
		        wikitextFile,
		        wikitextDir,
		        rtwttextDir,
		        printer.getPrintoutType(),
		        true /* don't throw if file doesn't exist */);
		
		printTest(wom, printer, reftextFile);
	}
	
	// =========================================================================
	
	private CompiledPage postprocess(PageTitle title, File wikitextFile, boolean forInclusion) throws LinkTargetException, IOException, CompilerException
	{
		FileContent wikitext = new FileContent(wikitextFile);
		
		PageId id = new PageId(title, -1);
		
		FullPage fullPage = new FullPage(id, wikitext.getContent());
		
		return compiler.postprocess(
		        fullPage.getId(),
		        fullPage.getText(),
		        null);
	}
	
	// =========================================================================
	
	public static abstract class TestCode
	{
		public abstract void run(WomNode page);
		
		public static WomNode getNodeById(WomNode root, String id)
		{
			String nodeId = root.getAttribute("id");
			if (nodeId != null && nodeId.equals(id))
				return root;
			
			for (WomNode n : root)
			{
				WomNode found = getNodeById(n, id);
				if (found != null)
					return found;
			}
			
			return null;
		}
		
		public static <T> T getFirstChildOfNodeById(WomNode root, String id, Class<T> clazz)
		{
			return clazz.cast(getNodeById(root, id).getFirstChild());
		}
	}
}
