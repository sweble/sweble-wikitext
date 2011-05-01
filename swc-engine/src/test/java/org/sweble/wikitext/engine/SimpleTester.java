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

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import joptsimple.OptionException;

import org.apache.commons.io.FileUtils;
import org.sweble.wikitext.engine.utils.HtmlPrinter;
import org.sweble.wikitext.engine.utils.LogPrinter;
import org.sweble.wikitext.lazy.LinkTargetException;
import org.sweble.wikitext.lazy.utils.NodeStats;

import xtc.parser.ParseException;
import de.fau.cs.osr.ptk.common.AstPrinter;
import de.fau.cs.osr.ptk.common.Warning;
import de.fau.cs.osr.utils.getopt.Options;

public class SimpleTester
{
	private final Options options = new Options();
	
	private String page;
	
	private boolean preprocess;
	
	private boolean parse;
	
	private boolean postprocess;
	
	private boolean forInclusion;
	
	private boolean dontFollowRedirect;
	
	private boolean noAst;
	
	// =========================================================================
	
	public static void main(String[] args) throws IOException, ParseException, LinkTargetException, CompilerException
	{
		new SimpleTester().run(args);
	}
	
	// =========================================================================
	
	private void run(String[] args) throws IOException, ParseException, LinkTargetException, CompilerException
	{
		if (!parseCmdLine(args))
			return;
		
		CompilerTestBase c = new CompilerTestBase();
		
		CompiledPage compiled = null;
		if (postprocess)
		{
			if (forInclusion)
				System.err.println("Option --for-inclusion only meaningfull for --preprocess");
			
			compiled = c.postprocess(this.page, forInclusion);
		}
		else if (parse)
		{
			if (forInclusion)
				System.err.println("Option --for-inclusion only meaningfull for --preprocess");
			
			compiled = c.parse(this.page, forInclusion);
		}
		else if (preprocess)
		{
			compiled = c.preprocess(this.page, forInclusion);
		}
		
		if (compiled == null)
		{
			System.err.println("Processing failed!");
			return;
		}
		
		// Output results
		
		NodeStats.process(compiled.getPage());
		
		String filename = URLEncoder.encode(this.page, "UTF-8");
		
		if (!noAst)
		{
			String result = AstPrinter.print(compiled.getPage());
			FileUtils.writeStringToFile(new File(filename + ".ast"), result);
		}
		
		{
			String result = LogPrinter.print(compiled.getLog());
			FileUtils.writeStringToFile(new File(filename + ".log"), result);
		}
		
		{
			String result = HtmlPrinter.print(compiled.getPage(), this.page);
			FileUtils.writeStringToFile(new File(filename + ".html"), result);
		}
		
		{
			StringBuilder b = new StringBuilder();
			for (Warning w : compiled.getWarnings())
			{
				b.append(w.toString());
				b.append('\n');
			}
			
			FileUtils.writeStringToFile(new File(filename + ".warnings"), b.toString());
		}
	}
	
	private boolean parseCmdLine(String[] args)
	{
		options.createOption('h', "help")
		        .withDescription("Print help message.")
		        .create();
		
		options.createOption("for-inclusion")
		        .withDescription("Preprocess for inclusion.")
		        .create();
		
		options.createOption("dont-follow-redirect")
		        .withDescription("Don't follow redirects in pages (Not yet supported).")
		        .create();
		
		options.createOption("preprocess")
		        .withDescription("Preprocess.")
		        .create();
		
		options.createOption("parse")
		        .withDescription("Parse (overrides preprocess).")
		        .create();
		
		options.createOption("postprocess")
		        .withDescription("Postprocess (overrides parse, default).")
		        .create();
		
		options.createOption("no-ast")
		        .withDescription("Don't generate an AST dump.")
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
			
			options.optional("for-inclusion");
			options.optional("dont-follow-redirect");
			options.optional("preprocess");
			options.optional("parse");
			options.optional("postprocess");
			options.optional("no-ast");
			
			options.checkForInvalidOptions();
		}
		catch (OptionException e)
		{
			printArgsErrorMessage(e);
			return false;
		}
		
		if (options.getFreeArguments().isEmpty())
		{
			System.out.println("No sourceFile files given.");
			return false;
		}
		
		page = options.getFreeArguments().get(0);
		
		forInclusion = options.has("for-inclusion");
		
		dontFollowRedirect = options.has("dont-follow-redirect");
		if (dontFollowRedirect)
			System.err.println("Option --dont-follow-redirect not supported yet!");
		
		preprocess = options.has("preprocess");
		
		parse = options.has("parse");
		
		postprocess = options.has("postprocess");
		
		if (!preprocess && !parse)
			postprocess = true;
		
		noAst = options.has("no-ast");
		
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
