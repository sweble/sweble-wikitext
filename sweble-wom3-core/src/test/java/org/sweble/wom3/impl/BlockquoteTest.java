/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import java.net.URL;

import org.junit.Test;
import org.sweble.wom3.Wom3ElementNode;

public class BlockquoteTest
{
	private final Wom3ElementNode n = TestHelperDoc.genElem("blockquote");
	
	// =========================================================================
	
	@Test
	public void testCiteAttribute() throws Exception
	{
		String strValue = "http://example.com";
		URL realValue = new URL(strValue);
		TestHelperAttribute.testAttribute(this.n, "cite", "getCite", "setCite", realValue, strValue);
	}
}
