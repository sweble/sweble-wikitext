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

package org.sweble.wikitext.engine;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wikitext.engine.utils.CompilerTestBase;

import de.fau.cs.osr.ptk.common.test.TestResourcesFixture;
import de.fau.cs.osr.utils.NamedParametrized;
import de.fau.cs.osr.utils.WrappedException;

@RunWith(value = NamedParametrized.class)
public class MediaWikiTestRunner
		extends
			CompilerTestBase
{
	private final String name;
	
	private final TestDesc test;
	
	private final Map<String, String> articles;
	
	// =========================================================================
	
	public MediaWikiTestRunner(
			String name,
			TestDesc test,
			Map<String, String> articles) throws FileNotFoundException, JAXBException
	{
		name = name.replaceAll("[^A-Za-z0-9 -]*", "_");
		
		this.name = name;
		this.test = test;
		this.articles = articles;
	}
	
	// =========================================================================
	
	@Test
	public void test() throws Exception
	{
		throw new InternalError();
		/*
		PageTitle title = PageTitle.make(getConfig(), name);
		PageId pageId = new PageId(title, -1);
		String wikitext = test.getInput();
		
		CompiledPage cpage = getCompiler().postprocess(
				pageId,
				wikitext,
				new OurExpansionCallback());
		
		StringWriter sw = new StringWriter();
		HtmlPrinter p = new HtmlPrinter(sw, name);
		p.setStandaloneHtml(false, "");
		p.go(cpage.getPage().get(0));
		String actual = sw.toString();
		String expected = test.getResult();
		
		actual = clean(actual);
		expected = clean(expected);
		
		assertEquals(expected, actual);
		*/
	}
	
	private String clean(String html)
	{
		Document doc = Jsoup.parse(html);
		doc.outputSettings().charset("UTF-8");
		doc.outputSettings().indentAmount(2);
		doc.outputSettings().prettyPrint(true);
		html = doc.body().html();
		return html;
	}
	
	// =========================================================================
	
	private final class OurExpansionCallback
			implements
				ExpansionCallback
	{
		@Override
		public FullPage retrieveWikitext(
				ExpansionFrame expansionFrame,
				PageTitle pageTitle) throws Exception
		{
			PageId pageId = new PageId(pageTitle, -1);
			return new FullPage(pageId, articles.get(pageTitle.getDenormalizedFullTitle()));
		}
		
		@Override
		public String fileUrl(PageTitle pageTitle, int width, int height) throws Exception
		{
			return null;
		}
	}
	
	// =========================================================================
	
	private static final TestResourcesFixture resources;
	
	// =========================================================================
	
	static
	{
		URL url = MediaWikiTestRunner.class.getResource("/");
		
		try
		{
			resources = new TestResourcesFixture(new File(url.getFile()));
		}
		catch (FileNotFoundException e)
		{
			throw new WrappedException(e);
		}
	}
	
	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		String extension = ".mwtest";
		List<File> collections =
				resources.gather("/mediawiki", ".*" + Pattern.quote(extension), true);
		
		List<TestDesc> tests = new ArrayList<TestDesc>();
		
		Map<String, String> articles = new HashMap<String, String>();
		
		for (File inputFile : collections)
			readTestcases(inputFile, tests, articles);
		
		assertFalse(tests.isEmpty());
		
		List<Object[]> inputs = new ArrayList<Object[]>(tests.size());
		for (TestDesc test : tests)
		{
			String name = String.format(
					"%s - %s",
					test.getInputFile().getName(),
					test.getName());
			
			int eol1 = name.indexOf('\n');
			int eol2 = name.indexOf('\r');
			int par = name.indexOf('(');
			int i = name.length();
			if (par != -1 && par < i)
				i = par;
			if (eol1 != -1 && eol1 < i)
				i = eol1;
			if (eol2 != -1 && eol2 < i)
				i = eol2;
			if (i != -1)
				name = name.substring(0, i);
			
			inputs.add(new Object[] { name, test, articles });
		}
		
		return inputs;
	}
	
	// =========================================================================
	
	private static void readTestcases(
			File inputFile,
			List<TestDesc> tests,
			Map<String, String> articles) throws IOException
	{
		String src = FileUtils.readFileToString(inputFile);
		
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
								articles.put(article, text);
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
					tests.add(new TestDesc(inputFile, test, options, input, config, result));
					i = m.end();
					continue;
				}
			}
			
			StringWriter sw = new StringWriter();
			PrintWriter w = new PrintWriter(sw);
			w.println("Syntax error @ " + m.start());
			w.println("Extract:");
			int end = m.end() + 128;
			if (end > src.length())
				end = src.length();
			w.println(src.substring(m.start(), end));
			w.println();
			fail(sw.toString());
			
			i = m.end();
		}
	}
	
	// =========================================================================
	
	@SuppressWarnings("unused")
	private static final class TestDesc
	{
		private final File inputFile;
		
		private final String name;
		
		private final String options;
		
		private final String input;
		
		private final String config;
		
		private final String result;
		
		// =====================================================================
		
		public TestDesc(
				File inputFile,
				String name,
				String options,
				String input,
				String config,
				String result)
		{
			this.inputFile = inputFile;
			this.name = name;
			this.options = options;
			this.input = input;
			this.config = config;
			this.result = result;
		}
		
		// =====================================================================
		
		public File getInputFile()
		{
			return inputFile;
		}
		
		public String getName()
		{
			return name;
		}
		
		public String getOptions()
		{
			return options;
		}
		
		public String getInput()
		{
			return input;
		}
		
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
