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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sweble.wikitext.engine.config.Namespace;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngLogContainer;
import org.sweble.wikitext.engine.nodes.EngLogMagicWordResolution;
import org.sweble.wikitext.engine.nodes.EngLogParameterResolution;
import org.sweble.wikitext.engine.nodes.EngLogParserFunctionResolution;
import org.sweble.wikitext.engine.nodes.EngLogRedirectResolution;
import org.sweble.wikitext.engine.nodes.EngLogTagExtensionResolution;
import org.sweble.wikitext.engine.nodes.EngLogTransclusionResolution;
import org.sweble.wikitext.engine.nodes.EngNode;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.nodes.EngineNodeFactory;
import org.sweble.wikitext.engine.utils.EngineAstTextUtils;
import org.sweble.wikitext.parser.WikitextWarning.WarningSeverity;
import org.sweble.wikitext.parser.nodes.WtName;
import org.sweble.wikitext.parser.nodes.WtNewline;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtPageSwitch;
import org.sweble.wikitext.parser.nodes.WtRedirect;
import org.sweble.wikitext.parser.nodes.WtTagExtension;
import org.sweble.wikitext.parser.nodes.WtTagExtensionBody;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtTemplateArgument;
import org.sweble.wikitext.parser.nodes.WtTemplateParameter;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtValue;
import org.sweble.wikitext.parser.nodes.WtXmlAttribute;
import org.sweble.wikitext.parser.nodes.WtXmlAttributes;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wikitext.parser.utils.AstTextUtils.PartialConversion;
import org.sweble.wikitext.parser.utils.StringConversionException;

import de.fau.cs.osr.utils.StopWatch;

