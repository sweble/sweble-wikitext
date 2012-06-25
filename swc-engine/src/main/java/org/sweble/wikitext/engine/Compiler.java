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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.sweble.wikitext.engine.config.WikiConfigurationInterface;
import org.sweble.wikitext.engine.log.CompilerLog;
import org.sweble.wikitext.engine.log.ParseException;
import org.sweble.wikitext.engine.log.ParserLog;
import org.sweble.wikitext.engine.log.PostprocessorLog;
import org.sweble.wikitext.engine.log.PpResolverLog;
import org.sweble.wikitext.engine.log.PreprocessorLog;
import org.sweble.wikitext.engine.log.UnhandledException;
import org.sweble.wikitext.engine.log.ValidatorLog;
import org.sweble.wikitext.lazy.LazyEncodingValidator;
import org.sweble.wikitext.lazy.LazyParser;
import org.sweble.wikitext.lazy.LazyPostprocessor;
import org.sweble.wikitext.lazy.LazyPreprocessor;
import org.sweble.wikitext.lazy.parser.LazyParsedPage;
import org.sweble.wikitext.lazy.parser.PreprocessorToParserTransformer;
import org.sweble.wikitext.lazy.preprocessor.LazyPreprocessedPage;
import org.sweble.wikitext.lazy.preprocessor.PreprocessedWikitext;

import de.fau.cs.osr.ptk.common.EntityMap;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.ContentNode;
import de.fau.cs.osr.utils.StopWatch;

public class Compiler
{
	private static final Logger logger = Logger.getLogger(Compiler.class);
	
	// =========================================================================
	
	private WikiConfigurationInterface wikiConfig;
	
	// =========================================================================
	
	public Compiler(WikiConfigurationInterface wikiConfig)
	{
		super();
		this.wikiConfig = wikiConfig;
	}
	
	public WikiConfigurationInterface getWikiConfig()
	{
		return wikiConfig;
	}
	
	// =========================================================================
	
	/**
	 * Takes wikitext and preprocesses the wikitext (without performing
	 * expansion). The following steps are performed:
	 * <ul>
	 * <li>Validation</li>
	 * <li>Preprocessing (for inclusion/viewing)</li>
	 * <li>Entity substitution</li>
	 * <li>Optional: Expansion</li>
	 * </ul>
	 */
	public CompiledPage preprocess(
			PageId pageId,
			String wikitext,
			boolean forInclusion,
			ExpansionCallback callback)
			throws CompilerException
	{
		if (pageId == null)
			throw new NullPointerException();
		
		PageTitle title = pageId.getTitle();
		
		CompilerLog log = new CompilerLog();
		log.setTitle(title.getFullTitle());
		log.setRevision(pageId.getRevision());
		
		LazyPreprocessedPage pprAst;
		try
		{
			EntityMap entityMap = new EntityMap();
			
			String validatedWikitext =
					validate(title, wikitext, entityMap, log);
			
			LazyPreprocessedPage ppAst =
					preprocess(title, validatedWikitext, forInclusion, entityMap, log);
			
			pprAst = ppAst;
			if (callback != null)
				pprAst = expand(callback, title, ppAst, entityMap, null, false, log);
		}
		catch (CompilerException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new CompilerException("Compilation failed!", e, log);
		}
		
		return new CompiledPage(
				new Page(pprAst.getContent()),
				pprAst.getWarnings(),
				log);
	}
	
	/**
	 * Takes wikitext and expands the wikitext. The following steps are
	 * performed:
	 * <ul>
	 * <li>Validation</li>
	 * <li>Preprocessing (for viewing)</li>
	 * <li>Entity substitution</li>
	 * <li>Expansion</li>
	 * </ul>
	 */
	public CompiledPage expand(
			PageId pageId,
			String wikitext,
			ExpansionCallback callback)
			throws CompilerException
	{
		if (pageId == null || callback == null)
			throw new NullPointerException();
		
		PageTitle title = pageId.getTitle();
		
		CompilerLog log = new CompilerLog();
		log.setTitle(title.getFullTitle());
		log.setRevision(pageId.getRevision());
		
		LazyPreprocessedPage pAst;
		try
		{
			EntityMap entityMap = new EntityMap();
			
			String validatedWikitext =
					validate(title, wikitext, entityMap, log);
			
			LazyPreprocessedPage ppAst =
					preprocess(title, validatedWikitext, false, entityMap, log);
			
			LazyPreprocessedPage pprAst = ppAst;
			pprAst = expand(callback, title, ppAst, entityMap, null, false, log);
			
			pAst = pprAst;
		}
		catch (CompilerException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new CompilerException("Compilation failed!", e, log);
		}
		
		return new CompiledPage(
				new Page(pAst.getContent()),
				pAst.getWarnings(),
				log);
	}
	
