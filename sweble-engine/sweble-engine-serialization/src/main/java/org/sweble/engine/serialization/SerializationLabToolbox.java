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

import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.serialization.Wom3JsonTypeAdapterInterface;
import org.sweble.wom3.serialization.Wom3NodeCompactJsonTypeAdapter;
import org.sweble.wom3.serialization.Wom3NodeJsonTypeAdapter;
import org.w3c.dom.Node;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SerializationLabToolbox
{
	public static Wom3JsonTypeAdapterInterface createWom3JsonTypeAdapter(
			boolean compact)
	{
		return (compact ?
				(new Wom3NodeCompactJsonTypeAdapter()) :
				(new Wom3NodeJsonTypeAdapter()));
	}

	public static GsonBuilder registerWom3GsonAdapter(
			GsonBuilder builder,
			boolean compact)
	{
		Wom3JsonTypeAdapterInterface converter = createWom3JsonTypeAdapter(compact);
		builder.registerTypeHierarchyAdapter(Node.class, converter);
		builder.serializeNulls();
		return builder;
	}

	public static Gson createWom3Gson(boolean compact, boolean pretty)
	{
		GsonBuilder builder = registerWom3GsonAdapter(new GsonBuilder(), compact);
		if (pretty)
			builder.setPrettyPrinting();
		return builder.create();
	}

	public static String womToJson(
			Wom3Node what,
			boolean compact,
			boolean pretty)
	{
		return createWom3Gson(compact, pretty).toJson(what);
	}

	public static Wom3Node jsonToWom(
			String json,
			boolean compact,
			boolean pretty)
	{
		return createWom3Gson(compact, pretty).fromJson(json, Wom3Node.class);
	}
}
