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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
