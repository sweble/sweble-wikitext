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
import org.sweble.wikitext.engine.config.CompilerConfig;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.log.CompilerLog;
import org.sweble.wikitext.engine.log.ParseException;
import org.sweble.wikitext.engine.log.ParserLog;
import org.sweble.wikitext.engine.log.PostprocessorLog;
import org.sweble.wikitext.engine.log.PpResolverLog;
import org.sweble.wikitext.engine.log.PreprocessorLog;
import org.sweble.wikitext.engine.log.UnhandledException;
import org.sweble.wikitext.engine.log.ValidatorLog;
import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WikitextEncodingValidator;
import org.sweble.wikitext.parser.WikitextParser;
import org.sweble.wikitext.parser.WikitextPostprocessor;
import org.sweble.wikitext.parser.WikitextPreprocessor;
import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.encval.ValidatedWikitext;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.nodes.WtPreproWikitextPage;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtContentNode;
import org.sweble.wikitext.parser.parser.PreprocessorToParserTransformer;
import org.sweble.wikitext.parser.preprocessor.PreprocessedWikitext;

import de.fau.cs.osr.utils.StopWatch;

public class Compiler
{
	private static final Logger logger = Logger.getLogger(Compiler.class);
	
	// =========================================================================
	
	private WikiConfig wikiConfig;
	
	private ParserConfig parserConfig;
	
	private CompilerConfig compilerConfig;
	
	private ExpansionDebugHooks hooks;
	
	private boolean noRedirect = false;
	
	private boolean timingEnabled = false;
	
	private boolean catchAll = true;
	
	// =========================================================================
	
	public Compiler(WikiConfig wikiConfig)
	{
		super();
		this.wikiConfig = wikiConfig;
		this.parserConfig = wikiConfig.getParserConfig();
		this.compilerConfig = wikiConfig.getCompilerConfig();
	}
	
	// =========================================================================
	
	public void setDebugHooks(ExpansionDebugHooks hooks)
	{
		this.hooks = hooks;
	}
	
	public void setNoRedirect(boolean noRedirect)
	{
		this.noRedirect = noRedirect;
	}
	
	public void setTimingEnabled(boolean timingEnabled)
	{
		this.timingEnabled = timingEnabled;
	}
	
	public void setCatchAll(boolean catchAll)
	{
		this.catchAll = catchAll;
	}
	
	public WikiConfig getWikiConfig()
	{
		return wikiConfig;
	}
	
	public ExpansionDebugHooks getDebugHooks()
	{
		return hooks;
	}
	
	public boolean isNoRedirect()
	{
		return noRedirect;
	}
	
	public boolean isTimingEnabled()
	{
		return timingEnabled;
	}
	
