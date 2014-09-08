/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.sweble.wom3.Wom3HorizAlign;

public class ParagraphTest
{
	private ParagraphImpl n = (ParagraphImpl) TestHelperDoc.genElem("p");
	
	// =========================================================================
	
	@Test
	public void testInitialGapsAreZero() throws Exception
	{
		assertEquals(0, n.getTopGap());
		assertEquals(0, n.getBottomGap());
	}
	
	@Test
	public void testAlignAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3HorizAlign.CENTER, "center");
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3HorizAlign.JUSTIFY, "justify");
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3HorizAlign.LEFT, "left");
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3HorizAlign.RIGHT, "right");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAlignAttributeDoesNotAllowChar() throws Exception
	{
		n.setAttribute("align", "char");
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testCannotRemoveTopGap() throws Exception
	{
		n.removeAttribute("topgap");
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testCannotRemoveBottomGap() throws Exception
	{
		n.removeAttribute("bottomgap");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCannotSetNegativeTopGap() throws Exception
	{
		n.setTopGap(-1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCannotSetNegativeBottomGap() throws Exception
	{
		n.setBottomGap(-1);
	}
	
	@Test
	public void testGapSettersWork() throws Exception
	{
		TestHelperAttribute.testFixedAttributeNoObjectSetter(
				n, "topgap", "getTopGap", "setTopGap", 1, "1", 0, "0");
		
		TestHelperAttribute.testFixedAttributeNoObjectSetter(
				n, "bottomgap", "getBottomGap", "setBottomGap", 1, "1", 0, "0");
	}
}
