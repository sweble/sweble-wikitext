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

package ru.denplusplus.wikipedia.dumpcruncher;

import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.articlecruncher.Processor;
import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.parser.parser.LinkTargetException;

public class RevisionProcessor
		implements
			Processor
{
	private final DumpCruncher dumpCruncher;
	
	public RevisionProcessor(DumpCruncher dumpCruncher)
	{
		this.dumpCruncher = dumpCruncher;
	}
	
	@Override
	public Object process(Job job) throws LinkTargetException, EngineException
	{
		RevisionJob revJob = (RevisionJob) job;
		
		WikiConfig config = dumpCruncher.getWikiConfig();
		
		// Instantiate a compiler for wiki pages
		WtEngineImpl engine = new WtEngineImpl(config);
		
		// Retrieve a page
		PageTitle pageTitle = PageTitle.make(config, revJob.getPageTitle());
		
		PageId pageId = new PageId(pageTitle, revJob.getId().longValue());
		
		String wikitext = revJob.getTextText();
		
		// Compile the retrieved page
		EngProcessedPage cp = engine.postprocess(pageId, wikitext, null);
		
		return cp;
	}
}