	public boolean isCatchAll()
	{
		return catchAll;
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
		log.setTitle(title.getDenormalizedFullTitle());
		log.setRevision(pageId.getRevision());
		
		WtPreproWikitextPage pprAst;
		try
		{
			ValidatedWikitext validatedWikitext =
					validate(title, wikitext, log, null);
			
			WtPreproWikitextPage ppAst =
					preprocess(title, validatedWikitext, forInclusion, log);
			
			pprAst = ppAst;
			if (callback != null)
				pprAst = expand(callback, title, ppAst, null, false, log);
		}
		catch (CompilerException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new CompilerException(title, "Compilation failed!", e, log);
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
		return expand(pageId, wikitext, false, callback);
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
			boolean forInclusion,
			ExpansionCallback callback)
			throws CompilerException
	{
		if (pageId == null || callback == null)
			throw new NullPointerException();
		
		PageTitle title = pageId.getTitle();
		
		CompilerLog log = new CompilerLog();
		log.setTitle(title.getDenormalizedFullTitle());
		log.setRevision(pageId.getRevision());
		
		WtPreproWikitextPage pAst;
		try
		{
			ValidatedWikitext validatedWikitext =
					validate(title, wikitext, log, null);
			
			WtPreproWikitextPage ppAst =
					preprocess(title, validatedWikitext, forInclusion, log);
			
			WtPreproWikitextPage pprAst = ppAst;
			pprAst = expand(callback, title, ppAst, null, forInclusion, log);
			
			pAst = pprAst;
		}
		catch (CompilerException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new CompilerException(title, "Compilation failed!", e, log);
		}
		
		return new CompiledPage(
				new Page(pAst.getContent()),
				pAst.getWarnings(),
				pAst.getEntityMap(),
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
		log.setTitle(title.getDenormalizedFullTitle());
		log.setRevision(pageId.getRevision());
		
		WtParsedWikitextPage pAst;
		try
		{
			ValidatedWikitext validatedWikitext =
					validate(title, wikitext, log, null);
			
			WtPreproWikitextPage ppAst =
					preprocess(title, validatedWikitext, false, log);
			
			WtPreproWikitextPage pprAst = ppAst;
			if (callback != null)
				pprAst = expand(callback, title, ppAst, null, false, log);
			
			pAst = parse(title, pprAst, log);
		}
		catch (CompilerException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new CompilerException(title, "Compilation failed!", e, log);
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
		log.setTitle(title.getDenormalizedFullTitle());
		log.setRevision(pageId.getRevision());
		
		WtParsedWikitextPage pAst;
		try
		{
			ValidatedWikitext validatedWikitext =
					validate(title, wikitext, log, null);
			
			WtPreproWikitextPage ppAst =
					preprocess(title, validatedWikitext, false, log);
			
			WtPreproWikitextPage pprAst = ppAst;
			if (callback != null)
				pprAst = expand(callback, title, ppAst, null, false, log);
			
			pAst = parse(title, pprAst, log);
			
			pAst = postprocess(title, pAst, log);
		}
		catch (CompilerException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new CompilerException(title, "Compilation failed!", e, log);
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
			WtPreproWikitextPage pprAst)
			throws CompilerException
	{
		if (pageId == null)
			throw new NullPointerException();
		
		PageTitle title = pageId.getTitle();
		
		CompilerLog log = new CompilerLog();
		log.setTitle(title.getDenormalizedFullTitle());
		log.setRevision(pageId.getRevision());
		
		WtParsedWikitextPage pAst;
		try
		{
			pAst = parse(title, pprAst, log);
			
			pAst = postprocess(title, pAst, log);
		}
		catch (CompilerException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new CompilerException(title, "Compilation failed!", e, log);
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
			WtEntityMap entityMap,
			Map<String, WtNode> arguments,
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
		log.setTitle(title.getDenormalizedFullTitle());
		log.setRevision(pageId.getRevision());
		
		WtPreproWikitextPage pprAst;
		try
		{
			ValidatedWikitext validatedWikitext =
					validate(title, wikitext, log, entityMap);
			
			WtPreproWikitextPage ppAst =
					preprocess(title, validatedWikitext, forInclusion, log);
			
			pprAst = expand(
					callback,
					title,
					ppAst,
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
			throw new CompilerException(title, "Compilation failed!", e, log);
		}
		
		return new CompiledPage(
				new Page(pprAst.getContent()),
				pprAst.getWarnings(),
				log);
	}
	
	protected CompiledPage expand(
			ExpansionCallback callback,
			PageId pageId,
			WtPreproWikitextPage ppAst,
			WtEntityMap entityMap,
			boolean forInclusion,
			Map<String, WtNode> arguments,
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
		log.setTitle(title.getDenormalizedFullTitle());
		log.setRevision(pageId.getRevision());
		
		WtPreproWikitextPage pprAst;
		try
		{
			pprAst = expand(
					callback,
					title,
					ppAst,
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
			throw new CompilerException(title, "Compilation failed!", e, log);
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
	private ValidatedWikitext validate(
			PageTitle title,
			String wikitext,
			WtContentNode parentLog,
			WtEntityMap entityMap)
			throws CompilerException
	{
		ValidatorLog log = new ValidatorLog();
		parentLog.getContent().add(log);
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		try
		{
			WikitextEncodingValidator validator = new WikitextEncodingValidator();
			
			if (entityMap == null)
				entityMap = new WtEntityMap();
			
			ValidatedWikitext validatedWikitext = validator.validate(
					wikitext,
					title.getDenormalizedFullTitle(),
					entityMap);
			
			return validatedWikitext;
		}
		catch (Exception e)
		{
			logger.error("Validation failed!", e);
			
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.getContent().add(new UnhandledException(e, w.toString()));
			
			throw new CompilerException(title, "Validation failed!", e);
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
	private WtPreproWikitextPage preprocess(
			PageTitle title,
			ValidatedWikitext validatedWikitext,
			boolean forInclusion,
			WtContentNode parentLog)
			throws CompilerException
	{
		PreprocessorLog log = new PreprocessorLog();
		parentLog.getContent().add(log);
		
		log.setForInclusion(forInclusion);
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		try
		{
			WikitextPreprocessor preprocessor = new WikitextPreprocessor(parserConfig);
			
			WtPreproWikitextPage preprocessedAst =
					(WtPreproWikitextPage) preprocessor.parseArticle(
							validatedWikitext,
							title.getDenormalizedFullTitle(),
							forInclusion);
			
			return preprocessedAst;
		}
		catch (xtc.parser.ParseException e)
		{
			log.getContent().add(new ParseException(e.getMessage()));
			
			throw new CompilerException(title, "Preprocessing failed!", e);
		}
		catch (Exception e)
		{
			logger.error("Preprocessing failed!", e);
			
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.getContent().add(new UnhandledException(e, w.toString()));
			
			throw new CompilerException(title, "Preprocessing failed!", e);
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
	private WtPreproWikitextPage expand(
			ExpansionCallback callback,
			PageTitle title,
			WtPreproWikitextPage ppAst,
			LinkedHashMap<String, WtNode> arguments,
			boolean forInclusion,
			WtContentNode parentLog)
			throws CompilerException
	{
		return expand(
				callback,
				title,
				ppAst,
				arguments,
				forInclusion,
				null,
				null,
				parentLog);
	}
	
	/**
	 * Starts the expansion process of a preprocessed page.
	 */
	private WtPreproWikitextPage expand(
			ExpansionCallback callback,
			PageTitle title,
			WtPreproWikitextPage ppAst,
			Map<String, WtNode> arguments,
			boolean forInclusion,
			ExpansionFrame rootFrame,
			ExpansionFrame parentFrame,
			WtContentNode parentLog)
			throws CompilerException
	{
		PpResolverLog log = new PpResolverLog();
		parentLog.getContent().add(log);
		
		if (arguments == null)
			arguments = new HashMap<String, WtNode>();
		
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
						hooks,
						title,
						ppAst.getEntityMap(),
						arguments,
						forInclusion,
						noRedirect,
						rootFrame,
						parentFrame,
						ppAst.getWarnings(),
						log,
						timingEnabled,
						catchAll);
			}
			else
			{
				frame = new ExpansionFrame(
						this,
						callback,
						hooks,
						title,
						ppAst.getEntityMap(),
						noRedirect,
						ppAst.getWarnings(),
						log,
						timingEnabled,
						catchAll);
			}
			
			WtPreproWikitextPage expanded =
					(WtPreproWikitextPage) frame.expand(ppAst);
			
			return expanded;
		}
		catch (Exception e)
		{
			logger.error("Resolution failed!", e);
			
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.getContent().add(new UnhandledException(e, w.toString()));
			
			throw new CompilerException(title, "Resolution failed!", e);
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
	private WtParsedWikitextPage parse(
			PageTitle title,
			WtPreproWikitextPage ppAst,
			WtContentNode parentLog)
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
							compilerConfig.isTrimTransparentBeforeParsing());
			
			WikitextParser parser = new WikitextParser(parserConfig);
			
			WtParsedWikitextPage parsedAst =
					(WtParsedWikitextPage) parser.parseArticle(
							preprocessedWikitext,
							title.getTitle());
			
			parsedAst.getWarnings().addAll(ppAst.getWarnings());
			
			return parsedAst;
		}
		catch (xtc.parser.ParseException e)
		{
			log.getContent().add(new ParseException(e.getMessage()));
			
			throw new CompilerException(title, "Parsing failed!", e);
		}
		catch (Exception e)
		{
			logger.error("Parsing failed!", e);
			
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.getContent().add(new UnhandledException(e, w.toString()));
			
			throw new CompilerException(title, "Parsing failed!", e);
		}
		finally
		{
			stopWatch.stop();
			log.setTimeNeeded(stopWatch.getElapsedTime());
		}
	}
	
	private WtParsedWikitextPage postprocess(
			PageTitle title,
			WtParsedWikitextPage pAst,
			CompilerLog parentLog)
			throws CompilerException
	{
		PostprocessorLog log = new PostprocessorLog();
		parentLog.getContent().add(log);
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		try
		{
			WikitextPostprocessor lpp = new WikitextPostprocessor(parserConfig);
			
			pAst = (WtParsedWikitextPage) lpp.postprocess(pAst, title.getTitle());
			
			return pAst;
		}
		catch (Exception e)
		{
			logger.error("Postprocessing failed!", e);
			
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.getContent().add(new UnhandledException(e, w.toString()));
			
			throw new CompilerException(title, "Postprocessing failed!", e);
		}
		finally
		{
			stopWatch.stop();
			log.setTimeNeeded(stopWatch.getElapsedTime());
		}
	}
}
