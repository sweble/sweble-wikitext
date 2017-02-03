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
package org.sweble.wikitext.engine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;

public class PageTitleTest
{
	/** Tests fix to issue #45. */
	@Test
	public void testName() throws Exception
	{
		WikiConfigImpl config = DefaultConfigEnWp.generate();

		// Must not fail with illegal entity error
		PageTitle title = PageTitle.make(
				config,
				"Template:Did you know nominations/Steve Taylor & The Perfect Foil; Wow to the Deadness");

		PageTitle title2 = PageTitle.make(
				config,
				title.getNormalizedFullTitle());

		assertEquals(title, title2);

		PageTitle title3 = PageTitle.make(
				config,
				title.getDenormalizedFullTitle());

		assertEquals(title, title3);
	}
}
