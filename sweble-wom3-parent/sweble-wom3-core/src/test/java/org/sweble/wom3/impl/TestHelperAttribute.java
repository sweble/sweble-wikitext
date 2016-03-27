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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.sweble.wom3.Wom3ElementNode;
import org.sweble.wom3.Wom3I18nDir;

public abstract class TestHelperAttribute
{
	// ==[ Core Attributes ]====================================================

	public static void testClassAttribute(Wom3ElementNode node) throws Exception
	{
		testAttribute(node, "class", "getClasses", "setClasses", "my-class");
	}

	public static void testIdAttribute(Wom3ElementNode node) throws Exception
	{
		testAttribute(node, "id", "getId", "setId", "my-id");
	}

	public static void testStyleAttribute(Wom3ElementNode node) throws Exception
	{
		testAttribute(node, "style", "getStyle", "setStyle", "width: 10px;");
	}

	public static void testTitleAttribute(Wom3ElementNode node) throws Exception
	{
		testAttribute(node, "title", "getTitle", "setTitle", "some title");
	}

	// ==[ I18n Attributes ]====================================================

	public static void testDirAttribute(Wom3ElementNode node) throws Exception
	{
		testAttribute(node, "dir", "getDir", "setDir", Wom3I18nDir.LTR, "ltr");
		testAttribute(node, "dir", "getDir", "setDir", Wom3I18nDir.RTL, "rtl");
	}

	public static void testLangAttribute(Wom3ElementNode node) throws Exception
	{
		testAttribute(node, "lang", "getLang", "setLang", "de");
	}

	public static void testXmlLangAttribute(Wom3ElementNode node) throws Exception
	{
		testAttribute(node, "xml:lang", "getXmlLang", "setXmlLang", "de");
	}

	// ==[ Event Attributes ]===================================================

	public static void testOnClickAttribute(Wom3ElementNode node) throws Exception
	{
		testAttribute(node, "onclick", "getOnclick", "setOnclick", "alert();");
	}

	public static void testOnDblClickAttribute(Wom3ElementNode node) throws Exception
	{
		testAttribute(node, "ondblclick", "getOndblclick", "setOndblclick", "alert();");
	}

	public static void testOnMouseDownAttribute(Wom3ElementNode node) throws Exception
	{
		testAttribute(node, "onmousedown", "getOnmousedown", "setOnmousedown", "alert();");
	}

	public static void testOnMouseUpAttribute(Wom3ElementNode node) throws Exception
	{
		testAttribute(node, "onmouseup", "getOnmouseup", "setOnmouseup", "alert();");
	}

	public static void testOnMouseOverAttribute(Wom3ElementNode node) throws Exception
	{
		testAttribute(node, "onmouseover", "getOnmouseover", "setOnmouseover", "alert();");
	}

	public static void testOnMouseMoveAttribute(Wom3ElementNode node) throws Exception
	{
		testAttribute(node, "onmousemove", "getOnmousemove", "setOnmousemove", "alert();");
	}

	public static void testOnMouseOutAttribute(Wom3ElementNode node) throws Exception
	{
		testAttribute(node, "onmouseout", "getOnmouseout", "setOnmouseout", "alert();");
	}

	public static void testOnKeyPressAttribute(Wom3ElementNode node) throws Exception
	{
		testAttribute(node, "onkeypress", "getOnkeypress", "setOnkeypress", "alert();");
	}

	public static void testOnKeyUpAttribute(Wom3ElementNode node) throws Exception
	{
		testAttribute(node, "onkeyup", "getOnkeyup", "setOnkeyup", "alert();");
	}

	public static void testOnKeyDownAttribute(Wom3ElementNode node) throws Exception
	{
		testAttribute(node, "onkeydown", "getOnkeydown", "setOnkeydown", "alert();");
	}

	// =========================================================================

	public static <T extends Wom3ElementNode> void testAttribute(
			T node,
			String name,
			String getter,
			String setter,
			Object value,
			String strValue) throws Exception
	{
		Wom3ElementNode u = (Wom3ElementNode) node;

		Method[] gs = getGetterAndSetter(u, getter, setter);
		Method g = gs[0];
		Method s = gs[1];

		if (strValue == null)
			throw new NullPointerException();

		assertEquals("", node.getAttribute(name));
		assertNull(g.invoke(u));

		node.setAttribute(name, strValue);
		assertEquals(strValue, node.getAttribute(name));
		assertEquals(value, g.invoke(u));

		assertEquals(value, s.invoke(u, (Object) null));
		assertEquals("", node.getAttribute(name));
		assertNull(g.invoke(u));

		assertNull(s.invoke(u, (Object) value));
		assertEquals(strValue, node.getAttribute(name));
		assertEquals(value, g.invoke(u));

		node.setAttribute(name, null);
		assertEquals("", node.getAttribute(name));
		assertNull(g.invoke(u));
	}

	public static <T extends Wom3ElementNode> void testAttribute(
			T node,
			String name,
			String getter,
			String setter,
			String strValue) throws Exception
	{
		testAttribute(node, name, getter, setter, strValue, strValue);
	}

