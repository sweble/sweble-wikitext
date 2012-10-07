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

import static org.junit.Assert.*;

import org.junit.Test;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtXmlEntityRef;
import org.sweble.wikitext.parser.utils.TextUtils;

public class TextUtilsTest
{
	@Test
	public void testStringToAst()
	{
		WtNode ast = TextUtils.stringToAst("H&llo <Welt>!");
		
		assertEquals(7, ast.size());
		int i = 0;
		
		assertTrue(ast.get(i) instanceof WtText);
		assertEquals("H", ast.get(i, WtText.class).getContent());
		++i;
		
		assertTrue(ast.get(i) instanceof WtXmlEntityRef);
		assertEquals("&", ast.get(i, WtXmlEntityRef.class).getResolved());
		assertEquals("amp", ast.get(i, WtXmlEntityRef.class).getName());
		++i;
		
		assertTrue(ast.get(i) instanceof WtText);
		assertEquals("llo ", ast.get(i, WtText.class).getContent());
		++i;
		
		assertTrue(ast.get(i) instanceof WtXmlEntityRef);
		assertEquals("<", ast.get(i, WtXmlEntityRef.class).getResolved());
		assertEquals("lt", ast.get(i, WtXmlEntityRef.class).getName());
		++i;
		
		assertTrue(ast.get(i) instanceof WtText);
		assertEquals("Welt", ast.get(i, WtText.class).getContent());
		++i;
		
		assertTrue(ast.get(i) instanceof WtXmlEntityRef);
		assertEquals(">", ast.get(i, WtXmlEntityRef.class).getResolved());
		assertEquals("gt", ast.get(i, WtXmlEntityRef.class).getName());
		++i;
		
		assertTrue(ast.get(i) instanceof WtText);
		assertEquals("!", ast.get(i, WtText.class).getContent());
		++i;
	}
}
