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

import org.junit.Assert;
import org.junit.Test;
import org.sweble.wikitext.engine.astwom.adapters.PageAdapter;
import org.sweble.wikitext.engine.wom.WomAttribute;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomPage;
import org.sweble.wikitext.engine.wom.tools.WomPrinter;
import org.sweble.wikitext.lazy.utils.AstPrinter;
import org.sweble.wikitext.lazy.utils.RtWikitextPrinter;
import org.sweble.wikitext.lazy.utils.WikitextPrinter;

import de.fau.cs.osr.ptk.common.ast.ContentNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public class AttributeAdapterTest
		extends
			AstWomTestBase
{
	@Test(expected = IllegalArgumentException.class)
	public void test()
	{
		WomAttribute a = null;
		try
		{
			WomPage p = new PageAdapter((String) null, (String) null, "Title");
			
			WomNode bold = f.createBold(f.createText("Hallo"));
			p.getBody().appendChild(bold);
			bold.setAttribute("class", "foo");
			bold.setAttribute("style", "bar");
			
			p.setCategory("Foo");
			p.setCategory("Bar");
			
			a = bold.getAttributeNode("style");
		}
		catch (Exception e)
		{
			Assert.fail();
		}
		a.setName("class");
	}
	
	@Test
	public void testCompare()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		
		WomNode bold = f.createBold(f.createText("Hallo"));
		p.getBody().appendChild(bold);
		bold.setAttribute("class", "foo");
		bold.setAttribute("style", "bar");
		
		p.setCategory("Foo");
		p.setCategory("Bar");
		
		ContentNode astNode = ((PageAdapter) p).getAstNode();
		compare(p, astNode, astNode.getContent());
	}
	
	private void compare(WomPage p, ContentNode astNode, NodeList content)
	{
		Assert.assertEquals(
				"<b class=\"foo\" style=\"bar\">Hallo</b>[[Category:Foo]][[Category:Bar]]",
				WikitextPrinter.print(content));
		
		Assert.assertEquals(
				"<b class=\"foo\" style=\"bar\">Hallo</b>[[Category:Foo]][[Category:Bar]]",
				RtWikitextPrinter.print(astNode));
		
		Assert.assertEquals(
				"Page([\n" +
						"  XmlElement(\n" +
						"    Properties:\n" +
						"          RTD = RtData: [0] = \"<b\", [1] = \">\", [2] = \"</b>\"\n" +
						"      {N} empty = false\n" +
						"      {N} name = \"b\"\n" +
						"\n" +
						"    [\n" +
						"      XmlAttribute(\n" +
						"        Properties:\n" +
						"              RTD = RtData: [0] = \" class=\\\"\", [1] = \"\\\"\"\n" +
						"          {N} hasValue = true\n" +
						"          {N} name = \"class\"\n" +
						"\n" +
						"        [ Text(\"foo\") ]\n" +
						"      )\n" +
						"      XmlAttribute(\n" +
						"        Properties:\n" +
						"              RTD = RtData: [0] = \" style=\\\"\", [1] = \"\\\"\"\n" +
						"          {N} hasValue = true\n" +
						"          {N} name = \"style\"\n" +
						"\n" +
						"        [ Text(\"bar\") ]\n" +
						"      )\n" +
						"    ]\n" +
						"    [ Text(\"Hallo\") ]\n" +
						"  )\n" +
						"  InternalLink(\n" +
						"    Properties:\n" +
						"          RTD = RtData: [0] = \"[[Category:Foo\", [1] = \"]]\"\n" +
						"      {N} postfix = \"\"\n" +
						"      {N} prefix = \"\"\n" +
						"      {N} target = \"Category:Foo\"\n" +
						"\n" +
						"    LinkTitle([ ])\n" +
						"  )\n" +
						"  InternalLink(\n" +
						"    Properties:\n" +
						"          RTD = RtData: [0] = \"[[Category:Bar\", [1] = \"]]\"\n" +
						"      {N} postfix = \"\"\n" +
						"      {N} prefix = \"\"\n" +
						"      {N} target = \"Category:Bar\"\n" +
						"\n" +
						"    LinkTitle([ ])\n" +
						"  )\n" +
						"])\n",
				AstPrinter.print(astNode));
		
		Assert.assertEquals(
				"\n<page version=\"1.0\" title=\"Title\">\n" +
						"  <category name=\"Foo\" />\n" +
						"  <category name=\"Bar\" />\n" +
						"  <body><b class=\"foo\" style=\"bar\">Hallo</b></body>\n" +
						"</page>",
				WomPrinter.print(p));
	}
}
