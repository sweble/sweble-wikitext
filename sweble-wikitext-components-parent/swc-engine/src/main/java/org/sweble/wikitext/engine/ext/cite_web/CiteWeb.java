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

package org.sweble.wikitext.engine.ext.cite_web;

import de.fau.cs.osr.utils.StringTools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngSoftErrorNode;
import org.sweble.wikitext.engine.nodes.EngineRtData;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.StringConversionException;

public class CiteWeb
		extends
			ParserFunctionBase
{
	private static int MIN_ARGS = 2;

	public CiteWeb()
	{
		super("cite web");
	}

	public CiteWeb(WikiConfig wikiConfig)
	{
		super(wikiConfig, "cite web");
	}

	@Override
	public WtNode invoke(WtNode template, ExpansionFrame preprocessorFrame, List<? extends WtNode> argsValues)
	{
		String result;
		try {
			final HashMap<CiteWebArgument, String> map = parseArguments(preprocessorFrame, argsValues);
			result = formatCiteWebOutput(map);
		} catch (IllegalArgumentException ex)
		{
			return error(ex.getMessage());
		}
		return nf().text(result);
	}

	/**
	 * Parses the given arguments into a String list and also resolves some
	 * formating parameters.
	 *
	 * @param frame
	 * @param args
	 * @throws IllegalArgumentException
	 */
	private HashMap<CiteWebArgument, String> parseArguments(
			final ExpansionFrame frame,
			final List<? extends WtNode> args)
			throws IllegalArgumentException
	{
		if (args.size() < MIN_ARGS)
		{
			throw new IllegalArgumentException("Too few arguments!");
		}

		ArrayList<String> strArgs = new ArrayList<String>(args.size());
		for (int i = 0; i < args.size(); i++)
		{
			String tmpStr = expandArgToString(frame, args, i);
			if (tmpStr == null)
			{
				throw new IllegalArgumentException("Cannot convert argument to string!");
			}
			strArgs.add(tmpStr);
		}

		HashMap<CiteWebArgument, String> map = new HashMap<CiteWebArgument, String>();

		for (int i = 0; i < strArgs.size(); i++)
		{
			final String arg = strArgs.get(i);
			if (arg.contains("="))
			{
				final String[] spl = arg.split("=");
				final String opt = StringUtils.trim(spl[0]);
				final String param = StringUtils.trim(spl[1]);

				CiteWebArgument argEnum = CiteWebArgument.findArgument(opt);
				if (argEnum == null)
				{
					throw new IllegalArgumentException("Unknow argument!");
				}
				map.put(argEnum, param);
			} else
			{
				throw new IllegalArgumentException("Wrong use of arguments!");
			}
		}
		return map;
	}

	private String expandArgToString(
			ExpansionFrame preprocessorFrame,
			List<? extends WtNode> args,
			final int index)
	{
		WtNode arg = preprocessorFrame.expand(args.get(index));

		tu().trim(arg);

		String format = null;
		try
		{
			format = tu().astToText(arg).trim();
		} catch (StringConversionException e1)
		{
		}
		return format;
	}

	private EngSoftErrorNode error(final String msg)
	{
		return EngineRtData.set(nf().softError(
				EngineRtData.set(nf().nowiki(StringTools.escHtml(msg)))));
	}

	private String formatCiteWebOutput(final HashMap<CiteWebArgument, String> map)
	{
		if (!map.containsKey(CiteWebArgument.ARG_URL))
		{
			throw new IllegalArgumentException("URL argument is missing!");
		}

		if (map.containsKey(CiteWebArgument.ARG_TITLE))
		{
			throw new IllegalArgumentException("Title argument is missing!");
		}

		if (map.containsKey(CiteWebArgument.ARG_FRIST) ^ map.containsKey(CiteWebArgument.ARG_LAST))
		{
			throw new IllegalArgumentException("First name or last name was not given. (One can not be defined without the other.)");
		}

		boolean useDot = false;
		String resultHtmlStr = "<cite class=\"citation web\">";

		if (map.containsKey(CiteWebArgument.ARG_LAST))
		{
			resultHtmlStr += map.get(CiteWebArgument.ARG_LAST) + ", "
					+ map.get(CiteWebArgument.ARG_FRIST) + " ";
			useDot = true;
		}

		if (map.containsKey(CiteWebArgument.ARG_DATE))
		{
			resultHtmlStr += "(" + map.get(CiteWebArgument.ARG_DATE) + ") ";
			useDot = true;
		}

		if (useDot)
		{
			resultHtmlStr += ". ";
			useDot = false;
		}

		resultHtmlStr += "<a href=\"" + map.get(CiteWebArgument.ARG_URL) + ">\"" + map.get(CiteWebArgument.ARG_TITLE) + "\"</a>. ";

		if (map.containsKey(CiteWebArgument.ARG_WEBSITE))
		{
			resultHtmlStr += "<i>" + map.get(CiteWebArgument.ARG_URL) + "</i>. ";
		}

		if (map.containsKey(CiteWebArgument.ARG_PUBLISHER))
		{
			resultHtmlStr += map.get(CiteWebArgument.ARG_PUBLISHER) + ". ";
		}

		if (map.containsKey(CiteWebArgument.ARG_ACCESS_DATE))
		{
			resultHtmlStr += "Retrieved " + map.get(CiteWebArgument.ARG_ACCESS_DATE) + ". ";
		}

		resultHtmlStr += "</cite>";
		return resultHtmlStr;
	}
}
