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

import java.io.IOException;

public class ProfileSerialization
{
	private static final SerializationMethod SERIALIZATION_METHOD = SerializationMethod.XML;
	
	private static final String ARTICLE = "raw-Germany.wikitext";
	
	public static void main(String[] args) throws IOException, Exception
	{
		Serializer serializer = new Serializer(
				ProfileSerialization.class.getResourceAsStream("/" + ARTICLE),
				ARTICLE);
		
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
		/*
		System.out.println("go");
		
		if (!PROFILE_DESERIALIZATION)
		{
			for (int i = 0; i < COUNT; ++i)
			{
				serializer.serializeTo(SERIALIZATION_METHOD);
				System.out.print('.');
			}
		}
		
		if (PROFILE_DESERIALIZATION)
		{
			byte[] serialized = serializer.serializeTo(SERIALIZATION_METHOD);
			for (int i = 0; i < COUNT; ++i)
			{
				serializer.deserializeFrom(SERIALIZATION_METHOD, serialized);
				System.out.print('.');
			}
		}
		*/
		// =====================================================================
		
		serializer.setTimeParsing(true);
		serializer.setTimeSerialization(true);
		serializer.setTimeDeserialization(true);
		serializer.setTimeCompression(true);
		
		//serializer.setMeasurementIterations(measurementIterations);
		//serializer.setWarumpIterations(warumpIterations);
		
		serializer.roundTrip(SerializationMethod.JAVA);
		serializer.roundTrip(SerializationMethod.JSON);
		serializer.roundTrip(SerializationMethod.XML);
	}
}
