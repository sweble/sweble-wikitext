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

import static org.junit.Assert.*;
import static org.sweble.wikitext.lazy.utils.AstBuilder.*;

import org.junit.Test;

public class EngineTextUtilsTest
{
	@Test
	public void testTrimLeftLeavesUntrimmableTextUnaltered() throws Exception
	{
		assertEquals(
				astText("Hello World"),
				EngineTextUtils.trimLeft(astText("Hello World")));
	}
	
	@Test
	public void testTrimLeftTrimsLeftWhitespace() throws Exception
	{
		assertEquals(
				astText("Hello World"),
				EngineTextUtils.trimLeft(astText("  Hello World")));
	}
	
	@Test
	public void testTrimLeftCullsWhitespaceOnlyNodes() throws Exception
	{
		assertEquals(
				astList(astText("Hello World")),
				EngineTextUtils.trimLeft(astList(astText("  "), astText("  Hello World"))));
	}
	
	@Test
	public void testTrimLeftIgnoresCommentsAndIgnoredItems() throws Exception
	{
		assertEquals(
				astList(astComment("Comment"), astIgnored(), astText("Hello World")),
				EngineTextUtils.trimLeft(astList(astText("  "), astComment("Comment"), astIgnored(), astText("  Hello World"))));
	}
	
	// -------
	
	@Test
	public void testTrimRightLeavesUntrimmableTextUnaltered() throws Exception
	{
		assertEquals(
				astText("Hello World"),
				EngineTextUtils.trimRight(astText("Hello World")));
	}
	
	@Test
	public void testTrimRightTrimsRightWhitespace() throws Exception
	{
		assertEquals(
				astText("Hello World"),
				EngineTextUtils.trimRight(astText("Hello World  ")));
	}
	
	@Test
	public void testTrimRightCullsWhitespaceOnlyNodes() throws Exception
	{
		assertEquals(
				astList(astText("Hello World")),
				EngineTextUtils.trimRight(astList(astText("Hello World  "), astText("  "))));
	}
	
	@Test
	public void testTrimRightIgnoresCommentsAndIgnoredItems() throws Exception
	{
		assertEquals(
				astList(astText("Hello World"), astComment("Comment"), astIgnored()),
				EngineTextUtils.trimRight(astList(astText("Hello World  "), astComment("Comment"), astIgnored(), astText("  "))));
	}
}
