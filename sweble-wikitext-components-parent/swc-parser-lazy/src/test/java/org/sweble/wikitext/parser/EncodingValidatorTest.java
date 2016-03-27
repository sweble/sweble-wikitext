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

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.sweble.wikitext.parser.encval.ValidatedWikitext;
import org.sweble.wikitext.parser.nodes.WtIllegalCodePoint;
import org.sweble.wikitext.parser.nodes.WtIllegalCodePoint.IllegalCodePointType;
import org.sweble.wikitext.parser.utils.SimpleParserConfig;

import de.fau.cs.osr.ptk.common.ast.AstLocation;

public class EncodingValidatorTest
{
	@Test
	public void testEncodingValidator() throws IOException
	{
		String title = "dummy";

		StringBuilder source = new StringBuilder();
		source.append("Ein einfacher Test-String\n"); // L 0
		source.append("mit ein paar \uE800 und \r\n"); // L 1:13
		source.append("nat\u00FCrlich ein paar \uFDEE.\n"); // L 2:19
		source.append("Aber auch \uDBEF und \uDC80 \r"); // L 3:10, 3:16
		source.append("d\u00FCrfen nicht fehlen. Zu guter \n");// L4
		source.append("Letzt noch ein Wohlklang \u0007."); // L 5:25

		/* Ruins the test string!
		InputStreamReader in = new InputStreamReader(
		        IOUtils.toInputStream(source.toString(), "UTF-8"));
		*/

		/*
		StringReader in = new StringReader(source.toString());
		while (true)
		{
			int c = in.read();
			if (c == -1)
				break;
			
			System.out.format("%c: U+%04x\n", c, (int) c);
		}
		in.close();
		*/

		SimpleParserConfig parserConfig = new SimpleParserConfig();

		WikitextEncodingValidator v = new WikitextEncodingValidator();
		ValidatedWikitext result = v.validate(parserConfig, source.toString(), title);
		String validatedWikitext = result.getWikitext();
		WtEntityMap entityMap = result.getEntityMap();

		WtIllegalCodePoint x0 = (WtIllegalCodePoint) entityMap.getEntity(0);
		assertEquals("\uE800", x0.getCodePoint());
		assertEquals(IllegalCodePointType.PRIVATE_USE_CHARACTER, x0.getType());
		assertEquals(new AstLocation(title, 1, 13), x0.getNativeLocation());

		WtIllegalCodePoint x1 = (WtIllegalCodePoint) entityMap.getEntity(1);
		assertEquals("\uFDEE", x1.getCodePoint());
		assertEquals(IllegalCodePointType.NON_CHARACTER, x1.getType());
		assertEquals(new AstLocation(title, 2, 19), x1.getNativeLocation());

		WtIllegalCodePoint x2 = (WtIllegalCodePoint) entityMap.getEntity(2);
		assertEquals("\uDBEF", x2.getCodePoint());
		assertEquals(IllegalCodePointType.ISOLATED_SURROGATE, x2.getType());
		assertEquals(new AstLocation(title, 3, 10), x2.getNativeLocation());

		WtIllegalCodePoint x3 = (WtIllegalCodePoint) entityMap.getEntity(3);
		assertEquals("\uDC80", x3.getCodePoint());
		assertEquals(IllegalCodePointType.ISOLATED_SURROGATE, x3.getType());
		assertEquals(new AstLocation(title, 3, 16), x3.getNativeLocation());

		WtIllegalCodePoint x4 = (WtIllegalCodePoint) entityMap.getEntity(4);
		assertEquals("\u0007", x4.getCodePoint());
		assertEquals(IllegalCodePointType.CONTROL_CHARACTER, x4.getType());
		assertEquals(new AstLocation(title, 5, 25), x4.getNativeLocation());

		StringBuilder ref = new StringBuilder();
		ref.append("Ein einfacher Test-String\n");
		ref.append("mit ein paar \uE0000\uE001 und \r\n");
		ref.append("nat\u00FCrlich ein paar \uE0001\uE001.\n");
		ref.append("Aber auch \uE0002\uE001 und \uE0003\uE001 \r");
		ref.append("d\u00FCrfen nicht fehlen. Zu guter \n");
		ref.append("Letzt noch ein Wohlklang \uE0004\uE001.");

		assertEquals(ref.toString(), validatedWikitext);
	}
}
