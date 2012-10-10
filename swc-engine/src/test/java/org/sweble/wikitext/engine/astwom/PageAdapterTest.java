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

import static org.junit.Assert.*;
import static org.sweble.wikitext.engine.wom.tools.AstWomBuilder.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sweble.wikitext.parser.nodes.WtInternalLink;

public class PageAdapterTest
{
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	// =========================================================================
	
	@Test
	public void newPageAdpaterIsCorrectlyInitialized()
	{
		WomPage page = womPage().withNamespace("Ns").withPath("Path").build();
		
		assertEquals("page", page.getNodeName());
		assertEquals("1.0", page.getVersion());
		assertEquals("Ns", page.getNamespace());
		assertEquals("Path", page.getPath());
		assertEquals("Default Page", page.getTitle());
		
		assertNotNull(page.getBody());
		
		assertFalse(page.isRedirect());
		assertNull(page.getRedirect());
		
		assertEquals(WomNodeType.DOCUMENT, page.getNodeType());
		assertNull(page.getText());
		assertNull(page.getValue());
		assertTrue(page.supportsAttributes());
		assertTrue(page.supportsChildren());
		assertTrue(page.hasChildNodes());
		assertNull(page.getParent());
		assertTrue(page.getFirstChild() == page.getBody());
		assertTrue(page.getLastChild() == page.getBody());
		
		assertArrayEquals(
				new WomNode[] { page.getBody() },
				page.childNodes().toArray());
	}
	
	@Test
	public void getNameConcatenatesNamespacePathAndTitle()
	{
		WomPage page = womPage().withNamespace("Ns").withPath("Path").build();
		assertEquals("Ns:Path/Default Page", page.getName());
	}
	
	@Test
	public void emptyStringInNamespaceAndPathAreTreatedAsNull()
	{
		WomPage page = womPage().withNamespace("").withPath("").build();
		assertNull(page.getNamespace());
		assertNull(page.getPath());
	}
	
	@Test
	public void pagePropertiesCanBeEnumeratedInAttributes() throws Exception
	{
		WomPage page = womPage().withNamespace("Ns").withPath("Path").build();
		
		for (WomAttribute attr : page.getAttributes())
		{
			if (attr.getName().equalsIgnoreCase("namespace"))
				assertEquals("Ns", attr.getValue());
			else if (attr.getName().equalsIgnoreCase("path"))
				assertEquals("Path", attr.getValue());
			else if (attr.getName().equalsIgnoreCase("title"))
				assertEquals("Default Page", attr.getValue());
			else if (attr.getName().equalsIgnoreCase("version"))
				assertEquals("1.0", attr.getValue());
			else
				fail();
		}
	}
	
