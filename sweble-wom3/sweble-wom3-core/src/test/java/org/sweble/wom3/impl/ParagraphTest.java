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
import org.sweble.wom3.Wom3HorizAlign;

public class ParagraphTest
{
	private ParagraphImpl n = (ParagraphImpl) TestHelperDoc.genElem("p");

	// =========================================================================

	@Test
	public void testInitialGapsAreZero() throws Exception
	{
		assertEquals(0, n.getTopGap());
		assertEquals(0, n.getBottomGap());
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

	@Test(expected = UnsupportedOperationException.class)
	public void testCannotRemoveTopGap() throws Exception
	{
		n.removeAttribute("topgap");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testCannotRemoveBottomGap() throws Exception
	{
		n.removeAttribute("bottomgap");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCannotSetNegativeTopGap() throws Exception
	{
		n.setTopGap(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCannotSetNegativeBottomGap() throws Exception
	{
		n.setBottomGap(-1);
	}

	@Test
	public void testGapSettersWork() throws Exception
	{
		TestHelperAttribute.testFixedAttributeNoObjectSetter(
				n, "topgap", "getTopGap", "setTopGap", 1, "1", 0, "0");

		TestHelperAttribute.testFixedAttributeNoObjectSetter(
				n, "bottomgap", "getBottomGap", "setBottomGap", 1, "1", 0, "0");
	}
}
