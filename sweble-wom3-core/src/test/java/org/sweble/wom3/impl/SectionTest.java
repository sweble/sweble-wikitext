/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import static org.junit.Assert.*;

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
