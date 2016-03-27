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

import de.fau.cs.osr.ptk.common.ast.AstLocation;
import de.fau.cs.osr.utils.StringTools;

public class ParserShouldNotBeHereException
		extends
			RuntimeException
{
	private static final long serialVersionUID = 1L;

	private final AstLocation location;

	private final String context;

	private final int pos;

	// =========================================================================

	public ParserShouldNotBeHereException(
			AstLocation location,
			String context,
			int pos)
	{
		this.location = location;
		this.context = context;
		this.pos = pos;
	}

	public ParserShouldNotBeHereException(
			xtc.tree.Location location,
			String context,
			int pos)
	{
		this(new AstLocation(location), context, pos);
	}

	// =========================================================================

	public AstLocation getLocation()
	{
		return location;
	}

	public String getContext()
	{
		return context;
	}

	public int getPos()
	{
		return pos;
	}

	// =========================================================================

	@Override
	public String toString()
	{
		int p = pos;
		String before = "";
		if (context.length() < p)
			p = context.length();
		StringTools.escJava(context.substring(0, p));

		String after = "";
		if (context.length() > p)
			after = StringTools.escJava(context.substring(p));

		if (before.isEmpty() && after.isEmpty())
		{
			return String.format(
					"ParserShouldNotBeHereException: %s: No context available",
					location.toString());
		}
		else if (before.isEmpty())
		{
			return String.format(
					"ParserShouldNotBeHereException: %s: <!> \"%s\"",
					location.toString(),
					after);
		}
		else if (after.isEmpty())
		{
			return String.format(
					"ParserShouldNotBeHereException: %s: \"%s\" <!>",
					location.toString(),
					before);
		}
		else
		{
			return String.format(
					"ParserShouldNotBeHereException: %s: \"%s\" <!> \"%s\"",
					location.toString(),
					before,
					after);
		}
	}
}
