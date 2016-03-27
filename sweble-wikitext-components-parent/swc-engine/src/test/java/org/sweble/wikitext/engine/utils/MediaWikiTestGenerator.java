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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import de.fau.cs.osr.utils.TestResourcesFixture;

public class MediaWikiTestGenerator
{
	/**
	 * Generates a list of tuples (<code>String testName</code>,
	 * <code>TestDesc test</code>, <code>Map&lt;String,
	 * String> articles</code>).
	 */
	public static List<Object[]> enumerateInputs(
			TestResourcesFixture resources,
			List<File> testCollectionFiles) throws Exception
	{
		List<TestDesc> tests = new ArrayList<TestDesc>();

		Map<String, String> articles = new HashMap<String, String>();

		for (File inputFile : testCollectionFiles)
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

			inputs.add(new Object[] { name, resources, test, articles });
		}

		return inputs;
	}

	// =========================================================================

	private static void readTestcases(
			File inputFile,
			List<TestDesc> tests,
			Map<String, String> articles) throws IOException
	{
		String src = FileUtils.readFileToString(inputFile, "UTF-8");

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

	public static final class TestDesc
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
