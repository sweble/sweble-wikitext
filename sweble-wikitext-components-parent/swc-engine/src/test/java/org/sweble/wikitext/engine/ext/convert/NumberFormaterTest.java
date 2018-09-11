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

package org.sweble.wikitext.engine.ext.convert;

import org.junit.Test;
import static org.junit.Assert.*;


public class NumberFormaterTest
{
	private static final double EPSILON = 1E-6;

	@Test
	public void testIsNumberValid()
	{
		assertTrue(NumberFormater.isNumberValid("1"));
		assertTrue(NumberFormater.isNumberValid("123"));
		assertTrue(NumberFormater.isNumberValid("–123")); // en dash (u+2013)
		assertTrue(NumberFormater.isNumberValid("1,234,567"));
		assertTrue(NumberFormater.isNumberValid("0.5"));
		assertTrue(NumberFormater.isNumberValid("12.3e-15"));
		assertTrue(NumberFormater.isNumberValid("12.3e–15")); // en dash
		assertTrue(NumberFormater.isNumberValid("1/2"));
		assertTrue(NumberFormater.isNumberValid("1⁄2"));
		assertTrue(NumberFormater.isNumberValid("2+1⁄2"));
		assertTrue(NumberFormater.isNumberValid("-2-1⁄2"));
		assertTrue(NumberFormater.isNumberValid("–2–1⁄2")); // en dash
		assertTrue(NumberFormater.isNumberValid("1//2"));
		assertTrue(NumberFormater.isNumberValid("2+1//2"));

		assertFalse(NumberFormater.isNumberValid("12 34"));
		assertFalse(NumberFormater.isNumberValid("0x1234"));
		assertFalse(NumberFormater.isNumberValid("1234d"));
	}

	@Test
	public void testParseNumber1()
	{
		assertEquals(1d, NumberFormater.parseNumber("1"), EPSILON);
		assertEquals(123d, NumberFormater.parseNumber("123"), EPSILON);
		assertEquals(-123d, NumberFormater.parseNumber("–123"), EPSILON); // en dash
		assertEquals(1234567d, NumberFormater.parseNumber("1,234,567"), EPSILON);
		assertEquals(0.5, NumberFormater.parseNumber("0.5"), EPSILON);
		assertEquals(1.23e-4, NumberFormater.parseNumber("12.3e-5"), EPSILON);
		assertEquals(1.23e-4, NumberFormater.parseNumber("12.3e–5"), EPSILON); // en dash
		assertEquals(0.5, NumberFormater.parseNumber("1/2"), EPSILON);
		assertEquals(0.33333333, NumberFormater.parseNumber("1⁄3"), EPSILON);
		assertEquals(2.5, NumberFormater.parseNumber("2+1⁄2"), EPSILON);
		assertEquals(-2.5, NumberFormater.parseNumber("-2-1⁄2"), EPSILON);
		assertEquals(0.5, NumberFormater.parseNumber("1//2"), EPSILON);
		assertEquals(2.5, NumberFormater.parseNumber("2+1//2"), EPSILON);
		assertEquals(123d, NumberFormater.parseNumber("+123"), EPSILON);
		assertEquals(-123d, NumberFormater.parseNumber("-123"), EPSILON);
	}

	@Test(expected=NumberFormatException.class)
	public void testParseNumber2()
	{
		NumberFormater.parseNumber("-2+1⁄2");
	}

