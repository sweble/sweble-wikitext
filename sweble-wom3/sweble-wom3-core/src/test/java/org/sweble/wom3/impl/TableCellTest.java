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
import org.sweble.wom3.Wom3TableCellScope;
import org.sweble.wom3.Wom3TableVAlign;
import org.sweble.wom3.Wom3Unit;

public class TableCellTest
{
	private TableCellImpl n = (TableCellImpl) TestHelperDoc.genElem("td");

	@Test
	public void testAbbrAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "abbr", "getAbbr", "setAbbr", "some text");
	}

	@Test
	public void testAxisAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "axis", "getAxis", "setAxis", "some text");
	}

	@Test
	public void testScopeAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "scope", "getScope", "setScope", Wom3TableCellScope.COL, "col");
		TestHelperAttribute.testAttribute(this.n, "scope", "getScope", "setScope", Wom3TableCellScope.COLGROUP, "colgroup");
		TestHelperAttribute.testAttribute(this.n, "scope", "getScope", "setScope", Wom3TableCellScope.ROW, "row");
		TestHelperAttribute.testAttribute(this.n, "scope", "getScope", "setScope", Wom3TableCellScope.ROWGROUP, "rowgroup");
	}

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
	public void testVAlignAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "valign", "getVAlign", "setVAlign", Wom3TableVAlign.BASELINE, "baseline");
		TestHelperAttribute.testAttribute(this.n, "valign", "getVAlign", "setVAlign", Wom3TableVAlign.BOTTOM, "bottom");
		TestHelperAttribute.testAttribute(this.n, "valign", "getVAlign", "setVAlign", Wom3TableVAlign.MIDDLE, "middle");
		TestHelperAttribute.testAttribute(this.n, "valign", "getVAlign", "setVAlign", Wom3TableVAlign.TOP, "top");
	}

	@Test
	public void testColorAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(n, "bgcolor", "getBgColor", "setBgColor", ColorImpl.valueOf("#abcdef"), "#ABCDEF");
	}

	@Test
	public void testColorCanCopeWithSpaces() throws Exception
	{
		n.setAttribute("bgcolor", " blue ");
	}

	@Test
	public void testColspanAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(n, "colspan", "getColspan", "setColspan", 5, "5");
	}

	@Test
	public void testRowspanAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(n, "rowspan", "getRowspan", "setRowspan", 5, "5");
	}

	@Test
	public void testNowrapAttribute() throws Exception
	{
		TestHelperAttribute.testBooleanAttribute(n, "nowrap", "isNowrap", "setNowrap");
	}

	@Test
	public void testWidthAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(n, "width", "getWidth", "setWidth", new ValueWithUnitImpl(Wom3Unit.PERCENT, 5.2f), "5.2%");
		TestHelperAttribute.testAttribute(n, "width", "getWidth", "setWidth", new ValueWithUnitImpl(Wom3Unit.PIXELS, 5), "5");
	}

	@Test
	public void testHeightAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(n, "height", "getHeight", "setHeight", new ValueWithUnitImpl(Wom3Unit.PERCENT, 5.2f), "5.2%");
		TestHelperAttribute.testAttribute(n, "height", "getHeight", "setHeight", new ValueWithUnitImpl(Wom3Unit.PIXELS, 5), "5");
	}
}
