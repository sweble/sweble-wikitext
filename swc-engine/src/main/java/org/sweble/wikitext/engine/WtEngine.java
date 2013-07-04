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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.sweble.wikitext.engine.config.EngineConfig;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.lognodes.EngineLog;
import org.sweble.wikitext.engine.lognodes.LogContainer;
import org.sweble.wikitext.engine.lognodes.ParseException;
import org.sweble.wikitext.engine.lognodes.ParserLog;
import org.sweble.wikitext.engine.lognodes.PostprocessorLog;
import org.sweble.wikitext.engine.lognodes.PpResolverLog;
import org.sweble.wikitext.engine.lognodes.PreprocessorLog;
import org.sweble.wikitext.engine.lognodes.UnhandledException;
import org.sweble.wikitext.engine.lognodes.ValidatorLog;
import org.sweble.wikitext.engine.nodes.EngCompiledPage;
import org.sweble.wikitext.engine.nodes.EngineNodeFactory;
import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WikitextEncodingValidator;
import org.sweble.wikitext.parser.WikitextParser;
import org.sweble.wikitext.parser.WikitextPostprocessor;
import org.sweble.wikitext.parser.WikitextPreprocessor;
import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.WtEntityMapImpl;
import org.sweble.wikitext.parser.encval.ValidatedWikitext;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.nodes.WtPreproWikitextPage;
import org.sweble.wikitext.parser.parser.PreprocessorToParserTransformer;
import org.sweble.wikitext.parser.preprocessor.PreprocessedWikitext;

import de.fau.cs.osr.ptk.common.Warning;
import de.fau.cs.osr.utils.StopWatch;

