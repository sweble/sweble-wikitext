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

package org.sweble.wikitext.parser.parser;

import java.util.regex.Pattern;

import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.WtEntityMapImpl;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtPageName;

import de.fau.cs.osr.ptk.common.ParserState;

public class WikitextParserState
		extends
			ParserState<WikitextParserContext>
{
	private WtEntityMap entityMap = new WtEntityMapImpl();

	private ParserConfig config;

	private Pattern postfixPattern;

	private Pattern prefixPattern;

	private boolean autoCorrect;

	private boolean warningsEnabled;

	private boolean gatherRtData;

	// =========================================================================

	@Override
	protected WikitextParserContext instantiateContext()
	{
		return new WikitextParserContext();
	}

	// =========================================================================

	public WtEntityMap getEntityMap()
	{
		return entityMap;
	}

	public WtNode getEntity(int id)
	{
		return entityMap.getEntity(id);
	}

	// =========================================================================

	public ParserConfig getConfig()
	{
		return config;
	}

	public void init(ParserConfig config, WtEntityMap entityMap)
	{
		this.config = config;

		this.entityMap = entityMap;

		this.autoCorrect = config.isAutoCorrect();

		this.warningsEnabled = config.isWarningsEnabled();

		this.gatherRtData = config.isGatherRtData();

		this.prefixPattern = Pattern.compile(
				"(" + config.getInternalLinkPrefixPattern() + ")$");

		this.postfixPattern = Pattern.compile(
				config.getInternalLinkPostfixPattern());
	}

	// =========================================================================

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

	public Pattern getInternalLinkPrefixPattern()
	{
		return prefixPattern;
	}

	public Pattern getInternalLinkPostfixPattern()
	{
		return postfixPattern;
	}

	// =========================================================================

	public LinkBuilder getLinkBuilder()
	{
		return getTop().getLinkBuilder();
	}

	public void initLinkBuilder(WtPageName target)
	{
		getTop().initLinkBuilder(config, target);
	}

	// =========================================================================

	public ParserScopes getScope()
	{
		return getTop().getScope();
	}

	public void setScope(ParserScopes scope)
	{
		WikitextParserContext c = getTop();
		c.setScope(scope);
		if (scope.isSticky())
			c.addStickingScope(scope);
	}

	public boolean accepts(ParserAtoms atom)
	{
		WikitextParserContext c = getTop();
		if (c.getScope().accepts(atom))
		{
			int sticking = c.getStickingScopes();
			for (int i = 0; sticking != 0; ++i, sticking >>= 1)
			{
				if ((sticking & 1) != 0)
				{
					if (!ParserScopes.values()[i].accepts(atom))
						return false;
				}
			}
			return true;
		}
		return false;
	}

	public boolean inScope(ParserScopes scope)
	{
		WikitextParserContext c = getTop();
		if (c.getScope() == scope)
		{
			return true;
		}
		else
		{
			int bit = 1 << scope.ordinal();
			return 0 != (c.getStickingScopes() & bit);
		}
	}
}
