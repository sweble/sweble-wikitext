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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.serialization.EngineAstNodeConverter;
import org.sweble.wikitext.parser.comparer.WtComparer;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.utils.AstCompressor;
import org.sweble.wikitext.parser.utils.NodeStats;
import org.sweble.wikitext.parser.utils.NonExpandingParser;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import de.fau.cs.osr.ptk.common.json.JsonConverter;
import de.fau.cs.osr.ptk.common.xml.AstNodeConverter;
import de.fau.cs.osr.utils.ComparisonException;
import de.fau.cs.osr.utils.NameAbbrevService;
import de.fau.cs.osr.utils.StopWatch;

public class Serializer
{
	private static final StopWatch watch = new StopWatch();
	
	// =========================================================================
	
	private static final NameAbbrevService abbrevService = new NameAbbrevService(
			"de.fau.cs.osr.ptk.common.ast",
			"org.sweble.wikitext.parser",
			"org.sweble.wikitext.parser.nodes",
			"org.sweble.wikitext.parser.nodes.parser",
			"org.sweble.wikitext.parser.nodes.preprocessor",
			"org.sweble.wikitext.parser.nodes.utils",
			"org.sweble.wikitext.engine");
	
	private boolean quiet = false;
	
	private int warumpIterations = 128;
	
	private int measurementIterations = 32;
	
	private boolean timeParsing = true;
	
	private boolean timeCompression = true;
	
	private boolean timeDeserialization = true;
	
	private boolean timeSerialization = true;
	
	private boolean parserRtdEnabled = false;
	
	private boolean parserWarningsEnabled = false;
	
	private boolean parserAutoCorrectEnabled = false;
	
	private boolean ppStripAllAttributes = true;
	
	private boolean ppStripRtdAttributes = true;
	
	private boolean ppStripLocations = true;
	
	private boolean ppSimplifyAst = true;
	
	// =========================================================================
	
	private String title;
	
	private WtNode original;
	
	private int wikitextLength;
	
	private File source;
	
	// =========================================================================
	
	public Serializer(File source) throws Exception
	{
		this.source = source;
	}
	
	// =========================================================================
	
	public boolean isQuiet()
	{
		return quiet;
	}
	
	public int getWarumpIterations()
	{
		return warumpIterations;
	}
	
	public int getMeasurementIterations()
	{
		return measurementIterations;
	}
	
	public boolean isTimeParsing()
	{
		return timeParsing;
	}
	
	public boolean isTimeCompression()
	{
		return timeCompression;
	}
	
	public boolean isTimeDeserialization()
	{
		return timeDeserialization;
	}
	
	public boolean isTimeSerialization()
	{
		return timeSerialization;
	}
	
	public boolean isParserRtdEnabled()
	{
		return parserRtdEnabled;
	}
	
	public boolean isParserWarningsEnabled()
	{
		return parserWarningsEnabled;
	}
	
	public boolean isParserAutoCorrectEnabled()
	{
		return parserAutoCorrectEnabled;
	}
	
	public boolean isPpStripAllAttributes()
	{
		return ppStripAllAttributes;
	}
	
	public boolean isPpStripRtdAttributes()
	{
		return ppStripRtdAttributes;
	}
	
	public boolean isPpStripLocations()
	{
		return ppStripLocations;
	}
	
	public boolean isPpSimplifyAst()
	{
		return ppSimplifyAst;
	}
	
	public void setQuiet(boolean quiet)
	{
		this.quiet = quiet;
	}
	
	public void setWarumpIterations(int warumpIterations)
	{
		this.warumpIterations = warumpIterations;
	}
	
	public void setMeasurementIterations(int measurementIterations)
	{
		this.measurementIterations = measurementIterations;
	}
	
	public void setTimeParsing(boolean timeParsing)
	{
		this.timeParsing = timeParsing;
	}
	
	public void setTimeCompression(boolean timeCompression)
	{
		this.timeCompression = timeCompression;
	}
	
