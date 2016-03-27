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
package org.sweble.wikitext.engine.output;

import java.io.File;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wikitext.engine.ExpansionCallback;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.EngineIntegrationTestBase;
import org.sweble.wikitext.engine.utils.UrlEncoding;
import org.sweble.wikitext.parser.nodes.WtUrl;

import de.fau.cs.osr.utils.FileCompare;
import de.fau.cs.osr.utils.FileContent;
import de.fau.cs.osr.utils.NamedParametrized;
import de.fau.cs.osr.utils.TestResourcesFixture;

@RunWith(value = NamedParametrized.class)
public class HtmlRendererTest
		extends
			EngineIntegrationTestBase
{
	private static final String FILTER_RX = ".*?\\.wikitext";

	private static final String INPUT_SUB_DIR = "engine/output/wikitext";

	private static final String EXPECTED_AST_SUB_DIR = "engine/output/rendered";

	// =========================================================================

	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		TestResourcesFixture resources = getTestResourcesFixture();
		return resources.gatherAsParameters(INPUT_SUB_DIR, FILTER_RX, false);
	}

	// =========================================================================

	private final File inputFile;

	// =========================================================================

	public HtmlRendererTest(
			String title,
			TestResourcesFixture resources,
			File inputFile)
	{
		super(resources);
		this.inputFile = inputFile;
	}

	// =========================================================================

	@Test
	@Ignore
	public void testRenderHtml() throws Exception
	{
		FileContent inputFileContent = new FileContent(inputFile);

		WikiConfig wikiConfig = getConfig();
		PageTitle pageTitle = PageTitle.make(wikiConfig, inputFile.getName());
		PageId pageId = new PageId(pageTitle, -1);
		String wikitext = inputFileContent.getContent();
		ExpansionCallback expCallback = null;

		EngProcessedPage ast = getEngine().postprocess(pageId, wikitext, expCallback);

		TestCallback rendererCallback = new TestCallback();

		String actual = HtmlRenderer.print(rendererCallback, wikiConfig, pageTitle, ast);

		File expectedFile = TestResourcesFixture.rebase(
				inputFile,
				INPUT_SUB_DIR,
				EXPECTED_AST_SUB_DIR,
				"html",
				true /* don't throw if file doesn't exist */);

		FileCompare cmp = new FileCompare(getResources());
		cmp.compareWithExpectedOrGenerateExpectedFromActual(expectedFile, actual);
	}

	// =========================================================================

	private static final class TestCallback
			implements
				HtmlRendererCallback
	{
		protected static final String LOCAL_URL = "/mediawiki/index.php/";

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
