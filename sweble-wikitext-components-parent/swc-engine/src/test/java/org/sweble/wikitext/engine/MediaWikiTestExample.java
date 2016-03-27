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

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wikitext.engine.utils.EngineIntegrationTestBase;
import org.sweble.wikitext.engine.utils.MediaWikiTestGenerator;
import org.sweble.wikitext.engine.utils.MediaWikiTestGenerator.TestDesc;

import de.fau.cs.osr.utils.NamedParametrized;
import de.fau.cs.osr.utils.TestResourcesFixture;

@RunWith(value = NamedParametrized.class)
public class MediaWikiTestExample
		extends
			EngineIntegrationTestBase
{
	private static final String FILTER_RX = ".*?\\.mwtest";

	private static final String INPUT_SUB_DIR = "mediawiki";

	// =========================================================================

	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		TestResourcesFixture resources = getTestResourcesFixture();

		List<File> testCollectionFiles =
				resources.gather(INPUT_SUB_DIR, FILTER_RX, false);

		return MediaWikiTestGenerator.enumerateInputs(resources, testCollectionFiles);
	}

	// =========================================================================

	@SuppressWarnings("unused")
	private final String name;

	@SuppressWarnings("unused")
	private final TestDesc test;

	@SuppressWarnings("unused")
	private final Map<String, String> articles;

	// =========================================================================

	public MediaWikiTestExample(
			String name,
			TestResourcesFixture resources,
			TestDesc test,
			Map<String, String> articles)
	{
		super(resources);
		this.name = name;
		this.test = test;
		this.articles = articles;
	}

	@Test
	@Ignore
	public void testAstAfterPostprocessingMatchesReference() throws Exception
	{
	}
}
