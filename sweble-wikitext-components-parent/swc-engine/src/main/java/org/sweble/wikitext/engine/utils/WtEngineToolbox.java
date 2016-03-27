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

import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.engine.ExpansionCallback;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.FullPage;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.parser.LinkTargetException;

import de.fau.cs.osr.ptk.common.AstPrinter;

/**
 * Starter-Kit for those who want to play with the Engine.
 */
public class WtEngineToolbox
{
	private WikiConfigImpl wikiConfig;

	private WtEngineImpl engine;

	// =========================================================================

	public WtEngineToolbox()
	{
		restartEngine(DefaultConfigEnWp.generate());
	}

	// =========================================================================

	public void restartEngine(WikiConfigImpl wikiConfig)
	{
		this.wikiConfig = wikiConfig;
		this.engine = new WtEngineImpl(wikiConfig);
	}

	public WikiConfigImpl getWikiConfig()
	{
		return wikiConfig;
	}

	public WtEngineImpl getEngine()
	{
		return engine;
	}

	// =========================================================================
	//  AST helpers
	// =========================================================================

	public PageId makePageId(String title) throws LinkTargetException
	{
		return makePageId(wikiConfig, title);
	}

	public PageId makePageId(String title, long id) throws LinkTargetException
	{
		return makePageId(wikiConfig, title, id);
	}

	public static PageId makePageId(WikiConfig wikiConfig, String title) throws LinkTargetException
	{
		return makePageId(wikiConfig, title, -1);
	}

	public static PageId makePageId(WikiConfig wikiConfig, String title, long id) throws LinkTargetException
	{
		PageTitle pageTitle = PageTitle.make(wikiConfig, title);
		return new PageId(pageTitle, id);
	}

	public EngProcessedPage wmToAst(PageId pageId, String wikitext) throws EngineException
	{
		return wmToAst(pageId, wikitext, new TestExpansionCallback());
	}

	public EngProcessedPage wmToAst(
			PageId pageId,
			String wikitext,
			ExpansionCallback callback) throws EngineException
	{
		return engine.postprocess(pageId, wikitext, callback);
	}

	public static String printAst(EngProcessedPage ast)
	{
		return AstPrinter.print((WtNode) ast.getPage());
	}

	// =========================================================================

	public static final class TestExpansionCallback
			implements
				ExpansionCallback
	{
		@Override
		public FullPage retrieveWikitext(
				ExpansionFrame expansionFrame,
				PageTitle pageTitle)
		{
			return null;
		}

		@Override
		public String fileUrl(PageTitle pageTitle, int width, int height)
		{
			return null;
		}
	}
}
