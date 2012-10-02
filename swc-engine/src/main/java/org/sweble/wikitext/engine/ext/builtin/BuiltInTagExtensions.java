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
import org.sweble.wikitext.parser.nodes.TagExtension;
import org.sweble.wikitext.parser.nodes.WikitextNode;
import org.sweble.wikitext.parser.nodes.WtList;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.XmlElement;
import org.sweble.wikitext.parser.preprocessor.ProtectedText;

public class BuiltInTagExtensions
		extends
			TagExtensionGroup
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	protected BuiltInTagExtensions()
	{
		super("Built-in Tag Extensions");
		addTagExtension(new TagExtensionPre());
		addTagExtension(new TagExtensionNoWiki());
	}
	
	public static BuiltInTagExtensions group()
	{
		return new BuiltInTagExtensions();
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
		
		public TagExtensionPre()
		{
			super("pre");
		}
		
		@Override
		public WikitextNode invoke(
				ExpansionFrame frame,
				TagExtension tagExt,
				Map<String, WtList> attrs,
				String body)
		{
			return new XmlElement(
					"pre",
					false,
					tagExt.getXmlAttributes(),
					new WtList(new WtText(body)));
		}
	}
	
	// =========================================================================
	// ==
	// == <nowiki>
	// ==
	// =========================================================================
	
	public static final class TagExtensionNoWiki
			extends
				TagExtensionBase
	{
		private static final long serialVersionUID = 1L;
		
		public TagExtensionNoWiki()
		{
			super("nowiki");
		}
		
		@Override
		public WikitextNode invoke(
				ExpansionFrame frame,
				TagExtension tagExt,
				Map<String, WtList> attrs,
				String body)
		{
			ProtectedText pt;
			if (tagExt.getBody() == null)
			{
				pt = new ProtectedText("");
				pt.setRtd("<nowiki />");
			}
			else
			{
				pt = new ProtectedText(body);
				pt.setRtd("<nowiki>", tagExt.getBody(), "</nowiki>");
			}
			return pt;
		}
	}
	
}
