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
package org.example;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.WtAstPrinter;

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
		byte[] serialized = serializer.serializeTo(SerializationMethod.JAVA);
		
		WtNode deserializedAst = serializer.deserializeFrom(SerializationMethod.JAVA, serialized);
		
		String originalAstPrinted = WtAstPrinter.print(serializer.getAst());
		
		String deserializedAstPrinted = WtAstPrinter.print(deserializedAst);
		
		assertEquals(originalAstPrinted, deserializedAstPrinted);
		
		serializer.roundTrip(SerializationMethod.JAVA);
	}
	
	/*
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
	*/
}
