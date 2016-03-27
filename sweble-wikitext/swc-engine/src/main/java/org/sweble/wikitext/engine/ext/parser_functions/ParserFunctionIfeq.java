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

public class ParserFunctionIfeq
		extends
			ParserFunctionsExtPfn.IfThenElseStmt
{
	private static final long serialVersionUID = 1L;

	/**
	 * For un-marshaling only.
	 */
	public ParserFunctionIfeq()
	{
		super("ifeq", 2 /* thenArgIndex */);
	}

	/**
	 * <pre>
	 * {{#ifeq: 
	 *       string 1 
	 *     | string 2 
	 *     | value if identical 
	 *     | value if different }}
	 * </pre>
	 */
	public ParserFunctionIfeq(WikiConfig wikiConfig)
	{
		super(wikiConfig, "ifeq", 2 /* thenArgIndex */);
	}

	@Override
	protected boolean evaluateCondition(
			WtTemplate pfn,
			ExpansionFrame frame,
			List<? extends WtNode> args)
	{
		WtNode arg0 = frame.expand(args.get(0));
		WtNode arg1 = frame.expand(args.get(1));

		String a = null;
		String b = null;
		try
		{
			a = tu().astToText(arg0).trim();
			b = tu().astToText(arg1).trim();
		}
		catch (StringConversionException e1)
		{
			// FIXME: Do recursive equality check
		}

		boolean eval = false;
		if (a != null && b != null)
		{
			boolean numbers = false;
			double ia = -1;
			double ib = +1;
			try
			{
				ia = Double.parseDouble(a);
				ib = Double.parseDouble(b);
				numbers = true;
			}
			catch (NumberFormatException e)
			{
				numbers = false;
			}

			if (numbers)
			{
				eval = ia == ib;
			}
			else
			{
				eval = a.equals(b);
			}
		}

		return eval;
	}
}
