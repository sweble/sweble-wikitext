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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.sweble.wom3.Wom3HorizAlign;
import org.sweble.wom3.Wom3TableFrame;
import org.sweble.wom3.Wom3TableRules;
import org.sweble.wom3.Wom3Unit;

public class TableTest
{
	private TableImpl n = (TableImpl) TestHelperDoc.genElem("table");

	@Test
	public void testAlignAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3HorizAlign.CENTER, "center");
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3HorizAlign.LEFT, "left");
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3HorizAlign.RIGHT, "right");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAlignAttributeDoesNotAllowJustify() throws Exception
	{
		n.setAttribute("align", "jusitfy");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAlignAttributeDoesNotAllowChar() throws Exception
	{
		n.setAttribute("align", "char");
	}

	@Test
	public void testBorderAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(n, "border", "getBorder", "setBorder", 5, "5");
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
	public void testCellpaddingAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(n, "cellpadding", "getCellPadding", "setCellPadding", new ValueWithUnitImpl(Wom3Unit.PERCENT, 5.2f), "5.2%");
		TestHelperAttribute.testAttribute(n, "cellpadding", "getCellPadding", "setCellPadding", new ValueWithUnitImpl(Wom3Unit.PIXELS, 5), "5");
	}

	@Test
	public void testCellspacingAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(n, "cellspacing", "getCellSpacing", "setCellSpacing", new ValueWithUnitImpl(Wom3Unit.PERCENT, 5.2f), "5.2%");
		TestHelperAttribute.testAttribute(n, "cellspacing", "getCellSpacing", "setCellSpacing", new ValueWithUnitImpl(Wom3Unit.PIXELS, 5), "5");
	}

	@Test
	public void testFrameAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "frame", "getFrame", "setFrame", Wom3TableFrame.ABOVE, "above");
		TestHelperAttribute.testAttribute(this.n, "frame", "getFrame", "setFrame", Wom3TableFrame.BELOW, "below");
		TestHelperAttribute.testAttribute(this.n, "frame", "getFrame", "setFrame", Wom3TableFrame.BORDER, "border");
		TestHelperAttribute.testAttribute(this.n, "frame", "getFrame", "setFrame", Wom3TableFrame.BOX, "box");
		TestHelperAttribute.testAttribute(this.n, "frame", "getFrame", "setFrame", Wom3TableFrame.HSIDES, "hsides");
		TestHelperAttribute.testAttribute(this.n, "frame", "getFrame", "setFrame", Wom3TableFrame.LHS, "lhs");
		TestHelperAttribute.testAttribute(this.n, "frame", "getFrame", "setFrame", Wom3TableFrame.RHS, "rhs");
		TestHelperAttribute.testAttribute(this.n, "frame", "getFrame", "setFrame", Wom3TableFrame.VOID, "void");
		TestHelperAttribute.testAttribute(this.n, "frame", "getFrame", "setFrame", Wom3TableFrame.VSIDES, "vsides");
	}

	@Test
	public void testRulesAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "rules", "getRules", "setRules", Wom3TableRules.ALL, "all");
		TestHelperAttribute.testAttribute(this.n, "rules", "getRules", "setRules", Wom3TableRules.COLS, "cols");
		TestHelperAttribute.testAttribute(this.n, "rules", "getRules", "setRules", Wom3TableRules.GROUPS, "groups");
		TestHelperAttribute.testAttribute(this.n, "rules", "getRules", "setRules", Wom3TableRules.NONE, "none");
		TestHelperAttribute.testAttribute(this.n, "rules", "getRules", "setRules", Wom3TableRules.ROWS, "rows");
	}

	@Test
	public void testSummary() throws Exception
	{
		TestHelperAttribute.testAttribute(n, "summary", "getSummary", "setSummary", "some text");
	}

	@Test
	public void testWidthAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(n, "width", "getWidth", "setWidth", new ValueWithUnitImpl(Wom3Unit.PERCENT, 5.2f), "5.2%");
		TestHelperAttribute.testAttribute(n, "width", "getWidth", "setWidth", new ValueWithUnitImpl(Wom3Unit.PIXELS, 5), "5");
	}

	@Test
	public void testDoesNotHaveBodyOrCaptionInitially() throws Exception
	{
		assertNull(n.getCaption());
		assertTrue(n.getPartitions().isEmpty());
		assertNull(n.getFirstChild());
		assertNull(n.getLastChild());
	}

	@Test
	public void testCanRemoveCaption() throws Exception
	{
		TableCaptionImpl caption = (TableCaptionImpl) TestHelperDoc.genElem("caption");
		n.setCaption(caption);
		assertTrue(caption == n.getCaption());
		assertTrue(caption == n.getFirstChild());
		assertTrue(caption == n.getLastChild());
		n.setCaption(null);
		assertNull(n.getCaption());
		assertNull(n.getFirstChild());
		assertNull(n.getLastChild());
	}

	@Test
	public void testCanRemoveBody() throws Exception
	{
		TableBodyImpl body = (TableBodyImpl) TestHelperDoc.genElem("tbody");
		n.appendChild(body);
		assertTrue(body == n.getPartitions().iterator().next());
		assertTrue(body == n.getFirstChild());
		assertTrue(body == n.getLastChild());
		n.removeChild(body);
		assertTrue(n.getPartitions().isEmpty());
		assertNull(n.getFirstChild());
		assertNull(n.getLastChild());
	}

	@Test
	public void testCanHaveBothBodyAndCaption() throws Exception
	{
		TableBodyImpl body = (TableBodyImpl) TestHelperDoc.genElem("tbody");
		n.appendChild(body);
		TableCaptionImpl caption = (TableCaptionImpl) TestHelperDoc.genElem("caption");
		n.setCaption(caption);
		assertTrue(caption == n.getCaption());
		assertTrue(body == n.getPartitions().iterator().next());
		assertTrue(caption == n.getFirstChild());
		assertTrue(body == n.getLastChild());
	}

	@Test
	public void testCanReplaceBothBodyAndCaption() throws Exception
	{
		TableBodyImpl body0 = (TableBodyImpl) TestHelperDoc.genElem("tbody");
		n.appendChild(body0);
		TableCaptionImpl caption0 = (TableCaptionImpl) TestHelperDoc.genElem("caption");
		n.setCaption(caption0);

		TableBodyImpl body1 = (TableBodyImpl) TestHelperDoc.genElem("tbody");
		n.replaceChild(body1, body0);
		TableCaptionImpl caption1 = (TableCaptionImpl) TestHelperDoc.genElem("caption");
		n.replaceChild(caption1, caption0);

		assertTrue(caption1 == n.getCaption());
		assertTrue(body1 == n.getPartitions().iterator().next());
		assertTrue(caption1 == n.getFirstChild());
		assertTrue(body1 == n.getLastChild());
	}
}
