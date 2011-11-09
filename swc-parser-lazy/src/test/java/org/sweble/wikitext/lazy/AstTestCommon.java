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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xtc.parser.ParseException;
import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.GenericPrinterInterface;
import de.fau.cs.osr.ptk.common.ParserInterface;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.test.FileContent;
import de.fau.cs.osr.ptk.common.test.ParserTestCommon;
import de.fau.cs.osr.ptk.common.test.ParserTestResources;

public class AstTestCommon
		extends
			ParserTestCommon
{
	private final Class<?> parser;
	
	// =========================================================================
	
	/**
	 * @deprecated Unused
	 */
	public AstTestCommon(ParserTestResources resources, Class<?> parserClass)
	{
		super(resources);
		this.parser = parserClass;
	}
	
	public AstTestCommon(
			ParserTestResources resources,
			Class<?> parserClass,
			String noRefReplace,
			String noRefReplaceBy,
			boolean randomRefName)
	{
		super(resources, noRefReplace, noRefReplaceBy, randomRefName);
		this.parser = parserClass;
	}
	
	// =========================================================================
	
	/**
	 * @deprecated Unused
	 */
	public List<String> gatherParseAndPrint(
			String wikitextDir,
			AstVisitor[] visitors,
			GenericPrinterInterface printer) throws IOException, ParseException
	{
		final List<File> input =
				resources.gather(wikitextDir, ".*?\\.wikitext", true);
		
		final ArrayList<String> result = new ArrayList<String>(input.size());
		
		for (File wikitextFile : input)
		{
			result.add(parseAndPrint(visitors, printer, wikitextFile));
		}
		
		return result;
	}
	
	public void gatherParseAndPrintTest(
			String wikitextDir,
			String asttextDir,
			AstVisitor[] visitors,
			GenericPrinterInterface printer) throws IOException, ParseException
	{
		gatherParseAndPrintTest(null, wikitextDir, asttextDir, visitors, printer);
	}
	
	public void gatherParseAndPrintTest(
			String filter,
			String wikitextDir,
			String asttextDir,
			AstVisitor[] visitors,
			GenericPrinterInterface printer) throws IOException, ParseException
	{
		System.out.println();
		System.out.println("Parser & Print test:");
		System.out.println("  Input:      " + wikitextDir);
		System.out.println("  Reference:  " + asttextDir);
		System.out.println("  Printer:    " + printer.getClass().getSimpleName());
		System.out.println();
		
		final List<File> input =
				resources.gather(wikitextDir, ".*?\\.wikitext", true);
		
		for (File wikitextFile : input)
		{
			if (filter != null && !wikitextFile.getName().equalsIgnoreCase(filter))
				continue;
			
			File asttextFile = ParserTestResources.rebase(
					wikitextFile,
					wikitextDir,
					asttextDir,
					printer.getPrintoutType(),
					true /* don't throw if file doesn't exist */);
			
			System.out.println("Testing: " + wikitextDir + wikitextFile.getName());
			parseAndPrintTest(visitors, printer, wikitextFile, asttextFile);
		}
		
		System.out.println();
	}
	
	/**
	 * @deprecated Unused
	 */
	public String parseAndPrint(
			final AstVisitor[] visitors,
			GenericPrinterInterface printer,
			File wikitextFile) throws IOException, ParseException
	{
		FileContent wikitext = new FileContent(wikitextFile);
		
		AstNode ast = parse(wikitext, visitors);
		
		return printToString(ast, printer);
	}
	
	public void parseAndPrintTest(
			final AstVisitor[] visitors,
			GenericPrinterInterface printer,
			File wikitextFile,
			File reftextFile) throws IOException, ParseException
	{
		FileContent wikitext = new FileContent(wikitextFile);
		
		AstNode ast = parse(wikitext, visitors);
		
		printTest(ast, printer, reftextFile);
	}
	
	// =========================================================================
	
	private AstNode parse(FileContent wikitext, AstVisitor[] visitors)
		throws IOException,
		ParseException
	{
		ParserInterface parser = instantiateParser();
		
		if (visitors != null)
			parser.addVisitors(Arrays.asList(visitors));
		
		return parser.parseArticle(
				wikitext.getContent(),
				wikitext.getFile().getAbsolutePath());
	}
	
	private ParserInterface instantiateParser()
	{
		try
		{
			return (ParserInterface) this.parser.newInstance();
		}
		catch (InstantiationException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
}
