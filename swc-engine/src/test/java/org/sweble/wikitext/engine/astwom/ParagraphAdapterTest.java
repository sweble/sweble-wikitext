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
import org.junit.Ignore;
import org.junit.Test;
import org.sweble.wikitext.engine.CompilerException;
import org.sweble.wikitext.engine.astwom.adapters.PageAdapter;
import org.sweble.wikitext.engine.wom.WomPage;
import org.sweble.wikitext.engine.wom.tools.WomPrinter;
import org.sweble.wikitext.lazy.LinkTargetException;
import org.sweble.wikitext.lazy.utils.RtWikitextPrinter;

import de.fau.cs.osr.ptk.common.test.ParserTestResources;

public class ParagraphAdapterTest
		extends
			AstWomTestBase
{
	public ParagraphAdapterTest() throws FileNotFoundException, IOException
	{
		URL url = TextAdapterTest.class.getResource("/");
		Assert.assertTrue(url != null);
		
		ParserTestResources resources =
				new ParserTestResources(new File(url.getFile()));
		
		common = new AstWomTestCommon(
				resources,
				"(.*?)/target/test-classes/",
				"$1/src/test/resources/",
				false,
				"basic/wikitext/",
				"basic/result/",
				"basic/result/",
				"basic/result/");
	}
	
	@Test
	public void test() throws LinkTargetException, CompilerException
	{
		WomPage page = parseToWom("<p align=\"lefttt\">Paragraph</p>");
		
		System.out.println(RtWikitextPrinter.print(((PageAdapter) page).getAstNode()));
		
		System.out.println(WomPrinter.print(page));
	}
	
	@Ignore
	@Test
	public void test2() throws LinkTargetException, CompilerException
	{
		WomPage page = parseToWom("<p align=\"left\" align=\"right\">Paragraph</p>");
		
		System.out.println(RtWikitextPrinter.print(((PageAdapter) page).getAstNode()));
		
		System.out.println(WomPrinter.print(page));
	}
	
	/*
	@Test
	public void test()
	{
		WomPage page = new PageAdapter((String) null, (String) null, "Title");
		
		WomParagraph p = f.createParagraph(f.createText("..."));
		page.getBody().appendChild(p);
		
		p.setAttribute("style", "  margin: \n\n 5px; \n");
		
		p.setStyle("margin: 5px;");
		
		ContentNode astNode = ((PageAdapter) page).getAstNode();
		compare(page, astNode, astNode.getContent());
	}
	
	private void compare(WomPage p, ContentNode astNode, NodeList content)
	{
		Assert.assertEquals(
				"",
				WikitextPrinter.print(content));
		
		Assert.assertEquals(
				"",
				RtWikitextPrinter.print(astNode));
		
		Assert.assertEquals(
				"",
				AstPrinter.print(astNode));
		
		Assert.assertEquals(
				"",
				WomPrinter.print(p));
	}
	*/
}
