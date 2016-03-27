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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.sweble.wom3.Wom3Heading;

public class SectionTest
{
	private SectionImpl n = (SectionImpl) TestHelperDoc.genElem("section");

	// =========================================================================

	@Test
	public void testHasCorrectLevel() throws Exception
	{
		assertEquals(1, n.getLevel());
	}

	@Test
	public void testLevelAttribute() throws Exception
	{
		TestHelperAttribute.testFixedAttributeNoObjectSetter(n, "level", "getLevel", "setLevel", 6, "6", 3, "3");
	}

	@Test(expected = NullPointerException.class)
	public void testCannotRemoveHeading() throws Exception
	{
		n.setHeading(null);
	}

	@Test(expected = NullPointerException.class)
	public void testCannotRemoveBody() throws Exception
	{
		n.setBody(null);
	}

	@Test
	public void testResetHeading() throws Exception
	{
		n.setHeading((HeadingImpl) TestHelperDoc.genElem("heading"));
		assertNotNull(n.getHeading());

		HeadingImpl heading = (HeadingImpl) TestHelperDoc.genElem("heading");
		n.setHeading(heading);

		assertTrue(heading == n.getHeading());
	}

	@Test
	public void testResetBody() throws Exception
	{
		n.setBody((BodyImpl) TestHelperDoc.genElem("body"));
		assertNotNull(n.getBody());

		BodyImpl body = (BodyImpl) TestHelperDoc.genElem("body");
		n.setBody(body);

		assertTrue(body == n.getBody());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCannotSetArbitraryAttr() throws Exception
	{
		n.setAttribute("foo", "bar");
	}

	@Test
	public void testReplaceHeadingAndBody() throws Exception
	{
		n = (SectionImpl) TestHelperDoc.genElem("section");
		n.setLevel(2);
		n.setHeading((Wom3Heading) TestHelperDoc.genElem("heading"));
		n.setBody((BodyImpl) TestHelperDoc.genElem("body"));
		assertNotNull(n.getHeading());
		assertNotNull(n.getBody());

		HeadingImpl heading = (HeadingImpl) TestHelperDoc.genElem("heading");
		n.replaceChild(heading, n.getHeading());
		BodyImpl body = (BodyImpl) TestHelperDoc.genElem("body");
		n.replaceChild(body, n.getBody());

		assertTrue(heading == n.getHeading());
		assertTrue(body == n.getBody());
		assertTrue(heading == n.getFirstChild());
		assertTrue(body == n.getLastChild());
	}
}
