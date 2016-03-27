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

import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.nodes.EngineNodeFactory;
import org.sweble.wikitext.parser.nodes.WtPreproWikitextPage;

public interface WtEngine
{

	public void setDebugHooks(ExpansionDebugHooks hooks);

	public void setNoRedirect(boolean noRedirect);

	public void setTimingEnabled(boolean timingEnabled);

	public void setCatchAll(boolean catchAll);

	public WikiConfig getWikiConfig();

	public ExpansionDebugHooks getDebugHooks();

	public boolean isNoRedirect();

	public boolean isTimingEnabled();

	public boolean isCatchAll();

	public EngineNodeFactory nf();

	/**
	 * Takes wikitext and preprocesses the wikitext (without performing
	 * expansion). The following steps are performed:
	 * <ul>
	 * <li>Validation</li>
	 * <li>Preprocessing (for inclusion/viewing)</li>
	 * <li>Entity substitution</li>
	 * <li>Optional: Expansion</li>
	 * </ul>
	 */
	public EngProcessedPage preprocess(
			PageId pageId,
			String wikitext,
			boolean forInclusion,
			ExpansionCallback callback)
			throws EngineException;

	/**
	 * Takes wikitext and expands the wikitext. The following steps are
	 * performed:
	 * <ul>
	 * <li>Validation</li>
	 * <li>Preprocessing (for viewing)</li>
	 * <li>Entity substitution</li>
	 * <li>Expansion</li>
	 * </ul>
	 */
	public EngProcessedPage expand(
			PageId pageId,
			String wikitext,
			ExpansionCallback callback)
			throws EngineException;

	/**
	 * Takes wikitext and expands the wikitext. The following steps are
	 * performed:
	 * <ul>
	 * <li>Validation</li>
	 * <li>Preprocessing (for viewing)</li>
	 * <li>Entity substitution</li>
	 * <li>Expansion</li>
	 * </ul>
	 */
	public EngProcessedPage expand(
			PageId pageId,
			String wikitext,
			boolean forInclusion,
			ExpansionCallback callback)
			throws EngineException;

	/**
	 * Takes wikitext and parses the wikitext for viewing. The following steps
	 * are performed:
	 * <ul>
	 * <li>Validation</li>
	 * <li>Preprocessing (for viewing)</li>
	 * <li>Entity substitution</li>
	 * <li>Optional: Expansion</li>
	 * <li>Parsing</li>
	 * <li>Entity substitution</li>
	 * </ul>
	 */
	public EngProcessedPage parse(
			PageId pageId,
			String wikitext,
			ExpansionCallback callback)
			throws EngineException;

	/**
	 * Takes wikitext and parses the wikitext for viewing. The following steps
	 * are performed:
	 * <ul>
	 * <li>Validation</li>
	 * <li>Preprocessing (for viewing)</li>
	 * <li>Entity substitution</li>
	 * <li>Optional: Expansion</li>
	 * <li>Parsing</li>
	 * <li>Entity substitution</li>
	 * <li>Postprocessing</li>
	 * </ul>
	 */
	public EngProcessedPage postprocess(
			PageId pageId,
			String wikitext,
			ExpansionCallback callback)
			throws EngineException;

	/**
	 * Takes an AST after preprocessing or after expansion and performs the
	 * following steps:
	 * <ul>
	 * <li>Parsing</li>
	 * <li>Entity substitution</li>
	 * <li>Postprocessing</li>
	 * </ul>
	 */
	public EngProcessedPage postprocessPpOrExpAst(
			PageId pageId,
			WtPreproWikitextPage pprAst)
			throws EngineException;

}
