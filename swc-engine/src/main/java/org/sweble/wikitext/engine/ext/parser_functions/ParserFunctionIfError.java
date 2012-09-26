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

import static org.sweble.wikitext.parser.utils.AstBuilder.*;

import java.util.List;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.SoftErrorNode;
import org.sweble.wikitext.parser.nodes.Template;

import de.fau.cs.osr.ptk.common.ast.AstNode;

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
	public ParserFunctionIfError()
	{
		super("iferror", 1, true);
	}
	
	@Override
	protected boolean evaluateCondition(
			Template pfn,
			ExpansionFrame frame,
			List<? extends AstNode> args)
	{
		AstNode arg0 = frame.expand(args.get(0));
		
		boolean eval = searchErrorNode(arg0);
		
		// If NO error occurred the test statement becomes the default result
		setDefault(eval ? astList() : arg0);
		//		if (!eval)
		//			setDefault(arg0);
		
		return eval;
	}
	
	private static boolean searchErrorNode(AstNode arg0)
	{
		if (arg0 instanceof SoftErrorNode)
		{
			return true;
		}
		else
		{
			for (AstNode n : arg0)
			{
				if (searchErrorNode(n))
					return true;
			}
			
			return false;
		}
	}
}
