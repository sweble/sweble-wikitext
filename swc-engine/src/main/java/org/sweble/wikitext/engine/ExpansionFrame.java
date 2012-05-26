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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.sweble.wikitext.engine.config.Namespace;
import org.sweble.wikitext.engine.config.WikiConfigurationInterface;
import org.sweble.wikitext.engine.log.IllegalNameException;
import org.sweble.wikitext.engine.log.ParseException;
import org.sweble.wikitext.engine.log.ResolveRedirectLog;
import org.sweble.wikitext.engine.log.ResolveTransclusionLog;
import org.sweble.wikitext.engine.log.UnhandledException;
import org.sweble.wikitext.lazy.AstNodeTypes;
import org.sweble.wikitext.lazy.LinkTargetException;
import org.sweble.wikitext.lazy.preprocessor.Redirect;
import org.sweble.wikitext.lazy.preprocessor.TagExtension;
import org.sweble.wikitext.lazy.preprocessor.Template;
import org.sweble.wikitext.lazy.preprocessor.TemplateArgument;
import org.sweble.wikitext.lazy.preprocessor.TemplateParameter;
import org.sweble.wikitext.lazy.utils.StringConversionException;
import org.sweble.wikitext.lazy.utils.StringConverter;
import org.sweble.wikitext.lazy.utils.XmlAttribute;

import de.fau.cs.osr.ptk.common.EntityMap;
import de.fau.cs.osr.ptk.common.Warning;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.ContentNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;
import de.fau.cs.osr.utils.StopWatch;

public class ExpansionFrame
{
	private final Compiler compiler;
	
	private final ExpansionFrame rootFrame;
	
	private final ExpansionFrame parentFrame;
	
	private final PageTitle title;
	
	private final Map<String, AstNode> arguments;
	
	private final boolean forInclusion;
	
	private final ContentNode frameLog;
	
	private final ExpansionCallback callback;
	
	private final List<Warning> warnings;
	
	private final EntityMap entityMap;
	
	// =========================================================================
	
	public ExpansionFrame(
	        Compiler compiler,
	        ExpansionCallback callback,
	        PageTitle title,
	        EntityMap entityMap,
	        List<Warning> warnings,
	        ContentNode frameLog)
	{
		this.compiler = compiler;
		this.callback = callback;
		this.title = title;
		this.entityMap = entityMap;
		this.arguments = new HashMap<String, AstNode>();
		this.forInclusion = false;
		this.warnings = warnings;
		this.frameLog = frameLog;
		this.rootFrame = this;
		this.parentFrame = null;
	}
	
	public ExpansionFrame(
	        Compiler compiler,
	        ExpansionCallback callback,
	        PageTitle title,
	        EntityMap entityMap,
	        Map<String, AstNode> arguments,
	        boolean forInclusion,
	        ExpansionFrame rootFrame,
	        ExpansionFrame parentFrame,
	        List<Warning> warnings,
	        ContentNode frameLog)
	{
		this.compiler = compiler;
		this.callback = callback;
		this.title = title;
		this.entityMap = entityMap;
		this.arguments = arguments;
		this.forInclusion = forInclusion;
		this.warnings = warnings;
		this.frameLog = frameLog;
		this.rootFrame = rootFrame;
		this.parentFrame = parentFrame;
	}
	
	// =========================================================================
	
	public Compiler getCompiler()
	{
		return compiler;
	}
	
	public ExpansionFrame getRootFrame()
	{
		return rootFrame;
	}
	
	public ExpansionFrame getParentFrame()
	{
		return parentFrame;
	}
	
	public PageTitle getTitle()
	{
		return title;
	}
	
	public Map<String, AstNode> getArguments()
	{
		return arguments;
	}
	
	public boolean isForInclusion()
	{
		return forInclusion;
	}
	
	public ContentNode getFrameLog()
	{
		return frameLog;
	}
	
	public WikiConfigurationInterface getWikiConfig()
	{
		return compiler.getWikiConfig();
	}
	
	public void fileWarning(Warning warning)
	{
		warnings.add(warning);
	}
	
	private void addAllWarnings(Collection<Warning> warnings)
	{
		this.warnings.addAll(warnings);
	}
	
	public List<Warning> getWarnings()
	{
		return warnings;
	}
	
