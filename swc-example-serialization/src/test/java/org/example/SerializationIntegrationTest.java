package org.example;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import de.fau.cs.osr.utils.NamedParametrized;

@RunWith(value = NamedParametrized.class)
public class SerializationIntegrationTest
{
	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		LinkedList<Object[]> inputs = new LinkedList<Object[]>();
		
		String[] articles = {
				"exp-Saxby+Chambliss.wikitext",
				"raw-Germany.wikitext",
				"raw-Wallace+Neff.wikitext",
				"raw-Zygmunt+Kubiak.wikitext" };
		
		for (String article : articles)
		{
			URL url = SerializationIntegrationTest.class.getResource(
					"/" + article);
			
			Serializer serializer =
					new Serializer(new File(url.getFile()));
			
			// Parsing options
			serializer.setParserAutoCorrectEnabled(false);
			serializer.setParserWarningsEnabled(false);
			serializer.setParserRtdEnabled(true);
			
			// Postprocessing options
			serializer.setPpSimplifyAst(true);
			serializer.setPpStripLocations(false);
			serializer.setPpStripAllAttributes(false);
			serializer.setPpStripRtdAttributes(false);
			
			// Be quiet, don't do timings
			serializer.setQuiet(true);
			
			inputs.add(new Object[] { article, serializer });
		}
		
		return inputs;
	}
	
	// =========================================================================
	
	private final Serializer serializer;
	
	// =========================================================================
	
	public SerializationIntegrationTest(String title, Serializer serializer)
	{
		this.serializer = serializer;
	}
	
	// =========================================================================
	
	@Test
	public void testJavaSerialization() throws Exception
	{
		// Must complete without throwing an exception
		serializer.roundTrip(SerializationMethod.JAVA);
	}
	
	@Test
	public void testXmlSerialization() throws Exception
	{
		// Must complete without throwing an exception
		serializer.roundTrip(SerializationMethod.XML);
	}
	
	@Test
	public void testJsonSerialization() throws Exception
	{
		// Must complete without throwing an exception
		serializer.roundTrip(SerializationMethod.JSON);
	}
}
