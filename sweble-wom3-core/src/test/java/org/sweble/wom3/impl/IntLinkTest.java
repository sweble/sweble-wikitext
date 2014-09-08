/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntLinkTest
{
	private IntLinkImpl n;
	
	public IntLinkTest()
	{
		n = (IntLinkImpl) TestHelperDoc.genElem("intlink");
		n.setTarget("some page");
	}
	
	@Test
	public void testTargetAttribute() throws Exception
	{
		String titleA = "a nice page";
		String titleB = "some other page";
		TestHelperAttribute.testFixedAttribute(n, "target", "getTarget", "setTarget", titleA, titleB);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUnknownAttributeThrows() throws Exception
	{
		n.setAttribute("test", "v");
	}
	
	@Test
	public void testSetTargetAttributeIsReflectedByGetLinkTarget() throws Exception
	{
		n.setAttribute("target", "foo");
		assertEquals(n.getTarget(), n.getLinkTarget());
	}
	
	@Test
	public void testSetTitle() throws Exception
	{
		assertNull(n.getFirstChild());
		assertNull(n.getLastChild());
		
		TitleImpl b = (TitleImpl) TestHelperDoc.genElem("title");
		n.setLinkTitle(b);
		assertTrue(b == n.getLinkTitle());
		
		assertTrue(b == n.getFirstChild());
		assertTrue(b == n.getLastChild());
	}
	
	@Test
	public void testResetLinkTitle() throws Exception
	{
		TitleImpl b0 = (TitleImpl) TestHelperDoc.genElem("title");
		n.setLinkTitle(b0);
		assertTrue(b0 == n.getLinkTitle());
		
		TitleImpl b1 = (TitleImpl) TestHelperDoc.genElem("title");
		n.setLinkTitle(b1);
		assertTrue(b1 == n.getLinkTitle());
		
		assertTrue(b1 == n.getFirstChild());
		assertTrue(b1 == n.getLastChild());
	}
	
	@Test
	public void testRemoveTitle() throws Exception
	{
		TitleImpl b0 = (TitleImpl) TestHelperDoc.genElem("title");
		n.setLinkTitle(b0);
		assertTrue(b0 == n.getLinkTitle());
		
		assertTrue(b0 == n.setLinkTitle(null));
		assertNull(n.getLinkTitle());
	}
	
	@Test
	public void testReplaceTitle() throws Exception
	{
		TitleImpl b0 = (TitleImpl) TestHelperDoc.genElem("title");
		n.setLinkTitle(b0);
		assertTrue(b0 == n.getLinkTitle());
		
		TitleImpl b1 = (TitleImpl) TestHelperDoc.genElem("title");
		n.replaceChild(b1, b0);
		
		assertTrue(b1 == n.getLinkTitle());
	}
}