	// =========================================================================
	
	public AstNode expand(AstNode ppAst)
	{
		return (AstNode) new ExpansionVisitor(this).go(ppAst);
	}
	
	// =========================================================================
	
	protected AstNode resolveRedirect(Redirect n, String target)
	{
		ResolveRedirectLog log = new ResolveRedirectLog(target, false);
		frameLog.getContent().add(log);
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		try
		{
			// FIXME: What if we don't want to redirect, but view the redirect 
			//        page itself? Then we probably don't want to return the
			//        contents...
			return redirectTo(n, target, log);
		}
		catch (LinkTargetException e)
		{
			// FIXME: Should we rather file a warning? Or additionally file a warning?
			log.getContent().add(new ParseException(e.getMessage()));
			
			return n;
		}
		catch (Throwable e)
		{
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.getContent().add(new UnhandledException(e, w.toString()));
			
			return n;
		}
		finally
		{
			stopWatch.stop();
			log.setTimeNeeded(stopWatch.getElapsedTime());
		}
	}
	
	protected AstNode resolveParserFunction(
	        Template n,
	        String target,
	        List<TemplateArgument> arguments)
	{
		ResolveTransclusionLog log = new ResolveTransclusionLog(target, false);
		frameLog.getContent().add(log);
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		try
		{
			ParserFunctionBase pfn = getWikiConfig().getParserFunction(target);
			if (pfn == null)
			{
				// FIXME: File warning that we couldn't find the parser function?
				return null;
			}
			
			return invokeParserFunction(n, pfn, arguments, log);
		}
		catch (Throwable e)
		{
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.getContent().add(new UnhandledException(e, w.toString()));
			
			return n;
		}
		finally
		{
			stopWatch.stop();
			log.setTimeNeeded(stopWatch.getElapsedTime());
		}
	}
	
	protected AstNode resolveTransclusionOrMagicWord(
	        Template n,
	        String target,
	        List<TemplateArgument> arguments)
	{
		ResolveTransclusionLog log = new ResolveTransclusionLog(target, false);
		frameLog.getContent().add(log);
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		try
		{
			if (arguments.isEmpty())
			{
				ParserFunctionBase pfn = getWikiConfig().getParserFunction(target);
				if (pfn != null)
					return invokeParserFunction(n, pfn, arguments, log);
			}
			
			return transcludePage(n, target, arguments, log);
		}
		catch (LinkTargetException e)
		{
			// FIXME: Should we rather file a warning? Or additionally file a warning?
			log.getContent().add(new ParseException(e.getMessage()));
			
			return n;
		}
		catch (Throwable e)
		{
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.getContent().add(new UnhandledException(e, w.toString()));
			
			return n;
		}
		finally
		{
			stopWatch.stop();
			log.setTimeNeeded(stopWatch.getElapsedTime());
		}
	}
	
	protected AstNode resolveParameter(TemplateParameter n, String name)
	{
		return arguments.get(name);
	}
	
	protected AstNode resolveTagExtension(
	        TagExtension n,
	        String name,
	        NodeList attributes,
	        String body)
	{
		ResolveTransclusionLog log = new ResolveTransclusionLog(name, false);
		frameLog.getContent().add(log);
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		try
		{
			TagExtensionBase te = getWikiConfig().getTagExtension(name);
			if (te == null)
			{
				// FIXME: File warning that we couldn't find the tag extension?
				return n;
			}
			
			return invokeTagExtension(n, attributes, body, te);
		}
		catch (Throwable e)
		{
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.getContent().add(new UnhandledException(e, w.toString()));
			
			return n;
		}
		finally
		{
			stopWatch.stop();
			log.setTimeNeeded(stopWatch.getElapsedTime());
		}
	}
	
	private AstNode invokeTagExtension(
	        TagExtension tagExtension,
	        NodeList attributes,
	        String body,
	        TagExtensionBase te)
	{
		HashMap<String, NodeList> attrs = new HashMap<String, NodeList>();
		
		for (AstNode attr : attributes)
		{
			if (attr.isNodeType(AstNodeTypes.NT_XML_ATTRIBUTE))
			{
				XmlAttribute a = (XmlAttribute) attr;
				attrs.put(a.getName(), a.getValue());
			}
		}
		
		return te.invoke(this, tagExtension, attrs, body);
	}
	
