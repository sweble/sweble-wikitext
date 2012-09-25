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
import org.sweble.wikitext.engine.ext.parser_functions.ExprParser.ExprError;
import org.sweble.wikitext.parser.preprocessor.Template;
import org.sweble.wikitext.parser.utils.StringConversionException;
import org.sweble.wikitext.parser.utils.StringConverter;

import de.fau.cs.osr.ptk.common.ast.AstNode;

public class ParserFunctionIfExpr
		extends
			ParserFunctionsExtPfn.IfThenElseStmt
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * <pre>
	 * {{#ifexpr: 
	 *       expression 
	 *     | value if true 
	 *     | value if false
	 * }}
	 * </pre>
	 */
	public ParserFunctionIfExpr()
	{
		super("ifexpr", 1 /* thenArgIndex */);
	}
	
	@Override
	protected boolean evaluateCondition(
			Template pfn,
			ExpansionFrame frame,
			List<? extends AstNode> args)
	{
		AstNode test = frame.expand(args.get(0));
		
		String expr = null;
		try
		{
			expr = StringConverter.convert(test).trim();
		}
		catch (StringConversionException e)
		{
			// Invalid expressions evaluate to false
			return false;
		}
		
		if (expr.isEmpty())
			return false;
		
		ExprParser p = new ExprParser();
		String result;
		try
		{
			result = p.parse(expr);
		}
		catch (ExprError e)
		{
			// Invalid expressions evaluate to false
			return false;
		}
		
		if (result == null || result.isEmpty())
			return false;
		
		return Double.parseDouble(result) != 0.;
	}
}
