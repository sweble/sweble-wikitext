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
import java.io.StringReader;

import org.sweble.wikitext.lazy.encval.EncodingValidatorLexer;
import org.sweble.wikitext.lazy.encval.ValidatedWikitext;

import de.fau.cs.osr.ptk.common.EntityMap;

public class LazyEncodingValidator
{
	/**
	 * @deprecated Use other validate() method instead
	 */
	public String validate(
			String source,
			String title,
			EntityMap entityMap)
			throws IOException
	{
		StringReader in = new StringReader(source);
		EncodingValidatorLexer lexer = new EncodingValidatorLexer(in);
		
		lexer.setFile(title);
		lexer.setEntityMap(entityMap);
		
		while (lexer.yylex() != null)
			;
		
		in.close();
		
		return lexer.getWikitext();
	}
	
	public ValidatedWikitext validate(String source, String title) throws IOException
	{
		StringReader in = new StringReader(source);
		EncodingValidatorLexer lexer = new EncodingValidatorLexer(in);
		
		EntityMap entityMap = new EntityMap();
		
		lexer.setFile(title);
		lexer.setEntityMap(entityMap);
		
		while (lexer.yylex() != null)
			;
		
		in.close();
		
		return new ValidatedWikitext(lexer.getWikitext(), entityMap);
	}
}
