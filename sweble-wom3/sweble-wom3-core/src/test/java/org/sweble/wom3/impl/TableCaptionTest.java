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
import org.sweble.wom3.Wom3TableCaptionAlign;

public class TableCaptionTest
{
	private TableCaptionImpl n = (TableCaptionImpl) TestHelperDoc.genElem("caption");

	@Test
	public void testTableVAlignAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3TableCaptionAlign.BOTTOM, "bottom");
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3TableCaptionAlign.LEFT, "left");
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3TableCaptionAlign.RIGHT, "right");
		TestHelperAttribute.testAttribute(this.n, "align", "getAlign", "setAlign", Wom3TableCaptionAlign.TOP, "top");
	}
}
