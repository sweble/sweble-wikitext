package org.sweble.wikitext.engine.config;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;
import org.sweble.wikitext.engine.utils.DefaultConfigEn;

public class WikiConfigTest
{
	@Test
	public void testSave() throws Exception
	{
		// We just want to know if the process works without failing fatally
		DefaultConfigEn.generate().save(new StringWriter());
	}
	
	@Test
	public void testLoadConfig() throws Exception
	{
		WikiConfigImpl gconf = DefaultConfigEn.generate();
		
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
		WikiConfigImpl gconf = DefaultConfigEn.generate();
		StringWriter wgconf = new StringWriter();
		gconf.save(wgconf);
		
		WikiConfigImpl xconf = WikiConfigImpl.load(getClass().getResourceAsStream("/org/sweble/wikitext/engine/DefaultEnWikiConfig.xml"));
		StringWriter wxconf = new StringWriter();
		xconf.save(wxconf);
		
		// First check if saved results looks identical (easier to debug)
		assertEquals(wgconf.toString(), wxconf.toString());
		
		// Now check if the configurations are really identical
		assertEquals(gconf, xconf);
	}
}
