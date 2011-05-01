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

package org.sweble.wikitext.lazy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

import org.junit.Test;

public class LinkPrefixTest
{
	@Test
	public void test()
	{
		String prefix = "asdfäöüß";
		
		Matcher m = Pattern.compile("(" + "[äöüßa-z]+" + ")$").matcher(
				"Hallo Welt, hier kommt ein German Link " + prefix);
		
		Assert.assertTrue(m.find());
		Assert.assertEquals(prefix, m.group(1));
	}
}
