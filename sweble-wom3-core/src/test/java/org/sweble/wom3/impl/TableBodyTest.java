/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.junit.Test;
import org.sweble.wom3.Wom3HorizAlign;
import org.sweble.wom3.Wom3TableVAlign;

public class TableBodyTest
{
	private TableBodyImpl n = (TableBodyImpl) TestHelperDoc.genElem("tbody");
	
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
	
	@Test
	public void testTableVAlignAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "valign", "getVAlign", "setTableVAlign", Wom3TableVAlign.BASELINE, "baseline");
		TestHelperAttribute.testAttribute(this.n, "valign", "getVAlign", "setTableVAlign", Wom3TableVAlign.BOTTOM, "bottom");
		TestHelperAttribute.testAttribute(this.n, "valign", "getVAlign", "setTableVAlign", Wom3TableVAlign.MIDDLE, "middle");
		TestHelperAttribute.testAttribute(this.n, "valign", "getVAlign", "setTableVAlign", Wom3TableVAlign.TOP, "top");
	}
}
