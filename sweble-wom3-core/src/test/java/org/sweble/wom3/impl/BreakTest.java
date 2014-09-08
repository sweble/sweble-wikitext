/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.junit.Test;
import org.sweble.wom3.Wom3Clear;
import org.sweble.wom3.Wom3ElementNode;

public class BreakTest
{
	private final Wom3ElementNode n = TestHelperDoc.genElem("br");
	
	// =========================================================================
	
	@Test
	public void testClearAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "clear", "getClear", "setClear", Wom3Clear.ALL, "all");
		TestHelperAttribute.testAttribute(this.n, "clear", "getClear", "setClear", Wom3Clear.LEFT, "left");
		TestHelperAttribute.testAttribute(this.n, "clear", "getClear", "setClear", Wom3Clear.NONE, "none");
		TestHelperAttribute.testAttribute(this.n, "clear", "getClear", "setClear", Wom3Clear.RIGHT, "right");
	}
	
	// ==[ Core Attributes ]====================================================
	
	@Test
	public void testClassAttribute() throws Exception
	{
		TestHelperAttribute.testClassAttribute(this.n);
	}
	
	@Test
	public void testIdAttribute() throws Exception
	{
		TestHelperAttribute.testIdAttribute(this.n);
	}
	
	@Test
	public void testStyleAttribute() throws Exception
	{
		TestHelperAttribute.testStyleAttribute(this.n);
	}
	
	@Test
	public void testTitleAttribute() throws Exception
	{
		TestHelperAttribute.testTitleAttribute(this.n);
	}
}
