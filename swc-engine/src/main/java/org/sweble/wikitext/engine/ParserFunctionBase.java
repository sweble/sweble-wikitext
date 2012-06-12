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

package org.sweble.wikitext.engine;

import java.io.Serializable;
import java.util.List;

import org.sweble.wikitext.lazy.preprocessor.Template;

import de.fau.cs.osr.ptk.common.ast.AstNode;

public abstract class ParserFunctionBase
		implements
			Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final String name;
	
	private final PfnArgumentMode argMode;
	
	// =========================================================================
	
	public ParserFunctionBase(String name)
	{
		this(PfnArgumentMode.UNEXPANDED_VALUES, name);
		
	}
	
	public ParserFunctionBase(PfnArgumentMode argMode, String name)
	{
		this.argMode = argMode;
		this.name = name;
	}
	
	// =========================================================================
	
	public String getName()
	{
		return name;
	}
	
	public PfnArgumentMode getArgMode()
	{
		return argMode;
	}
	
	public abstract AstNode invoke(
			Template template,
			ExpansionFrame preprocessorFrame,
			List<? extends AstNode> argsValues);
}
