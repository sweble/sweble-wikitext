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

package org.sweble.wikitext.dumpreader.processors;

import java.math.BigInteger;
import java.util.List;

import org.sweble.wikitext.dumpreader.CompletedJob;
import org.sweble.wikitext.dumpreader.CompletedJobState;
import org.sweble.wikitext.dumpreader.Job;
import org.sweble.wikitext.dumpreader.Result;
import org.sweble.wikitext.dumpreader.xjc.PageType;
import org.sweble.wikitext.dumpreader.xjc.RevisionType;
import org.sweble.wikitext.engine.CompiledPage;
import org.sweble.wikitext.engine.Compiler;
import org.sweble.wikitext.engine.FullPage;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.config.WikiConfigurationInterface;

public class ParserAndAnalyzer
{
	private final WikiConfigurationInterface config;
	
	private Compiler compiler;
	
	// =========================================================================
	
	public ParserAndAnalyzer(WikiConfigurationInterface config) throws Exception
	{
		this.config = config;
		
		this.compiler = new Compiler(config);
	}
	
	public CompletedJob process(Job job) throws Exception
	{
		PageType pageType = job.getPage();
		
		BigInteger pageId = pageType.getId();
		
		String pageTitle = pageType.getTitle();
		
		List<Object> ruls = pageType.getRevisionOrUploadOrLogitem();
		// TODO: Parse single revisions instead!
		for (Object rulItem : ruls)
		{
			if (rulItem instanceof RevisionType)
			{
				RevisionType revisionType = (RevisionType) rulItem;
				
				String revText = revisionType.getText().getValue();
				
				BigInteger revId = revisionType.getId();
				
				try
				{
					CompiledPage page = postprocess(pageId, pageTitle, revText);
					
					String stats = NodeStatsVisitor.run(page.getPage());
					
					/*
					writer.tell(new LogActor.Success(
							pageId.longValue(),
							pageTitle,
							revId.longValue(),
							stats));
					*/
					
					return new CompletedJob(job, new Result(page));
				}
				catch (Exception e)
				{
					/*
					writer.tell(new LogActor.Failure(
							e,
							pageId.longValue(),
							pageTitle,
							revId.longValue(),
							revText));
					*/
					
					return new CompletedJob(
							CompletedJobState.PROCESSING_FAILED,
							job,
							new Result(e));
				}
			}
		}
		
		return new CompletedJob(
				CompletedJobState.EMPTY_JOB,
				job,
				null);
	}
	
	private CompiledPage postprocess(
			BigInteger pageId,
			String pageTitle,
			String text) throws Exception
	{
		PageTitle title = PageTitle.make(config, pageTitle);
		
		FullPage fullPage = new FullPage(
				new PageId(title, pageId.longValue()),
				text);
		
		return compiler.postprocess(
				fullPage.getId(),
				fullPage.getText(),
				null);
	}
}
