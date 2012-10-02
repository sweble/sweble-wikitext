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

import static org.sweble.wikitext.parser.utils.AstBuilder.astText;

import java.util.List;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.PfnArgumentMode;
import org.sweble.wikitext.engine.utils.EngineTextUtils;
import org.sweble.wikitext.parser.nodes.Template;
import org.sweble.wikitext.parser.nodes.WtNode;

public abstract class ParserFunctionsExtPfn
		extends
			ParserFunctionBase
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public ParserFunctionsExtPfn(String name)
	{
		super(name);
	}
	
	public ParserFunctionsExtPfn(PfnArgumentMode argMode, String name)
	{
		super(argMode, name);
	}
	
	// =========================================================================
	
	@Override
	public final WtNode invoke(
			WtNode template,
			ExpansionFrame frame,
			List<? extends WtNode> argsValues)
	{
		return invoke((Template) template, frame, argsValues);
	}
	
	public abstract WtNode invoke(
			Template template,
			ExpansionFrame frame,
			List<? extends WtNode> argsValues);
	
	// =========================================================================
	
	public static abstract class CtrlStmt
			extends
				ParserFunctionsExtPfn
	{
		private static final long serialVersionUID = 1L;
		
		protected CtrlStmt(String name)
		{
			super(PfnArgumentMode.UNEXPANDED_VALUES, name);
		}
		
		@Override
		public final WtNode invoke(
				Template pfn,
				ExpansionFrame frame,
				List<? extends WtNode> args)
		{
			WtNode result = evaluate((Template) pfn, frame, args);
			
			// All control flow statements expand and trim their results.
			
			if (result != null)
			{
				return EngineTextUtils.trim(frame.expand(result));
			}
			else
			{
				return astText("");
			}
		}
		
		protected abstract WtNode evaluate(
				Template pfn,
				ExpansionFrame frame,
				List<? extends WtNode> args);
	}
	
	// =========================================================================
	
	public static abstract class IfThenElseStmt
			extends
				CtrlStmt
	{
		private static final long serialVersionUID = 1L;
		
		private final boolean hasDefault;
		
		private WtNode defaultValue;
		
		private final int thenArgIndex;
		
		protected IfThenElseStmt(String name, int thenArgIndex)
		{
			super(name);
			this.hasDefault = false;
			this.thenArgIndex = thenArgIndex;
		}
		
		protected IfThenElseStmt(
				String name,
				int thenArgIndex,
				boolean hasDefault)
		{
			super(name);
			this.hasDefault = hasDefault;
			this.thenArgIndex = thenArgIndex;
		}
		
		@Override
		protected WtNode evaluate(
				Template pfn,
				ExpansionFrame frame,
				List<? extends WtNode> args)
		{
			if (args.size() <= (hasDefault ? thenArgIndex - 1 : thenArgIndex))
				return astText("");
			
			boolean cond = evaluateCondition(pfn, frame, args);
			
			WtNode result = defaultValue;
			if (cond)
			{
				result = args.get(thenArgIndex);
			}
			else
			{
				int elseArgIndex = thenArgIndex + 1;
				
				if (args.size() > elseArgIndex)
					result = args.get(elseArgIndex);
			}
			
			return result;
		}
		
		protected void setDefault(WtNode defaultValue)
		{
			this.defaultValue = defaultValue;
		}
		
		protected abstract boolean evaluateCondition(
				Template pfn,
				ExpansionFrame frame,
				List<? extends WtNode> args);
	}
}
