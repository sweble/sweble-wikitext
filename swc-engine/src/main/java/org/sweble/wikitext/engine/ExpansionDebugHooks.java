package org.sweble.wikitext.engine;

import java.util.List;

import org.sweble.wikitext.engine.log.ResolveMagicWordLog;
import org.sweble.wikitext.engine.log.ResolveParameterLog;
import org.sweble.wikitext.engine.log.ResolveParserFunctionLog;
import org.sweble.wikitext.engine.log.ResolveRedirectLog;
import org.sweble.wikitext.engine.log.ResolveTagExtensionLog;
import org.sweble.wikitext.engine.log.ResolveTransclusionLog;
import org.sweble.wikitext.lazy.parser.MagicWord;
import org.sweble.wikitext.lazy.preprocessor.Redirect;
import org.sweble.wikitext.lazy.preprocessor.TagExtension;
import org.sweble.wikitext.lazy.preprocessor.Template;
import org.sweble.wikitext.lazy.preprocessor.TemplateArgument;
import org.sweble.wikitext.lazy.preprocessor.TemplateParameter;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public abstract class ExpansionDebugHooks
{
	public static final AstNode PROCEED = new AstNode()
	{
		private static final long serialVersionUID = 1L;
	};
	
	// =========================================================================
	
	public AstNode beforeResolveRedirect(
			ExpansionVisitor expansionVisitor,
			Redirect n,
			String target)
	{
		return PROCEED;
	}
	
	public AstNode afterResolveRedirect(
			ExpansionVisitor expansionVisitor,
			Redirect n,
			String target,
			AstNode result,
			ResolveRedirectLog log)
	{
		return result;
	}
	
	public AstNode beforeResolveParserFunction(
			ExpansionVisitor expansionVisitor,
			Template n,
			ParserFunctionBase pfn,
			List<? extends AstNode> argsValues)
	{
		return PROCEED;
	}
	
	public AstNode afterResolveParserFunction(
			ExpansionVisitor expansionVisitor,
			Template n,
			ParserFunctionBase pfn,
			List<? extends AstNode> argsValues,
			AstNode result,
			ResolveParserFunctionLog log)
	{
		return result;
	}
	
	public AstNode beforeResolveTransclusion(
			ExpansionVisitor expansionVisitor,
			Template n,
			String target,
			List<TemplateArgument> args)
	{
		return PROCEED;
	}
	
	public AstNode afterResolveTransclusion(
			ExpansionVisitor expansionVisitor,
			Template n,
			String target,
			List<TemplateArgument> args,
			AstNode result,
			ResolveTransclusionLog log)
	{
		return result;
	}
	
	public AstNode beforeResolveParameter(
			ExpansionVisitor expansionVisitor,
			TemplateParameter n,
			String name)
	{
		return PROCEED;
	}
	
	public AstNode afterResolveParameter(
			ExpansionVisitor expansionVisitor,
			TemplateParameter n,
			String name,
			AstNode result,
			ResolveParameterLog log)
	{
		return result;
	}
	
	public AstNode beforeResolveTagExtension(
			ExpansionVisitor expansionVisitor,
			TagExtension n,
			String name,
			NodeList attrs,
			String body)
	{
		return PROCEED;
	}
	
	public AstNode afterResolveTagExtension(
			ExpansionVisitor expansionVisitor,
			TagExtension n,
			String name,
			NodeList attributes,
			String body,
			AstNode result,
			ResolveTagExtensionLog log)
	{
		return result;
	}
	
	public AstNode beforeResolveMagicWord(
			ExpansionVisitor expansionVisitor,
			MagicWord n,
			String word)
	{
		return PROCEED;
	}
	
	public AstNode afterResolveMagicWord(
			ExpansionVisitor expansionVisitor,
			MagicWord n,
			String word,
			AstNode result,
			ResolveMagicWordLog log)
	{
		return result;
	}
}
