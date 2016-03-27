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

import org.apache.commons.lang3.StringUtils;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.PfnArgumentMode;
import org.sweble.wikitext.engine.config.ParserFunctionGroup;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.utils.ApplyToText;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.utils.StringConversionException;

public class CorePfnFunctionsFormatting
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	protected CorePfnFunctionsFormatting(WikiConfig wikiConfig)
	{
		super("Core - Parser Functions - Formatting");
		addParserFunction(new LcPfn(wikiConfig));
		addParserFunction(new LcFirstPfn(wikiConfig));
		addParserFunction(new UcPfn(wikiConfig));
		addParserFunction(new UcFirstPfn(wikiConfig));
		addParserFunction(new PadLeftPfn(wikiConfig));
	}

	public static CorePfnFunctionsFormatting group(WikiConfig wikiConfig)
	{
		return new CorePfnFunctionsFormatting(wikiConfig);
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

		/**
		 * For un-marshaling only.
		 */
		public LcPfn()
		{
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "lc");
		}

		public LcPfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "lc");
		}

		@Override
		public WtNode invoke(
				WtTemplate pfn,
				ExpansionFrame preprocessorFrame,
				List<? extends WtNode> args)
		{
			if (args.size() < 1)
				return nf().list();

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

		/**
		 * For un-marshaling only.
		 */
		public LcFirstPfn()
		{
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "lcfirst");
		}

		public LcFirstPfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "lcfirst");
		}

		@Override
		public WtNode invoke(
				WtTemplate pfn,
				ExpansionFrame preprocessorFrame,
				List<? extends WtNode> args)
		{
			if (args.size() < 1)
				return nf().list();

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

		/**
		 * For un-marshaling only.
		 */
		public UcPfn()
		{
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "uc");
		}

		public UcPfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "uc");
		}

		@Override
		public WtNode invoke(
				WtTemplate pfn,
				ExpansionFrame preprocessorFrame,
				List<? extends WtNode> args)
		{
			if (args.size() < 1)
				return nf().list();

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

		/**
		 * For un-marshaling only.
		 */
		public UcFirstPfn()
		{
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "ucfirst");
		}

		public UcFirstPfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "ucfirst");
		}

		@Override
		public WtNode invoke(
				WtTemplate pfn,
				ExpansionFrame preprocessorFrame,
				List<? extends WtNode> args)
		{
			if (args.size() < 1)
				return nf().list();

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

		/**
		 * For un-marshaling only.
		 */
		public PadLeftPfn()
		{
			super(PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "padleft");
		}

		public PadLeftPfn(WikiConfig wikiConfig)
		{
			super(wikiConfig, PfnArgumentMode.EXPANDED_AND_TRIMMED_VALUES, "padleft");
		}

		@Override
		public WtNode invoke(
				WtTemplate pfn,
				ExpansionFrame frame,
				List<? extends WtNode> args)
		{
			if (args.size() < 1)
				return nf().list();

			WtNode arg0 = frame.expand(args.get(0));

			if (args.size() < 2)
				return arg0;

			int len;
			String text;
			String padStr = "0";
			try
			{
				text = tu().astToText(arg0).trim();

				WtNode arg1 = frame.expand(args.get(1));
				String lenStr = tu().astToText(arg1).trim();
				len = Integer.parseInt(lenStr);
				if (len <= 0)
					return arg0;

				if (args.size() >= 3)
				{
					WtNode arg2 = frame.expand(args.get(2));
					try
					{
						padStr = tu().astToText(arg2);//.trim();
						/* Trimming the pad string can lead to division by zero 
						 * divisions. Of course an empty pad string doesn't make 
						 * sense. But I'm not sure, if the pad string should
						 * be trimmed in the first place. After all, padding with
						 * spaces makes perfect sense... */
					}
					catch (StringConversionException e)
					{
					}

					if (padStr.isEmpty())
						return arg0;
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

			return nf().text(padding + text);
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
