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

package org.sweble.wikitext.engine.ext.parser_functions;

import java.util.List;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.ext.parser_functions.ExprParser.ExprError;
import org.sweble.wikitext.engine.nodes.EngineRtData;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.utils.StringConversionException;

public class ParserFunctionExpr
		extends
			ParserFunctionsExtPfn
{
	private static final long serialVersionUID = 1L;

	/**
	 * For un-marshaling only.
	 */
	public ParserFunctionExpr()
	{
		super("expr");
	}

	public ParserFunctionExpr(WikiConfig wikiConfig)
	{
		super(wikiConfig, "expr");
	}

	@Override
	public WtNode invoke(
			WtTemplate pfn,
			ExpansionFrame frame,
			List<? extends WtNode> args)
	{
		if (args.size() < 1)
			return nf().text("");

		WtNode arg0 = frame.expand(args.get(0));

		String expr = null;
		try
		{
			expr = tu().astToText(arg0).trim();
		}
		catch (StringConversionException e)
		{
			return EngineRtData.set(nf().softError(pfn));
		}

		ExprParser p = new ExprParser();
		try
		{
			return nf().text(p.parse(expr));
		}
		catch (ExprError e)
		{
			return EngineRtData.set(nf().softError(e.getMessage()));
		}
	}
}
