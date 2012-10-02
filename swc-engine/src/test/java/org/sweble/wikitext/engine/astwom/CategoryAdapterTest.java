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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.sweble.wikitext.engine.wom.tools.AstWomBuilder.womComment;
import static org.sweble.wikitext.engine.wom.tools.AstWomBuilder.womPage;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sweble.wikitext.engine.astwom.adapters.BodyAdapter;
import org.sweble.wikitext.engine.astwom.adapters.BoldAdapter;
import org.sweble.wikitext.engine.wom.WomAttribute;
import org.sweble.wikitext.engine.wom.WomBold;
import org.sweble.wikitext.engine.wom.WomCategory;
import org.sweble.wikitext.engine.wom.WomPage;
import org.sweble.wikitext.engine.wom.WomParagraph;
import org.sweble.wikitext.parser.nodes.InternalLink;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.XmlElement;

public class CategoryAdapterTest
{
	private WomPage womPage;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void initialize() throws Exception
	{
		womPage = womPage().withBody(
				womComment().build()).build();
	}
	
	@Test
	@Ignore
	public void cannotAddCategoryToAnyElement()
	{
		/* This is not possible: WomCategory nodes are always children of a 
		 * WomPage. But WomPage doesn't allow to add children. */
	}
	
	@Test
	@Ignore
	public void cannotRemoveCategoryFromAnyElement() throws Exception
	{
		/* This is not possible: WomCategory nodes are always children of a 
		 * WomPage. But WomPage doesn't allow the removal of its children. */
	}
	
	@Test()
	public void removingANodeWhichContainsACategoryNodeMovesTheCategoryToThePage() throws Exception
	{
		womPage = AstWomTestFixture.quickPostprocessToWom("<b>[[Category:Test]]</b>");
		
		WomParagraph para = (WomParagraph) womPage.getBody().getFirstChild();
		WomBold bold = (WomBold) para.getFirstChild();
		
		assertTrue(womPage.hasCategory("Test"));
		
		XmlElement astBold = (XmlElement) ((BoldAdapter) bold).getAstNode();
		assertFalse(astBold.getBody().isEmpty());
		assertTrue(astBold.getBody().get(0) instanceof InternalLink);
		
		para.removeChild(bold);
		
		assertTrue(womPage.hasCategory("Test"));
		assertTrue(astBold.getBody().isEmpty());
		
		WtNodeList body = ((BodyAdapter) womPage.getBody()).getAstNode();
		assertTrue(body.get(body.size() - 1) instanceof InternalLink);
	}
	
	@Test
	public void settingTheNameAttributeOfACategoryToTheNameOfAnExistingCategoryRaisesException()
	{
		womPage.addCategory("Foo");
		WomCategory foo = womPage.getCategories().iterator().next();
		
		womPage.addCategory("Bar");
		
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("The page is already assigned to a category called `Bar'");
		foo.setAttribute("name", "Bar");
	}
	
	@Test
	public void pageKnowsAboutRenameAfterSettingTheNameAttributeOfACategory()
	{
		womPage.addCategory("Foo");
		WomCategory foo = womPage.getCategories().iterator().next();
		assertTrue(womPage.hasCategory("Foo"));
		
		foo.setAttribute("name", "Bar");
		assertTrue(womPage.hasCategory("Bar"));
		assertFalse(womPage.hasCategory("Foo"));
	}
	
	/*
	 * NativeOrXmlAttributeAdapter does not yet inform the attribute container
	 * (in this case the category element) of changes (be it name or value).
	 * This is especially problematic for native attributes which do not have an
	 * AST counterpart but must be set on the container AST node explicitly by
	 * the container.
	 * 
	 * Right now changes are stored in the WOM attribute but not in the
	 * container element. Thus the changes are lost in the AST if the attribute
	 * does not have an AST attribute as counterpart.
	 * 
	 * See also NativeOrXmlAttributeAdapter for explanation of problem.
	 */
	@Ignore
	@Test
	public void settingCategoryNameUsingAttributeNodeReflectsInCategory() throws Exception
	{
		womPage.addCategory("Foo");
		WomCategory cat = womPage.getCategories().iterator().next();
		assertTrue(womPage.hasCategory("Foo"));
		
		WomAttribute caNode = cat.getAttributeNode("name");
		assertEquals("Foo", caNode.getValue());
		
		caNode.setValue("Bar");
		
		assertEquals("Bar", cat.getName());
		assertFalse(womPage.hasCategory("Foo"));
		assertTrue(womPage.hasCategory("Bar"));
	}
}