	@Test
	public void testFormatNumberDefault()
	{
		final int SIG_FIG = 2;
		assertEquals("1.0", NumberFormater.formatNumberDefault(1, SIG_FIG));
		assertEquals("−1.0", NumberFormater.formatNumberDefault(-1, SIG_FIG));
		assertEquals("11", NumberFormater.formatNumberDefault(11, SIG_FIG));
		assertEquals("110", NumberFormater.formatNumberDefault(111, SIG_FIG));
		assertEquals("120", NumberFormater.formatNumberDefault(115, SIG_FIG));
		assertEquals("1,100", NumberFormater.formatNumberDefault(1111, SIG_FIG));
		assertEquals("0.10", NumberFormater.formatNumberDefault(0.1, SIG_FIG));
		assertEquals("0.11", NumberFormater.formatNumberDefault(0.11, SIG_FIG));
		assertEquals("0.11", NumberFormater.formatNumberDefault(0.111, SIG_FIG));
		assertEquals("0.11", NumberFormater.formatNumberDefault(0.114, SIG_FIG));
		assertEquals("0.12", NumberFormater.formatNumberDefault(0.115, SIG_FIG));
		assertEquals("−0.11", NumberFormater.formatNumberDefault(-0.114, SIG_FIG));
		assertEquals("−0.12", NumberFormater.formatNumberDefault(-0.115, SIG_FIG));
		assertEquals("0.0011", NumberFormater.formatNumberDefault(0.0011, SIG_FIG));
		assertEquals("0.0000000010", NumberFormater.formatNumberDefault(0.000000001, SIG_FIG));
		assertEquals("1.0×10⁻¹⁰", NumberFormater.formatNumberDefault(0.0000000001, SIG_FIG));
		assertEquals("−1.0×10⁻¹⁰", NumberFormater.formatNumberDefault(-0.0000000001, SIG_FIG));
		assertEquals("11,000,000", NumberFormater.formatNumberDefault(11000000, SIG_FIG));
		assertEquals("−11,000,000", NumberFormater.formatNumberDefault(-11000000, SIG_FIG));
		assertEquals("1.1×10¹⁰", NumberFormater.formatNumberDefault(11000000000d, SIG_FIG));
		assertEquals("1", NumberFormater.formatNumberDefault(1.234, 1));
		assertEquals("1.2", NumberFormater.formatNumberDefault(1.234, 2));
		assertEquals("1.23", NumberFormater.formatNumberDefault(1.234, 3));
		assertEquals("1.234", NumberFormater.formatNumberDefault(1.234, 4));
		assertEquals("1.2340", NumberFormater.formatNumberDefault(1.234, 5));
		assertEquals("10", NumberFormater.formatNumberDefault(12.34, 1));
		assertEquals("12", NumberFormater.formatNumberDefault(12.34, 2));
		assertEquals("12.3", NumberFormater.formatNumberDefault(12.34, 3));
		assertEquals("12.34", NumberFormater.formatNumberDefault(12.34, 4));
		assertEquals("12.340", NumberFormater.formatNumberDefault(12.34, 5));
		assertEquals("100", NumberFormater.formatNumberDefault(123.4, 1));
		assertEquals("120", NumberFormater.formatNumberDefault(123.4, 2));
		assertEquals("123", NumberFormater.formatNumberDefault(123.4, 3));
		assertEquals("123.4", NumberFormater.formatNumberDefault(123.4, 4));
		assertEquals("123.40", NumberFormater.formatNumberDefault(123.4, 5));
		assertEquals("1,000", NumberFormater.formatNumberDefault(1234.5, 1));
		assertEquals("1,200", NumberFormater.formatNumberDefault(1234.5, 2));
		assertEquals("1,230", NumberFormater.formatNumberDefault(1234.5, 3));
		assertEquals("1,235", NumberFormater.formatNumberDefault(1234.5, 4));
		assertEquals("1,234.5", NumberFormater.formatNumberDefault(1234.5, 5));
		
	}

	@Test
	public void testFormatNumberRounded()
	{
		assertEquals("1", NumberFormater.formatNumberRounded(1.123456789, 0));
		assertEquals("1.1", NumberFormater.formatNumberRounded(1.123456789, 1));
		assertEquals("1.12", NumberFormater.formatNumberRounded(1.123456789, 2));
		assertEquals("1.123", NumberFormater.formatNumberRounded(1.123456789, 3));
		assertEquals("1", NumberFormater.formatNumberRounded(1.4, 0));
		assertEquals("2", NumberFormater.formatNumberRounded(1.5, 0));
		assertEquals("−1", NumberFormater.formatNumberRounded(-1.4, 0));
		assertEquals("−2", NumberFormater.formatNumberRounded(-1.5, 0));
		assertEquals("1", NumberFormater.formatNumberRounded(1.123456789, -1));
		assertEquals("1.123456789012346", NumberFormater.formatNumberRounded(1.123456789012345678, 15));
		assertEquals("1.1234567890123457", NumberFormater.formatNumberRounded(1.123456789012345678, 16));
		assertEquals("1.1234567890123457", NumberFormater.formatNumberRounded(1.123456789012345678, 17));
		assertEquals("−1.123456789012346", NumberFormater.formatNumberRounded(-1.123456789012345678, 15));
		assertEquals("−1.1234567890123457", NumberFormater.formatNumberRounded(-1.123456789012345678, 16));
		assertEquals("−1.1234567890123457", NumberFormater.formatNumberRounded(-1.123456789012345678, 17));
		assertEquals("100,000.00", NumberFormater.formatNumberRounded(100000, 2));
	}
}
