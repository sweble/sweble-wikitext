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

package org.sweble.wikitext.engine.ext.core;

import static org.sweble.wikitext.parser.utils.AstBuilder.*;

import java.util.List;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.PfnArgumentMode;
import org.sweble.wikitext.engine.config.Namespace;
import org.sweble.wikitext.engine.config.ParserFunctionGroup;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.utils.UrlEncoding;
import org.sweble.wikitext.parser.LinkTargetException;
import org.sweble.wikitext.parser.nodes.Template;
import org.sweble.wikitext.parser.utils.StringConversionException;
import org.sweble.wikitext.parser.utils.StringConverter;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.Text;

public class CorePfnVariablesPageNames
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	protected CorePfnVariablesPageNames()
	{
		super("Core - Variables - Page names");
		addParserFunction(new FullPagenamePfn());
		addParserFunction(new FullPagenameePfn());
		addParserFunction(new PagenamePfn());
		addParserFunction(new PagenameePfn());
		addParserFunction(new BasePagenamePfn());
		addParserFunction(new SubjectPagenamePfn());
		addParserFunction(new TalkPagenamePfn());
	}
	
	public static CorePfnVariablesPageNames group()
	{
		return new CorePfnVariablesPageNames();
	}
	
	// =========================================================================
	// ==
	// == {{FULLPAGENAME}}
	// ==
	// =========================================================================
	
	public static final class FullPagenamePfn
			extends
				CorePfnVariable
	{
		private static final long serialVersionUID = 1L;
		
		public FullPagenamePfn()
		{
			super("fullpagename");
		}
		
		@Override
		protected final AstNode invoke(Template var, ExpansionFrame frame)
		{
			return new Text(frame.getRootFrame().getTitle().getDenormalizedFullTitle());
		}
	}
	
	// =========================================================================
	// ==
	// == {{FULLPAGENAMEE}}
	// ==
	// =========================================================================
	
	public static final class FullPagenameePfn
			extends
				CorePfnVariable
	{
		private static final long serialVersionUID = 1L;
		
		public FullPagenameePfn()
		{
			super("fullpagenamee");
		}
		
		@Override
		protected final AstNode invoke(Template var, ExpansionFrame frame)
		{
			return astText(UrlEncoding.WIKI.encode(frame.getRootFrame().getTitle().getNormalizedFullTitle()));
		}
	}
	
	// =========================================================================
	// ==
	// == {{PAGENAME}}
	// ==
	// =========================================================================
	
	public static final class PagenamePfn
			extends
				CorePfnVariable
	{
		private static final long serialVersionUID = 1L;
		
		public PagenamePfn()
		{
			super("pagename");
		}
		
		@Override
		protected final AstNode invoke(Template var, ExpansionFrame frame)
		{
			return new Text(frame.getRootFrame().getTitle().getDenormalizedTitle());
		}
	}
	
	// =========================================================================
	// ==
	// == {{PAGENAMEE}}
	// ==
	// =========================================================================
	
	public static final class PagenameePfn
			extends
				CorePfnVariable
	{
		private static final long serialVersionUID = 1L;
		
		public PagenameePfn()
		{
			// FIXME: DIESEN FIX FUER ALLE!
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "pagenamee");
		}
		
		@Override
		public AstNode invoke(
				Template var,
				ExpansionFrame frame,
				List<? extends AstNode> argsValues)
		{
			PageTitle title = frame.getRootFrame().getTitle();
			
			if (!argsValues.isEmpty())
			{
				try
				{
					String titleStr = StringConverter.convert(argsValues.get(0)).trim();
					
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
			
			String link = title.getTitle();
			return new Text(UrlEncoding.WIKI.encode(link));
		}
	}
	
	// =========================================================================
	// ==
	// == {{BASEPAGENAME}}
	// ==
	// =========================================================================
	
	public static final class BasePagenamePfn
			extends
				CorePfnVariable
	{
		private static final long serialVersionUID = 1L;
		
		public BasePagenamePfn()
		{
			super("basepagename");
		}
		
		@Override
		protected final AstNode invoke(Template var, ExpansionFrame frame)
		{
			return new Text(frame.getRootFrame().getTitle().getBaseTitle().getDenormalizedFullTitle());
		}
	}
	
	// =========================================================================
	// ==
	// == TODO: {{BASEPAGENAMEE}}
	// == TODO: {{SUBPAGENAME}}
	// == TODO: {{SUBPAGENAMEE}}
	// ==
	// =========================================================================
	
	// =========================================================================
	// ==
	// == {{SUBJECTPAGENAME}}, {{ARTICLEPAGENAME}}
	// ==
	// =========================================================================
	
	public static final class SubjectPagenamePfn
			extends
				CorePfnVariable
	{
		private static final long serialVersionUID = 1L;
		
		public SubjectPagenamePfn()
		{
			super("subjectpagename");
		}
		
		@Override
		protected final AstNode invoke(Template var, ExpansionFrame frame)
		{
			return invokeStatic(frame);
		}
		
		protected static AstNode invokeStatic(ExpansionFrame frame)
		{
			WikiConfig config = frame.getWikiConfig();
			
			PageTitle title = frame.getRootFrame().getTitle();
			
			Namespace ns = title.getNamespace();
			Namespace subjectNs = config.getSubjectNamespaceFor(ns);
			if (subjectNs != ns)
				title = title.newWithNamespace(subjectNs);
			
			return new Text(title.getDenormalizedFullTitle());
		}
	}
	
	// =========================================================================
	// ==
	// == TODO: {{SUBJECTPAGENAMEE}}, {{ARTICLEPAGENAMEE}}
	// ==
	// =========================================================================
	
	// =========================================================================
	// ==
	// == {{TALKPAGENAME}}
	// ==
	// =========================================================================
	
	public static final class TalkPagenamePfn
			extends
				CorePfnVariable
	{
		private static final long serialVersionUID = 1L;
		
		public TalkPagenamePfn()
		{
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "talkpagename");
		}
		
		/*
		@Override
		protected final AstNode invoke(Template var, ExpansionFrame frame)
		{
			WikiConfig config = frame.getWikiConfig();
			
			PageTitle title = frame.getRootFrame().getTitle();
			
			Namespace ns = title.getNamespace();
			Namespace talkNs = config.getTalkNamespaceFor(ns);
			if (talkNs != ns)
				title = title.newWithNamespace(talkNs);
			
			return new Text(title.getDenormalizedFullTitle());
		}
		*/
		
		@Override
		public AstNode invoke(
				Template var,
				ExpansionFrame frame,
				List<? extends AstNode> argsValues)
		{
			PageTitle title = frame.getRootFrame().getTitle();
			
			if (!argsValues.isEmpty())
			{
				try
				{
					String titleStr = StringConverter.convert(argsValues.get(0)).trim();
					
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
			
			//String link = title.getTitle();
			//return new Text(UrlEncoding.WIKI.encode(link));
			
			WikiConfig config = frame.getWikiConfig();
			
			Namespace ns = title.getNamespace();
			Namespace talkNs = config.getTalkNamespaceFor(ns);
			if (talkNs != ns)
				title = title.newWithNamespace(talkNs);
			
			return new Text(title.getDenormalizedFullTitle());
		}
	}
	
	// =========================================================================
	// ==
	// == TODO: {{TALKPAGENAMEE}}
	// ==
	// =========================================================================
}
