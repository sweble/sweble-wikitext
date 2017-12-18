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


public class ConvertTest
{
	private static final double EPSILON = 1E-6;

	@Test
	public void testIsNumberValid()
	{
		assertTrue(Convert.isNumberValid("1"));
		assertTrue(Convert.isNumberValid("123"));
		assertTrue(Convert.isNumberValid("–123")); // en dash (u+2013)
		assertTrue(Convert.isNumberValid("1,234,567"));
		assertTrue(Convert.isNumberValid("0.5"));
		assertTrue(Convert.isNumberValid("12.3e-15"));
		assertTrue(Convert.isNumberValid("12.3e–15")); // en dash
		assertTrue(Convert.isNumberValid("1/2"));
		assertTrue(Convert.isNumberValid("1⁄2"));
		assertTrue(Convert.isNumberValid("2+1⁄2"));
		assertTrue(Convert.isNumberValid("-2-1⁄2"));
		assertTrue(Convert.isNumberValid("–2–1⁄2")); // en dash
		assertTrue(Convert.isNumberValid("1//2"));
		assertTrue(Convert.isNumberValid("2+1//2"));

		assertFalse(Convert.isNumberValid("12 34"));
		assertFalse(Convert.isNumberValid("0x1234"));
		assertFalse(Convert.isNumberValid("1234d"));
	}

	@Test
	public void testParseNumber1()
	{
		assertEquals(1d, Convert.parseNumber("1"), EPSILON);
		assertEquals(123d, Convert.parseNumber("123"), EPSILON);
		assertEquals(-123d, Convert.parseNumber("–123"), EPSILON); // en dash
		assertEquals(1234567d, Convert.parseNumber("1,234,567"), EPSILON);
		assertEquals(0.5, Convert.parseNumber("0.5"), EPSILON);
		assertEquals(1.23e-4, Convert.parseNumber("12.3e-5"), EPSILON);
		assertEquals(1.23e-4, Convert.parseNumber("12.3e–5"), EPSILON); // en dash
		assertEquals(0.5, Convert.parseNumber("1/2"), EPSILON);
		assertEquals(0.33333333, Convert.parseNumber("1⁄3"), EPSILON);
		assertEquals(2.5, Convert.parseNumber("2+1⁄2"), EPSILON);
		assertEquals(-2.5, Convert.parseNumber("-2-1⁄2"), EPSILON);
		assertEquals(0.5, Convert.parseNumber("1//2"), EPSILON);
		assertEquals(2.5, Convert.parseNumber("2+1//2"), EPSILON);
		assertEquals(123d, Convert.parseNumber("+123"), EPSILON);
		assertEquals(-123d, Convert.parseNumber("-123"), EPSILON);
	}

	@Test(expected=NumberFormatException.class)
	public void testParseNumber2()
	{
		Convert.parseNumber("-2+1⁄2");
	}
}
