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
import org.sweble.wikitext.engine.wom.WomText;

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
		
		URL url = AstWomTest.class.getResource("/");
		Assert.assertTrue(url != null);
		
		resources = new ParserTestResources(new File(url.getFile()));
		
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
	
	@Test
	public void testText() throws Exception
	{
		common.setExplicitTextNodes(true);
		common.gatherParseAndPrintTest(
				"text",
				new TestCode()
				{
					@Override
					public void run(final WomNode page)
					{
						// -- append --
						
						getFirstChildOfNodeById(page, "a1", WomText.class).appendText("...test...");
						
						getFirstChildOfNodeById(page, "a2", WomText.class).appendText("...test...");
						
						getFirstChildOfNodeById(page, "a3", WomText.class).appendText("...test...");
						
						getFirstChildOfNodeById(page, "a4", WomText.class).appendText("...test...");
						
						getFirstChildOfNodeById(page, "a5", WomText.class).appendText("&");
						
						getFirstChildOfNodeById(page, "b1", WomText.class).appendText("...&...");
						
						getFirstChildOfNodeById(page, "b2", WomText.class).appendText("...&...");
						
						getFirstChildOfNodeById(page, "b3", WomText.class).appendText("...&...");
						
						getFirstChildOfNodeById(page, "b4", WomText.class).appendText("...&...");
						
						// -- delete --
						
						Assert.assertEquals("Hallo Welt!", getFirstChildOfNodeById(page, "c1", WomText.class).getText());
						
						getFirstChildOfNodeById(page, "c1", WomText.class).deleteText(0, 0);
						
						getFirstChildOfNodeById(page, "c1", WomText.class).deleteText(10, 0);
						
						getFirstChildOfNodeById(page, "c1", WomText.class).deleteText(11, 0);
						
						new ExpectException(IndexOutOfBoundsException.class)
						{
							public void run()
							{
								getFirstChildOfNodeById(page, "c1", WomText.class).deleteText(11, 1);
							}
						};
						
						new ExpectException(IndexOutOfBoundsException.class)
						{
							public void run()
							{
								getFirstChildOfNodeById(page, "c1", WomText.class).deleteText(12, 0);
							}
						};
						
						new ExpectException(IndexOutOfBoundsException.class)
						{
							public void run()
							{
								getFirstChildOfNodeById(page, "c1", WomText.class).deleteText(-1, 0);
							}
						};
						
						new ExpectException(IndexOutOfBoundsException.class)
						{
							public void run()
							{
								getFirstChildOfNodeById(page, "c1", WomText.class).deleteText(10, 2);
							}
						};
						
						getFirstChildOfNodeById(page, "c1", WomText.class).deleteText(0, 5);
						
						getFirstChildOfNodeById(page, "c2", WomText.class).deleteText(6, 4);
						
						getFirstChildOfNodeById(page, "c3", WomText.class).deleteText(10, 1);
						
						getFirstChildOfNodeById(page, "c4", WomText.class).deleteText(0, 10);
						
						getFirstChildOfNodeById(page, "c5", WomText.class).deleteText(9, 2);
						
						getFirstChildOfNodeById(page, "c6", WomText.class).deleteText(0, 11);
						
						//getFirstChildOfNodeById(page, "c7", WomText.class).deleteText(0, 11);
						
						getFirstChildOfNodeById(page, "d1", WomText.class).deleteText(1, 3);
						
					}
				});
	}
	
	public static abstract class ExpectException
	{
		public <T> ExpectException(Class<T> exceptionClass)
		{
			try
			{
				run();
				Assert.fail(
						"Expected an exception of type " +
								exceptionClass.getName() +
								" but no exception was thrown!");
			}
			catch (Throwable t)
			{
				Assert.assertTrue(
						"Expected an exception of type " +
								exceptionClass.getName() +
								" but caught an exception of type " +
								t.getClass().getName(),
						exceptionClass.isInstance(t));
			}
		}
		
		public abstract void run();
	}
}
