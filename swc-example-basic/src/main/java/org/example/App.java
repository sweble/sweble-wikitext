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

package org.example;

import java.io.File;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.sweble.wikitext.engine.CompiledPage;
import org.sweble.wikitext.engine.Compiler;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.utils.DefaultConfigEn;

public class App
{
	public static void main(String[] args) throws Exception
	{
		if (args.length < 1)
		{
			System.err.println("Usage: java -jar swc-example-basic-VERSION.jar [--html|--text] TITLE");
			System.err.println();
			System.err.println("  The program will look for a file called `TITLE.wikitext',");
			System.err.println("  parse the file and write an HTML version to `TITLE.html'.");
			return;
		}
		
		boolean renderHtml = true;
		
		int i = 0;
		if (args[i].equalsIgnoreCase("--html"))
		{
			renderHtml = true;
			++i;
		}
		else if (args[i].equalsIgnoreCase("--text"))
		{
			renderHtml = false;
			++i;
		}
		
		String fileTitle = args[i];
		
		String html = run(
				new File(fileTitle + ".wikitext"),
				fileTitle,
				renderHtml);
		
		FileUtils.writeStringToFile(
				new File(fileTitle + (renderHtml ? ".html" : ".text")),
				html);
	}
	
	static String run(File file, String fileTitle, boolean renderHtml) throws Exception
	{
		// Set-up a simple wiki configuration
		WikiConfig config = DefaultConfigEn.generate();
		
		final int wrapCol = 80;
		
		// Instantiate a compiler for wiki pages
		Compiler compiler = new Compiler(config);
		
		// Retrieve a page
		PageTitle pageTitle = PageTitle.make(config, fileTitle);
		
		PageId pageId = new PageId(pageTitle, -1);
		
		String wikitext = FileUtils.readFileToString(file);
		
		// Compile the retrieved page
		CompiledPage cp = compiler.postprocess(pageId, wikitext, null);
		
		// Render the compiled page as HTML
		StringWriter w = new StringWriter();
		
		if (renderHtml)
		{
			/*
			HtmlPrinter p = new HtmlPrinter(w, pageTitle.getDenormalizedFullTitle());
			p.setCssResource("/org/sweble/wikitext/engine/utils/HtmlPrinter.css", "");
			p.setStandaloneHtml(true, "");
			p.go(cp.getPage());
			return w.toString();
			*/
			// FIXME:
			throw new UnsupportedOperationException("Rendering to HTML is currently not supported :(");
		}
		else
		{
			TextConverter p = new TextConverter(config, wrapCol);
			return (String) p.go(cp.getPage());
		}
	}
}