	protected void illegalTemplateName(StringConversionException e, NodeList name)
	{
		frameLog.getContent().add(new IllegalNameException(
		        name,
		        "Cannot resolve target of transclusion to a simple name."));
	}
	
	protected void illegalParameterName(StringConversionException e, NodeList name)
	{
		frameLog.getContent().add(new IllegalNameException(
		        name,
		        "Cannot resolve name of parameter to a simple name."));
	}
	
	// =========================================================================
	
	/* FIXME: These don't work! You would have to clone a cached page first, 
	 * before you can perform any kind of template expansion!
	 * 
	private AstNode redirectTo(
	        Redirect n,
	        String target,
	        ResolveRedirectLog log)
	            throws ArticleTitleException, CompilerException, UnsupportedEncodingException
	{
		PageTitle title = new PageTitle(getWikiConfig(), target);
		
		log.setCanonical(title.getFullTitle());
		
		FullPreprocessedPage fpPage =
		        callback.retrievePreprocessedPage(this, title, forInclusion);
		
		if (fpPage == null)
		{
			FullPage page = getWikitext(title, log);
			if (page == null)
			{
				return n;
			}
			else
			{
				CompiledPage ppPage = this.compiler.preprocess(
				        page.getId(),
				        page.getText(),
				        forInclusion,
				        null);
				
				fpPage = new FullPreprocessedPage(
				        page.getId(),
				        forInclusion,
				        ppPage);
				
				callback.cachePreprocessedPage(this, fpPage);
				
				LazyPreprocessedPage lppPage =
				        (LazyPreprocessedPage) ppPage.getPage();
				
				CompiledPage compiledPage = this.compiler.expand(
				        callback,
				        page.getId(),
				        lppPage,
				        forInclusion,
				        arguments,
				        rootFrame,
				        this);
				
				log.setSuccess(true);
				log.getContent().append(compiledPage.getLog());
				
				return ((LazyPreprocessedPage) compiledPage.getPage()).getContent();
			}
		}
		else
		{
			LazyPreprocessedPage lppPage =
			        (LazyPreprocessedPage) fpPage.getPage().getPage();
			
			CompiledPage compiledPage = this.compiler.expand(
			        callback,
			        fpPage.getId(),
			        lppPage,
			        forInclusion,
			        arguments,
			        rootFrame,
			        this);
			
			log.setSuccess(true);
			log.getContent().append(compiledPage.getLog());
			
			return ((LazyPreprocessedPage) compiledPage.getPage()).getContent();
		}
	}
	
	private AstNode transcludePage(
	        Template n,
	        String target,
	        List<TemplateArgument> arguments,
	        ResolveTransclusionLog log)
	            throws ArticleTitleException, CompilerException, UnsupportedEncodingException
	{
		Namespace tmplNs = getWikiConfig().getTemplateNamespace();
		
		PageTitle title = new PageTitle(getWikiConfig(), target, tmplNs);
		
		log.setCanonical(title.getFullTitle());
		
		FullPreprocessedPage fpPage =
		        callback.retrievePreprocessedPage(this, title, true);
		
		if (fpPage == null)
		{
			FullPage page = getWikitext(title, log);
			if (page == null)
			{
				return null;
			}
			else
			{
				Map<String, AstNode> args =
				        prepareTransclusionArguments(arguments);
				
				CompiledPage ppPage = this.compiler.preprocess(
				        page.getId(),
				        page.getText(),
				        true,
				        null);
				
				fpPage = new FullPreprocessedPage(
				        page.getId(),
				        true,
				        ppPage);
				
				callback.cachePreprocessedPage(this, fpPage);
				
				LazyPreprocessedPage lppPage =
				        (LazyPreprocessedPage) ppPage.getPage();
				
				CompiledPage compiledPage = this.compiler.expand(
				        callback,
				        page.getId(),
				        lppPage,
				        true,
				        args,
				        rootFrame,
				        this);
				
				log.setSuccess(true);
				log.getContent().append(compiledPage.getLog());
				
				return ((LazyPreprocessedPage) compiledPage.getPage()).getContent();
			}
		}
		else
		{
			Map<String, AstNode> args =
			        prepareTransclusionArguments(arguments);
			
			LazyPreprocessedPage lppPage =
			        (LazyPreprocessedPage) fpPage.getPage().getPage();
			
			CompiledPage compiledPage = this.compiler.expand(
			        callback,
			        fpPage.getId(),
			        lppPage,
			        true,
			        args,
			        rootFrame,
			        this);
			
			log.setSuccess(true);
			log.getContent().append(compiledPage.getLog());
			
			return ((LazyPreprocessedPage) compiledPage.getPage()).getContent();
		}
	}
	*/

