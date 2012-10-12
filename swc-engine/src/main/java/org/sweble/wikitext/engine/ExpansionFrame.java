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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.lognodes.LogContainer;
import org.sweble.wikitext.engine.utils.UrlService;
import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtValue;

import de.fau.cs.osr.ptk.common.Warning;

public class ExpansionFrame
{
	private final WtEngine compiler;
	
	private final ExpansionFrame rootFrame;
	
	private final ExpansionFrame parentFrame;
	
	private final PageTitle title;
	
	private final Map<String, WtValue> arguments;
	
	private final boolean forInclusion;
	
	private final LogContainer frameLog;
	
	private final ExpansionCallback callback;
	
	private final List<Warning> warnings;
	
	private final WtEntityMap entityMap;
	
	private final boolean noRedirect;
	
	private ExpansionVisitor expansionVisitor;
	
	// FIXME: That should have been initialized from a request!
	//        And only once for the whole expansion process!
	private UrlService urlService = new UrlService();
	
	// =========================================================================
	
	public ExpansionFrame(
			WtEngine compiler,
			ExpansionCallback callback,
			ExpansionDebugHooks hooks,
			PageTitle title,
			WtEntityMap entityMap,
			boolean noRedirect,
			List<Warning> warnings,
			LogContainer frameLog,
			boolean timingEnabled,
			boolean catchAll)
	{
		this.compiler = compiler;
		this.callback = callback;
		this.title = title;
		this.entityMap = entityMap;
		this.arguments = new HashMap<String, WtValue>();
		this.forInclusion = false;
		this.noRedirect = noRedirect;
		this.warnings = warnings;
		this.frameLog = frameLog;
		this.rootFrame = this;
		this.parentFrame = null;
		
		expansionVisitor = new ExpansionVisitor(
				this,
				frameLog,
				hooks,
				timingEnabled,
				catchAll);
	}
	
	public ExpansionFrame(
			WtEngine compiler,
			ExpansionCallback callback,
			ExpansionDebugHooks hooks,
			PageTitle title,
			WtEntityMap entityMap,
			Map<String, WtValue> arguments,
			boolean forInclusion,
			boolean noRedirect,
			ExpansionFrame rootFrame,
			ExpansionFrame parentFrame,
			List<Warning> warnings,
			LogContainer frameLog,
			boolean timingEnabled,
			boolean catchAll)
	{
		this.compiler = compiler;
		this.callback = callback;
		this.title = title;
		this.entityMap = entityMap;
		this.arguments = arguments;
		this.forInclusion = forInclusion;
		this.noRedirect = noRedirect;
		this.warnings = warnings;
		this.frameLog = frameLog;
		this.rootFrame = rootFrame;
		this.parentFrame = parentFrame;
		
		expansionVisitor = new ExpansionVisitor(
				this,
				frameLog,
				hooks,
				timingEnabled,
				catchAll);
	}
	
	// =========================================================================
	
	public WtEngine getCompiler()
	{
		return compiler;
	}
	
	public ExpansionFrame getRootFrame()
	{
		return rootFrame;
	}
	
	public ExpansionFrame getParentFrame()
	{
		return parentFrame;
	}
	
	public PageTitle getTitle()
	{
		return title;
	}
	
	public Map<String, WtValue> getArguments()
	{
		return arguments;
	}
	
	public boolean isForInclusion()
	{
		return forInclusion;
	}
	
	public LogContainer getFrameLog()
	{
		return frameLog;
	}
	
	public WikiConfig getWikiConfig()
	{
		return compiler.getWikiConfig();
	}
	
	public void fileWarning(Warning warning)
	{
		warnings.add(warning);
	}
	
	public void addWarnings(Collection<Warning> warnings)
	{
		this.warnings.addAll(warnings);
	}
	
	public List<Warning> getWarnings()
	{
		return warnings;
	}
	
	public WtEntityMap getEntityMap()
	{
		return entityMap;
	}
	
	public ExpansionCallback getCallback()
	{
		return callback;
	}
	
	public boolean isNoRedirect()
	{
		return noRedirect;
	}
	
	public UrlService getUrlService()
	{
		return urlService;
	}
	
	// =========================================================================
	
	public WtNode expand(WtNode ppAst) throws ExpansionException
	{
		try
		{
			return (WtNode) expansionVisitor.go(ppAst);
		}
		catch (Exception e)
		{
			throw new ExpansionException(e);
		}
	}
	
	// =========================================================================
	
	public boolean existsPage(PageTitle pageTitle) throws Exception
	{
		return (callback.retrieveWikitext(this, pageTitle) != null) ||
				(callback.fileUrl(pageTitle, -1, -1) != null);
	}
}
