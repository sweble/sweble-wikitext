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

import org.sweble.wikitext.engine.config.ParserFunctionGroup;
import org.sweble.wikitext.engine.config.WikiConfig;

public class ParserFunctionsPfnExt
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	protected ParserFunctionsPfnExt(WikiConfig wikiConfig)
	{
		super("Extension - Parser Functions");
		addParserFunction(new ParserFunctionExpr(wikiConfig));
		addParserFunction(new ParserFunctionIf(wikiConfig));
		addParserFunction(new ParserFunctionIfeq(wikiConfig));
		addParserFunction(new ParserFunctionIfError(wikiConfig));
		addParserFunction(new ParserFunctionIfExist(wikiConfig));
		addParserFunction(new ParserFunctionIfExpr(wikiConfig));
		addParserFunction(new ParserFunctionSwitch(wikiConfig));
		addParserFunction(new ParserFunctionTime(wikiConfig));
		addParserFunction(new ParserFunctionTimeLocal(wikiConfig));
		addParserFunction(new ParserFunctionTitleparts(wikiConfig));
	}

	public static ParserFunctionsPfnExt group(WikiConfig wikiConfig)
	{
		return new ParserFunctionsPfnExt(wikiConfig);
	}
}
