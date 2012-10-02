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
package org.sweble.wikitext.engine.astwom;

import static org.sweble.wikitext.engine.astwom.AstWomTestFixture.womTestFixtureGather;
import static org.sweble.wikitext.engine.astwom.AstWomTestFixture.womTestFixtureGet;
import static org.sweble.wikitext.engine.astwom.AstWomTestFixture.womTestFixtureInitialize;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wikitext.engine.Page;
import org.sweble.wikitext.engine.wom.WomPage;

@Ignore
@RunWith(value = NamedParametrized.class)
public class AstWomIntegrationTests
{
	private static final ClassLoader CLASSLOADER = AstWomIntegrationTests.class.getClassLoader();
	
	private static final String RESOURCE_BASE = "org/sweble/wikitext/engine";
	
	private static final String INPUT_DIR = "basic/wikitext";
	
	private static final String INPUT_PATTERN = ".*\\.wikitext";
	
	private static final String OUTPUT_DIR = "basic/result";
	
	// =========================================================================
	
	static
	{
		womTestFixtureInitialize(
				CLASSLOADER,
				RESOURCE_BASE);
	}
	
	@Parameters
	public static List<Object[]> enumerateInputs() throws FileNotFoundException
	{
		LinkedList<Object[]> inputs = new LinkedList<Object[]>();
		
		for (File i : womTestFixtureGather(INPUT_DIR, INPUT_PATTERN, true))
		{
			inputs.add(new Object[] { i.getName(), i });
		}
		
		return inputs;
	}
	
	// =========================================================================
	
	private final File wikitextFile;
	
	private AstWomTestFixture fixture;
	
	// =========================================================================
	
	public AstWomIntegrationTests(String title, File input)
	{
		this.wikitextFile = input;
	}
	
	@Before
	public void initialize() throws Exception
	{
		this.fixture = womTestFixtureGet();
		this.fixture.getConfig().getCompilerConfig().setTrimTransparentBeforeParsing(false);
	}
	
	@Test
	public void checkPrintedAst() throws Exception
	{
		Page page = fixture.postprocessToAst(wikitextFile).getPage();
		
		fixture.printAstTest(INPUT_DIR, OUTPUT_DIR, page, wikitextFile);
	}
	
	@Test
	public void checkPrintedRtWt() throws Exception
	{
		Page page = fixture.postprocessToAst(wikitextFile).getPage();
		
		fixture.printRtWtTest(INPUT_DIR, OUTPUT_DIR, page, wikitextFile);
	}
	
	@Test
	public void checkPrintedWom() throws Exception
	{
		WomPage page = fixture.postprocessToWom(wikitextFile);
		
		fixture.setExplicitTextNodes(true);
		fixture.printWomTest(INPUT_DIR, OUTPUT_DIR, page, wikitextFile);
	}
	
	/*
	@Test
	public void testSimple() throws Exception
	{
		common.setExplicitTextNodes(true);
		common.gatherParseAndPrintTest(
				"simple",
				new TestCode()
				{
					@Override
					public void run(WomNode root)
					{
						// WomPage page = (WomPage) root;
					}
				});
	}
	*/
	
	// =========================================================================
	
	/*
	
	@Test
	public void testChild1()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		BodyAdapter newBody = new BodyAdapter();
		WomBody oldBody = p.getBody();
		assertTrue(oldBody == p.setBody(newBody));
		assertTrue(newBody == p.getBody());
		assertTrue(newBody == p.getFirstChild());
		assertTrue(newBody == p.getLastChild());
	}
	
	@Test(expected = NullPointerException.class)
	public void testChild2()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.setBody(null);
	}
	
	@Test
	public void testChild3()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		RedirectAdapter newRedirect = new RedirectAdapter("asdf");
		assertNull(p.setRedirect(newRedirect));
		assertTrue(p.isRedirect());
		assertTrue(newRedirect == p.getRedirect());
		assertTrue(newRedirect == p.getFirstChild());
		assertTrue(p.getLastChild() == p.getBody());
		assertTrue(newRedirect == p.setRedirect(null));
		assertNull(p.getRedirect());
		assertFalse(p.isRedirect());
		assertTrue(p.getFirstChild() == p.getBody());
		assertTrue(p.getLastChild() == p.getBody());
	}
	
	@Test
	public void testChild4()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.setCategory("Test");
		p.setCategory("Test2");
		
		System.out.println(WomPrinter.print(p));
		
		WikitextNode n = ((PageAdapter) p).getAstNode();
		System.out.print(AstPrinter.print(n));
	}
	
	// =========================================================================
	
	@Test(expected = UnsupportedOperationException.class)
	public void testLowLevelChild1()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.appendChild(new BodyAdapter());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testLowLevelChild2()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.insertBefore(p.getLastChild(), new RedirectAdapter("asdf"));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testLowLevelChild3()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.removeChild(p.getLastChild());
	}
	
	@Test
	public void testLowLevelChild4()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		BodyAdapter newBody = new BodyAdapter();
		p.replaceChild(p.getBody(), newBody);
		assertTrue(newBody == p.getBody());
		assertTrue(newBody == p.getFirstChild());
		assertTrue(newBody == p.getLastChild());
	}
	*/
	
	// =========================================================================
	
	/*
	// =========================================================================
	
	private ParserTestResources resources;
	
	private AstWomTestCommon common;
	
	// =========================================================================
	
	public AstWomIntegrationTests() throws FileNotFoundException, IOException
	{
		System.out.println();
		System.out.println("AST->WOM & Print test:");
		
		URL url = AstWomIntegrationTests.class.getClassLoader().getResource("org/sweble/wikitext/engine/index");
		Assert.assertTrue(url != null);
		
		File index = new File(url.getFile());
		File directory = index.getParentFile();
		
		resources = new ParserTestResources(directory);
		
		common = new AstWomTestCommon(
				resources,
				"(.*?)/target/test-classes/",
				"$1/src/test/resources/",
				false,
				"basic/wikitext/",
				"basic/result/",
				"basic/result/",
				"basic/result/");
		
		common.getConfig().setTrimTransparentBeforeParsing(false);
	}
	
	// =========================================================================
	
	@Test
	public void testCategories() throws Exception
	{
		common.gatherParseAndPrintTest(
				"categories",
				new TestCode()
				{
					@Override
					public void run(WomNode root)
					{
						WomPage p = (WomPage) root;
						
						p.setCategory("main");
						p.removeCategory("ya habibi yalla");
						
						for (WomCategory c : p.getCategories())
						{
							if (c.getName().equalsIgnoreCase("bold"))
							{
								c.setName("bOlD");
								p.replaceChild(c, new CategoryAdapter("muhaha"));
								break;
							}
						}
						
						p.setCategory("ich bin neu hier");
					}
				});
	}
	
	// =========================================================================
	
	@Test(expected = IllegalArgumentException.class)
	public void testCategories2() throws Exception
	{
		common.gatherParseAndPrintTest(
				"categories",
				new TestCode()
				{
					@Override
					public void run(WomNode root)
					{
						WomPage p = (WomPage) root;
						
						p.setCategory("main");
						for (WomCategory c : p.getCategories())
						{
							if (c.getName().equalsIgnoreCase("bold"))
								c.setName("main");
						}
					}
				});
	}

	// =========================================================================
	*/
}
