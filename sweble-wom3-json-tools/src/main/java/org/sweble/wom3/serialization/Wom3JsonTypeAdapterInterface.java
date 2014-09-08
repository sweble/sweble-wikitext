/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
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
