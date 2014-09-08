/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.junit.Test;
import org.sweble.wom3.Wom3ElementNode;

public class OrderedListTest
{
	private final Wom3ElementNode n;
	
	// =========================================================================
	
	public OrderedListTest()
	{
		this.n = TestHelperDoc.genElem("ol");
	}
	
	// =========================================================================
	
	@Test
	public void testStartAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "start", "getStart", "setStart", 5, "5");
	}
	
	@Test
	public void testTypeAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "type", "getItemType", "setItemType", "i");
	}
}