	public void setTimeDeserialization(boolean timeDeserialization)
	{
		this.timeDeserialization = timeDeserialization;
	}
	
	public void setTimeSerialization(boolean timeSerialization)
	{
		this.timeSerialization = timeSerialization;
	}
	
	public void setParserRtdEnabled(boolean parserRtdEnabled)
	{
		this.parserRtdEnabled = parserRtdEnabled;
	}
	
	public void setParserWarningsEnabled(boolean parserWarningsEnabled)
	{
		this.parserWarningsEnabled = parserWarningsEnabled;
	}
	
	public void setParserAutoCorrectEnabled(boolean parserAutoCorrectEnabled)
	{
		this.parserAutoCorrectEnabled = parserAutoCorrectEnabled;
	}
	
	public void setPpStripAllAttributes(boolean ppStripAllAttributes)
	{
		this.ppStripAllAttributes = ppStripAllAttributes;
	}
	
	public void setPpStripRtdAttributes(boolean ppStripRtdAttributes)
	{
		this.ppStripRtdAttributes = ppStripRtdAttributes;
	}
	
	public void setPpStripLocations(boolean ppStripLocations)
	{
		this.ppStripLocations = ppStripLocations;
	}
	
	public void setPpSimplifyAst(boolean ppSimplifyAst)
	{
		this.ppSimplifyAst = ppSimplifyAst;
	}
	
	// =========================================================================
	
	public void parse() throws Exception
	{
		if (original != null)
			return;
		
		String title = source.getName();
		if (!quiet)
		{
			System.out.println();
			System.out.println("Parsing: " + title);
		}
		
		String content = FileUtils.readFileToString(source);
		
		WtNode original = parse(title, content);
		
		int wikitextLength = content.getBytes().length;
		
		this.title = title;
		this.original = original;
		this.wikitextLength = wikitextLength;
		
		if (!quiet)
		{
			System.out.format("  Wikitext size: %d Bytes\n", wikitextLength);
			System.out.println();
			
			NodeStats.process(original);
		}
	}
	
	/**
	 * Do everything: Serialize, compress, deserialize and compare.
	 */
	public void roundTrip(SerializationMethod method) throws Exception
	{
		parse();
		
		if (!quiet)
		{
			System.out.println();
			System.out.println("==[  " + method.toString() + " serialization: " + title + "  ]==");
			System.out.println();
		}
		
		byte[] serialized = serialize(method);
		compare(deserialize(method, serialized));
		
		if (timeSerialization)
		{
			timedSerialization(method);
		}
		
		if (timeCompression)
		{
			timedZip(serialized);
			
			if (timeDeserialization)
				timedUnzipAndDeserialize(method, zip(serialized));
		}
		
		if (timeDeserialization)
		{
			timedDeserialization(method, serialized);
		}
	}
	
	public void serializeTo(SerializationMethod method, File output) throws IOException, Exception
	{
		parse();
		FileUtils.writeByteArrayToFile(output, serialize(method));
	}
	
	public byte[] serializeTo(SerializationMethod method) throws IOException, Exception
	{
		parse();
		return serialize(method);
	}
	
	public WtNode deserializeFrom(SerializationMethod method, File input) throws Exception
	{
		return deserialize(method, FileUtils.readFileToByteArray(input));
	}
	
	public WtNode deserializeFrom(SerializationMethod method, byte[] buffer) throws Exception
	{
		return deserialize(method, buffer);
	}
	
	public WtNode getAst() throws Exception
	{
		parse();
		return original;
	}
	
	// =========================================================================
	
