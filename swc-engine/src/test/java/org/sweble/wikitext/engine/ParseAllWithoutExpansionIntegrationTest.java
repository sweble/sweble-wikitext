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

package org.sweble.wikitext.engine;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.utils.CompilerTestBase;
import org.sweble.wikitext.parser.parser.Paragraph;
import org.sweble.wikitext.parser.preprocessor.PreproWikitextPage;
import org.sweble.wikitext.parser.preprocessor.TemplateArgument;
import org.sweble.wikitext.parser.preprocessor.TemplateParameter;
import org.sweble.wikitext.parser.AstNodeTypes;
import org.sweble.wikitext.parser.utils.AstPrinter;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.EntityMap;
import de.fau.cs.osr.ptk.common.Warning;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public class ParseAllWithoutExpansionIntegrationTest
{
	private CompilerTestBase ctb;
	
	private Compiler compiler;
	
	private WikiConfigImpl config;
	
	// =========================================================================
	
	@Before
	public void setUp() throws Exception
	{
		ctb = new CompilerTestBase();
		config = ctb.getConfig();
		compiler = ctb.getCompiler();
	}
	
	// =========================================================================
	
	@Test
	public void testParseAndExpandParametersToDefaultValues() throws Exception
	{
		String wikitext = ""
				+ "''' some bold text '''\n"
				+ "{{{1|''italic default value''}}}\n"
				+ "{{some template|{{{2|[[some link]]}}}}}";
		
		PageTitle title = PageTitle.make(config, "-");
		PageId pageId = new PageId(title, -1);
		CompiledPage page = compiler.postprocess(pageId, wikitext, null);
		
		new ParameterToDefaultValueResolver(pageId).go(page);
		
		// We don't want to see the log
		page.setLog(null);
		
		String printedAst = AstPrinter.print(page);
		
		String expected = ""
				+ "CompiledPage(\n"
				+ "  Properties:\n"
				+ "    {N} entityMap = null\n"
				+ "    {N} log = null\n"
				+ "    {N} warnings = []\n"
				+ "\n"
				+ "  Page([\n"
				+ "    Paragraph([\n"
				+ "      Bold(\n"
				+ "        Properties:\n"
				+ "              RTD = RtData: [0] = \"'''\", [1] = \"'''\"\n"
				+ "\n"
				+ "        [ Text(\" some bold text \") ]\n"
				+ "      )\n"
				+ "      Newline(\"\\n\")\n"
				+ "      Italics(\n"
				+ "        Properties:\n"
				+ "              RTD = RtData: [0] = \"''\", [1] = \"''\"\n"
				+ "\n"
				+ "        [ Text(\"italic default value\") ]\n"
				+ "      )\n"
				+ "      Newline(\"\\n\")\n"
				+ "      Template(\n"
				+ "        Properties:\n"
				+ "              RTD = RtData: [0] = \"{{\", [1], [2] = \"}}\"\n"
				+ "          {N} precededByNewline = true\n"
				+ "\n"
				+ "        [ Text(\"some template\") ]\n"
				+ "        [\n"
				+ "          TemplateArgument(\n"
				+ "            Properties:\n"
				+ "                  RTD = RtData: [0] = \"|\", [1], [2]\n"
				+ "              {N} hasName = false\n"
				+ "\n"
				+ "            [ ]\n"
				+ "            [\n"
				+ "              InternalLink(\n"
				+ "                Properties:\n"
				+ "                      RTD = RtData: [0] = \"[[some link\", [1] = \"]]\"\n"
				+ "                  {N} postfix = \"\"\n"
				+ "                  {N} prefix = \"\"\n"
				+ "                  {N} target = \"some link\"\n"
				+ "\n"
				+ "                LinkTitle([ ])\n"
				+ "              )\n"
				+ "            ]\n"
				+ "          )\n"
				+ "        ]\n"
				+ "      )\n"
				+ "    ])\n"
				+ "  ])\n"
				+ ")\n";
		
		assertEquals(expected, printedAst);
	}
	
	// =========================================================================
	
	protected final class ParameterToDefaultValueResolver
			extends
				AstVisitor
	{
		private PageId pageId;
		
		private EntityMap entityMap;
		
		private List<Warning> warnings;
		
		// =====================================================================
		
		public ParameterToDefaultValueResolver(PageId pageId)
		{
			this.pageId = pageId;
		}
		
		// =====================================================================
		
		public AstNode visit(AstNode n)
		{
			mapInPlace(n);
			return n;
		}
		
		public AstNode visit(CompiledPage n)
		{
			this.warnings = n.getWarnings();
			this.entityMap = n.getEntityMap();
			mapInPlace(n);
			return n;
		}
		
		public AstNode visit(TemplateParameter n) throws CompilerException
		{
			TemplateArgument defValArg = n.getDefaultValue();
			if (defValArg == null)
				return n;
			
			NodeList defVal = defValArg.getValue();
			
			// Shortcut for all those empty default values
			if (defVal.isEmpty())
				return defValArg;
			
			PreproWikitextPage pprAst = new PreproWikitextPage(
					defVal, warnings, entityMap);
			
			CompiledPage parsed = compiler.postprocessPpOrExpAst(pageId, pprAst);
			
			NodeList content = parsed.getPage().getContent();
			
			// The parser of course thinks that the given wikitext is a 
			// individual page and will wrap even single line text into a 
			// paragraph node. We try to catch at least simple cases to improve
			// the resulting AST
			if (content.size() == 1 && content.get(0).getNodeType() == AstNodeTypes.NT_PARAGRAPH)
				content = ((Paragraph) content.get(0)).getContent();
			
			return content;
		}
	}
	
}
