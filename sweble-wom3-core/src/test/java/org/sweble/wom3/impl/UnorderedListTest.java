/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.junit.Test;
import org.sweble.wom3.Wom3BulletStyle;
import org.sweble.wom3.Wom3ElementNode;

public class UnorderedListTest
{
	private final Wom3ElementNode n;
	
	// =========================================================================
	
	public UnorderedListTest()
	{
		this.n = TestHelperDoc.genElem("ul");
	}
	
	// =========================================================================
	
	@Test
	public void testTypeAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "type", "getItemType", "setItemType", Wom3BulletStyle.CIRCLE, "circle");
		TestHelperAttribute.testAttribute(this.n, "type", "getItemType", "setItemType", Wom3BulletStyle.DISC, "disc");
		TestHelperAttribute.testAttribute(this.n, "type", "getItemType", "setItemType", Wom3BulletStyle.SQUARE, "square");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidTypeAttribute() throws Exception
	{
		n.setAttribute("type", "foo");
	}
}
