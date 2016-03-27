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

package org.sweble.wikitext.engine.ext.parser_functions;

import java.util.List;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.utils.StringConversionException;

public class ParserFunctionSwitch
		extends
			ParserFunctionsExtPfn.CtrlStmt
{
	private static final long serialVersionUID = 1L;

	/**
	 * For un-marshaling only.
	 */
	public ParserFunctionSwitch()
	{
		super("switch");
	}

	public ParserFunctionSwitch(WikiConfig wikiConfig)
	{
		super(wikiConfig, "switch");
	}

	@Override
	protected WtNode evaluate(
			WtTemplate pfn,
			ExpansionFrame frame,
			List<? extends WtNode> args)
	{
		if (args.size() < 1)
			return nf().list();

		return new Evaluator(frame, args).evaluate();
	}

	private final class Evaluator
	{
		private ExpansionFrame frame;

		private List<? extends WtNode> args;

		private WtNodeList after;

		private WtNodeList before;

		private WtNode result;

		public Evaluator(ExpansionFrame frame, List<? extends WtNode> args)
		{
			this.frame = frame;
			this.args = args;
		}

		public WtNode evaluate()
		{
			WtNode arg0 = frame.expand(args.get(0));

			String cmp = null;
			Double icmp = null;
			try
			{
				cmp = tu().astToText(arg0).trim();

				icmp = strToDbl(cmp);
			}
			catch (StringConversionException e)
			{
				// FIXME: Do recursive equality check
			}

			boolean found = false;

			result = null;
			for (int i = 1; i < args.size(); ++i)
			{
				// Process each argument of the switch (after the test string)

				after = null;
				before = nf().list();
				if (args.get(i).isNodeType(WtNode.NT_NODE_LIST))
				{
					splitNodeListAtEquals(i);
				}
				else
				{
					WtNode c = args.get(i);
					if (c.isNodeType(WtNode.NT_TEXT))
						splitTextAtEquals(c);
				}

				// Now before holds the stuff in front of the "=" and after
				// contains everything after the "=". If no "=" was found, 
				// before contains everything and after == null.

				if (!found)
					found = compare(cmp, icmp);

				if (found && after != null)
				{
					result = after;
					break;
				}
			}

			if (before != null && after == null && result == null)
			{
				// result == null
				//     We have not encountered an explicit #default case

				// before != null && after == null
				//     The last case didn't have an "=" and therefore is an implicit default

				// $found could be true which means that the implicit default 
				// just happens to match the test string. But that's immaterial.

				result = before;
			}

			return result;
		}

		private boolean compare(String cmp, Double icmp)
		{
			// See if the case statement matches the test string.
			// This is the case if they equal numerically or if the 
			// expanded string representations match. Finally there the
			// $before part could hold "#default", in which case we only
			// set the result to the $after part.

			before = (WtNodeList) frame.expand(before);

			String cmp2;
			try
			{
				cmp2 = tu().astToText(before).trim();
			}
			catch (StringConversionException e)
			{
				// FIXME: Do recursive equality check
				return false;
			}

			if (cmp2.equals("#default"))
			{
				result = after;
				return false;
			}

			if (icmp != null && cmp2 != null)
			{
				Double icmp2 = strToDbl(cmp2);
				if (icmp.equals(icmp2))
					return true;
			}

			return (cmp != null) && cmp.equals(cmp2);
		}

		private Double strToDbl(String str)
		{
			try
			{
				return Double.parseDouble(str);
			}
			catch (NumberFormatException e)
			{
				return null;
			}
		}

		private void splitNodeListAtEquals(int i)
		{
			for (WtNode c : args.get(i))
			{
				if (after == null)
				{
					if (c.isNodeType(WtNode.NT_TEXT))
					{
						splitTextAtEquals(c);
					}
					else
					{
						before.add(c);
					}
				}
				else
				{
					after.add(c);
				}
			}
		}

		private void splitTextAtEquals(WtNode c)
		{
			String text = ((WtText) c).getContent();

			int j = text.indexOf('=');
			if (j != -1)
			{
				before.add(nf().text(text.substring(0, j)));
				after = nf().list(nf().text(text.substring(j + 1)));
			}
			else
			{
				before.add(c);
			}
		}
	}
}