public final class ExpansionVisitor
		extends
			NodeTypeEngVisitor
{
	private static final Pattern STARTS_WITH_BLOCK_ELEMENT =
			Pattern.compile("^(\\{\\||:|;|#|\\*)");

	// =========================================================================

	private static final String SKIP_ATTR_NAME = "__SKIP__";

	private final ExpansionFrame expFrame;

	private final EngLogContainer frameLog;

	private final ExpansionDebugHooks hooks;

	private final boolean timingEnabled;

	private final boolean catchAll;

	private final EngineNodeFactory nf;

	private final EngineAstTextUtils tu;

	private boolean hadNewlineGlobal;

	// =========================================================================

	public ExpansionVisitor(
			ExpansionFrame expFrame,
			EngLogContainer frameLog,
			ExpansionDebugHooks hooks,
			boolean timingEnabled,
			boolean catchAll)
	{
		this.expFrame = expFrame;
		this.frameLog = frameLog;
		this.hooks = hooks;
		this.timingEnabled = timingEnabled;
		this.catchAll = catchAll;
		this.nf = expFrame.getWikiConfig().getNodeFactory();
		this.tu = expFrame.getWikiConfig().getAstTextUtils();
	}

	// =========================================================================

	/**
	 * Visit a node which if of no intereset to the expansion process.
	 * 
	 * Visits all it's children to see if there is something that has to be
	 * expanded. If one of the children got expanded the child will be replaced
	 * by the expanded AST.
	 */
	@Override
	protected WtNode visitUnspecific(WtNode n)
	{
		mapInPlace(n);
		return n;
	}

	@Override
	protected Object resolveAndVisit(WtNode n, int type) throws ExpansionException
	{
		switch (type)
		{
		// -- These set the hadNewline flag --

			case EngNode.NT_TEXT:
				return visitText((WtText) n);
			case EngNode.NT_NEWLINE:
				return visitNewline((WtNewline) n);

				// -- We must NOT reset hadNewline for these elements! --

			case EngNode.NT_SOFT_ERROR:
				return visitError(n);

			case EngNode.NT_IGNORED:
			case EngNode.NT_XML_COMMENT:
			case EngNode.NT_NODE_LIST:
				return visitUnspecific(n);

				// -- We MUST reset hadNewline for these elements! --

			default:
				hadNewlineGlobal = false;
				switch (type)
				{
					case EngNode.NT_REDIRECT:
						return visit((WtRedirect) n);
					case EngNode.NT_TEMPLATE_PARAMETER:
						return visit((WtTemplateParameter) n);
					case EngNode.NT_TEMPLATE:
						return visit((WtTemplate) n);
					case EngNode.NT_TAG_EXTENSION:
						return visit((WtTagExtension) n);
					case EngNode.NT_PAGE_SWITCH:
						return visit((WtPageSwitch) n);
					default:
						return visitUnspecific(n);

						// We don't care about other node types than the ones handled above.
						//return super.resolveAndVisit(node, type);
				}
		}
	}

	// =========================================================================
	// ==
	// ==  Newlines
	// ==
	// =========================================================================

	private WtNewline visitNewline(WtNewline n)
	{
		hadNewlineGlobal = true;
		return n;
	}

	private WtText visitText(WtText n)
	{
		String text = n.getContent();
		if (!text.isEmpty())
		{
			hadNewlineGlobal = false;
			if (text.indexOf('\n') != -1)
				hadNewlineGlobal = true;
		}
		return n;
	}

	// =========================================================================
	// ==
	// ==  Error
	// ==
	// =========================================================================

	private Object visitError(WtNode n)
	{
		/* Don't descend into error nodes!
		 */
		return n;
	}

	// =========================================================================
	// ==
	// ==  R e d i r e c t
	// ==
	// =========================================================================

	private WtNode visit(WtRedirect n) throws ExpansionException
	{
		if (skip(n))
			return n;

		if (expFrame.isNoRedirect())
			return n;

		if (!n.getTarget().isResolved())
			return markError(n);

		String target = n.getTarget().getAsString();

		WtNode result = resolveRedirectWrapper(n, target);
		if (result == null)
			result = markError(n);

		return result;
	}

	/**
	 * @return Returns null if the redirect target page cannot be found. Returns
	 *         the redirect node n itself, if an error occurred in the expansion
	 *         process. Otherwise the expanded form of the redirect target page
	 *         will be returned.
	 */
	private WtNode resolveRedirectWrapper(WtRedirect n, String target) throws ExpansionException
	{
		if (hooks != null)
		{
			WtNode cont = hooks.beforeResolveRedirect(this, n, target);
			if (cont != ExpansionDebugHooks.PROCEED)
				return cont;
		}

		EngLogRedirectResolution log = null;
		if (frameLog != null)
		{
			log = nf.logRedirectResolution(target, false);
			frameLog.add(log);
		}

		StopWatch stopWatch = null;
		if (timingEnabled)
		{
			stopWatch = new StopWatch();
			stopWatch.start();
		}

		WtNode result = null;
		try
		{
			result = expandRedirectionTargetPage(n, target, log);
		}
		catch (Exception e)
		{
			result = markError(n, e);

			if (log != null)
				logUnhandledException(log, e);

			if (!catchAll)
				throw new ExpansionException(e);
		}
		finally
		{
			if (timingEnabled && log != null)
				log.setTimeNeeded(stopWatch.getElapsedTime());
		}

		return (hooks != null) ?
				hooks.afterResolveRedirect(this, n, target, result, log) :
				result;
	}

	/**
	 * Expands the target page of the redirect statement inside a NEW expansion
	 * frame and returns the expanded AST.
	 * 
	 * @return Returns null if the redirect target page cannot be found. Returns
	 *         the redirect node n itself, if an error occurred in the expansion
	 *         process. Otherwise the expanded form of the redirect target page
	 *         will be returned.
	 */
	private WtNode expandRedirectionTargetPage(
			WtRedirect n,
			String target,
			EngLogRedirectResolution log) throws EngineException
	{
		PageTitle title;
		try
		{
			title = PageTitle.make(getWikiConfig(), target);
		}
		catch (LinkTargetException e)
		{
			if (log != null)
				log.add(nf.logParserError(e.getMessage()));

			fileInvalidPageNameWarning(n, target);

			return n;
		}

		if (log != null)
			log.setCanonical(title.getDenormalizedFullTitle());

		FullPage page = getWikitext(title);
		if (page != null)
		{
			/* Since we are redirecting some stuff is passed on from this frame
			 * to the frame in which the target page is expanded: - The
			 * forInclusion flag is passed on.
			 * 
			 * - If the page we are redirecting from was thought for inclusion
			 *   so will be the replacement page.
			 * 
			 * - The arguments that were passed to the page we are redirecting
			 *   from will also be passed to the replacement page.
			 */
			EngProcessedPage processedPage = getEngine().preprocessAndExpand(
					expFrame.getCallback(),
					page.getId(),
					page.getText(),
					expFrame.isForInclusion(),
					expFrame.getEntityMap(),
					expFrame.getArguments(),
					expFrame.getRootFrame(),
					expFrame);

			log.setSuccess(true);

			return mergeLogsAndWarnings(log, processedPage);
		}
		else
		{
			filePageNotFoundWarning(n, title);

			return null;
		}
	}

	// =========================================================================
	// ==
	// ==  T e m p l a t e
	// ==
	// =========================================================================

	/**
	 * A template statement was found.
	 * 
	 * First fully expand the name of the template. Then determine if this is a
	 * call to a parser function, a transclusion or magic word.
	 * 
	 * This method WILL ONLY EXPAND the parameters of the template statement if
	 * it is a transclusion. If it is a call to a parser function or magic word,
	 * the respective parser function can decide if it needs to expand
	 * parameters.
	 */
	private WtNode visit(WtTemplate n) throws ExpansionException
	{
		if (skip(n))
			return n;

		// Safe newline state!
		boolean hadNewline = this.hadNewlineGlobal;

		// First: Fully expand name.
		WtName name = (WtName) dispatch(n.getName());
		//n.setName(name);

		PartialConversion nameConv = tu.astToTextPartial(name);
		// DO NOT expand parameters (yet)
		ArrayList<WtTemplateArgument> args =
				new ArrayList<WtTemplateArgument>(n.getArgs().size() + 1);

		for (Object arg : n.getArgs())
			args.add((WtTemplateArgument) arg);

		// First see if it is a parser function
		WtNode result = resolveTemplateAsPfn(n, nameConv.getText(), nameConv.getTail(), args, hadNewline);

		if (result == null)
		{
			// A template or magic word cannot be resolved if the name is not 
			// fully convertable to a string
			if (nameConv.getTail().isEmpty())
			{
				// If not see if it is a magic word
				// Magic Word calls cannot have arguments!
				if (args.isEmpty())
					result = resolveTemplateAsMagicWord(n, nameConv.getText(), hadNewline);

				// If not try to transclude
				if (result == null)
					result = resolveTemplateAsTransclusion(n, nameConv.getText(), args, hadNewline);
			}
			else
			{
				StringConversionException e = new StringConversionException(nameConv.getTail());

				if (frameLog != null)
					frameLog.add(nf.logParserError(e.getMessage()));

				fileInvalidTemplateNameWarning(n, e);
			}
		}

		if (result == null)
			result = markError(n);
		else if (result != n)
			this.hadNewlineGlobal = endedWithNewline(result);

		return result;
	}

	private boolean endedWithNewline(WtNode result)
	{
		// FIXME: IMPLEMENT!
		return false;
	}

	/**
	 * Try to resolve the template as parser function.
	 * 
	 * A parser function looks like a transclusion, only that its name is
	 * separated from the first argument by a colon. This method checks if the
	 * template name contains a colon and then tries to look up the parser
	 * function.
	 * 
	 * @return Returns the expanded AST of produced by the parser function if
	 *         everything went fine. If the given template does not have parser
	 *         function syntax (no colon) or no matching parser function can be
	 *         found null is returned. If a parser function was found but an
	 *         error occurs afterwards, the template node itself is returned.
	 */
	private WtNode resolveTemplateAsPfn(
			WtTemplate n,
			String title,
			WtNodeList tail,
			ArrayList<WtTemplateArgument> args,
			boolean hadNewline) throws ExpansionException
	{
		int i = title.indexOf(':');
		if (i == -1)
			return null;

		/*
		title = title.trim();
		
		boolean hash = !title.isEmpty() && title.charAt(0) == '#';
		String name = hash ?
				(title.substring(1, i)) :
				(title.substring(0, i) + ":");
		*/

		String name = title.substring(0, i).trim() + ":";

		ParserFunctionBase pfn = getWikiConfig().getParserFunction(name);
		if (pfn == null)
			return null;

		String arg0Prefix = title.substring(i + 1).trim();

		List<? extends WtNode> argsValues = preparePfnArguments(
				pfn.getArgMode(),
				arg0Prefix,
				tail,
				args);

		return invokePfn(n, pfn, argsValues, hadNewline);
	}

	/**
	 * Prepare arguments to pass to parser function.
	 * 
	 * While a parser function looks like a template it does not support
	 * parameter names. Therefore, all `name "=" value' parameters of the
	 * template will be converted into a single value `$name=$value'.
	 * 
	 * Also the first argument begins after the colon, the first template
	 * argument is actually the second argument to the parser function. To fix
	 * this, the part after the colon is stitched together to form the first
	 * argument to the parser function.
	 * 
	 * The arguments of a parser function ARE NOT EXPANDED! This has to be done
	 * by each parser function individually, IF the parser function requires it.
	 * 
	 * @param pfnArgumentMode
	 */
	private List<? extends WtNode> preparePfnArguments(
			PfnArgumentMode pfnArgumentMode,
			String arg0Prefix,
			WtNodeList tail,
			List<WtTemplateArgument> args)
	{
		/*

		MediaWiki has two ways of invoking parser functions:
		1) The arguments are passed in preprocessor DOM format.
		2) The arguments are expanded and passed as text.
		
		In case 2) the expanded arguments are TRIMMED! before they are passed to 
		the parser function. It is my understanding that, even though the 
		arguments have been expanded and converted to text, there are still 
		so-called markers in that text.
		
		Places in the code:
		includes/parser/Parser.php:3172-3186 (83b70491fd7cbe9f8a0eadec0f9500636742f6fd)
		
		 */

		switch (pfnArgumentMode)
		{
			case EXPANDED_AND_TRIMMED_VALUES:
			{
				ArrayList<WtNode> argValues = new ArrayList<WtNode>(args.size() + 1);

				WtNode arg0 = nf.list(nf.text(arg0Prefix), tail);

				arg0 = tu.trim((WtNode) dispatch(arg0));
				argValues.add(arg0);

				for (int j = 0; j < args.size(); ++j)
				{
					WtTemplateArgument tmplArg = args.get(j);

					WtNodeList arg = nf.list();
					if (tmplArg.hasName())
					{
						arg.addAll(tmplArg.getName());
						arg.add(nf.text("="));
					}
					arg.addAll(tmplArg.getValue());

					argValues.add(tu.trim((WtNode) dispatch(arg)));
				}

				return argValues;
			}
			case TEMPLATE_ARGUMENTS:
			{
				ArrayList<WtTemplateArgument> argsWithArg0 =
						new ArrayList<WtTemplateArgument>(args.size() + 1);

				argsWithArg0.add(
						nf.tmplArg(nf.value(
								nf.list(nf.text(arg0Prefix), tail))));

				for (WtTemplateArgument arg : args)
					argsWithArg0.add(arg);

				return argsWithArg0;
			}
			case UNEXPANDED_VALUES:
			{
				ArrayList<WtNode> argValues = new ArrayList<WtNode>(args.size() + 1);

				argValues.add(nf.list(nf.text(arg0Prefix), tail));

				for (int j = 0; j < args.size(); ++j)
				{
					WtTemplateArgument arg = args.get(j);

					WtNodeList value = nf.list();
					if (arg.hasName())
					{
						value.addAll(arg.getName());
						value.add(nf.text("="));
					}
					value.addAll(arg.getValue());

					argValues.add(value);
				}

				return argValues;
			}
			default:
				throw new AssertionError();
		}
	}

	/**
	 * Try to resolve the template as magic word (and call the associated parser
	 * function).
	 * 
	 * A template that is not a call to a parser function (no colon in name) and
	 * does not take any parameters can be a call to a parser function
	 * identified solely by a magic word.
	 * 
	 * @return Returns the expanded AST of produced by the parser function if
	 *         everything went fine. If the given template does not have parser
	 *         function syntax (no colon) or no matching parser function can be
	 *         found null is returned. If a parser function was found but an
	 *         error occurs afterwards, the template node itself is returned.
	 */
	private WtNode resolveTemplateAsMagicWord(
			WtTemplate n,
			String title,
			boolean hadNewline) throws ExpansionException
	{
		ParserFunctionBase pfn = getWikiConfig().getParserFunction(title);
		if (pfn == null)
			return null;

		List<WtNode> argsValues = Collections.emptyList();

		return invokePfn(n, pfn, argsValues, hadNewline);
	}

	/**
	 * Nothing left to do but call the actual parser function.
	 */
	private WtNode invokePfn(
			WtTemplate n,
			ParserFunctionBase pfn,
			List<? extends WtNode> argsValues,
			boolean hadNewline) throws ExpansionException
	{
		if (hooks != null)
		{
			WtNode cont = hooks.beforeResolveParserFunction(this, n, pfn, argsValues);
			if (cont != ExpansionDebugHooks.PROCEED)
				return cont;
		}

		EngLogParserFunctionResolution log = null;
		if (frameLog != null)
		{
			log = nf.logParserFunctionResolution(pfn.getId(), false);
			frameLog.add(log);
		}

		StopWatch stopWatch = null;
		if (timingEnabled)
		{
			stopWatch = new StopWatch();
			stopWatch.start();
		}

		WtNode result = null;
		try
		{
			result = pfn.invoke(n, expFrame, argsValues);
			if (result == null)
				throw new NullPointerException("Parser function `" + pfn.getId() + "' returned null value!");

			if (result != n)
				result = treatBlockElements(n, result);

			log.setSuccess(true);
		}
		catch (Exception e)
		{
			result = markError(n, e);

			if (log != null)
				logUnhandledException(log, e);

			if (!catchAll)
				throw new ExpansionException(e);
		}
		finally
		{
			if (timingEnabled && log != null)
				log.setTimeNeeded(stopWatch.getElapsedTime());
		}

		return (hooks != null) ?
				hooks.afterResolveParserFunction(this, n, pfn, argsValues, result, log) :
				result;
	}

	/**
	 * Try to resolve the template as transclusion.
	 * 
	 * @return Returns the expanded AST of the transcluded page if everything
	 *         went fine. If the page to transclude cannot be found, null is
	 *         returned. If the page was found but an error occurs afterwards,
	 *         the template node itself is returned.
	 */
	private WtNode resolveTemplateAsTransclusion(
			WtTemplate n,
			String title,
			ArrayList<WtTemplateArgument> args,
			boolean hadNewline) throws ExpansionException
	{
		if (hooks != null)
		{
			WtNode cont = hooks.beforeResolveTransclusion(this, n, title, args);
			if (cont != ExpansionDebugHooks.PROCEED)
				return cont;
		}

		EngLogTransclusionResolution log = null;
		if (frameLog != null)
		{
			log = nf.logTransclusionResolution(title, false);
			frameLog.add(log);
		}

		StopWatch stopWatch = null;
		if (timingEnabled)
		{
			stopWatch = new StopWatch();
			stopWatch.start();
		}

		WtNode result = null;
		try
		{
			result = transcludePage(n, title, args, log);
		}
		catch (Exception e)
		{
			result = markError(n, e);

			if (log != null)
				logUnhandledException(log, e);

			if (!catchAll)
				throw new ExpansionException(e);
		}
		finally
		{
			if (timingEnabled && log != null)
				log.setTimeNeeded(stopWatch.getElapsedTime());
		}

		return (hooks != null) ?
				hooks.afterResolveTransclusion(this, n, title, args, result, log) :
				result;
	}

	/**
	 * Expands the name of the template, the template parameters and then
	 * preprocesses and expands the page to transclude.
	 * 
	 * @return Returns the expanded AST of the transcluded page if everything
	 *         went fine. If the page to transclude cannot be found, null is
	 *         returned. If the page was found but an error occurs afterwards,
	 *         the template node itself is returned.
	 */
	private WtNode transcludePage(
			WtTemplate n,
			String target,
			List<WtTemplateArgument> args,
			EngLogTransclusionResolution log) throws EngineException, RecursiveTransclusionException
	{
		Namespace tmplNs = getWikiConfig().getTemplateNamespace();

		PageTitle title;
		try
		{
			title = PageTitle.make(getWikiConfig(), target, tmplNs);
		}
		catch (LinkTargetException e)
		{
			if (log != null)
				log.add(nf.logParserError(e.getMessage()));

			fileInvalidPageNameWarning(n, target);

			return n;
		}

		checkTransclusionRecursion(title);

		log.setCanonical(title.getDenormalizedFullTitle());

		FullPage page = getWikitext(title);
		if (page != null)
		{
			// EXPANDS ARGUMENTS!
			Map<String, WtNodeList> tmplArgs = prepareTransclusionArguments(args, log);

			EngProcessedPage processedPage = getEngine().preprocessAndExpand(
					expFrame.getCallback(),
					page.getId(),
					page.getText(),
					true,
					expFrame.getEntityMap(),
					tmplArgs,
					expFrame.getRootFrame(),
					expFrame);

			log.setSuccess(true);

			WtNode tResult = mergeLogsAndWarnings(log, processedPage);

			return treatBlockElements(n, tResult);
		}
		else
		{
			filePageNotFoundWarning(n, title);

			return null;
		}
	}

	/**
	 * Check if a page transcludes itself more than once (directly or
	 * indirectly).
	 */
	private void checkTransclusionRecursion(PageTitle title) throws RecursiveTransclusionException
	{
		int count = 0;

		ExpansionFrame f = expFrame;
		while (f != null)
		{
			if (f.getTitle().equals(title))
			{
				if (++count > 2)
					throw new RecursiveTransclusionException(title, count);
			}

			f = f.getParentFrame();
		}
	}

	/**
	 * Prepares the template argument list for transclusion. This encompasses
	 * the expansion of name and value of each argument.
	 * 
	 * Each argument is added to the mapping with its one-based index as key.
	 * 
	 * If an argument has a name which can be resolved to a string, the argument
	 * will additionally be put into the mapping with the resolved name as key.
	 */
	private Map<String, WtNodeList> prepareTransclusionArguments(
			List<WtTemplateArgument> args,
			EngLogTransclusionResolution log)
	{
		HashMap<String, WtNodeList> transclArgs = new HashMap<String, WtNodeList>();

		int index = 1;
		for (WtTemplateArgument arg : args)
		{
			// EXPAND VALUE!
			WtValue value = (WtValue) dispatch(arg.getValue());

			boolean named = false;
			if (arg.hasName())
			{
				// ONLY TRIM NAMED VALUES!
				value = (WtValue) tu.trim(value);

				// EXPAND NAME!
				WtName name = (WtName) dispatch(arg.getName());

				try
				{
					String nameStr = tu.astToText(name).trim();

					if (!nameStr.isEmpty())
					{
						transclArgs.put(nameStr, nf.toList(value));
						named = true;
					}
				}
				catch (StringConversionException e)
				{
					if (log != null)
						log.add(nf.logParserError(e.getMessage()));

					fileInvalidArgumentNameWarning(arg, e);
				}
			}

			if (!named)
			{
				String id = String.valueOf(index);

				WtNodeList prev = transclArgs.put(id, nf.toList(value));
				// Automatic indices never overwrite!
				if (prev != null)
					transclArgs.put(id, prev);

				// Only unnamed arguments increase the index
				index++;
			}
		}

		return transclArgs;
	}

	// =========================================================================
	// ==
	// ==  T e m p l a t e  P a r a m e t e r
	// ==
	// =========================================================================

	/**
	 * A template parameter was found.
	 * 
	 * First fully expand the name of the parameter. Then try to find the value
	 * in the argument list that was passed to this frame. Otherwise try to use
	 * the default value.
	 * 
	 * This method WILL ONLY EXPAND the default value if no value could be found
	 * among the frame arguments.
	 */
	private WtNode visit(WtTemplateParameter n) throws ExpansionException
	{
		if (skip(n))
			return n;

		// Fully expand name!
		WtName name = (WtName) dispatch(n.getName());

		String nameStr = null;
		try
		{
			nameStr = tu.astToText(name);
		}
		catch (StringConversionException e)
		{
			if (frameLog != null)
				frameLog.add(nf.logParserError(e.getMessage()));

			fileInvalidParameterNameWarning(n, e);
		}

		WtNode value = null;
		if (nameStr != null)
			value = resolveParameterWrapper(n, nameStr.trim());

		if (value == null)
			value = markError(n);

		return value;
	}

	/**
	 * Try to map the parameter to its value.
	 * 
	 * @return Returns the expanded value to which the parameter resolved. If no
	 *         value can be found in the frame arguments and no default value is
	 *         given, null is returned. Otherwise, if an error occurs, the
	 *         parameter node itself is returned.
	 */
	private WtNode resolveParameterWrapper(
			WtTemplateParameter n,
			String name) throws ExpansionException
	{
		if (hooks != null)
		{
			WtNode cont = hooks.beforeResolveParameter(this, n, name);
			if (cont != ExpansionDebugHooks.PROCEED)
				return cont;
		}

		EngLogParameterResolution log = null;
		if (frameLog != null)
		{
			log = nf.logParameterResolution(name, false);
			frameLog.add(log);
		}

		StopWatch stopWatch = null;
		if (timingEnabled)
		{
			stopWatch = new StopWatch();
			stopWatch.start();
		}

		WtNode result = null;
		try
		{
			result = resolveParameter(n, name, log);
		}
		catch (Exception e)
		{
			result = markError(n, e);

			if (log != null)
				logUnhandledException(log, e);

			if (!catchAll)
				throw new ExpansionException(e);
		}
		finally
		{
			if (timingEnabled && log != null)
				log.setTimeNeeded(stopWatch.getElapsedTime());
		}

		return (hooks != null) ?
				hooks.afterResolveParameter(this, n, name, result, log) :
				result;
	}

	/**
	 * Tries to get the parameter value from the list of arguments passed to
	 * this frame. If no matching argument can be found, the default value is
	 * used, if present. Otherwise, null is returned.
	 */
	private WtNodeList resolveParameter(
			WtTemplateParameter n,
			String name,
			EngLogParameterResolution log)
	{
		WtNodeList value = getFrameArgument(name);

		if (value == null && n.hasDefault())
		{
			// Only the first value after the pipe is the default 
			// value. The rest is ignored.

			// EXPAND DEFAULT VALUE!
			value = nf.toList((WtValue) dispatch(n.getDefault()));
		}

		if (value != null)
		{
			log.setSuccess(true);

			//value = treatBlockElements(n, value);
		}

		return value;
	}

	// =========================================================================
	// ==
	// ==  T A G   E X T E N S I O N
	// ==
	// =========================================================================

	private WtNode visit(WtTagExtension n) throws ExpansionException
	{
		if (skip(n))
			return n;

		WtNode result = resolveTagExtensionWrapper(n, n.getName(), n.getXmlAttributes(), n.getBody());
		if (result == null)
			result = markError(n);

		return result;
	}

	private WtNode resolveTagExtensionWrapper(
			WtTagExtension n,
			String name,
			WtXmlAttributes attrs,
			WtTagExtensionBody wtTagExtensionBody) throws ExpansionException
	{
		if (hooks != null)
		{
			WtNode cont = hooks.beforeResolveTagExtension(this, n, name, attrs, wtTagExtensionBody);
			if (cont != ExpansionDebugHooks.PROCEED)
				return cont;
		}

		EngLogTagExtensionResolution log = null;
		if (frameLog != null)
		{
			log = nf.logTagExtensionResolution(name, false);
			frameLog.add(log);
		}

		StopWatch stopWatch = null;
		if (timingEnabled)
		{
			stopWatch = new StopWatch();
			stopWatch.start();
		}

		WtNode result = null;
		try
		{
			result = resolveTagExtension(n, name, attrs, wtTagExtensionBody, log);
		}
		catch (Exception e)
		{
			result = markError(n, e);

			if (log != null)
				logUnhandledException(log, e);

			if (!catchAll)
				throw new ExpansionException(e);
		}
		finally
		{
			if (timingEnabled && log != null)
				log.setTimeNeeded(stopWatch.getElapsedTime());
		}

		return (hooks != null) ?
				hooks.afterResolveTagExtension(this, n, name, attrs, wtTagExtensionBody, result, log) :
				result;
	}

	private WtNode resolveTagExtension(
			WtTagExtension n,
			String name,
			WtXmlAttributes attrs,
			WtTagExtensionBody wtTagExtensionBody,
			EngLogTagExtensionResolution log)
	{
		TagExtensionBase te = getWikiConfig().getTagExtension(name);
		if (te == null)
			/* This should not happen: If you register a tag extension with the
			 * parser (which only then will produce this tag extension node)
			 * there also has to be a tag extension object.
			 * 
			 * That's not true any more: the #tag: pfn can also generate
			 * tag extensions, whether they were registered or not.
			 */
			//throw new AssertionError("Cannot find tag extension: " + name);
			return null;

		HashMap<String, WtNodeList> attrMap = prepareTagExtensionAttributes(attrs);

		WtNode result = te.invoke(expFrame, n, attrMap, wtTagExtensionBody);

		log.setSuccess(true);

		return result;
	}

	/**
	 * Converts the list of attributes into a map, ignoring any non-attribute
	 * nodes and also ignoring attributes with unresolved names.
	 */
	private HashMap<String, WtNodeList> prepareTagExtensionAttributes(
			WtXmlAttributes attrs)
	{
		HashMap<String, WtNodeList> attrMap = new HashMap<String, WtNodeList>();

		for (WtNode attr : attrs)
		{
			if (attr.isNodeType(WtNode.NT_XML_ATTRIBUTE))
			{
				WtXmlAttribute a = (WtXmlAttribute) attr;
				if (a.getName().isResolved())
					attrMap.put(a.getName().getAsString(), nf.toList(a.getValue()));
			}
		}

		return attrMap;
	}

	// =========================================================================
	// ==
	// ==  M A G I C   W O R D
	// ==
	// =========================================================================

	private WtNode visit(WtPageSwitch n) throws ExpansionException
	{
		if (skip(n))
			return n;

		WtNode result = resolveMagicWordWrapper(n, n.getName());
		if (result == null)
			result = markError(n);

		return result;
	}

	private WtNode resolveMagicWordWrapper(
			WtPageSwitch n,
			String name) throws ExpansionException
	{
		if (hooks != null)
		{
			WtNode cont = hooks.beforeResolvePageSwitch(this, n, name);
			if (cont != ExpansionDebugHooks.PROCEED)
				return cont;
		}

		EngLogMagicWordResolution log = null;
		if (frameLog != null)
		{
			log = nf.logMagicWordResolution(name, false);
			frameLog.add(log);
		}

		StopWatch stopWatch = null;
		if (timingEnabled)
		{
			stopWatch = new StopWatch();
			stopWatch.start();
		}

		WtNode result = null;
		try
		{
			result = resolvePageSwitch(n, name, log);
		}
		catch (Exception e)
		{
			result = markError(n, e);

			if (log != null)
				logUnhandledException(log, e);

			if (!catchAll)
				throw new ExpansionException(e);
		}
		finally
		{
			if (timingEnabled && log != null)
				log.setTimeNeeded(stopWatch.getElapsedTime());
		}

		return (hooks != null) ?
				hooks.afterResolvePageSwitch(this, n, name, result, log) :
				result;
	}

	private WtNode resolvePageSwitch(
			WtPageSwitch n,
			String name,
			EngLogMagicWordResolution log)
	{
		ParserFunctionBase mw =
				getWikiConfig().getPageSwitch("__" + name + "__");

		if (mw == null)
			/* This should not happen: If you register a magic word with the
			 * parser (which only then will produce this magic word node)
			 * there also has to be a magic word object.
			 */
			throw new AssertionError("Cannot find tag extension: " + name);

		WtNode result = mw.invoke(
				n,
				expFrame,
				Collections.<WtNode> emptyList());

		log.setSuccess(true);

		return result;
	}

	// =========================================================================

	public ExpansionFrame getExpFrame()
	{
		return expFrame;
	}

	public boolean isHadNewline()
	{
		return hadNewlineGlobal;
	}

	private WikiConfig getWikiConfig()
	{
		return expFrame.getWikiConfig();
	}

	private WtEngineImpl getEngine()
	{
		return expFrame.getEngine();
	}

	private WtNodeList getFrameArgument(String name)
	{
		return expFrame.getArguments().get(name);
	}

	private FullPage getWikitext(PageTitle title)
	{
		return expFrame.getCallback().retrieveWikitext(expFrame, title);
	}

	private void logUnhandledException(EngLogContainer log, Exception e)
	{
		StringWriter w = new StringWriter();
		e.printStackTrace(new PrintWriter(w));
		log.add(nf.logUnhandledError(e, w.toString()));
	}

	private void fileInvalidPageNameWarning(WtNode n, String target)
	{
		expFrame.fileWarning(new InvalidPagenameWarning(
				WarningSeverity.FATAL,
				getClass(),
				n,
				target));
	}

	private void fileInvalidTemplateNameWarning(
			WtTemplate n,
			StringConversionException e)
	{
		expFrame.fileWarning(new InvalidNameWarning(
				WarningSeverity.FATAL,
				getClass(),
				n));
	}

	private void fileInvalidArgumentNameWarning(
			WtTemplateArgument n,
			StringConversionException e)
	{
		expFrame.fileWarning(new InvalidNameWarning(
				WarningSeverity.FATAL,
				getClass(),
				n));
	}

	private void fileInvalidParameterNameWarning(
			WtTemplateParameter n,
			StringConversionException e)
	{
		expFrame.fileWarning(new InvalidNameWarning(
				WarningSeverity.FATAL,
				getClass(),
				n));
	}

	private void filePageNotFoundWarning(WtNode n, PageTitle title)
	{
		expFrame.fileWarning(new PageNotFoundWarning(
				WarningSeverity.NORMAL,
				getClass(),
				n,
				title));
	}

	private WtNodeList mergeLogsAndWarnings(
			EngLogContainer log,
			EngProcessedPage processedPage)
	{
		if (log != null)
			log.add(processedPage.getLog());

		expFrame.addWarnings(processedPage.getWarnings());

		return nf.unwrap(processedPage.getPage());
	}

	private WtNode treatBlockElements(WtTemplate tmpl, WtNode result)
	{
		if (result != null)
			return treatBlockElements(tmpl, result, tmpl.isPrecededByNewline());
		return null;
	}

	/*
	private WtNode treatBlockElements(TemplateParameter tmpl, WtNode result)
	{
		if (result != null)
			return treatBlockElements(tmpl, result, tmpl.getPrecededByNewline());
		return null;
	}
	*/

	/**
	 * If we don't had a newline before some template element but the template
	 * resolved to a block level element, THEN we prepend a newline to make the
	 * block level element start on a new line (so that it can be identified
	 * correctly).
	 */
	private WtNode treatBlockElements(
			WtNode tmpl,
			WtNode result,
			boolean hadNewline)
	{
		if (result != null && !hadNewline)
		{
			PartialConversion split = tu.astToTextPartial(
					result,
					EngineAstTextUtils.FAIL_ON_UNRESOLVED_ENTITY_REF,
					EngineAstTextUtils.DO_NOT_CONVERT_NOWIKI);

			Matcher m = STARTS_WITH_BLOCK_ELEMENT.matcher(split.getText());
			if (m.find())
				result = nf.list(nf.text("\n" + split.getText()), split.getTail());
		}

		return result;
	}

	/**
	 * Returns if the expansion of the specified node should be skipped. This
	 * would for example be the case when we already tried to expand the node
	 * but failed.
	 */
	private boolean skip(WtNode n)
	{
		return n.hasAttribute(SKIP_ATTR_NAME);
	}

	/**
	 * Called when something could not be resolved. This could happen if a
	 * transclusion statement cannot find the target page. This method MUST NOT
	 * BE CALLED when the expansion process for this element failed due to an
	 * exception.
	 */
	private WtNode markError(WtNode n)
	{
		//return new SoftErrorNode(n);
		n.setAttribute(SKIP_ATTR_NAME, false);
		return n;
	}

	/**
	 * Called when the expansion process for the specified element failed due to
	 * an exception.
	 */
	private WtNode markError(WtNode n, Exception e)
	{
		//return new SoftErrorNode(n, e);
		n.setAttribute(SKIP_ATTR_NAME, e);
		return n;
	}
}
