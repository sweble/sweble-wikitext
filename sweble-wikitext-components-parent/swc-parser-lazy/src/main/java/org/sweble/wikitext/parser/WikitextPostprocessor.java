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

import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.postprocessor.TicksAnalyzer;
import org.sweble.wikitext.parser.postprocessor.TreeBuilder;

public class WikitextPostprocessor
{
	private final ParserConfig config;

	// =========================================================================

	public WikitextPostprocessor(ParserConfig config)
	{
		this.config = config;
	}

	// =========================================================================

	public WtNode postprocess(WtNode ast, String title)
	{
		WtNode result = ast;

		result = TicksAnalyzer.process(config, result);
		result = TreeBuilder.process(config, result);

		return result;
	}
}
