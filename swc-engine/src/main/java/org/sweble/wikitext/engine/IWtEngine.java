package org.sweble.wikitext.engine;

import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngCompiledPage;
import org.sweble.wikitext.engine.nodes.EngineNodeFactory;
import org.sweble.wikitext.parser.nodes.WtPreproWikitextPage;

public interface IWtEngine
{
	
	public void setDebugHooks(ExpansionDebugHooks hooks);
	
	public void setNoRedirect(boolean noRedirect);
	
	public void setTimingEnabled(boolean timingEnabled);
	
	public void setCatchAll(boolean catchAll);
	
	public WikiConfig getWikiConfig();
	
	public ExpansionDebugHooks getDebugHooks();
	
	public boolean isNoRedirect();
	
	public boolean isTimingEnabled();
	
	public boolean isCatchAll();
	
	public EngineNodeFactory nf();
	
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
			throws CompilerException;
	
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
			throws CompilerException;
	
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
			throws CompilerException;
	
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
			throws CompilerException;
	
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
			throws CompilerException;
	
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
			throws CompilerException;
	
}