	// =====================================================================
	
	private AstNode redirectTo(
	        Redirect n,
	        String target,
	        ResolveRedirectLog log) throws LinkTargetException, CompilerException
	{
		PageTitle title = PageTitle.make(getWikiConfig(), target);
		
		log.setCanonical(title.getFullTitle());
		
		FullPage page = getWikitext(title, log);
		if (page == null)
		{
			// FIXME: File warning that we couldn't find the page?
			return n;
		}
		else
		{
			CompiledPage compiledPage = this.compiler.preprocessAndExpand(
			        callback,
			        page.getId(),
			        page.getText(),
			        forInclusion,
			        entityMap,
			        arguments,
			        rootFrame,
			        this);
			
			log.setSuccess(true);
			log.getContent().add(compiledPage.getLog());
			
			addAllWarnings(compiledPage.getWarnings());
			
			return compiledPage.getPage().getContent();
		}
	}
	
	private AstNode transcludePage(
	        Template n,
	        String target,
	        List<TemplateArgument> arguments,
	        ResolveTransclusionLog log) throws LinkTargetException, CompilerException
	{
		Namespace tmplNs = getWikiConfig().getTemplateNamespace();
		
		PageTitle title = PageTitle.make(getWikiConfig(), target, tmplNs);
		
		log.setCanonical(title.getFullTitle());
		
		FullPage page = getWikitext(title, log);
		if (page == null)
		{
			// FIXME: File warning that we couldn't find the page?
			return null;
		}
		else
		{
			Map<String, AstNode> args =
			        prepareTransclusionArguments(arguments);
			
			CompiledPage compiledPage = this.compiler.preprocessAndExpand(
			        callback,
			        page.getId(),
			        page.getText(),
			        true,
			        entityMap,
			        args,
			        rootFrame,
			        this);
			
			log.setSuccess(true);
			log.getContent().add(compiledPage.getLog());
			
			addAllWarnings(compiledPage.getWarnings());
			
			return compiledPage.getPage().getContent();
		}
	}
	
	private Map<String, AstNode> prepareTransclusionArguments(List<TemplateArgument> arguments)
	{
		HashMap<String, AstNode> args = new HashMap<String, AstNode>();
		
		for (TemplateArgument arg : arguments)
		{
			String id = String.valueOf(args.size() + 1);
			
			args.put(id, arg.getValue());
			
			if (arg.getHasName())
			{
				try
				{
					String name = StringConverter.convert(arg.getName());
					
					name = name.trim();
					
					if (!name.isEmpty() && !name.equals(id))
						args.put(name, arg.getValue());
				}
				catch (StringConversionException e)
				{
					illegalParameterName(e, arg.getName());
				}
			}
		}
		
		return args;
	}
	
	private AstNode invokeParserFunction(Template template, ParserFunctionBase pfn, List<TemplateArgument> arguments, ResolveTransclusionLog log)
	{
		LinkedList<AstNode> args = new LinkedList<AstNode>();
		
		for (TemplateArgument arg : arguments)
		{
			if (arg.getHasName())
			{
				args.add(new NodeList(
				        arg.getName(),
				        new Text("="),
				        arg.getValue()));
			}
			else
			{
				args.add(arg.getValue());
			}
		}
		
		AstNode result = pfn.invoke(template, this, args);
		
		log.setSuccess(true);
		
		return result;
	}
	
	private FullPage getWikitext(PageTitle title, ContentNode log)
	{
		try
		{
			return callback.retrieveWikitext(this, title);
		}
		catch (Exception e)
		{
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.getContent().add(new UnhandledException(e, w.toString()));
			return null;
		}
	}
}
