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
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wikitext.parser.utils.StringConversionException;

public class ParserFunctionIfExist
		extends
			ParserFunctionsExtPfn.IfThenElseStmt
{
	private static final long serialVersionUID = 1L;

	/**
	 * For un-marshaling only.
	 */
	public ParserFunctionIfExist()
	{
		super("ifexist", 1 /* thenArgIndex */);
	}

	/**
	 * <pre>
	 * {{#ifexist: 
	 *       page title 
	 *     | value if exists 
	 *     | value if doesn't exist 
	 * }}
	 * </pre>
	 */
	public ParserFunctionIfExist(WikiConfig wikiConfig)
	{
		super(wikiConfig, "ifexist", 1 /* thenArgIndex */);
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

			PageTitle pageTitle = PageTitle.make(frame.getWikiConfig(), testStr);

			return frame.existsPage(pageTitle);
		}
		catch (StringConversionException e1)
		{
			// We have to convert the entire argument to a string to create a page name from it.
			return false;
		}
		catch (LinkTargetException e)
		{
			// A page with an illegal name cannot exist.
			return false;
		}
		catch (Exception e)
		{
			// Interpret an error while testing for existence as non-existence.
			return false;
		}
	}
}
