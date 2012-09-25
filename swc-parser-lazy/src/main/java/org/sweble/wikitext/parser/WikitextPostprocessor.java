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

package org.sweble.wikitext.parser;

import org.sweble.wikitext.parser.postprocessor.ScopedElementBuilder;
import org.sweble.wikitext.parser.postprocessor.TicksAnalyzer;

import de.fau.cs.osr.ptk.common.ast.AstNode;

public class WikitextPostprocessor
{
	private final ParserConfig config;
	
	// =========================================================================
	
	public WikitextPostprocessor(ParserConfig config)
	{
		this.config = config;
	}
	
	// =========================================================================
	
	public AstNode postprocess(AstNode ast, String title)
	{
		AstNode result = ast;
		
		result = TicksAnalyzer.process(/*config, */result);
		result = ScopedElementBuilder.process(config, result);
		
		return result;
	}
}
