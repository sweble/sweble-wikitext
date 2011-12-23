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
package org.sweble.wikitext.engine.astwom;

import org.junit.Test;
import org.sweble.wikitext.engine.astwom.adapters.PageAdapter;
import org.sweble.wikitext.engine.wom.WomCategory;
import org.sweble.wikitext.engine.wom.WomPage;

public class CategoryAdapterTest
{
	@Test(expected = IllegalArgumentException.class)
	public void test() throws Throwable
	{
		WomCategory cat = null;
		try
		{
			WomPage p = new PageAdapter((String) null, (String) null, "Title");
			p.setCategory("Foo");
			p.setCategory("Bar");
			for (WomCategory c : p.getCategories())
			{
				cat = c;
				c.setAttribute("name", "FooBar");
				break;
			}
			p.setCategory("Bar");
		}
		catch (Exception e)
		{
			throw new Throwable(e)
			{
				private static final long serialVersionUID = 1L;
			};
		}
		cat.setAttribute("name", "Bar");
	}
}
