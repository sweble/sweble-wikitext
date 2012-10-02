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
import org.sweble.wikitext.parser.nodes.Template;
import org.sweble.wikitext.parser.nodes.WikitextNode;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.utils.StringConversionException;
import org.sweble.wikitext.parser.utils.StringConverter;

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
		public WikitextNode invoke(
				Template template,
				ExpansionFrame preprocessorFrame,
				List<? extends WikitextNode> args)
		{
			if (args.size() < 0)
				return null;
			
			WikitextNode arg0 = preprocessorFrame.expand(args.get(0));
			
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
			
			return new WtText(result);
		}
	}
}
