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
package org.sweble.wikitext.lazy;

import junit.framework.Assert;

import org.junit.Test;
import org.sweble.wikitext.lazy.utils.SimpleParserConfig;

public class LinkTargetParserTest
{
	private ParserConfigInterface config = new SimpleParserConfig();

	@Test
	public void testXmlDecode()
	{
		Assert.assertEquals("", LinkTargetParser.xmlDecode(config, ""));

		Assert.assertEquals("ASDF", LinkTargetParser.xmlDecode(config, "ASDF"));

		Assert.assertEquals("&", LinkTargetParser.xmlDecode(config, "&"));

		// ----

		Assert.assertEquals("&MUHAHA;", LinkTargetParser.xmlDecode(config, "&MUHAHA;"));

		Assert.assertEquals("&...", LinkTargetParser.xmlDecode(config, "&amp;..."));

		Assert.assertEquals("...&", LinkTargetParser.xmlDecode(config, "...&amp;"));

		Assert.assertEquals("...&...", LinkTargetParser.xmlDecode(config, "...&amp;..."));

		Assert.assertEquals("...&amp;...", LinkTargetParser.xmlDecode(config, "...&amp;amp;..."));

		// ----

		Assert.assertEquals("&#--;", LinkTargetParser.xmlDecode(config, "&#--;"));

		Assert.assertEquals("&#4294967296;", LinkTargetParser.xmlDecode(config, "&#4294967296;"));

		Assert.assertEquals(" ...", LinkTargetParser.xmlDecode(config, "&#32;..."));

		Assert.assertEquals("... ", LinkTargetParser.xmlDecode(config, "...&#32;"));

		Assert.assertEquals("... ...", LinkTargetParser.xmlDecode(config, "...&#32;..."));

		Assert.assertEquals("...&#20;...", LinkTargetParser.xmlDecode(config, "...&#2&#48;;..."));

		// ----

		Assert.assertEquals("&#x--;", LinkTargetParser.xmlDecode(config, "&#x--;"));

		Assert.assertEquals("&#x100000000;", LinkTargetParser.xmlDecode(config, "&#x100000000;"));

		Assert.assertEquals(" ...", LinkTargetParser.xmlDecode(config, "&#x20;..."));

		Assert.assertEquals("... ", LinkTargetParser.xmlDecode(config, "...&#x20;"));

		Assert.assertEquals("... ...", LinkTargetParser.xmlDecode(config, "...&#x20;..."));

		Assert.assertEquals("...&#x20;...", LinkTargetParser.xmlDecode(config, "...&#x2&#x30;;..."));
	}

	@Test
	public void testUrlDecode()
	{
		Assert.assertEquals("", LinkTargetParser.urlDecode(""));

		Assert.assertEquals("ASDF", LinkTargetParser.urlDecode("ASDF"));

		Assert.assertEquals("%", LinkTargetParser.urlDecode("%"));

		// ---

		Assert.assertEquals("%-", LinkTargetParser.urlDecode("%-"));

		Assert.assertEquals("%--", LinkTargetParser.urlDecode("%--"));

		Assert.assertEquals("%gg", LinkTargetParser.urlDecode("%gg"));

		Assert.assertEquals("%0", LinkTargetParser.urlDecode("%0"));

		Assert.assertEquals("%00", LinkTargetParser.urlDecode("%00"));

		Assert.assertEquals("A", LinkTargetParser.urlDecode("%41"));

		Assert.assertEquals("Aa", LinkTargetParser.urlDecode("%41a"));

		Assert.assertEquals("aA", LinkTargetParser.urlDecode("a%41"));

		Assert.assertEquals("aAa", LinkTargetParser.urlDecode("a%41a"));
	}
	
	@Test
	public void testTrimUnderscoreEmptyString() throws Exception
	{
		Assert.assertEquals("", LinkTargetParser.trimUnderscore(""));
	}
	
	@Test
	public void testTrimEmptyString() throws Exception
	{
		Assert.assertEquals("", LinkTargetParser.trim(""));
	}
	
	@Test
	public void testTrimUnderscoreWithOnlyUnderscores() throws Exception
	{
		Assert.assertEquals("", LinkTargetParser.trimUnderscore("_"));
	}
}
