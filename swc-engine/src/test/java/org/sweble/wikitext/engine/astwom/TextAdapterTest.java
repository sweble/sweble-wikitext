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
package org.sweble.wikitext.engine.astwom;

import org.junit.Assert;
import org.junit.Test;
import org.sweble.wikitext.engine.CompilerException;
import org.sweble.wikitext.engine.astwom.adapters.BoldAdapter;
import org.sweble.wikitext.engine.utils.SimpleWikiConfiguration;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.engine.wom.WomPage;
import org.sweble.wikitext.engine.wom.WomParagraph;
import org.sweble.wikitext.engine.wom.WomText;
import org.sweble.wikitext.engine.wom.tools.WomPrinter;
import org.sweble.wikitext.lazy.LinkTargetException;
import org.sweble.wikitext.lazy.parser.XmlElement;
import org.sweble.wikitext.lazy.utils.RtWikitextPrinter;

public class TextAdapterTest
{
	@Test
	public void testTextAdapter() throws Exception
	{
		WomPage page = AstWomTestFixture.quickParseToWom("<b>some text</b>");
		
		WomParagraph para = (WomParagraph) page.getBody().getFirstChild();
		
		WomText text = (WomText) para.getFirstChild().getFirstChild();
		
		Assert.assertEquals("text", text.getNodeName());
		
		Assert.assertEquals(WomNodeType.TEXT, text.getNodeType());
		
		// creation of TextAdapter
		
		parse("text", "text", "<text>text</text>");
		
		parse("text\ntext", "text\ntext", "<text>text\ntext</text>");
		
		parse("&", "&", "<text>&amp;</text>");
		
		parse("\uD840\uDC00", "\uD840\uDC00", "<text>\uD840\uDC00</text>");
	}
	
	private void parse(
			String wt,
			String expectedWt,
			String expectedWom) throws Exception
	{
		WomText text = parseGetText(wt);
		
		Assert.assertEquals(text.getText(), text.getValue());
		
		Assert.assertEquals(text.getText(), text.getValue());
		
		compareWikitext(expectedWt, text);
		
		compareWom(expectedWom, text);
	}
	
	// =========================================================================
	
	@Test
	public void testAppend() throws Exception
	{
		// text + text
		
		append("Hallo Welt", "", "Hallo Welt", "<text>Hallo Welt</text>");
		
		append("Hallo Welt", "!", "Hallo Welt!", "<text>Hallo Welt!</text>");
		
		// text + non-text
		
		append("Hallo", " & Welt", "Hallo &amp; Welt", "<text>Hallo &amp; Welt</text>");
		
		append("Hallo", "\nWelt", "Hallo\nWelt", "<text>Hallo\nWelt</text>");
		
		// non-text + text
		
		append("&amp;", " - amp", "&amp; - amp", "<text>&amp; - amp</text>");
		
		append("&#38;", " - amp", "&#38; - amp", "<text>&amp; - amp</text>");
		
		append("\n", " - eol", "\n - eol", "<text>\n - eol</text>");
		
		// non-text + non-text
		
		append("&amp;", " - &", "&amp; - &amp;", "<text>&amp; - &amp;</text>");
		
		append("&#38;", " - &", "&#38; - &amp;", "<text>&amp; - &amp;</text>");
		
		append("\n", " - \n", "\n - \n", "<text>\n - \n</text>");
		
		// text,non-text + non-text,non-text
		
		append("Hallo &amp; ", "Welt", "Hallo &amp; Welt", "<text>Hallo &amp; Welt</text>");
		
		append("Hallo &amp;", "\nWelt", "Hallo &amp;\nWelt", "<text>Hallo &amp;\nWelt</text>");
		
		append("Hallo", " & und &uf Wiedersehen", "Hallo &amp; und &amp;uf Wiedersehen", "<text>Hallo &amp; und &amp;uf Wiedersehen</text>");
		
		append("Hallo", " \uD840\uDC00", "Hallo \uD840\uDC00", "<text>Hallo \uD840\uDC00</text>");
		
		append("Hallo", " \uDBC0\uDC00", "Hallo &#x100000;", "<text>Hallo &#x100000;</text>");
		
		append("Hallo", " \f", "Hallo &#xC;", "<text>Hallo &#xc;</text>");
		
		// make it fail!
		
		append("Hallo\n", " \t\nWelt", UnsupportedOperationException.class);
		
		append("Hallo", "\n\t \n Welt", UnsupportedOperationException.class);
		
		append("Hallo", " \uD840", IllegalArgumentException.class);
		
		append("Hallo", " \uDC00", IllegalArgumentException.class);
		
		// some strange stuff is happening here
		
		append("Hallo", "\r", "Hallo&#xD;", "<text>Hallo&#xd;</text>");
	}
	
