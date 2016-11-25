package org.sweble.wikitext.engine;

import org.junit.Test;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;

public class PageTitleTest
{
	@Test
	public void testName() throws Exception
	{
		WikiConfigImpl config = DefaultConfigEnWp.generate();
		PageTitle.make(
				config,
				"Template:Did you know nominations/Steve Taylor & The Perfect Foil; Wow to the Deadness");
	}
}