public class WtEngine
		implements
			IWtEngine
{
	private static final Logger logger = Logger.getLogger(WtEngine.class);
	
	// =========================================================================
	
	private WikiConfig wikiConfig;
	
	private ParserConfig parserConfig;
	
	private EngineConfig engineConfig;
	
	private ExpansionDebugHooks hooks;
	
	private boolean noRedirect = false;
	
	private boolean timingEnabled = false;
	
	private boolean catchAll = true;
	
	// =========================================================================
	
	public WtEngine(WikiConfig wikiConfig)
	{
		super();
		this.wikiConfig = wikiConfig;
		this.parserConfig = wikiConfig.getParserConfig();
		this.engineConfig = wikiConfig.getEngineConfig();
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
	
	public EngineNodeFactory nf()
	{
		return wikiConfig.getNodeFactory();
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
	public EngCompiledPage preprocess(
			PageId pageId,
			String wikitext,
			boolean forInclusion,
			ExpansionCallback callback)
			throws EngineException
	{
		if (pageId == null)
			throw new NullPointerException();
		
		PageTitle title = pageId.getTitle();
		
		EngineLog log = new EngineLog();
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
		catch (EngineException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new EngineException(title, "Compilation failed!", e, log);
		}
		
		return nf().compiledPage(
				nf().page(pprAst),
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
	public EngCompiledPage expand(
			PageId pageId,
			String wikitext,
			ExpansionCallback callback)
			throws EngineException
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
	public EngCompiledPage expand(
			PageId pageId,
			String wikitext,
			boolean forInclusion,
			ExpansionCallback callback)
			throws EngineException
	{
		if (pageId == null || callback == null)
			throw new NullPointerException();
		
		PageTitle title = pageId.getTitle();
		
		EngineLog log = new EngineLog();
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
		catch (EngineException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new EngineException(title, "Compilation failed!", e, log);
		}
		
		return nf().compiledPage(
				nf().page(pAst),
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
	public EngCompiledPage parse(
			PageId pageId,
			String wikitext,
			ExpansionCallback callback)
			throws EngineException
	{
		if (pageId == null)
			throw new NullPointerException();
		
		PageTitle title = pageId.getTitle();
		
		EngineLog log = new EngineLog();
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
		catch (EngineException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new EngineException(title, "Compilation failed!", e, log);
		}
		
		return nf().compiledPage(
				nf().page(pAst),
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
	public EngCompiledPage postprocess(
			PageId pageId,
			String wikitext,
			ExpansionCallback callback)
			throws EngineException
	{
		if (pageId == null)
			throw new NullPointerException();
		
		PageTitle title = pageId.getTitle();
		
		EngineLog log = new EngineLog();
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
		catch (EngineException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new EngineException(title, "Compilation failed!", e, log);
		}
		
		return nf().compiledPage(
				nf().page(pAst),
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
	public EngCompiledPage postprocessPpOrExpAst(
			PageId pageId,
			WtPreproWikitextPage pprAst)
			throws EngineException
	{
		if (pageId == null)
			throw new NullPointerException();
		
		PageTitle title = pageId.getTitle();
		
		EngineLog log = new EngineLog();
		log.setTitle(title.getDenormalizedFullTitle());
		log.setRevision(pageId.getRevision());
		
		WtParsedWikitextPage pAst;
		try
		{
			pAst = parse(title, pprAst, log);
			
			pAst = postprocess(title, pAst, log);
		}
		catch (EngineException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new EngineException(title, "Compilation failed!", e, log);
		}
		
		return nf().compiledPage(
				nf().page(pAst),
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
	protected EngCompiledPage preprocessAndExpand(
			ExpansionCallback callback,
			PageId pageId,
			String wikitext,
			boolean forInclusion,
			WtEntityMap entityMap,
			Map<String, WtNodeList> arguments,
			ExpansionFrame rootFrame,
			ExpansionFrame parentFrame)
			throws EngineException
	{
		if (pageId == null)
			throw new NullPointerException();
		
		if (wikitext == null)
			throw new NullPointerException();
		
		PageTitle title = pageId.getTitle();
		
		EngineLog log = new EngineLog();
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
		catch (EngineException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new EngineException(title, "Compilation failed!", e, log);
		}
		
		return nf().compiledPage(
				nf().page(pprAst),
				pprAst.getWarnings(),
				log);
	}
	
	protected EngCompiledPage expand(
			ExpansionCallback callback,
			PageId pageId,
			WtPreproWikitextPage ppAst,
			WtEntityMap entityMap,
			boolean forInclusion,
			Map<String, WtNodeList> arguments,
			ExpansionFrame rootFrame,
			ExpansionFrame parentFrame)
			throws EngineException
	{
		if (pageId == null)
			throw new NullPointerException();
		
		if (ppAst == null)
			throw new NullPointerException();
		
		PageTitle title = pageId.getTitle();
		
		EngineLog log = new EngineLog();
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
		catch (EngineException e)
		{
			e.attachLog(log);
			throw e;
		}
		catch (Throwable e)
		{
			throw new EngineException(title, "Compilation failed!", e, log);
		}
		
		return nf().compiledPage(
				nf().page(pprAst),
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
			LogContainer parentLog,
			WtEntityMap entityMap)
			throws EngineException
	{
		ValidatorLog log = new ValidatorLog();
		parentLog.add(log);
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		try
		{
			WikitextEncodingValidator validator = new WikitextEncodingValidator();
			
			if (entityMap == null)
				entityMap = new WtEntityMapImpl();
			
			ValidatedWikitext validatedWikitext = validator.validate(
					parserConfig,
					entityMap,
					title.getDenormalizedFullTitle(),
					wikitext);
			
			return validatedWikitext;
		}
		catch (Exception e)
		{
			logger.error("Validation failed!", e);
			
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.add(new UnhandledException(e, w.toString()));
			
			throw new EngineException(title, "Validation failed!", e);
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
			LogContainer parentLog)
			throws EngineException
	{
		PreprocessorLog log = new PreprocessorLog();
		parentLog.add(log);
		
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
			log.add(new ParseException(e.getMessage()));
			
			throw new EngineException(title, "Preprocessing failed!", e);
		}
		catch (Exception e)
		{
			logger.error("Preprocessing failed!", e);
			
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.add(new UnhandledException(e, w.toString()));
			
			throw new EngineException(title, "Preprocessing failed!", e);
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
			LinkedHashMap<String, WtNodeList> arguments,
			boolean forInclusion,
			LogContainer parentLog)
			throws EngineException
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
			Map<String, WtNodeList> arguments,
			boolean forInclusion,
			ExpansionFrame rootFrame,
			ExpansionFrame parentFrame,
			LogContainer parentLog)
			throws EngineException
	{
		PpResolverLog log = new PpResolverLog();
		parentLog.add(log);
		
		if (arguments == null)
			arguments = new HashMap<String, WtNodeList>();
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		try
		{
			// Copy in case ppAst stores an immutable (empty) warning list.
			List<Warning> warnings =
					new LinkedList<Warning>(ppAst.getWarnings());
			
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
						warnings,
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
						warnings,
						log,
						timingEnabled,
						catchAll);
			}
			
			WtPreproWikitextPage expanded =
					(WtPreproWikitextPage) frame.expand(ppAst);
			
			if (!warnings.isEmpty())
				ppAst.setWarnings(warnings);
			
			return expanded;
		}
		catch (Exception e)
		{
			logger.error("Resolution failed!", e);
			
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.add(new UnhandledException(e, w.toString()));
			
			throw new EngineException(title, "Resolution failed!", e);
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
			LogContainer parentLog)
			throws EngineException
	{
		ParserLog log = new ParserLog();
		parentLog.add(log);
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		try
		{
			PreprocessedWikitext preprocessedWikitext =
					PreprocessorToParserTransformer.transform(
							ppAst,
							engineConfig.isTrimTransparentBeforeParsing());
			
			WikitextParser parser = new WikitextParser(parserConfig);
			
			WtParsedWikitextPage parsedAst =
					(WtParsedWikitextPage) parser.parseArticle(
							preprocessedWikitext,
							title.getTitle());
			
			// if there were no warnings we would try to add to the EMPTY_LIST
			if (parsedAst.getWarnings() == Collections.EMPTY_LIST)
			{
				parsedAst.setWarnings(ppAst.getWarnings());
			}
			else
			{
				parsedAst.getWarnings().addAll(ppAst.getWarnings());
			}
			
			return parsedAst;
		}
		catch (xtc.parser.ParseException e)
		{
			log.add(new ParseException(e.getMessage()));
			
			throw new EngineException(title, "Parsing failed!", e);
		}
		catch (Exception e)
		{
			logger.error("Parsing failed!", e);
			
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.add(new UnhandledException(e, w.toString()));
			
			throw new EngineException(title, "Parsing failed!", e);
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
			EngineLog parentLog)
			throws EngineException
	{
		PostprocessorLog log = new PostprocessorLog();
		parentLog.add(log);
		
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
			log.add(new UnhandledException(e, w.toString()));
			
			throw new EngineException(title, "Postprocessing failed!", e);
		}
		finally
		{
			stopWatch.stop();
			log.setTimeNeeded(stopWatch.getElapsedTime());
		}
	}
}
