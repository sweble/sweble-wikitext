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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.sweble.wikitext.engine.CompiledPage;
import org.sweble.wikitext.engine.Compiler;
import org.sweble.wikitext.engine.CompilerException;
import org.sweble.wikitext.engine.ExpansionCallback;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.FullPage;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.config.Namespace;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.utils.DefaultConfigEn;
import org.sweble.wikitext.parser.LinkTargetException;

public class CompilerTestBase
{
	private final Compiler compiler;
	
	private final WikiConfigImpl config;
	
	private final ExpansionCallbackImpl expansionCallback;
	
	private final List<String> contentDirs = Arrays.asList(
			"/content-freeform",
			"/content-enwp");
	
	// =========================================================================
	
	public CompilerTestBase() throws FileNotFoundException, JAXBException
	{
		this.config = DefaultConfigEn.generate();
		
		this.compiler = new Compiler(config);
		
		this.expansionCallback = new ExpansionCallbackImpl();
	}
	
	// =========================================================================
	
	public WikiConfigImpl getConfig()
	{
		return config;
	}
	
	public Compiler getCompiler()
	{
		return compiler;
	}
	
	public ExpansionCallbackImpl getExpansionCallback()
	{
		return expansionCallback;
	}
	
	// =========================================================================
	
	public CompiledPage preprocess(String page, boolean forInclusion) throws LinkTargetException, IOException, CompilerException
	{
		return preprocess(null, page, forInclusion);
	}
	
	public CompiledPage preprocess(
			String namespace,
			String page,
			boolean forInclusion) throws LinkTargetException, IOException, CompilerException
	{
		Namespace ns = null;
		if (namespace != null)
			ns = config.getNamespace(namespace);
		
		PageTitle title = PageTitle.make(config, page, ns);
		FullPage fullPage = retrieve(title);
		if (fullPage == null)
			return null;
		return compiler.preprocess(
				fullPage.getId(),
				fullPage.getText(),
				forInclusion,
				expansionCallback);
	}
	
	public CompiledPage expand(String page, boolean forInclusion) throws LinkTargetException, IOException, CompilerException
	{
		PageTitle title = PageTitle.make(config, page);
		FullPage fullPage = retrieve(title);
		if (fullPage == null)
			return null;
		return compiler.expand(
				fullPage.getId(),
				fullPage.getText(),
				expansionCallback);
	}
	
	public CompiledPage parse(String page, boolean forInclusion) throws LinkTargetException, IOException, CompilerException
	{
		PageTitle title = PageTitle.make(config, page);
		FullPage fullPage = retrieve(title);
		if (fullPage == null)
			return null;
		return compiler.parse(
				fullPage.getId(),
				fullPage.getText(),
				expansionCallback);
	}
	
	public CompiledPage postprocess(String page, boolean forInclusion) throws LinkTargetException, IOException, CompilerException
	{
		PageTitle title = PageTitle.make(config, page);
		FullPage fullPage = retrieve(title);
		if (fullPage == null)
			return null;
		return compiler.postprocess(
				fullPage.getId(),
				fullPage.getText(),
				expansionCallback);
	}
	
	public FullPage retrieve(PageTitle pageTitle) throws IOException
	{
		PageId pageId = new PageId(pageTitle, -1);
		
		String fullTitle = pageId.getTitle().getDenormalizedFullTitle();
		String encFullTitle = URLEncoder.encode(fullTitle, "UTF-8");
		
		String wikitext = null;
		for (String dir : contentDirs)
		{
			String path = dir + "/" + encFullTitle + ".wikitext";
			InputStream resource = getClass().getResourceAsStream(path);
			if (resource == null)
				continue;
			
			wikitext = IOUtils.toString(resource);
		}
		
		if (wikitext == null)
		{
			System.err.println("Cannot find resource: " + fullTitle);
			return null;
		}
		
		return new FullPage(pageId, wikitext);
	}
	
	// =========================================================================
	
	private final class ExpansionCallbackImpl
			implements
				ExpansionCallback
	{
		@Override
		public FullPage retrieveWikitext(
				ExpansionFrame expansionFrame,
				PageTitle pageTitle) throws Exception
		{
			return retrieve(pageTitle);
		}
		
		@Override
		public String fileUrl(PageTitle pageTitle, int width, int height) throws Exception
		{
			return null;
		}
	}
}
