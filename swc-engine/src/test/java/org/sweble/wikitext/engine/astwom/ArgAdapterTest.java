package org.sweble.wikitext.engine.astwom;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
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
		assertEquals(tmplArg.get(0), ((NameAdapter) arg.getName()).getAstNode());
		assertEquals(tmplArg.get(1), ((ValueAdapter) arg.getArgValue()).getAstNode());
	}
}
