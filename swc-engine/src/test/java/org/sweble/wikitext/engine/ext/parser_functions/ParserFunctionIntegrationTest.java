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

package org.sweble.wikitext.engine.ext.parser_functions;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.sweble.wikitext.engine.CompiledPage;
import org.sweble.wikitext.engine.Compiler;
import org.sweble.wikitext.engine.ExpansionCallback;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.FullPage;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.utils.DefaultConfigEn;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wikitext.parser.utils.RtDataPrinter;

public class ParserFunctionIntegrationTest
{
	private Compiler compiler;
	
	private PageId pageId;
	
	@Before
	public void setUp() throws FileNotFoundException, JAXBException, LinkTargetException
	{
		WikiConfigImpl config = DefaultConfigEn.generate();
		
		this.compiler = new Compiler(config);
		
		this.pageId = new PageId(PageTitle.make(config, "dummy"), -1);
	}
	
	@Test
	public void testComplexNestedIfExpr() throws Exception
	{
		String wikitext = ""
				+ "{{#ifexpr:8<0"
				+ "|{{#ifexpr:((8)round 0)!=(8)"
				+ " |{{#expr:12-(((0.5-(8))round 0)mod 12)}}"
				+ " |{{#expr:12-(((11.5-(8))round 0)mod 12)}}"
				+ " }}"
				+ "|8}}";
		
		CompiledPage result = compiler.expand(pageId, wikitext, new ExpansionCallbackDummy());
		
		String expandedWt = RtDataPrinter.print(result.getPage());
		
		assertEquals("8", expandedWt.trim());
	}
	
	@Test
	public void testMonthNameToNumberSwitchWith8() throws Exception
	{
		String wikitext = ""
				+ "{{#switch:8"
				+ "  |january|jan=1"
				+ "  |february|feb=2"
				+ "  |march|mar=3"
				+ "  |apr|april=4"
				+ "  |may=5"
				+ "  |june|jun=6"
				+ "  |july|jul=7"
				+ "  |august|aug=8"
				+ "  |september|sep|sept=9"
				+ "  |october|oct=10"
				+ "  |november|nov=11"
				+ "  |december|dec=12"
				+ "  |{{#ifexpr:8<0"
				+ "   |{{#ifexpr:((8)round 0)!=(8)"
				+ "    |{{#expr:12-(((0.5-(8))round 0)mod 12)}}"
				+ "    |{{#expr:12-(((11.5-(8))round 0)mod 12)}}"
				+ "   }}"
				+ "  |8}}"
				+ " }}";
		
		CompiledPage result = compiler.expand(pageId, wikitext, new ExpansionCallbackDummy());
		
		String expandedWt = RtDataPrinter.print(result.getPage());
		
		assertEquals("8", expandedWt.trim());
	}
	
	private final class ExpansionCallbackDummy
			implements
				ExpansionCallback
	{
		@Override
		public FullPage retrieveWikitext(
				ExpansionFrame expansionFrame,
				PageTitle pageTitle) throws Exception
		{
			return null;
		}
		
		@Override
		public String fileUrl(PageTitle pageTitle, int width, int height) throws Exception
		{
			return null;
		}
	}
}
