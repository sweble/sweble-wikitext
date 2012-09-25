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
import org.sweble.wikitext.parser.parser.PageSwitch;
import org.sweble.wikitext.parser.preprocessor.Redirect;
import org.sweble.wikitext.parser.preprocessor.TagExtension;
import org.sweble.wikitext.parser.preprocessor.Template;
import org.sweble.wikitext.parser.preprocessor.TemplateArgument;
import org.sweble.wikitext.parser.preprocessor.TemplateParameter;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public abstract class ExpansionDebugHooks
{
	public static final AstNode PROCEED = new AstNode()
	{
		private static final long serialVersionUID = 1L;
	};
	
	// =========================================================================
	
	public AstNode beforeResolveRedirect(
			ExpansionVisitor expansionVisitor,
			Redirect n,
			String target)
	{
		return PROCEED;
	}
	
	public AstNode afterResolveRedirect(
			ExpansionVisitor expansionVisitor,
			Redirect n,
			String target,
			AstNode result,
			ResolveRedirectLog log)
	{
		return result;
	}
	
	public AstNode beforeResolveParserFunction(
			ExpansionVisitor expansionVisitor,
			Template n,
			ParserFunctionBase pfn,
			List<? extends AstNode> argsValues)
	{
		return PROCEED;
	}
	
	public AstNode afterResolveParserFunction(
			ExpansionVisitor expansionVisitor,
			Template n,
			ParserFunctionBase pfn,
			List<? extends AstNode> argsValues,
			AstNode result,
			ResolveParserFunctionLog log)
	{
		return result;
	}
	
	public AstNode beforeResolveTransclusion(
			ExpansionVisitor expansionVisitor,
			Template n,
			String target,
			List<TemplateArgument> args)
	{
		return PROCEED;
	}
	
	public AstNode afterResolveTransclusion(
			ExpansionVisitor expansionVisitor,
			Template n,
			String target,
			List<TemplateArgument> args,
			AstNode result,
			ResolveTransclusionLog log)
	{
		return result;
	}
	
	public AstNode beforeResolveParameter(
			ExpansionVisitor expansionVisitor,
			TemplateParameter n,
			String name)
	{
		return PROCEED;
	}
	
	public AstNode afterResolveParameter(
			ExpansionVisitor expansionVisitor,
			TemplateParameter n,
			String name,
			AstNode result,
			ResolveParameterLog log)
	{
		return result;
	}
	
	public AstNode beforeResolveTagExtension(
			ExpansionVisitor expansionVisitor,
			TagExtension n,
			String name,
			NodeList attrs,
			String body)
	{
		return PROCEED;
	}
	
	public AstNode afterResolveTagExtension(
			ExpansionVisitor expansionVisitor,
			TagExtension n,
			String name,
			NodeList attributes,
			String body,
			AstNode result,
			ResolveTagExtensionLog log)
	{
		return result;
	}
	
	public AstNode beforeResolvePageSwitch(
			ExpansionVisitor expansionVisitor,
			PageSwitch n,
			String word)
	{
		return PROCEED;
	}
	
	public AstNode afterResolvePageSwitch(
			ExpansionVisitor expansionVisitor,
			PageSwitch n,
			String word,
			AstNode result,
			ResolveMagicWordLog log)
	{
		return result;
	}
}