	/**
	 * Takes wikitext and parses the wikitext for viewing. The following steps
	 * are performed:
	 * <ul>
	 * <li>Validation</li>
	 * <li>Preprocessing (for viewing)</li>
	 * <li>Entity substitution</li>
	 * <li>Optional: Expansion</li>
	 * <li>Parsing</li>
	 * <li>Entity substitution</li>
	 * </ul>
	 */
	public CompiledPage parse(
			PageId pageId,
			String wikitext,
			ExpansionCallback callback)
			throws CompilerException
	{
		if (pageId == null)
			throw new NullPointerException();
		
		PageTitle title = pageId.getTitle();
		
		CompilerLog log = new CompilerLog();
		log.setTitle(title.getFullTitle());
		log.setRevision(pageId.getRevision());
		
		LazyParsedPage pAst;
		try
		{
			EntityMap entityMap = new EntityMap();
			
			String validatedWikitext =
					validate(title, wikitext, entityMap, log);
			
			LazyPreprocessedPage ppAst =
					preprocess(title, validatedWikitext, false, entityMap, log);
			
			LazyPreprocessedPage pprAst = ppAst;
			if (callback != null)
				pprAst = expand(callback, title, ppAst, entityMap, null, false, log);
			
			pAst = parse(title, pprAst, entityMap, log);
		}
		catch (CompilerException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new CompilerException("Compilation failed!", e, log);
		}
		
		return new CompiledPage(
				new Page(pAst.getContent()),
				pAst.getWarnings(),
				log);
	}
	
	/**
	 * Takes wikitext and parses the wikitext for viewing. The following steps
	 * are performed:
	 * <ul>
	 * <li>Validation</li>
	 * <li>Preprocessing (for viewing)</li>
	 * <li>Entity substitution</li>
	 * <li>Optional: Expansion</li>
	 * <li>Parsing</li>
	 * <li>Entity substitution</li>
	 * <li>Postprocessing</li>
	 * </ul>
	 */
	public CompiledPage postprocess(
			PageId pageId,
			String wikitext,
			ExpansionCallback callback)
			throws CompilerException
	{
		if (pageId == null)
			throw new NullPointerException();
		
		PageTitle title = pageId.getTitle();
		
		CompilerLog log = new CompilerLog();
		log.setTitle(title.getFullTitle());
		log.setRevision(pageId.getRevision());
		
		LazyParsedPage pAst;
		try
		{
			EntityMap entityMap = new EntityMap();
			
			String validatedWikitext =
					validate(title, wikitext, entityMap, log);
			
			LazyPreprocessedPage ppAst =
					preprocess(title, validatedWikitext, false, entityMap, log);
			
			LazyPreprocessedPage pprAst = ppAst;
			if (callback != null)
				pprAst = expand(callback, title, ppAst, entityMap, null, false, log);
			
			pAst = parse(title, pprAst, entityMap, log);
			
			pAst = postprocess(title, pAst, log);
		}
		catch (CompilerException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new CompilerException("Compilation failed!", e, log);
		}
		
		return new CompiledPage(
				new Page(pAst.getContent()),
				pAst.getWarnings(),
				log);
	}
	
	/**
	 * Takes an AST after preprocessing or after expansion and performs the
	 * following steps:
	 * <ul>
	 * <li>Parsing</li>
	 * <li>Entity substitution</li>
	 * <li>Postprocessing</li>
	 * </ul>
	 */
	public CompiledPage postprocessPpOrExpAst(
			PageId pageId,
			LazyPreprocessedPage pprAst,
			EntityMap entityMap)
			throws CompilerException
	{
		if (pageId == null)
			throw new NullPointerException();
		
		PageTitle title = pageId.getTitle();
		
		CompilerLog log = new CompilerLog();
		log.setTitle(title.getFullTitle());
		log.setRevision(pageId.getRevision());
		
		LazyParsedPage pAst;
		try
		{
			pAst = parse(title, pprAst, entityMap, log);
			
			pAst = postprocess(title, pAst, log);
		}
		catch (CompilerException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new CompilerException("Compilation failed!", e, log);
		}
		
		return new CompiledPage(
				new Page(pAst.getContent()),
				pAst.getWarnings(),
				log);
	}
	
	// =========================================================================
	
