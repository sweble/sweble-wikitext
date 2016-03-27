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

package org.sweble.wikitext.engine.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngineNodeFactory;

public class EngineTextUtilsTest
{
	WikiConfig config = DefaultConfigEnWp.generate();

	EngineNodeFactory nf = config.getNodeFactory();

	EngineAstTextUtils tu = config.getAstTextUtils();

	@Test
	public void testTrimLeftLeavesUntrimmableTextUnaltered() throws Exception
	{
		assertEquals(
				nf.text("Hello World"),
				tu.trimLeft(nf.text("Hello World")));
	}

	@Test
	public void testTrimLeftTrimsLeftWhitespace() throws Exception
	{
		assertEquals(
				nf.text("Hello World"),
				tu.trimLeft(nf.text("  Hello World")));
	}

	@Test
	public void testTrimLeftCullsWhitespaceOnlyNodes() throws Exception
	{
		assertEquals(
				nf.list(nf.text("Hello World")),
				tu.trimLeft(nf.list(nf.text("  "), nf.text("  Hello World"))));
	}

	@Test
	public void testTrimLeftIgnoresCommentsAndIgnoredItems() throws Exception
	{
		assertEquals(
				nf.list(nf.comment("Comment"), nf.ignored(""), nf.text("Hello World")),
				tu.trimLeft(nf.list(nf.text("  "), nf.comment("Comment"), nf.ignored(""), nf.text("  Hello World"))));
	}

	// -------

	@Test
	public void testTrimRightLeavesUntrimmableTextUnaltered() throws Exception
	{
		assertEquals(
				nf.text("Hello World"),
				tu.trimRight(nf.text("Hello World")));
	}

	@Test
	public void testTrimRightTrimsRightWhitespace() throws Exception
	{
		assertEquals(
				nf.text("Hello World"),
				tu.trimRight(nf.text("Hello World  ")));
	}

	@Test
	public void testTrimRightCullsWhitespaceOnlyNodes() throws Exception
	{
		assertEquals(
				nf.list(nf.text("Hello World")),
				tu.trimRight(nf.list(nf.text("Hello World  "), nf.text("  "))));
	}

	@Test
	public void testTrimRightIgnoresCommentsAndIgnoredItems() throws Exception
	{
		assertEquals(
				nf.list(nf.text("Hello World"), nf.comment("Comment"), nf.ignored("")),
				tu.trimRight(nf.list(nf.text("Hello World  "), nf.comment("Comment"), nf.ignored(""), nf.text("  "))));
	}
}
