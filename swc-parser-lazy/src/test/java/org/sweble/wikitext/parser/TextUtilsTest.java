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

import org.junit.Assert;
import org.junit.Test;
import org.sweble.wikitext.parser.utils.TextUtils;

import de.fau.cs.osr.ptk.common.AstPrinter;

public class TextUtilsTest
{
	@Test
	public void testStringToAst()
	{
		final String actual =
				AstPrinter.print(TextUtils.stringToAst("H&llo <Welt>!"));
		
		final String expected =
				"[\n" +
						"  Text(\"H\")\n" +
						"  XmlEntityRef(\n" +
						"    Properties:\n" +
						"          RTD = RtData: [0] = \"&amp;\"\n" +
						"      {N} name = \"amp\"\n" +
						"      {N} resolved = \"&\"\n" +
						"  )\n" +
						"  Text(\"llo \")\n" +
						"  XmlEntityRef(\n" +
						"    Properties:\n" +
						"          RTD = RtData: [0] = \"&lt;\"\n" +
						"      {N} name = \"lt\"\n" +
						"      {N} resolved = \"<\"\n" +
						"  )\n" +
						"  Text(\"Welt\")\n" +
						"  XmlEntityRef(\n" +
						"    Properties:\n" +
						"          RTD = RtData: [0] = \"&gt;\"\n" +
						"      {N} name = \"gt\"\n" +
						"      {N} resolved = \">\"\n" +
						"  )\n" +
						"  Text(\"!\")\n" +
						"]\n";
		
		Assert.assertEquals(expected, actual);
	}
}
