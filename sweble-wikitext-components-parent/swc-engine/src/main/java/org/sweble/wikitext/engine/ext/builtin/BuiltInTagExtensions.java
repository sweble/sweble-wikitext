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

package org.sweble.wikitext.engine.ext.builtin;

import java.util.Map;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.TagExtensionBase;
import org.sweble.wikitext.engine.config.TagExtensionGroup;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngineRtData;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtTagExtension;
import org.sweble.wikitext.parser.nodes.WtTagExtensionBody;
import org.sweble.wikitext.parser.nodes.WtXmlElement;

public class BuiltInTagExtensions
		extends
			TagExtensionGroup
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	protected BuiltInTagExtensions(WikiConfig wikiConfig)
	{
		super("Built-in Tag Extensions");
		addTagExtension(new TagExtensionPre(wikiConfig));
		addTagExtension(new TagExtensionNowiki(wikiConfig));
	}

	public static BuiltInTagExtensions group(WikiConfig wikiConfig)
	{
		return new BuiltInTagExtensions(wikiConfig);
	}

	// =========================================================================
	// ==
	// == <pre>
	// ==
	// =========================================================================

	public static final class TagExtensionPre
			extends
				TagExtensionBase
	{
		private static final long serialVersionUID = 1L;

		/**
		 * For un-marshaling only.
		 */
		public TagExtensionPre()
		{
			super("pre");
		}

		public TagExtensionPre(WikiConfig wikiConfig)
		{
			super(wikiConfig, "pre");
		}

		@Override
		public WtNode invoke(
				ExpansionFrame frame,
				WtTagExtension tagExt,
				Map<String, WtNodeList> attrs,
				WtTagExtensionBody body)
		{
			WtXmlElement pre = nf().elem(
					"pre",
					tagExt.getXmlAttributes(),
					nf().body(nf().list(nf().text(body.getContent()))));
			pre.setRtd(tagExt.getRtd());
			return pre;
		}
	}

	// =========================================================================
	// ==
	// == <nowiki>
	// ==
	// =========================================================================

	public static final class TagExtensionNowiki
			extends
				TagExtensionBase
	{
		private static final long serialVersionUID = 1L;

		/**
		 * For un-marshaling only.
		 */
		public TagExtensionNowiki()
		{
			super("nowiki");
		}

		public TagExtensionNowiki(WikiConfig wikiConfig)
		{
			super(wikiConfig, "nowiki");
		}

		@Override
		public WtNode invoke(
				ExpansionFrame frame,
				WtTagExtension tagExt,
				Map<String, WtNodeList> attrs,
				WtTagExtensionBody body)
		{
			return EngineRtData.set(nf().nowiki(body.getContent()));
		}
	}
}
