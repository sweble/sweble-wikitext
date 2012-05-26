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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.sweble.wikitext.lazy.postprocessor.AstCompressor;

import xtc.parser.ParseException;
import de.fau.cs.osr.ptk.common.AstPrinterInterface;
import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ParserInterface;
import de.fau.cs.osr.ptk.common.ast.AstNode;

public class MediaWikiTest
{
	private static final List<Test> tests = new ArrayList<Test>();
	
	private static final List<Article> articles = new ArrayList<Article>();
	
	@BeforeClass
	public static void setUp() throws IOException
	{
		readTestcases("/mediawiki/parserTests.txt");
		readTestcases("/mediawiki/extraParserTests.txt");
	}
	
	@Ignore
	@org.junit.Test
	public void testWhiteboxAst() throws IOException, ParseException
	{
		parseAndPrintTest(
				new AstVisitor[] { new AstCompressor() },
				new FullTest.AstPrinter(),
				false);
	}
	
	@Ignore
	@org.junit.Test
	public void testWhiteboxRtWikitextPrinter() throws IOException, ParseException
	{
		parseAndPrintTest(
				new AstVisitor[] { new AstCompressor() },
				new FullTest.RtWikitextAstPrinter(),
				true);
	}
	
	// =========================================================================
	
	public void parseAndPrintTest(
			AstVisitor[] visitors,
			AstPrinterInterface printer,
			boolean cmpInput) throws IOException, ParseException
	{
		System.out.println();
		System.out.println("Parser & Print test:");
		System.out.println("  Printer:    " + printer.getClass().getSimpleName());
		System.out.println();
		
		for (Test test : tests)
		{
			
			System.out.println("Testing: " + test.getName());
			
			ParserInterface p = new FullParser();
			p.addVisitors(Arrays.asList(visitors));
			
			AstNode r = p.parseArticle(test.getInput(), test.getName());
			
			StringWriter writer = new StringWriter();
			printer.print(r, writer);
			String result = writer.toString();
			
			if (cmpInput)
			{
				Assert.assertEquals(test.getInput(), result);
			}
			else
			{
				Assert.assertEquals(test.getResult(), result);
			}
		}
		
		System.out.println();
	}
	
	private static void readTestcases(String file) throws IOException
	{
		int asize = articles.size();
		int tsize = tests.size();
		System.out.println("Reading test cases: " + file);
		
		InputStream s =
				MediaWikiTest.class.getResourceAsStream(file);
		
		String src = IOUtils.toString(s);
		
		Pattern p = Pattern.compile(
				"(?m)^[ \t]*!![ \t]*(\\w+):?[ \t]*(?:\r\n?|\n)");
		
		Matcher m = p.matcher(src);
		
		int i = 0;
		while (m.find(i))
		{
			String kw = m.group(1);
			if (kw.equals("article"))
			{
				int aStart = m.end();
				if (m.find(aStart))
				{
					kw = m.group(1);
					if (kw.equals("text"))
					{
						int aEnd = m.start();
						int tStart = m.end();
						String article = src.substring(aStart, aEnd).trim();
						if (m.find(tStart))
						{
							kw = m.group(1);
							if (kw.equals("endarticle"))
							{
								int tEnd = m.start();
								String text = src.substring(tStart, tEnd);
								articles.add(new Article(article, text));
								i = m.end();
								continue;
							}
						}
					}
				}
			}
			else if (kw.equals("test"))
			{
				String last = "test";
				int start = m.end();
				
				String test = null;
				String options = null;
				String config = null;
				String input = null;
				String result = null;
				boolean ok = false;
				
				while (m.find(start))
				{
					String str = src.substring(start, m.start());
					if ("test".equals(last))
					{
						if (test != null)
							break;
						test = str.trim();
					}
					else if ("options".equals(last))
					{
						if (options != null)
							break;
						options = str.trim();
					}
					else if ("config".equals(last))
					{
						if (config != null)
							break;
						config = str.trim();
					}
					else if ("input".equals(last))
					{
						if (input != null)
							break;
						input = str;
					}
					else if ("result".equals(last))
					{
						if (result != null)
							break;
						result = str;
					}
					
					last = m.group(1);
					if ("end".equals(last))
					{
						if (input != null && result != null)
						{
							ok = true;
							break;
						}
						else
							break;
					}
					else if (!"options,config,input,result".contains(last))
					{
						break;
					}
					
					start = m.end();
				}
				
				if (ok)
				{
					tests.add(new Test(test, options, input, config, result));
					i = m.end();
					continue;
				}
			}
			
			System.err.println("Syntax error @ " + m.start());
			System.err.println("Extract:");
			int end = m.end() + 128;
			if (end > src.length())
				end = src.length();
			System.err.println(src.substring(m.start(), end));
			System.err.println();
			
			i = m.end();
		}
		
		System.out.println("  Number of articles found: " + (articles.size() - asize));
		System.out.println("  Number of tests found: " + (tests.size() - tsize));
	}
	
	private static final class Article
	{
		private final String name;
		
		private final String text;
		
		public Article(String name, String text)
		{
			super();
			this.name = name;
			this.text = text;
		}
		
		@SuppressWarnings("unused")
		public String getName()
		{
			return name;
		}
		
		@SuppressWarnings("unused")
		public String getText()
		{
			return text;
		}
	}
	
	private static final class Test
	{
		private final String name;
		
		private final String options;
		
		private final String input;
		
		private final String config;
		
		private final String result;
		
		public Test(
				String name,
				String options,
				String input,
				String config,
				String result)
		{
			super();
			this.name = name;
			this.options = options;
			this.input = input;
			this.config = config;
			this.result = result;
		}
		
		public String getName()
		{
			return name;
		}
		
		@SuppressWarnings("unused")
		public String getOptions()
		{
			return options;
		}
		
		public String getInput()
		{
			return input;
		}
		
		@SuppressWarnings("unused")
		public String getConfig()
		{
			return config;
		}
		
		public String getResult()
		{
			return result;
		}
	}
}
