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

package org.sweble.wikitext.engine.config;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;

public class WikiConfigTest
{
	@Test
	public void testSave() throws Exception
	{
		// We just want to know if the process works without failing fatally
		DefaultConfigEnWp.generate().save(new StringWriter());
	}

	@Test
	public void testLoadConfig() throws Exception
	{
		WikiConfigImpl gconf = DefaultConfigEnWp.generate();

		StringWriter writer = new StringWriter();
		gconf.save(writer);

		String original = writer.toString();
		StringReader reader = new StringReader(original);
		WikiConfigImpl c = WikiConfigImpl.load(reader);

		writer = new StringWriter();
		c.save(writer);

		// First check if saved results looks identical (easier to debug)
		assertEquals(original, writer.toString());

		// Now check if the configurations are really identical
		assertEquals(gconf, c);
	}

	@Test
	public void testXmlAndGeneratedConfigAreEqual() throws Exception
	{
		WikiConfigImpl gconf = DefaultConfigEnWp.generate();
		StringWriter wgconf = new StringWriter();
		gconf.save(wgconf);

		WikiConfigImpl xconf = WikiConfigImpl.load(getClass().getResourceAsStream(
				"/org/sweble/wikitext/engine/utils/DefaultConfigEnWp.xml"));
		StringWriter wxconf = new StringWriter();
		xconf.save(wxconf);

		// First check if saved results looks identical (easier to debug)
		assertEquals(wxconf.toString(), wgconf.toString());

		// Now check if the configurations are really identical
		assertEquals(xconf, gconf);
	}
}
