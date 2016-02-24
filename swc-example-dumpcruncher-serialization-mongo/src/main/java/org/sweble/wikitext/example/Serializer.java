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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.RtData;
import de.fau.cs.osr.ptk.common.json.AstNodeJsonTypeAdapter;
import de.fau.cs.osr.ptk.common.json.AstRtDataJsonTypeAdapter;
import de.fau.cs.osr.ptk.common.serialization.NodeFactory;
import de.fau.cs.osr.ptk.common.xml.AstNodeXmlConverter;
import de.fau.cs.osr.utils.StopWatch;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.serialization.EngineAstNodeConverter;
import org.sweble.wikitext.parser.WtRtData;
import org.sweble.wikitext.parser.comparer.WtComparer;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.NodeStats;
import org.sweble.wikitext.parser.utils.NonExpandingParser;

import java.io.*;
import java.util.Collections;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Serializer
{
	private static final StopWatch watch = new StopWatch();
	
	// =========================================================================
	
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
	

	private WtNode original;
	

	// =========================================================================
	
	public Serializer(WtNode source)
	{
		original = source;
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
	


	
	public void serializeTo(SerializationMethod method, File output) throws IOException, Exception
	{
		FileUtils.writeByteArrayToFile(output, serialize(method));
	}
	
	public byte[] serializeTo(SerializationMethod method) throws IOException, Exception
	{
		return serialize(method);
	}

	
	public WtNode getAst() throws Exception
	{
		return original;
	}
	
	public WtNode getFixedOriginalAst(final SerializationMethod method) throws Exception, CloneNotSupportedException
	{
		WtNode originalAst = getAst();
		if (method == SerializationMethod.JSON)
		{
			// As long as GSON does not handle Object collections and polymorphism 
			// correctly, the "warnings" attribute cannot be serialized
			originalAst = (WtNode) originalAst.clone();
			originalAst.setProperty("warnings", Collections.EMPTY_LIST);
		}
		return originalAst;
	}
	


	private byte[] serialize(SerializationMethod method) throws Exception
	{
		ByteArrayOutputStream objBaos = new ByteArrayOutputStream();
		
		switch (method)
		{
			case JAVA:
			{
				ObjectOutputStream oos = new ObjectOutputStream(objBaos);
				oos.writeObject(getAst());
				oos.close();
				break;
			}
			case XML:
			{
				OutputStreamWriter osw = new OutputStreamWriter(objBaos, "UTF-8");
				getXmlSerializer().toXML(getAst(), osw);
				osw.close();
				break;
			}
			case JSON:
			{
				OutputStreamWriter osw = new OutputStreamWriter(objBaos, "UTF-8");
				getJsonSerializer().toJson(getAst(), osw);
				osw.close();
				break;
			}
			default:
				objBaos.close();
				throw new UnsupportedOperationException();
		}
		
		return objBaos.toByteArray();
	}
	
	private XStream getXmlSerializer()
	{
		AstNodeXmlConverter<WtNode> converter =
				AstNodeXmlConverter.forNodeType(WtNode.class);
		
		EngineAstNodeConverter.setup(new WikiConfigImpl(), converter);
		
		XStream xstream = new XStream(new DomDriver());
		xstream.registerConverter(converter);
		xstream.setMode(XStream.NO_REFERENCES);
		
		return xstream;
	}
	
	private Gson getJsonSerializer()
	{
		final AstNodeJsonTypeAdapter<WtNode> nodeConverter =
				AstNodeJsonTypeAdapter.forNodeType(WtNode.class);
		EngineAstNodeConverter.setup(new WikiConfigImpl(), nodeConverter);
		
		// As long as GSON does not handle Object collections and polymorphism 
		// correctly, the "warnings" attribute cannot be serialized
		nodeConverter.suppressProperty("warnings");
		nodeConverter.setNodeFactory(new NodeFactory<WtNode>()
		{
			NodeFactory<WtNode> nf = nodeConverter.getNodeFactory();
			
			@Override
			public WtNode instantiateNode(Class<?> clazz)
			{
				return nf.instantiateNode(clazz);
			}
			
			@Override
			public WtNode instantiateDefaultChild(
					NamedMemberId id,
					Class<?> childType)
			{
				return nf.instantiateDefaultChild(id, childType);
			}
			
			@Override
			public Object instantiateDefaultProperty(
					NamedMemberId id,
					Class<?> type)
			{
				if (id.memberName == "warnings")
					return Collections.EMPTY_LIST;
				return nf.instantiateDefaultProperty(id, type);
			}
		});
		
		AstRtDataJsonTypeAdapter<WtRtData> rtdConverter = new AstRtDataJsonTypeAdapter<WtRtData>(WtRtData.class);
		EngineAstNodeConverter.setup(rtdConverter);
		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeHierarchyAdapter(RtData.class, rtdConverter);
		builder.registerTypeHierarchyAdapter(AstNode.class, nodeConverter);
		builder.serializeNulls();
		builder.setPrettyPrinting();
		return builder.create();
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
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				result = (WtNode) getXmlSerializer().fromXML(isr, WtNode.class);
				isr.close();
				break;
			}
			case JSON:
			{
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				result = (WtNode) getJsonSerializer().fromJson(isr, WtNode.class);
				isr.close();
				break;
			}
			default:
				throw new UnsupportedOperationException();
		}
		
		return result;
	}
	

}
