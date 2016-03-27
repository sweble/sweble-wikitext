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
import org.sweble.wikitext.engine.config.Namespace;
import org.sweble.wikitext.engine.config.ParserFunctionGroup;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.utils.StringConversionException;

public class CorePfnFunctionsNamespaces
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	protected CorePfnFunctionsNamespaces(WikiConfig wikiConfig)
	{
		super("Core - Parser Functions - Namespaces");
		addParserFunction(new NsPfn(wikiConfig));
	}

	public static CorePfnFunctionsNamespaces group(WikiConfig wikiConfig)
	{
		return new CorePfnFunctionsNamespaces(wikiConfig);
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

		/**
		 * For un-marshaling only.
		 */
		public NsPfn()
		{
			super("ns");
		}

		public NsPfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, "ns");
		}

		@Override
		public WtNode invoke(
				WtTemplate wtTemplate,
				ExpansionFrame preprocessorFrame,
				List<? extends WtNode> args)
		{
			if (args.size() < 0)
				return null;

			WtNode arg0 = preprocessorFrame.expand(args.get(0));

			String arg;
			try
			{
				arg = tu().astToText(arg0).trim();
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

			return nf().text(result);
		}
	}
}
