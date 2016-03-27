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

package org.sweble.wikitext.parser.utils;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.sweble.wikitext.parser.parser.RatsWikitextParser;

import de.fau.cs.osr.utils.StringTools;

public class ParserStats
{
	public static final class MemoizedProduction
	{
		public int called;

		public int failed;

		public int reused;

		public int parsed;

		@Override
		public String toString()
		{
			return String.format(
					"MemoizedProduction [called=%5d, reused=%5d, parsed=%5d, failed=%5d]",
					called,
					reused,
					parsed,
					failed);
		}
	}

	private final Map<String, MemoizedProduction> memoizedProductions =
			new HashMap<String, MemoizedProduction>();

	public Map<String, MemoizedProduction> getMemoizedProductions()
	{
		return memoizedProductions;
	}

	public void parsed(String prod)
	{
		getMemoizedProduction(prod).parsed++;
	}

	public void failed(String prod)
	{
		getMemoizedProduction(prod).failed++;
	}

	public void called(String prod)
	{
		getMemoizedProduction(prod).called++;
	}

	public void reused(String prod)
	{
		getMemoizedProduction(prod).reused++;
	}

	private MemoizedProduction getMemoizedProduction(String prod)
	{
		MemoizedProduction p = memoizedProductions.get(prod);
		if (p == null)
		{
			p = new MemoizedProduction();
			memoizedProductions.put(prod, p);
		}
		return p;
	}

	public void dump(PrintStream err)
	{
		for (Entry<String, MemoizedProduction> x : RatsWikitextParser.getStats().getMemoizedProductions().entrySet())
		{
			err.print(x.getKey() + ":");
			err.print(StringTools.strrep(' ', 20 - x.getKey().length()));
			err.println(x.getValue());
		}
	}
}
