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

package org.sweble.wikitext.dumpreader;

import java.io.IOException;
import java.io.InputStream;

public final class CountingInputStream
		extends
			InputStream
{
	private InputStream in;

	private long count;

	// =========================================================================

	public CountingInputStream(InputStream inputStream)
	{
		this.in = inputStream;
		this.count = 0;
	}

	// =========================================================================

	public long getCount()
	{
		return count;
	}

	// =========================================================================

	@Override
	public int read() throws IOException
	{
		int read = in.read();
		if (read != -1)
			++count;
		return read;
	}

	@Override
	public int read(byte[] b) throws IOException
	{
		int read = in.read(b);
		if (read != -1)
			count += read;
		return read;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		int read = in.read(b, off, len);
		if (read != -1)
			count += read;
		return read;
	}

	@Override
	public long skip(long n) throws IOException
	{
		long skipped = in.skip(n);
		if (skipped != -1)
			++count;
		return skipped;
	}

	@Override
	public int available() throws IOException
	{
		return in.available();
	}

	@Override
	public void close() throws IOException
	{
		in.close();
	}

	@Override
	public void mark(int readlimit)
	{
		in.mark(readlimit);
	}

	@Override
	public void reset() throws IOException
	{
		in.reset();
	}

	@Override
	public boolean markSupported()
	{
		return in.markSupported();
	}
}
