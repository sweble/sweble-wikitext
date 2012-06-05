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