	@Test
	public void cannotRemoveUnknownAttribute() throws Exception
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("An attribute named `iDontExist' is not supported by this element!");
		womPage().build().removeAttribute("iDontExist");
	}
	
	@Test
	public void cannotAddAribtraryAttribute() throws Exception
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("An attribute named `imRandom' is not supported by this element!");
		womPage().build().setAttribute("imRandom", "value");
	}
	
	// =========================================================================
	
	@Test
	public void canInstantiateWomPageWithNullNamespace()
	{
		WomPage page = womPage().withNamespace(null).build();
		assertNull(page.getNamespace());
	}
	
	@Test
	public void canRetrieveNamespaceAfterInstantiation()
	{
		WomPage page = womPage().withNamespace("Ns").build();
		assertEquals("Ns", page.getNamespace());
	}
	
	@Test
	public void canRetrieveNamespaceAfterInstantiationFromAttribute()
	{
		WomPage page = womPage().withNamespace("Ns").build();
		assertEquals("Ns", page.getAttribute("namespace"));
	}
	
	@Test
	public void canSetNamespaceAttributeToNull()
	{
		WomPage page = womPage().withNamespace("Ns").build();
		assertEquals("Ns", page.getNamespace());
		
		page.setAttribute("namespace", null);
		assertNull(page.getNamespace());
	}
	
	@Test
	public void settingNamespaceToEmptyStringRemovesTheNamespaceAttribute()
	{
		WomPage page = womPage().withNamespace("Ns").build();
		assertEquals("Ns", page.getNamespace());
		
		page.setNamespace("");
		assertNull(page.getAttribute("namesapce"));
		assertNull(page.getNamespace());
	}
	
	@Test
	public void namespaceContainingColonRaisesException()
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid namespace");
		womPage().withNamespace("Namespace:").build();
	}
	
	@Test
	public void namespaceContainingSlashRaisesException()
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid namespace");
		womPage().withNamespace("Namespace/").build();
	}
	
	@Test
	public void namespaceIsCheckedWhenSettingAttribute()
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid namespace");
		womPage().build().setAttribute("namespace", "Namespace/");
	}
	
	@Test
	public void namespaceIsCheckedWhenUsingSetter()
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid namespace");
		womPage().build().setNamespace("Namespace/");
	}
	
	@Test
	public void namespaceAttributeNameIsCaseSsensitive()
	{
		WomPage page = womPage().withNamespace("Ns").build();
		assertNull(page.getAttribute("NAMESPACE"));
		
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("An attribute named `NAMESPACE' is not supported by this element!");
		page.setAttribute("NAMESPACE", "Ns 2");
	}
	
	@Test
	public void canRemoveNamespaceAttribute() throws Exception
	{
		WomPage page = womPage().withNamespace("Ns").build();
		assertNotNull(page.getAttribute("namespace"));
		assertEquals("Ns:Default Page", page.getName());
		
		page.removeAttribute("namespace");
		assertNull(page.getAttribute("namespace"));
		assertNull(page.getNamespace());
		assertEquals("Default Page", page.getName());
	}
	
	// =========================================================================
	
	@Test
	public void canInstantiateWomPageWithNullPath()
	{
		WomPage page = womPage().withPath(null).build();
		assertNull(page.getPath());
	}
	
	@Test
	public void canRetrievePathAfterInstantiation()
	{
		WomPage page = womPage().withPath("Path").build();
		assertEquals("Path", page.getPath());
	}
	
	@Test
	public void canRetrievePathAfterInstantiationFromAttribute()
	{
		WomPage page = womPage().withPath("Path").build();
		assertEquals("Path", page.getAttribute("path"));
	}
	
	@Test
	public void canSetPathAttributeToNull()
	{
		WomPage page = womPage().withPath("Path").build();
		assertEquals("Path", page.getPath());
		
		page.setAttribute("path", null);
		assertNull(page.getPath());
	}
	
	@Test
	public void settingPathToEmptyStringRemovesThePathAttribute()
	{
		WomPage page = womPage().withPath("Path").build();
		assertEquals("Path", page.getPath());
		
		page.setPath("");
		assertNull(page.getAttribute("path"));
		assertNull(page.getPath());
	}
	
	@Test
	public void pathThatIsOnlyASlashRaisesException()
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid path");
		womPage().withPath("/").build();
	}
	
	@Test
	public void pathThatStartsWithSlashRaisesException()
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid path");
		womPage().withPath("/Path").build();
	}
	
	@Test
	public void slashAtTheEndOfPathIsStripped()
	{
		WomPage page = womPage().withPath("Path/").build();
		assertEquals("Path", page.getPath());
	}
	
	@Test
	public void doubleSlashInPathRaisesException() throws Exception
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid path");
		womPage().withPath("Path//").build();
	}
	
	@Test
	public void pathIsCheckedWhenSettingAttribute()
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid path");
		womPage().build().setAttribute("path", "/");
	}
	
	@Test
	public void pathIsCheckedWhenUsingSetter()
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid path");
		womPage().build().setPath("/");
	}
	
	@Test
	public void pathAttributeNameIsCaseSsensitive()
	{
		WomPage page = womPage().withPath("Path").build();
		assertNull(page.getAttribute("PATH"));
		
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("An attribute named `PATH' is not supported by this element!");
		page.setAttribute("PATH", "Path 2");
	}
	
	@Test
	public void canRemovePathAttribute() throws Exception
	{
		WomPage page = womPage().withPath("Path").build();
		assertNotNull(page.getAttribute("path"));
		assertEquals("Path/Default Page", page.getName());
		
		page.removeAttribute("path");
		assertNull(page.getAttribute("path"));
		assertNull(page.getPath());
		assertEquals("Default Page", page.getName());
	}
	
	// =========================================================================
	
	@Test
	public void cannotInstantiatePageWithNullTitle()
	{
		expectedEx.expect(UnsupportedOperationException.class);
		expectedEx.expectMessage("Cannot remove attribute `title'");
		womPage().withTitle(null).build();
	}
	
	@Test
	public void cannotInstantiatePageWithEmptyTitle()
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid title");
		womPage().withTitle("").build();
	}
	
	@Test
	public void cannotSetPageWithEmptyTitle()
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid title");
		womPage().withTitle("").build();
	}
	
	@Test
	public void slashInTitleRaisesException() throws Exception
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid title");
		womPage().withTitle("Title/").build();
	}
	
	@Test
	public void colonInTitleRaisesException() throws Exception
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid title");
		womPage().withTitle("Title:").build();
	}
	
	@Test
	public void titleIsCheckedWhenSettingAttribute()
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid title");
		womPage().build().setAttribute("title", "Title/");
	}
	
	@Test
	public void titleIsCheckedWhenUsingSetter()
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid title");
		womPage().build().setTitle("Title/");
	}
	
	@Test
	public void titleAttributeNameIsCaseSsensitive()
	{
		WomPage page = womPage().build();
		assertNull(page.getAttribute("TITLE"));
		
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("An attribute named `TITLE' is not supported by this element!");
		page.setAttribute("TITLE", "Title");
	}
	
	@Test
	public void cannotRemoveTitleAttribute() throws Exception
	{
		WomPage page = womPage().build();
		assertNotNull(page.getAttribute("title"));
		
		expectedEx.expect(UnsupportedOperationException.class);
		expectedEx.expectMessage("Attribute `title' cannot be removed");
		page.removeAttribute("title");
	}
	
	// =========================================================================
	
	@Test
	public void versionAttributeCannotBeRemoved() throws Exception
	{
		WomPage page = womPage().build();
		
		expectedEx.expect(UnsupportedOperationException.class);
		expectedEx.expectMessage("Attribute `version' cannot be removed");
		page.removeAttribute("version");
	}
	
	@Test
	public void versionAttributeCannotBeAltered() throws Exception
	{
		WomPage page = womPage().build();
		
		expectedEx.expect(UnsupportedOperationException.class);
		expectedEx.expectMessage("Cannot alter read-only attribute `version'");
		page.setAttribute("version", "2.0");
	}
	
	// =========================================================================
	
	@Test
	public void setBodyReAttachesCategoriesToNewBody() throws Exception
	{
		WomPage page = womPage().build();
		page.addCategory("some category");
		
		WomBody oldBody = page.getBody();
		assertTrue(((BodyAdapter) oldBody).getAstNode().get(0) instanceof WtInternalLink);
		
		WomBody newBody = womBody().build();
		page.setBody(newBody);
		
		assertTrue(page.hasCategory("some category"));
		assertTrue(((BodyAdapter) newBody).getAstNode().get(0) instanceof WtInternalLink);
		assertTrue(((BodyAdapter) oldBody).getAstNode().isEmpty());
	}
}
