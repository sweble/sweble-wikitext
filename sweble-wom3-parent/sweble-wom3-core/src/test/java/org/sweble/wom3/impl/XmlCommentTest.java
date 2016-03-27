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

import org.junit.Test;
import org.sweble.wom3.Wom3XmlComment;
import org.w3c.dom.Node;

public class XmlCommentTest
{
	private Wom3XmlComment n;

	public XmlCommentTest()
	{
		n = TestHelperDoc.genComment("comment");
	}

	@Test
	public void testCommentCorrectlyCreated() throws Exception
	{
		assertEquals("comment", n.getNodeValue());
	}

	@Test
	public void testCorrectNodeName() throws Exception
	{
		assertEquals("#comment", n.getNodeName());
	}

	@Test
	public void testCorrectNodeType() throws Exception
	{
		assertEquals(Node.COMMENT_NODE, n.getNodeType());
	}

	@Test
	public void testSetterWorks() throws Exception
	{
		String text = "bla bla bla";
		n.setNodeValue(text);
		assertEquals(text, n.getNodeValue());
	}

	public void testGetTextThrows() throws Exception
	{
		n.setNodeValue("bla bla bla");
		assertEquals("", n.getNodeValue());
	}
}
