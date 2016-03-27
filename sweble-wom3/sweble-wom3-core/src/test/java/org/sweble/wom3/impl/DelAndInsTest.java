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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wom3.Wom3ElementNode;

import de.fau.cs.osr.utils.NamedParametrized;

@RunWith(value = NamedParametrized.class)
public class DelAndInsTest
{
	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		List<Object[]> inputs = new ArrayList<Object[]>();
		inputs.add(new Object[] { "del" });
		inputs.add(new Object[] { "ins" });
		return inputs;
	}

	// =========================================================================

	private final Wom3ElementNode n;

	public DelAndInsTest(String name) throws InstantiationException, IllegalAccessException
	{
		this.n = TestHelperDoc.genElem(name);
	}

	// =========================================================================

	@Test
	public void testCiteAttribute() throws Exception
	{
		String strValue = "http://example.com";
		URL realValue = new URL(strValue);
		TestHelperAttribute.testAttribute(this.n, "cite", "getCite", "setCite", realValue, strValue);
	}

	@Test
	public void testDateTimeAttribute() throws Exception
	{
		String strValue = "2007-12-24T18:21Z";
		DateTime realValue = new DateTime(strValue);
		strValue = Toolbox.dateTimeToString(realValue);
		TestHelperAttribute.testAttribute(this.n, "datetime", "getDatetime", "setDatetime", realValue, strValue);
	}
}
