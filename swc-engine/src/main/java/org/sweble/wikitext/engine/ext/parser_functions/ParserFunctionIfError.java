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
import org.sweble.wikitext.engine.nodes.EngSoftErrorNode;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTemplate;

public class ParserFunctionIfError
		extends
			ParserFunctionsExtPfn.IfThenElseStmt
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * <pre>
	 * {{#iferror: 
	 *       test string 
	 *     | value if error 
	 *     | value if correct 
	 * }}
	 * </pre>
	 */
	public ParserFunctionIfError(WikiConfig wikiConfig)
	{
		super(wikiConfig, "iferror", 1, true);
	}
	
	@Override
	protected boolean evaluateCondition(
			WtTemplate pfn,
			ExpansionFrame frame,
			List<? extends WtNode> args)
	{
		WtNode arg0 = frame.expand(args.get(0));
		
		boolean hasError = searchErrorNode(arg0);
		
		// If NO error occurred the test statement becomes the default result
		setDefault(hasError ? nf().list() : arg0);
		//		if (!eval)
		//			setDefault(arg0);
		
		return hasError;
	}
	
	private static boolean searchErrorNode(WtNode arg0)
	{
		if (arg0 instanceof EngSoftErrorNode)
		{
			return true;
		}
		else
		{
			for (WtNode n : arg0)
			{
				if (searchErrorNode(n))
					return true;
			}
			
			return false;
		}
	}
}
