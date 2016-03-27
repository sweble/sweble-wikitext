/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ColorTest
{
	@Test
	public void testGetByName() throws Exception
	{
		ColorImpl color = ColorImpl.valueOf(" DodgerBlUe "); // #1E90FF
		assertEquals(0x1E, color.getRed());
		assertEquals(0x90, color.getGreen());
		assertEquals(0xFF, color.getBlue());
	}

	@Test
	public void testGetByLongCode() throws Exception
	{
		ColorImpl color = ColorImpl.valueOf(" #1e90fF ");
		assertEquals(0x1E, color.getRed());
		assertEquals(0x90, color.getGreen());
		assertEquals(0xFF, color.getBlue());
	}

	@Test
	public void testGetByShortCode() throws Exception
	{
		ColorImpl color = ColorImpl.valueOf(" #19f ");
		assertEquals(0x11, color.getRed());
		assertEquals(0x99, color.getGreen());
		assertEquals(0xFF, color.getBlue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidColorName() throws Exception
	{
		ColorImpl.valueOf("foo");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidHexCode() throws Exception
	{
		ColorImpl.valueOf("#11gg11");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidColorCode() throws Exception
	{
		ColorImpl.valueOf("#1fff");
	}

	@Test
	public void testToString() throws Exception
	{
		assertEquals("#010101", ColorImpl.valueOf("#010101").toString());
		assertEquals("#1E90FF", ColorImpl.valueOf("#1E90FF").toString());
	}
}
