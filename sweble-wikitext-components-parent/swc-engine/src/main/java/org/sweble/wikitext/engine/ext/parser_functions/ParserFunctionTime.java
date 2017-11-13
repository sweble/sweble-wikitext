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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngSoftErrorNode;
import org.sweble.wikitext.engine.nodes.EngineRtData;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.utils.StringConversionException;

import de.fau.cs.osr.utils.StringTools;

public class ParserFunctionTime
		extends
			ParserFunctionsExtPfn
{
	private static final long serialVersionUID = 1L;

	/**
	 * For un-marshaling only.
	 */
	public ParserFunctionTime()
	{
		super("time");
	}

	public ParserFunctionTime(WikiConfig wikiConfig)
	{
		super(wikiConfig, "time");
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

		// ---- timestamp

		String timestampStr = null;
		if (args.size() >= 2)
		{
			timestampStr = expandArgToString(frame, args, 1);
			if (timestampStr == null)
				return error("Cannot convert timestamp argument to string!");
		}

		if (timestampStr != null && !timestampStr.isEmpty())
			return notYetImplemented("Cannot handle non-empty timestamp argument!");

		// ---- language

		String languageTag = null;
		if (args.size() >= 3)
		{
			languageTag = expandArgToString(frame, args, 2);
			if (languageTag == null)
				return error("Cannot convert language argument to string!");
		}

		if (languageTag != null && !languageTag.isEmpty())
			return notYetImplemented("Cannot handle non-empty language argument!");

		languageTag = "en";

		// ---- let's format ourselves a date...

		Locale locale = new Locale(languageTag);

		Calendar timestamp = getWikiConfig().getRuntimeInfo().getDateAndTime(locale);

		return format(format, timestamp, locale);
	}

	/**
	 * @see https://www.mediawiki.org/wiki/Help:Extension:ParserFunctions##time
	 */
	private WtNode format(String format, Calendar timestamp, Locale locale)
	{
		StringBuilder sb = new StringBuilder();
		boolean isCharInComment = false;
		int tmp;

		for (int i = 0; i < format.length(); ++i)
		{
			char ch = format.charAt(i);

			if (ch == '"')
			{
				isCharInComment = !isCharInComment;
				continue;
			}

			if (isCharInComment)
			{
				// character is part of a comment, skip interpretation
				sb.append(ch);
				continue;
			}

			switch (ch)
			{
				case 'Y': // 4-digit year
					sb.append(timestamp.get(Calendar.YEAR));
					break;

				case 'y': // 2-digit year
					sb.append(timestamp.get(Calendar.YEAR) % 100);
					break;

				case 'L': // '1' if it's a leap year, '0' if not
					GregorianCalendar gregCal = new GregorianCalendar(locale);
					if (gregCal.isLeapYear(timestamp.get(Calendar.YEAR)))
					{
						sb.append('1');
					} else
					{
						sb.append('0');
					}
					break;

				case 'n': // month index, not zero-padded	
					sb.append(timestamp.get(Calendar.MONTH) + 1);
					break;

				case 'm': // month index, zero-padded	
					tmp = timestamp.get(Calendar.MONTH) + 1;
					if (tmp < 10)
					{
						sb.append('0');
					}
					sb.append(tmp);
					break;

				case 'M': // abbreviation of the month name
					sb.append(timestamp.getDisplayName(
							Calendar.MONTH,
							Calendar.SHORT,
							locale));
					break;

				case 'F': // full month name
					sb.append(timestamp.getDisplayName(
							Calendar.MONTH,
							Calendar.LONG,
							locale));
					break;

				case 'W': // ISO 8601 week number, zero-padded.
					tmp = timestamp.get(Calendar.WEEK_OF_YEAR);
					if (tmp < 10)
					{
						sb.append('0');
					}
					sb.append(tmp);
					break;

				case 'j': // day of the month, not zero-padded
					sb.append(timestamp.get(Calendar.DAY_OF_MONTH));
					break;

				case 'd': // day of the month, zero-padded
					tmp = timestamp.get(Calendar.DAY_OF_MONTH);
					if (tmp < 10)
					{
						sb.append('0');
					}
					sb.append(tmp);
					break;

				case 'z': // day of the year (January 1 = 0)
					sb.append(timestamp.get(Calendar.DAY_OF_YEAR) - 1);
					break;

				case 'D': // abbreviation for the day of the week
					sb.append(timestamp.getDisplayName(
							Calendar.DAY_OF_WEEK,
							Calendar.SHORT,
							locale));
					break;

				case 'l': // the full weekday name
					sb.append(timestamp.getDisplayName(
							Calendar.DAY_OF_WEEK,
							Calendar.LONG,
							locale));
					break;

				case 'N': // ISO 8601 day of the week (Monday = 1, Sunday = 7)
					sb.append(((timestamp.get(Calendar.DAY_OF_WEEK) + 5) % 7) + 1);
					break;

				case 'w': // Number of the day of the week (Sunday = 0, Saturday = 6)
					sb.append(timestamp.get(Calendar.DAY_OF_WEEK) - 1);
					break;

				case 'a': // am, pm
					if (timestamp.get(Calendar.AM_PM) == Calendar.AM)
					{
						sb.append("am");
					} else
					{
						sb.append("pm");
					}
					break;

				case 'A': // AM, PM
					if (timestamp.get(Calendar.AM_PM) == Calendar.AM)
					{
						sb.append("AM");
					} else
					{
						sb.append("PM");
					}
					break;

				case 'g': // hour in 12-hour format, not zero-padded	
					sb.append(timestamp.get(Calendar.HOUR));
					break;

				case 'h': // hour in 12-hour format, zero-padded	
					tmp = timestamp.get(Calendar.HOUR);
					if (tmp < 10)
					{
						sb.append('0');
					}
					sb.append(tmp);
					break;

				case 'G': // hour in 24-hour format, not zero-padded	
					sb.append(timestamp.get(Calendar.HOUR_OF_DAY));
					break;

				case 'H': // hour in 24-hour format, zero-padded	
					tmp = timestamp.get(Calendar.HOUR_OF_DAY);
					if (tmp < 10)
					{
						sb.append('0');
					}
					sb.append(tmp);
					break;

				case 'i': // minutes past the hour, zero-padded	
					tmp = timestamp.get(Calendar.MINUTE);
					if (tmp < 10)
					{
						sb.append('0');
					}
					sb.append(tmp);
					break;

				case 's': // seconds past the minute, zero-padded	
					tmp = timestamp.get(Calendar.SECOND);
					if (tmp < 10)
					{
						sb.append('0');
					}
					sb.append(tmp);
					break;

				case 'U': // Unix time
					sb.append(timestamp.getTimeInMillis() / 1000);
					break;

				case 't': // number of days in the current month
					sb.append(timestamp.getActualMaximum(timestamp.get(Calendar.MONTH)));
					break;

				case 'c': // ISO 8601 formatted date, equivalent to Y-m-d"T"H:i:s+00:00	
					sb.append(timestamp.get(Calendar.YEAR)).append('-');

					tmp = timestamp.get(Calendar.MONTH) + 1;
					if (tmp < 10)
					{
						sb.append('0');
					}
					sb.append(tmp).append('-');

					tmp = timestamp.get(Calendar.DAY_OF_MONTH);
					if (tmp < 10)
					{
						sb.append('0');
					}
					sb.append(tmp).append('T');

					tmp = timestamp.get(Calendar.HOUR_OF_DAY);
					if (tmp < 10)
					{
						sb.append('0');
					}
					sb.append(tmp).append(':');

					tmp = timestamp.get(Calendar.MINUTE);
					if (tmp < 10)
					{
						sb.append('0');
					}
					sb.append(tmp).append(':');

					tmp = timestamp.get(Calendar.SECOND);
					if (tmp < 10)
					{
						sb.append('0');
					}
					sb.append(tmp).append("+00:00");
					break;

				case 'r': // RFC 5322 formatted date, equivalent to D, j M Y H:i:s +0000
					sb.append(timestamp.getDisplayName(
							Calendar.DAY_OF_WEEK,
							Calendar.SHORT,
							Locale.US)) // no i18n
							.append(", ")
							.append(timestamp.get(Calendar.DAY_OF_MONTH))
							.append(' ')
							.append(timestamp.getDisplayName(
									Calendar.MONTH,
									Calendar.SHORT,
									Locale.US)) // no i18n
							.append(' ')
							.append(timestamp.get(Calendar.YEAR))
							.append(' ');

					tmp = timestamp.get(Calendar.HOUR_OF_DAY);
					if (tmp < 10)
					{
						sb.append('0');
					}
					sb.append(tmp).append(':');

					tmp = timestamp.get(Calendar.MINUTE);
					if (tmp < 10)
					{
						sb.append('0');
					}
					sb.append(tmp).append(':');

					tmp = timestamp.get(Calendar.SECOND);
					if (tmp < 10)
					{
						sb.append('0');
					}
					sb.append(tmp).append(" +0000");
					break;

				default:
					sb.append(ch);
					break;
			}
		}

		return nf().text(sb.toString());
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
