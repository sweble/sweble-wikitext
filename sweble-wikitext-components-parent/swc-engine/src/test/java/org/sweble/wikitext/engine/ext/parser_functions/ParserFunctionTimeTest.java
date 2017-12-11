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
import java.util.Locale;
import java.util.TimeZone;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserFunctionTimeTest
{
	@Test
	public void testFormat()
	{
		// Tuesday, 7th March 2017, 01:02:03 AM
		final Calendar timestamp = new GregorianCalendar(2017, Calendar.MARCH, 7, 1, 2, 3);
		timestamp.setTimeZone(TimeZone.getTimeZone("UTC"));
		Locale locale = Locale.ENGLISH;

		// check year 4-digit
		String format = "Y";
		String expResult = "2017";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check year 2-digit
		format = "y";
		expResult = "17";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check if leap year
		format = "L";
		expResult = "0";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));
		Calendar leap = new GregorianCalendar(2016, 1, 1, 0, 0, 0);
		expResult = "1";
		assertEquals(expResult, ParserFunctionTime.format(format, leap, locale));

		// check month index, not zero-padded
		format = "n";
		expResult = "3";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check month index, zero-padded
		format = "m";
		expResult = "03";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check abbreviation of the month name
		format = "M";
		expResult = "Mar";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));
		expResult = "Mär";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, Locale.GERMAN));

		// check full month name
		format = "F";
		expResult = "March";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));
		expResult = "März";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, Locale.GERMAN));

		// check ISO 8601 week number, zero-padded.
		format = "W";
		expResult = "10";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		//check day of the month, not zero-padded
		format = "j";
		expResult = "7";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check day of the month, zero-padded
		format = "d";
		expResult = "07";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check day of the year (January 1 = 0)
		format = "z";
		expResult = "0";
		Calendar firstDayInTheYear = new GregorianCalendar(2017, Calendar.JANUARY, 1, 0, 0, 0);
		assertEquals(expResult, ParserFunctionTime.format(format, firstDayInTheYear, locale));
		expResult = "364";
		Calendar lastDayInTheYear = new GregorianCalendar(2017, Calendar.DECEMBER, 31, 23, 59, 59);
		assertEquals(expResult, ParserFunctionTime.format(format, lastDayInTheYear, locale));

		// check abbreviation for the day of the week
		format = "D";
		expResult = "Tue";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));
		expResult = "Di";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, Locale.GERMAN));

		// check full weekday name
		format = "l";
		expResult = "Tuesday";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));
		expResult = "Dienstag";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, Locale.GERMAN));

		// check ISO 8601 day of the week (Monday = 1, Sunday = 7)
		format = "N";
		expResult = "2";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check number of the day of the week (Sunday = 0, Saturday = 6)
		format = "w";
		expResult = "2";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));
		Calendar sunday = new GregorianCalendar(2017, Calendar.DECEMBER, 24, 0, 0, 0);
		expResult = "0";
		assertEquals(expResult, ParserFunctionTime.format(format, sunday, locale));

		// check am/pm
		format = "a";
		expResult = "am";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check AM/PM
		format = "A";
		expResult = "AM";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check hour in 12-hour format, not zero-padded
		format = "g";
		expResult = "1";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check hour in 12-hour format, zero-padded
		format = "h";
		expResult = "01";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check hour in 24-hour format, not zero-padded
		format = "G";
		expResult = "1";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check hour in 24-hour format, zero-padded
		format = "H";
		expResult = "01";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check minutes past the hour, zero-padded
		format = "i";
		expResult = "02";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check seconds past the minute, zero-padded
		format = "s";
		expResult = "03";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check Unix time
		format = "U";
		expResult = "1488848523";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check time zone identifier
		format = "e";
		expResult = "UTC";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check if Daylight Saving Time is currently used
		format = "I";
		expResult = "0";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		format = "O";
		expResult = "+0000";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		format = "P";
		expResult = "+00:00";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check time zone offset in seconds
		format = "Z";
		expResult = "0";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check time zone abbreviation
		format = "T";
		expResult = "UTC";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check number of days in the current month
		format = "t";
		expResult = "31";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check ISO 8601 formatted date, equivalent to Y-m-d"T"H:i:s+00:00
		format = "c";
		expResult = "2017-03-07T01:02:03+00:00";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));
		format = "Y-m-d\"T\"H:i:s+00:00";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check RFC 5322 formatted date, equivalent to D, j M Y H:i:s +0000
		format = "r";
		expResult = "Tue, 7 Mar 2017 01:02:03 +0000";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));
		format = "D, j M Y H:i:s +0000";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check ignored comments
		format = "Y-\"Ymds h\"-m-\"CD\"-d";
		expResult = "2017-Ymds h-03-CD-07";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));
	}

}
