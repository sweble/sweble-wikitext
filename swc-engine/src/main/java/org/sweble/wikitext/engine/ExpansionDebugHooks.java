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
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtLeafNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;

public abstract class ExpansionDebugHooks
{
	public static final WtNode PROCEED = new WtLeafNode()
	{
		private static final long serialVersionUID = 1L;
	};
	
	// =========================================================================
	
	public WtNode beforeResolveRedirect(
			ExpansionVisitor expansionVisitor,
			Redirect n,
			String target)
	{
		return PROCEED;
	}
	
	public WtNode afterResolveRedirect(
			ExpansionVisitor expansionVisitor,
			Redirect n,
			String target,
			WtNode result,
			ResolveRedirectLog log)
	{
		return result;
	}
	
	public WtNode beforeResolveParserFunction(
			ExpansionVisitor expansionVisitor,
			Template n,
			ParserFunctionBase pfn,
			List<? extends WtNode> argsValues)
	{
		return PROCEED;
	}
	
	public WtNode afterResolveParserFunction(
			ExpansionVisitor expansionVisitor,
			Template n,
			ParserFunctionBase pfn,
			List<? extends WtNode> argsValues,
			WtNode result,
			ResolveParserFunctionLog log)
	{
		return result;
	}
	
	public WtNode beforeResolveTransclusion(
			ExpansionVisitor expansionVisitor,
			Template n,
			String target,
			List<TemplateArgument> args)
	{
		return PROCEED;
	}
	
	public WtNode afterResolveTransclusion(
			ExpansionVisitor expansionVisitor,
			Template n,
			String target,
			List<TemplateArgument> args,
			WtNode result,
			ResolveTransclusionLog log)
	{
		return result;
	}
	
	public WtNode beforeResolveParameter(
			ExpansionVisitor expansionVisitor,
			TemplateParameter n,
			String name)
	{
		return PROCEED;
	}
	
	public WtNode afterResolveParameter(
			ExpansionVisitor expansionVisitor,
			TemplateParameter n,
			String name,
			WtNode result,
			ResolveParameterLog log)
	{
		return result;
	}
	
	public WtNode beforeResolveTagExtension(
			ExpansionVisitor expansionVisitor,
			TagExtension n,
			String name,
			WtNodeList attrs,
			String body)
	{
		return PROCEED;
	}
	
	public WtNode afterResolveTagExtension(
			ExpansionVisitor expansionVisitor,
			TagExtension n,
			String name,
			WtNodeList attributes,
			String body,
			WtNode result,
			ResolveTagExtensionLog log)
	{
		return result;
	}
	
	public WtNode beforeResolvePageSwitch(
			ExpansionVisitor expansionVisitor,
			PageSwitch n,
			String word)
	{
		return PROCEED;
	}
	
	public WtNode afterResolvePageSwitch(
			ExpansionVisitor expansionVisitor,
			PageSwitch n,
			String word,
			WtNode result,
			ResolveMagicWordLog log)
	{
		return result;
	}
}
