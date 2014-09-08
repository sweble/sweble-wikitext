/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import static org.junit.Assert.*;

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