	private WtNode parse(String title, String content) throws Exception
	{
		if (!quiet && timeParsing)
		{
			for (int i = 0; i < warumpIterations; ++i)
				doParse(title, content);
			
			watch.start();
			for (int i = 0; i < measurementIterations; ++i)
				original = doParse(title, content);
			watch.stop();
			
			float time = watch.getElapsedTime() / (float) measurementIterations;
			float len = content.getBytes().length;
			float tp = len / 1024.f / 1024.f / (time / 1000.f);
			
			System.out.println("  Parser settings:");
			System.out.println("    Warnings enabled     : " + parserWarningsEnabled);
			System.out.println("    RTD enabled          : " + parserRtdEnabled);
			System.out.println("    Auto correct enabled : " + parserAutoCorrectEnabled);
			System.out.println("    Strip all attributes : " + ppStripAllAttributes);
			System.out.println("    Strip RTD attributes : " + ppStripRtdAttributes);
			System.out.println("    Strip locations      : " + ppStripLocations);
			System.out.println("    Simplify AST         : " + ppSimplifyAst);
			System.out.println();
			System.out.println(String.format("  Time: %d ms", (long) time));
			System.out.println(String.format("  Throughput: %1.2f MB/s", tp));
		}
		
		return doParse(title, content);
	}
	
	private WtNode doParse(String title, String content) throws Exception
	{
		NonExpandingParser parser = new NonExpandingParser(
				parserWarningsEnabled,
				parserRtdEnabled,
				parserAutoCorrectEnabled);
		
		if (ppStripAllAttributes || ppStripRtdAttributes || ppStripLocations)
			parser.addVisitor(new StripAstVisitor(
					ppStripAllAttributes,
					ppStripRtdAttributes,
					ppStripLocations));
		
		if (ppSimplifyAst)
			parser.addVisitor(new AstCompressor());
		
		return parser.parseArticle(content, title);
	}
	
	// =========================================================================
	
	private byte[] timedSerialization(SerializationMethod method) throws Exception
	{
		if (!quiet)
		{
			for (int i = 0; i < warumpIterations; ++i)
				serialize(method);
			
			watch.start();
			for (int i = 0; i < measurementIterations; ++i)
				serialize(method);
			watch.stop();
		}
		
		byte[] serialized = serialize(method);
		
		if (!quiet)
		{
			long time = watch.getElapsedTime() / measurementIterations;
			long pow = serialized.length * 100 / wikitextLength;
			
			System.out.println("Timed serialization:");
			System.out.format("  Average time: %d ms\n", time);
			System.out.format("  Serialized size: %d Bytes\n", serialized.length);
			System.out.format("    Inflation: %3d%%\n", pow);
			System.out.println();
		}
		
		return serialized;
	}
	
	private byte[] serialize(SerializationMethod method) throws Exception
	{
		ByteArrayOutputStream objBaos = new ByteArrayOutputStream();
		
		switch (method)
		{
			case JAVA:
			{
				ObjectOutputStream oos = new ObjectOutputStream(objBaos);
				oos.writeObject(original);
				oos.close();
				break;
			}
			case XML:
			{
				OutputStreamWriter osw = new OutputStreamWriter(objBaos, "UTF-8");
				getXmlSerializer().toXML(original, osw);
				objBaos.close();
				break;
			}
			case JSON:
			{
				OutputStreamWriter osw = new OutputStreamWriter(objBaos, "UTF-8");
				Gson converter = JsonConverter.createGsonConverter(
						true,
						abbrevService,
						!ppStripLocations,
						WtNode.class,
						WtNodeList.class,
						WtText.class);
				converter.toJson(original, osw);
				osw.close();
				break;
			}
			default:
				throw new UnsupportedOperationException();
		}
		
		return objBaos.toByteArray();
	}
	
	private XStream getXmlSerializer()
	{
		AstNodeConverter<WtNode> converter =
				AstNodeConverter.forNodeType(WtNode.class);
		
		EngineAstNodeConverter.setup(new WikiConfigImpl(), converter);
		
		XStream xstream = new XStream(new DomDriver());
		xstream.registerConverter(converter);
		xstream.setMode(XStream.NO_REFERENCES);
		
		return xstream;
	}
	
	// =========================================================================
	
