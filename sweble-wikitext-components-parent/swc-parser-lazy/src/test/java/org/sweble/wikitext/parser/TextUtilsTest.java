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

package org.sweble.wikitext.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sweble.wikitext.parser.nodes.WikitextNodeFactory;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.utils.AstTextUtils;
import org.sweble.wikitext.parser.utils.AstTextUtils.PartialConversion;
import org.sweble.wikitext.parser.utils.SimpleParserConfig;
import org.sweble.wikitext.parser.utils.StringConversionException;

public class TextUtilsTest
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private final ParserConfig config;

	private final AstTextUtils tu;

	private final WikitextNodeFactory nf;

	// =========================================================================

	public TextUtilsTest()
	{
		config = new SimpleParserConfig();
		tu = config.getAstTextUtils();
		nf = config.getNodeFactory();
	}

	// =========================================================================

	@Test
	public void testJustOneTextNode() throws Exception
	{
		assertEquals("Hallo!", tu.astToText(nf.text("Hallo!")));
	}

	@Test
	public void testTwoTextNodes() throws Exception
	{
		assertEquals("Hallo Welt!", tu.astToText(nf.list(
				nf.text("Hallo "),
				nf.text("Welt!"))));
	}

	@Test
	public void testTwoTextNodesAndEntityRef() throws Exception
	{
		assertEquals("Yes & No", tu.astToText(nf.list(
				nf.text("Yes "),
				nf.entityRef("amp", "&"),
				nf.text(" No"))));
	}

	@Test
	public void testTwoTextNodesAndUnresolvedEntityRef() throws Exception
	{
		thrown.expect(StringConversionException.class);
		tu.astToText(nf.list(
				nf.text("Yes "),
				nf.entityRef("unknown", null),
				nf.text(" No")),
				AstTextUtils.FAIL_ON_UNRESOLVED_ENTITY_REF);
	}

	@Test
	public void testIgnoresTransparentNodes() throws Exception
	{
		assertEquals("Yes & No", tu.astToText(nf.list(
				nf.text("Yes "),
				nf.ignored("Ignored"),
				nf.entityRef("amp", "&"),
				nf.comment("Ignored"),
				nf.text(" No"))));
	}

	@Test
	public void testFailsOnUnkonwnNodes() throws Exception
	{
		thrown.expect(StringConversionException.class);
		tu.astToText(nf.list(
				nf.text("Yes "),
				nf.url("http", "//example.com"),
				nf.text(" No")));
	}

	// =========================================================================

	@Test
	public void testPartialJustOneTextNode() throws Exception
	{
		PartialConversion result = tu.astToTextPartial(nf.text("Hallo!"));
		assertEquals("Hallo!", result.getText());
		assertTrue(result.getTail().isEmpty());
	}

	@Test
	public void testPartialTwoTextNodes() throws Exception
	{
		PartialConversion result = tu.astToTextPartial(nf.list(
				nf.text("Hallo "),
				nf.text("Welt!")));
		assertEquals("Hallo Welt!", result.getText());
		assertTrue(result.getTail().isEmpty());
	}

	@Test
	public void testPartialTwoTextNodesAndEntityRef() throws Exception
	{
		PartialConversion result = tu.astToTextPartial(nf.list(
				nf.text("Yes "),
				nf.entityRef("amp", "&"),
				nf.text(" No")));
		assertEquals("Yes & No", result.getText());
		assertTrue(result.getTail().isEmpty());
	}

	@Test
	public void testPartialTwoTextNodesAndUnresolvedEntityRef() throws Exception
	{
		WtNodeList ast = nf.list(
				nf.text("Yes "),
				nf.entityRef("unknown", null),
				nf.text(" No"));
		PartialConversion result = tu.astToTextPartial(
				ast,
				AstTextUtils.FAIL_ON_UNRESOLVED_ENTITY_REF);
		assertEquals("Yes ", result.getText());
		assertEquals(2, result.getTail().size());
		assertEquals(ast.get(1), result.getTail().get(0));
		assertEquals(ast.get(2), result.getTail().get(1));
	}

	@Test
	public void testPartialDoesNotFailOnUnkonwnNodes() throws Exception
	{
		WtNodeList ast = nf.list(
				nf.text("Yes "),
				nf.url("http", "//example.com"),
				nf.text(" No"));
		PartialConversion result = tu.astToTextPartial(ast);
		assertEquals("Yes ", result.getText());
		assertEquals(2, result.getTail().size());
		assertEquals(ast.get(1), result.getTail().get(0));
		assertEquals(ast.get(2), result.getTail().get(1));
	}

}
