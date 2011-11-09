/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sweble.wikitext.engine.astwom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Test;
import org.sweble.wikitext.engine.Page;
import org.sweble.wikitext.engine.astwom.adapters.BodyAdapter;
import org.sweble.wikitext.engine.astwom.adapters.PageAdapter;
import org.sweble.wikitext.engine.astwom.adapters.RedirectAdapter;
import org.sweble.wikitext.engine.wom.WomAttribute;
import org.sweble.wikitext.engine.wom.WomBody;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.engine.wom.WomPage;
import org.sweble.wikitext.engine.wom.tools.WomPrinter;

import de.fau.cs.osr.ptk.common.AstPrinter;
import de.fau.cs.osr.ptk.common.ast.AstNode;

public class PageAdapterTest
{
	/*
	 * Test element low level functions
	 */
	
	// =========================================================================
	
	@Test
	public void testCtor1()
	{
		{
			WomPage p = new PageAdapter((String) null, (String) null, "Title");
			assertEquals(null, p.getNamespace());
			assertEquals(null, p.getPath());
			assertEquals("Title", p.getTitle());
			assertEquals("Title", p.getName());
		}
		{
			WomPage p = new PageAdapter("", "", "Title");
			assertEquals(null, p.getNamespace());
			assertEquals(null, p.getPath());
			assertEquals("Title", p.getTitle());
			assertEquals("Title", p.getName());
		}
		{
			WomPage p = new PageAdapter("Namespace", "Path", "Title");
			assertEquals("Namespace", p.getNamespace());
			assertEquals("Path", p.getPath());
			assertEquals("Title", p.getTitle());
			assertEquals("Namespace:Path/Title", p.getName());
		}
		{
			WomPage p = new PageAdapter("Namespace", "Path/", "Title");
			assertEquals("Namespace", p.getNamespace());
			assertEquals("Path", p.getPath());
			assertEquals("Title", p.getTitle());
			assertEquals("Namespace:Path/Title", p.getName());
		}
		{
			WomPage p = new PageAdapter("", "", "Title");
			assertEquals(WomNodeType.DOCUMENT, p.getNodeType());
			assertEquals("page", p.getNodeName());
			assertEquals("1.0", p.getVersion());
			assertEquals(null, p.getNamespace());
			assertEquals(null, p.getPath());
			assertEquals("Title", p.getTitle());
			assertEquals("Title", p.getName());
			assertTrue(p.supportsAttributes());
			assertTrue(p.supportsChildren());
			assertTrue(p.hasChildNodes());
			assertFalse(p.isRedirect());
			assertNotNull(p.getBody());
			assertNull(p.getParent());
			assertNull(p.getRedirect());
			assertNull(p.getText());
			assertNull(p.getValue());
			assertTrue(p.getFirstChild() == p.getBody());
			assertTrue(p.getLastChild() == p.getBody());
			assertEquals(
			        Arrays.asList(new WomNode[] { p.getBody() }),
			        new LinkedList<WomNode>(p.childNodes()));
		}
		{
			WomPage p = new PageAdapter("Namespace", "Path", "Title");
			for (WomAttribute attr : p.getAttributes())
			{
				if (attr.getName().equalsIgnoreCase("namespace"))
					assertEquals("Namespace", attr.getValue());
				else if (attr.getName().equalsIgnoreCase("path"))
					assertEquals("Path", attr.getValue());
				else if (attr.getName().equalsIgnoreCase("title"))
					assertEquals("Title", attr.getValue());
				else if (attr.getName().equalsIgnoreCase("version"))
					assertEquals("1.0", attr.getValue());
				else
					assertTrue("Unexpected attribute: " + attr.getName(), false);
			}
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCtor1_1()
	{
		new PageAdapter("Namespace:", "Path", "Title");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCtor1_2()
	{
		new PageAdapter("Namespace/", "Path", "Title");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCtor1_3()
	{
		new PageAdapter("Namespace", "/", "Title");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCtor1_4()
	{
		new PageAdapter("Namespace", "/Path", "Title");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCtor1_5()
	{
		new PageAdapter("Namespace", "/Path/", "Title");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCtor1_6()
	{
		new PageAdapter("Namespace", "Path//", "Title");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCtor1_7()
	{
		new PageAdapter("", "", "Title:");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCtor1_8()
	{
		new PageAdapter("", "", "Title/");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCtor1_9()
	{
		new PageAdapter("", "", "");
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testCtor1_10()
	{
		new PageAdapter("", "", (String) null);
	}
	
	// =========================================================================
	
	@Test
	public void testLowLevelAttr()
	{
		{
			WomPage p = new PageAdapter("x", "y", "z");
			p.setAttribute("namespace", null);
			p.setAttribute("path", null);
			p.setAttribute("title", "Title");
			assertEquals(null, p.getNamespace());
			assertEquals(null, p.getPath());
			assertEquals("Title", p.getTitle());
			assertEquals("Title", p.getName());
		}
		{
			WomPage p = new PageAdapter("x", "y", "z");
			p.setAttribute("namespace", "");
			p.setAttribute("path", "");
			p.setAttribute("title", "Title");
			assertEquals(null, p.getNamespace());
			assertEquals(null, p.getPath());
			assertEquals("Title", p.getTitle());
			assertEquals("Title", p.getName());
		}
		{
			WomPage p = new PageAdapter("x", "y", "z");
			p.setAttribute("namespace", "Namespace");
			p.setAttribute("path", "Path");
			p.setAttribute("title", "Title");
			assertEquals("Namespace", p.getNamespace());
			assertEquals("Path", p.getPath());
			assertEquals("Title", p.getTitle());
			assertEquals("Namespace:Path/Title", p.getName());
		}
		{
			WomPage p = new PageAdapter("x", "y", "z");
			p.setAttribute("namespace", "Namespace");
			p.setAttribute("path", "Path/");
			p.setAttribute("title", "Title");
			assertEquals("Namespace", p.getNamespace());
			assertEquals("Path", p.getPath());
			assertEquals("Title", p.getTitle());
			assertEquals("Namespace:Path/Title", p.getName());
		}
		{
			WomPage p = new PageAdapter("x", "y", "z");
			p.setAttribute("NamespacE", "Namespace");
			p.setAttribute("PatH", "Path/");
			p.setAttribute("TitlE", "Title");
			assertEquals("Namespace:Path/Title", p.getName());
			assertEquals("Namespace", p.getAttribute("namespace"));
			assertEquals("Namespace", p.getAttribute("NAMESPACE"));
			assertEquals("Path", p.getAttribute("path"));
			assertEquals("Path", p.getAttribute("PATH"));
			assertEquals("Title", p.getAttribute("title"));
			assertEquals("Title", p.getAttribute("TITLE"));
			assertNull(p.getAttribute("name"));
		}
		{
			WomPage p = new PageAdapter("x", "y", "z");
			p.setAttribute("NamespacE", "Namespace");
			p.setAttribute("PatH", "Path/");
			p.setAttribute("TitlE", "Title");
			assertEquals("NamespacE", p.getAttributeNode("namespace").getName());
			assertEquals("NamespacE", p.getAttributeNode("NAMESPACE").getName());
			assertEquals("Namespace", p.getAttributeNode("namespace").getValue());
			assertEquals("Namespace", p.getAttributeNode("NAMESPACE").getValue());
			assertEquals("PatH", p.getAttributeNode("path").getName());
			assertEquals("PatH", p.getAttributeNode("PATH").getName());
			assertEquals("Path", p.getAttributeNode("path").getValue());
			assertEquals("Path", p.getAttributeNode("PATH").getValue());
			assertEquals("TitlE", p.getAttributeNode("title").getName());
			assertEquals("TitlE", p.getAttributeNode("TITLE").getName());
			assertEquals("Title", p.getAttributeNode("title").getValue());
			assertEquals("Title", p.getAttributeNode("TITLE").getValue());
			assertNull(p.getAttributeNode("name"));
			assertNull(p.getAttribute("idontexist"));
		}
	}
	
	// =========================================================================
	
	@Test
	public void testAttrSetter1()
	{
		WomPage p = new PageAdapter("", "", "Title");
		
		assertNull(p.setNamespace("Namespace"));
		assertEquals("Namespace", p.setNamespace("Namespace2"));
		
		assertNull(p.setPath("Path"));
		assertEquals("Path", p.setPath("Path2"));
		
		assertEquals("Title", p.setTitle("Title2"));
		assertEquals("Title2", p.setTitle("Title3"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAttrSetter2()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.setNamespace("Namespace:");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAttrSetter3()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.setPath("Path//");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAttrSetter4()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.setTitle("Title/");
	}
	
	// =========================================================================
	
	@Test(expected = IllegalArgumentException.class)
	public void testLowLevelAttrSetter1()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.setAttribute("namespace", "Namespace:");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLowLevelAttrSetter2()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.setAttribute("path", "Path//");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLowLevelAttrSetter3()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.setAttribute("title", "Title/");
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testLowLevelAttrSetter4()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.setAttribute("version", "1.0");
	}
	
	// =========================================================================
	
	@Test
	public void testLowLevelAttrRemove1()
	{
		WomPage p = new PageAdapter("ns", "path", "Title");
		assertEquals("ns", p.removeAttribute("namespace").getValue());
		assertEquals("path", p.removeAttribute("path").getValue());
		assertNull(p.getAttribute("namespace"));
		assertNull(p.getAttribute("path"));
		assertNull(p.setAttribute("namespace", "ns"));
		assertNull(p.setAttribute("path", "path"));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testLowLevelAttrRemove2()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.setAttribute("version", null);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testLowLevelAttrRemove3()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.setAttribute("title", null);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testLowLevelAttrRemove4()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.removeAttribute("version");
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testLowLevelAttrRemove5()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.removeAttribute("title");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLowLevelAttrRemove6()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.removeAttribute("idontexist");
	}
	
	// =========================================================================
	
	@Test
	public void testChild1()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		BodyAdapter newBody = new BodyAdapter();
		WomBody oldBody = p.getBody();
		assertTrue(oldBody == p.setBody(newBody));
		assertTrue(newBody == p.getBody());
		assertTrue(newBody == p.getFirstChild());
		assertTrue(newBody == p.getLastChild());
	}
	
	@Test(expected = NullPointerException.class)
	public void testChild2()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.setBody(null);
	}
	
	@Test
	public void testChild3()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		RedirectAdapter newRedirect = new RedirectAdapter("asdf");
		assertNull(p.setRedirect(newRedirect));
		assertTrue(p.isRedirect());
		assertTrue(newRedirect == p.getRedirect());
		assertTrue(newRedirect == p.getFirstChild());
		assertTrue(p.getLastChild() == p.getBody());
		assertTrue(newRedirect == p.setRedirect(null));
		assertNull(p.getRedirect());
		assertFalse(p.isRedirect());
		assertTrue(p.getFirstChild() == p.getBody());
		assertTrue(p.getLastChild() == p.getBody());
	}
	
	@Test
	public void testChild4()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.setCategory("Test");
		p.setCategory("Test2");
		
		System.out.println(WomPrinter.print(p));
		
		AstNode n = ((PageAdapter) p).getAstNode();
		System.out.print(AstPrinter.print(n));
	}
	
	// =========================================================================
	
	@Test(expected = UnsupportedOperationException.class)
	public void testLowLevelChild1()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.appendChild(new BodyAdapter());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testLowLevelChild2()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.insertBefore(p.getLastChild(), new RedirectAdapter("asdf"));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testLowLevelChild3()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		p.removeChild(p.getLastChild());
	}
	
	@Test
	public void testLowLevelChild4()
	{
		WomPage p = new PageAdapter((String) null, (String) null, "Title");
		BodyAdapter newBody = new BodyAdapter();
		p.replaceChild(p.getBody(), newBody);
		assertTrue(newBody == p.getBody());
		assertTrue(newBody == p.getFirstChild());
		assertTrue(newBody == p.getLastChild());
	}
	
	// =========================================================================
	
	@Test
	public void testAst()
	{
		WomPage p = new PageAdapter("ns", "path", "Title");
		AstNode n = ((PageAdapter) p).getAstNode();
		assertFalse(n.hasAttributes());
		assertFalse(n.hasProperties());
		assertTrue(n instanceof Page);
		assertNotNull(((Page) n).getContent());
		assertEquals(0, ((Page) n).getContent().size());
		//System.out.print(AstPrinter.print(n));
	}
}
