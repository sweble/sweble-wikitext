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
package org.sweble.wikitext.engine.utils;

import org.junit.Assert;
import org.junit.Test;

public class UnicodeBehavior
{
	@Test
	public void testNonBmpSplit()
	{
		// non BMP Unicode Character U+20000 = U+D840 U+DC00 in UTF-16
		String x = "\uD840\uDC00...";

		Assert.assertEquals(5, x.length());
		Assert.assertEquals("\uD840", x.substring(0, 1));
		Assert.assertEquals("\uDC00", x.substring(1, 2));
		Assert.assertEquals(2, x.indexOf('.'));
		Assert.assertEquals(0, x.indexOf(0x20000));
		Assert.assertEquals(0x20000, x.codePointAt(0));
		Assert.assertEquals(0xDC00, x.codePointAt(1));
		Assert.assertEquals('.', x.codePointAt(2));
	}
}
