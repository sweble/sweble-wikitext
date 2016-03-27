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
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.utils.StringConversionException;

public class ParserFunctionIf
		extends
			ParserFunctionsExtPfn.IfThenElseStmt
{
	private static final long serialVersionUID = 1L;

	/**
	 * For un-marshaling only.
	 */
	public ParserFunctionIf()
	{
		super("if", 1 /* thenArgIndex */);
	}

	/**
	 * <pre>
	 * {{#if: 
	 *       test string 
	 *     | value if test string is not empty 
	 *     | value if test string is empty (or only white space) 
	 * }}
	 * </pre>
	 */
	public ParserFunctionIf(WikiConfig wikiConfig)
	{
		super(wikiConfig, "if", 1 /* thenArgIndex */);
	}

	@Override
	protected boolean evaluateCondition(
			WtTemplate pfn,
			ExpansionFrame frame,
			List<? extends WtNode> args)
	{
		WtNode test = frame.expand(args.get(0));

		String testStr = null;
		try
		{
			testStr = tu().astToText(test).trim();
		}
		catch (StringConversionException e)
		{
			// There are non-text elements. This means something was not 
			// expanded, right? Let's say this evaluates to true since it won't 
			// produce the empty string.
			return true;
		}

		boolean eval = false;
		if (testStr != null)
			eval = !testStr.isEmpty();

		return eval;
	}
}
