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

import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.nodes.WtPageName;

import de.fau.cs.osr.ptk.common.ParserContext;

public class WikitextParserContext
		extends
			ParserContext
{
	private int stickingScopes;

	private ParserScopes scope;

	private LinkBuilder linkBuilder;

	// =========================================================================

	@Override
	public final void clear()
	{
		this.scope = null;
		this.stickingScopes = 0;
		this.linkBuilder = null;
	}

	@Override
	public final void init(ParserContext parent)
	{
		WikitextParserContext p = (WikitextParserContext) parent;
		this.stickingScopes = p.stickingScopes;
		this.scope = p.scope;
		this.linkBuilder = p.linkBuilder; // null;
	}

	// =========================================================================

	public final ParserScopes getScope()
	{
		return scope;
	}

	public final void setScope(ParserScopes scope)
	{
		this.scope = scope;
	}

	public final int getStickingScopes()
	{
		return stickingScopes;
	}

	public final void addStickingScope(ParserScopes scope)
	{
		stickingScopes |= 1 << scope.ordinal();
	}

	public final LinkBuilder getLinkBuilder()
	{
		return linkBuilder;
	}

	public final void initLinkBuilder(
			ParserConfig parserConfig,
			WtPageName target)
	{
		this.linkBuilder = new LinkBuilder(parserConfig, target);
	}

	// =========================================================================

	@Override
	public final int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		result = prime * result + stickingScopes;
		return result;
	}

	@Override
	public final boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return equals((WikitextParserContext) obj);
	}

	public final boolean equals(WikitextParserContext other)
	{
		if (this == other)
			return true;
		if (scope != other.scope)
			return false;
		if (stickingScopes != other.stickingScopes)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "WikitextParserContext [stickingScopes=" + stickingScopes + ", scope=" + scope + "]";
	}
}
