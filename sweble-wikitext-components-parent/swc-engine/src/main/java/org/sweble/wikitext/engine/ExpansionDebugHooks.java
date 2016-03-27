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

import java.util.List;

import org.sweble.wikitext.engine.nodes.EngLogMagicWordResolution;
import org.sweble.wikitext.engine.nodes.EngLogParameterResolution;
import org.sweble.wikitext.engine.nodes.EngLogParserFunctionResolution;
import org.sweble.wikitext.engine.nodes.EngLogRedirectResolution;
import org.sweble.wikitext.engine.nodes.EngLogTagExtensionResolution;
import org.sweble.wikitext.engine.nodes.EngLogTransclusionResolution;
import org.sweble.wikitext.parser.nodes.WtLeafNode;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtPageSwitch;
import org.sweble.wikitext.parser.nodes.WtRedirect;
import org.sweble.wikitext.parser.nodes.WtTagExtension;
import org.sweble.wikitext.parser.nodes.WtTagExtensionBody;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtTemplateArgument;
import org.sweble.wikitext.parser.nodes.WtTemplateParameter;

public abstract class ExpansionDebugHooks
{
	public static final WtNode PROCEED = new WtLeafNode()
	{
		private static final long serialVersionUID = 1L;
	};

	// =========================================================================

	public WtNode beforeResolveRedirect(
			ExpansionVisitor expansionVisitor,
			WtRedirect n,
			String target)
	{
		return PROCEED;
	}

	public WtNode afterResolveRedirect(
			ExpansionVisitor expansionVisitor,
			WtRedirect n,
			String target,
			WtNode result,
			EngLogRedirectResolution log)
	{
		return result;
	}

	public WtNode beforeResolveParserFunction(
			ExpansionVisitor expansionVisitor,
			WtTemplate n,
			ParserFunctionBase pfn,
			List<? extends WtNode> argsValues)
	{
		return PROCEED;
	}

	public WtNode afterResolveParserFunction(
			ExpansionVisitor expansionVisitor,
			WtTemplate n,
			ParserFunctionBase pfn,
			List<? extends WtNode> argsValues,
			WtNode result,
			EngLogParserFunctionResolution log)
	{
		return result;
	}

	public WtNode beforeResolveTransclusion(
			ExpansionVisitor expansionVisitor,
			WtTemplate n,
			String target,
			List<WtTemplateArgument> args)
	{
		return PROCEED;
	}

	public WtNode afterResolveTransclusion(
			ExpansionVisitor expansionVisitor,
			WtTemplate n,
			String target,
			List<WtTemplateArgument> args,
			WtNode result,
			EngLogTransclusionResolution log)
	{
		return result;
	}

	public WtNode beforeResolveParameter(
			ExpansionVisitor expansionVisitor,
			WtTemplateParameter n,
			String name)
	{
		return PROCEED;
	}

	public WtNode afterResolveParameter(
			ExpansionVisitor expansionVisitor,
			WtTemplateParameter n,
			String name,
			WtNode result,
			EngLogParameterResolution log)
	{
		return result;
	}

	public WtNode beforeResolveTagExtension(
			ExpansionVisitor expansionVisitor,
			WtTagExtension n,
			String name,
			WtNodeList attrs,
			WtTagExtensionBody wtTagExtensionBody)
	{
		return PROCEED;
	}

	public WtNode afterResolveTagExtension(
			ExpansionVisitor expansionVisitor,
			WtTagExtension n,
			String name,
			WtNodeList attributes,
			WtTagExtensionBody wtTagExtensionBody,
			WtNode result,
			EngLogTagExtensionResolution log)
	{
		return result;
	}

	public WtNode beforeResolvePageSwitch(
			ExpansionVisitor expansionVisitor,
			WtPageSwitch n,
			String word)
	{
		return PROCEED;
	}

	public WtNode afterResolvePageSwitch(
			ExpansionVisitor expansionVisitor,
			WtPageSwitch n,
			String word,
			WtNode result,
			EngLogMagicWordResolution log)
	{
		return result;
	}
}