	private void append(
			String wt,
			String append,
			String expectedWt,
			String expectedWom) throws Exception
	{
		WomText text = parseGetText(wt);
		
		Assert.assertEquals(text.getText(), text.getValue());
		
		text.appendText(append);
		
		Assert.assertEquals(text.getText(), text.getValue());
		
		compareWikitext(expectedWt, text);
		
		compareWom(expectedWom, text);
	}
	
	private void append(
			String wt,
			String append,
			Class<? extends Exception> eClazz) throws LinkTargetException, CompilerException
	{
		try
		{
			append(wt, append, "who cares", "who cares");
		}
		catch (Exception e)
		{
			if (!eClazz.isInstance(e))
				throw new RuntimeException(
						String.format("Expected exception of type %s, but caught exception of type: %s",
								eClazz.getName(),
								e.getClass().getName()),
						e);
		}
	}
	
	// =========================================================================
	
	@Test
	public void testDelete() throws Exception
	{
		// one part
		
		delete("Hallo Welt", 0, 0, "Hallo Welt", "<text>Hallo Welt</text>");
		
		delete("Hallo Welt", 10, 0, "Hallo Welt", "<text>Hallo Welt</text>");
		
		delete("Hallo Welt", 5, 1, "HalloWelt", "<text>HalloWelt</text>");
		
		delete("Hallo Welt", 0, 6, "Welt", "<text>Welt</text>");
		
		delete("Hallo Welt", 5, 5, "Hallo", "<text>Hallo</text>");
		
		delete("Hallo Welt", 0, 10, "", "<text></text>");
		
		// more parts
		
		delete("Hallo &amp; Welt", 6, 1, "Hallo  Welt", "<text>Hallo  Welt</text>");
		
		delete("Hallo &amp; Welt", 6, 2, "Hallo Welt", "<text>Hallo Welt</text>");
		
		delete("HalloA&amp;BWelt", 5, 3, "HalloWelt", "<text>HalloWelt</text>");
		
		// parts, non-text, ...
		
		delete("&amp; ...", 0, 1, " ...", "<text> ...</text>");
		
		delete("... &amp;", 4, 1, "... ", "<text>... </text>");
		
		delete("Hallo\nWelt\n", 0, 11, "", "<text></text>");
		
		// delete nothing at all
		
		delete("Hallo\nWelt\n", 0, 0, "Hallo\nWelt\n", "<text>Hallo\nWelt\n</text>");
		
		delete("Hallo\nWelt\n", 11, 0, "Hallo\nWelt\n", "<text>Hallo\nWelt\n</text>");
		
		delete("Hallo\nWelt\n", 5, 0, "Hallo\nWelt\n", "<text>Hallo\nWelt\n</text>");
		
		// these shouldn't work
		
		delete("Hallo Welt!", -1, 0, IndexOutOfBoundsException.class);
		
		delete("Hallo Welt!", 12, 0, IndexOutOfBoundsException.class);
		
		delete("Hallo Welt!", 5, -2, IndexOutOfBoundsException.class);
		
		delete("Hallo&#x20000;Welt!", 6, 1, UnsupportedOperationException.class);
		
		delete("Hallo\nWelt\n!", 6, 4, UnsupportedOperationException.class);
	}
	
