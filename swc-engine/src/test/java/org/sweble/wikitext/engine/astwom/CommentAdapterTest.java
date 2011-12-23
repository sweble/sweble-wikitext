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
import org.sweble.wikitext.engine.wom.WomPage;
import org.sweble.wikitext.engine.wom.tools.WomPrinter;
import org.sweble.wikitext.lazy.utils.AstPrinter;
import org.sweble.wikitext.lazy.utils.RtWikitextPrinter;
import org.sweble.wikitext.lazy.utils.WikitextPrinter;

import de.fau.cs.osr.ptk.common.ast.ContentNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public class CommentAdapterTest
		extends
			AstWomTestBase
{
	@Test
	public void test()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.getBody().appendChild(f.createComment("Hallo"));
		
		ContentNode astNode = ((PageAdapter) p).getAstNode();
		compare(p, astNode, astNode.getContent());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testException()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.getBody().appendChild(f.createComment("Muhaha -- Muhaha"));
	}
	
	private void compare(WomPage p, ContentNode astNode, NodeList content)
	{
		Assert.assertEquals(
				"",
				WikitextPrinter.print(content));
		
		Assert.assertEquals(
				"<!--Hallo-->",
				RtWikitextPrinter.print(astNode));
		
		Assert.assertEquals(
				"Page([\n" +
						"  XmlComment(\n" +
						"    Properties:\n" +
						"          RTD = RtData: [0] = \"<!--Hallo-->\"\n" +
						"      {N} content = \"Hallo\"\n" +
						"      {N} prefix = null\n" +
						"      {N} suffix = null\n" +
						"  )\n" +
						"])\n",
				AstPrinter.print(astNode));
		
		Assert.assertEquals(
				"\n<page version=\"1.0\" title=\"Title\">\n" +
						"  <body><comment>Hallo</comment>\n" +
						"  </body>\n" +
						"</page>",
				WomPrinter.print(p));
	}
}
