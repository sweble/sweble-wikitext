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

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.nodes.WtBody;
import org.sweble.wikitext.parser.nodes.WtEmptyImmutableNode;
import org.sweble.wikitext.parser.nodes.WtLinkOptionGarbage;
import org.sweble.wikitext.parser.nodes.WtNewline;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtPage;
import org.sweble.wikitext.parser.nodes.WtParagraph;
import org.sweble.wikitext.parser.nodes.WtTableCaption;
import org.sweble.wikitext.parser.nodes.WtTableCell;
import org.sweble.wikitext.parser.nodes.WtTableHeader;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtXmlAttributeGarbage;

import xtc.parser.ParseException;
import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ParserInterface;
import de.fau.cs.osr.ptk.common.test.FileCompare;
import de.fau.cs.osr.ptk.common.test.IntegrationTestBase;
import de.fau.cs.osr.ptk.common.test.TestResourcesFixture;

public class WtPrettyPrinterTest
{
	public static void test(
			ParserConfig config,
			IntegrationTestBase<WtNode> test,
			File inputFile,
			String inputSubDir,
			String expectedSubDir,
			TestResourcesFixture resources) throws IOException, ParseException
	{
		// -- pretty print
		
		AstVisitor<WtNode>[] visitors = new AstVisitor[] { new AstCompressor() };
		
		Object ast = test.parse(inputFile, visitors);
		
		String pp = test.printToString(ast, new TypedPrettyPrinter());
		
		// -- generate cleaned ASTs from original and pretty printed wikitext
		
		visitors = new AstVisitor[] {
				new CleanupAst(config),
				new AstCompressor() };
		
		ParserInterface<WtNode> parser = test.instantiateParser();
		for (AstVisitor<WtNode> v : visitors)
			parser.addVisitor(v);
		
		Object cppAst = parser.parseArticle(pp, "pp");
		
		Object cAst = test.parse(inputFile, visitors);
		
		// -- print and compare ASTs
		
		TypedWtAstPrinter astPrinter = new TypedWtAstPrinter();
		
		String cppAstStr = test.printToString(cppAst, astPrinter);
		cppAstStr = compressWhitespace(cppAstStr);
		
		try
		{
			File expectedFile = TestResourcesFixture.rebase(
					inputFile,
					inputSubDir,
					expectedSubDir,
					astPrinter.getPrintoutType(),
					false /* throw if file doesn't exist */);
			
			FileCompare cmp = new FileCompare(resources);
			String actual = cmp.fixActualText(cppAstStr);
			
			cmp.assertEquals(expectedFile, actual);
		}
		catch (IllegalArgumentException e)
		{
			String cAstStr = test.printToString(cAst, astPrinter);
			
			Assert.assertEquals(compressWhitespace(cAstStr), cppAstStr);
		}
	}
	
	private static String compressWhitespace(String text)
	{
		/* Nonsense!
		text = text.replaceAll("[ \\t][ \\t]+", " ");
		text = text.replaceAll("^\\s+?\\n|\\s+?\\n|\\n\\s*?\\n", "\n");
		*/
		return text;
	}
	
	public static final class CleanupAst
			extends
				AstVisitor<WtNode>
	{
		private final ParserConfig config;
		
		public CleanupAst(ParserConfig config)
		{
			this.config = config;
		}
		
		public WtPage visit(WtPage n)
		{
			n.setEntityMap(WtEntityMap.EMPTY_ENTITY_MAP);
			clearNode(n);
			
			mapInPlace(n);
			int last = n.size() - 1;
			if (last >= 0 && n.get(last).isNodeType(WtNode.NT_NEWLINE))
				n.remove(last);
			
			return n;
		}
		
		public Object visit(WtNewline n)
		{
			return REMOVE;
		}
		
		public WtNode visit(WtTableCaption n)
		{
			clearNode(n);
			mapInPlace(n);
			removeParagraph(n.getBody());
			return n;
		}
		
		public WtNode visit(WtTableHeader n)
		{
			clearNode(n);
			mapInPlace(n);
			removeParagraph(n.getBody());
			return n;
		}
		
		public WtNode visit(WtTableCell n)
		{
			clearNode(n);
			mapInPlace(n);
			removeParagraph(n.getBody());
			return n;
		}
		
		private void removeParagraph(WtBody n)
		{
			if (n.isEmpty())
				return;
			
			WtNode first = n.get(0);
			if (first.isNodeType(WtNode.NT_PARAGRAPH))
			{
				n.remove(0);
				n.addAll(0, first);
			}
		}
		
		public Object visit(WtParagraph n)
		{
			WtNodeList l = config.getNodeFactory().list();
			l.exchange(n);
			mapInPlace(l);
			return l;
		}
		
		public Object visit(WtText n)
		{
			clearNode(n);
			n.setContent(n.getContent().trim());
			if (n.getContent().isEmpty())
				return REMOVE;
			return n;
		}
		
		public Object visit(WtLinkOptionGarbage n)
		{
			return REMOVE;
		}
		
		public Object visit(WtXmlAttributeGarbage n)
		{
			return REMOVE;
		}
		
		public WtNode visit(WtNode n)
		{
			if (!(n instanceof WtEmptyImmutableNode))
				clearNode(n);
			
			mapInPlace(n);
			return n;
		}
		
		private void clearNode(WtNode n)
		{
			n.clearRtd();
			n.clearAttributes();
		}
	}
}