	private WtNode timedDeserialization(
			SerializationMethod method,
			byte[] serialized) throws Exception
	{
		if (!quiet)
		{
			for (int i = 0; i < warumpIterations; ++i)
				deserialize(method, serialized);
			
			watch.start();
			for (int i = 0; i < measurementIterations; ++i)
				deserialize(method, serialized);
			watch.stop();
		}
		
		WtNode deserialized = deserialize(method, serialized);
		
		if (!quiet)
		{
			long time = watch.getElapsedTime() / measurementIterations;
			
			System.out.println("Timed deserialization:");
			System.out.format("  Average time: %d ms\n", time);
			System.out.println();
		}
		
		return deserialized;
	}
	
	private WtNode deserialize(
			SerializationMethod method,
			byte[] serialized) throws Exception
	{
		return deserialize(method, new ByteArrayInputStream(serialized));
	}
	
	private WtNode deserialize(SerializationMethod method, InputStream is) throws Exception
	{
		WtNode result = null;
		switch (method)
		{
			case JAVA:
			{
				ObjectInputStream ois = new ObjectInputStream(is);
				result = (WtNode) ois.readObject();
				ois.close();
				break;
			}
			case XML:
			{
				result = (WtNode) getXmlSerializer().fromXML(is, original);
				is.close();
				break;
			}
			case JSON:
			{
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				Gson converter = JsonConverter.createGsonConverter(
						true,
						abbrevService,
						!ppStripLocations,
						WtNode.class,
						WtNodeList.class,
						WtText.class);
				result = converter.fromJson(isr, WtNode.class);
				isr.close();
				break;
			}
			default:
				throw new UnsupportedOperationException();
		}
		
		return result;
	}
	
	// =========================================================================
	
	private byte[] timedZip(byte[] serialized) throws Exception
	{
		if (!quiet)
		{
			for (int i = 0; i < warumpIterations; ++i)
				zip(serialized);
			
			watch.start();
			for (int i = 0; i < measurementIterations; ++i)
				zip(serialized);
			watch.stop();
		}
		
		byte[] zipped = zip(serialized);
		
		if (!quiet)
		{
			long time = watch.getElapsedTime() / measurementIterations;
			long pou = serialized.length * 100 / zipped.length;
			long pow = wikitextLength * 100 / zipped.length;
			
			System.out.println("Timed zip:");
			System.out.format("  Average time: %d ms\n", time);
			System.out.format("  Zipped size: %d Bytes\n", zipped.length);
			System.out.format("    Compression ratio serialized to serialized zipped: %3d%%\n", pou);
			System.out.format("    Compression ratio wikitext to serialized zipped: %3d%%\n", pow);
			System.out.println();
		}
		
		return zipped;
	}
	
	private byte[] zip(byte[] serialized) throws Exception
	{
		ByteArrayOutputStream zipBaos = new ByteArrayOutputStream();
		
		GZIPOutputStream gzos = new GZIPOutputStream(zipBaos);
		gzos.write(serialized);
		gzos.close();
		
		return zipBaos.toByteArray();
	}
	
	// =========================================================================
	
	private WtNode timedUnzipAndDeserialize(
			SerializationMethod method,
			byte[] serialized) throws Exception
	{
		if (!quiet)
		{
			for (int i = 0; i < warumpIterations; ++i)
				unzipAndDeserialize(method, serialized);
			
			watch.start();
			for (int i = 0; i < measurementIterations; ++i)
				unzipAndDeserialize(method, serialized);
			watch.stop();
			
			long time = watch.getElapsedTime() / measurementIterations;
			
			System.out.println("Timed unzip and deserialize:");
			System.out.format("  Average time: %d ms\n", time);
			System.out.println();
		}
		
		return unzipAndDeserialize(method, serialized);
	}
	
	private WtNode unzipAndDeserialize(
			SerializationMethod method,
			byte[] zipped) throws Exception
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(zipped);
		
		GZIPInputStream gzis = new GZIPInputStream(bais);
		
		BufferedInputStream bis = new BufferedInputStream(gzis);
		
		return deserialize(method, bis);
	}
	
	// =========================================================================
	
	private void compare(WtNode deserialize) throws ComparisonException
	{
		WtComparer.compareAndThrow(original, deserialize, true, true);
	}
}
