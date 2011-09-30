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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.regex.Pattern;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.sweble.wikitext.lazy.postprocessor.AstCompressor;
import org.sweble.wikitext.lazy.utils.RtWikitextPrinter;
import org.sweble.wikitext.lazy.utils.WikitextPrinter;

import xtc.parser.ParseException;
import de.fau.cs.osr.ptk.common.AstPrinterInterface;
import de.fau.cs.osr.ptk.common.Visitor;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.test.ParserTestCommon;
import de.fau.cs.osr.ptk.common.test.ParserTestResources;

public class FullTest
{
	private static ParserTestResources resources;
	
	private static ParserTestCommon common;
	
	private static final String SC = File.separator;
	
	private static final String QSC = SC.replace("\\", "\\\\");//Pattern.quote(SC);
	
	@BeforeClass
	public static void setUp()
	{
		URL url = FullTest.class.getResource("/");
		assertTrue(url != null);
		
		resources = new ParserTestResources(new File(url.getFile()));
		
		common = new ParserTestCommon(
		        resources,
		        FullParser.class,
		        "(.*?)" + QSC + "target" + QSC + "test-classes" + QSC,
		        "$1" + QSC + "src" + QSC + "test" + QSC + "resources" + QSC,
		        false);
	}
	
	// ==[ Basic Tests ]========================================================
	
	@Test
	public void testBasicAst() throws IOException, ParseException
	{
		common.gatherParseAndPrintTest(
		        "basic" + SC + "wikitext" + SC,
		        "basic" + SC + "ast" + SC,
		        new Visitor[] { new AstCompressor() },
		        new AstPrinter());
	}
	
	@Test
	public void testBasicRtWikitextPrinter() throws IOException, ParseException
	{
		common.gatherParseAndPrintTest(
		        "basic" + SC + "wikitext" + SC,
		        "basic" + SC + "wikitext" + SC,
		        new Visitor[] { new AstCompressor() },
		        new RtWikitextAstPrinter());
	}
	
	// FIXME: Make this test work!
	@Test
	@Ignore
	public void testBasicWikitextPrinter() throws IOException, ParseException
	{
		common.gatherParseAndPrintTest(
		        "basic" + SC + "wikitext" + SC,
		        "basic" + SC + "wikitextprinter" + SC,
		        new Visitor[] { new AstCompressor() },
		        new WikitextAstPrinter());
	}
	
	// ==[ Regression Tests ]===================================================
	
	@Test
	public void testRegression() throws IOException, ParseException
	{
		common.gatherParseAndPrintTest(
		        "regression" + SC + "wikitext" + SC,
		        "regression" + SC + "ast" + SC,
		        new Visitor[] { new AstCompressor() },
		        new AstPrinter());
	}
	
	// ==[ Complex Regression Tests ]===========================================
	
	@Test
	public void testComplexRegression() throws IOException, ParseException
	{
		common.gatherParseAndPrintTest(
		        "complex" + SC + "wikitext" + SC,
		        "complex" + SC + "ast" + SC,
		        new Visitor[] { new AstCompressor() },
		        new AstPrinter());
	}
	
	// ==[ ScopedElementBuilder Tests ]=========================================
	
	@Test
	public void testScopes() throws IOException, ParseException
	{
		common.gatherParseAndPrintTest(
		        "scopes" + SC + "wikitext" + SC,
		        "scopes" + SC + "ast" + SC,
		        new Visitor[] { new AstCompressor() },
		        new AstPrinter());
	}
	
	// =========================================================================
	
	public static final class AstPrinter
	        implements
	            AstPrinterInterface
	{
		@Override
		public String getPrintoutType()
		{
			return "ast";
		}
		
		@Override
		public void print(AstNode ast, Writer out) throws IOException
		{
			org.sweble.wikitext.lazy.utils.AstPrinter printer =
			        new org.sweble.wikitext.lazy.utils.AstPrinter(out);
			
			printer.setLegacyIndentation(true);
			printer.go(ast);
		}
	}
	
	public static final class WikitextAstPrinter
	        implements
	            AstPrinterInterface
	{
		@Override
		public String getPrintoutType()
		{
			return "wikitext";
		}
		
		@Override
		public void print(AstNode ast, Writer out) throws IOException
		{
			WikitextPrinter.print(out, ast);
		}
	}
	
	public static final class RtWikitextAstPrinter
	        implements
	            AstPrinterInterface
	{
		@Override
		public String getPrintoutType()
		{
			return "wikitext";
		}
		
		@Override
		public void print(AstNode ast, Writer out) throws IOException
		{
			RtWikitextPrinter.print(out, ast);
		}
	}
}
