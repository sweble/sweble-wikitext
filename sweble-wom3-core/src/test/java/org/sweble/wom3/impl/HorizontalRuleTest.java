/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.junit.Test;
import org.sweble.wom3.Wom3ElementNode;
import org.sweble.wom3.Wom3HorizAlign;
import org.sweble.wom3.Wom3Unit;

public class HorizontalRuleTest
{
	private final Wom3ElementNode n = TestHelperDoc.genElem("hr");
	
	// =========================================================================
	
	@Test
	public void testAlignAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3HorizAlign.CENTER, "center");
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3HorizAlign.LEFT, "left");
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3HorizAlign.RIGHT, "right");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAlignAttributeDoesNotAllowChar() throws Exception
	{
		n.setAttribute("align", "char");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAlignAttributeDoesNotAllowJustify() throws Exception
	{
		n.setAttribute("align", "justify");
	}
	
	@Test
	public void testNoshadeAttribute() throws Exception
	{
		TestHelperAttribute.testBooleanAttribute(n, "noshade", "isNoshade", "setNoshade");
	}
	
	@Test
	public void testSizeAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "size", "getSize", "setSize", 100, "100");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNegativeSizeThrows() throws Exception
	{
		n.setAttribute("size", "-10");
	}
	
	@Test
	public void testWidthAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "width", "getWidth", "setWidth",
				new ValueWithUnitImpl(Wom3Unit.PERCENT, 100.2f), "100.2%");
		TestHelperAttribute.testAttribute(this.n, "width", "getWidth", "setWidth",
				new ValueWithUnitImpl(Wom3Unit.PERCENT, -100.2f), "-100.2%");
		TestHelperAttribute.testAttribute(this.n, "width", "getWidth", "setWidth",
				new ValueWithUnitImpl(Wom3Unit.PIXELS, 100), "100");
		TestHelperAttribute.testAttribute(this.n, "width", "getWidth", "setWidth",
				new ValueWithUnitImpl(Wom3Unit.PIXELS, 100), "100");
		TestHelperAttribute.testAttribute(this.n, "width", "getWidth", "setWidth",
				new ValueWithUnitImpl(Wom3Unit.PIXELS, -100), "-100");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidWidthThrows() throws Exception
	{
		n.setAttribute("width", "-10.0");
	}
}
