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

package org.sweble.wikitext.example;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.output.HtmlRenderer;
import org.sweble.wikitext.engine.output.HtmlRendererCallback;
import org.sweble.wikitext.engine.output.MediaInfo;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;
import org.sweble.wikitext.engine.utils.UrlEncoding;
import org.sweble.wikitext.parser.nodes.WtUrl;
import org.sweble.wikitext.parser.parser.LinkTargetException;

import de.fau.cs.osr.utils.StringTools;

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

		if (renderHtml)
		{
			String ourHtml = HtmlRenderer.print(new MyRendererCallback(), config, pageTitle, cp.getPage());

			String template = IOUtils.toString(App.class.getResourceAsStream("/render-template.html"), "UTF8");

			String html = template;
			html = html.replace("{$TITLE}", StringTools.escHtml(pageTitle.getDenormalizedFullTitle()));
			html = html.replace("{$CONTENT}", ourHtml);

			return html;
		}
		else
		{
			TextConverter p = new TextConverter(config, wrapCol);
			return (String) p.go(cp.getPage());
		}
	}

	private static final class MyRendererCallback
			implements
				HtmlRendererCallback
	{
		protected static final String LOCAL_URL = "";

		@Override
		public boolean resourceExists(PageTitle target)
		{
			// TODO: Add proper check
			return false;
		}

		@Override
		public MediaInfo getMediaInfo(String title, int width, int height)
		{
			// TODO: Return proper media info
			return null;
		}

		@Override
		public String makeUrl(PageTitle target)
		{
			String page = UrlEncoding.WIKI.encode(target.getNormalizedFullTitle());
			String f = target.getFragment();
			String url = page;
			if (f != null && !f.isEmpty())
				url = page + "#" + UrlEncoding.WIKI.encode(f);
			return LOCAL_URL + "/" + url;
		}

		@Override
		public String makeUrl(WtUrl target)
		{
			if (target.getProtocol() == "")
				return target.getPath();
			return target.getProtocol() + ":" + target.getPath();
		}

		@Override
		public String makeUrlMissingTarget(String path)
		{
			return LOCAL_URL + "?title=" + path + "&amp;action=edit&amp;redlink=1";

		}
	}
}
