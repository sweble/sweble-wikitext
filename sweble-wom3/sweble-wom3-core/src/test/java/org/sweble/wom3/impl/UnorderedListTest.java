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
import org.sweble.wom3.Wom3BulletStyle;
import org.sweble.wom3.Wom3ElementNode;

public class UnorderedListTest
{
	private final Wom3ElementNode n;

	// =========================================================================

	public UnorderedListTest()
	{
		this.n = TestHelperDoc.genElem("ul");
	}

	// =========================================================================

	@Test
	public void testTypeAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "type", "getItemType", "setItemType", Wom3BulletStyle.CIRCLE, "circle");
		TestHelperAttribute.testAttribute(this.n, "type", "getItemType", "setItemType", Wom3BulletStyle.DISC, "disc");
		TestHelperAttribute.testAttribute(this.n, "type", "getItemType", "setItemType", Wom3BulletStyle.SQUARE, "square");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidTypeAttribute() throws Exception
	{
		n.setAttribute("type", "foo");
	}
}
