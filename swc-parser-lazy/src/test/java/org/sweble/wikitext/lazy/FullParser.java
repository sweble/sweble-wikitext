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

package org.sweble.wikitext.lazy;

import java.io.IOException;

import org.sweble.wikitext.lazy.encval.ValidatedWikitext;
import org.sweble.wikitext.lazy.parser.LazyParsedPage;
import org.sweble.wikitext.lazy.parser.PreprocessorToParserTransformer;
import org.sweble.wikitext.lazy.preprocessor.LazyPreprocessedPage;
import org.sweble.wikitext.lazy.preprocessor.PreprocessedWikitext;
import org.sweble.wikitext.lazy.utils.SimpleParserConfig;

import xtc.parser.ParseException;
import de.fau.cs.osr.ptk.common.ParserCommon;
import de.fau.cs.osr.ptk.common.ast.AstNode;

public final class FullParser
		extends
			ParserCommon
{
	private ParserConfig parserConfig;
	
	public FullParser()
	{
		parserConfig = new SimpleParserConfig();
	}
	
	public FullParser(
			boolean warningsEnabled,
			boolean gatherRtd,
			boolean autoCorrect)
	
	{
		parserConfig = new SimpleParserConfig(
				warningsEnabled,
				gatherRtd,
				autoCorrect);
	}
	
	@Override
	public AstNode parseArticle(String source, String title) throws IOException, ParseException
	{
		// Encoding validation
		
		LazyEncodingValidator v = new LazyEncodingValidator();
		
		ValidatedWikitext validated = v.validate(source, title);
		
		// Pre-processing
		
		LazyPreprocessor prep = new LazyPreprocessor(parserConfig);
		
		LazyPreprocessedPage prepArticle =
				(LazyPreprocessedPage) prep.parseArticle(validated, title, false);
		
		// Parsing
		
		PreprocessedWikitext ppw = PreprocessorToParserTransformer
				.transform(prepArticle);
		
		LazyParser p = new LazyParser(parserConfig);
		
		LazyParsedPage parsedArticle =
				(LazyParsedPage) p.parseArticle(ppw, title);
		
		// Post-processing
		
		LazyPostprocessor postp = new LazyPostprocessor(parserConfig);
		
		LazyParsedPage postpArticle =
				(LazyParsedPage) postp.postprocess(parsedArticle, title);
		
		// User-defined processing
		
		LazyParsedPage userProcessed = (LazyParsedPage) process(postpArticle);
		
		return userProcessed;
	}
}
