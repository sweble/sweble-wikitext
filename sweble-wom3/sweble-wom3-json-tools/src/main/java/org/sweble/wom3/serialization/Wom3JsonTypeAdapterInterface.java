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
package org.sweble.wom3.serialization;

import java.lang.reflect.Type;

import org.sweble.wom3.Wom3Document;
import org.w3c.dom.Node;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public interface Wom3JsonTypeAdapterInterface
		extends
			JsonSerializer<Node>,
			JsonDeserializer<Node>
{
	@Override
	public JsonElement serialize(
			Node src,
			Type typeOfSrc,
			JsonSerializationContext context);

	@Override
	public Node deserialize(
			JsonElement json,
			Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException;

	Wom3Document getDoc();

	void setDoc(Wom3Document doc);
}
