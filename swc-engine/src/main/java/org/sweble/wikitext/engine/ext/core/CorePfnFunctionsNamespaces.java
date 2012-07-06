package org.sweble.wikitext.engine.ext.core;

import java.util.List;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.config.Namespace;
import org.sweble.wikitext.engine.config.ParserFunctionGroup;
import org.sweble.wikitext.lazy.preprocessor.Template;
import org.sweble.wikitext.lazy.utils.StringConversionException;
import org.sweble.wikitext.lazy.utils.StringConverter;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.Text;

public class CorePfnFunctionsNamespaces
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	protected CorePfnFunctionsNamespaces()
	{
		super("Core - Parser Functions - Namespaces");
		addParserFunction(new NsPfn());
	}
	
	public static CorePfnFunctionsNamespaces group()
	{
		return new CorePfnFunctionsNamespaces();
	}
	
	// =========================================================================
	// ==
	// == {{ns:index}}
	// == {{ns:canonical name}}
	// == {{ns:local alias}}
	// ==
	// =========================================================================
	
	public static final class NsPfn
			extends
				CorePfnFunction
	{
		private static final long serialVersionUID = 1L;
		
		public NsPfn()
		{
			super("ns");
		}
		
		@Override
		public AstNode invoke(
				Template template,
				ExpansionFrame preprocessorFrame,
				List<? extends AstNode> args)
		{
			if (args.size() < 0)
				return null;
			
			AstNode arg0 = preprocessorFrame.expand(args.get(0));
			
			String arg;
			try
			{
				arg = StringConverter.convert(arg0).trim();
			}
			catch (StringConversionException e1)
			{
				return null;
			}
			
			Namespace namespace = preprocessorFrame.getWikiConfig().getNamespace(arg);
			if (namespace == null)
			{
				int ns;
				try
				{
					ns = Integer.parseInt(arg);
				}
				catch (NumberFormatException e)
				{
					return null;
				}
				
				namespace = preprocessorFrame.getWikiConfig().getNamespace(ns);
			}
			
			String result = "";
			if (namespace != null)
				result = namespace.getName();
			
			return new Text(result);
		}
	}
}
