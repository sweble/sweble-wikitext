/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.engine.serialization;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.sweble.engine.serialization.CompressorFactory.CompressionFormat;
import org.sweble.engine.serialization.WomSerializer.SerializationFormat;
import org.sweble.wom3.Wom3Document;
import org.sweble.wom3.swcadapter.utils.WtWom3Toolbox.Artifacts;

import de.fau.cs.osr.utils.SimpleConsoleOutput;
import de.fau.cs.osr.utils.StringUtils;
import de.fau.cs.osr.utils.TestResourcesFixture;

public class TimeSerialization
		extends
			SerializationTestBase
{
	private static final int WARM_UP = 100;
	
	private static final int ITERATIONS = 100;
	
	private static final String INPUT_SUB_DIR = "/various";
	
	private static final String FILTER_RX = ".*?\\.wikitext";
	
	private static final CompressionFormat[] compressionFormats = {
			CompressionFormat.BZIP2,
			CompressionFormat.GZIP,
			CompressionFormat.XZ
	};
	
	private static final SerializationFormat[] serializationFormats = {
			SerializationFormat.JAVA,
			SerializationFormat.JSON,
			SerializationFormat.XML
	};
	
	private static final Charset CHARSET = Charset.forName("UTF8");
	
	// =========================================================================
	
	public static void main(String[] args) throws Exception
	{
		new TimeSerialization(SerializationTestBase.getTestResourcesFixture()).run();
	}
	
	// =========================================================================
	
	private final WomSerializer serializer = new WomSerializer();
	
	public TimeSerialization(TestResourcesFixture resources)
	{
		super(resources);
	}
	
	private void run() throws Exception
	{
		TestResourcesFixture resources = getTestResourcesFixture();
		List<File> inputs = resources.gather(INPUT_SUB_DIR, FILTER_RX, false);
		
		for (File file : inputs)
		{
			String title = file.getName();
			SimpleConsoleOutput.printBigSep(title);
			
			Artifacts af = timeParsing(file);
			Wom3Document wom = af.womDoc;
			
			System.out.println();
			long uncompressedWikiMarkupSize = getSize(af.wm);
			printSize(2, "wiki markup", uncompressedWikiMarkupSize);
			
			for (CompressionFormat compressionFormat : compressionFormats)
			{
				System.out.println();
				System.out.println("  Compression format: " + compressionFormat);
				
				System.out.println();
				long compressedWikiMarkupSize = getSize(compress(af.wm, compressionFormat));
				printSize(4,
						"compressed wiki markup", compressedWikiMarkupSize,
						"uncompressed wiki markup", uncompressedWikiMarkupSize);
				
				for (SerializationFormat serializationFormat : serializationFormats)
				{
					System.out.println();
					SimpleConsoleOutput.printSep(4, "Serialization format: " + serializationFormat);
					
					for (int format = 0; format < 4; ++format)
					{
						boolean compact = ((format & 1) == 1);
						boolean pretty = ((format & 2) == 2);
						if (!compact || pretty)
							continue;
						
						System.out.println();
						System.out.println("      "
								+ "Compact: " + compact + "; "
								+ "Pretty: " + pretty);
						
						System.out.println();
						byte[] serialized = timeSerialization(wom, serializationFormat, compact, pretty);
						timeDeserialization(serialized, serializationFormat, compact, pretty);
						
						if (!serializationFormat.equals("JAVA"))
							saveSerialized(title, serializationFormat, compact, pretty, serialized);
						
						System.out.println();
						printSize(8,
								"serialized WOM document", getSize(serialized),
								"uncompressed wiki markup", uncompressedWikiMarkupSize);
						
						System.out.println();
						byte[] compressed = timeCompression(serialized, compressionFormat);
						timeDecompression(compressed, compressionFormat);
						
						System.out.println();
						printSize(8,
								"compressed WOM document", getSize(compressed),
								"compressed wiki markup", compressedWikiMarkupSize);
						
						System.out.println();
						compressed = timeCompressedSerialization(wom, compressionFormat, serializationFormat, compact, pretty);
						timeCompressedDeserialization(compressed, compressionFormat, serializationFormat, compact, pretty);
					}
				}
			}
		}
	}
	
	private void saveSerialized(
			String title,
			SerializationFormat serializationFormat,
			boolean compact,
			boolean pretty,
			byte[] serialized) throws IOException
	{
		String fname = title + "-" + serializationFormat;
		if (compact)
			fname += "-compact";
		if (pretty)
			fname += "-pretty";
		fname += "." + serializationFormat.toString().toLowerCase();
		FileUtils.writeByteArrayToFile(new File(fname), serialized);
	}
	
	private Artifacts timeParsing(final File file) throws Exception
	{
		RunMe runMe = new RunMe()
		{
			@Override
			public void run() throws Exception
			{
				parse(file);
			}
		};
		print(2, "Parsing", timeIt(runMe, WARM_UP, ITERATIONS));
		return parse(file);
	}
	
	private byte[] timeSerialization(
			final Wom3Document wom,
			final SerializationFormat serializationFormat,
			final boolean compact,
			final boolean pretty) throws Exception
	{
		RunMe runMe = new RunMe()
		{
			@Override
			public void run() throws Exception
			{
				serializer.serialize(wom, serializationFormat, compact, pretty);
			}
		};
		print(8, "Serialization", timeIt(runMe, WARM_UP, ITERATIONS));
		return serializer.serialize(wom, serializationFormat, compact, pretty);
	}
	
	private void timeDeserialization(
			final byte[] serialized,
			final SerializationFormat serializationFormat,
			final boolean compact,
			final boolean pretty) throws Exception
	{
		RunMe runMe = new RunMe()
		{
			@Override
			public void run() throws Exception
			{
				serializer.deserialize(serialized, serializationFormat, compact);
			}
			
		};
		print(8, "Deserialization", timeIt(runMe, WARM_UP, ITERATIONS));
		serializer.deserialize(serialized, serializationFormat, compact);
	}
	
	private byte[] timeCompression(
			final byte[] serialized,
			final CompressionFormat compressionFormat) throws Exception
	{
		RunMe runMe = new RunMe()
		{
			@Override
			public void run() throws Exception
			{
				serializer.compress(serialized, compressionFormat);
			}
		};
		print(8, "Compression", timeIt(runMe, WARM_UP, ITERATIONS));
		return serializer.compress(serialized, compressionFormat);
	}
	
	private void timeDecompression(
			final byte[] compressed,
			final CompressionFormat compressionFormat) throws Exception
	{
		RunMe runMe = new RunMe()
		{
			@Override
			public void run() throws Exception
			{
				serializer.decompress(compressed, compressionFormat);
			}
		};
		print(8, "Decompression", timeIt(runMe, WARM_UP, ITERATIONS));
		serializer.decompress(compressed, compressionFormat);
	}
	
	private byte[] timeCompressedSerialization(
			final Wom3Document wom,
			final CompressionFormat compressionFormat,
			final SerializationFormat serializationFormat,
			final boolean compact,
			final boolean pretty) throws Exception
	{
		RunMe runMe = new RunMe()
		{
			@Override
			public void run() throws Exception
			{
				serializer.serializeAndCompress(wom, compressionFormat, serializationFormat, compact, pretty);
			}
		};
		print(8, "Serialization & Compression", timeIt(runMe, WARM_UP, ITERATIONS));
		return serializer.serializeAndCompress(wom, compressionFormat, serializationFormat, compact, pretty);
	}
	
	private void timeCompressedDeserialization(
			final byte[] compressed,
			final CompressionFormat compressionFormat,
			final SerializationFormat serializationFormat,
			final boolean compact,
			final boolean pretty) throws Exception
	{
		RunMe runMe = new RunMe()
		{
			@Override
			public void run() throws Exception
			{
				serializer.decompressAndDeserialize(compressed, compressionFormat, serializationFormat, compact);
			}
		};
		print(8, "Decompression and deserialization", timeIt(runMe, WARM_UP, ITERATIONS));
		serializer.decompressAndDeserialize(compressed, compressionFormat, serializationFormat, compact);
	}
	
	// =========================================================================
	
	private Artifacts parse(final File file) throws Exception
	{
		return wmToWom(file);
	}
	
	// =========================================================================
	
	private void print(int indent, String header, TimingResults timings)
	{
		String msg = String.format(""
				+ "Mean:   % 7.2f ms\n"
				+ "Median: % 7.2f ms\n"
				+ "StdDev: % 7.2f ms",
				timings.mean.evaluate() * 1000.0,
				timings.median.evaluate() * 1000.0,
				timings.stddev.evaluate() * 1000.0);
		System.out.println(StringUtils.indent(header + ":", StringUtils.strrep(' ', indent)));
		System.out.println(StringUtils.indent(msg, StringUtils.strrep(' ', indent + 2)));
	}
	
	private long getSize(byte[] bytes)
	{
		return bytes.length;
	}
	
	private long getSize(String str)
	{
		return getSize(str.getBytes(CHARSET));
	}
	
	private void printSize(int indent, String what, long bytes)
	{
		String msg = String.format("Size of %s: %d Bytes", what, bytes);
		System.out.println(StringUtils.indent(msg, StringUtils.strrep(' ', indent)));
	}
	
	private void printSize(
			int indent,
			String what,
			long bytes,
			String comparedTo,
			long comparedToBytes)
	{
		final double percent = (double) bytes / (double) comparedToBytes * 100.;
		String msg = String.format(
				"Size of %s: %d Bytes (% 3.1f %% of the size of %s)",
				what, bytes, percent, comparedTo);
		System.out.println(StringUtils.indent(msg, StringUtils.strrep(' ', indent)));
	}
	
	// =========================================================================
	
	private byte[] compress(String str, CompressionFormat compressionFormat) throws IOException, CompressionException
	{
		return serializer.compress(str.getBytes(CHARSET), compressionFormat);
	}
	
	// =========================================================================
	
	private TimingResults timeIt(RunMe runMe, int warmUp, int iterations) throws Exception
	{
		timeIt(runMe, warmUp);
		return timeIt(runMe, iterations);
	}
	
	private TimingResults timeIt(RunMe runMe, int iterations) throws Exception
	{
		double[] values = new double[iterations];
		for (int i = 0; i < iterations; ++i)
			values[i] = timeIt(runMe);
		TimingResults results = new TimingResults();
		results.mean.setData(values);
		results.median.setData(values);
		results.stddev.setData(values);
		return results;
	}
	
	private double timeIt(RunMe runMe) throws Exception
	{
		long startTime = System.nanoTime();
		runMe.run();
		long endTime = System.nanoTime();
		
		return ((double) (endTime - startTime)) / 1000000000.;
	}
	
	private static interface RunMe
	{
		public void run() throws Exception;
	}
	
	private static final class TimingResults
	{
		public final Mean mean = new Mean();
		
		public final Median median = new Median();
		
		public final StandardDeviation stddev = new StandardDeviation();
	}
}
