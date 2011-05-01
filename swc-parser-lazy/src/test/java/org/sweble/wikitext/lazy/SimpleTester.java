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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import joptsimple.OptionException;

import org.apache.commons.io.FileUtils;
import org.sweble.wikitext.lazy.parser.LazyParsedPage;
import org.sweble.wikitext.lazy.parser.PreprocessorToParserTransformer;
import org.sweble.wikitext.lazy.preprocessor.LazyPreprocessedPage;
import org.sweble.wikitext.lazy.preprocessor.PreprocessedWikitext;
import org.sweble.wikitext.lazy.utils.NodeStats;
import org.sweble.wikitext.lazy.utils.SimpleParserConfig;

import xtc.parser.ParseException;
import de.fau.cs.osr.ptk.common.AstPrinter;
import de.fau.cs.osr.ptk.common.EntityMap;
import de.fau.cs.osr.ptk.common.ParserCommon;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.utils.getopt.Options;

public class SimpleTester
{
	private final Options options = new Options();
	
	private String baseDir;
	
	private String sourceFile;
	
	private boolean dontPreprocess;
	
	private boolean dontParse;
	
	private boolean dontPostprocess;
	
	private Set<String> postprocessSteps;
	
	private boolean dontValidate;
	
	// =========================================================================
	
	public static void main(String[] args) throws IOException, ParseException, URISyntaxException
	{
		new SimpleTester().run(args);
	}
	
	// =========================================================================
	
	private void run(String[] args) throws IOException, ParseException, URISyntaxException
	{
		if (!parseCmdLine(args))
			return;
		
		String filename = baseDir;
		if (!baseDir.endsWith("/"))
			filename += "/";
		filename += sourceFile;
		
		URL url = getClass().getResource(filename);
		
		if (url == null)
		{
			System.err.println("Cannot find file for article: `" + filename + "'");
			return;
		}
		
		File file = new File(url.toURI());
		if (!file.exists())
		{
			System.err.println("Cannot find file for article: `" + filename + "'");
			return;
		}
		
		String content;
		try
		{
			content = FileUtils.readFileToString(file);
		}
		catch (IOException e)
		{
			System.err.println("Failed to read article: `" + sourceFile + "'");
			e.printStackTrace();
			return;
		}
		
		EntityMap entityMap = new EntityMap();
		
		if (!dontValidate)
		{
			LazyEncodingValidator v = new LazyEncodingValidator();
			
			content = v.validate(content, sourceFile, entityMap);
		}
		
		AstNode showArticle = null;
		
		SimpleParserConfig parserConfig = new SimpleParserConfig();
		
		LazyPreprocessedPage ppArticle = null;
		if (!dontPreprocess)
		{
			ParserCommon pp = new LazyPreprocessor(parserConfig);
			
			ppArticle =
			        (LazyPreprocessedPage) pp.parseArticle(content, sourceFile);
			
			showArticle = ppArticle;
		}
		
		if (!dontParse)
		{
			LazyParser p = new LazyParser(parserConfig);
			
			PreprocessedWikitext ppw = new PreprocessedWikitext(content, entityMap);
			if (!dontPreprocess)
				ppw = PreprocessorToParserTransformer.transform(ppArticle, entityMap);
			
			LazyParsedPage parsedArticle =
			        (LazyParsedPage) p.parseArticle(ppw, sourceFile);
			
			if (!dontPostprocess)
			{
				LazyPostprocessor postp = new LazyPostprocessor(parserConfig);
				
				parsedArticle =
				        (LazyParsedPage) postp.postprocess(parsedArticle, sourceFile);
			}
			
			/* FIXME!
			if (!dontPostprocess)
			{
				if (postprocessSteps.contains("section"))
					parsedArticle = SectionBuilder.process(parsedArticle);
				
				if (postprocessSteps.contains("list"))
					parsedArticle = ListBuilder.process(parsedArticle);
				
				if (postprocessSteps.contains("ticks"))
					parsedArticle = TicksAnalyzer.process(parsedArticle);
				
				if (postprocessSteps.contains("scoped"))
					parsedArticle = ScopedElementBuilder.process(parsedArticle);
				
				if (postprocessSteps.contains("paragraph"))
					parsedArticle = ParagraphBuilder.process(parsedArticle);
			}
			*/

			showArticle = parsedArticle;
		}
		
		{
			NodeStats.process(showArticle);
		}
		
		{
			String result = AstPrinter.print(showArticle);
			FileUtils.writeStringToFile(new File(sourceFile + ".ast"), result);
		}
		
		/*
		{
			String result = HtmlPrinter.print(showArticle, sourceFile);
			FileUtils.writeStringToFile(new File(sourceFile + ".html"), result);
		}
		*/
	}
	
	private boolean parseCmdLine(String[] args)
	{
		options.createOption('h', "help")
		        .withDescription("Print help message.")
		        .create();
		
		options.createOption("base-dir")
		        .withDescription("Base directory for sourceFile files.")
		        .withArgName("DIR")
		        .withRequiredArg()
		        .create();
		
		options.createOption("sourceFile")
		        .withDescription("File to parse.")
		        .withArgName("FILE")
		        .withRequiredArg()
		        .create();
		
		options.createOption("dont-preprocess")
		        .withDescription("Don't preprocess.")
		        .create();
		
		options.createOption("dont-parse")
		        .withDescription("Don't parse.")
		        .create();
		
		options.createOption("dont-postprocess")
		        .withDescription("Don't postprocess.")
		        .create();
		
		try
		{
			options.parse(args);
			
			if (options.has("help"))
			{
				printHelpMessage(options);
				return false;
			}
			
			// ... nothing expected ...
			
			options.optional("base-dir");
			options.optional("sourceFile");
			options.optional("dont-preprocess");
			options.optional("dont-parse");
			options.optional("dont-postprocess");
			
			options.checkForInvalidOptions();
		}
		catch (OptionException e)
		{
			printArgsErrorMessage(e);
			return false;
		}
		
		baseDir = options.value(
		        "base-dir",
		        String.class,
		        "/dropbox");
		
		if (options.getFreeArguments().isEmpty())
		{
			System.out.println("No sourceFile files given.");
			return false;
		}
		
		sourceFile = options.getFreeArguments().get(0);
		
		dontPreprocess = options.has("dont-preprocess");
		
		dontParse = options.has("dont-parse");
		
		dontPostprocess = options.has("dont-postprocess");
		
		postprocessSteps = new HashSet<String>();
		postprocessSteps.addAll(Arrays.asList(options.value(
		        "postprocess-steps",
		        String.class,
		        "section,list,ticks,scoped,paragraph")
		        .toLowerCase().split("\\s*,\\s*")));
		
		if (dontPreprocess && dontParse)
			return false;
		
		return true;
	}
	
	private void printArgsErrorMessage(OptionException e)
	{
		System.err.println(e.getMessage());
		System.err.println("Try `--help' for more information.");
	}
	
	private void printHelpMessage(Options options)
	{
		options.help(System.out);
	}
}
