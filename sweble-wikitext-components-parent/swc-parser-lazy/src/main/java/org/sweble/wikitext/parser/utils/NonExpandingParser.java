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

package org.sweble.wikitext.parser.utils;

import java.io.IOException;

import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WikitextEncodingValidator;
import org.sweble.wikitext.parser.WikitextParser;
import org.sweble.wikitext.parser.WikitextPostprocessor;
import org.sweble.wikitext.parser.WikitextPreprocessor;
import org.sweble.wikitext.parser.encval.ValidatedWikitext;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.nodes.WtPreproWikitextPage;
import org.sweble.wikitext.parser.parser.PreprocessorToParserTransformer;
import org.sweble.wikitext.parser.preprocessor.PreprocessedWikitext;

import de.fau.cs.osr.ptk.common.ParserCommon;
import xtc.parser.ParseException;

public final class NonExpandingParser
		extends
			ParserCommon<WtNode>
{
	private ParserConfig parserConfig;

	// =========================================================================

	public NonExpandingParser()
	{
		parserConfig = new NonExpandingParserConfig();
	}

	public NonExpandingParser(
			boolean warningsEnabled,
			boolean gatherRtd,
			boolean autoCorrect)

	{
		parserConfig = new NonExpandingParserConfig(
				warningsEnabled,
				gatherRtd,
				autoCorrect);
	}

	public NonExpandingParser(
			boolean warningsEnabled,
			boolean gatherRtd,
			boolean autoCorrect,
			boolean langConvTagsEnabled)

	{
		parserConfig = new SimpleParserConfig(
				warningsEnabled,
				gatherRtd,
				autoCorrect,
				langConvTagsEnabled);
	}

	// =========================================================================

	@Override
	public WtNode parseArticle(String source, String title) throws IOException, ParseException
	{
		// Encoding validation

		WikitextEncodingValidator v = new WikitextEncodingValidator();

		ValidatedWikitext validated = v.validate(parserConfig, source, title);

		// Pre-processing

		WikitextPreprocessor prep = new WikitextPreprocessor(parserConfig);

		WtPreproWikitextPage prepArticle = (WtPreproWikitextPage) prep.parseArticle(validated, title, false);

		// Parsing

		PreprocessedWikitext ppw = PreprocessorToParserTransformer
				.transform(prepArticle);

		WikitextParser p = new WikitextParser(parserConfig);

		WtParsedWikitextPage parsedArticle = (WtParsedWikitextPage) p.parseArticle(ppw, title);

		// Post-processing

		WikitextPostprocessor postp = new WikitextPostprocessor(parserConfig);

		WtParsedWikitextPage postpArticle = (WtParsedWikitextPage) postp.postprocess(parsedArticle, title);

		// User-defined processing

		WtParsedWikitextPage userProcessed = (WtParsedWikitextPage) process(postpArticle);

		return userProcessed;
	}

	@Override
	public Object getConfig()
	{
		return parserConfig;
	}
}
