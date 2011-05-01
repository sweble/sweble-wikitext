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

package org.sweble.wikitext.lazy;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.sweble.wikitext.lazy.parser.RtData;
import org.sweble.wikitext.lazy.utils.NodeStats;

import xtc.parser.ParseException;
import de.fau.cs.osr.ptk.common.AstPrinter;
import de.fau.cs.osr.ptk.common.Visitor;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.AstNodeInputStream;
import de.fau.cs.osr.ptk.common.ast.AstNodeOutputStream;
import de.fau.cs.osr.ptk.common.ast.Location;
import de.fau.cs.osr.ptk.common.xml.XmlWriter;
import de.fau.cs.osr.utils.StopWatch;

@SuppressWarnings("deprecation")
public class SerializationTest
{
	private static final StopWatch watch = new StopWatch();
	
	private static final boolean QUIET = false;
	
	// Should be at least 128 to get realistic values
	private static final int WARMUP_ITERATIONS = 1;
	
	// Should be at least 32 to get realistic values
	private static final int MEASUREMENT_ITERATIONS = 1;
	
	private static final boolean TEXT_PARSING = true;
	
	private static final boolean TIME_ZIPPER = true;
	
	private static final boolean TIME_RECOVERY = true;
	
	private static final boolean TIME_SERIALIZATION = true;
	
	private static final boolean TEXT_COMPARE = true;
	
	private static final boolean STRIP_ATTRIBUTES = false;
	
	private static final boolean KEEP_SOME_ATTRS = true;
	
	private static final boolean GATHER_RTD = false;
	
	private static final boolean WARNINGS_ENABLED = false;
	
	private static final ArrayList<ParsedWikitext> wikitexts =
	        new ArrayList<ParsedWikitext>();
	
	private static AstNode original;
	
	private static int wikitextLength;
	
	@BeforeClass
	public static void loadWikitext() throws IOException, ParseException
	{
		parse("raw-Germany.wikitext");
		parse("raw-Wallace+Neff.wikitext");
		parse("raw-Zygmunt+Kubiak.wikitext");
		parse("exp-Saxby+Chambliss.wikitext");
	}
	
	@Ignore
	@Test
	public void testNativeSerialization() throws IOException, ParseException, ClassNotFoundException
	{
		// Doesn't work any more. We are now using nodes without nullary ctor.
		// The native code can't handle that.
		doSerialization(Method.NATIVE);
	}
	
	@Test
	public void testJavaSerialization() throws IOException, ParseException, ClassNotFoundException
	{
		doSerialization(Method.JAVA);
	}
	
	@Ignore
	@Test
	public void testXmlSerialization() throws IOException, ParseException, ClassNotFoundException
	{
		// Now implemented yet :(
		doSerialization(Method.XML);
	}
	
	private void doSerialization(Method method) throws IOException, ParseException, ClassNotFoundException
	{
		for (ParsedWikitext wt : wikitexts)
		{
			prepare(wt);
			
			if (!QUIET)
			{
				System.out.println();
				System.out.println("==[  " + method.toString() + " serialization: " + wt.getTitle() + "  ]==");
				System.out.println();
			}
			
			doTest(method);
		}
	}
	
	private void prepare(ParsedWikitext wt) throws IOException, ParseException
	{
		original = wt.getAst();
		
		wikitextLength = wt.getLength();
	}
	
	private static void parse(String title) throws IOException, ParseException
	{
		if (!QUIET)
		{
			System.out.println();
			System.out.println("Parsing: " + title);
		}
		
		String path = "/serialization/" + title;
		
		String content = load(path);
		
		AstNode original = parse(path, content);
		
		int wikitextLength = content.getBytes().length;
		
		wikitexts.add(new ParsedWikitext(title, original, wikitextLength));
		
		if (!QUIET)
		{
			System.out.format("  Wikitext size: %d Bytes\n", wikitextLength);
			System.out.println();
		}
		
		NodeStats.process(original);
	}
	
	private void doTest(Method method) throws IOException, ClassNotFoundException, ParseException
	{
		byte[] serialized = serialize(method);
		compare(deserialize(method, serialized));
		
		if (TIME_SERIALIZATION)
		{
			timedSerialization(method);
		}
		
		if (TIME_ZIPPER)
		{
			timedZip(serialized);
			
			if (TIME_RECOVERY)
				timedUnzipAndDeserialize(method, zip(serialized));
		}
		
		if (TIME_RECOVERY)
		{
			timedDeserialization(method, serialized);
		}
	}
	
