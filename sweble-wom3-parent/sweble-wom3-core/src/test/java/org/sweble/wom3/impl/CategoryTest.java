/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Ignore;
import org.junit.Test;
import org.sweble.wom3.Wom3Article;
import org.sweble.wom3.Wom3Category;

public class CategoryTest
{
	private Wom3Article p;

	private Wom3Category n;

	// =========================================================================

	public CategoryTest()
	{
		this.p = (Wom3Article) TestHelperDoc.genElem("article");
		this.p.setNamespace("User");
		this.p.setTitle("John");
		this.p.setPath("Page");

		this.n = (Wom3Category) TestHelperDoc.genElem("category");
		this.n.setName("name");
	}

	// =========================================================================

	@Test
	public void testNameAttribute() throws Exception
	{
		TestHelperAttribute.testFixedAttribute(this.n, "name", "getName", "setName", "X", "Y");
	}

	@Test
	public void testNameAttributeWhileAttached() throws Exception
	{
		n = (CategoryImpl) p.addCategory("test");
		TestHelperAttribute.testFixedAttribute(this.n, "name", "getName", "setName", "X", "Y");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetUnknownCategoryThrows() throws Exception
	{
		n.setAttribute("foo", "bar");
	}

	@Test(expected = UnsupportedOperationException.class)
	@Ignore
	public void testTryToAddCategoryToWrongParent() throws Exception
	{
		TestHelperDoc.genElem("b").appendChild(n);
	}

	@Test
	@Ignore
	public void testCategoryHasEmptyLinkTitle() throws Exception
	{
		assertFalse(n.getLinkTitle().hasChildNodes());
	}

	@Test
	@Ignore
	public void testLinkTargetIsCorrect() throws Exception
	{
		// TODO: It's not always "Category:"!
		assertEquals("Category:name", n.getLinkTarget());
	}
}
