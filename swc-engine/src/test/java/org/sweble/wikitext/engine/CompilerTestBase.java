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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.sweble.wikitext.engine.config.Namespace;
import org.sweble.wikitext.engine.utils.SimpleWikiConfiguration;
import org.sweble.wikitext.lazy.LinkTargetException;

public class CompilerTestBase
{
	private final Compiler compiler;
	
	private final SimpleWikiConfiguration config;
	
	private final ExpansionCallbackImpl expansionCallback;
	
	private final List<String> contentDirs = Arrays.asList(
	        "/content-freeform",
	        "/content-enwp");
	
	// =========================================================================
	
	protected CompilerTestBase() throws FileNotFoundException, IOException
	{
		this.config = new SimpleWikiConfiguration(
		        "classpath:/org/sweble/wikitext/engine/SimpleWikiConfiguration.xml");
		
		this.compiler = new Compiler(config);
		
		this.expansionCallback = new ExpansionCallbackImpl();
	}
	
	protected CompiledPage preprocess(String page, boolean forInclusion) throws LinkTargetException, IOException, CompilerException
	{
		return preprocess(null, page, forInclusion);
	}
	
	protected CompiledPage preprocess(String namespace, String page, boolean forInclusion) throws LinkTargetException, IOException, CompilerException
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
	
	protected CompiledPage parse(String page, boolean forInclusion) throws LinkTargetException, IOException, CompilerException
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
	
	protected CompiledPage postprocess(String page, boolean forInclusion) throws LinkTargetException, IOException, CompilerException
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
	
	protected FullPage retrieve(PageTitle pageTitle) throws IOException
	{
		PageId pageId = new PageId(pageTitle, -1);
		
		String fullTitle = pageId.getTitle().getFullTitle();
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
		public FullPage retrieveWikitext(ExpansionFrame expansionFrame, PageTitle pageTitle) throws Exception
		{
			return retrieve(pageTitle);
		}
	}
}
