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

package org.sweble.wikitext.parser.preprocessor;

import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.nodes.WtNode;

import de.fau.cs.osr.ptk.common.ParserState;

public class WikitextPreprocessorState
		extends
			ParserState<WikitextPreprocessorContext>
{
	private ParserConfig config;

	private WtEntityMap entityMap;

	private boolean autoCorrect;

	private boolean warningsEnabled;

	private boolean gatherRtData;

	// =========================================================================

	private boolean hasOnlyInclude;

	private boolean parseForInclusion;

	// =========================================================================

	public WikitextPreprocessorState()
	{
		super(WikitextPreprocessorContext.class);

		this.hasOnlyInclude = false;

		this.parseForInclusion = false;
	}

	// =========================================================================

	public ParserConfig getConfig()
	{
		return config;
	}

	public void init(
			ParserConfig config,
			WtEntityMap entityMap,
			boolean forInclusion)
	{
		this.config = config;

		this.entityMap = entityMap;

		this.parseForInclusion = forInclusion;

		this.autoCorrect = config.isAutoCorrect();

		this.warningsEnabled = config.isWarningsEnabled();

		this.gatherRtData = config.isGatherRtData();
	}

	// =========================================================================

	public WtEntityMap getEntityMap()
	{
		return entityMap;
	}

	public boolean isAutoCorrect()
	{
		return autoCorrect;
	}

	public boolean isWarnignsEnabled()
	{
		return warningsEnabled;
	}

	public boolean isGatherRtData()
	{
		return gatherRtData;
	}

	// =========================================================================

	public void setTagExtensionName(String name)
	{
		getTop().setTagExtensionName(name);
	}

	public boolean isValidClosingTag(String name)
	{
		String cur = getTop().getTagExtensionName();
		if (cur == null)
			return false;
		return name.compareToIgnoreCase(cur) == 0;
	}

	public void setTemplateBraces(int i)
	{
		getTop().setTemplateBraces(i);
	}

	public int getTemplateBraces()
	{
		return getTop().getTemplateBraces();
	}

	public boolean hasAtLeastTemplateBraces(int i)
	{
		return getTop().getTemplateBraces() >= i;
	}

	public void eatTemplateBraces(int i)
	{
		getTop().setTemplateBraces(
				getTop().getTemplateBraces() - i);
	}

	// =========================================================================

	public boolean isHasOnlyInclude()
	{
		return hasOnlyInclude;
	}

	public void setHasOnlyInclude(boolean hasOnlyInclude)
	{
		this.hasOnlyInclude = hasOnlyInclude;
	}

	public boolean isParseForInclusion()
	{
		return parseForInclusion;
	}

	public boolean isIgnoredElement(String name)
	{
		String lcName = name.toLowerCase();
		if (isParseForInclusion())
		{
			return "noinclude".compareTo(lcName) == 0;
		}
		else
		{
			return "includeonly".compareTo(lcName) == 0;
		}
	}

	public boolean isIgnoredTag(String name)
	{
		String lcName = name.toLowerCase();
		if (isParseForInclusion())
		{
			return "includeonly".compareTo(lcName) == 0;
		}
		else
		{
			return ("noinclude".compareTo(lcName) == 0) ||
					("onlyinclude".compareTo(lcName) == 0);
		}
	}

	public boolean isRedirectKeyword(String keyword)
	{
		return config.isRedirectKeyword(keyword);
	}

	public WtNode getEntity(int id)
	{
		return entityMap.getEntity(id);
	}
}
