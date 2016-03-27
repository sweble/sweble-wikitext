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

import java.util.List;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.PfnArgumentMode;
import org.sweble.wikitext.engine.config.Namespace;
import org.sweble.wikitext.engine.config.ParserFunctionGroup;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.utils.UrlEncoding;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wikitext.parser.utils.StringConversionException;

public class CorePfnVariablesPageNames
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	protected CorePfnVariablesPageNames(WikiConfig wikiConfig)
	{
		super("Core - Variables - Page names");
		addParserFunction(new FullPagenamePfn(wikiConfig));
		addParserFunction(new FullPagenameePfn(wikiConfig));
		addParserFunction(new PagenamePfn(wikiConfig));
		addParserFunction(new PagenameePfn(wikiConfig));
		addParserFunction(new BasePagenamePfn(wikiConfig));
		addParserFunction(new SubjectPagenamePfn(wikiConfig));
		addParserFunction(new TalkPagenamePfn(wikiConfig));
	}

	public static CorePfnVariablesPageNames group(WikiConfig wikiConfig)
	{
		return new CorePfnVariablesPageNames(wikiConfig);
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

		/**
		 * For un-marshaling only.
		 */
		public FullPagenamePfn()
		{
			super("fullpagename");
		}

		public FullPagenamePfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, "fullpagename");
		}

		@Override
		protected final WtNode invoke(WtTemplate var, ExpansionFrame frame)
		{
			return nf().text(frame.getRootFrame().getTitle().getDenormalizedFullTitle());
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

		/**
		 * For un-marshaling only.
		 */
		public FullPagenameePfn()
		{
			super("fullpagenamee");
		}

		public FullPagenameePfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, "fullpagenamee");
		}

		@Override
		protected final WtNode invoke(WtTemplate var, ExpansionFrame frame)
		{
			return nf().text(UrlEncoding.WIKI.encode(frame.getRootFrame().getTitle().getNormalizedFullTitle()));
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

		/**
		 * For un-marshaling only.
		 */
		public PagenamePfn()
		{
			super("pagename");
		}

		public PagenamePfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, "pagename");
		}

		@Override
		protected final WtNode invoke(WtTemplate var, ExpansionFrame frame)
		{
			return nf().text(frame.getRootFrame().getTitle().getDenormalizedTitle());
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

		/**
		 * For un-marshaling only.
		 */
		public PagenameePfn()
		{
			// FIXME: DIESEN FIX FUER ALLE!
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "pagenamee");
		}

		public PagenameePfn(WikiConfig wikiConfig)
		{
			// FIXME: DIESEN FIX FUER ALLE!
			super(wikiConfig, PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "pagenamee");
		}

		@Override
		public WtNode invoke(
				WtTemplate var,
				ExpansionFrame frame,
				List<? extends WtNode> argsValues)
		{
			PageTitle title = frame.getRootFrame().getTitle();

			if (!argsValues.isEmpty())
			{
				try
				{
					String titleStr = tu().astToText(argsValues.get(0)).trim();

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
			return nf().text(UrlEncoding.WIKI.encode(link));
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

		/**
		 * For un-marshaling only.
		 */
		public BasePagenamePfn()
		{
			super("basepagename");
		}

		public BasePagenamePfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, "basepagename");
		}

		@Override
		protected final WtNode invoke(WtTemplate var, ExpansionFrame frame)
		{
			return nf().text(frame.getRootFrame().getTitle().getBaseTitle().getDenormalizedFullTitle());
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

		/**
		 * For un-marshaling only.
		 */
		public SubjectPagenamePfn()
		{
			super("subjectpagename");
		}

		public SubjectPagenamePfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, "subjectpagename");
		}

		@Override
		protected final WtNode invoke(WtTemplate var, ExpansionFrame frame)
		{
			return invokeStatic(frame);
		}

		protected WtNode invokeStatic(ExpansionFrame frame)
		{
			WikiConfig config = frame.getWikiConfig();

			PageTitle title = frame.getRootFrame().getTitle();

			Namespace ns = title.getNamespace();
			Namespace subjectNs = config.getSubjectNamespaceFor(ns);
			if (subjectNs != ns)
				title = title.newWithNamespace(subjectNs);

			return nf().text(title.getDenormalizedFullTitle());
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

		/**
		 * For un-marshaling only.
		 */
		public TalkPagenamePfn()
		{
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "talkpagename");
		}

		public TalkPagenamePfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "talkpagename");
		}

		/*
		@Override
		protected final WtNode invoke(WtTemplate var, ExpansionFrame frame)
		{
			WikiConfig config = frame.getWikiConfig();
			
			PageTitle title = frame.getRootFrame().getTitle();
			
			Namespace ns = title.getNamespace();
			Namespace talkNs = config.getTalkNamespaceFor(ns);
			if (talkNs != ns)
				title = title.newWithNamespace(talkNs);
			
			return nf().text(title.getDenormalizedFullTitle());
		}
		*/

		@Override
		public WtNode invoke(
				WtTemplate var,
				ExpansionFrame frame,
				List<? extends WtNode> argsValues)
		{
			PageTitle title = frame.getRootFrame().getTitle();

			if (!argsValues.isEmpty())
			{
				try
				{
					String titleStr = tu().astToText(argsValues.get(0)).trim();

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
			//return nf().text(UrlEncoding.WIKI.encode(link));

			WikiConfig config = frame.getWikiConfig();

			Namespace ns = title.getNamespace();
			Namespace talkNs = config.getTalkNamespaceFor(ns);
			if (talkNs != ns)
				title = title.newWithNamespace(talkNs);

			return nf().text(title.getDenormalizedFullTitle());
		}
	}

	// =========================================================================
	// ==
	// == TODO: {{TALKPAGENAMEE}}
	// ==
	// =========================================================================
}
