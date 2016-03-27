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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.sweble.wikitext.parser.encval.ValidatedWikitext;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtPreproWikitextPage;
import org.sweble.wikitext.parser.preprocessor.RatsWikitextPreprocessor;

import de.fau.cs.osr.ptk.common.ParserCommon;
import xtc.parser.ParseError;
import xtc.parser.ParseException;
import xtc.parser.Result;
import xtc.parser.SemanticValue;

public class WikitextPreprocessor
		extends
			ParserCommon<WtNode>
{
	private RatsWikitextPreprocessor preprocessor = null;

	private final ParserConfig config;

	// =========================================================================

	public WikitextPreprocessor(ParserConfig config)
	{
		this.config = config;
	}

	// =========================================================================

	@Override
	public Object getConfig()
	{
		return config;
	}

	// =========================================================================

	@Override
	public WtNode parseArticle(String src, String title) throws IOException, ParseException
	{
		return parseArticle(new ValidatedWikitext(src, new WtEntityMapImpl()), title, false);
	}

	public WtNode parseArticle(
			ValidatedWikitext wikitext,
			String title,
			boolean forInclusion) throws IOException, ParseException
	{
		Reader in = new StringReader(wikitext.getWikitext());

		int inputSize = wikitext.getWikitext().getBytes().length;

		preprocessor = new RatsWikitextPreprocessor(in, title, inputSize);

		preprocessor.getState().init(config, wikitext.getEntityMap(), forInclusion);

		preprocessor.setNodeFactory(config.getNodeFactory());

		Result r = this.preprocessor.pArticle(0);

		if (r.hasValue())
		{
			SemanticValue v = (SemanticValue) r;

			if (v.value instanceof WtPreproWikitextPage)
			{
				return (WtNode) process((WtPreproWikitextPage) v.value);
			}
			else
			{
				throw new ParseException(
						"Internal preprocessor error: " +
								"Unexpected preprocessor result type!");
			}
		}
		else
		{
			ParseError err = (ParseError) r;

			if (err.index == -1)
			{
				throw new ParseException(
						"Parse error: No information available");
			}
			else
			{
				throw new ParseException(String.format(
						"%s: %s",
						preprocessor.location(err.index),
						err.msg));
			}
		}
	}
}
