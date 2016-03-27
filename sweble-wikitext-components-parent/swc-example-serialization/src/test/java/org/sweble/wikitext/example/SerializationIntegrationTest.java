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
package org.sweble.wikitext.example;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
	private static final boolean VERBOSE = false;

	private static final boolean TEXTUAL_COMPARISON = false;

	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		LinkedList<Object[]> inputs = new LinkedList<Object[]>();

		String[] articles = {
				"exp-Saxby Chambliss.wikitext",
				"raw-Germany.wikitext",
				"raw-Wallace Neff.wikitext",
				"raw-Zygmunt Kubiak.wikitext" };

		for (String title : articles)
			inputs.add(new Object[] { title });

		return inputs;
	}

	// =========================================================================

	private final String title;

	private Serializer serializer;

	// =========================================================================

	public SerializationIntegrationTest(String title)
	{
		this.title = title;
	}

	// =========================================================================

	@Test
	public void testJavaSerialization() throws Exception
	{
		setupSerializer();
		go(SerializationMethod.JAVA, TEXTUAL_COMPARISON, VERBOSE);
	}

	@Test
	public void testXmlSerialization() throws Exception
	{
		setupSerializer();
		go(SerializationMethod.XML, TEXTUAL_COMPARISON, VERBOSE);
	}

	@Test
	public void testJsonSerialization() throws Exception
	{
		setupSerializer();

		// As long as GSON does not handle Object collections and polymorphism 
		// correctly, the "warnings" attribute cannot be serialized
		serializer.setParserWarningsEnabled(false);

		go(SerializationMethod.JSON, TEXTUAL_COMPARISON, VERBOSE);
	}

	// =========================================================================

	private void setupSerializer() throws Exception
	{
		this.serializer = new Serializer(
				SerializationIntegrationTest.class.getResourceAsStream("/" + title),
				title,
				"UTF8");

		// Parsing options
		serializer.setParserAutoCorrectEnabled(false);
		serializer.setParserWarningsEnabled(true);
		serializer.setParserRtdEnabled(true);

		// Postprocessing options
		serializer.setPpSimplifyAst(true);
		serializer.setPpStripLocations(false);
		serializer.setPpStripAllAttributes(false);
		serializer.setPpStripRtdAttributes(false);

		// Be quiet, don't do timings
		serializer.setQuiet(true);
	}

	private void go(
			final SerializationMethod method,
			final boolean textualComparison,
			final boolean verbose) throws IOException, Exception, UnsupportedEncodingException
	{
		if (textualComparison)
		{
			byte[] serialized = serializer.serializeTo(method);

			if (verbose)
				System.out.println(new String(serialized, "UTF8"));

			WtNode deserializedAst = serializer.deserializeFrom(method, serialized);

			String originalAstPrinted = WtAstPrinter.print(serializer.getFixedOriginalAst(method));

			String deserializedAstPrinted = WtAstPrinter.print(deserializedAst);

			assertEquals(originalAstPrinted, deserializedAstPrinted);
		}

		serializer.roundTrip(method);
	}
}
