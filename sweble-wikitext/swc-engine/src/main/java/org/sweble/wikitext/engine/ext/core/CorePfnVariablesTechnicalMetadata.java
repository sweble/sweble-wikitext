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
import org.sweble.wikitext.engine.PfnArgumentMode;
import org.sweble.wikitext.engine.config.ParserFunctionGroup;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTemplate;

public class CorePfnVariablesTechnicalMetadata
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	protected CorePfnVariablesTechnicalMetadata(WikiConfig wikiConfig)
	{
		super("Core - Variables - Technical Metadata");
		addParserFunction(new SitenamePfn(wikiConfig));
		addParserFunction(new ContentLanguagePfn(wikiConfig));
		addParserFunction(new ProtectionLevelPfn(wikiConfig));
		addParserFunction(new DefaultsortPfn(wikiConfig));
	}

	public static CorePfnVariablesTechnicalMetadata group(WikiConfig wikiConfig)
	{
		return new CorePfnVariablesTechnicalMetadata(wikiConfig);
	}

	// =========================================================================
	// ==
	// == Site
	// == ----
	// == TODO: {{SITENAME}}
	// ==
	// =========================================================================

	public static final class SitenamePfn
			extends
				CorePfnVariable
	{
		private static final long serialVersionUID = 1L;

		/**
		 * For un-marshaling only.
		 */
		public SitenamePfn()
		{
			super("sitename");
		}

		public SitenamePfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, "sitename");
		}

		@Override
		public WtNode invoke(
				WtTemplate var,
				ExpansionFrame frame,
				List<? extends WtNode> args)
		{
			return nf().text(frame.getWikiConfig().getSiteName());
		}
	}

	// =========================================================================
	// ==
	// == TODO: {{SERVER}}
	// == TODO: {{SERVERNAME}}
	// == TODO: {{DIRMARK}}, {{DIRECTIONMARK}}
	// == TODO: {{SCRIPTPATH}}
	// == TODO: {{STYLEPATH}}
	// == TODO: {{CURRENTVERSION}}
	// ==
	// =========================================================================

	// =========================================================================
	// ==
	// == {{CONTENTLANGUAGE}}, {{CONTENTLANG}}
	// ==
	// =========================================================================

	public static final class ContentLanguagePfn
			extends
				CorePfnVariable
	{
		private static final long serialVersionUID = 1L;

		/**
		 * For un-marshaling only.
		 */
		public ContentLanguagePfn()
		{
			super("contentlanguage");
		}

		public ContentLanguagePfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, "contentlanguage");
		}

		@Override
		public WtNode invoke(
				WtTemplate var,
				ExpansionFrame frame,
				List<? extends WtNode> args)
		{
			return nf().text(frame.getWikiConfig().getContentLanguage());
		}
	}

	// =========================================================================
	// ==
	// == Latest revision to current page
	// == -------------------------------
	// == TODO: {{REVISIONID}}
	// == TODO: {{REVISIONDAY}}
	// == TODO: {{REVISIONDAY2}}
	// == TODO: {{REVISIONMONTH}}
	// == TODO: {{REVISIONMONTH1}}
	// == TODO: {{REVISIONYEAR}}
	// == TODO: {{REVISIONTIMESTAMP}}
	// == TODO: {{REVISIONUSER}}
	// == TODO: {{PAGESIZE:page name}}, {{PAGESIZE:page name|R}}
	// ==
	// =========================================================================

	// =========================================================================
	// ==
	// == {{PROTECTIONLEVEL:action}}
	// ==
	// =========================================================================

	public static final class ProtectionLevelPfn
			extends
				CorePfnVariable
	{
		private static final long serialVersionUID = 1L;

		/**
		 * For un-marshaling only.
		 */
		public ProtectionLevelPfn()
		{
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "protectionlevel");
		}

		public ProtectionLevelPfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "protectionlevel");
		}

		@Override
		public WtNode invoke(
				WtTemplate var,
				ExpansionFrame frame,
				List<? extends WtNode> args)
		{
			// FIXME: Proper implementation:
			return nf().list();
		}
	}

	// =========================================================================
	// ==
	// == Affects page content
	// == --------------------
	// == TODO: {{DISPLAYTITLE:title}}
	// ==
	// =========================================================================

	// =========================================================================
	// ==
	// == TODO: {{DEFAULTSORT:sortkey}}, {{DEFAULTSORTKEY:sortkey}}, 
	// ==       {{DEFAULTCATEGORYSORT:sortkey}}, {{DEFAULTSORT:sortkey|noerror}}, 
	// ==       {{DEFAULTSORT:sortkey|noreplace}}
	// ==
	// =========================================================================

	public static final class DefaultsortPfn
			extends
				CorePfnVariable
	{
		private static final long serialVersionUID = 1L;

		/**
		 * For un-marshaling only.
		 */
		public DefaultsortPfn()
		{
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "defaultsort");
		}

		public DefaultsortPfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "defaultsort");
		}

		@Override
		public WtNode invoke(
				WtTemplate var,
				ExpansionFrame frame,
				List<? extends WtNode> args)
		{
			// FIXME: Proper implementation:
			return nf().list();
		}
	}
}
