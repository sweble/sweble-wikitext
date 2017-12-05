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

import de.fau.cs.osr.utils.StringTools;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngSoftErrorNode;
import org.sweble.wikitext.engine.nodes.EngineRtData;
import org.sweble.wikitext.engine.utils.StringToDateTimeConverter;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.utils.StringConversionException;

/**
 *
 */
public class ParserFunctionTimeLocal
		extends
			ParserFunctionsExtPfn
{

	/**
	 * For un-marshaling only.
	 */
	public ParserFunctionTimeLocal()
	{
		super("timel");
	}

	public ParserFunctionTimeLocal(WikiConfig wikiConfig)
	{
		super(wikiConfig, "timel");
	}

	@Override
	public WtNode invoke(
			WtTemplate pfn,
			ExpansionFrame frame,
			List<? extends WtNode> args)
	{
		if (args.size() < 1)
			return pfn;

		// ---- format

		String format = expandArgToString(frame, args, 0);
		if (format == null)
			return error("Cannot convert format argument to string!");

		// ---- language

		String languageTag = null;
		if (args.size() >= 3)
		{
			languageTag = expandArgToString(frame, args, 2);
			if (languageTag == null)
				return error("Cannot convert language argument to string!");
		}

		Locale locale = null;
		if (languageTag != null && !languageTag.isEmpty())
		{

			locale = Locale.forLanguageTag(languageTag);
			if (locale == null)
			{
				return notYetImplemented("Cannot handle non-empty language argument!");
			}
		}

		if (locale == null)
		{
			locale = Locale.ENGLISH; // default Locale if not defined
		}

		// ---- timestamp

		Calendar timestamp = getWikiConfig().getRuntimeInfo().getDateAndTime(locale);

		if (args.size() >= 2)
		{
			String timestampStr = expandArgToString(frame, args, 1);
			if (timestampStr == null)
				return error("Cannot convert timestamp argument to string!");

			if (!timestampStr.isEmpty())
			{
				StringToDateTimeConverter conv = new StringToDateTimeConverter(timestamp);
				Date argumentDate = conv.convertString(timestampStr);
				if (argumentDate != null)
				{
					timestamp.setTime(argumentDate);
				}
				else
				{
					return notYetImplemented("Cannot handle non-empty timestamp argument!");
				}
			}
		}

		// ---- let's format ourselves a date...

		return nf().text(ParserFunctionTime.format(format, timestamp, locale));
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
		}
		catch (StringConversionException e1)
		{
		}
		return format;
	}

	private EngSoftErrorNode error(String msg)
	{
		return EngineRtData.set(nf().softError(
				EngineRtData.set(nf().nowiki(StringTools.escHtml(msg)))));
	}

	private EngSoftErrorNode notYetImplemented(String msg)
	{
		return nf().addCssClass(
				EngineRtData.set(nf().softError(
						EngineRtData.set(nf().nowiki(StringTools.escHtml(msg))))),
				"not-yet-implemented");
	}
}