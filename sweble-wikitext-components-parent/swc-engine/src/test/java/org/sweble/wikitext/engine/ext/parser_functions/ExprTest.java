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

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class ExprTest
{
	private ExprParser p;

	@Before
	public void setUp()
	{
		p = new ExprParser();
	}

	@Test
	public void testConstantsOnlyReturnConstant() throws Exception
	{
		assertEquals(Math.E, Double.parseDouble(p.parse("e")), 0);
		assertEquals(Math.PI, Double.parseDouble(p.parse("pi")), 0);
	}

	@Test
	public void testScientificNotationWorks() throws Exception
	{
		assertEquals(1.5e5, Double.parseDouble(p.parse("1.5e5")), 0);
	}

	@Test
	public void testUnaryPlusMinusAndNotWork() throws Exception
	{
		assertEquals(-5, Double.parseDouble(p.parse("-5")), 0);
		assertEquals(+5, Double.parseDouble(p.parse("+5")), 0);
		assertEquals(0, Double.parseDouble(p.parse("not 5")), 0);
		assertEquals(1, Double.parseDouble(p.parse("not 0")), 0);
	}

	@Test
	public void testUnaryFunctionsWork() throws Exception
	{
		assertEquals(Math.sin(.5), Double.parseDouble(p.parse("sin 0.5")), 0);
		assertEquals(Math.cos(.5), Double.parseDouble(p.parse("cos 0.5")), 0);
		assertEquals(Math.tan(.5), Double.parseDouble(p.parse("tan 0.5")), 0);
		assertEquals(Math.asin(.5), Double.parseDouble(p.parse("asin 0.5")), 0);
		assertEquals(Math.acos(.5), Double.parseDouble(p.parse("acos 0.5")), 0);
		assertEquals(Math.atan(.5), Double.parseDouble(p.parse("atan 0.5")), 0);
		assertEquals(Math.exp(.5), Double.parseDouble(p.parse("exp 0.5")), 0);
		assertEquals(Math.log(.5), Double.parseDouble(p.parse("ln 0.5")), 0);
		assertEquals(Math.abs(-.5), Double.parseDouble(p.parse("abs -0.5")), 0);
		assertEquals(Math.floor(.5), Double.parseDouble(p.parse("floor 0.5")), 0);
		assertEquals((double) (int) (.5), Double.parseDouble(p.parse("trunc 0.5")), 0);
		assertEquals(Math.ceil(.5), Double.parseDouble(p.parse("ceil 0.5")), 0);
	}

	@Test
	public void testBinaryFunctionsWork() throws Exception
	{
		assertEquals(256, Double.parseDouble(p.parse("2^8")), 0);
		assertEquals(16, Double.parseDouble(p.parse("2*8")), 0);
		assertEquals(2 / 8., Double.parseDouble(p.parse("2/8")), 0);
		assertEquals(2., Double.parseDouble(p.parse("2mod8")), 0);
		assertEquals(2.57, Double.parseDouble(p.parse("2.567 round 2")), 0);
		assertEquals(10, Double.parseDouble(p.parse("2+8")), 0);
		assertEquals(-6, Double.parseDouble(p.parse("2-8")), 0);
	}

	@Test
	public void testComparisonOperatorsWork() throws Exception
	{
		assertEquals(1, Double.parseDouble(p.parse("1=1")), 0);
		assertEquals(0, Double.parseDouble(p.parse("1=0")), 0);

		assertEquals(0, Double.parseDouble(p.parse("1!=1")), 0);
		assertEquals(1, Double.parseDouble(p.parse("1!=0")), 0);
		assertEquals(0, Double.parseDouble(p.parse("1<>1")), 0);
		assertEquals(1, Double.parseDouble(p.parse("1<>0")), 0);

		assertEquals(0, Double.parseDouble(p.parse("1<0")), 0);
		assertEquals(1, Double.parseDouble(p.parse("0<1")), 0);
		assertEquals(0, Double.parseDouble(p.parse("0<0")), 0);

		assertEquals(1, Double.parseDouble(p.parse("1>0")), 0);
		assertEquals(0, Double.parseDouble(p.parse("0>1")), 0);
		assertEquals(0, Double.parseDouble(p.parse("0>0")), 0);

		assertEquals(1, Double.parseDouble(p.parse("1>=0")), 0);
		assertEquals(0, Double.parseDouble(p.parse("0>=1")), 0);
		assertEquals(1, Double.parseDouble(p.parse("0>=0")), 0);

		assertEquals(0, Double.parseDouble(p.parse("1<=0")), 0);
		assertEquals(1, Double.parseDouble(p.parse("0<=1")), 0);
		assertEquals(1, Double.parseDouble(p.parse("0<=0")), 0);
	}

	@Test
	public void testLogicalOperatorsWork() throws Exception
	{
		assertEquals(1, Double.parseDouble(p.parse("1 and 1")), 0);
		assertEquals(0, Double.parseDouble(p.parse("1 and 0")), 0);
		assertEquals(0, Double.parseDouble(p.parse("0 and 1")), 0);
		assertEquals(0, Double.parseDouble(p.parse("0 and 0")), 0);

		assertEquals(1, Double.parseDouble(p.parse("1 or 1")), 0);
		assertEquals(1, Double.parseDouble(p.parse("1 or 0")), 0);
		assertEquals(1, Double.parseDouble(p.parse("0 or 1")), 0);
		assertEquals(0, Double.parseDouble(p.parse("0 or 0")), 0);

		assertEquals(1, Double.parseDouble(p.parse("0.1 and 0.1")), 0);
		assertEquals(1, Double.parseDouble(p.parse("0 or 0.1")), 0);
	}

	@Test
	public void testPrecedenceRules() throws Exception
	{
		assertEquals(
				(5 + (5 * Math.pow(2, (Math.sin(2e5))))),
				Double.parseDouble(p.parse("5 + 5 * 2 ^ sin 2e5")),
				0);

		assertEquals(
				round(5.1234 + 5.1234, 2),
				Double.parseDouble(p.parse("5.1234 + 5.1234 round 2")),
				0);

		assertEquals(
				true, // true && (false == false),
				Double.parseDouble(p.parse("1 and 0 = 0")) == 1.);
	}

	@Test
	public void testParantheses() throws Exception
	{
		assertEquals(
				(5 + 5) * 5,
				Double.parseDouble(p.parse("(5 + 5) * 5")),
				0);
		assertEquals(
				(5 + (5 + 5)) * 5,
				Double.parseDouble(p.parse("(5 + (5 + 5)) * 5")),
				0);
	}

	private static double round(double value, int digits)
	{
		return new BigDecimal(value)
				.setScale(digits, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}
}