	private static String load(String title) throws IOException
	{
		InputStream in = SerializationTest.class.getResourceAsStream(title);
		
		return IOUtils.toString(in);
	}
	
	private static AstNode parse(String title, String content) throws IOException, ParseException
	{
		if (TEXT_PARSING)
		{
			for (int i = 0; i < WARMUP_ITERATIONS; ++i)
				doParse(title, content);
		}
		
		AstNode original = null;
		
		watch.start();
		for (int i = 0; i < MEASUREMENT_ITERATIONS; ++i)
			original = doParse(title, content);
		watch.stop();
		
		if (!QUIET)
		{
			float time = watch.getElapsedTime() / (float) MEASUREMENT_ITERATIONS;
			float len = content.getBytes().length;
			float tp = len / 1024.f / 1024.f / (time / 1000.f);
			
			System.out.format("  Time: %d ms\n", (long) time);
			System.out.format("  Throughput: %1.2f mb/s\n", tp);
		}
		
		return original;
	}
	
	private static AstNode doParse(String title, String content) throws IOException, ParseException
	{
		FullParser parser = new FullParser(WARNINGS_ENABLED, GATHER_RTD, false);
		
		if (STRIP_ATTRIBUTES)
		{
			parser.addVisitors(Arrays.asList(
			        new Visitor[] { new StripAstVisitor() }));
		}
		
		AstNode original = parser.parseArticle(content, title);
		return original;
	}
	
	private byte[] timedSerialization(Method method) throws IOException
	{
		for (int i = 0; i < WARMUP_ITERATIONS; ++i)
			serialize(method);
		
		byte[] serialized = null;
		
		watch.start();
		for (int i = 0; i < MEASUREMENT_ITERATIONS; ++i)
			serialized = serialize(method);
		watch.stop();
		
		if (!QUIET)
		{
			long time = watch.getElapsedTime() / MEASUREMENT_ITERATIONS;
			long pow = serialized.length * 100 / wikitextLength;
			
			System.out.println("Timed serialization:");
			System.out.format("  Average time: %d ms\n", time);
			System.out.format("  Serialized size: %d Bytes\n", serialized.length);
			System.out.format("    Inflation: %3d%%\n", pow);
			System.out.println();
		}
		
		return serialized;
	}
	
	private byte[] serialize(Method method) throws IOException
	{
		ByteArrayOutputStream objBaos = new ByteArrayOutputStream();
		
		doSerialize(method, objBaos);
		
		return objBaos.toByteArray();
	}
	
	private void doSerialize(Method method, OutputStream os) throws IOException
	{
		switch (method)
		{
			case NATIVE:
			{
				AstNodeOutputStream oos = new AstNodeOutputStream(os);
				oos.writeNode(original);
				oos.close();
				break;
			}
			case JAVA:
			{
				ObjectOutputStream oos = new ObjectOutputStream(os);
				oos.writeObject(original);
				oos.close();
				break;
			}
			case XML:
			{
				XmlWriter xmlw = new XmlWriter(os);
				xmlw.go(original);
				break;
			}
		}
	}
	
	private AstNode timedDeserialization(Method method, byte[] serialized) throws IOException, ClassNotFoundException
	{
		for (int i = 0; i < WARMUP_ITERATIONS; ++i)
			deserialize(method, serialized);
		
		AstNode deserialized = null;
		
		watch.start();
		for (int i = 0; i < MEASUREMENT_ITERATIONS; ++i)
			deserialized = deserialize(method, serialized);
		watch.stop();
		
		if (!QUIET)
		{
			long time = watch.getElapsedTime() / MEASUREMENT_ITERATIONS;
			
			System.out.println("Timed deserialization:");
			System.out.format("  Average time: %d ms\n", time);
			System.out.println();
		}
		
		return deserialized;
	}
	
	private AstNode deserialize(Method method, byte[] serialized) throws IOException, ClassNotFoundException
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
		
