/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class RedirectTest
{
	private RedirectImpl n = (RedirectImpl) TestHelperDoc.genElem("redirect");
	
	// =========================================================================
	
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
	@Ignore
	public void testCategoryHasEmptyLinkTitle() throws Exception
	{
		assertFalse(n.getLinkTitle().hasChildNodes());
	}
}
