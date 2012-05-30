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

package org.sweble.wikitext.lazy.utils;

import static org.junit.Assert.*;
import static org.sweble.wikitext.lazy.utils.AstBuilder.*;

import org.junit.Test;
import org.sweble.wikitext.lazy.parser.HorizontalRule;
import org.sweble.wikitext.lazy.preprocessor.XmlComment;

public class TestWikitextPrinter
{
	@Test
	public void wikitextPrinterGeneratesCorrectOutputForHorizontalRule()
	{
		HorizontalRule node = astHr().build();
		
		assertEquals(
				"\n\n----",
				WikitextPrinter.print(node));
	}
	
	@Test
	public void wikitextPrinterProducesNoOutputForComments()
	{
		XmlComment node = astComment().build();
		
		assertEquals(
				"",
				WikitextPrinter.print(node));
	}
}
