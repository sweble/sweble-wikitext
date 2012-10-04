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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.sweble.wikitext.engine.wom.tools.AstWomBuilder.womComment;
import static org.sweble.wikitext.engine.wom.tools.AstWomBuilder.womPage;

import org.junit.Before;
import org.junit.Test;
import org.sweble.wikitext.engine.astwom.adapters.PageAdapter;
import org.sweble.wikitext.engine.wom.WomPage;
import org.sweble.wikitext.parser.nodes.WtContentNode;
import org.sweble.wikitext.parser.nodes.WtXmlComment;
import org.sweble.wikitext.parser.utils.RtWikitextPrinter;

import de.fau.cs.osr.ptk.common.ast.RtData;

public class CommentAdapterTest
{
	private WomPage womPage;
	
	private WtContentNode astPage;
	
	@Before
	public void initialize()
	{
		womPage = womPage().withBody(
				womComment().build()).build();
		
		astPage = ((PageAdapter) womPage).getAstNode();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void illegalCommentTextRaisesException()
	{
		womPage().withBody(
				womComment().withText(" -- ").build()).build();
	}
	
	@Test
	public void rtDataSupportsCorrectRendering()
	{
		assertEquals(
				"<!-- Default Comment WtText -->",
				RtWikitextPrinter.print(astPage));
	}
	
	@Test
	public void theAstOfAWomCommentIsCorrect()
	{
		WtXmlComment c = (WtXmlComment) astPage.getContent().get(0);
		
		assertNull(c.getPrefix());
		assertNull(c.getSuffix());
		assertEquals(" Default Comment WtText ", c.getContent());
		assertEquals(
				new RtData(1, "<!-- Default Comment WtText -->"),
				c.getRtd());
	}
}
