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

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sweble.wikitext.parser.nodes.WtXmlAttribute;

public class AttrAdapterTest
{
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void canDefaultConstructAttrAdapterAndRetrieveAttributes() throws Exception
	{
		AttrAdapter attr = new AttrAdapter("test");
		assertEquals("test", attr.getName());
		assertEquals("", attr.getAttrValue());
	}
	
	@Test
	public void canConstructAttrAdapterFromAst() throws Exception
	{
		WtXmlAttribute astAttr = AstBuilder.astXmlAttrib()
				.withName("test")
				.withValue("value")
				.build();
		
		AttrAdapter attr = new AttrAdapter(astAttr);
		assertEquals("test", attr.getName());
		assertEquals("value", attr.getAttrValue());
	}
	
	@Test
	public void astAttribWithoutValueHasItsNameAsValueInWom() throws Exception
	{
		WtXmlAttribute astAttr = AstBuilder.astXmlAttrib()
				.withoutValue()
				.build();
		
		AttrAdapter attr = new AttrAdapter(astAttr);
		assertEquals(attr.getName(), attr.getAttrValue());
		
		// This is only how the WOM sees it, the AST remains unchanged!
		assertNull(((WtXmlAttribute) attr.getAstNode()).getValue());
	}
	
	/*
	 * See NativeOrXmlAttributeAdapter for explanation of problem
	 */
	@Ignore
	@Test
	public void astAttribWithoutValueGetsValueAfterSetting() throws Exception
	{
		WtXmlAttribute astAttr = AstBuilder.astXmlAttrib()
				.withoutValue()
				.build();
		
		AttrAdapter attr = new AttrAdapter(astAttr);
		assertEquals(attr.getName(), attr.getAttrValue());
		
		attr.setAttrValue("new value");
		
		assertNotNull(((WtXmlAttribute) attr.getAstNode()).getValue());
	}
	
	@Test
	public void cannotGiveAttrInvalidName() throws Exception
	{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Not a valid XML Name");
		new AttrAdapter("not a valid attribute name");
	}
	
	@Test
	public void cannotRemoveNameAttribute() throws Exception
	{
		AttrAdapter attr = new AttrAdapter("test");
		expectedEx.expect(UnsupportedOperationException.class);
		expectedEx.expectMessage("Attribute `name' cannot be removed");
		attr.removeAttribute("name");
	}
	
	@Test
	public void cannotRemoveValueAttribute() throws Exception
	{
		AttrAdapter attr = new AttrAdapter("test");
		expectedEx.expect(UnsupportedOperationException.class);
		expectedEx.expectMessage("Attribute `value' cannot be removed");
		attr.removeAttribute("value");
	}
}
