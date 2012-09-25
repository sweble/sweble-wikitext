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

import static org.junit.Assert.*;
import static org.sweble.wikitext.engine.wom.tools.AstWomBuilder.*;

import org.junit.Before;
import org.junit.Test;
import org.sweble.wikitext.engine.astwom.adapters.PageAdapter;
import org.sweble.wikitext.engine.wom.WomHorizontalRule;
import org.sweble.wikitext.engine.wom.WomPage;
import org.sweble.wikitext.parser.parser.HorizontalRule;
import org.sweble.wikitext.parser.parser.XmlElement;
import org.sweble.wikitext.parser.utils.XmlAttribute;
import org.sweble.wikitext.parser.parser.RtData;
import org.sweble.wikitext.parser.utils.RtWikitextPrinter;

import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;

public class HorizontalRuleAdapterTest
{
	private WomPage womPage;
	
	private NodeList astPageContent;
	
	private WomHorizontalRule hRule;
	
	@Before
	public void initialize()
	{
		hRule = womHr().build();
		womPage = womPage().withBody(hRule).build();
		
		astPageContent = ((PageAdapter) womPage).getAstNode().getContent();
	}
	
	@Test
	public void theAstOfAHorizontalRuleIsCorrect()
	{
		HorizontalRule hr = (HorizontalRule) astPageContent.get(0);
		
		assertEquals(
				RtData.build("----"),
				hr.getAttribute("RTD"));
	}
	
	@Test
	public void theAstOfAHorizontalRuleAfterConversionToHtmlIsCorrect()
	{
		hRule.setAttribute("style", "foo");
		
		XmlElement e = (XmlElement) astPageContent.get(0);
		
		assertEquals(1, e.getXmlAttributes().size());
		
		XmlAttribute a = (XmlAttribute) e.getXmlAttributes().get(0);
		assertEquals("style", a.getName());
		assertTrue(a.getHasValue());
		assertEquals(1, a.getValue().size());
		assertEquals("foo", ((Text) a.getValue().get(0)).getContent());
		
		assertEquals(
				RtData.build("<hr", " />", null),
				e.getAttribute("RTD"));
	}
	
	@Test
	public void theAstOfAHorizontalRuleAfterAddingInvalidContentIsCorrect()
	{
		hRule.appendChild(womText().withText("Invalid").build());
		
		XmlElement e = (XmlElement) astPageContent.get(0);
		
		assertEquals(
				RtData.build("<hr", ">", "</hr>"),
				e.getAttribute("RTD"));
	}
	
	@Test
	public void removingAllChildrenTurnsHrIntoEmptyElementAgain()
	{
		hRule.appendChild(womText().withText("Invalid").build());
		
		hRule.removeChild(hRule.getFirstChild());
		
		XmlElement e = (XmlElement) astPageContent.get(0);
		
		assertTrue(e.getEmpty());
		assertEquals(
				RtData.build("<hr", " />", null),
				e.getAttribute("RTD"));
	}
	
	@Test
	public void rtDataSupportsCorrectRendering()
	{
		assertEquals(
				"----",
				RtWikitextPrinter.print(astPageContent));
	}
	
	@Test
	public void rtDataSupportsCorrectRenderingAfterConversionToHtml()
	{
		hRule.setAttribute("style", "foo");
		
		assertEquals(
				"<hr style=\"foo\" />",
				RtWikitextPrinter.print(astPageContent));
	}
	
	@Test
	public void rtDataSupportsCorrectRenderingAfterAddingInvalidContent()
	{
		hRule.appendChild(womText().withText("Invalid").build());
		
		assertEquals(
				"<hr>Invalid</hr>",
				RtWikitextPrinter.print(astPageContent));
	}
}
