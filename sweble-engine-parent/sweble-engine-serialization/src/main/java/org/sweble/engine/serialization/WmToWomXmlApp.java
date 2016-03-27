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
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wom3.swcadapter.utils.WtWom3Toolbox;
import org.sweble.wom3.swcadapter.utils.WtWom3Toolbox.Artifacts;

public class WmToWomXmlApp
		extends
			SerializationLabToolbox
{
	public static void main(String[] args) throws TransformerException, LinkTargetException, IOException, EngineException
	{
		new WmToWomXmlApp().run(args);
	}

	private void run(String[] args) throws TransformerException, LinkTargetException, IOException, EngineException
	{
		if (args.length < 1)
		{
			System.err.println("Usage: java -cp JAR-with-dependencies.jar " +
					getClass().getName() +
					" WIKITEXT-FILE [pretty]");

			return;
		}

		boolean pretty = false;

		for (int i = 1; i < args.length; ++i)
		{
			if (args[i].compareToIgnoreCase("pretty") == 0)
				pretty = true;
			else
				System.err.println("Unknown command line option: '" + args[i] + "'");

		}

		WtWom3Toolbox wtWomToolbox = new WtWom3Toolbox();
		Artifacts af = wtWomToolbox.wmToWom(new File(args[0]), Charset.defaultCharset().name());

		Transformer transformer;
		if (pretty)
		{
			TransformerFactory tf = TransformerFactory.newInstance(
					"org.apache.xalan.processor.TransformerFactoryImpl",
					null);

			InputStream xslt = getClass().getResourceAsStream("/org/sweble/wom3/pretty-print.xslt");

			Transformer prettyXmlTransformer = tf.newTransformer(new StreamSource(xslt));

			transformer = prettyXmlTransformer;
		}
		else
		{
			TransformerFactory tf = TransformerFactory.newInstance();

			Transformer normalXmlTransformer = tf.newTransformer();

			transformer = normalXmlTransformer;
		}

		transformer.transform(
				new DOMSource(af.womDoc),
				new StreamResult(System.out));
	}
}
