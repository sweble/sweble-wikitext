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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wom3.Wom3Div;
import org.sweble.wom3.Wom3ElementNode;
import org.sweble.wom3.Wom3Node;

import de.fau.cs.osr.utils.NamedParametrized;

@RunWith(value = NamedParametrized.class)
public class BackboneHtmlTest
{
	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		List<Object[]> inputs = new ArrayList<Object[]>();

		inputs.add(new Object[] { "hr" });
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
		inputs.add(new Object[] { "br" });
		inputs.add(new Object[] { "del" });
		inputs.add(new Object[] { "div" });
		inputs.add(new Object[] { "dl" });
		inputs.add(new Object[] { "font" });
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

	public BackboneHtmlTest(String name) throws InstantiationException, IllegalAccessException
	{
		DomImplementationImpl domImpl = DomImplementationImpl.get();
		doc = domImpl.createDocument(Wom3Node.WOM_NS_URI, "article", null);
		this.n = (Wom3ElementNode) doc.createElementNS(Wom3Node.WOM_NS_URI, name);
	}

	// =========================================================================

	@Test
	public void testIsInitiallyEmpty() throws Exception
	{
		assertFalse(n.hasChildNodes());
	}

	@Test
	public void testCanAddChildren() throws Exception
	{
		Wom3Div child = (Wom3Div) doc.createElementNS(Wom3Node.WOM_NS_URI, "div");
		n.appendChild(child);
		assertTrue(n.hasChildNodes());
		assertTrue(child == n.getFirstChild());
		assertTrue(child == n.getLastChild());
	}

	// =========================================================================

	@Test
	public void testAcceptsUnknownAttributes() throws Exception
	{
		assertEquals("", n.getAttribute("some-attribute"));

		n.setAttribute("some-attribute", "some-value");
		assertEquals("some-value", n.getAttribute("some-attribute"));

		n.setAttribute("some-attribute", null);
		assertEquals("", n.getAttribute("some-attribute"));
	}

	@Test
	public void removingAbsentAttributeDoesNotThrow() throws Exception
	{
		assertEquals("", n.getAttribute("michgibtsnet"));
		n.removeAttribute("michgibtsnet");
	}
}
