/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.junit.Test;
import org.sweble.wom3.Wom3TableCaptionAlign;

public class TableCaptionTest
{
	private TableCaptionImpl n = (TableCaptionImpl) TestHelperDoc.genElem("caption");
	
	@Test
	public void testTableVAlignAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3TableCaptionAlign.BOTTOM, "bottom");
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3TableCaptionAlign.LEFT, "left");
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3TableCaptionAlign.RIGHT, "right");
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3TableCaptionAlign.TOP, "top");
	}
}
