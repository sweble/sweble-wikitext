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

import static org.sweble.wikitext.parser.utils.AstBuilder.astText;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.PfnArgumentMode;
import org.sweble.wikitext.engine.config.ParserFunctionGroup;
import org.sweble.wikitext.engine.utils.ApplyToText;
import org.sweble.wikitext.parser.nodes.Template;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.utils.StringConversionException;
import org.sweble.wikitext.parser.utils.StringConverter;

public class CorePfnFunctionsFormatting
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	protected CorePfnFunctionsFormatting()
	{
		super("Core - Parser Functions - Formatting");
		addParserFunction(new LcPfn());
		addParserFunction(new LcFirstPfn());
		addParserFunction(new UcPfn());
		addParserFunction(new UcFirstPfn());
		addParserFunction(new PadLeftPfn());
	}
	
	public static CorePfnFunctionsFormatting group()
	{
		return new CorePfnFunctionsFormatting();
	}
	
	// =========================================================================
	// ==
	// == TODO: {{formatnum:unformatted num}}
	// ==       {{formatnum:formatted num|R}}
	// == TODO: {{#dateformat:date}}
	// ==       {{#formatdate:date}}
	// ==       {{#dateformat:date|format}}
	// ==       {{#formatdate:date|format}}
	// ==
	// =========================================================================
	
	// =========================================================================
	// ==
	// == {{lc:string}}
	// ==
	// =========================================================================
	
	public static final class LcPfn
			extends
				CorePfnFunction
	{
		private static final long serialVersionUID = 1L;
		
		public LcPfn()
		{
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "lc");
		}
		
		@Override
		public WtNode invoke(
				Template pfn,
				ExpansionFrame preprocessorFrame,
				List<? extends WtNode> args)
		{
			if (args.size() < 1)
				return new WtNodeList();
			
			new ApplyToText(new ApplyToText.Functor()
			{
				@Override
				public String apply(String text)
				{
					return text.toLowerCase();
				}
			}).go(args.get(0));
			
			return args.get(0);
		}
	}
	
	// =========================================================================
	// ==
	// == {{lcfirst:string}}
	// ==
	// =========================================================================
	
	public static final class LcFirstPfn
			extends
				CorePfnFunction
	{
		private static final long serialVersionUID = 1L;
		
		public LcFirstPfn()
		{
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "lcfirst");
		}
		
		@Override
		public WtNode invoke(
				Template pfn,
				ExpansionFrame preprocessorFrame,
				List<? extends WtNode> args)
		{
			if (args.size() < 1)
				return new WtNodeList();
			
			new ApplyToText(new ApplyToText.Functor()
			{
				@Override
				public String apply(String text)
				{
					if (text.isEmpty())
						return text;
					return text.substring(0, 1).toLowerCase() + text.substring(1);
				}
			}).go(args.get(0));
			
			return args.get(0);
		}
	}
	
	// =========================================================================
	// ==
	// == {{uc:string}}
	// ==
	// =========================================================================
	
	public static final class UcPfn
			extends
				CorePfnFunction
	{
		private static final long serialVersionUID = 1L;
		
		public UcPfn()
		{
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "uc");
		}
		
		@Override
		public WtNode invoke(
				Template pfn,
				ExpansionFrame preprocessorFrame,
				List<? extends WtNode> args)
		{
			if (args.size() < 1)
				return new WtNodeList();
			
			new ApplyToText(new ApplyToText.Functor()
			{
				@Override
				public String apply(String text)
				{
					return text.toUpperCase();
				}
			}).go(args.get(0));
			
			return args.get(0);
		}
	}
	
	// =========================================================================
	// ==
	// == {{ucfirst:string}}
	// ==
	// =========================================================================
	
	public static final class UcFirstPfn
			extends
				CorePfnFunction
	{
		private static final long serialVersionUID = 1L;
		
		public UcFirstPfn()
		{
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "ucfirst");
		}
		
		@Override
		public WtNode invoke(
				Template pfn,
				ExpansionFrame preprocessorFrame,
				List<? extends WtNode> args)
		{
			if (args.size() < 1)
				return new WtNodeList();
			
			new ApplyToText(new ApplyToText.Functor()
			{
				@Override
				public String apply(String text)
				{
					if (text.isEmpty())
						return text;
					return text.substring(0, 1).toUpperCase() + text.substring(1);
				}
			}).go(args.get(0));
			
			return args.get(0);
		}
	}
	
	// =========================================================================
	// ==
	// == {{padleft:xyz|stringlength}}
	// == {{padleft:xyz|strlen|char}}
	// == {{padleft:xyz|strlen|string}}
	// ==
	// =========================================================================
	
	public static final class PadLeftPfn
			extends
				CorePfnFunction
	{
		private static final long serialVersionUID = 1L;
		
		public PadLeftPfn()
		{
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "padleft");
		}
		
		@Override
		public WtNode invoke(
				Template pfn,
				ExpansionFrame frame,
				List<? extends WtNode> args)
		{
			if (args.size() < 1)
				return new WtNodeList();
			
			WtNode arg0 = frame.expand(args.get(0));
			
			if (args.size() < 2)
				return arg0;
			
			int len;
			String text;
			String padStr = "0";
			try
			{
				text = StringConverter.convert(arg0).trim();
				
				WtNode arg1 = frame.expand(args.get(1));
				String lenStr = StringConverter.convert(arg1).trim();
				len = Integer.parseInt(lenStr);
				if (len <= 0)
					return arg0;
				
				if (args.size() >= 3)
				{
					WtNode arg2 = frame.expand(args.get(2));
					try
					{
						padStr = StringConverter.convert(arg2).trim();
					}
					catch (StringConversionException e)
					{
					}
				}
			}
			catch (StringConversionException e)
			{
				return arg0;
			}
			catch (NumberFormatException e)
			{
				return arg0;
			}
			
			int padLen = len - text.length();
			if (padLen <= 0)
				return arg0;
			
			int repeat = 1 + ((padLen - 1) / padStr.length());
			String padding = StringUtils.repeat(padStr, repeat);
			padding = padding.substring(0, padLen);
			
			return astText(padding + text);
		}
	}
	
	// =========================================================================
	// ==
	// == TODO: {{padright:xyz|stringlength}}
	// ==       {{padright:xyz|strlen|char}}
	// ==       {{padright:xyz|strlen|string}}
	// ==
	// =========================================================================
}
