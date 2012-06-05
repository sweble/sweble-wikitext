package org.sweble.wikitext.engine;

import java.util.List;

import org.sweble.wikitext.lazy.parser.MagicWord;
import org.sweble.wikitext.lazy.preprocessor.Redirect;
import org.sweble.wikitext.lazy.preprocessor.TagExtension;
import org.sweble.wikitext.lazy.preprocessor.Template;
import org.sweble.wikitext.lazy.preprocessor.TemplateArgument;
import org.sweble.wikitext.lazy.preprocessor.TemplateParameter;
import org.sweble.wikitext.lazy.utils.StringConversionException;

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
			ExpansionFrame expansionFrame,
			Redirect n,
			String target)
	{
		return PROCEED;
	}
	
	public AstNode afterResolveRedirect(
			ExpansionFrame expansionFrame,
			Redirect n,
			String target,
			AstNode result)
	{
		return result;
	}
	
	public AstNode beforeResolveParserFunction(
			ExpansionFrame expansionFrame,
			Template n,
			String target,
			List<TemplateArgument> arguments)
	{
		return PROCEED;
	}
	
	public AstNode afterResolveParserFunction(
			ExpansionFrame expansionFrame,
			Template n,
			String target,
			List<TemplateArgument> arguments,
			AstNode result)
	{
		return result;
	}
	
	public AstNode beforeResolveTransclusionOrMagicWord(
			ExpansionFrame expansionFrame,
			Template n,
			String target,
			List<TemplateArgument> arguments)
	{
		return PROCEED;
	}
	
	public AstNode resolveTransclusionOrMagicWordBeforeInvokeParserFunction(
			ExpansionFrame expansionFrame,
			Template n,
			ParserFunctionBase pfn,
			List<TemplateArgument> arguments)
	{
		return PROCEED;
	}
	
	public AstNode resolveTransclusionOrMagicWordAfterInvokeParserFunction(
			ExpansionFrame expansionFrame,
			Template n,
			ParserFunctionBase pfn,
			List<TemplateArgument> arguments,
			AstNode result)
	{
		return result;
	}
	
	public AstNode resolveTransclusionOrMagicWordBeforeTranscludePage(
			ExpansionFrame expansionFrame,
			Template n,
			String target,
			List<TemplateArgument> arguments)
	{
		return PROCEED;
	}
	
	public AstNode resolveTransclusionOrMagicWordAfterTranscludePage(
			ExpansionFrame expansionFrame,
			Template n,
			String target,
			List<TemplateArgument> arguments,
			AstNode result)
	{
		return result;
	}
	
	public AstNode afterResolveTransclusionOrMagicWord(
			ExpansionFrame expansionFrame,
			Template n,
			String target,
			List<TemplateArgument> arguments,
			AstNode result)
	{
		return result;
	}
	
	public AstNode beforeResolveParameter(
			ExpansionFrame expansionFrame,
			TemplateParameter n,
			String name)
	{
		return PROCEED;
	}
	
	public AstNode afterResolveParameter(
			ExpansionFrame expansionFrame,
			TemplateParameter n,
			String name,
			AstNode result)
	{
		return result;
	}

	public AstNode beforeResolveTagExtension(
			ExpansionFrame expansionFrame,
			TagExtension n,
			String name,
			NodeList attributes,
			String body)
	{
		return PROCEED;
	}

	public AstNode afterResolveTagExtension(
			ExpansionFrame expansionFrame,
			TagExtension n,
			String name,
			NodeList attributes,
			String body,
			AstNode result)
	{
		return result;
	}

	public AstNode beforeResolveMagicWord(
			ExpansionFrame expansionFrame,
			MagicWord n,
			String word)
	{
		return PROCEED;
	}

	public AstNode afterResolveMagicWord(
			ExpansionFrame expansionFrame,
			MagicWord n,
			String word,
			AstNode result)
	{
		return result;
	}

	public void illegalTemplateName(StringConversionException e, NodeList name)
	{
		// TODO Auto-generated method stub
		
	}

	public void illegalParameterName(StringConversionException e, NodeList name)
	{
		// TODO Auto-generated method stub
		
	}
}