	/**
	 * This function is only called by preprocessor frames to pull in pages for
	 * transclusion or redirection. It takes wikitext and parses the wikitext
	 * for inclusion or viewing. The following steps are performed:
	 * <ul>
	 * <li>Validation</li>
	 * <li>Preprocessing (for inclusion/viewing)</li>
	 * <li>Entity substitution</li>
	 * <li>Expansion</li>
	 * </ul>
	 */
	protected CompiledPage preprocessAndExpand(
			ExpansionCallback callback,
			PageId pageId,
			String wikitext,
			boolean forInclusion,
			EntityMap entityMap,
			Map<String, AstNode> arguments,
			ExpansionFrame rootFrame,
			ExpansionFrame parentFrame)
			throws CompilerException
	{
		if (pageId == null)
			throw new NullPointerException();
		
		if (wikitext == null)
			throw new NullPointerException();
		
		PageTitle title = pageId.getTitle();
		
		CompilerLog log = new CompilerLog();
		log.setTitle(title.getFullTitle());
		log.setRevision(pageId.getRevision());
		
		LazyPreprocessedPage pprAst;
		try
		{
			String validatedWikitext =
					validate(title, wikitext, entityMap, log);
			
			LazyPreprocessedPage ppAst =
					preprocess(title, validatedWikitext, forInclusion, entityMap, log);
			
			pprAst = expand(
					callback,
					title,
					ppAst,
					entityMap,
					arguments,
					forInclusion,
					rootFrame,
					parentFrame,
					log);
		}
		catch (CompilerException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new CompilerException("Compilation failed!", e, log);
		}
		
		return new CompiledPage(
				new Page(pprAst.getContent()),
				pprAst.getWarnings(),
				log);
	}
	
	protected CompiledPage expand(
			ExpansionCallback callback,
			PageId pageId,
			LazyPreprocessedPage ppAst,
			EntityMap entityMap,
			boolean forInclusion,
			Map<String, AstNode> arguments,
			ExpansionFrame rootFrame,
			ExpansionFrame parentFrame)
			throws CompilerException
	{
		if (pageId == null)
			throw new NullPointerException();
		
		if (ppAst == null)
			throw new NullPointerException();
		
		PageTitle title = pageId.getTitle();
		
		CompilerLog log = new CompilerLog();
		log.setTitle(title.getFullTitle());
		log.setRevision(pageId.getRevision());
		
		LazyPreprocessedPage pprAst;
		try
		{
			pprAst = expand(
					callback,
					title,
					ppAst,
					entityMap,
					arguments,
					forInclusion,
					rootFrame,
					parentFrame,
					log);
		}
		catch (CompilerException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new CompilerException("Compilation failed!", e, log);
		}
		
		return new CompiledPage(
				new Page(pprAst.getContent()),
				pprAst.getWarnings(),
				log);
	}
	
	// =========================================================================
	
	/**
	 * Validates wikitext.
	 */
	private String validate(
			PageTitle title,
			String wikitext,
			EntityMap entityMap,
			ContentNode parentLog)
			throws CompilerException
	{
		ValidatorLog log = new ValidatorLog();
		parentLog.getContent().add(log);
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		try
		{
			LazyEncodingValidator validator = new LazyEncodingValidator();
			
			String validatedWikitext = validator.validate(
					wikitext,
					title.getFullTitle(),
					entityMap);
			
			return validatedWikitext;
		}
		catch (Throwable e)
		{
			logger.error("Validation failed!", e);
			
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.getContent().add(new UnhandledException(e, w.toString()));
			
			throw new CompilerException("Validation failed!", e);
		}
		finally
		{
			stopWatch.stop();
			log.setTimeNeeded(stopWatch.getElapsedTime());
		}
	}
	
	/**
	 * Preprocesses validated wikitext and substitutes entities.
	 */
	private LazyPreprocessedPage preprocess(
			PageTitle title,
			String validatedWikitext,
			boolean forInclusion,
			EntityMap entityMap,
			ContentNode parentLog)
			throws CompilerException
	{
		PreprocessorLog log = new PreprocessorLog();
		parentLog.getContent().add(log);
		
		log.setForInclusion(forInclusion);
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		try
		{
			LazyPreprocessor preprocessor = new LazyPreprocessor(wikiConfig);
			
			LazyPreprocessedPage preprocessedAst =
					(LazyPreprocessedPage) preprocessor.parseArticle(
							validatedWikitext,
							title.getFullTitle(),
							forInclusion);
			
			return preprocessedAst;
		}
		catch (xtc.parser.ParseException e)
		{
			log.getContent().add(new ParseException(e.getMessage()));
			
			throw new CompilerException("Preprocessing failed!", e);
		}
		catch (Throwable e)
		{
			logger.error("Preprocessing failed!", e);
			
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.getContent().add(new UnhandledException(e, w.toString()));
			
			throw new CompilerException("Preprocessing failed!", e);
		}
		finally
		{
			stopWatch.stop();
			log.setTimeNeeded(stopWatch.getElapsedTime());
		}
	}
	
