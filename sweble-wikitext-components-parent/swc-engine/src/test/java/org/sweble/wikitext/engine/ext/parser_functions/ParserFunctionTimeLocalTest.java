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

public class ParserFunctionTimeLocalTest
{
	@Test
	public void testFormat()
	{
		// Tuesday, 7th March 2017, 01:02:03 AM
		final Calendar timestamp = new GregorianCalendar(2017, Calendar.MARCH, 7, 1, 2, 3);
		timestamp.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
		Locale locale = Locale.GERMAN;

		String format = "h";
		String expResult = "01";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check Unix time
		format = "U";
		expResult = "1488848523";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check time zone identifier
		format = "e";
		expResult = "Europe/Berlin";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check if Daylight Saving Time is currently used
		format = "I";
		expResult = "0";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));
		Calendar activeDst = new GregorianCalendar(2017, Calendar.JULY, 7, 1, 2, 3);
		expResult = "1";
		assertEquals(expResult, ParserFunctionTime.format(format, activeDst, locale));

		format = "O";
		expResult = "+0100";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		format = "P";
		expResult = "+01:00";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check time zone offset in seconds
		format = "Z";
		expResult = "3600";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check time zone abbreviation
		format = "T";
		expResult = "Europe/Berlin";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		timestamp.setTimeZone(TimeZone.getTimeZone("PST")); // UTC-08:00
		locale = Locale.US;

		// check time zone identifier
		format = "e";
		expResult = "PST";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check if Daylight Saving Time is currently used
		format = "I";
		expResult = "0";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));
		activeDst = new GregorianCalendar(2017, Calendar.JULY, 7, 1, 2, 3);
		expResult = "1";
		assertEquals(expResult, ParserFunctionTime.format(format, activeDst, locale));

		format = "O";
		expResult = "-0800";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		format = "P";
		expResult = "-08:00";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));

		// check time zone offset in seconds
		format = "Z";
		expResult = "-28800";
		assertEquals(expResult, ParserFunctionTime.format(format, timestamp, locale));
	}
}