	public static <T extends Wom3ElementNode> void testBooleanAttribute(
			T node,
			String name,
			String getter,
			String setter) throws Exception
	{
		Wom3ElementNode u = (Wom3ElementNode) node;

		Method[] gs = getGetterAndSetter(u, getter, setter);
		Method g = gs[0];
		Method s = gs[1];

		assertEquals("", node.getAttribute(name));
		assertFalse((Boolean) g.invoke(u));

		node.setAttribute(name, "foo");
		assertEquals(name, node.getAttribute(name));
		assertTrue((Boolean) g.invoke(u));

		assertTrue((Boolean) s.invoke(u, false));
		assertEquals("", node.getAttribute(name));
		assertFalse((Boolean) g.invoke(u));

		assertFalse((Boolean) s.invoke(u, true));
		assertEquals(name, node.getAttribute(name));
		assertTrue((Boolean) g.invoke(u));

		node.setAttribute(name, null);
		assertEquals("", node.getAttribute(name));
		assertFalse((Boolean) g.invoke(u));
	}

	public static <T extends Wom3ElementNode> void testFixedAttribute(
			T node,
			String name,
			String getter,
			String setter,
			Object valueA,
			String strValueA,
			Object valueB,
			String strValueB) throws Exception
	{
		Wom3ElementNode u = (Wom3ElementNode) node;

		Method[] gs = getGetterAndSetter(u, getter, setter);
		Method g = gs[0];
		Method s = gs[1];

		if (strValueA == null)
			throw new NullPointerException();
		if (strValueB == null)
			throw new NullPointerException();

		assertNotNull(node.getAttribute(name));
		assertNotNull(g.invoke(u));

		node.setAttribute(name, strValueA);
		assertEquals(strValueA, node.getAttribute(name));
		assertEquals(valueA, g.invoke(u));

		assertEquals(valueA, s.invoke(u, (Object) valueB));
		assertEquals(strValueB, node.getAttribute(name));
		assertEquals(valueB, g.invoke(u));

		tryToRemoveFixedAttr(node, name);
		assertEquals(strValueB, node.getAttribute(name));
		assertEquals(valueB, g.invoke(u));

		tryToRemoveFixedAttrWithSetter(node, s);
		assertEquals(strValueB, node.getAttribute(name));
		assertEquals(valueB, g.invoke(u));
	}

	public static <T extends Wom3ElementNode> void testFixedAttributeNoObjectSetter(
			T node,
			String name,
			String getter,
			String setter,
			Object valueA,
			String strValueA,
			Object valueB,
			String strValueB) throws Exception
	{
		Wom3ElementNode u = (Wom3ElementNode) node;

		Method[] gs = getGetterAndSetter(u, getter, setter);
		Method g = gs[0];
		Method s = gs[1];

		if (strValueA == null)
			throw new NullPointerException();
		if (strValueB == null)
			throw new NullPointerException();

		assertNotNull(node.getAttribute(name));
		assertNotNull(g.invoke(u));

		node.setAttribute(name, strValueA);
		assertEquals(strValueA, node.getAttribute(name));
		assertEquals(valueA, g.invoke(u));

		assertEquals(valueA, s.invoke(u, (Object) valueB));
		assertEquals(strValueB, node.getAttribute(name));
		assertEquals(valueB, g.invoke(u));

		tryToRemoveFixedAttr(node, name);
		assertEquals(strValueB, node.getAttribute(name));
		assertEquals(valueB, g.invoke(u));
	}

	private static <T extends Wom3ElementNode> void tryToRemoveFixedAttr(
			T node,
			String name)
	{
		try
		{
			node.setAttribute(name, null);
			fail();
		}
		catch (UnsupportedOperationException e)
		{
		}
	}

	private static <T extends Wom3ElementNode> void tryToRemoveFixedAttrWithSetter(
			T node,
			Method s) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		try
		{
			s.invoke(node, (Object) null);
			fail();
		}
		catch (InvocationTargetException e)
		{
			assertTrue(e.getCause() instanceof UnsupportedOperationException);
		}
	}

	public static <T extends Wom3ElementNode> void testFixedAttribute(
			T node,
			String name,
			String getter,
			String setter,
			String strValueA,
			String strValueB) throws Exception
	{
		testFixedAttribute(node, name, getter, setter, strValueA, strValueA, strValueB, strValueB);
	}

	private static Method[] getGetterAndSetter(
			Wom3ElementNode u,
			String getter,
			Object setter)
	{
		Method g = null;
		Method s = null;

		Class<? extends Wom3ElementNode> clazz = u.getClass();
		for (Class<?> i : clazz.getInterfaces())
		{
			if (!i.getSimpleName().startsWith("Wom"))
				continue;

			try
			{
				g = i.getMethod(getter);
			}
			catch (NoSuchMethodException e)
			{
				continue;
			}

			Method[] ss = i.getMethods();
			for (Method m : ss)
			{
				if (m.getName().equals(setter))
				{
					s = m;
					break;
				}
			}

			break;
		}

		assertNotNull("Missing getter: " + getter, g);
		return new Method[] { g, s };
	}
}
