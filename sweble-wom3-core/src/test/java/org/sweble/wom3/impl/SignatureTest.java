/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.joda.time.DateTime;
import org.junit.Test;
import org.sweble.wom3.Wom3SignatureFormat;

public class SignatureTest
{
	private SignatureImpl n = (SignatureImpl) TestHelperDoc.genElem("signature");
	
	// =========================================================================
	
	@Test
	public void testFormatAttribute() throws Exception
	{
		TestHelperAttribute.testFixedAttribute(n, "format", "getSignatureFormat", "setSignatureFormat",
				Wom3SignatureFormat.USER, "user",
				Wom3SignatureFormat.TIMESTAMP, "timestamp");
	}
	
	@Test
	public void testAuthorAttribute() throws Exception
	{
		TestHelperAttribute.testFixedAttribute(n, "author", "getAuthor", "setAuthor", "Me-Man", "He-Man");
	}
	
	@Test
	public void testTimestampAttribute() throws Exception
	{
		DateTime t0 = DateTime.now();
		DateTime t1 = t0.plus(100);
		String t0Str = Toolbox.dateTimeToString(t0);
		String t1Str = Toolbox.dateTimeToString(t1);
		TestHelperAttribute.testFixedAttribute(n, "timestamp", "getTimestamp", "setTimestamp",
				t0, t0Str, t1, t1Str);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCannotSetArbitraryAttr() throws Exception
	{
		n.setAttribute("foo", "bar");
	}
}
