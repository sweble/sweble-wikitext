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

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

public class CompressorFactory
{
	public static enum CompressionFormat
	{
		BZIP2
		{
			@Override
			public OutputStream createCompressorOutputStream(OutputStream out) throws CompressorException
			{
				return (new CompressorStreamFactory()).createCompressorOutputStream(
						CompressorStreamFactory.BZIP2, out);

			}

			@Override
			public InputStream createCompressorInputStream(InputStream in) throws CompressorException
			{
				return (new CompressorStreamFactory()).createCompressorInputStream(
						CompressorStreamFactory.BZIP2, in);
			}
		},
		GZIP
		{
			@Override
			public OutputStream createCompressorOutputStream(OutputStream out) throws CompressorException
			{
				return (new CompressorStreamFactory()).createCompressorOutputStream(
						CompressorStreamFactory.GZIP, out);

			}

			@Override
			public InputStream createCompressorInputStream(InputStream in) throws CompressorException
			{
				return (new CompressorStreamFactory()).createCompressorInputStream(
						CompressorStreamFactory.GZIP, in);
			}
		},
		XZ
		{
			@Override
			public OutputStream createCompressorOutputStream(OutputStream out) throws CompressorException
			{
				return (new CompressorStreamFactory()).createCompressorOutputStream(
						CompressorStreamFactory.XZ, out);

			}

			@Override
			public InputStream createCompressorInputStream(InputStream in) throws CompressorException
			{
				return (new CompressorStreamFactory()).createCompressorInputStream(
						CompressorStreamFactory.XZ, in);
			}
		};

		public abstract OutputStream createCompressorOutputStream(
				OutputStream out) throws CompressorException;

		public abstract InputStream createCompressorInputStream(
				InputStream in) throws CompressorException;
	}

	public static OutputStream createCompressorOutputStream(
			CompressionFormat compressionFormat,
			OutputStream out) throws CompressorFactoryException
	{
		try
		{
			return compressionFormat.createCompressorOutputStream(out);
		}
		catch (Exception e)
		{
			throw new CompressorFactoryException(e);
		}
	}

	public static InputStream createCompressorInputStream(
			CompressionFormat compressionFormat,
			InputStream in) throws CompressorFactoryException
	{
		try
		{
			return compressionFormat.createCompressorInputStream(in);
		}
		catch (Exception e)
		{
			throw new CompressorFactoryException(e);
		}
	}
}