	private void delete(
			String wt,
			int from,
			int length,
			String expectedWt,
			String expectedWom) throws Exception
	{
		WomText text = parseGetText(wt);
		
		Assert.assertEquals(text.getText(), text.getValue());
		
		text.deleteText(from, length);
		
		Assert.assertEquals(text.getText(), text.getValue());
		
		compareWikitext(expectedWt, text);
		
		compareWom(expectedWom, text);
	}
	
	private void delete(
			String wt,
			int from,
			int length,
			Class<? extends Exception> eClazz)
	{
		try
		{
			delete(wt, from, length, "doesn't matter", "doesn't matter");
		}
		catch (Exception e)
		{
			Assert.assertTrue(eClazz.isInstance(e));
		}
	}
	
	// =========================================================================
	
	@Test
	public void testInsert() throws Exception
	{
		insert("Hallo Welt", 6, "& ", "Hallo &amp; Welt", "<text>Hallo &amp; Welt</text>");
		
		insert("Hallo Welt", 0, "!", "!Hallo Welt", "<text>!Hallo Welt</text>");
		
		insert("Hallo Welt", 10, "!", "Hallo Welt!", "<text>Hallo Welt!</text>");
		
		insert("Hallo Welt", 0, "<", "&lt;Hallo Welt", "<text>&lt;Hallo Welt</text>");
		
		insert("Hallo Welt", 10, ">", "Hallo Welt&gt;", "<text>Hallo Welt&gt;</text>");
		
		insert("Hallo\nWelt\n", 5, " schnoede", "Hallo schnoede\nWelt\n", "<text>Hallo schnoede\nWelt\n</text>");
		
		insert("Hallo\nWelt\n", 8, "ee", "Hallo\nWeeelt\n", "<text>Hallo\nWeeelt\n</text>");
		
		insert("Hallo\nWelt\n", 8, "", "Hallo\nWelt\n", "<text>Hallo\nWelt\n</text>");
		
		insert("Hallo\nWelt\n", 8, "", "Hallo\nWelt\n", "<text>Hallo\nWelt\n</text>");
		
		// these shouldn't work
		
		insert("Hallo Welt", -1, "", IndexOutOfBoundsException.class);
		
		insert("Hallo Welt", 11, "", IndexOutOfBoundsException.class);
		
		insert("Hallo&#x20000;Welt", 6, "x", UnsupportedOperationException.class);
		
		insert("Hallo\nWelt", 5, "-\n", UnsupportedOperationException.class);
	}
	
	private void insert(
			String wt,
			int at,
			String what,
			String expectedWt,
			String expectedWom) throws Exception
	{
		WomText text = parseGetText(wt);
		
		Assert.assertEquals(text.getText(), text.getValue());
		
		text.insertText(at, what);
		
		Assert.assertEquals(text.getText(), text.getValue());
		
		compareWikitext(expectedWt, text);
		
		compareWom(expectedWom, text);
	}
	
	private void insert(
			String wt,
			int at,
			String what,
			Class<? extends Exception> eClazz)
	{
		try
		{
			insert(wt, at, what, "doesn't matter", "doesn't matter");
		}
		catch (Exception e)
		{
			Assert.assertTrue(eClazz.isInstance(e));
		}
	}
	
	// =========================================================================
	
	@Test
	public void testStringReplace() throws Exception
	{
		replace("Hallo Welt!", "Welt!", "Hans!", true, "Hallo Hans!", "<text>Hallo Hans!</text>");
		
		replace("Loop", "Loop", "Loop", true, "Loop", "<text>Loop</text>");
		
		replace("1 2 3 1 2 3", "1", "0", true, "0 2 3 0 2 3", "<text>0 2 3 0 2 3</text>");
		
		replace("Hallo", "allo", "Hallo", true, "HHallo", "<text>HHallo</text>");
		
		replace("Hallo", "ello", "allo", false, "Hallo", "<text>Hallo</text>");
		
		replace("Hallo", "Hallo", "", true, "", "<text></text>");
		
		replace("<\uD840\uDC00>", "\uD840", "Buh!", UnsupportedOperationException.class);
	}
	
