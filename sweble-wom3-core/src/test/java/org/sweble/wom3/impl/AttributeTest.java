/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.sweble.wom3.Wom3Attribute;
import org.sweble.wom3.Wom3Bold;
import org.w3c.dom.Node;

public class AttributeTest
{
	private Wom3Attribute a = TestHelperDoc.genAttr("name", "value");
	
	// =========================================================================
	
	@Test
	public void testNodeNameEqualsAttributeName() throws Exception
	{
		assertEquals(a.getName(), a.getNodeName());
	}
	
	@Test
	public void testNodeTypeIsAttribute() throws Exception
	{
		assertEquals(Node.ATTRIBUTE_NODE, a.getNodeType());
	}
	
	@Test
	public void testNameAndValueAreCorrect() throws Exception
	{
		assertEquals("name", a.getName());
		assertEquals("value", a.getValue());
	}
	
	@Test
	public void testCanChangeAttributeNameWhenAttached() throws Exception
	{
		Wom3Bold b = (Wom3Bold) TestHelperDoc.genElem("b");
		b.setAttributeNode(this.a);
		assertTrue(a == b.getAttributeNode("name"));
		a.setName("name2");
		assertTrue(a == b.getAttributeNode("name2"));
		assertNull(b.getAttributeNode("name"));
	}
	
	@Test
	public void testSetNameWithoutChangingItWhenAttachedDoesNotThrow() throws Exception
	{
		Wom3Bold b = (Wom3Bold) TestHelperDoc.genElem("b");
		b.setAttributeNode(this.a);
		assertTrue(a == b.getAttributeNode("name"));
		a.setName("name");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCannotSetNameToNameOfAnotherAttributeWhenAttached() throws Exception
	{
		Wom3Bold b = (Wom3Bold) TestHelperDoc.genElem("b");
		b.setAttribute("another", "value");
		b.setAttributeNode(this.a);
		assertTrue(a == b.getAttributeNode("name"));
		assertNotNull(b.getAttribute("another"));
		a.setName("another");
	}
	
	@Test(expected = NullPointerException.class)
	public void testCannotSetNameToNull() throws Exception
	{
		a.setName(null);
	}
	
	/**
	 * Apparently the xerces implementaiton of the DOM won't fail on a null
	 * argument.
	 */
	//@Ignore
	@Test(expected = NullPointerException.class)
	public void testCannotSetValueToNull() throws Exception
	{
		a.setValue(null);
	}
}
