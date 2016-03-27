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
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wom3.Wom3Document;
import org.sweble.wom3.swcadapter.AstToWomConverter;

public class App
{
	public static void main(String[] args) throws IOException, LinkTargetException, EngineException
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
				html,
				Charset.defaultCharset().name());
	}

	static String run(File file, String fileTitle, boolean renderHtml) throws IOException, LinkTargetException, EngineException
	{
		// Set-up a simple wiki configuration
		WikiConfig config = DefaultConfigEnWp.generate();

		final int wrapCol = 80;

		// Instantiate a compiler for wiki pages
		WtEngineImpl engine = new WtEngineImpl(config);

		// Retrieve a page
		PageTitle pageTitle = PageTitle.make(config, fileTitle);

		PageId pageId = new PageId(pageTitle, -1);

		String wikitext = FileUtils.readFileToString(file, Charset.defaultCharset().name());

		// Compile the retrieved page
		EngProcessedPage cp = engine.postprocess(pageId, wikitext, null);

		// Convert the AST to a WOM document
		Wom3Document womDoc = AstToWomConverter.convert(
				config.getParserConfig(),
				null /*pageNamespace*/,
				null /*pagePath*/,
				pageId.getTitle().getTitle(),
				"Mr. Tester",
				DateTime.parse("2012-12-07T12:15:30.000+01:00"),
				cp.getPage());

		if (renderHtml)
		{
			throw new UnsupportedOperationException(
					"HTML rendering is not yet supported!");

			/*
			String ourHtml = HtmlRenderer.print(new MyRendererCallback(), config, pageTitle, cp.getPage());
			
			String template = IOUtils.toString(App.class.getResourceAsStream("/render-template.html"), "UTF8");
			
			String html = template;
			html = html.replace("{$TITLE}", StringUtils.escHtml(pageTitle.getDenormalizedFullTitle()));
			html = html.replace("{$CONTENT}", ourHtml);
			
			return html;
			*/
		}
		else
		{
			TextConverter p = new TextConverter(config, wrapCol);
			return (String) p.go(womDoc);
		}
	}

	/*
	private static final class MyRendererCallback
			implements
				HtmlRendererCallback
	{
		@Override
		public boolean resourceExists(PageTitle target)
		{
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public MediaInfo getMediaInfo(
				String title,
				int width,
				int height)
		{
			// TODO Auto-generated method stub
			return null;
		}
	}
	*/
}
