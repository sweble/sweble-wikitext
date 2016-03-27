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
package org.sweble.wikitext.engine.utils;

import java.io.StringWriter;
import java.io.Writer;

import org.sweble.wikitext.engine.nodes.CompleteEngineVisitorNoReturn;
import org.sweble.wikitext.engine.nodes.EngNowiki;
import org.sweble.wikitext.engine.nodes.EngPage;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.nodes.EngSoftErrorNode;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtXmlElement;
import org.sweble.wikitext.parser.utils.WtPrettyPrinter;

public class EnginePrettyPrinter
		extends
			WtPrettyPrinter
		implements
			CompleteEngineVisitorNoReturn
{
	public void visit(EngNowiki n)
	{
		if (n.getContent().isEmpty())
		{
			p.print("<nowiki />");
		}
		else
		{
			p.print("<nowiki>");
			p.print(n.getContent());
			p.print("</nowiki>");
		}
	}

	@Override
	public void visit(EngProcessedPage n)
	{
		dispatch(n.getPage());
	}

	@Override
	public void visit(EngPage n)
	{
		iterate(n);
	}

	@Override
	public void visit(EngSoftErrorNode n)
	{
		visit((WtXmlElement) n);
	}

	// =========================================================================

	public static <T extends WtNode> String print(T node)
	{
		return print(new StringWriter(), node).toString();
	}

	public static <T extends WtNode> Writer print(Writer writer, T node)
	{
		new EnginePrettyPrinter(writer).go(node);
		return writer;
	}

	// =========================================================================

	public EnginePrettyPrinter(Writer writer)
	{
		super(writer);
	}
}
