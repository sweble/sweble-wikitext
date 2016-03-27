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

import org.sweble.wikitext.parser.encval.EncodingValidatorLexer;
import org.sweble.wikitext.parser.encval.ValidatedWikitext;

public class WikitextEncodingValidator
{
	public ValidatedWikitext validate(
			ParserConfig parserConfig,
			WtEntityMap entityMap,
			String title,
			String source)
			throws IOException
	{
		StringReader in = new StringReader(source);
		EncodingValidatorLexer lexer = new EncodingValidatorLexer(in);

		lexer.setFile(title);
		lexer.setEntityMap(entityMap);
		lexer.setWikitextNodeFactory(parserConfig.getNodeFactory());

		while (lexer.yylex() != null)
			;

		in.close();

		return new ValidatedWikitext(lexer.getWikitext(), entityMap);
	}

	public ValidatedWikitext validate(
			ParserConfig parserConfig,
			WtEntityMap entityMap,
			String title,
			Reader source)
			throws IOException
	{
		EncodingValidatorLexer lexer = new EncodingValidatorLexer(source);

		lexer.setFile(title);
		lexer.setEntityMap(entityMap);
		lexer.setWikitextNodeFactory(parserConfig.getNodeFactory());

		while (lexer.yylex() != null)
			;

		return new ValidatedWikitext(lexer.getWikitext(), entityMap);
	}

	public ValidatedWikitext validate(
			ParserConfig parserConfig,
			String source,
			String title) throws IOException
	{
		return validate(parserConfig, new WtEntityMapImpl(), title, source);
	}

	public ValidatedWikitext validate(
			ParserConfig parserConfig,
			Reader source,
			String title) throws IOException
	{
		return validate(parserConfig, new WtEntityMapImpl(), title, source);
	}
}
