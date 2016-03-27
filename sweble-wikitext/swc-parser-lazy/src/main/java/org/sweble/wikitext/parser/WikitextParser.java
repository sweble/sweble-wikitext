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

import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.parser.RatsWikitextParser;
import org.sweble.wikitext.parser.preprocessor.PreprocessedWikitext;

import de.fau.cs.osr.ptk.common.ParserCommon;
import xtc.parser.ParseError;
import xtc.parser.ParseException;
import xtc.parser.Result;
import xtc.parser.SemanticValue;

public class WikitextParser
		extends
			ParserCommon<WtNode>
{
	private final ParserConfig config;

	private RatsWikitextParser parser;

	// =========================================================================

	public WikitextParser(ParserConfig config)
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
		PreprocessedWikitext ppWt =
				new PreprocessedWikitext(src, new WtEntityMapImpl());

		return parseArticle(ppWt, title);
	}

	public WtNode parseArticle(PreprocessedWikitext wikitext, String title) throws IOException, ParseException
	{
		Reader in = new StringReader(wikitext.getWikitext());

		int inputSize = wikitext.getWikitext().getBytes().length;

		parser = new RatsWikitextParser(in, title, inputSize);

		parser.getState().init(config, wikitext.getEntityMap());

		parser.setNodeFactory(config.getNodeFactory());

		Result r = null;

		//RatsWikitextParser.enableStats();
		{
			r = this.parser.pArticle(0);
		}
		if (RatsWikitextParser.isStatsEnabled())
			RatsWikitextParser.getStats().dump(System.err);

		if (r.hasValue())
		{
			SemanticValue v = (SemanticValue) r;

			if (v.value instanceof WtParsedWikitextPage)
			{
				return (WtNode) process((WtParsedWikitextPage) v.value);
			}
			else
			{
				throw new ParseException(
						"Internal parser error: Unexpected parser result type!");
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
						parser.location(err.index),
						err.msg));
			}
		}
	}
}