	/**
	 * Starts the expansion process of a preprocessed page with the preprocessed
	 * page as root of the expansion process.
	 */
	private LazyPreprocessedPage expand(
			ExpansionCallback callback,
			PageTitle title,
			LazyPreprocessedPage ppAst,
			EntityMap entityMap,
			LinkedHashMap<String, AstNode> arguments,
			boolean forInclusion,
			ContentNode parentLog)
			throws CompilerException
	{
		return expand(
				callback,
				title,
				ppAst,
				entityMap,
				arguments,
				forInclusion,
				null,
				null,
				parentLog);
	}
	
	/**
	 * Starts the expansion process of a preprocessed page.
	 */
	private LazyPreprocessedPage expand(
			ExpansionCallback callback,
			PageTitle title,
			LazyPreprocessedPage ppAst,
			EntityMap entityMap,
			Map<String, AstNode> arguments,
			boolean forInclusion,
			ExpansionFrame rootFrame,
			ExpansionFrame parentFrame,
			ContentNode parentLog)
			throws CompilerException
	{
		PpResolverLog log = new PpResolverLog();
		parentLog.getContent().add(log);
		
		if (arguments == null)
			arguments = new HashMap<String, AstNode>();
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		try
		{
			ExpansionFrame frame;
			if (rootFrame != null)
			{
				frame = new ExpansionFrame(
						this,
						callback,
						title,
						entityMap,
						arguments,
						forInclusion,
						rootFrame,
						parentFrame,
						ppAst.getWarnings(),
						log);
			}
			else
			{
				frame = new ExpansionFrame(
						this,
						callback,
						title,
						entityMap,
						ppAst.getWarnings(),
						log);
			}
			
			LazyPreprocessedPage expanded =
					(LazyPreprocessedPage) frame.expand(ppAst);
			
			return expanded;
		}
		catch (Throwable e)
		{
			logger.error("Resolution failed!", e);
			
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.getContent().add(new UnhandledException(e, w.toString()));
			
			throw new CompilerException("Resolution failed!", e);
		}
		finally
		{
			stopWatch.stop();
			log.setTimeNeeded(stopWatch.getElapsedTime());
		}
	}
	
	/**
	 * Parses a preprocessed page and substitutes entities.
	 */
	private LazyParsedPage parse(
			PageTitle title,
			LazyPreprocessedPage ppAst,
			EntityMap entityMap,
			ContentNode parentLog)
			throws CompilerException
	{
		ParserLog log = new ParserLog();
		parentLog.getContent().add(log);
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		try
		{
			PreprocessedWikitext preprocessedWikitext =
					PreprocessorToParserTransformer.transform(
							ppAst,
							entityMap,
							true);
			
			LazyParser parser = new LazyParser(wikiConfig);
			
			LazyParsedPage parsedAst =
					(LazyParsedPage) parser.parseArticle(
							preprocessedWikitext,
							title.getTitle());
			
			parsedAst.getWarnings().addAll(ppAst.getWarnings());
			
			return parsedAst;
		}
		catch (xtc.parser.ParseException e)
		{
			log.getContent().add(new ParseException(e.getMessage()));
			
			throw new CompilerException("Parsing failed!", e);
		}
		catch (Throwable e)
		{
			logger.error("Parsing failed!", e);
			
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.getContent().add(new UnhandledException(e, w.toString()));
			
			throw new CompilerException("Parsing failed!", e);
		}
		finally
		{
			stopWatch.stop();
			log.setTimeNeeded(stopWatch.getElapsedTime());
		}
	}
	
	private LazyParsedPage postprocess(
			PageTitle title,
			LazyParsedPage pAst,
			CompilerLog parentLog)
			throws CompilerException
	{
		PostprocessorLog log = new PostprocessorLog();
		parentLog.getContent().add(log);
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		try
		{
			LazyPostprocessor lpp = new LazyPostprocessor(wikiConfig);
			
			pAst = (LazyParsedPage) lpp.postprocess(pAst, title.getTitle());
			
			return pAst;
		}
		catch (Throwable e)
		{
			logger.error("Postprocessing failed!", e);
			
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.getContent().add(new UnhandledException(e, w.toString()));
			
			throw new CompilerException("Postprocessing failed!", e);
		}
		finally
		{
			stopWatch.stop();
			log.setTimeNeeded(stopWatch.getElapsedTime());
		}
	}
}
