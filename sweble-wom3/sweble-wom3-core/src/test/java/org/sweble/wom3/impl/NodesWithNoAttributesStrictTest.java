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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wom3.Wom3ElementNode;
import org.sweble.wom3.Wom3Node;

import de.fau.cs.osr.utils.NamedParametrized;

@RunWith(value = NamedParametrized.class)
public class NodesWithNoAttributesStrictTest
{
	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		List<Object[]> inputs = new ArrayList<Object[]>();

		inputs.add(new Object[] { "text" });
		inputs.add(new Object[] { "body" });
		inputs.add(new Object[] { "imgcaption" });
		inputs.add(new Object[] { "nowiki" });
		inputs.add(new Object[] { "title" });

		return inputs;
	}

	// =========================================================================

	private final Wom3ElementNode n;

	private final DocumentImpl doc;

	public NodesWithNoAttributesStrictTest(String name) throws InstantiationException, IllegalAccessException
	{
		DomImplementationImpl domImpl = DomImplementationImpl.get();
		doc = domImpl.createDocument(Wom3Node.WOM_NS_URI, "article", null);
		this.n = (Wom3ElementNode) doc.createElementNS(Wom3Node.WOM_NS_URI, name);
	}

	// =========================================================================

	@Test(expected = IllegalArgumentException.class)
	public void testSetAttributeThrows() throws Exception
	{
		n.setAttribute("aribtrary", "peng");
	}

	public void testRemoveAbsentAttributeDoesNotThrow() throws Exception
	{
		n.removeAttribute("aribtrary");
	}

	@Test
	public void testGetAttributesDoesNotThrows() throws Exception
	{
		assertEquals("", n.getAttribute("arbitrary"));
		assertNull(n.getAttributeNode("arbitrary"));
	}
}
