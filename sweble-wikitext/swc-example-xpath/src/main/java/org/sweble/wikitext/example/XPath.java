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

package org.sweble.wikitext.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.WtRtDataPrinter;

import de.fau.cs.osr.ptk.common.jxpath.AstNodePointerFactory;

public class XPath
{
	/**
	 * IMPORTANT! Do not remove this, otherwise XPath queries won't work
	 * properly on ASTs.
	 */
	static
	{
		JXPathContextReferenceImpl.addNodePointerFactory(
				new AstNodePointerFactory());
	}

	static String query(EngProcessedPage cp, String query)
	{
		Iterator<?> results = null;
		try
		{
			JXPathContext context = JXPathContext.newContext(cp.getPage());

			results = context.iterate(query);
		}
		catch (Throwable t)
		{
			System.err.println("An error occurred when executing XPath query.");
			t.printStackTrace();
		}

		if (results != null)
		{
			if (!results.hasNext())
			{
				System.err.println("XPath result empty!");
			}
			else
			{
				List<Object> r = new ArrayList<Object>();
				while (results.hasNext())
					r.add(results.next());

				System.err.println("Found " + r.size() + " matching nodes.");

				StringBuilder b = new StringBuilder();

				int i = 1;
				for (Object o : r)
				{
					WtNode n = (WtNode) o;
					b.append('(');
					b.append(query);
					b.append(")[");
					b.append(i);
					b.append("]:\n\"\"\"");
					b.append(WtRtDataPrinter.print(n));
					b.append("\"\"\"\n\n");
					++i;
				}

				return b.toString();
			}
		}

		return "";
	}
}
