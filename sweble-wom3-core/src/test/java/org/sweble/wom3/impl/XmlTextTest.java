/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.sweble.wom3.Wom3XmlText;
import org.w3c.dom.Node;

public class XmlTextTest
{
	private Wom3XmlText n = (Wom3XmlText) TestHelperDoc.genXmlText("content");
	
	@Test
	public void testHasCorrectName() throws Exception
	{
		assertEquals("#text", n.getNodeName());
	}
	
	@Test
	public void testHasCorrectNodeType() throws Exception
	{
		assertEquals(Node.TEXT_NODE, n.getNodeType());
	}
	
	@Test
	public void testBothGetTextAndGetValueReturnText() throws Exception
	{
		assertEquals("content", n.getNodeValue());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testCannotAppendNode() throws Exception
	{
		n.appendChild(TestHelperDoc.genElem("b"));
	}
	
	@Test
	public void testAppendTextWorks() throws Exception
	{
		n.appendData(" is not everything");
		assertEquals("content is not everything", n.getNodeValue());
	}
	
	@Test
	public void testDeleteTextWorks() throws Exception
	{
		n.deleteData(0, n.getNodeValue().length());
		assertEquals(0, n.getNodeValue().length());
		n.appendData("Test 1 2 3");
		n.deleteData(7, 2);
		assertEquals("Test 1 3", n.getNodeValue());
	}
	
	@Test
	public void testInsertWorks() throws Exception
	{
		n.deleteData(0, n.getNodeValue().length());
		assertEquals(0, n.getNodeValue().length());
		n.appendData("Test 1 3");
		n.insertData(7, "2 ");
		assertEquals("Test 1 2 3", n.getNodeValue());
	}
	
	@Test
	public void testInsertAtBothEndsWorks() throws Exception
	{
		n.insertData(0, "no ");
		n.insertData(n.getNodeValue().length(), " is good content");
		assertEquals("no content is good content", n.getNodeValue());
	}
	
	@Test
	public void testReplaceTextByPosition() throws Exception
	{
		n.deleteData(0, n.getNodeValue().length());
		n.appendData("a b c d a b c d");
		n.replaceData(4, 1, "X");
		assertEquals("a b X d a b c d", n.getNodeValue());
	}
}
