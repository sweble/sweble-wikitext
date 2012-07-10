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

package org.sweble.wikitext.engine.ext.core;

import java.util.List;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.PfnArgumentMode;
import org.sweble.wikitext.lazy.preprocessor.Template;

import de.fau.cs.osr.ptk.common.ast.AstNode;

public abstract class CorePfnVariable
		extends
			ParserFunctionBase
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public CorePfnVariable(String name)
	{
		// Most variables don't take arguments so don't waste time with funny 
		// conversions.
		super(PfnArgumentMode.TEMPLATE_ARGUMENTS, name);
	}
	
	public CorePfnVariable(PfnArgumentMode argMode, String name)
	{
		super(argMode, name);
	}
	
	// =========================================================================
	
	@Override
	public final AstNode invoke(
			AstNode var,
			ExpansionFrame frame,
			List<? extends AstNode> argsValues)
	{
		return invoke((Template) var, frame, argsValues);
	}
	
	public AstNode invoke(
			Template var,
			ExpansionFrame frame,
			List<? extends AstNode> argsValues)
	{
		return invoke(var, frame);
	}
	
	protected AstNode invoke(Template var, ExpansionFrame frame)
	{
		return var;
	}
}
