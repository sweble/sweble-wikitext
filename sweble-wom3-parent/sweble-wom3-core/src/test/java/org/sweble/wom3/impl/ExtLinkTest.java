/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.sweble.wom3.Wom3ExtLink;
import org.sweble.wom3.Wom3Title;

public class ExtLinkTest
{
	private Wom3ExtLink n;

	public ExtLinkTest() throws NullPointerException, MalformedURLException
	{
		//n = new Wom2ExtLink(new URL("http://example.com"));
		n = (Wom3ExtLink) TestHelperDoc.genElem("extlink");
		n.setTarget(new URL("http://example.com"));
	}

	@Test
	public void testTargetAttribute() throws Exception
	{
		String urlA = "http://example.com";
		String urlB = "http://sweble.org";
		TestHelperAttribute.testFixedAttribute(n, "target", "getTarget", "setTarget",
				new URL(urlA), urlA,
				new URL(urlB), urlB);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnknownAttributeThrows() throws Exception
	{
		n.setAttribute("test", "v");
	}

	@Test
	public void testSetTargetAttributeIsReflectedByGetLinkTarget() throws Exception
	{
		n.setAttribute("target", "http://example.com");
		assertEquals(n.getTarget(), n.getLinkTarget());
	}

	@Test
	public void testSetTitle() throws Exception
	{
		assertNull(n.getFirstChild());
		assertNull(n.getLastChild());

		Wom3Title b = (Wom3Title) TestHelperDoc.genElem("title");
		n.setLinkTitle(b);
		assertTrue(b == n.getLinkTitle());

		assertTrue(b == n.getFirstChild());
		assertTrue(b == n.getLastChild());
	}

	@Test
	public void testResetLinkTitle() throws Exception
	{
		Wom3Title b0 = (Wom3Title) TestHelperDoc.genElem("title");
		n.setLinkTitle(b0);
		assertTrue(b0 == n.getLinkTitle());

		Wom3Title b1 = (Wom3Title) TestHelperDoc.genElem("title");
		n.setLinkTitle(b1);
		assertTrue(b1 == n.getLinkTitle());

		assertTrue(b1 == n.getFirstChild());
		assertTrue(b1 == n.getLastChild());
	}

	@Test
	public void testRemoveTitle() throws Exception
	{
		Wom3Title b0 = (Wom3Title) TestHelperDoc.genElem("title");
		n.setLinkTitle(b0);
		assertTrue(b0 == n.getLinkTitle());

		assertTrue(b0 == n.setLinkTitle(null));
		assertNull(n.getLinkTitle());
	}

	@Test
	public void testReplaceTitle() throws Exception
	{
		Wom3Title b0 = (Wom3Title) TestHelperDoc.genElem("title");
		n.setLinkTitle(b0);
		assertTrue(b0 == n.getLinkTitle());

		Wom3Title b1 = (Wom3Title) TestHelperDoc.genElem("title");
		n.replaceChild(b1, b0);

		assertTrue(b1 == n.getLinkTitle());
	}
}
