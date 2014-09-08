/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import static org.junit.Assert.*;

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