		return doDeserialize(method, bais);
	}
	
	private AstNode doDeserialize(Method method, InputStream is) throws IOException, ClassNotFoundException
	{
		switch (method)
		{
			case NATIVE:
			{
				AstNodeInputStream ois = new AstNodeInputStream(is);
				return ois.readNode();
			}
			case JAVA:
			{
				ObjectInputStream ois = new ObjectInputStream(is);
				return (AstNode) ois.readObject();
			}
			default:
				throw new UnsupportedOperationException();
		}
	}
	
	private byte[] timedZip(byte[] serialized) throws IOException
	{
		for (int i = 0; i < WARMUP_ITERATIONS; ++i)
			zip(serialized);
		
		byte[] zipped = null;
		
		watch.start();
		for (int i = 0; i < MEASUREMENT_ITERATIONS; ++i)
			zipped = zip(serialized);
		watch.stop();
		
		if (!QUIET)
		{
			long time = watch.getElapsedTime() / MEASUREMENT_ITERATIONS;
			long pou = serialized.length * 100 / zipped.length;
			long pow = wikitextLength * 100 / zipped.length;
			
			System.out.println("Timed zip:");
			System.out.format("  Average time: %d ms\n", time);
			System.out.format("  Zipped size: %d Bytes\n", zipped.length);
			System.out.format("    Compression: %3d%%\n", pou);
			System.out.format("    Wikitext compression: %3d%%\n", pow);
			System.out.println();
		}
		
		return zipped;
	}
	
	private byte[] zip(byte[] serialized) throws IOException
	{
		ByteArrayOutputStream zipBaos = new ByteArrayOutputStream();
		
		GZIPOutputStream gzos = new GZIPOutputStream(zipBaos);
		gzos.write(serialized);
		gzos.close();
		
		return zipBaos.toByteArray();
	}
	
	private AstNode timedUnzipAndDeserialize(Method method, byte[] serialized) throws IOException, ClassNotFoundException
	{
		for (int i = 0; i < WARMUP_ITERATIONS; ++i)
			unzipAndDeserialize(method, serialized);
		
		AstNode deserialized = null;
		
		watch.start();
		for (int i = 0; i < MEASUREMENT_ITERATIONS; ++i)
			deserialized = unzipAndDeserialize(method, serialized);
		watch.stop();
		
		if (!QUIET)
		{
			long time = watch.getElapsedTime() / MEASUREMENT_ITERATIONS;
			
			System.out.println("Timed unzip and deserialize:");
			System.out.format("  Average time: %d ms\n", time);
			System.out.println();
		}
		
		return deserialized;
	}
	
	private AstNode unzipAndDeserialize(Method method, byte[] zipped) throws IOException, ClassNotFoundException
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(zipped);
		
		GZIPInputStream gzis = new GZIPInputStream(bais);
		
		BufferedInputStream bis = new BufferedInputStream(gzis);
		
		return doDeserialize(method, bis);
	}
	
	private void compare(AstNode recovered)
	{
		if (TEXT_COMPARE)
		{
			String originalPrinted = AstPrinter.print(original);
			
			String recoveredPrinted = AstPrinter.print(recovered);
			
			Assert.assertEquals(originalPrinted, recoveredPrinted);
		}
		
		Assert.assertEquals(original, recovered);
	}
	
	// =========================================================================
	
	public static enum Method
	{
		NATIVE,
		JAVA,
		XML,
	}
	
	public static final class ParsedWikitext
	{
		private final String title;
		
		private final AstNode ast;
		
		private final int length;
		
		public ParsedWikitext(String title, AstNode ast, int length)
		{
			this.title = title;
			this.ast = ast;
			this.length = length;
		}
		
		public String getTitle()
		{
			return title;
		}
		
		public AstNode getAst()
		{
			return ast;
		}
		
		public int getLength()
		{
			return length;
		}
	}
	
	// =========================================================================
	
	public static final class StripAstVisitor
	        extends
	            Visitor
	{
		public void visit(AstNode n)
		{
			if (KEEP_SOME_ATTRS)
			{
				RtData rtData = (RtData) n.getAttribute("RTD");
				n.clearAttributes();
				if (rtData != null)
				{
					cleanRtd(rtData);
					if (rtData.getRts().length > 0)
						n.setAttribute("RTD", rtData);
				}
			}
			else
			{
				n.setNativeLocation((Location) null);
				n.clearAttributes();
			}
			
			iterate(n);
		}
		
		private void cleanRtd(RtData rtData)
		{
			if (rtData.getRts() == null)
				return;
			
			for (Object[] x : rtData.getRts())
			{
				if (x == null)
					continue;
				
				for (Object y : x)
				{
					if (y instanceof AstNode)
						dispatch((AstNode) y);
				}
			}
		}
	}
}
