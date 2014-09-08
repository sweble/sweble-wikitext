/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.junit.Test;
import org.sweble.wom3.Wom3ElementNode;

public class ListItemTest
{
	private final Wom3ElementNode n;
	
	// =========================================================================
	
	public ListItemTest()
	{
		this.n = TestHelperDoc.genElem("li");
	}
	
	// =========================================================================
	
	@Test
	public void testTypeAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "type", "getItemType", "setItemType", "i");
	}
	
	@Test
	public void testValueAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "value", "getItemValue", "setItemValue", 5, "5");
	}
}
