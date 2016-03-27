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
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.sweble.wom3.Wom3ElementNode;
import org.sweble.wom3.Wom3Node;
import org.w3c.dom.DOMException;

public class BackboneAttributeTest
{
	private final ArrayList<String> events = new ArrayList<String>();

	private final DocumentImpl doc;

	private final MyAttribSupportingElem e;

	private Mode mode = Mode.STRING_ATTR;

	// =========================================================================

	public BackboneAttributeTest()
	{
		final DomImplementationImpl domImpl = new DomImplementationImpl();

		this.doc = new DocumentImpl(domImpl)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public AttributeImpl createAttribute(String name) throws DOMException
			{
				return new AttribImpl(this, name);
			}
		};

		this.e = new MyAttribSupportingElem(doc);
	}

	// =========================================================================

	enum Mode
	{
		STRING_ATTR,
		INT_ATTR,
		FUNNY_ATTR,
		BOOL_ATTR,
		NOT_REMOVABLE,
	}

	// =========================================================================

	@Test
	public void testCorrectPathSetAttributeWithValue() throws Exception
	{
		mode = Mode.INT_ATTR;
		e.setAttribute("name", "5");
		assertEquals(Arrays.asList(
				"getAttributeDescriptor",
				"verifyAndConvert",
				"createAttribute",
				"set",
				"customAction"), events);
	}

	@Test
	public void testCorrectPathSetAttributeDirectWithValue() throws Exception
	{
		mode = Mode.INT_ATTR;
		e.setAttributeDirect(new AttribDesc(), "name", 5);
		assertEquals(Arrays.asList(
				"verifyAndConvert",
				"createAttribute",
				"set",
				"customAction"), events);
		assertEquals(5, e.getAttributeNativeData("name"));
	}

	@Test
	public void testCorrectPathSetAttributeDirectToNull() throws Exception
	{
		mode = Mode.INT_ATTR;
		e.setAttributeDirect(new AttribDesc(), "name", 5);
		events.clear();

		e.setAttributeDirect(new AttribDesc(), "name", null);
		assertEquals(Arrays.asList(
				"isReadOnly",
				"isRemovable",
				"customAction"), events);
		assertNull(e.getAttributeNode("name"));
	}

	@Test
	public void testCorrectPathSetAttributeToNull() throws Exception
	{
		mode = Mode.INT_ATTR;
		e.setAttributeDirect(new AttribDesc(), "name", 5);
		events.clear();

		e.setAttribute("name", null);
		assertEquals(Arrays.asList(
				"getAttributeDescriptor",
				"isReadOnly",
				"isRemovable",
				"customAction"), events);
		assertNull(e.getAttributeNode("name"));
	}

	@Test
	public void testSetBooleanAttributeToFalse() throws Exception
	{
		mode = Mode.BOOL_ATTR;
		e.setAttributeDirect(new AttribDesc(), "name", true);
		events.clear();

		e.setAttributeDirect(new AttribDesc(), "name", false);
		assertEquals(Arrays.asList(
				"verifyAndConvert",
				"isReadOnly",
				"isRemovable",
				"customAction"), events);
		assertNull(e.getAttributeNode("name"));
	}

	@Test
	public void testSetAttributeNodeVerifiesNewlyAttachedAttribute() throws Exception
	{
		AttributeImpl a = new AttribImpl("name", "value");
		e.setAttributeNode(a);
		assertEquals(Arrays.asList(
				"setValue",
				"set",
				"getAttributeDescriptor",
				"verifyAndConvert",
				"set",
				"customAction"), events);

		events.clear();
		a = new AttribImpl("name", "value2");
		e.setAttributeNode(a);
		assertEquals(Arrays.asList(
				"setValue",
				"set",
				"getAttributeDescriptor",
				"verifyAndConvert",
				"isReadOnly",
				"set",
				"customAction"), events);
	}

	@Test
	public void testSetValueOnAttachedAttributeTriggersVerification() throws Exception
	{
		AttributeImpl a = new AttribImpl("name", "value");
		e.setAttributeNode(a);
		events.clear();

		a.setValue("something else");
		assertEquals(Arrays.asList(
				"setValue",
				"getAttributeDescriptor",
				"verifyAndConvert",
				"isReadOnly",
				"set",
				"customAction"), events);
	}

	@Test
	public void testSetNameOnAttachedAttributeTriggersCustomAction() throws Exception
	{
		AttribImpl a = new AttribImpl("name", "value");
		e.setAttributeNode(a);
		events.clear();

		a.setName("other");
		assertEquals(Arrays.asList(
				"getAttributeDescriptor",
				"getAttributeDescriptor",
				"customAction"), events);
	}

	@Test
	public void testAttachAttributeNodeWhenVerifierRequestsDeletionWorks() throws Exception
	{
		mode = Mode.FUNNY_ATTR;

		e.setAttributeNode(new AttribImpl("name", "kill me"));
		assertEquals(Arrays.asList(
				"setValue",
				"set",
				"getAttributeDescriptor",
				"verifyAndConvert",
				"isReadOnly",
				"isRemovable"), events);

		e.setAttribute("name", "don't kill me");
		events.clear();

		e.setAttributeNode(new AttribImpl("name", "kill me"));
		assertEquals(Arrays.asList(
				"setValue",
				"set",
				"getAttributeDescriptor",
				"verifyAndConvert",
				"isReadOnly",
				"isRemovable",
				"customAction"), events);
	}

	@Test
	public void testSetValueOnAttributeWhenVerifierRequestsDeletionThrows() throws Exception
	{
		mode = Mode.FUNNY_ATTR;

		e.setAttribute("name", "don't kill me");
		events.clear();

		try
		{
			e.getAttributeNode("name").setValue("kill me");
			fail();
		}
		catch (UnsupportedOperationException e)
		{
			assertEquals(Arrays.asList(
					"setValue",
					"getAttributeDescriptor",
					"verifyAndConvert"), events);
		}
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemovingUnremovableAttributeThrows() throws Exception
	{
		mode = Mode.NOT_REMOVABLE;

		e.setAttribute("name", "value");
		events.clear();

		e.removeAttribute("name");
	}

	@Test
	public void testRemoveAttributeByNameWorks() throws Exception
	{
		e.setAttribute("name", "value");
		events.clear();

		e.removeAttribute("name");
		assertEquals(Arrays.asList(
				"getAttributeDescriptor",
				"isReadOnly",
				"isRemovable",
				"customAction"), events);
	}

	@Test
	public void testRemovingAbsentAttributeDoesNotThrow() throws Exception
	{
		e.removeAttribute("name");
		assertEquals(Arrays.asList(
				"getAttributeDescriptor",
				"isReadOnly",
				"isRemovable"), events);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemovingAttributeNodeThatIsNotAChildThrowsEvenIfAnAttributeWithTheSameNameIsAttached() throws Exception
	{
		e.setAttribute("name", "value");
		events.clear();

		e.removeAttributeNode(doc.createAttribute("name"));
	}

	@Test
	public void testRemoveAttributeNodeByRefWorks() throws Exception
	{
		e.setAttribute("name", "value");
		e.removeAttributeNode(e.getAttributeNode("name"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetAttributeWithNullNameThrows() throws Exception
	{
		e.getAttribute(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetAttributeNodeWithNullThrows() throws Exception
	{
		e.getAttributeNode(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveAttributeNodeWithNullThrows() throws Exception
	{
		e.removeAttributeNode(null);
	}

	@Test
	public void testSetAttributeNodeWithAlreadyAttachedNodeDoesNothing() throws Exception
	{
		e.setAttribute("name", "value");
		AttributeBase a = e.getAttributeNode("name");
		events.clear();

		e.setAttributeNode(a);
		assertEquals(Arrays.asList(), events);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetAttributeNodeWithNullThrows() throws Exception
	{
		e.setAttributeNode(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testAttributeCannotBePartOfTwoNodes() throws Exception
	{
		e.setAttribute("attr", "value");
		new MyAttribSupportingElem(doc).setAttributeNode(e.getAttributeNode("attr"));
	}

	@Test
	public void testAddReplaceAndRemoveMultipleAttributes() throws Exception
	{
		e.setAttribute("a0", "value");
		e.setAttribute("a1", "value");
		e.setAttribute("a2", "What will become of me");
		e.setAttribute("a3", "value");
		e.removeAttribute("a1");
		e.removeAttribute("a0");
		e.setAttribute("a2", "I will survive");
		e.removeAttribute("a3");
		assertEquals(1, e.getWomAttributes().size());
		assertEquals("I will survive", e.getAttribute("a2"));
	}

	// =========================================================================

	class AttribDesc
			extends
				AttributeDescriptor
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			events.add("verifyAndConvert");
			switch (mode)
			{
				case INT_ATTR:
					if (verified.strValue != null)
						verified.value = Integer.parseInt(verified.strValue);
					else
						verified.strValue = String.valueOf((Integer) verified.value);
					return true;
				case BOOL_ATTR:
					if (verified.strValue != null)
						verified.value = true;
					else
						verified.strValue = "dummy";
					return (Boolean) verified.value;
				case FUNNY_ATTR:
					return !("kill me".equals(verified.strValue));
				default:
					return true;
			}
		}

		@Override
		public boolean isRemovable()
		{
			events.add("isRemovable");
			switch (mode)
			{
				case NOT_REMOVABLE:
					return false;
				default:
					return true;
			}
		}

		@Override
		public boolean isReadOnly()
		{
			events.add("isReadOnly");
			switch (mode)
			{
				default:
					return false;
			}
		}

		@Override
		public Normalization getNormalizationMode()
		{
			events.add("getNormalizationMode");
			return Normalization.NON_CDATA;
		}

		@Override
		public void customAction(
				Wom3Node parent,
				AttributeBase oldAttr,
				AttributeBase newAttr)
		{
			events.add("customAction");
		}

		@Override
		public int getFlags()
		{
			return makeFlags(true, false, true, Normalization.NON_CDATA);
		}
	}

	// =========================================================================

	class AttribImpl
			extends
				AttributeImpl
	{
		private static final long serialVersionUID = 1L;

		public AttribImpl(DocumentImpl doc, String name)
		{
			super(doc, name);
		}

		public AttribImpl(String name, String value)
		{
			super(doc, name);
			setValue(value);
		}

		@Override
		public void setValue(String value)
		{
			events.add("setValue");
			super.setValue(value);
		}

		@Override
		public void setValue(Object value, String strValue, boolean cloning)
		{
			events.add("set");
			super.setValue(value, strValue, cloning);
		}
	}

	// =========================================================================

	class MyAttribSupportingElem
			extends
				BackboneWomElement
			implements
				Wom3ElementNode
	{
		private static final long serialVersionUID = 1L;

		AttribDesc attribDesc = new AttribDesc();

		public MyAttribSupportingElem(DocumentImpl owner)
		{
			super(owner);
		}

		@Override
		public String getWomName()
		{
			return "MyAttribSupportingElem";
		}

		@Override
		protected AttributeDescriptor getAttributeDescriptor(
				String namespaceUri,
				String localName,
				String qualifiedName)
		{
			events.add("getAttributeDescriptor");
			return attribDesc;
		}

		@Override
		protected AttributeBase createAttribute(
				String name,
				NativeAndStringValuePair verified)
		{
			events.add("createAttribute");
			return super.createAttribute(name, verified);
		}
	}
}
