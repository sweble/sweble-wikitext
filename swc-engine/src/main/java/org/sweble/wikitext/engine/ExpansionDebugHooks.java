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

import org.sweble.wikitext.engine.log.ResolveMagicWordLog;
import org.sweble.wikitext.engine.log.ResolveParameterLog;
import org.sweble.wikitext.engine.log.ResolveParserFunctionLog;
import org.sweble.wikitext.engine.log.ResolveRedirectLog;
import org.sweble.wikitext.engine.log.ResolveTagExtensionLog;
import org.sweble.wikitext.engine.log.ResolveTransclusionLog;
import org.sweble.wikitext.parser.nodes.PageSwitch;
import org.sweble.wikitext.parser.nodes.Redirect;
import org.sweble.wikitext.parser.nodes.TagExtension;
import org.sweble.wikitext.parser.nodes.Template;
import org.sweble.wikitext.parser.nodes.TemplateArgument;
import org.sweble.wikitext.parser.nodes.TemplateParameter;
import org.sweble.wikitext.parser.nodes.WikitextNode;
import org.sweble.wikitext.parser.nodes.WtLeafNode;
import org.sweble.wikitext.parser.nodes.WtList;

public abstract class ExpansionDebugHooks
{
	public static final WikitextNode PROCEED = new WtLeafNode()
	{
		private static final long serialVersionUID = 1L;
	};
	
	// =========================================================================
	
	public WikitextNode beforeResolveRedirect(
			ExpansionVisitor expansionVisitor,
			Redirect n,
			String target)
	{
		return PROCEED;
	}
	
	public WikitextNode afterResolveRedirect(
			ExpansionVisitor expansionVisitor,
			Redirect n,
			String target,
			WikitextNode result,
			ResolveRedirectLog log)
	{
		return result;
	}
	
	public WikitextNode beforeResolveParserFunction(
			ExpansionVisitor expansionVisitor,
			Template n,
			ParserFunctionBase pfn,
			List<? extends WikitextNode> argsValues)
	{
		return PROCEED;
	}
	
	public WikitextNode afterResolveParserFunction(
			ExpansionVisitor expansionVisitor,
			Template n,
			ParserFunctionBase pfn,
			List<? extends WikitextNode> argsValues,
			WikitextNode result,
			ResolveParserFunctionLog log)
	{
		return result;
	}
	
	public WikitextNode beforeResolveTransclusion(
			ExpansionVisitor expansionVisitor,
			Template n,
			String target,
			List<TemplateArgument> args)
	{
		return PROCEED;
	}
	
	public WikitextNode afterResolveTransclusion(
			ExpansionVisitor expansionVisitor,
			Template n,
			String target,
			List<TemplateArgument> args,
			WikitextNode result,
			ResolveTransclusionLog log)
	{
		return result;
	}
	
	public WikitextNode beforeResolveParameter(
			ExpansionVisitor expansionVisitor,
			TemplateParameter n,
			String name)
	{
		return PROCEED;
	}
	
	public WikitextNode afterResolveParameter(
			ExpansionVisitor expansionVisitor,
			TemplateParameter n,
			String name,
			WikitextNode result,
			ResolveParameterLog log)
	{
		return result;
	}
	
	public WikitextNode beforeResolveTagExtension(
			ExpansionVisitor expansionVisitor,
			TagExtension n,
			String name,
			WtList attrs,
			String body)
	{
		return PROCEED;
	}
	
	public WikitextNode afterResolveTagExtension(
			ExpansionVisitor expansionVisitor,
			TagExtension n,
			String name,
			WtList attributes,
			String body,
			WikitextNode result,
			ResolveTagExtensionLog log)
	{
		return result;
	}
	
	public WikitextNode beforeResolvePageSwitch(
			ExpansionVisitor expansionVisitor,
			PageSwitch n,
			String word)
	{
		return PROCEED;
	}
	
	public WikitextNode afterResolvePageSwitch(
			ExpansionVisitor expansionVisitor,
			PageSwitch n,
			String word,
			WikitextNode result,
			ResolveMagicWordLog log)
	{
		return result;
	}
}
