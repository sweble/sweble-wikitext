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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sweble.wikitext.engine.astwom.adapters.ArgAdapter;
import org.sweble.wikitext.engine.astwom.adapters.NameAdapter;
import org.sweble.wikitext.engine.astwom.adapters.ValueAdapter;
import org.sweble.wikitext.engine.wom.WomName;
import org.sweble.wikitext.engine.wom.WomValue;
import org.sweble.wikitext.lazy.preprocessor.TemplateArgument;
import org.sweble.wikitext.lazy.utils.AstBuilder;

public class ArgAdapterTest
{
	private AstWomTestFixture f;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void initialize()
	{
		this.f = new AstWomTestFixture();
	}
	
	// =========================================================================
	
	@Test
	public void canCreateArgUsingDefaultConstructor() throws Exception
	{
		ArgAdapter arg = new ArgAdapter();
		
		assertNull(arg.getName());
		assertNotNull(arg.getArgValue());
		
		TemplateArgument astNode = arg.getAstNode();
		assertEquals(
				astNode.get(0),
				((ValueAdapter) arg.getArgValue()).getAstNode());
	}
	
	@Test
	public void canCreateArgUsingOnlyValue() throws Exception
	{
		WomValue value = new ValueAdapter();
		ArgAdapter arg = new ArgAdapter(value);
		
		assertEquals(value, arg.getArgValue());
		assertNull(arg.getName());
		
		TemplateArgument astNode = arg.getAstNode();
		assertEquals(
				astNode.get(0),
				((ValueAdapter) arg.getArgValue()).getAstNode());
	}
	
	@Test
	public void canCreateArgUsingNameAndValue() throws Exception
	{
		WomName name = new NameAdapter();
		WomValue value = new ValueAdapter();
		ArgAdapter arg = new ArgAdapter(name, value);
		
		assertEquals(name, arg.getName());
		assertEquals(value, arg.getArgValue());
		
		TemplateArgument astNode = arg.getAstNode();
		assertEquals(
				astNode.get(0),
				((NameAdapter) arg.getName()).getAstNode());
		assertEquals(
				astNode.get(1),
				((ValueAdapter) arg.getArgValue()).getAstNode());
	}
	
	@Test
	public void canCreateArgFromAst() throws Exception
	{
		TemplateArgument tmplArg = AstBuilder.astTmplArg()
				.withName("arg")
				.withValue("value")
				.build();
		
		ArgAdapter arg = new ArgAdapter(f.makeFactory(), tmplArg);
		assertEquals("arg", arg.getName().getFirstChild().getText());
		assertEquals("value", arg.getArgValue().getFirstChild().getText());
		
		assertEquals(tmplArg, arg.getAstNode());
		assertEquals(tmplArg.getName(), ((NameAdapter) arg.getName()).getAstNode());
		assertEquals(tmplArg.getValue(), ((ValueAdapter) arg.getArgValue()).getAstNode());
	}
	
	@Test
	public void canRemoveNameFromArg() throws Exception
	{
		TemplateArgument tmplArg = AstBuilder.astTmplArg()
				.withName("arg")
				.withValue("value")
				.build();
		
		ArgAdapter arg = new ArgAdapter(f.makeFactory(), tmplArg);
		
		arg.setName(null);
		
		assertNull(arg.getName());
		assertEquals(0, arg.getAstNode().getName().size());
		assertEquals(
				arg.getAstNode().getValue(),
				((ValueAdapter) arg.getArgValue()).getAstNode());
	}
	
	@Test
	public void cannotRemoveValueFromArg() throws Exception
	{
		TemplateArgument tmplArg = AstBuilder.astTmplArg()
				.withName("arg")
				.withValue("value")
				.build();
		
		ArgAdapter arg = new ArgAdapter(f.makeFactory(), tmplArg);
		
		expectedEx.expect(NullPointerException.class);
		expectedEx.expectMessage("Cannot remove mandatory child <value> from <arg>");
		arg.setArgValue(null);
	}
	
	@Test
	public void canRelaceValue() throws Exception
	{
		TemplateArgument tmplArg = AstBuilder.astTmplArg()
				.withName("arg")
				.withValue("value")
				.build();
		
		ArgAdapter arg = new ArgAdapter(f.makeFactory(), tmplArg);
		
		ValueAdapter newValue = new ValueAdapter();
		arg.replaceChild(arg.getArgValue(), newValue);
		
		assertEquals(newValue, arg.getArgValue());
		assertEquals(arg.getAstNode().getValue(), newValue.getAstNode());
	}
}
