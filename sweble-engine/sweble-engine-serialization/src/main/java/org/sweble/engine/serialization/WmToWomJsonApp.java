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

import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wom3.serialization.Wom3NodeCompactJsonTypeAdapter;
import org.sweble.wom3.serialization.Wom3NodeJsonTypeAdapter;
import org.sweble.wom3.swcadapter.utils.WtWom3Toolbox;
import org.sweble.wom3.swcadapter.utils.WtWom3Toolbox.Artifacts;
import org.w3c.dom.Node;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;

public class WmToWomJsonApp
		extends
			SerializationLabToolbox
{
	public static void main(String[] args) throws LinkTargetException, IOException, EngineException
	{
		new WmToWomJsonApp().run(args);
	}

	private void run(String[] args) throws LinkTargetException, IOException, EngineException
	{
		if (args.length < 1)
		{
			System.err.println("Usage: java -cp JAR-with-dependencies.jar " +
					getClass().getName() +
					" WIKITEXT-FILE [compact|pretty]");

			return;
		}

		boolean pretty = false;
		boolean compact = false;

		for (int i = 1; i < args.length; ++i)
		{
			if (args[i].compareToIgnoreCase("pretty") == 0)
				pretty = true;
			else if (args[i].compareToIgnoreCase("compact") == 0)
				compact = true;
			else
				System.err.println("Unknown command line option: '" + args[i] + "'");

		}

		WtWom3Toolbox wtWomToolbox = new WtWom3Toolbox();
		Artifacts af = wtWomToolbox.wmToWom(new File(args[0]), Charset.defaultCharset().name());

		JsonSerializer<Node> converter =
				compact ?
						new Wom3NodeCompactJsonTypeAdapter() :
						new Wom3NodeJsonTypeAdapter();

		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeHierarchyAdapter(Node.class, converter);
		builder.serializeNulls();
		if (pretty)
			builder.setPrettyPrinting();
		Gson gson = builder.create();

		gson.toJson(af.womDoc, System.out);
	}
}
