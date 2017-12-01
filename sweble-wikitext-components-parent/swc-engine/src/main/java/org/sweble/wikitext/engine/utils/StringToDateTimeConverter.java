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

package org.sweble.wikitext.engine.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;


/**
 * Converter class to extract date and time values from Strings.
 */
public final class StringToDateTimeConverter
{
	private ArrayList<DateTimeMatcher> matchers = new ArrayList<DateTimeMatcher>();
	
	/**
	 * Holds additional date time parameters like the Time Zone,
	 * Daylight Saving Time, the language specific weekday Locale etc.
	 * 
	 * @note The contained timestamp may not be in a valid state and should
	 * therefore not be used directly.
	 */
	private final Calendar calendar;

	/**
	 * Default constructor. Uses UTC as time base and English as Locale.
	 */
	public StringToDateTimeConverter()
	{
		calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
		calendar.set(Calendar.DST_OFFSET, 0);
		initDefault();
	}

	public StringToDateTimeConverter(Calendar cal)
	{
		this.calendar = cal;
		initDefault();
	}

	private void initDefault()
	{
		matchers.add(new NowMatcher());
		matchers.add(new TomorrowMatcher());
		matchers.add(new DaysMatcher());
		matchers.add(new DateFormatMatcher("dd.MM.yyyy"));
		matchers.add(new DateFormatMatcher("d MMM yyyy"));
		matchers.add(new DateFormatMatcher("yyyy.MM.dd G 'at' HH:mm:ss z"));
		matchers.add(new DateFormatMatcher("EEE, d MMM yyyy HH:mm:ss Z"));
		matchers.add(new DateFormatMatcher("yyyy MM dd"));
		matchers.add(new DateFormatMatcher("yyyy-MM-dd"));
	}

	public void registerDateTimeMatcher(DateTimeMatcher matcher)
	{
		matchers.add(matcher);
	}

	/**
	 * Tries to convert and interpret the input string which contains the time
	 * and date informations using the registered DateTimeMatcher.
	 *
	 * @param input The String to convert.
	 * @return A Date object with the extracted date/time value, or null on
	 * error.
	 */
	public Date convertString(String input)
	{
		for (DateTimeMatcher matcher : matchers)
		{
			Date date = matcher.tryConvert(input);
			if (date != null)
			{
				return date;
			}
		}
		return null;
	}

	public static interface DateTimeMatcher
	{
		public Date tryConvert(String input);
	}

	private class DateFormatMatcher implements DateTimeMatcher
	{
		private final DateFormat dateFormat;

		/**
		 * Constructor. The time zone and other details (e.g. Daylight Saving
		 * Time, Weekday Locale) are taken from the <code>calendar</code>
		 * object.
		 *
		 * @param dateFormat The DateFormat String defining the pattern.
		 */
		public DateFormatMatcher(final String dateString)
		{
			dateFormat = new SimpleDateFormat(dateString);
			dateFormat.setCalendar(calendar);
		}

		@Override
		public Date tryConvert(String input)
		{
			try
			{
				return dateFormat.parse(input);
			} catch (ParseException ex)
			{
				return null;
			}
		}
	}

	private class NowMatcher implements DateTimeMatcher
	{
		private final Pattern now = Pattern.compile("now");

		@Override
		public Date tryConvert(String input)
		{
			if (now.matcher(input).matches())
			{
				return new Date();
			} else
			{
				return null;
			}
		}
	}

	private class TomorrowMatcher implements DateTimeMatcher
	{
		private final Pattern tomorrow = Pattern.compile("tomorrow");

		@Override
		public Date tryConvert(String input)
		{
			if (tomorrow.matcher(input).matches())
			{
				calendar.setTime(new Date()); // set current time
				calendar.add(Calendar.DAY_OF_YEAR, + 1);
				return calendar.getTime();
			} else
			{
				return null;
			}
		}
	}

	private class DaysMatcher implements DateTimeMatcher
	{
		private final Pattern days = Pattern.compile("[\\-\\+]?\\d+ days");

		@Override
		public Date tryConvert(String input)
		{
			if (days.matcher(input).matches())
			{
				int days = Integer.parseInt(input.split(" ")[0]);
				calendar.setTime(new Date()); // set current time
				calendar.add(Calendar.DAY_OF_YEAR, days);
				return calendar.getTime();
			}
			return null;
		}
	}
}
