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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.sweble.wikitext.engine.astwom.AstWomTestBase.TestCode;
import org.sweble.wikitext.engine.astwom.adapters.CategoryAdapter;
import org.sweble.wikitext.engine.wom.WomCategory;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomPage;
import org.sweble.wikitext.engine.wom.WomParagraph;

import de.fau.cs.osr.ptk.common.test.ParserTestResources;

public class AstWomTest
{
	private ParserTestResources resources;
	
	private AstWomTestBase common;
	
	// =========================================================================
	
	public AstWomTest() throws FileNotFoundException, IOException
	{
		System.out.println();
		System.out.println("AST->WOM & Print test:");
		
		URL url = AstWomTest.class.getClassLoader().getResource("org/sweble/wikitext/engine/index");
		Assert.assertTrue(url != null);
		
		File index = new File(url.getFile());
		File directory = index.getParentFile();
		
		resources = new ParserTestResources(directory);
		
		common = new AstWomTestBase(
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
	public void testSimple() throws Exception
	{
		common.gatherParseAndPrintTest(
				"simple",
				new TestCode()
				{
					@Override
					public void run(WomNode root)
					{
						WomPage page = (WomPage) root;
						WomParagraph p = (WomParagraph) page.getBody().getFirstChild();
						p.setAttribute("class", "funny");
					}
				});
	}
	
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
							if (c.getCategory().equalsIgnoreCase("bold"))
							{
								c.setCategory("bOlD");
								p.replaceChild(c, new CategoryAdapter("muhaha"));
								break;
							}
						}
						
						p.setCategory("ich bin neu hier");
					}
				});
	}
	
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
							if (c.getCategory().equalsIgnoreCase("bold"))
								c.setCategory("main");
						}
					}
				});
	}
}
