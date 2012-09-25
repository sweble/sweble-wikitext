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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sweble.wikitext.engine.astwom.adapters.BoldAdapter;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.tools.AstWomBuilder;
import org.sweble.wikitext.parser.parser.XmlElement;
import org.sweble.wikitext.parser.utils.XmlAttribute;

import de.fau.cs.osr.ptk.common.ast.Text;

public class AttributeAdapterTest
{
	private WomNode womBold;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void initialize()
	{
		womBold = AstWomBuilder.womBold().build();
		
		/*
		womPage = AstWomBuilder.womPage().withBody(
				womBold).build();
		*/
		
	}
	
	@Test
	public void settingAnAttribNameToANameOfAnotherAttribRaisesAnException()
	{
		womBold.setAttribute("class", "foo");
		
		womBold.setAttribute("style", "bar");
		
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Attribute with this name already exists for the corresponding element!");
		womBold.getAttributeNode("style").setName("class");
	}
	
	@Test
	public void setAttribIsRetrievableAndHasCorrectValue()
	{
		womBold.setAttribute("class", "foo");
		womBold.setAttribute("style", "bar");
		
		assertEquals("foo", womBold.getAttribute("class"));
		assertEquals("bar", womBold.getAttribute("style"));
	}
	
	@Test
	public void astAssociatedWithAttiribIsCorrect()
	{
		womBold.setAttribute("class", "foo");
		
		XmlElement e = (XmlElement) ((BoldAdapter) womBold).getAstNode();
		
		assertEquals(1, e.getXmlAttributes().size());
		
		XmlAttribute a = (XmlAttribute) e.getXmlAttributes().get(0);
		assertEquals("class", a.getName());
		assertTrue(a.getHasValue());
		assertEquals(1, a.getValue().size());
		assertEquals("foo", ((Text) a.getValue().get(0)).getContent());
	}
}
