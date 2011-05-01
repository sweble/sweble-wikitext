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

import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.sweble.wikitext.lazy.LinkTargetException;
import org.sweble.wikitext.lazy.utils.WikitextPrinter;

import de.fau.cs.osr.ptk.common.AstPrinter;

public class ParserFunctionTest
        extends
            CompilerTestBase
{
	public ParserFunctionTest() throws FileNotFoundException, IOException
	{
		super();
	}
	
	// =========================================================================
	
	@Test
	@Ignore
	public void testBasePagename() throws CompilerException, LinkTargetException, IOException
	{
		// FIXME: Activate (and fix?) once BASEPAGENAME is fixed.
		String expected = "<DUNNO>";
		String page = "pfn-basepagename";
		doTest(expected, page);
	}
	
	@Test
	public void testFullPagename() throws CompilerException, LinkTargetException, IOException
	{
		String expected = "Pfn-fullpagename";
		String page = "pfn-fullpagename";
		doTest(expected, page);
	}
	
	@Test
	@Ignore
	public void testFullurl() throws CompilerException, LinkTargetException, IOException
	{
		// FIXME: Activate (and fix?) once fullurl is fixed.
		String expected = "http://localhost/wiki/Category:Top_level\nhttp://localhost/w/index.php?title=Category:Top_level&action=edit";
		String page = "pfn-fullurl";
		doTest(expected, page);
	}
	
	@Test
	public void TEMPREG_fullurl() throws CompilerException, LinkTargetException, IOException
	{
		// FIXME: Fix fullurl and then fix this test case
		String expected = "http://localhost/wikiCategory:Top_level\nhttp://localhost/wikiCategory:Top_level&action=edit";
		String page = "pfn-fullurl";
		doTest(expected, page);
	}
	
	@Test
	public void testIf() throws CompilerException, LinkTargetException, IOException
	{
		String expected = "no\nyes\nno\nno\nyes\nyes\nyes\n\n";
		String page = "pfn-if";
		doTest(expected, page);
	}
	
	@Test
	@Ignore
	public void testIfeq() throws CompilerException, LinkTargetException, IOException
	{
		// FIXME: Activate (and fix?) once fullurl is fixed.
		String expected = "<DUNNO>";
		String page = "pfn-ifeq";
		doTest(expected, page);
	}
	
	@Test
	public void TEMPREG_Ifeq() throws CompilerException, LinkTargetException, IOException
	{
		// FIXME: Fix ifeq and then fix this test case
		String expected = "yes\nyes\nyes\nno\nno\nno\nno\nno\n1\nyes\nyes\nno\nyes";
		String page = "pfn-ifeq";
		doTest(expected, page);
	}
	
	@Test
	@Ignore
	public void testIferror() throws CompilerException, LinkTargetException, IOException
	{
		// FIXME: Activate (and fix?) once iferror is fixed.
		String expected = "<DUNNO>";
		String page = "pfn-iferror";
		doTest(expected, page);
	}
	
	@Test
	public void TEMPREG_Iferror() throws CompilerException, LinkTargetException, IOException
	{
		// FIXME: Fix iferror and then fix this test case
		String expected = "correct\ncorrect\n{{#expr: 1 + 2 }}\n{{#expr: 1 + X }}\n{{#expr: 1 + 2 }}\n{{#expr: 1 + X }}‌\nerror";
		String page = "pfn-iferror";
		doTest(expected, page);
	}
	
	@Test
	public void testLc() throws CompilerException, LinkTargetException, IOException
	{
		String expected = "lowercase";
		String page = "pfn-lc";
		doTest(expected, page);
	}
	
	@Test
	public void testNamespace() throws CompilerException, LinkTargetException, IOException
	{
		String namespace = "File";
		String page = "pfn-namespace";
		doTest(namespace, namespace, page);
	}
	
	@Test
	public void testNs() throws CompilerException, LinkTargetException, IOException
	{
		String expected = "Media;Media\nSpecial;Special\n;\nUser;User\nWikipedia;Wikipedia\nFile;File;File\nMediaWiki;MediaWiki\nTemplate;Template\nHelp;Help\nCategory;Category\nTalk;Talk\nUser talk;User talk\nWikipedia talk;Wikipedia talk\nFile talk;File talk;File talk\nMediaWiki talk;MediaWiki talk\nTemplate talk;Template talk\nHelp talk;Help talk\nCategory talk;Category talk";
		String page = "pfn-ns";
		doTest(expected, page);
	}
	
	@Test
	@Ignore
	public void testSubPagename() throws CompilerException, LinkTargetException, IOException
	{
		// FIXME: Activate (and fix?) once SUBPAGENAME is fixed.
		String expected = "<DUNNO>";
		String page = "pfn-subpagename";
		doTest(expected, page);
	}
	
	@Test
	@Ignore
	public void testSwitch() throws CompilerException, LinkTargetException, IOException
	{
		// FIXME: Activate (and fix?) once switch is fixed.
		String expected = "-0---\nBaz\n-1---\nBar\n-2---\n\n-3---\n\n-4---\nBar\n-5---\n\n-6---\nthree\n-7---\none\n-8---\nA\n-9---\nC\n-10--\nNothing\n-11--\nBar\n-12--\nB\n-13--\ntemplate\n-14--";
		String page = "pfn-switch";
		doTest(expected, page);
	}
	
	@Test
	public void TEMPREG_Switch() throws CompilerException, LinkTargetException, IOException
	{
		// FIXME: Fix switch and then fix this test case
		String expected = "-0---\n Baz \n-1---\n\n-2---\n\n-3---\n\n-4---\n Bar \n-5---\n\n-6---\n\n-7---\n\n-8---\n A \n-9---\n\n-10---\n Nothing \n-11---\n Bar \n-12---\n A \n-13---\n\n-14---";
		String page = "pfn-switch";
		doTest(expected, page);
	}
	
	// =========================================================================
	
	private void doTest(String expected, String page) throws LinkTargetException, IOException, CompilerException
	{
		final CompiledPage preprocessed = preprocess(page, false);
		Assert.assertNotNull(preprocessed);
		Assert.assertEquals(
		        expected,
		        WikitextPrinter.print(preprocessed.getPage().getContent()));
	}
	
	@SuppressWarnings("unused")
	private void doTestAst(String expected, String page) throws LinkTargetException, IOException, CompilerException
	{
		final CompiledPage preprocessed = preprocess(page, false);
		Assert.assertNotNull(preprocessed);
		Assert.assertEquals(
		        expected,
		        AstPrinter.print(preprocessed.getPage().getContent()));
	}
	
	private void doTest(String namespace, String expected, String page) throws LinkTargetException, IOException, CompilerException
	{
		final CompiledPage preprocessed = preprocess(namespace, page, false);
		Assert.assertNotNull(preprocessed);
		Assert.assertEquals(
		        expected,
		        WikitextPrinter.print(preprocessed.getPage().getContent()));
	}
}
