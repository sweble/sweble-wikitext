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

package org.example;

import java.io.IOException;

import org.sweble.wikitext.engine.config.ParserConfigImpl;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.utils.DefaultConfigEn;
import org.sweble.wikitext.lazy.LazyEncodingValidator;
import org.sweble.wikitext.lazy.LazyParser;
import org.sweble.wikitext.lazy.LazyPostprocessor;
import org.sweble.wikitext.lazy.LazyPreprocessor;
import org.sweble.wikitext.lazy.parser.LazyParsedPage;
import org.sweble.wikitext.lazy.parser.PreprocessorToParserTransformer;
import org.sweble.wikitext.lazy.preprocessor.LazyPreprocessedPage;
import org.sweble.wikitext.lazy.preprocessor.PreprocessedWikitext;

import xtc.parser.ParseException;
import de.fau.cs.osr.ptk.common.EntityMap;
import de.fau.cs.osr.ptk.common.ParserCommon;
import de.fau.cs.osr.ptk.common.ast.AstNode;

public final class FullParser
		extends
			ParserCommon
{
	private ParserConfigImpl parserConfig;
	
	public FullParser()
	{
		WikiConfigImpl config = DefaultConfigEn.generate();
		parserConfig = config.getParserConfig();
	}
	
	public FullParser(
			boolean warningsEnabled,
			boolean gatherRtData,
			boolean autoCorrect)
	
	{
		WikiConfigImpl config = DefaultConfigEn.generate();
		parserConfig = config.getParserConfig();
		parserConfig.setWarningsEnabled(warningsEnabled);
		parserConfig.setGatherRtData(gatherRtData);
		parserConfig.setAutoCorrect(autoCorrect);
	}
	
	@Override
	public AstNode parseArticle(String source, String title) throws IOException, ParseException
	{
		EntityMap entityMap = new EntityMap();
		
		// Encoding validation
		
		LazyEncodingValidator v = new LazyEncodingValidator();
		
		String validated = v.validate(source, title, entityMap);
		
		// Pre-processing
		
		ParserCommon prep = new LazyPreprocessor(parserConfig);
		
		LazyPreprocessedPage prepArticle =
				(LazyPreprocessedPage) prep.parseArticle(validated, title);
		
		// Parsing
		
		PreprocessedWikitext ppw = PreprocessorToParserTransformer.transform(
				prepArticle, entityMap);
		
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
