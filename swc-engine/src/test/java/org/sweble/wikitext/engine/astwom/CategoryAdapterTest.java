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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sweble.wikitext.engine.astwom.adapters.CategoryAdapter;
import org.sweble.wikitext.engine.wom.WomBold;
import org.sweble.wikitext.engine.wom.WomCategory;
import org.sweble.wikitext.engine.wom.WomPage;
import org.sweble.wikitext.engine.wom.WomParagraph;

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
	public void cannotAddCategoryToAnyElement()
	{
		WomBold bold = womBold().build();
		womPage.getBody().appendChild(bold);
		
		CategoryAdapter c = new CategoryAdapter("Some Category");
		
		expectedEx.expect(UnsupportedOperationException.class);
		expectedEx.expectMessage("Cannot add category node to bold node!");
		bold.appendChild(c);
	}
	
	@Test()
	public void removingANodeWhichContainsACategoryNodeRemovesTheCategory() throws Exception
	{
		womPage = AstWomTestFixture.quickParseToWom("<b>[[Category:Test]]</b>");
		
		assertTrue(womPage.hasCategory("Test"));
		
		WomParagraph para = (WomParagraph) womPage.getBody().getFirstChild();
		WomBold bold = (WomBold) para.getFirstChild();
		para.removeChild(bold);
		
		assertFalse(womPage.hasCategory("Test"));
	}
	
	@Test
	public void settingTheNameAttributeOfACategoryToTheNameOfAnExistingCategoryRaisesException()
	{
		womPage.setCategory("Foo");
		WomCategory foo = womPage.getCategories().iterator().next();
		
		womPage.setCategory("Bar");
		
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("The page is already assigned to a category called `Bar'");
		foo.setAttribute("name", "Bar");
	}
	
	@Test
	public void pageKnowsAboutRenameAfterSettingTheNameAttributeOfACategory()
	{
		womPage.setCategory("Foo");
		WomCategory foo = womPage.getCategories().iterator().next();
		assertTrue(womPage.hasCategory("Foo"));
		
		foo.setAttribute("name", "Bar");
		assertTrue(womPage.hasCategory("Bar"));
		assertFalse(womPage.hasCategory("Foo"));
	}
}
