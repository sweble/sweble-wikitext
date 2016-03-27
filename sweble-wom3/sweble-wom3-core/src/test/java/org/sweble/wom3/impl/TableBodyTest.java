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

import org.junit.Test;
import org.sweble.wom3.Wom3HorizAlign;
import org.sweble.wom3.Wom3TableVAlign;

public class TableBodyTest
{
	private TableBodyImpl n = (TableBodyImpl) TestHelperDoc.genElem("tbody");

	@Test
	public void testAlignAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3HorizAlign.CENTER, "center");
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3HorizAlign.JUSTIFY, "justify");
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3HorizAlign.LEFT, "left");
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3HorizAlign.RIGHT, "right");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAlignAttributeDoesNotAllowChar() throws Exception
	{
		n.setAttribute("align", "char");
	}

	@Test
	public void testTableVAlignAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "valign", "getVAlign", "setTableVAlign", Wom3TableVAlign.BASELINE, "baseline");
		TestHelperAttribute.testAttribute(this.n, "valign", "getVAlign", "setTableVAlign", Wom3TableVAlign.BOTTOM, "bottom");
		TestHelperAttribute.testAttribute(this.n, "valign", "getVAlign", "setTableVAlign", Wom3TableVAlign.MIDDLE, "middle");
		TestHelperAttribute.testAttribute(this.n, "valign", "getVAlign", "setTableVAlign", Wom3TableVAlign.TOP, "top");
	}
}
