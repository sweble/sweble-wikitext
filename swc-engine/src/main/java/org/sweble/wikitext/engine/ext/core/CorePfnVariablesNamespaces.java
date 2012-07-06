package org.sweble.wikitext.engine.ext.core;

import java.util.List;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.PfnArgumentMode;
import org.sweble.wikitext.engine.config.Namespace;
import org.sweble.wikitext.engine.config.ParserFunctionGroup;
import org.sweble.wikitext.lazy.LinkTargetException;
import org.sweble.wikitext.lazy.preprocessor.Template;
import org.sweble.wikitext.lazy.utils.StringConversionException;
import org.sweble.wikitext.lazy.utils.StringConverter;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.Text;

public class CorePfnVariablesNamespaces
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	protected CorePfnVariablesNamespaces()
	{
		super("Core - Variables - Namespaces");
		addParserFunction(new NamespacePfn());
		addParserFunction(new TalkspacePfn());
		addParserFunction(new SubjectspacePfn());
	}
	
	public static CorePfnVariablesNamespaces group()
	{
		return new CorePfnVariablesNamespaces();
	}
	
	// =========================================================================
	// ==
	// == {{NAMESPACE}}
	// ==
	// =========================================================================
	
	public static final class NamespacePfn
			extends
				CorePfnVariable
	{
		private static final long serialVersionUID = 1L;
		
		public NamespacePfn()
		{
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "namespace");
		}
		
		@Override
		public AstNode invoke(
				Template var,
				ExpansionFrame frame,
				List<? extends AstNode> argsValues)
		{
			PageTitle title;
			if (argsValues.size() > 0)
			{
				AstNode titleNode = argsValues.get(0);
				
				try
				{
					String titleStr = StringConverter.convert(titleNode);
					
					title = PageTitle.make(frame.getWikiConfig(), titleStr);
				}
				catch (StringConversionException e)
				{
					return var;
				}
				catch (LinkTargetException e)
				{
					return var;
				}
			}
			else
			{
				title = frame.getRootFrame().getTitle();
			}
			
			return new Text(title.getNamespace().getName());
		}
	}
	
	// =========================================================================
	// ==
	// == TODO: {{NAMESPACEE}}
	// == TODO: {{NAMESPACENUMBER}}
	// ==
	// =========================================================================
	
	// =========================================================================
	// ==
	// == TODO: {{SUBJECTSPACE}}, {{ARTICLESPACE}}
	// ==
	// =========================================================================
	
	public static final class SubjectspacePfn
			extends
				CorePfnVariable
	{
		private static final long serialVersionUID = 1L;
		
		public SubjectspacePfn()
		{
			super("subjectspace");
		}
		
		@Override
		protected final AstNode invoke(Template var, ExpansionFrame frame)
		{
			PageTitle title = frame.getRootFrame().getTitle();
			
			Namespace talkNs =
					frame.getWikiConfig().getSubjectNamespaceFor(title.getNamespace());
			
			return new Text(talkNs.getName());
		}
	}
	
	// =========================================================================
	// ==
	// == TODO: {{SUBJECTSPACEE}}, {{ARTICLESPACEE}}
	// ==
	// =========================================================================
	
	// =========================================================================
	// ==
	// == {{TALKSPACE}}
	// ==
	// =========================================================================
	
	public static final class TalkspacePfn
			extends
				CorePfnVariable
	{
		private static final long serialVersionUID = 1L;
		
		public TalkspacePfn()
		{
			super("talkspace");
		}
		
		@Override
		protected final AstNode invoke(Template var, ExpansionFrame frame)
		{
			PageTitle title = frame.getRootFrame().getTitle();
			
			Namespace talkNs =
					frame.getWikiConfig().getTalkNamespaceFor(title.getNamespace());
			
			return new Text(talkNs.getName());
		}
	}
	
	// =========================================================================
	// ==
	// == TODO: {{TALKSPACEE}}
	// ==
	// =========================================================================
}
