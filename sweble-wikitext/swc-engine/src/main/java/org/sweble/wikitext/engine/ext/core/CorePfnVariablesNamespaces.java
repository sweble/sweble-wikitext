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
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wikitext.parser.utils.StringConversionException;

public class CorePfnVariablesNamespaces
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	protected CorePfnVariablesNamespaces(WikiConfig wikiConfig)
	{
		super("Core - Variables - Namespaces");
		addParserFunction(new NamespacePfn(wikiConfig));
		addParserFunction(new TalkspacePfn(wikiConfig));
		addParserFunction(new SubjectspacePfn(wikiConfig));
	}

	public static CorePfnVariablesNamespaces group(WikiConfig wikiConfig)
	{
		return new CorePfnVariablesNamespaces(wikiConfig);
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

		/**
		 * For un-marshaling only.
		 */
		public NamespacePfn()
		{
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "namespace");
		}

		public NamespacePfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "namespace");
		}

		@Override
		public WtNode invoke(
				WtTemplate var,
				ExpansionFrame frame,
				List<? extends WtNode> argsValues)
		{
			PageTitle title;
			if (argsValues.size() > 0)
			{
				WtNode titleNode = argsValues.get(0);

				try
				{
					String titleStr = tu().astToText(titleNode);

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

			return nf().text(title.getNamespace().getName());
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

		/**
		 * For un-marshaling only.
		 */
		public SubjectspacePfn()
		{
			super("subjectspace");
		}

		public SubjectspacePfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, "subjectspace");
		}

		@Override
		protected final WtNode invoke(WtTemplate var, ExpansionFrame frame)
		{
			PageTitle title = frame.getRootFrame().getTitle();

			Namespace talkNs =
					frame.getWikiConfig().getSubjectNamespaceFor(title.getNamespace());

			return nf().text(talkNs.getName());
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

		/**
		 * For un-marshaling only.
		 */
		public TalkspacePfn()
		{
			super("talkspace");
		}

		public TalkspacePfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, "talkspace");
		}

		@Override
		protected final WtNode invoke(WtTemplate var, ExpansionFrame frame)
		{
			PageTitle title = frame.getRootFrame().getTitle();

			Namespace talkNs =
					frame.getWikiConfig().getTalkNamespaceFor(title.getNamespace());

			return nf().text(talkNs.getName());
		}
	}

	// =========================================================================
	// ==
	// == TODO: {{TALKSPACEE}}
	// ==
	// =========================================================================
}
