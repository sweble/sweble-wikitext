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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wom3.Wom3ElementNode;
import org.sweble.wom3.Wom3Node;

import de.fau.cs.osr.utils.NamedParametrized;

@RunWith(value = NamedParametrized.class)
public class NodesWithUniversalAttributesTest
{
	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		List<Object[]> inputs = new ArrayList<Object[]>();

		inputs.add(new Object[] { "hr" });
		inputs.add(new Object[] { "table" });
		inputs.add(new Object[] { "pre" });
		inputs.add(new Object[] { "ol" });
		inputs.add(new Object[] { "ul" });
		inputs.add(new Object[] { "abbr" });
		inputs.add(new Object[] { "big" });
		inputs.add(new Object[] { "b" });
		inputs.add(new Object[] { "center" });
		inputs.add(new Object[] { "cite" });
		inputs.add(new Object[] { "code" });
		inputs.add(new Object[] { "dd" });
		inputs.add(new Object[] { "dt" });
		inputs.add(new Object[] { "dfn" });
		inputs.add(new Object[] { "em" });
		inputs.add(new Object[] { "i" });
		inputs.add(new Object[] { "kbd" });
		inputs.add(new Object[] { "samp" });
		inputs.add(new Object[] { "small" });
		inputs.add(new Object[] { "span" });
		inputs.add(new Object[] { "strike" });
		inputs.add(new Object[] { "strong" });
		inputs.add(new Object[] { "sub" });
		inputs.add(new Object[] { "sup" });
		inputs.add(new Object[] { "tt" });
		inputs.add(new Object[] { "u" });
		inputs.add(new Object[] { "var" });
		inputs.add(new Object[] { "blockquote" });
		inputs.add(new Object[] { "del" });
		inputs.add(new Object[] { "div" });
		inputs.add(new Object[] { "dl" });
		inputs.add(new Object[] { "heading" });
		inputs.add(new Object[] { "ins" });
		inputs.add(new Object[] { "li" });
		inputs.add(new Object[] { "p" });
		inputs.add(new Object[] { "tbody" });
		inputs.add(new Object[] { "tcaption" });
		inputs.add(new Object[] { "td" });
		inputs.add(new Object[] { "th" });
		inputs.add(new Object[] { "tr" });

		return inputs;
	}

	// =========================================================================

	private final Wom3ElementNode n;

	private final DocumentImpl doc;

	public NodesWithUniversalAttributesTest(String name) throws InstantiationException, IllegalAccessException
	{
		DomImplementationImpl domImpl = DomImplementationImpl.get();
		doc = domImpl.createDocument(Wom3Node.WOM_NS_URI, "article", null);
		this.n = (Wom3ElementNode) doc.createElementNS(Wom3Node.WOM_NS_URI, name);
	}

	// =========================================================================

	@Test
	public void testCanSetArbitraryAttributes() throws Exception
	{
		n.setAttribute("foo", "bar");
		assertEquals("bar", n.getAttribute("foo"));
	}

	// ==[ Core Attributes ]====================================================

	@Test
	public void testClassAttribute() throws Exception
	{
		TestHelperAttribute.testClassAttribute(this.n);
	}

	@Test
	public void testIdAttribute() throws Exception
	{
		TestHelperAttribute.testIdAttribute(this.n);
	}

	@Test
	public void testStyleAttribute() throws Exception
	{
		TestHelperAttribute.testStyleAttribute(this.n);
	}

	@Test
	public void testTitleAttribute() throws Exception
	{
		TestHelperAttribute.testTitleAttribute(this.n);
	}

	// ==[ I18n Attributes ]====================================================

	@Test
	public void testDirAttribute() throws Exception
	{
		TestHelperAttribute.testDirAttribute(this.n);
	}

	@Test
	public void testLangAttribute() throws Exception
	{
		TestHelperAttribute.testLangAttribute(this.n);
	}

	@Test
	public void testXmlLangAttribute() throws Exception
	{
		TestHelperAttribute.testXmlLangAttribute(this.n);
	}

	// ==[ Event Attributes ]===================================================

	@Test
	public void testOnClickAttribute() throws Exception
	{
		TestHelperAttribute.testOnClickAttribute(this.n);
	}

	@Test
	public void testOnDblClickAttribute() throws Exception
	{
		TestHelperAttribute.testOnDblClickAttribute(this.n);
	}

	@Test
	public void testOnMouseDownAttribute() throws Exception
	{
		TestHelperAttribute.testOnMouseDownAttribute(this.n);
	}

	@Test
	public void testOnMouseUpAttribute() throws Exception
	{
		TestHelperAttribute.testOnMouseUpAttribute(this.n);
	}

	@Test
	public void testOnMouseOverAttribute() throws Exception
	{
		TestHelperAttribute.testOnMouseOverAttribute(this.n);
	}

	@Test
	public void testOnMouseMoveAttribute() throws Exception
	{
		TestHelperAttribute.testOnMouseMoveAttribute(this.n);
	}

	@Test
	public void testOnMouseOutAttribute() throws Exception
	{
		TestHelperAttribute.testOnMouseOutAttribute(this.n);
	}

	@Test
	public void testOnKeyPressAttribute() throws Exception
	{
		TestHelperAttribute.testOnKeyPressAttribute(this.n);
	}

	@Test
	public void testOnKeyUpAttribute() throws Exception
	{
		TestHelperAttribute.testOnKeyUpAttribute(this.n);
	}

	@Test
	public void testOnKeyDownAttribute() throws Exception
	{
		TestHelperAttribute.testOnKeyDownAttribute(this.n);
	}
}
