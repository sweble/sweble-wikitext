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
import org.sweble.wikitext.engine.wom.WomHorizontalRule;
import org.sweble.wikitext.engine.wom.WomPage;
import org.sweble.wikitext.engine.wom.tools.WomPrinter;
import org.sweble.wikitext.lazy.utils.AstPrinter;
import org.sweble.wikitext.lazy.utils.RtWikitextPrinter;
import org.sweble.wikitext.lazy.utils.WikitextPrinter;

import de.fau.cs.osr.ptk.common.ast.ContentNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public class HorizontalRuleAdapterTest
		extends
			AstWomTestBase
{
	@Test
	public void test()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		
		WomHorizontalRule hr = f.createHorizontalRule();
		p.getBody().appendChild(hr);
		
		hr = f.createHorizontalRule();
		hr.setAttribute("style", "foo");
		hr.appendChild(f.createText("Invalid"));
		p.getBody().appendChild(hr);
		
		ContentNode astNode = ((PageAdapter) p).getAstNode();
		compare(p, astNode, astNode.getContent());
	}
	
	private void compare(WomPage p, ContentNode astNode, NodeList content)
	{
		Assert.assertEquals(
				"\n\n----<hr style=\"foo\" />",
				WikitextPrinter.print(content));
		
		Assert.assertEquals(
				"----<hr style=\"foo\">Invalid</hr>",
				RtWikitextPrinter.print(astNode));
		
		Assert.assertEquals(
				"Page([\n" +
						"  HorizontalRule(\n" +
						"    Properties:\n" +
						"          RTD = RtData: [0] = \"----\"\n" +
						"  )\n" +
						"  XmlElement(\n" +
						"    Properties:\n" +
						"          RTD = RtData: [0] = \"<hr\", [1] = \">\", [2] = \"</hr>\"\n" +
						"      {N} empty = true\n" +
						"      {N} name = \"hr\"\n" +
						"\n" +
						"    [\n" +
						"      XmlAttribute(\n" +
						"        Properties:\n" +
						"              RTD = RtData: [0] = \" style=\\\"\", [1] = \"\\\"\"\n" +
						"          {N} hasValue = true\n" +
						"          {N} name = \"style\"\n" +
						"\n" +
						"        [ Text(\"foo\") ]\n" +
						"      )\n" +
						"    ]\n" +
						"    [ Text(\"Invalid\") ]\n" +
						"  )\n" +
						"])\n",
				AstPrinter.print(astNode));
		
		Assert.assertEquals(
				"\n<page version=\"1.0\" title=\"Title\">\n" +
						"  <body>\n" +
						"    <hr />\n" +
						"    <hr style=\"foo\">Invalid</hr>\n" +
						"  </body>\n" +
						"</page>",
				WomPrinter.print(p));
	}
}
