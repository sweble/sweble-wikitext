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

package org.sweble.wikitext.engine.ext.builtin;

import java.util.List;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.PfnArgumentMode;
import org.sweble.wikitext.engine.config.ParserFunctionGroup;
import org.sweble.wikitext.parser.RtData;
import org.sweble.wikitext.parser.nodes.Template;
import org.sweble.wikitext.parser.nodes.TemplateArgument;
import org.sweble.wikitext.parser.utils.AstBuilder;

import de.fau.cs.osr.ptk.common.ast.AstNode;

public class BuiltInParserFunctions
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	protected BuiltInParserFunctions()
	{
		super("Built-in Parser Functions");
		addParserFunction(new ParserFunctionSafeSubst());
	}
	
	public static BuiltInParserFunctions group()
	{
		return new BuiltInParserFunctions();
	}
	
	// =========================================================================
	// ==
	// == {{safesubst}}
	// ==
	// =========================================================================
	
	public static final class ParserFunctionSafeSubst
			extends
				ParserFunctionBase
	{
		private static final long serialVersionUID = 1L;
		
		public ParserFunctionSafeSubst()
		{
			super(PfnArgumentMode.TEMPLATE_ARGUMENTS, "safesubst");
		}
		
		@Override
		public AstNode invoke(
				AstNode template,
				ExpansionFrame preprocessorFrame,
				List<? extends AstNode> args)
		{
			if (args.size() < 0)
				return null;
			
			// Assuming we are  NOT doing a pre save transformation
			
			AstNode name = ((TemplateArgument) args.get(0)).getValue();
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<TemplateArgument> tmplArgs = (List) args.subList(1, args.size());
			
			Template tmpl = AstBuilder.astTemplate()
					.withName(name)
					.withArguments(tmplArgs)
					.build();
			
			RtData rtd = (RtData) template.getAttribute("RTD");
			if (rtd != null)
				tmpl.setAttribute("RTD", rtd);
			
			return preprocessorFrame.expand(tmpl);
		}
	}
}
