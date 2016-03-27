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

import java.io.IOException;

public class ProfileSerialization
{
	private static final String ARTICLE = "raw-Germany.wikitext";

	public static void main(String[] args) throws IOException, Exception
	{
		Serializer serializer = new Serializer(
				ProfileSerialization.class.getResourceAsStream("/" + ARTICLE),
				ARTICLE,
				"UTF8");

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
		serializer.setQuiet(false);

		// =====================================================================

		serializer.setTimeParsing(true);
		serializer.setTimeSerialization(false);
		serializer.setTimeDeserialization(false);
		serializer.setTimeCompression(false);

		//serializer.setMeasurementIterations(measurementIterations);
		//serializer.setWarumpIterations(warumpIterations);

		serializer.roundTrip(SerializationMethod.JAVA);
		//serializer.roundTrip(SerializationMethod.JSON);
		//serializer.roundTrip(SerializationMethod.XML);
	}
}