	@Test
	public void testIndexReplace() throws Exception
	{
		replace("Hallo Welt!", 6, 5, "Hans!", "Welt!", "Hallo Hans!", "<text>Hallo Hans!</text>");
		
		replace("Loop", 0, 4, "Loop", "Loop", "Loop", "<text>Loop</text>");
		
		replace("Hallo", 1, 4, "Hallo", "allo", "HHallo", "<text>HHallo</text>");
		
		replace("Hallo", 0, 0, "-", "", "-Hallo", "<text>-Hallo</text>");
		
		replace("Hallo", 5, 0, "-", "", "Hallo-", "<text>Hallo-</text>");
		
		replace("Hallo", -1, 0, "-", IndexOutOfBoundsException.class);
		
		replace("Hallo", 6, 0, "-", IndexOutOfBoundsException.class);
	}
	
	private void replace(
			String wt,
			String search,
			String replacement,
			boolean didReplace,
			String expectedWt,
			String expectedWom) throws Exception
	{
		WomText text = parseGetText(wt);
		
		Assert.assertEquals(text.getText(), text.getValue());
		
		Assert.assertEquals(didReplace, text.replaceText(search, replacement));
		
		Assert.assertEquals(text.getText(), text.getValue());
		
		compareWikitext(expectedWt, text);
		
		compareWom(expectedWom, text);
	}
	
	private void replace(
			String wt,
			String search,
			String replacement,
			Class<? extends Exception> eClazz)
	{
		try
		{
			replace(wt, search, replacement, false, "doesn't matter", "doesn't matter");
		}
		catch (Exception e)
		{
			Assert.assertTrue(eClazz.isInstance(e));
		}
	}
	
	private void replace(
			String wt,
			int from,
			int length,
			String replacement,
			String expectedReplacedText,
			String expectedWt,
			String expectedWom) throws Exception
	{
		WomText text = parseGetText(wt);
		
		Assert.assertEquals(text.getText(), text.getValue());
		
		Assert.assertEquals(expectedReplacedText, text.replaceText(from, length, replacement));
		
		Assert.assertEquals(text.getText(), text.getValue());
		
		compareWikitext(expectedWt, text);
		
		compareWom(expectedWom, text);
	}
	
	private void replace(
			String wt,
			int from,
			int length,
			String replacement,
			Class<? extends Exception> eClazz)
	{
		try
		{
			replace(wt, from, length, replacement, "doesn't matter", "doesn't matter", "doesn't matter");
		}
		catch (Exception e)
		{
			Assert.assertTrue(eClazz.isInstance(e));
		}
	}
	
	// =========================================================================
	
	@Test
	public void testTextNodeInsertion() throws Exception
	{
		WomText text = parseGetText("Hello");
		
		SimpleWikiConfiguration config = new AstWomTestFixture().getConfig();
		WomNodeFactory f = new AstWomNodeFactory(config);
		
		text.getParent().appendChild(f.createText(" World!"));
		
		compareWikitext("Hello World!", text);
		
		compareWom("<text>Hello</text><text> World!</text>", text);
	}
	
	// =========================================================================
	
	private WomText parseGetText(String wt) throws Exception
	{
		WomPage page = AstWomTestFixture.quickParseToWom(
				String.format("<b>%s</b>", wt));
		
		WomParagraph para = (WomParagraph) page.getBody().getFirstChild();
		
		return (WomText) para.getFirstChild().getFirstChild();
	}
	
	private void compareWikitext(
			String expectedWt,
			WomNode textNode)
	{
		WomNode womNode = textNode.getParent();
		XmlElement ast = (XmlElement) ((BoldAdapter) womNode).getAstNode();
		String actual = RtWikitextPrinter.print(ast.getBody());
		Assert.assertEquals(expectedWt, actual);
	}
	
	private void compareWom(
			String expectedWom,
			WomNode textNode)
	{
		WomNode womNode = textNode.getParent();
		StringBuffer actual = new StringBuffer();
		for (WomNode n : womNode)
			actual.append(WomPrinter.print(n, true));
		Assert.assertEquals(expectedWom, actual.toString());
	}
}
